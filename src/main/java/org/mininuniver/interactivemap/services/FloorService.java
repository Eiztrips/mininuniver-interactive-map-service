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

        List<Room> rooms = roomRepository.findByFloor(floor);

        List<Edge> edges = edgeRepository.findByFloor(floor);

        List<Stairs> stairs = stairsRepository.findByFloor(floor);

        List<Node> nodes = nodeRepository.findByFloor(floor);

        return new FloorDTO(floor, rooms, edges, stairs, nodes);
    }

    @Transactional
    public FloorDTO saveOrUpdateFloorData(int id, FloorDTO floorDTO) {
        Floor floor = floorRepository.findById(id).orElseGet(() -> new Floor());
        floor.setFloorNumber(id);
        floor.setName(floorDTO.getFloor().getName());
        floor.setPoints(floorDTO.getFloor().getPoints());
        floorRepository.save(floor);

        nodeRepository.deleteAllByFloor(floor);
        edgeRepository.deleteAllByFloor(floor);
        roomRepository.deleteAllByFloor(floor);
        stairsRepository.deleteAllByFloor(floor);

        Map<Integer, Integer> nodeIdMapping = new HashMap<>();

        for (Node node : floorDTO.getNodes()) {
            Integer oldId = node.getId();
            node.setNodeNumber(oldId);
            node.setId(null);
            node.setFloor(floor);
            Node savedNode = nodeRepository.save(node);
            nodeIdMapping.put(oldId, savedNode.getId());
        }

        for (Node node : floorDTO.getNodes()) {
            Node savedNode = nodeRepository.findById(nodeIdMapping.get(node.getNodeNumber())).orElseThrow();
            int[] oldNeighbors = savedNode.getNeighbors();
            if (oldNeighbors != null) {
                int[] newNeighbors = new int[oldNeighbors.length];
                for (int i = 0; i < oldNeighbors.length; i++) {
                    newNeighbors[i] = nodeIdMapping.getOrDefault(oldNeighbors[i], oldNeighbors[i]);
                }
                savedNode.setNeighbors(newNeighbors);
                nodeRepository.save(savedNode);
            }
        }

        for (Edge edge : floorDTO.getEdges()) {
            edge.setId(null);
            edge.setFloor(floor);
            int[] oldNodes = edge.getNodes();
            int[] newNodes = new int[oldNodes.length];
            for (int i = 0; i < oldNodes.length; i++) {
                newNodes[i] = nodeIdMapping.getOrDefault(oldNodes[i], oldNodes[i]);
            }
            edge.setNodes(newNodes);
            edgeRepository.save(edge);
        }

        for (Room room : floorDTO.getRooms()) {
            room.setId(null);
            room.setFloor(floor);
            if (room.getNodeId() != null) {
                room.setNodeId(nodeIdMapping.getOrDefault(room.getNodeId(), room.getNodeId()));
            }
            roomRepository.save(room);
        }

        for (Stairs stair : floorDTO.getStairs()) {
            stair.setId(null);
            stair.setFloor(floor);
            stairsRepository.save(stair);
        }

        return floorDTO;
    }

    @Transactional
    public void deleteFloor(int number) {
        Floor floor = floorRepository.findByFloorNumber(number)
                .orElseThrow(() -> new EntityNotFoundException("Этаж не найден"));

        try {
            roomRepository.deleteAllByFloor(floor);
            edgeRepository.deleteAllByFloor(floor);
            stairsRepository.deleteAllByFloor(floor);
            nodeRepository.deleteAllByFloor(floor);
            floorRepository.delete(floor);
        } catch (OptimisticLockException e) {
            throw new RuntimeException("Ошибка при удалении этажа: " + e.getMessage());
        }
    }

}
