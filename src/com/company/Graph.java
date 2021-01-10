package com.company;

import java.io.Serializable;
import java.util.ArrayList;

public class Graph implements Serializable {
    ArrayList<Node> nodes = new ArrayList<>();
    boolean colorable = true;

    Graph(){
    }

    @Override
    public String toString() {
        String msg = "";
        for(Node node : nodes){
            msg += "id:" + node.id + " color:" + node.color + "\n";
        }
        return msg;
    }

    public void addNode(Node node){
        this.nodes.add(node);
    }

    public Node getNodeById(int id){
        for(Node node : this.nodes){
            if(node.id == id){
                return node;
            }
        }
        return null;
    }

    public Node DFS(int id){
        Node start = this.getNodeById(id);
        ArrayList<Node> visited = new ArrayList<>();
        ArrayList<Node> stack = new ArrayList<>();
        stack.add(start);
        visited.add(start);
        while(stack.size() > 0) {
            start = stack.get(0);
            stack.remove(0);
            for (Node next : start.neighbours) {
                if (next.color == null) {
                    return next;
                }
                if (!visited.contains(next)) {
                    stack.add(next);
                }
                visited.add(start);
            }
        }
        return null;
    }

    public boolean complete() {
        for(Node node : this.nodes){
            if(node.color == null){
                return false;
            }
        }
        return true;
    }
}
