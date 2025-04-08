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

import org.mininuniver.interactivemap.dto.FloorDataDTO;
import org.mininuniver.interactivemap.models.*;
import org.mininuniver.interactivemap.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FloorService {

    private final FloorRepository floorRepository;
    private final RoomRepository roomRepository;
    private final EdgeRepository edgeRepository;
    private final StairsRepository stairsRepository;
    private final NodeRepository nodeRepository;

    @Autowired
    public FloorService (FloorRepository floorRepository, RoomRepository roomRepository,
                       EdgeRepository edgeRepository, StairsRepository stairsRepository, NodeRepository nodeRepository) {
        this.floorRepository = floorRepository;
        this.roomRepository = roomRepository;
        this.edgeRepository = edgeRepository;
        this.stairsRepository = stairsRepository;
        this.nodeRepository = nodeRepository;
    }

    public FloorDataDTO getFloorData(int id) {
        Floor floor = floorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Этаж не найден"));

        List<Room> rooms = roomRepository.findByFloorId(id);

        List<Edge> edges = edgeRepository.findByFloorId(id);

        List<Stairs> stairs = stairsRepository.findByFloorId(id);

        List<Node> nodes = nodeRepository.findByFloorId(id);

        return new FloorDataDTO(floor, rooms, edges, stairs, nodes);
    }

    public FloorDataDTO updateFloorData(int id, FloorDataDTO floorDataDTO) {
        // Проверяем, существует ли этаж с таким id
        if (floorDataDTO.getFloor().getId() != id) {
            throw new IllegalArgumentException("ID этажа в пути и в теле запроса не совпадают");
        }

        // Сохраняем данные в базу
        floorRepository.save(floorDataDTO.getFloor());
        roomRepository.saveAll(floorDataDTO.getRooms());
        edgeRepository.saveAll(floorDataDTO.getEdges());
        stairsRepository.saveAll(floorDataDTO.getStairs());
        nodeRepository.saveAll(floorDataDTO.getNodes());

        // Возвращаем обновленные данные
        return getFloorData(id);
    }
}
