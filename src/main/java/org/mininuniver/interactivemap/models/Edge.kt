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

package org.mininuniver.interactivemap.models

import jakarta.persistence.*

@Entity
@Table(name = "Edges")
class Edge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null

    @Column(name = "nodes", columnDefinition = "integer[]", nullable = false)
    var nodes: IntArray = intArrayOf()

    var floorId: Int? = null

    @Column(name = "distance", nullable = false)
    var distance: Float = 0f

    constructor()

    constructor(nodes: IntArray, floorId: Int?, distance: Float) {
        this.nodes = nodes
        this.floorId = floorId
        this.distance = distance
    }
}