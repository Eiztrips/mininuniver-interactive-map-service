package org.mininuniver.interactiveMap.dto.models.stairs;

import lombok.Getter;
import lombok.Setter;
import org.mininuniver.interactiveMap.models.Stairs;
import org.mininuniver.interactiveMap.models.submodels.Point;

import java.util.List;

@Getter
@Setter
public class StairsDTO {
    private Integer id;
    private Integer floorId;
    private Integer nodeId;
    private List<Point> points;
    private int[] floors;

    public StairsDTO(Stairs stairs) {
        this.id = stairs.getId();
        this.floorId = stairs.getFloor() != null ? stairs.getFloor().getId() : null;
        this.nodeId = stairs.getNode() != null ? stairs.getNode().getId() : null;
        this.points = stairs.getPoints();
        this.floors = stairs.getFloors();
    }
}
