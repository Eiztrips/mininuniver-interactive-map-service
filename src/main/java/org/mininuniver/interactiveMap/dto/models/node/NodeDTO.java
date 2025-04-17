package org.mininuniver.interactiveMap.dto.models.node;

import lombok.Getter;
import lombok.Setter;
import org.mininuniver.interactiveMap.models.Node;

import java.util.Map;

@Getter
@Setter
public class NodeDTO {
    private Integer id;
    private Integer nodeNumber;
    private Integer floorId;
    private Map<String, Object> pos;
    private int[] neighbors;

    public NodeDTO(Node node) {
        this.id = node.getId();
        this.nodeNumber = node.getNodeNumber();
        this.floorId = node.getFloor() != null ? node.getFloor().getId() : null;
        this.pos = node.getPos();
        this.neighbors = node.getNeighbors();
    }
}
