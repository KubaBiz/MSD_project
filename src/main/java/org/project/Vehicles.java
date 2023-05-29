package org.project;

import static java.lang.Math.max;



public class Vehicles {

    private int length;
    private int maxSpeed;
    private int acceleration;
    private int deceleration;
    private int speed;
    private Vector2d position;
    private Vector2d tail[];
    public boolean moved;

    public void setPosition(Vector2d position) {
        this.position = position;
    }

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
