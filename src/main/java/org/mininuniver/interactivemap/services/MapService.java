package org.mininuniver.interactivemap.services;

import jakarta.annotation.PostConstruct;
import org.mininuniver.interactivemap.dto.FloorDataDTO;
import org.mininuniver.interactivemap.models.*;
import org.mininuniver.interactivemap.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MapService {

    private final FloorRepository floorRepository;
    private final RoomRepository roomRepository;
    private final EdgeRepository edgeRepository;
    private final StairsRepository stairsRepository;
    private final NodeRepository nodeRepository;

    @Autowired
    public MapService(FloorRepository floorRepository, RoomRepository roomRepository,
                      EdgeRepository edgeRepository, StairsRepository stairsRepository, NodeRepository nodeRepository) {
        this.floorRepository = floorRepository;
        this.roomRepository = roomRepository;
        this.edgeRepository = edgeRepository;
        this.stairsRepository = stairsRepository;
        this.nodeRepository = nodeRepository;
    }

    // FLOOR

    public FloorDataDTO getFloorData(int id) {
        Floor floor = floorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Этаж не найден"));

        List<Room> rooms = roomRepository.findByFloorId(id);

        List<Edge> edges = edgeRepository.findByFloorId(id);

        List<Stairs> stairs = stairsRepository.findByFloorId(id);

        List<Node> nodes = nodeRepository.findByFloorId(id);

        return new FloorDataDTO(floor, rooms, edges, stairs, nodes);
    }

    // ROOM

    public Room getRoomByName(String name) {
        return roomRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException(String.format("Помещение %s не найдено", name)));
    }

    public Room getRoomById(int id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("Помещение с ID:%d не найдено", id)));
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    // NODE

    public List<Node> getAllNodes() {
        return nodeRepository.findAll();
    }

}