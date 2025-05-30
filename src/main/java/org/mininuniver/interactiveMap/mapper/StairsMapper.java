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

package org.mininuniver.interactiveMap.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.mininuniver.interactiveMap.api.dto.models.stairs.StairsDTO;
import org.mininuniver.interactiveMap.core.models.Stairs;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StairsMapper {

    StairsMapper INSTANCE = Mappers.getMapper(StairsMapper.class);

    @Mapping(source = "floor.id", target = "floorId")
    @Mapping(source = "node.id", target = "nodeId")
    StairsDTO toDto(Stairs entity);

    @Mapping(target = "floor.id", source = "floorId")
    @Mapping(target = "node.id", source = "nodeId")
    Stairs toEntity(StairsDTO dto);

    List<StairsDTO> toDtoList(List<Stairs> entities);
    List<Stairs> toEntityList(List<StairsDTO> dtos);
}