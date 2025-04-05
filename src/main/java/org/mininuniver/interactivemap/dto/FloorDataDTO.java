package org.mininuniver.interactivemap.dto;

import lombok.Getter;
import lombok.Setter;
import org.mininuniver.interactivemap.models.*;

import java.util.List;

@Getter
@Setter
public class FloorDataDTO {
    private Floor floor;
    private List<Room> rooms;
    private List<Edge> edges;
    private List<Stairs> stairs;
    private List<Node> nodes;

    public FloorDataDTO(Floor floor, List<Room> rooms, List<Edge> edges, List<Stairs> stairs, List<Node> nodes) {
        this.floor = floor;
        this.rooms = rooms;
        this.edges = edges;
        this.stairs = stairs;
        this.nodes = nodes;
    }

}
