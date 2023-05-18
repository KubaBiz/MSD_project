package org.project;

import static java.lang.Math.random;

public class Point {
	public final int maxSpeed = 5;
	//public Point nNeighbor;
	//public Point wNeighbor;
	//public Point eNeighbor;
	//public Point sNeighbor;
	//public float nVel;
	//public float eVel;
	//public float wVel;
	//public float sVel;
	public int type;
	public int velocity;
	public boolean moved;

	public Point() {
		clear();
	}

	public void clicked() {
		type = 1;
	}
	
	public void clear() {
		type = 0;
		velocity = 0;
	}

	public void accelerate() {
		if(type == 1 && velocity<maxSpeed) {
			velocity+=1;
		}
	}

	public void slowingDown(int distance) {
		if(type == 1) {
			velocity=distance;
		}
	}

	public void randomization(){
		if(type == 1){
			int random = (int)(random() * 2);
			if(velocity > 0 && random == 1){
				velocity-=1;
			}
		}
	}

	public void move(int vel){
		type = 1;
		velocity = vel;
		moved = true;
	}
	public int getType() {
		return type;
	}

	public int getVelocity(){return velocity;}
}