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

package org.mininuniver.interactiveMap.api.dto.models.node;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.mininuniver.interactiveMap.core.models.Node;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class NodeDTO {
    private Long id;
    private Long floorId;
    private Map<String, Object> pos;
    private Long[] neighbors;
}