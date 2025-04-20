package org.mininuniver.interactiveMap.dto.models.path;

import lombok.Getter;
import lombok.Setter;
import org.mininuniver.interactiveMap.models.Path;

@Getter
@Setter
public class PathDTO {
    private Integer id;
    private Integer floorId;
    private Integer firstRoomId;
    private Integer secondRoomId;
    private int[] nodesInPath;

    public PathDTO(Path path) {
        this.id = path.getId();
        this.floorId = path.getFloor() != null ? path.getFloor().getId() : null;
        this.firstRoomId = path.getFirstRoom() != null ? path.getFirstRoom().getId() : null;
        this.secondRoomId = path.getSecondRoom() != null ? path.getSecondRoom().getId() : null;
        this.nodesInPath = path.getNodesInPath();
    }
}
