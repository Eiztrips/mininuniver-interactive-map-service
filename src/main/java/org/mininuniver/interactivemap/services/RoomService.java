package org.mininuniver.interactivemap.services;

import org.mininuniver.interactivemap.models.Room;
import org.mininuniver.interactivemap.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

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
}
