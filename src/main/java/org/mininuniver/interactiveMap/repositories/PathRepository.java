package org.mininuniver.interactiveMap.repositories;

import org.mininuniver.interactiveMap.models.Node;
import org.mininuniver.interactiveMap.models.Path;
import org.mininuniver.interactiveMap.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PathRepository extends JpaRepository<Path, Integer> {
    List<Path> findByFloorId(Integer floorId);
}
