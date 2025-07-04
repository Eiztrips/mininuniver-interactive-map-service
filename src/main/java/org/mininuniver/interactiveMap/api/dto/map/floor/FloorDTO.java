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

package org.mininuniver.interactiveMap.api.dto.map.floor;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.mininuniver.interactiveMap.api.dto.map.submodels.PointDTO;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class FloorDTO {
    private Long id;

    @NotNull(message = "Номер этажа обязателен")
    private Integer number;

    @NotNull(message = "Имя этажа не может быть пустым")
    private String name;

    @NotNull(message = "Контур этажа обязателен")
    @Size(min = 3, message = "Контур этажа должен содержать минимум 3 точки")
    private List<PointDTO> points;
}