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

package org.mininuniver.interactivemap.controllers;

import org.mininuniver.interactivemap.dto.FloorDTO;
import org.mininuniver.interactivemap.models.Node;
import org.mininuniver.interactivemap.models.Room;
import org.mininuniver.interactivemap.services.FloorService;
import org.mininuniver.interactivemap.services.NodeService;
import org.mininuniver.interactivemap.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/map")
public class MapController {

    @Autowired
    private NodeService nodeService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private FloorService floorService;

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
