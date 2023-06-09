package org.project;

import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.abs;
import static java.lang.Math.max;



public class Vehicles {
    // VEHICLE PARAMETERS
    private int length;
    private int maxSpeed;
    private int acceleration;
    private int deceleration;
    private int speed;
    private Vector2d position;
    private Vector2d tail[];
    public boolean moved;
    // END OF VEHICLE PARAMETERS

    // PEDESTRIANS PARAMETERS
    private static final int SFMAX = 100000;
    public ArrayList<Pedestrian> pedestrians = new ArrayList<>(5);
    public ArrayList<Vehicles> neighbors = new ArrayList<>();
    public ArrayList<Integer> staticFields = new ArrayList<>(8);
    public boolean isSidewalk;
    public boolean isExit;
    public int which_exit;
    // END OF PEDESTRIANS PARAMETERS

    public Vehicles(int initLength, int initMaxSpeed, int initAcceleration,
                    int initDeceleration, Vector2d initPosition, Vector2d tailVector){
        length = initLength;
        maxSpeed = initMaxSpeed;
        acceleration = initAcceleration;
        position = initPosition;
        deceleration = initDeceleration;
        speed = 0;
        if (length > 1) {
            tail  = new Vector2d[length-1];
            for(int i = 0; i < length-1; i++){
                tail[i] = position;
            }
        }
        //for (int i = 0; i < 5; i++) {
        //    pedestrians.add(null);
        //}
        for (int i = 0; i < 8; i++) {
            staticFields.add(SFMAX);
        }
    }

    public Vector2d getPosition(){
        return position;
    }

    public int getDeceleration() {
        return deceleration;
    }

    public int getAcceleration() {
        return acceleration;
    }

    public Vector2d[] getTail() {return tail;}
    public int getSpeed() {return speed;}

    public int getLength() {
        return length;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public void speedBoost(){ speed = max(maxSpeed, speed + acceleration);}
    public void setLength(int length){ this.length = length;}
    public void setMaxSpeed(int maxSpeed) {this.maxSpeed = maxSpeed;}
    public void setAcceleration(int acceleration) {this.acceleration = acceleration;}
    public void setDeceleration(int deceleration) {this.deceleration = deceleration;}
    public void setSpeed(int speed) {this.speed = speed;}
    public void setPosition(Vector2d position) {
        this.position = position;
    }
    public void setTail(/*Vector2d[] tail*/) { //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        if (length > 1) {
            tail  = new Vector2d[length-1];
            for(int i = 0; i < length-1; i++){
                tail[i] = position;
            }
        }
    } //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

    public void copyVehicleToAnotherTile(Vehicles other){
        length = other.length;
        maxSpeed = other.maxSpeed;
        acceleration = other.acceleration;
        deceleration = other.deceleration;
        speed = other.speed;
        position = other.position;
        tail = other.tail;
    }
    public void speedReduction(int obstacleDistance){  //przeszkoda jest cos nieruchomego badz z mniejsza predkoscia
        if(obstacleDistance == 0){
            speed = 0;
        }
        else {
            speed = (int) Math.max(1, Math.min(obstacleDistance, Math.round(Math.sqrt(2 * obstacleDistance * deceleration))));
            speed = Math.min(speed,maxSpeed);
        }
    }

    public void moveTail(Vector2d vector) {
        if (length > 1) {
            for (int i = length - 2; i > 0; i--) {
                tail[i] = tail[i - 1];
            }
            tail[0] = vector;
        }

    }
    //PEDESTRIAN FUNCTIONS
    public boolean calcStaticField(int type) {
        if (type == 0) {
            int min = SFMAX;
            for (Vehicles neighbor : neighbors) {
                if (neighbor.staticFields.get(0) <= min) {
                    min = neighbor.staticFields.get(0);
                }
                // REPULSION FORCE BETWEEN THE WALLS OR REPULSION IN DIFFERENT PASSAGES
            }
            if (staticFields.get(0) > min + 1) {
                staticFields.set(0, min + 1);
                return true;
            }
            return false;
        }
        //else if (type == 2){
        //    if (this.type == 1){
        //        staticFieldSecond = SFMAX;
        //        return false;
        //    }
        //    int min = SFMAX;
        //    for (Point neighbor : neighbors) {
        //        if (neighbor.staticFieldSecond <= min) {
        //            min = neighbor.staticFieldSecond;
        //        }
        //        if (neighbor.type == 1){
        //            walls += 1;
        //        }
        //    }
        //    if (staticFieldSecond > min + 1) {
        //        staticFieldSecond = min + 1 + walls;
        //        return true;
        //    }
        //    return false;
        //}
        return false;
    }

    public void movePedestrians(){

        for (Pedestrian pedestrian: pedestrians) {
            if(pedestrian.moved){
                continue;
            }
            if (pedestrian.getIteration() > 0) {
                pedestrian.minusIteration();
                continue;
            }
            if (pedestrian.getExit() == 1) {
                int min = SFMAX;
                ArrayList<Vehicles> possible = new ArrayList<>();
                for (Vehicles neighbor : neighbors) {
                    if (neighbor.staticFields.get(0) < min && neighbor.pedestrians.size()<5 && neighbor.isSidewalk) {
                        min = neighbor.staticFields.get(0);
                        possible.clear();
                        possible.add(neighbor);
                    } else if (neighbor.staticFields.get(0) == min && neighbor.pedestrians.size()<5 && neighbor.isSidewalk) {
                        possible.add(neighbor);
                    }
                }
                if (possible.size() > 0) {
                    Random random = new Random();
                    //Vehicles next = possible.get(random.nextInt(possible.size()));
                    Vehicles next = possible.get(0);
                    if (next.isExit) {
                        pedestrian.toRemove = true;
                    } else {
                        pedestrian.toRemove = true;
                        pedestrian.iteration = pedestrian.getTimeToMove();
                        next.pedestrians.add(pedestrian);
                        pedestrian.moved = true;
                    }
                }
            }
        }
        for (int i = pedestrians.size()-1; i>-1; i--){
            if (pedestrians.get(i).toRemove){
                pedestrians.get(i).toRemove = false;
                pedestrians.remove(i);
            }
        }
    }

    public void clearPedestrians(){
        pedestrians.clear();
        for (int i = 0; i < 8; i++){
            staticFields.set(i, SFMAX);
        }
    }
    public void addNeighbor(Vehicles nei) {
        neighbors.add(nei);
    }
    //END OF PEDESTRIAN FUNCTIONS


    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Position: ").append(position).append(", ");
        stringBuilder.append("Tail: ");
        if(length > 1){
            for (int i = 0; i < length-1; i++) {
                stringBuilder.append(tail[i]).append(" ");
            }
        }
        return stringBuilder.toString();
    }

    }
