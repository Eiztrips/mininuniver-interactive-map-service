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

package org.mininuniver.interactivemap.services;

import org.mininuniver.interactivemap.models.*;
import org.mininuniver.interactivemap.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NodeService {

    private final NodeRepository nodeRepository;
    private final FloorRepository floorRepository;

    @Autowired
    public NodeService(NodeRepository nodeRepository, FloorRepository floorRepository) {
        this.nodeRepository = nodeRepository;
        this.floorRepository = floorRepository;
    }

    public List<Node> getAllNodes() {
        return nodeRepository.findAll();
    }

    public Node getLastNodeId(int number) {
        Floor floor = floorRepository.findByFloorNumber(number).get();
        List<Node> nodes = nodeRepository.findAllByFloor(floor);
        if (nodes.isEmpty()) {
            return null;
        }
        return nodes.getLast();
    }
}