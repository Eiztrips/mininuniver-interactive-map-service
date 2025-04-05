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

import org.mininuniver.interactivemap.dto.FloorDataDTO;
import org.mininuniver.interactivemap.models.Node;
import org.mininuniver.interactivemap.models.Room;
import org.mininuniver.interactivemap.services.EdgeService;
import org.mininuniver.interactivemap.services.MapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/map")
public class MapController {

    @Autowired
    private MapService mapService;
    @Autowired
    private EdgeService edgeService;

    // FLOOR

    @GetMapping("/floors/{id}")
    public FloorDataDTO getFloorById(@PathVariable int id) {
        return mapService.getFloorData(id);
    }

    // ROOM

    @GetMapping("/rooms/{name}")
    public Room getRoomByName(@PathVariable String name) {
        return mapService.getRoomByName(name);
    }

    @GetMapping("/rooms")
    public List<Room> getAllRooms() {
        return mapService.getAllRooms();
    }

    // NODE

    @GetMapping("/nodes")
    public List<Node> getAllNodes() {
        return  mapService.getAllNodes();
    }

    // ADMIN

        @GetMapping("/generate/edges")
    public void generateEdges() {
        edgeService.generateEdges();
    }
}
