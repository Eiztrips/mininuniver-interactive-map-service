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

package org.mininuniver.interactiveMap.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.OptimisticLockException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mininuniver.interactiveMap.api.dto.models.MapDTO;
import org.mininuniver.interactiveMap.core.models.Floor;
import org.mininuniver.interactiveMap.core.models.Node;
import org.mininuniver.interactiveMap.core.models.Room;
import org.mininuniver.interactiveMap.core.models.Stairs;
import org.mininuniver.interactiveMap.core.repositories.*;
import org.mininuniver.interactiveMap.api.dto.models.node.NodeDTO;
import org.mininuniver.interactiveMap.api.dto.models.room.RoomDTO;
import org.mininuniver.interactiveMap.api.dto.models.stairs.StairsDTO;
import org.mininuniver.interactiveMap.api.dto.models.floor.FloorShortDTO;
import org.mininuniver.interactiveMap.api.dto.models.floor.FloorDTO;
import org.mininuniver.interactiveMap.mapper.FloorMapper;
import org.mininuniver.interactiveMap.mapper.NodeMapper;
import org.mininuniver.interactiveMap.mapper.RoomMapper;
import org.mininuniver.interactiveMap.mapper.StairsMapper;
import org.mininuniver.interactiveMap.service.interfaces.FloorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Validated
public class FloorServiceImpl implements FloorService {

    private final FloorMapper floorMapper;
    private final RoomMapper roomMapper;
    private final StairsMapper stairsMapper;
    private final NodeMapper nodeMapper;

    private final FloorRepository floorRepository;
    private final RoomRepository roomRepository;
    private final StairsRepository stairsRepository;
    private final NodeRepository nodeRepository;

    public List<FloorShortDTO> getAllFloors() {
        List<Floor> floors = floorRepository.findAll();
        return floors.stream()
                .sorted(Comparator.comparing(Floor::getNumber))
                .map(floorMapper::toShortDto)
                .toList();
    }

    public MapDTO getMapData(int number) {
        Floor floorEntity = floorRepository.findByNumber(number)
                .orElseThrow(() -> new EntityNotFoundException("Этаж не найден"));
        FloorDTO floor = floorMapper.toDto(floorEntity);

        List<RoomDTO> rooms = roomRepository.findByFloorId(floor.getId())
                .stream()
                .map(roomMapper::toDto)
                .toList();

        List<StairsDTO> stairs = stairsRepository.findByFloorId(floor.getId())
                .stream()
                .map(stairsMapper::toDto)
                .toList();

        List<NodeDTO> nodes = nodeRepository.findByFloorId(floor.getId())
                .stream()
                .map(nodeMapper::toDto)
                .toList();

        return new MapDTO(floor, rooms, stairs, nodes);
    }

