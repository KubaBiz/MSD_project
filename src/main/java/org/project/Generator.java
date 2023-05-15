package org.project;

import java.util.Random;

public class Generator {

    private Vector2d position;
    private Vector2d tailVector;


    public Generator(Vector2d initPosition, Vector2d initTailVector){
        position = initPosition;
        tailVector = initTailVector;
    }

    public Vector2d getPosition() {
        return position;
    }

    public Vehicles generateVehicle(){

        Vehicles bike = new Vehicles(1,2,1,1, position, tailVector);
        Vehicles car = new Vehicles(2,4,2,2,position,tailVector);
        Vehicles bigCar = new Vehicles(3,4,2,2,position, tailVector);

        Random r = new Random();
        int draw = r.nextInt(0,12);
        if(draw < 4) return bike;
        else if (draw < 10) return  car;
        else return bigCar;
    }
}

