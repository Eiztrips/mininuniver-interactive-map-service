package org.mininuniver.interactivemap.repositories;

import org.mininuniver.interactivemap.models.Node;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NodeRepository extends JpaRepository<Node, Integer>{
    List<Node> findByFloorId(int floorId);
    List<Node> findAll();
}
