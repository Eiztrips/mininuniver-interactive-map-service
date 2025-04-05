package org.mininuniver.interactivemap.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "Edges")
public class Edge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nodes", columnDefinition = "integer[]", nullable = false)
    private int[] nodes;

    @Column(name = "floor_id", nullable = false)
    private int floorId;

    @Column(name = "distance", nullable = false)
    private float distance;

    public Edge(int[] nodes, int floorId, float distance) {
        this.nodes = nodes;
        this.floorId = floorId;
        this.distance = distance;
    }

}
