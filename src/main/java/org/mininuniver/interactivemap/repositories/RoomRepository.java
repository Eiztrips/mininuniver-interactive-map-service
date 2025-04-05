package org.mininuniver.interactivemap.repositories;

import org.mininuniver.interactivemap.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {

    List<Room> findByFloorId(int floorId);
    List<Room> findAll();
    Optional<Room> findByName(String name);
    Optional<Room> findById(Integer id);

}