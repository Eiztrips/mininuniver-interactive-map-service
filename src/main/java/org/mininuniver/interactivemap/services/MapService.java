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

import jakarta.annotation.PostConstruct;
import org.mininuniver.interactivemap.dto.FloorDataDTO;
import org.mininuniver.interactivemap.models.*;
import org.mininuniver.interactivemap.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MapService {

    private final FloorRepository floorRepository;
    private final RoomRepository roomRepository;
    private final EdgeRepository edgeRepository;
    private final StairsRepository stairsRepository;
    private final NodeRepository nodeRepository;

    @Autowired
    public MapService(FloorRepository floorRepository, RoomRepository roomRepository,
                      EdgeRepository edgeRepository, StairsRepository stairsRepository, NodeRepository nodeRepository) {
        this.floorRepository = floorRepository;
        this.roomRepository = roomRepository;
        this.edgeRepository = edgeRepository;
        this.stairsRepository = stairsRepository;
        this.nodeRepository = nodeRepository;
    }

    // FLOOR

    public FloorDataDTO getFloorData(int id) {
        Floor floor = floorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Этаж не найден"));

        List<Room> rooms = roomRepository.findByFloorId(id);

        List<Edge> edges = edgeRepository.findByFloorId(id);

        List<Stairs> stairs = stairsRepository.findByFloorId(id);

        List<Node> nodes = nodeRepository.findByFloorId(id);

        return new FloorDataDTO(floor, rooms, edges, stairs, nodes);
    }

    // ROOM

    public Room getRoomByName(String name) {
        return roomRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException(String.format("Помещение %s не найдено", name)));
    }

    public Room getRoomById(int id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("Помещение с ID:%d не найдено", id)));
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    // NODE

    public List<Node> getAllNodes() {
        return nodeRepository.findAll();
    }

}