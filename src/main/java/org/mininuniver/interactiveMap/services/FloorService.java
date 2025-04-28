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
import org.mininuniver.interactiveMap.dto.models.node.NodeDTO;
import org.mininuniver.interactiveMap.dto.models.path.PathDTO;
import org.mininuniver.interactiveMap.dto.models.room.RoomDTO;
import org.mininuniver.interactiveMap.dto.models.stairs.StairsDTO;
import org.mininuniver.interactiveMap.dto.models.floor.FloorShortDTO;
import org.mininuniver.interactiveMap.dto.models.floor.FloorDTO;
import org.mininuniver.interactiveMap.models.*;
import org.mininuniver.interactiveMap.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.*;

@Service
@RequiredArgsConstructor
public class FloorService {

    private final FloorRepository floorRepository;
    private final RoomRepository roomRepository;
    private final StairsRepository stairsRepository;
    private final NodeRepository nodeRepository;
    private final PathRepository pathRepository;

    public List<FloorShortDTO> getAllFloors() {
        List<Floor> floors = floorRepository.findAll();
        return floors.stream()
                .map(FloorShortDTO::new)
                .toList();
    }

    public MapDTO getMapData(int number) {
        FloorDTO floor = new FloorDTO(floorRepository.findByNumber(number)
                .orElseThrow(() -> new EntityNotFoundException("Этаж не найден")));

        List<RoomDTO> rooms = roomRepository.findByFloorId(floor.getId())
                .stream()
                .map(RoomDTO::new)
                .toList();

        List<StairsDTO> stairs = stairsRepository.findByFloorId(floor.getId())
                .stream()
                .map(StairsDTO::new)
                .toList();

        List<PathDTO> paths = pathRepository.findByFloorId(floor.getId())
                .stream()
                .map(PathDTO::new)
                .toList();

        List<NodeDTO> nodes = nodeRepository.findByFloorId(floor.getId())
                .stream()
                .map(NodeDTO::new)
                .toList();

        return new MapDTO(floor, rooms, stairs, nodes, paths);
    }

    @Transactional
    public MapDTO updateFloorData(Integer number, MapDTO mapDTO) {
        Floor floor = floorRepository.findByNumber(number).orElseGet(Floor::new);
        floor.setNumber(number);
        floor.setName(mapDTO.getFloor().getName());
        floor.setPoints(mapDTO.getFloor().getPoints());
        floor = floorRepository.save(floor);

        roomRepository.deleteAllByFloorId(floor.getId());
        nodeRepository.deleteAllByFloorId(floor.getId());
        stairsRepository.deleteAllByFloorId(floor.getId());

        Map<Long, Long> nodeIdMapping = new HashMap<>();

        for (NodeDTO node : mapDTO.getNodes()) {
            Long oldId = node.getId();
            node.setId(null);
            node.setFloorId(floor.getId());
            Node saved = nodeRepository.save(new Node(node));
            nodeIdMapping.put(oldId, saved.getId());
        }

        for (Map.Entry<Long, Long> entry : nodeIdMapping.entrySet()) {
            NodeDTO node = new NodeDTO(nodeRepository.findById(entry.getValue()).orElseThrow());

            Long[] oldNeighbors = node.getNeighbors();

            if (oldNeighbors != null) {
                Long[] newNeighbors = Arrays.stream(oldNeighbors)
                        .map(n -> nodeIdMapping.getOrDefault(n, n))
                        .toArray(Long[]::new);
                node.setNeighbors(newNeighbors);
                nodeRepository.save(new Node(node));
            }
        }

        for (RoomDTO room : mapDTO.getRooms()) {
            room.setId(null);
            room.setFloorId(floor.getId());
            if (room.getNodeId() != null) {
                Long oldNodeId = room.getNodeId();
                if (nodeIdMapping.containsKey(oldNodeId)) {
                    room.setNodeId(nodeIdMapping.get(oldNodeId));
                }
            }
            roomRepository.save(new Room(room));
        }

        for (StairsDTO stair : mapDTO.getStairs()) {
            stair.setFloorId(floor.getId());
            stair.setId(null);
            stair.setNodeId(nodeIdMapping.get(stair.getNodeId()));
            stairsRepository.save(new Stairs(stair));
        }

        return mapDTO;
    }

    @Transactional
    public void deleteFloor(int number) {
        Floor floor = floorRepository.findByNumber(number)
                .orElseThrow(() -> new EntityNotFoundException("Этаж не найден"));

        try {
            roomRepository.deleteAllByFloorId(floor.getId());
            stairsRepository.deleteAllByFloorId(floor.getId());
            nodeRepository.deleteAllByFloorId(floor.getId());
            floorRepository.delete(floor);
        } catch (OptimisticLockException e) {
            throw new RuntimeException("Ошибка при удалении этажа: " + e.getMessage());
        }
    }

}
