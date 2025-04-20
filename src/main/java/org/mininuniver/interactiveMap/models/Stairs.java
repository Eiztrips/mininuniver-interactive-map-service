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

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import io.hypersistence.utils.hibernate.type.array.IntArrayType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.mininuniver.interactiveMap.dto.models.stairs.StairsDTO;
import org.mininuniver.interactiveMap.dto.models.submodels.PointDTO;

import java.util.List;

@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name = "Stairs")
public class Stairs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "floor_id")
    private Floor floor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "node_id")
    private Node node;

    @Column(columnDefinition = "jsonb")
    @Type(JsonBinaryType.class)
    private List<PointDTO> points;

    @Column(name = "floors", columnDefinition = "integer[]")
    @Type(IntArrayType.class)
    private int[] floors;

    public Stairs(StairsDTO stair) {
        this.id = stair.getId();
        if (stair.getFloorId() != null) {
            this.floor = new Floor();
            this.floor.setId(stair.getFloorId());
        } else this.floor = null;
        if (stair.getNodeId() != null) {
            this.node = new Node();
            this.node.setId(stair.getNodeId());
        } else this.node = null;
        this.points = stair.getPoints();
        this.floors = stair.getFloors();
    }
}
