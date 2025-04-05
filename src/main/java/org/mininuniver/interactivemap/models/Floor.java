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
@Table(name = "Floors")
public class Floor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Convert(converter = PointListJsonbConverter.class)
    @Column(columnDefinition = "jsonb")
    private List<Point> points;

}
