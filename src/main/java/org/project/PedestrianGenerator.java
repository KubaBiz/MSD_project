package org.project;

import java.util.Random;

public class PedestrianGenerator {
    private int[] pedestrianChances; // wielkość tablicy 4
    private int[] exitChances; //wielkość tablicy 7
    private Point entrance;
    private int nextiteration;

    public PedestrianGenerator(int[] pedestrianchances, int[] exitchances, Point entrance){
        pedestrianChances = pedestrianchances;
        exitChances = exitchances;
        this.entrance = entrance;
        nextiteration = 0;
        int sum = 0;
        for (int number : pedestrianchances) {
            sum += number;
        }
        if (sum > 100){
            System.out.println("Nie zgadzajace sie procenty dla generacji ilosci pieszych");
        }
        sum = 0;
        for (int number : exitchances) {
            sum += number;
        }
        if (sum > 100){
            System.out.println("Nie zgadzajace sie procenty dla generacji wyjsc dla pieszych");
        }
    }

    public void generate(){
        if (nextiteration == 0){
            Random random = new Random();
            int howMany;
            int randomNumber = random.nextInt(100);
            if(randomNumber<pedestrianChances[0]){ //0
                howMany = 1;
            }
            else if (randomNumber<pedestrianChances[0]+pedestrianChances[1]){ //1
                howMany = 2;
            }
            else if (randomNumber<pedestrianChances[0]+pedestrianChances[1]+pedestrianChances[2]){ //2
                howMany = 3;
            }
            else if (randomNumber<pedestrianChances[0]+pedestrianChances[1]+pedestrianChances[2]+pedestrianChances[3]){ //3
                howMany = 4;
            }
            else { //4
                howMany = 0;
            }

            for(int i = 0; i<howMany; i++){
                if (entrance.pedestrians.size() >= 5){
                    continue;
                }
                int whereTo;
                randomNumber = random.nextInt(100);
                if(randomNumber<exitChances[0]){ //1
                    whereTo = 1;
                }
                else if (randomNumber<exitChances[0]+exitChances[1]){ //2
                    whereTo = 2;
                }
                else if (randomNumber<exitChances[0]+exitChances[1]+exitChances[2]){ //3
                    whereTo = 3;
                }
                else if (randomNumber<exitChances[0]+exitChances[1]+exitChances[2]+exitChances[3]){ //4
                    whereTo = 4;
                }
                else if (randomNumber<exitChances[0]+exitChances[1]+exitChances[2]+exitChances[3]+exitChances[4]){ //5
                    whereTo = 5;
                }
                else if (randomNumber<exitChances[0]+exitChances[1]+exitChances[2]+exitChances[3]+exitChances[4]+exitChances[5]){ //6
                    whereTo = 6;
                }
                else if (randomNumber<exitChances[0]+exitChances[1]+exitChances[2]+exitChances[3]+exitChances[4]+exitChances[5]+exitChances[6]){ //7
                    whereTo = 7;
                }
                else { //8
                    whereTo = 8;
                }
                randomNumber = random.nextInt(100);
                int number;
                if (randomNumber < 20) {
                    number = 2;
                } else if (randomNumber < 80) {
                    number = 3;
                } else {
                    number = 4;
                }
                entrance.pedestrians.add(new Pedestrian(number,whereTo));
            }
            nextiteration = 3;
        }
        else{
            nextiteration -= 1;
        }
    }
}
