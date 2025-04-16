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

package org.mininuniver.interactiveMap.controllers;

import lombok.RequiredArgsConstructor;
import org.mininuniver.interactiveMap.dto.FloorDTO;
import org.mininuniver.interactiveMap.models.Node;
import org.mininuniver.interactiveMap.models.Room; // оптимизировать импорты
import org.mininuniver.interactiveMap.services.FloorService;
import org.mininuniver.interactiveMap.services.NodeService;
import org.mininuniver.interactiveMap.services.RoomService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/map")
@RequiredArgsConstructor
public class MapController {

    private final NodeService nodeService;
    private final RoomService roomService;
    private final FloorService floorService;

    @GetMapping("/floors/{number}")
    public FloorDTO getFloorByNumber(@PathVariable int number) {
        return floorService.getFloorData(number);
    }

    @GetMapping("/rooms/{name}")
    public Room getRoomByName(@PathVariable String name) {
        return roomService.getRoomByName(name);
    }

    @GetMapping("/rooms")
    public List<Room> getAllRooms() {
        return roomService.getAllRooms();
    }

    @GetMapping("/nodes")
    public List<Node> getAllNodes() {
        return  nodeService.getAllNodes();
    }

}
