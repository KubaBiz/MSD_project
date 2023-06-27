package org.project;

import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.max;


public class Point {
    // VEHICLE PARAMETERS
    private int length;
    Random random = new Random();    int color;
    private int maxSpeed;
    private int acceleration;
    private int deceleration;
    private int speed;
    private Vector2d position;
    private Vector2d tail[];
    public boolean moved;
    private int destination;
    // END OF VEHICLE PARAMETERS

    // PEDESTRIANS PARAMETERS
    private static final int SFMAX = 100000;
    public ArrayList<Pedestrian> pedestrians = new ArrayList<>(5);
    public ArrayList<Point> neighbors = new ArrayList<>();

    public Point downNeighbor;
    public Point rightNeighbor;
    public Point leftNeighbor;
    public Point upNeighbor;
    public ArrayList<Integer> staticFields = new ArrayList<>(8);
    public boolean isSidewalk;
    public boolean isExit;
    public int which_exit;
    int[] tab = {11,0,1};// 0 skręt w prawo, 1 prosto, 11 w lewo
    // END OF PEDESTRIANS PARAMETERS

    public Point(int initLength, int initMaxSpeed, int initAcceleration,
                 int initDeceleration, Vector2d initPosition, Vector2d tailVector){
        Random r = new Random();
        length = initLength;
        maxSpeed = initMaxSpeed;
        acceleration = initAcceleration;
        position = initPosition;
        deceleration = initDeceleration;
        speed = 0;
        destination = tab[r.nextInt(tab.length)];
        color = r.nextInt(8);
        if (length > 1) {
            tail  = new Vector2d[length-1];
            for(int i = 0; i < length-1; i++){
                tail[i] = position;
            }
        }
        for (int i = 0; i < 8; i++) {
            staticFields.add(SFMAX);
        }
    }

    public void addNewDestination(int... newDest) {
        Random r = new Random();
        if (newDest.length > 0) {
            destination = newDest[0];
        } else {
            destination = tab[r.nextInt(tab.length)];
        }
    }
    public int getDestination() {return destination;}
    public void setDestination(){destination = destination/10;}
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

