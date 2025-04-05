package org.mininuniver.interactivemap.controllers;

import org.mininuniver.interactivemap.dto.FloorDataDTO;
import org.mininuniver.interactivemap.models.Node;
import org.mininuniver.interactivemap.models.Room;
import org.mininuniver.interactivemap.services.EdgeService;
import org.mininuniver.interactivemap.services.MapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/map")
public class MapController {

    @Autowired
    private MapService mapService;
    @Autowired
    private EdgeService edgeService;

    // FLOOR

    @GetMapping("/floors/{id}")
    public FloorDataDTO getFloorById(@PathVariable int id) {
        return mapService.getFloorData(id);
    }

    // ROOM

    @GetMapping("/rooms/{name}")
    public Room getRoomByName(@PathVariable String name) {
        return mapService.getRoomByName(name);
    }

    @GetMapping("/rooms")
    public List<Room> getAllRooms() {
        return mapService.getAllRooms();
    }

    // NODE

    @GetMapping("/nodes")
    public List<Node> getAllNodes() {
        return  mapService.getAllNodes();
    }

    // ADMIN

        @GetMapping("/generate/edges")
    public void generateEdges() {
        edgeService.generateEdges();
    }
}
