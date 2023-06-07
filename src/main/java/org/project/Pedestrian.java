package org.project;

public class Pedestrian {
    private final int timeToMove;
    private final int exit;
    private int iteration;
    public int moved;
    public Pedestrian(int iterationsToMove, int initExit){
        if (iterationsToMove < 0) {System.out.println("Pieszy szybszy od swiatla");}
        timeToMove = iterationsToMove;
        exit = initExit;
        iteration = 0;
    }

    public int getTimeToMove() {return timeToMove;}
    public int getExit() {return exit;}
    public int getIteration() {return iteration;}
}