    public void copyVehicleToAnotherTile(Point other){
        length = other.length;
        maxSpeed = other.maxSpeed;
        acceleration = other.acceleration;
        deceleration = other.deceleration;
        speed = other.speed;
        position = other.position;
        tail = other.tail;
        destination = other.destination;
        color = other.color;
    }
    public void speedReduction(int obstacleDistance){  //przeszkoda jest cos nieruchomego badz z mniejsza predkoscia
        if(obstacleDistance < 1){
            speed = 0;
        }
        else {
            speed = (int) Math.max(1, Math.min(obstacleDistance - 1, Math.round(Math.sqrt(2 * obstacleDistance * deceleration))));
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
            for (Point neighbor : neighbors) {
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
        else if (type == 1) {
            int min = SFMAX;
            for (Point neighbor : neighbors) {
                if (neighbor.staticFields.get(1) <= min) {
                    min = neighbor.staticFields.get(1);
                }
                // REPULSION FORCE BETWEEN THE WALLS OR REPULSION IN DIFFERENT PASSAGES
            }
            if (staticFields.get(1) > min + 1) {
                staticFields.set(1, min + 1);
                return true;
            }
            return false;
        }
        else if (type == 2) {
            int min = SFMAX;
            for (Point neighbor : neighbors) {
                if (neighbor.staticFields.get(2) <= min) {
                    min = neighbor.staticFields.get(2);
                }
                // REPULSION FORCE BETWEEN THE WALLS OR REPULSION IN DIFFERENT PASSAGES
            }
            if (staticFields.get(2) > min + 1) {
                staticFields.set(2, min + 1);
                return true;
            }
            return false;
        }
        else if (type == 3) {
            int min = SFMAX;
            for (Point neighbor : neighbors) {
                if (neighbor.staticFields.get(3) <= min) {
                    min = neighbor.staticFields.get(3);
                }
                // REPULSION FORCE BETWEEN THE WALLS OR REPULSION IN DIFFERENT PASSAGES
            }
            if (staticFields.get(3) > min + 1) {
                staticFields.set(3, min + 1);
                return true;
            }
            return false;
        }
        else if (type == 4) {
            int min = SFMAX;
            for (Point neighbor : neighbors) {
                if (neighbor.staticFields.get(4) <= min) {
                    min = neighbor.staticFields.get(4);
                }
                // REPULSION FORCE BETWEEN THE WALLS OR REPULSION IN DIFFERENT PASSAGES
            }
            if (staticFields.get(4) > min + 1) {
                staticFields.set(4, min + 1);
                return true;
            }
            return false;
        }
        else if (type == 5) {
            int min = SFMAX;
            for (Point neighbor : neighbors) {
                if (neighbor.staticFields.get(5) <= min) {
                    min = neighbor.staticFields.get(5);
                }
                // REPULSION FORCE BETWEEN THE WALLS OR REPULSION IN DIFFERENT PASSAGES
            }
            if (staticFields.get(5) > min + 1) {
                staticFields.set(5, min + 1);
                return true;
            }
            return false;
        }
        else if (type == 6) {
            int min = SFMAX;
            for (Point neighbor : neighbors) {
                if (neighbor.staticFields.get(6) <= min) {
                    min = neighbor.staticFields.get(6);
                }
                // REPULSION FORCE BETWEEN THE WALLS OR REPULSION IN DIFFERENT PASSAGES
            }
            if (staticFields.get(6) > min + 1) {
                staticFields.set(6, min + 1);
                return true;
            }
            return false;
        }
        else if (type == 7) {
            int min = SFMAX;
            for (Point neighbor : neighbors) {
                if (neighbor.staticFields.get(7) <= min) {
                    min = neighbor.staticFields.get(7);
                }
                // REPULSION FORCE BETWEEN THE WALLS OR REPULSION IN DIFFERENT PASSAGES
            }
            if (staticFields.get(7) > min + 1) {
                staticFields.set(7, min + 1);
                return true;
            }
            return false;
        }
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
                ArrayList<Point> possible = new ArrayList<>();
                for (Point neighbor : neighbors) {
                    if (neighbor.staticFields.get(0) < min && neighbor.pedestrians.size()<5 && neighbor.isSidewalk) {
                        min = neighbor.staticFields.get(0);
                        possible.clear();
                        possible.add(neighbor);
                    } else if (neighbor.staticFields.get(0) == min && neighbor.pedestrians.size()<5 && neighbor.isSidewalk) {
                        possible.add(neighbor);
                    }
                }
                if (possible.size() > 0) {
                    Point next = possible.get(0);
                    if (next.isExit) {
                        pedestrian.toRemove = true;
                    } else {
                        pedestrian.toRemove = true;
                        pedestrian.iteration = pedestrian.getTimeToMove();
                        next.pedestrians.add(pedestrian);
                        pedestrian.moved = true;
                    }
                }
                continue;
            }
            if (pedestrian.getExit() == 2) {
                int min = SFMAX;
                ArrayList<Point> possible = new ArrayList<>();
                for (Point neighbor : neighbors) {
                    if (neighbor.staticFields.get(1) < min && neighbor.pedestrians.size()<5 && neighbor.isSidewalk) {
                        min = neighbor.staticFields.get(1);
                        possible.clear();
                        possible.add(neighbor);
                    } else if (neighbor.staticFields.get(1) == min && neighbor.pedestrians.size()<5 && neighbor.isSidewalk) {
                        possible.add(neighbor);
                    }
                }
                if (possible.size() > 0) {
                    Random random = new Random();
                    //Vehicles next = possible.get(random.nextInt(possible.size()));
                    Point next = possible.get(0);
                    if (next.isExit) {
                        pedestrian.toRemove = true;
                    } else {
                        pedestrian.toRemove = true;
                        pedestrian.iteration = pedestrian.getTimeToMove();
                        next.pedestrians.add(pedestrian);
                        pedestrian.moved = true;
                    }
                }
                continue;
            }
            if (pedestrian.getExit() == 3) {
                int min = SFMAX;
                ArrayList<Point> possible = new ArrayList<>();
                for (Point neighbor : neighbors) {
                    if (neighbor.staticFields.get(2) < min && neighbor.pedestrians.size()<5 && neighbor.isSidewalk) {
                        min = neighbor.staticFields.get(2);
                        possible.clear();
                        possible.add(neighbor);
                    } else if (neighbor.staticFields.get(2) == min && neighbor.pedestrians.size()<5 && neighbor.isSidewalk) {
                        possible.add(neighbor);
                    }
                }
                if (possible.size() > 0) {
                    Random random = new Random();
                    //Vehicles next = possible.get(random.nextInt(possible.size()));
                    Point next = possible.get(0);
                    for (Point element : possible) {
                        if (element.equals(downNeighbor)) {
                            next = element;
                            break;
                        }
                    }
                    if (next.isExit) {
                        pedestrian.toRemove = true;
                    } else {
                        pedestrian.toRemove = true;
                        pedestrian.iteration = pedestrian.getTimeToMove();
                        next.pedestrians.add(pedestrian);
                        pedestrian.moved = true;
                    }
                }
                continue;
            }
            if (pedestrian.getExit() == 4) {
                int min = SFMAX;
                ArrayList<Point> possible = new ArrayList<>();
                for (Point neighbor : neighbors) {
                    if (neighbor.staticFields.get(3) < min && neighbor.pedestrians.size()<5 && neighbor.isSidewalk) {
                        min = neighbor.staticFields.get(3);
                        possible.clear();
                        possible.add(neighbor);
                    } else if (neighbor.staticFields.get(3) == min && neighbor.pedestrians.size()<5 && neighbor.isSidewalk) {
                        possible.add(neighbor);
                    }
                }
                if (possible.size() > 0) {
                    Random random = new Random();
                    //Vehicles next = possible.get(random.nextInt(possible.size()));
                    Point next = possible.get(0);
                    if (next.isExit) {
                        pedestrian.toRemove = true;
                    } else {
                        pedestrian.toRemove = true;
                        pedestrian.iteration = pedestrian.getTimeToMove();
                        next.pedestrians.add(pedestrian);
                        pedestrian.moved = true;
                    }
                }
                continue;
            }
            if (pedestrian.getExit() == 5) {
                int min = SFMAX;
                ArrayList<Point> possible = new ArrayList<>();
                for (Point neighbor : neighbors) {
                    if (neighbor.staticFields.get(4) < min && neighbor.pedestrians.size()<5 && neighbor.isSidewalk) {
                        min = neighbor.staticFields.get(4);
                        possible.clear();
                        possible.add(neighbor);
                    } else if (neighbor.staticFields.get(4) == min && neighbor.pedestrians.size()<5 && neighbor.isSidewalk) {
                        possible.add(neighbor);
                    }
                }
                if (possible.size() > 0) {
                    Random random = new Random();
                    //Vehicles next = possible.get(random.nextInt(possible.size()));
                    Point next = possible.get(0);
                    if (next.isExit) {
                        pedestrian.toRemove = true;
                    } else {
                        pedestrian.toRemove = true;
                        pedestrian.iteration = pedestrian.getTimeToMove();
                        next.pedestrians.add(pedestrian);
                        pedestrian.moved = true;
                    }
                }
                continue;
            }
            if (pedestrian.getExit() == 6) {
                int min = SFMAX;
                ArrayList<Point> possible = new ArrayList<>();
                for (Point neighbor : neighbors) {
                    if (neighbor.staticFields.get(5) < min && neighbor.pedestrians.size()<5 && neighbor.isSidewalk) {
                        min = neighbor.staticFields.get(5);
                        possible.clear();
                        possible.clear();
                        possible.add(neighbor);
                    } else if (neighbor.staticFields.get(5) == min && neighbor.pedestrians.size()<5 && neighbor.isSidewalk) {
                        possible.add(neighbor);
                    }
                }
                if (possible.size() > 0) {
                    Random random = new Random();
                    //Vehicles next = possible.get(random.nextInt(possible.size()));
                    Point next = possible.get(0);
                    if (next.isExit) {
                        pedestrian.toRemove = true;
                    } else {
                        pedestrian.toRemove = true;
                        pedestrian.iteration = pedestrian.getTimeToMove();
                        next.pedestrians.add(pedestrian);
                        pedestrian.moved = true;
                    }
                }
                continue;
            }
            if (pedestrian.getExit() == 7) {
                int min = SFMAX;
                ArrayList<Point> possible = new ArrayList<>();
                for (Point neighbor : neighbors) {
                    if (neighbor.staticFields.get(6) < min && neighbor.pedestrians.size()<5 && neighbor.isSidewalk) {
                        min = neighbor.staticFields.get(6);
                        possible.clear();
                        possible.add(neighbor);
                    } else if (neighbor.staticFields.get(6) == min && neighbor.pedestrians.size()<5 && neighbor.isSidewalk) {
                        possible.add(neighbor);
                    }
                }
                if (possible.size() > 0) {
                    Random random = new Random();
                    //Vehicles next = possible.get(random.nextInt(possible.size()));
                    Point next = possible.get(0);
                    if (next.isExit) {
                        pedestrian.toRemove = true;
                    } else {
                        pedestrian.toRemove = true;
                        pedestrian.iteration = pedestrian.getTimeToMove();
                        next.pedestrians.add(pedestrian);
                        pedestrian.moved = true;
                    }
                }
                continue;
            }
            if (pedestrian.getExit() == 8) {
                int min = SFMAX;
                ArrayList<Point> possible = new ArrayList<>();
                for (Point neighbor : neighbors) {
                    if (neighbor.staticFields.get(7) < min && neighbor.pedestrians.size()<5 && neighbor.isSidewalk) {
                        min = neighbor.staticFields.get(7);
                        possible.clear();
                        possible.add(neighbor);
                    } else if (neighbor.staticFields.get(7) == min && neighbor.pedestrians.size()<5 && neighbor.isSidewalk) {
                        possible.add(neighbor);
                    }
                }
                if (possible.size() > 0) {
                    Random random = new Random();
                    //Vehicles next = possible.get(random.nextInt(possible.size()));
                    Point next = possible.get(0);
                    if (next.isExit) {
                        pedestrian.toRemove = true;
                    } else {
                        pedestrian.toRemove = true;
                        pedestrian.iteration = pedestrian.getTimeToMove();
                        next.pedestrians.add(pedestrian);
                        pedestrian.moved = true;
                    }
                }
                continue;
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
    public void addNeighbor(Point nei) {
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
