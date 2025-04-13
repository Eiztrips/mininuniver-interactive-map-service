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

package org.mininuniver.interactivemap.dto

import org.mininuniver.interactivemap.models.Node

data class NodeDTO(
    var id: Int? = null,
    var nodeNumber: Int? = null,
    var floorId: Int? = null,
    var pos: Map<String, Any>? = null,
    var neighbors: IntArray? = null
) {
    constructor(node: Node) : this(
        id = node.id,
        nodeNumber = node.nodeNumber,
        floorId = node.floorId,
        pos = node.pos,
        neighbors = node.neighbors
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NodeDTO

        if (id != other.id) return false
        if (nodeNumber != other.nodeNumber) return false
        if (floorId != other.floorId) return false
        if (pos != other.pos) return false
        if (neighbors != null) {
            if (other.neighbors == null) return false
            if (!neighbors.contentEquals(other.neighbors)) return false
        } else if (other.neighbors != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id ?: 0
        result = 31 * result + (nodeNumber ?: 0)
        result = 31 * result + (floorId ?: 0)
        result = 31 * result + (pos?.hashCode() ?: 0)
        result = 31 * result + (neighbors?.contentHashCode() ?: 0)
        return result
    }
}