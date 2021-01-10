package com.company;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.ThreadLocalRandom;;
public class Main {

    public static void main(String[] args) throws InterruptedException {
        Lock lock = new ReentrantLock();
        Graph graph = new Graph();
        int numberOfVertices = 10;
        int numberOfEdges = 30;
        int numberOfThreads = 2;
        int numberOfColors = 2;

        ArrayList<Node> graphNodes = getNodes(numberOfVertices,numberOfEdges);
        graph.nodes = graphNodes;
        do {
            graph.colorable = true;
            ArrayList<Integer> startingPoints = new ArrayList<>();
            ArrayList<Thread> threads = new ArrayList<>();
            for (int i = 0; i < numberOfVertices; i++) {
                startingPoints.add(i);
            }

            for (int i = 0; i < numberOfThreads; i++) {
                int j = ThreadLocalRandom.current().nextInt(0, startingPoints.size() - 1);
                Thread newThread = new Thread(new Coloring(graph, startingPoints.get(j), numberOfColors, lock));
                startingPoints.remove(j);
                threads.add(newThread);
            }

            for(int i = 0 ; i < numberOfThreads; i++){
                threads.get(i).start();
            }

            for (int i = 0; i < numberOfThreads; i++) {
                threads.get(i).join();
            }

            if(graph.colorable == false){
                for(Node node : graphNodes){
                    node.addColor(null);
                }
            }
            numberOfColors++;
            System.out.println(graph);


        }while(graph.colorable == false);


    }

    public static ArrayList<Node> getNodes(int numberOfVertices, int numberOfEdges){
        ArrayList<Pair> edges = new ArrayList<>();
        ArrayList<Node> nodes = new ArrayList<>();
        for(int i = 0 ; i < numberOfVertices; i++){
            nodes.add(new Node(i));
        }
        int addedEdges = 0;
        while(addedEdges < numberOfEdges){
            int first;
            int second;
            do {
                first = ThreadLocalRandom.current().nextInt(0, numberOfVertices);
                second = ThreadLocalRandom.current().nextInt(0, numberOfVertices);
            }while(first == second);

            Pair newEdge = new Pair(first,second);
            if(!edges.contains(newEdge)){
                addedEdges++;
                nodes.get(newEdge.first).addNeigbour(nodes.get(newEdge.second));
                nodes.get(newEdge.second).addNeigbour(nodes.get(newEdge.first));
                edges.add(newEdge);
            }
        }
        System.out.println(edges);
        return nodes;
    }
}
