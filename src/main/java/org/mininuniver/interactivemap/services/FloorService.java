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
import org.mininuniver.interactivemap.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public FloorDTO createFloorData(int id, FloorDTO floorDTO) {
        // Проверка соответствия ID
        if (floorDTO.getFloor().getId() != id) {
            throw new EntityNotFoundException("ID в URL не совпадает с ID в теле запроса");
        }

        // Получаем или создаем объект этажа
        Floor floorEntity;

        if (id > 0 && floorRepository.existsById(id)) {
            // Если этаж существует, удаляем все связанные данные
            floorEntity = floorRepository.getReferenceById(id);

            // Удаляем существующие связанные сущности
            nodeRepository.deleteAll(nodeRepository.findByFloor(floorEntity));
            roomRepository.deleteAll(roomRepository.findByFloor(floorEntity));
            edgeRepository.deleteAll(edgeRepository.findByFloor(floorEntity));
            stairsRepository.deleteAll(stairsRepository.findByFloor(floorEntity));

            // Обновляем свойства этажа
            floorEntity.setName(floorDTO.getFloor().getName());
            floorEntity.setPoints(floorDTO.getFloor().getPoints());
        } else {
            // Создаем новый этаж
            floorEntity = new Floor();
            floorEntity.setId(id > 0 ? id : null); // Устанавливаем ID только если он положительный
            floorEntity.setName(floorDTO.getFloor().getName());
            floorEntity.setPoints(floorDTO.getFloor().getPoints());
        }

        // Сохраняем этаж
        final Floor savedFloor = floorRepository.saveAndFlush(floorEntity);

        // Привязываем все сущности к сохраненному этажу
        floorDTO.getNodes().forEach(node -> node.setFloor(savedFloor));
        floorDTO.getRooms().forEach(room -> room.setFloor(savedFloor));
        floorDTO.getEdges().forEach(edge -> edge.setFloor(savedFloor));
        floorDTO.getStairs().forEach(stairs -> stairs.setFloor(savedFloor));

        // Сохраняем связанные сущности
        nodeRepository.saveAll(floorDTO.getNodes());
        roomRepository.saveAll(floorDTO.getRooms());
        edgeRepository.saveAll(floorDTO.getEdges());
        stairsRepository.saveAll(floorDTO.getStairs());

        return floorDTO;
    }

}
