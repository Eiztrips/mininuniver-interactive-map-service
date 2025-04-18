/*
 * This file is part of mininuniver-interactive-map-service.
 *
 * Copyright (C) 2025 Eiztrips
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package org.mininuniver.interactiveMap.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.mininuniver.interactiveMap.dto.models.*;
import org.mininuniver.interactiveMap.dto.models.edge.EdgeDTO;
import org.mininuniver.interactiveMap.dto.models.node.NodeDTO;
import org.mininuniver.interactiveMap.dto.models.room.RoomDTO;
import org.mininuniver.interactiveMap.dto.models.stairs.StairsDTO;
import org.mininuniver.interactiveMap.dto.models.floor.FloorShortDTO;
import org.mininuniver.interactiveMap.dto.models.floor.FloorDTO;
import org.mininuniver.interactiveMap.models.*;
import org.mininuniver.interactiveMap.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FloorService {

    private final FloorRepository floorRepository;
    private final RoomRepository roomRepository;
    private final EdgeRepository edgeRepository;
    private final StairsRepository stairsRepository;
    private final NodeRepository nodeRepository;

    public List<FloorShortDTO> getAllFloors() {
        List<Floor> floors = floorRepository.findAll();
        return floors.stream()
                .map(floor -> new FloorShortDTO(floor.getId(), floor.getFloorNumber(), floor.getName()))
                .toList();
    }

    public MapDTO getMapData(int number) {
        FloorDTO floor = new FloorDTO(floorRepository.findByFloorNumber(number)
                .orElseThrow(() -> new RuntimeException("Этаж не найден")));

        List<RoomDTO> rooms = roomRepository.findByFloorId(floor.getId())
                .stream()
                .map(RoomDTO::new)
                .toList();

        List<EdgeDTO> edges = edgeRepository.findByFloorId(floor.getId())
                .stream()
                .map(EdgeDTO::new)
                .toList();

        List<StairsDTO> stairs = stairsRepository.findByFloorId(floor.getId())
                .stream()
                .map(StairsDTO::new)
                .toList();

        List<NodeDTO> nodes = nodeRepository.findByFloorId(floor.getId())
                .stream()
                .map(NodeDTO::new)
                .toList();

        return new MapDTO(floor, rooms, edges, stairs, nodes);
    }

    @Transactional
    public MapDTO updateFloorData(int id, MapDTO mapDTO) {
        Floor floor = floorRepository.findById(id).orElseGet(Floor::new);
        floor.setFloorNumber(id);
        floor.setName(mapDTO.getFloor().getName());
        floor.setPoints(mapDTO.getFloor().getPoints());
        floor = floorRepository.save(floor);

        roomRepository.deleteAllByFloorId(floor.getId());
        nodeRepository.deleteAllByFloorId(floor.getId());
        edgeRepository.deleteAllByFloorId(floor.getId());
        stairsRepository.deleteAllByFloorId(floor.getId());

        Map<Integer, Integer> nodeIdMapping = new HashMap<>();

        for (NodeDTO node : mapDTO.getNodes()) {
            Integer oldId = node.getId();
            node.setId(null);
            node.setFloorId(floor.getId());
            node.setNodeNumber(oldId);
            Node saved = nodeRepository.save(new Node(node));
            nodeIdMapping.put(oldId, saved.getId());
        }

        for (Map.Entry<Integer, Integer> entry : nodeIdMapping.entrySet()) {
            NodeDTO node = new NodeDTO(nodeRepository.findById(entry.getValue()).orElseThrow());

            int[] oldNeighbors = mapDTO.getNodes().stream()
                    .filter(n -> n.getId().equals(entry.getKey()))
                    .findFirst()
                    .map(NodeDTO::getNeighbors)
                    .orElse(null);

            if (oldNeighbors != null) {
                int[] newNeighbors = Arrays.stream(oldNeighbors)
                        .map(n -> nodeIdMapping.getOrDefault(n, n))
                        .toArray();
                node.setNeighbors(newNeighbors);
                nodeRepository.save(new Node(node));
            }
        }

        for (EdgeDTO edge : mapDTO.getEdges()) {
            edge.setId(null);
            edge.setFloorId(floor.getId());
            int[] newNodes = Arrays.stream(edge.getNodes())
                    .map(n -> nodeIdMapping.getOrDefault(n, n))
                    .toArray();
            edge.setNodes(newNodes);
            edgeRepository.save(new Edge(edge));
        }

        for (RoomDTO room : mapDTO.getRooms()) {
            room.setId(null);
            room.setFloorId(floor.getId());
            if (room.getNodeId() != null) {
                Integer oldNodeId = room.getNodeId();
                if (nodeIdMapping.containsKey(oldNodeId)) {
                    room.setNodeId(nodeIdMapping.get(oldNodeId));
                }
            }
            roomRepository.save(new Room(room));
        }

        for (StairsDTO stair : mapDTO.getStairs()) {
            stair.setId(null);
            stair.setFloorId(floor.getId());
            stairsRepository.save(new Stairs(stair));
        }

        return mapDTO;
    }

    @Transactional
    public void deleteFloor(int number) {
        Floor floor = floorRepository.findByFloorNumber(number)
                .orElseThrow(() -> new EntityNotFoundException("Этаж не найден"));

        try {
            roomRepository.deleteAllByFloorId(floor.getId());
            edgeRepository.deleteAllByFloorId(floor.getId());
            stairsRepository.deleteAllByFloorId(floor.getId());
            nodeRepository.deleteAllByFloorId(floor.getId());
            floorRepository.delete(floor);
        } catch (OptimisticLockException e) {
            throw new RuntimeException("Ошибка при удалении этажа: " + e.getMessage());
        }
    }

}
