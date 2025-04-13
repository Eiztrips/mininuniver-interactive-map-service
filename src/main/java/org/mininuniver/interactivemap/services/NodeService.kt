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

package org.mininuniver.interactivemap.services

import org.mininuniver.interactivemap.models.Node
import org.mininuniver.interactivemap.repositories.FloorRepository
import org.mininuniver.interactivemap.repositories.NodeRepository
import org.springframework.stereotype.Service

@Service
class NodeService(
    private val nodeRepository: NodeRepository,
    private val floorRepository: FloorRepository
) {

    fun getAllNodes(): List<Node> {
        return nodeRepository.findAll()
    }

    fun getLastNodeId(number: Int): Node? {
        val floor = floorRepository.findByFloorNumber(number)
            .orElseThrow { RuntimeException("Этаж не найден") }
        val nodes = nodeRepository.findAllByFloorId(floor.id)
        return nodes.lastOrNull()
    }
}