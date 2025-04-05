package org.mininuniver.interactivemap.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.mininuniver.interactivemap.utils.JsonbConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Setter
@Getter
@Entity
@Table(name = "Nodes")
public class Node {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int floorId;

    @Convert(converter = JsonbConverter.class)
    @Column(name = "pos", columnDefinition = "jsonb")
    private Map<String, Object> pos;

    @Column(name = "neighbors", columnDefinition = "integer[]")
    private int[] neighbors;

}
