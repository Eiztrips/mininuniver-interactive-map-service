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

package org.mininuniver.interactivemap.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.OptimisticLockException;
import org.mininuniver.interactivemap.dto.FloorDTO;
import org.mininuniver.interactivemap.models.*;
import org.mininuniver.interactivemap.models.submodels.Point;
import org.mininuniver.interactivemap.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FloorService {

    private final FloorRepository floorRepository;
    private final RoomRepository roomRepository;
    private final EdgeRepository edgeRepository;
    private final StairsRepository stairsRepository;
    private final NodeRepository nodeRepository;

    @Autowired
    public FloorService(FloorRepository floorRepository, RoomRepository roomRepository,
                        EdgeRepository edgeRepository, StairsRepository stairsRepository, NodeRepository nodeRepository) {
        this.floorRepository = floorRepository;
        this.roomRepository = roomRepository;
        this.edgeRepository = edgeRepository;
        this.stairsRepository = stairsRepository;
        this.nodeRepository = nodeRepository;
    }

    public FloorDTO getFloorData(int id) {
        Floor floor = floorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Этаж не найден"));

        List<Room> rooms = roomRepository.findByFloorId(floor.getId());

        List<Edge> edges = edgeRepository.findByFloorId(floor.getId());

        List<Stairs> stairs = stairsRepository.findByFloorId(floor.getId());

        List<Node> nodes = nodeRepository.findByFloorId(floor.getId());

        return new FloorDTO(floor, rooms, edges, stairs, nodes);
    }

    @Transactional
    public FloorDTO saveOrUpdateFloorData(int id, FloorDTO floorDTO) {
        Floor floor = floorRepository.findById(id).orElseGet(Floor::new);
        floor.setFloorNumber(id);
        floor.setName(floorDTO.getFloor().getName());
        floor.setPoints(floorDTO.getFloor().getPoints());
        floor = floorRepository.save(floor); // сначала сохранили, потом чистим всё остальное

        // Удаляем старое только после сохранения (иначе падение)
        roomRepository.deleteAllByFloorId(floor.getId());
        nodeRepository.deleteAllByFloorId(floor.getId());
        edgeRepository.deleteAllByFloorId(floor.getId());
        stairsRepository.deleteAllByFloorId(floor.getId());

        Map<Integer, Integer> nodeIdMapping = new HashMap<>();

        for (Node node : floorDTO.getNodes()) {
            Integer oldId = node.getId();
            node.setId(null);
            node.setFloorId(floor.getId());
            node.setNodeNumber(oldId);
            Node saved = nodeRepository.save(node);
            nodeIdMapping.put(oldId, saved.getId());
        }

        for (Map.Entry<Integer, Integer> entry : nodeIdMapping.entrySet()) {
            Node node = nodeRepository.findById(entry.getValue()).orElseThrow();
            int[] oldNeighbors = floorDTO.getNodes().stream()
                    .filter(n -> n.getId().equals(entry.getKey()))
                    .findFirst()
                    .map(Node::getNeighbors)
                    .orElse(null);

            if (oldNeighbors != null) {
                int[] newNeighbors = Arrays.stream(oldNeighbors)
                        .map(n -> nodeIdMapping.getOrDefault(n, n))
                        .toArray();
                node.setNeighbors(newNeighbors);
                nodeRepository.save(node);
            }
        }

        for (Edge edge : floorDTO.getEdges()) {
            edge.setId(null);
            edge.setFloorId(floor.getId());
            int[] newNodes = Arrays.stream(edge.getNodes())
                    .map(n -> nodeIdMapping.getOrDefault(n, n))
                    .toArray();
            edge.setNodes(newNodes);
            edgeRepository.save(edge);
        }

        for (Room room : floorDTO.getRooms()) {
            room.setId(null);
            room.setFloorId(floor.getId());
            if (room.getNodeId() != null) {
                Integer oldNodeId = room.getNodeId();
                if (nodeIdMapping.containsKey(oldNodeId)) {
                    room.setNodeId(nodeIdMapping.get(oldNodeId));
                }
            }
            roomRepository.save(room);
        }

        for (Stairs stair : floorDTO.getStairs()) {
            stair.setId(null);
            stair.setFloorId(floor.getId());
            stairsRepository.save(stair);
        }

        return floorDTO;
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
