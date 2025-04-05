package org.mininuniver.interactivemap.repositories;

import org.mininuniver.interactivemap.models.Floor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FloorRepository extends JpaRepository<Floor, Integer> {
}
