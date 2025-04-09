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

import org.mininuniver.interactivemap.models.Edge;
import org.mininuniver.interactivemap.models.Node;
import org.mininuniver.interactivemap.repositories.EdgeRepository;
import org.mininuniver.interactivemap.repositories.NodeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class EdgeService {
    private final NodeRepository nodeRepository;
    private final EdgeRepository edgeRepository;

    public EdgeService(NodeRepository nodeRepository, EdgeRepository edgeRepository) {
        this.nodeRepository = nodeRepository;
        this.edgeRepository = edgeRepository;
    }

    @Transactional
    public void generateEdges() {
        List<Node> nodes = nodeRepository.findAll();
        Set<String> addedEdges = new HashSet<>();

        List<Edge> edges = new ArrayList<>();

        for (Node node : nodes) {
            int nodeId = Math.toIntExact(node.getId());
            Map<String, Object> pos1 = node.getPos();
            float x1 = ((Number) pos1.get("x")).floatValue();
            float y1 = ((Number) pos1.get("y")).floatValue();

            for (int neighborId : node.getNeighbors()) {
                if (nodeId < neighborId) {
                    Node neighbor = nodes.stream()
                            .filter(n -> n.getId() == neighborId)
                            .findFirst()
                            .orElse(null);

                    if (neighbor != null) {
                        Map<String, Object> pos2 = neighbor.getPos();
                        float x2 = ((Number) pos2.get("x")).floatValue();
                        float y2 = ((Number) pos2.get("y")).floatValue();

                        float distance = calculateDistance(x1, y1, x2, y2);
                        String edgeKey = nodeId + "-" + neighborId;

                        if (!addedEdges.contains(edgeKey)) {
                            edges.add(new Edge(new int[]{nodeId, neighborId}, node.getFloor(), distance));
                            addedEdges.add(edgeKey);
                        }
                    }
                }
            }
        }

        edgeRepository.saveAll(edges);
    }

    private float calculateDistance(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
}