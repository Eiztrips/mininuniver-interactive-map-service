package org.mininuniver.interactiveMap.dto.models.room;

import lombok.Getter;
import lombok.Setter;
import org.mininuniver.interactiveMap.models.Room;
import org.mininuniver.interactiveMap.dto.models.submodels.PointDTO;

import java.util.List;

@Getter
@Setter
public class RoomDTO {
    private Integer id;
    private String name;
    private Integer floorId;
    private Integer nodeId;
    private List<PointDTO> points;

    public RoomDTO(Room room) {
        this.id = room.getId();
        this.name = room.getName();
        this.floorId = room.getFloor() != null ? room.getFloor().getId() : null;
        this.nodeId = room.getNode() != null ? room.getNode().getId() : null;
        this.points = room.getPoints();
    }
}
