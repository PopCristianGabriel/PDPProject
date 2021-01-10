package com.company;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class Node implements Serializable {
    int id;
    String color = null;
    ArrayList<Node> neighbours;

    Node(int id){
        this.neighbours = new ArrayList<>();
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return id == node.id;
    }

    public void addColor(String color){
        this.color = color;
    }

    public void addNeigbour(Node newNode){
        this.neighbours.add(newNode);
    }

}
