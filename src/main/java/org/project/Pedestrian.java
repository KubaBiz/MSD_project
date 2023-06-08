package org.project;

public class Pedestrian {
    private final int timeToMove;
    private final int exit;
    public int iteration;
    public boolean moved;
    public boolean toRemove;
    public Pedestrian(int iterationsToMove, int initExit){
        if (iterationsToMove < 0) {System.out.println("Pieszy szybszy od swiatla");}
        timeToMove = iterationsToMove;
        exit = initExit;
        iteration = 0;
    }

    public int getTimeToMove() {return timeToMove;}
    public int getExit() {return exit;}
    public int getIteration() {return iteration;}

    public void minusIteration() {iteration -= 1;}
}
