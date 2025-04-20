package org.mininuniver.interactiveMap.models;

import com.vladmihalcea.hibernate.type.array.IntArrayType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.mininuniver.interactiveMap.dto.models.path.PathDTO;
import org.mininuniver.interactiveMap.dto.models.room.RoomDTO;

import java.util.List;

@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name = "Paths")
public class Path {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "first_room_id")
    private Room firstRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "second_room_id")
    private Room secondRoom;

    @Column(name = "nodes", columnDefinition = "integer[]")
    @Type(IntArrayType.class)
    private int[] nodesInPath;

    public Path(PathDTO path) {
        if (path.getFirstRoomId() != null) {
            this.firstRoom = new Room();
            this.firstRoom.setId(path.getFirstRoomId());
        }

        if (path.getSecondRoomId() != null) {
            this.secondRoom = new Room();
            this.secondRoom.setId(path.getSecondRoomId());
        }

        this.nodesInPath = path.getNodesInPath();
    }

}
