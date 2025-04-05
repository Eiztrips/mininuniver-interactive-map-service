package org.mininuniver.interactivemap.repositories;

import org.mininuniver.interactivemap.models.Stairs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StairsRepository extends JpaRepository<Stairs, Integer>{
    List<Stairs> findByFloorId(int floorId);
}
