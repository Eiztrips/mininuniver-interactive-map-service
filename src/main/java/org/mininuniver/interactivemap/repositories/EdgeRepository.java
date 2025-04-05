package org.mininuniver.interactivemap.repositories;

import org.mininuniver.interactivemap.models.Edge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EdgeRepository extends JpaRepository<Edge, Integer>{
    List<Edge> findByFloorId(int floorId);
}
