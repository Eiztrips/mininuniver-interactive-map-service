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

import org.mininuniver.interactiveMap.dto.FloorDataDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.mininuniver.interactiveMap.services.FloorService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private FloorService floorService;

    @DeleteMapping("/floors/{number}")
    public ResponseEntity<Void> deleteFloor(@PathVariable int number) {
        floorService.deleteFloor(number);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/floors/{number}")
    public FloorDataDTO updateFloorData(@PathVariable int number, @RequestBody FloorDataDTO floorDataDTO) {
        return floorService.updateFloorData(number, floorDataDTO);
    }

}
