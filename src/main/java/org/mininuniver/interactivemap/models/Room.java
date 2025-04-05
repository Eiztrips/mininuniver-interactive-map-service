package org.mininuniver.interactivemap.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.mininuniver.interactivemap.models.submodels.Point;
import org.mininuniver.interactivemap.utils.PointListJsonbConverter;

import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "Rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int floorId;
    private int nodeId;

    @Convert(converter = PointListJsonbConverter.class)
    @Column(columnDefinition = "jsonb")
    private List<Point> points;

}