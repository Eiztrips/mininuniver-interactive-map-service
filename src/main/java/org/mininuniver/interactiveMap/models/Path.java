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

package org.mininuniver.interactiveMap.models;

import io.hypersistence.utils.hibernate.type.array.IntArrayType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.mininuniver.interactiveMap.dto.models.path.PathDTO;

import java.util.List;

@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name = "Paths")
@Schema(name = "PathEntity", description = "Модель пути (entity)")
public class Path {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "floor_id")
    private Floor floor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "first_room_id")
    private Room firstRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "second_room_id")
    private Room secondRoom;

    @Column(name = "nodes", columnDefinition = "integer[]")
    @Type(IntArrayType.class)
    private int[] nodesInPath;

    public Path(PathDTO path) {
        if (path.getFloorId() != null) {
            this.floor = new Floor();
            this.floor.setId(path.getFloorId());
        }

        if (path.getFirstRoomId() != null) {
            this.firstRoom = new Room();
            this.firstRoom.setId(path.getFirstRoomId());
        }

        if (path.getSecondRoomId() != null) {
            this.secondRoom = new Room();
            this.secondRoom.setId(path.getSecondRoomId());
        }

        this.nodesInPath = path.getNodesInPath();
    }
}