    @Transactional
    public MapDTO updateFloorData(int number, @Valid MapDTO mapDTO) {
        Floor floor = floorRepository.findByNumber(number).orElseGet(Floor::new);
        floor.setNumber(number);
        floor.setName(mapDTO.getFloor().getName());
        floor.setPoints(mapDTO.getFloor().getPoints());
        floor = floorRepository.save(floor);

        List<Node> existingNodes = nodeRepository.findByFloorId(floor.getId());
        List<Room> existingRooms = roomRepository.findByFloorId(floor.getId());
        List<Stairs> existingStairs = stairsRepository.findByFloorId(floor.getId());

        Map<Long, Node> existingNodesMap = existingNodes.stream()
                .collect(Collectors.toMap(Node::getId, n -> n));
        Map<Long, Room> existingRoomsMap = existingRooms.stream()
                .collect(Collectors.toMap(Room::getId, r -> r));
        Map<Long, Stairs> existingStairsMap = existingStairs.stream()
                .collect(Collectors.toMap(Stairs::getId, s -> s));

        Map<Long, Long> nodeIdMapping = new HashMap<>();

        List<Node> updatedNodes = new ArrayList<>();
        for (NodeDTO nodeDTO : mapDTO.getNodes()) {
            Node node;
            if (nodeDTO.getId() != null && existingNodesMap.containsKey(nodeDTO.getId())) {
                node = existingNodesMap.get(nodeDTO.getId());
                node.setPos(nodeDTO.getPos());
                existingNodesMap.remove(nodeDTO.getId());
            } else {
                node = new Node();
                node.setPos(nodeDTO.getPos());
                node.setFloor(floor);
            }

            node = nodeRepository.save(node);

            Long oldId = nodeDTO.getId() != null ? nodeDTO.getId() : -node.getId();
            nodeIdMapping.put(oldId, node.getId());
            updatedNodes.add(node);
        }

        for (Node node : existingNodesMap.values()) {
            nodeRepository.delete(node);
        }

        for (int i = 0; i < mapDTO.getNodes().size(); i++) {
            NodeDTO nodeDTO = mapDTO.getNodes().get(i);
            Node node = updatedNodes.get(i);

            if (nodeDTO.getNeighbors() != null) {
                Long[] newNeighbors = Arrays.stream(nodeDTO.getNeighbors())
                        .map(n -> nodeIdMapping.getOrDefault(n, n))
                        .toArray(Long[]::new);
                node.setNeighbors(newNeighbors);
                nodeRepository.save(node);
            }
        }

        Map<String, Room> roomsByName = existingRooms.stream()
                .collect(Collectors.toMap(Room::getName, r -> r, (r1, r2) -> r1));

        for (RoomDTO roomDTO : mapDTO.getRooms()) {
            Room room;
            if (roomDTO.getId() != null && existingRoomsMap.containsKey(roomDTO.getId())) {
                room = existingRoomsMap.get(roomDTO.getId());
                existingRoomsMap.remove(roomDTO.getId());
            } else if (roomDTO.getName() != null && roomsByName.containsKey(roomDTO.getName())) {
                room = roomsByName.get(roomDTO.getName());
                existingRoomsMap.remove(room.getId());
            } else {
                room = new Room();
            }

            room.setName(roomDTO.getName());
            room.setFloor(floor);
            room.setPoints(roomDTO.getPoints());

            if (roomDTO.getNodeId() != null) {
                Long mappedNodeId = nodeIdMapping.getOrDefault(roomDTO.getNodeId(), roomDTO.getNodeId());
                Node node = new Node();
                node.setId(mappedNodeId);
                room.setNode(node);
            }

            roomRepository.save(room);
        }

        for (Room room : existingRoomsMap.values()) {
            roomRepository.delete(room);
        }

        for (StairsDTO stairsDTO : mapDTO.getStairs()) {
            Stairs stairs;
            if (stairsDTO.getId() != null && existingStairsMap.containsKey(stairsDTO.getId())) {
                stairs = existingStairsMap.get(stairsDTO.getId());
                existingStairsMap.remove(stairsDTO.getId());
            } else {
                stairs = new Stairs();
            }

            stairs.setFloor(floor);
            stairs.setPoints(stairsDTO.getPoints());
            stairs.setFloors(stairsDTO.getFloors());

            if (stairsDTO.getNodeId() != null) {
                Long mappedNodeId = nodeIdMapping.getOrDefault(stairsDTO.getNodeId(), stairsDTO.getNodeId());
                Node node = new Node();
                node.setId(mappedNodeId);
                stairs.setNode(node);
            }

            stairsRepository.save(stairs);
        }

        for (Stairs stairs : existingStairsMap.values()) {
            stairsRepository.delete(stairs);
        }

        return getMapData(number);
    }

    @Transactional
    public MapDTO createFloor(int number, @Valid MapDTO mapDTO) {
        if (floorRepository.existsByNumber(number)) {
            throw new IllegalArgumentException("Floor with this number already exists");
        }

        Floor floor = new Floor();
        floor.setNumber(number);
        floor.setName(mapDTO.getFloor().getName());
        floor.setPoints(mapDTO.getFloor().getPoints());
        floor = floorRepository.save(floor);

        Map<Long, Long> nodeIdMapping = new HashMap<>();

        for (NodeDTO nodeDTO : mapDTO.getNodes()) {
            Node node = new Node();
            node.setPos(nodeDTO.getPos());
            node.setFloor(floor);
            node = nodeRepository.save(node);

            Long oldId = nodeDTO.getId() != null ? nodeDTO.getId() : -node.getId();
            nodeIdMapping.put(oldId, node.getId());
        }

        for (RoomDTO roomDTO : mapDTO.getRooms()) {
            Room room = new Room();
            room.setName(roomDTO.getName());
            room.setFloor(floor);
            room.setPoints(roomDTO.getPoints());

            if (roomDTO.getNodeId() != null) {
                Long mappedNodeId = nodeIdMapping.getOrDefault(roomDTO.getNodeId(), roomDTO.getNodeId());
                Node node = new Node();
                node.setId(mappedNodeId);
                room.setNode(node);
            }

            roomRepository.save(room);
        }

        for (StairsDTO stairsDTO : mapDTO.getStairs()) {
            Stairs stairs = new Stairs();
            stairs.setFloor(floor);
            stairs.setPoints(stairsDTO.getPoints());
            stairs.setFloors(stairsDTO.getFloors());

            if (stairsDTO.getNodeId() != null) {
                Long mappedNodeId = nodeIdMapping.getOrDefault(stairsDTO.getNodeId(), stairsDTO.getNodeId());
                Node node = new Node();
                node.setId(mappedNodeId);
                stairs.setNode(node);
            }

            stairsRepository.save(stairs);
        }

        return getMapData(number);
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

    @Transactional
    public void resetDatabase() {
        try {
            roomRepository.deleteAll();
            stairsRepository.deleteAll();
            nodeRepository.deleteAll();
            floorRepository.deleteAll();

            floorRepository.resetSequences();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при сбросе базы данных: " + e.getMessage());
        }
    }

}
