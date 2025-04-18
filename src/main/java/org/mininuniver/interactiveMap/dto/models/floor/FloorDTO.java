package org.mininuniver.interactiveMap.dto.models.floor;

import lombok.Getter;
import lombok.Setter;
import org.mininuniver.interactiveMap.models.Floor;
import org.mininuniver.interactiveMap.dto.models.submodels.PointDTO;

import java.util.List;

@Getter
@Setter
public class FloorDTO {
    public Integer id;
    public Integer floorNumber;
    public String name;
    public List<PointDTO> points;

    public FloorDTO(Floor floor) {
        this.id = floor.getId();
        this.floorNumber = floor.getFloorNumber();
        this.name = floor.getName();
        this.points = floor.getPoints();
    }
}
