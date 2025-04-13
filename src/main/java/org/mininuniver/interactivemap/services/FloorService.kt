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

import jakarta.persistence.EntityNotFoundException
import jakarta.persistence.OptimisticLockException
import org.mininuniver.interactivemap.dto.FloorDTO
import org.mininuniver.interactivemap.models.Floor
import org.mininuniver.interactivemap.repositories.EdgeRepository
import org.mininuniver.interactivemap.repositories.FloorRepository
import org.mininuniver.interactivemap.repositories.NodeRepository
import org.mininuniver.interactivemap.repositories.RoomRepository
import org.mininuniver.interactivemap.repositories.StairsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FloorService @Autowired constructor(
    private val floorRepository: FloorRepository,
    private val roomRepository: RoomRepository,
    private val edgeRepository: EdgeRepository,
    private val stairsRepository: StairsRepository,
    private val nodeRepository: NodeRepository
) {

    fun getFloorData(id: Int): FloorDTO {
        val floor = floorRepository.findById(id)
            .orElseThrow { RuntimeException("Этаж не найден") }

        val rooms = roomRepository.findByFloorId(floor.id)
        val edges = edgeRepository.findByFloorId(floor.id)
        val stairs = stairsRepository.findByFloorId(floor.id)
        val nodes = nodeRepository.findByFloorId(floor.id)

        return FloorDTO(floor, rooms, edges, stairs, nodes)
    }

    @Transactional
    fun saveOrUpdateFloorData(id: Int, floorDTO: FloorDTO): FloorDTO {
        var floor = floorRepository.findById(id).orElse(Floor())
        floor.floorNumber = id
        floor.name = floorDTO.floor.name
        floor.points = floorDTO.floor.points
        floor = floorRepository.save(floor)

        roomRepository.deleteAllByFloorId(floor.id)
        nodeRepository.deleteAllByFloorId(floor.id)
        edgeRepository.deleteAllByFloorId(floor.id)
        stairsRepository.deleteAllByFloorId(floor.id)

        val nodeIdMapping = HashMap<Int, Int>()

        for (node in floorDTO.nodes) {
            val oldId = node.id
            node.id = null
            node.floorId = floor.id
            node.nodeNumber = oldId
            val saved = nodeRepository.save(node)
            oldId?.let { nodeIdMapping[it] = saved.id!! }
        }

        for ((oldId, newId) in nodeIdMapping) {
            val node = nodeRepository.findById(newId).orElseThrow()
            val oldNeighbors = floorDTO.nodes.find { it.id == oldId }?.neighbors

            oldNeighbors?.let {
                val newNeighbors = it.map { n -> nodeIdMapping.getOrDefault(n, n) }.toIntArray()
                node.neighbors = newNeighbors
                nodeRepository.save(node)
            }
        }

        for (edge in floorDTO.edges) {
            edge.id = null
            edge.floorId = floor.id
            val newNodes = edge.nodes.map { n -> nodeIdMapping.getOrDefault(n, n) }.toIntArray()
            edge.nodes = newNodes
            edgeRepository.save(edge)
        }

        for (room in floorDTO.rooms) {
            room.id = null
            room.floorId = floor.id
            room.nodeId?.let { oldNodeId ->
                if (nodeIdMapping.containsKey(oldNodeId)) {
                    room.nodeId = nodeIdMapping[oldNodeId]
                }
            }
            roomRepository.save(room)
        }

        for (stair in floorDTO.stairs) {
            stair.id = null
            stair.floorId = floor.id
            stairsRepository.save(stair)
        }

        return floorDTO
    }

    @Transactional
    fun deleteFloor(number: Int) {
        val floor = floorRepository.findByFloorNumber(number)
            .orElseThrow { EntityNotFoundException("Этаж не найден") }

        try {
            roomRepository.deleteAllByFloorId(floor.id!!)
            edgeRepository.deleteAllByFloorId(floor.id!!)
            stairsRepository.deleteAllByFloorId(floor.id!!)
            nodeRepository.deleteAllByFloorId(floor.id!!)
            floorRepository.delete(floor)
        } catch (e: OptimisticLockException) {
            throw RuntimeException("Ошибка при удалении этажа: ${e.message}")
        }
    }
}