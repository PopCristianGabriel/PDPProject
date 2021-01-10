package com.company;

import mpi.MPI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class Main {

    public static void main(String[] args) {
        MPI.Init(args);
        int me = MPI.COMM_WORLD.Rank();
        int nrProcs = MPI.COMM_WORLD.Size();

        if(me == 0){
            graphMaster();
        }
        else{
            coloring(me);
        }

        MPI.Finalize();
    }

    public static void graphMaster(){
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
            for (int i = 0; i < numberOfVertices; i++) {
                startingPoints.add(i);
            }

            for(int i = 1 ; i <= numberOfThreads; i++){
                int j = ThreadLocalRandom.current().nextInt(0, startingPoints.size() - 2);
                if(startingPoints.get(j) == 10){
                    int k = 0;
                }
                MPI.COMM_WORLD.Send(new Object[]{true}, 0, 1, MPI.OBJECT, i, 0);
                MPI.COMM_WORLD.Send(new Object[]{graph}, 0, 1, MPI.OBJECT, i, 0);
                MPI.COMM_WORLD.Send(new Object[]{numberOfColors}, 0, 1, MPI.OBJECT, i, 0);
                MPI.COMM_WORLD.Send(new Object[]{startingPoints.get(j)}, 0, 1, MPI.OBJECT, i, 0);
                Object[] results = new Object[2];
                MPI.COMM_WORLD.Recv(results, 0, 1, MPI.OBJECT, i, 0);
                startingPoints.remove(j);
                Graph result = (Graph) results[0];
                graph = result;
                if(result.colorable == false){
                    break;
                }
                if(result.complete() == true){
                    break;
                }

            }

            if(graph.colorable == false){
                for(Node node : graphNodes){
                    node.addColor(null);
                }
                numberOfColors++;
            }
            System.out.println(graph);
            System.out.println("------");


        }while(graph.colorable == false || !graph.complete());
        for(int i = 1; i <= numberOfThreads; i++){
            MPI.COMM_WORLD.Send(new Object[]{false}, 0, 1, MPI.OBJECT, i, 0);
        }
        System.out.println("done");
    }

    //1
    private static void coloring(int me){
        while(true) {
            Object[] continueObject = new Object[2];
            MPI.COMM_WORLD.Recv(continueObject, 0, 1, MPI.OBJECT, 0, 0);
            boolean toContinue = (Boolean) continueObject[0];
            if (!toContinue) {
                return;
            }
            Object[] graphObject = new Object[2];
            MPI.COMM_WORLD.Recv(graphObject, 0, 1, MPI.OBJECT, 0, 0);
            Graph graph = (Graph) graphObject[0];
            Object[] numberOfColorsObject = new Object[2];
            MPI.COMM_WORLD.Recv(numberOfColorsObject, 0, 1, MPI.OBJECT, 0, 0);
            Integer nrOfColors = (Integer) numberOfColorsObject[0];
            Object[] startingPointObject = new Object[2];
            MPI.COMM_WORLD.Recv(startingPointObject, 0, 1, MPI.OBJECT, 0, 0);
            Integer start = (Integer) startingPointObject[0];
            ArrayList<String> colors = new ArrayList(Arrays.asList("red", "blue", "yellow", "pink", "purple", "bluemarin", "magenta", "black", "white", "green", "darkgreen", "purple2","purple3"));

            if (graph.colorable == false) {
                MPI.COMM_WORLD.Send(new Object[]{graph}, 0, 1, MPI.OBJECT, 0, 0);
            } else {
                Node startingNode = graph.getNodeById(start);
                if(startingNode == null){
                    int i = 0;
                }
                if (startingNode.color != null) {
                    startingNode = graph.DFS(start);
                    start = startingNode.id;
                }
                ArrayList<String> availableColors = new ArrayList<>();
                for (int i = 0; i < nrOfColors; i++) {
                    availableColors.add(colors.get(i));
                }
                for (Node neighbour : startingNode.neighbours) {
                    if (neighbour.color != null) {
                        availableColors.remove(neighbour.color);
                    }
                }
                if (availableColors.size() > 0) {

                    int randomColor = ThreadLocalRandom.current().nextInt(0, availableColors.size());
                    startingNode.addColor(availableColors.get(randomColor));

                } else {
                    graph.colorable = false;
                }
                MPI.COMM_WORLD.Send(new Object[]{graph}, 0, 1, MPI.OBJECT, 0, 0);
                }
            }
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
