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

package org.mininuniver.interactiveMap.api.dto.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.mininuniver.interactiveMap.api.dto.models.floor.FloorDTO;
import org.mininuniver.interactiveMap.api.dto.models.node.NodeDTO;
import org.mininuniver.interactiveMap.api.dto.models.path.PathDTO;
import org.mininuniver.interactiveMap.api.dto.models.room.RoomDTO;
import org.mininuniver.interactiveMap.api.dto.models.stairs.StairsDTO;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class MapDTO {
    private final FloorDTO floor;
    private final List<RoomDTO> rooms;
    private final List<StairsDTO> stairs;
    private final List<NodeDTO> nodes;
    private final List<PathDTO> paths;
}
