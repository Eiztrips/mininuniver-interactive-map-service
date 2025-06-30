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

package org.mininuniver.interactiveMap.service.interfaces;

import jakarta.validation.Valid;
import org.mininuniver.interactiveMap.api.dto.models.MapDTO;
import org.mininuniver.interactiveMap.api.dto.models.floor.FloorShortDTO;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public interface FloorService {
    MapDTO getMapData(int number);
    MapDTO updateFloorData(int number, @Valid MapDTO mapDTO);
    MapDTO createFloor(int number, @Valid MapDTO mapDTO);
    List<FloorShortDTO> getAllFloors();
    void deleteFloor(int number);
}