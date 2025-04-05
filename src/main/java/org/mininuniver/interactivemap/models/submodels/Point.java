package org.mininuniver.interactivemap.models.submodels;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Point {
    private int x;
    private int y;

    public Point() {}

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}