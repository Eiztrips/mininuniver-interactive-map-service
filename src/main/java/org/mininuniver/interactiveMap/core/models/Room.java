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

package org.mininuniver.interactiveMap.core.models;

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.mininuniver.interactiveMap.api.dto.models.room.RoomDTO;
import org.mininuniver.interactiveMap.api.dto.models.submodels.PointDTO;

import java.util.List;

@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name = "Rooms")
@Schema(name = "RoomEntity", description = "Модель комнаты (entity)")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "floor_id")
    private Floor floor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "node_id")
    private Node node;

    @Column(columnDefinition = "jsonb")
    @Type(JsonBinaryType.class)
    private List<PointDTO> points;

    public Room(RoomDTO room) {
        this.name = room.getName();
        if (room.getFloorId() != null) {
            this.floor = new Floor();
            this.floor.setId(room.getFloorId());
        } else {
            this.floor = null;
        }
        if (room.getNodeId() != null) {
            this.node = new Node();
            this.node.setId(room.getNodeId());
        } else {
            this.node = null;
        }
        this.points = room.getPoints();
    }
}