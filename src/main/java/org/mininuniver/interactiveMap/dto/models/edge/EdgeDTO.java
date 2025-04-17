package org.mininuniver.interactiveMap.dto.models.edge;

import lombok.Getter;
import lombok.Setter;
import org.mininuniver.interactiveMap.models.Edge;

@Getter
@Setter
public class EdgeDTO {
    private Integer id;
    private Integer floorId;
    private Float distance;
    private int[] nodes;

    public EdgeDTO(Edge edge) {
        this.id = edge.getId();
        this.floorId = edge.getFloor() != null ? edge.getFloor().getId() : null;
        this.distance = edge.getDistance();
        this.nodes = edge.getNodes();
    }
}
