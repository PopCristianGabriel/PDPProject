package com.company;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Lock;

public class Coloring implements Runnable {
    Graph graph;
    int start;
    ArrayList<String> colors = new ArrayList(Arrays.asList("red", "blue", "yellow", "pink", "purple", "bluemarin", "magenta", "black", "white", "green", "darkgreen"));
    int nrOfColors = 2;
    Lock mutex;

    @Override
    public void run() {
        while (true) {
            this.mutex.lock();
            if (this.graph.colorable == false) {
                this.mutex.unlock();
                return;
            }
            Node startingNode = graph.getNodeById(this.start);
            ArrayList<String> availableColors = new ArrayList<>();
            for (int i = 0; i < this.nrOfColors; i++) {
                availableColors.add(colors.get(i));
            }
            for (Node neighbour : startingNode.neighbours) {
                if (neighbour.color != null) {
                    removeAvailableColor(availableColors, neighbour.color);
                }
            }
            System.out.println(availableColors);
            if (availableColors.size() > 0) {

                int randomColor = ThreadLocalRandom.current().nextInt(0, availableColors.size());
                startingNode.addColor(availableColors.get(randomColor));

            } else {
                this.mutex.unlock();
                this.graph.colorable = false;
                return;
            }

            Node nextNode = this.graph.DFS(this.start);
            if (nextNode == null) {
                this.mutex.unlock();
                return;
            }
            this.start = nextNode.id;

            System.out.println(graph.toString());
            System.out.println(graph.colorable);
            this.mutex.unlock();
        }

    }


    private void removeAvailableColor(ArrayList<String> availableColors, String color) {
        availableColors.remove(color);
    }

    Coloring(Graph graph, int startingId, int nrOfColors, Lock mutex) {
        this.graph = graph;
        this.start = startingId;
        this.nrOfColors = nrOfColors;
        this.mutex = mutex;
    }
}
