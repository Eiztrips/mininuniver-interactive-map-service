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

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.mininuniver.interactiveMap.dto.models.edge.EdgeDTO;


@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name = "Edges")
public class Edge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nodes", columnDefinition = "integer[]", nullable = false)
    private int[] nodes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "floor_id")
    private Floor floor;

    @Column(name = "distance", nullable = false)
    private float distance;

    public Edge(EdgeDTO edgeDTO) {
        this.id = edgeDTO.getId();
        if (edgeDTO.getFloorId() != null) {
            this.floor = new Floor();
            this.floor.setId(edgeDTO.getFloorId());
        }
        this.distance = edgeDTO.getDistance();
        this.nodes = edgeDTO.getNodes();
    }

}
