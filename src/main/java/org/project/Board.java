package org.project;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import static java.lang.Math.random;

public class Board extends JComponent implements MouseInputListener, ComponentListener {
	private static final long serialVersionUID = 1L;
	private Vehicles[][] points;
	private List<Vector2d>[][] directions;
	private Boolean[][] blocked;
	Generator generator1;
	Generator generator4;
	Generator generator5;
	Generator generator10;
	Generator generator11;
	Generator generator14;

	ArrayList<PedestrianGenerator> pedestrianGenerators = new ArrayList<>();
	private List<Vector2d> street1 = new ArrayList<>();
	private List<Vector2d> street2 = new ArrayList<>();
	private List<Vector2d> street3 = new ArrayList<>();
	private List<Vector2d> street4 = new ArrayList<>();
	private List<Vector2d> street5 = new ArrayList<>();
	private List<Vector2d> street6 = new ArrayList<>();
	private List<Vector2d> street7 = new ArrayList<>();
	private List<Vector2d> street8 = new ArrayList<>();
	private List<Vector2d> street9 = new ArrayList<>();
	private List<Vector2d> street10 = new ArrayList<>();
	private List<Vector2d> street11 = new ArrayList<>();
	private List<Vector2d> street12 = new ArrayList<>();
	private List<Vector2d> street13 = new ArrayList<>();
	private List<Vector2d> street14 = new ArrayList<>();
	private List<Vector2d> leftCrossroads = new ArrayList<>();
	private List<Vector2d> rightCrossroads = new ArrayList<>();
	private List<Vector2d> reduceMaxSpeedVectors = new ArrayList<>();
	private List<Vector2d> IncreaseMaxSpeedPoints = new ArrayList<>();
	private int time = 0;

	//private int length;
	//private int height;
	//private Point[][] points;
	private int size = 12;
	Vector2d start;

	public static Integer []types ={0,1,2,3,4,5,6,7,8,9};
	public int editType = 0;


	public Board(int length, int height) {
		initialize(length, height);
		addMouseListener(this);
		addComponentListener(this);
		addMouseMotionListener(this);
		setBackground(Color.WHITE);
		setOpaque(true);
	}

	public int carInFront(int x, int y, int maxSpeed){
		int sum = 0;
		int i = 0;
		int iter;
		int newX = x;
		int newY = y;
		while(i < maxSpeed+1 && directions[x][y].size() > 0){
			if(directions[newX][newY].size() > 1){
				iter = points[x][y].getDestination() % 10;

			}
			else{
				iter = 0;

			}
			newX = directions[x][y].get(iter).getX();
			newY = directions[x][y].get(iter).getY();


			if(!blocked[newX][newY]){
				sum++;
				x = newX;
				y = newY;
				i++;
			}
			else return sum;
		}
		return sum;
	}

	public void moveOnStreet(List<Vector2d> streetName){
		for(int i = 0; i < streetName.size(); i ++){
			Vector2d vector = streetName.get(i);
			if(points[vector.getX()][vector.getY()].getLength() > 0 && !points[vector.getX()][vector.getY()].moved
					&& points[vector.getX()][vector.getY()].getSpeed() > 0)
			{
				speedChanges(vector.getX(), vector.getY());
				moveVehicles(vector.getX(), vector.getY());}
		}
	}


	public void addVehicle(Generator generator, int frequency){
		if(time % frequency == 0){
			Vehicles vehicle = generator.generateVehicle();
			points[generator.getPosition().getX()][generator.getPosition().getY()] = vehicle;
			blocked[generator.getPosition().getX()][generator.getPosition().getY()] = true;
		}
	}
	public void speedChanges(int x, int y){
		int obstacle = carInFront(x,y,points[x][y].getMaxSpeed());
		points[x][y].speedBoost();
		points[x][y].speedReduction(obstacle);

	}

	public void setBlocked(int x,int y, boolean value){
		if(points[x][y].getLength() > 1){
			for (int j = 0; j < points[x][y].getTail().length; j++){
				blocked[points[x][y].getTail()[j].getX()][points[x][y].getTail()[j].getY()] = value;
			}
		}
	}
	public void moveVehicles(int x, int y) {
		int speed = points[x][y].getSpeed();
		int i = 0;
		int iter;
		Vector2d vector = new Vector2d(x, y);

		setBlocked(x,y,false);

		while (i < speed && directions[vector.getX()][vector.getY()].size() > 0) {
			if(directions[vector.getX()][vector.getY()].size() > 1){
				iter = points[x][y].getDestination() % 10;
				points[x][y].setDestination();

			}
			else{
				iter = 0;
			}
			points[x][y].moveTail(vector);
			vector = directions[vector.getX()][vector.getY()].get(iter);

			i++;
		}

		points[vector.getX()][vector.getY()].copyVehicleToAnotherTile(points[x][y]);
		blocked[vector.getX()][vector.getY()] = true;
		points[vector.getX()][vector.getY()].moved = true;
		points[vector.getX()][vector.getY()].setPosition(vector);

		clearVehicle(x, y);
		setBlocked(vector.getX(),vector.getY(),true);

	}

	public void enteringTheLeftCrossroads(Vector2d vector){

		Vector2d right = new Vector2d(0,0);

		if (vector.equals(new Vector2d(11,19))){
			right = new Vector2d(12,17);
		}
		else if (vector.equals(new Vector2d(12,17))){
			right = new Vector2d(10,16);
		}
		else if (vector.equals(new Vector2d(10,16))){
			right = new Vector2d(9,18);
		}
		else if (vector.equals(new Vector2d(9,18))){
			right = new Vector2d(11,19);
			if(blocked[12][17] && points[vector.getX()][vector.getY()].getDestination() == 11)
				points[vector.getX()][vector.getY()].setSpeed(0);
		}

		if(blocked[right.getX()][right.getY()])
			points[vector.getX()][vector.getY()].setSpeed(0);
		if(points[vector.getX()][vector.getY()].getDestination() < points[right.getX()][right.getY()].getDestination()){
			points[vector.getX()][vector.getY()].setSpeed(1);
		}
		else if(points[vector.getX()][vector.getY()].getDestination() == points[right.getX()][right.getY()].getDestination() &&
				points[vector.getX()][vector.getY()].getDestination() == 0){
			points[vector.getX()][vector.getY()].setSpeed(1);
		}

		for(int i = 0; i < leftCrossroads.size(); i++){
			Vector2d tmp = leftCrossroads.get(i);
			if(blocked[tmp.getX()][tmp.getY()]){
				points[vector.getX()][vector.getY()].moved = true;
				break;
			}
		}

		if(vector.equals(new Vector2d(10,16)) && points[10][16].getDestination() == 11 && points[11][19].getDestination() == 11){
			points[10][16].setSpeed(0);
		}

		if(points[9][18].getSpeed() == points[11][19].getSpeed() &&  points[12][17].getSpeed() == points[10][16].getSpeed()
				&& points[9][18].getSpeed() == points[12][17].getSpeed() && points[12][17].getSpeed() == 0){
			points[12][17].setSpeed(1);
		}
	}

	public void enteringTheRightCrossroads(Vector2d vector){

		Vector2d right = new Vector2d(0,0);

		if (vector.equals(new Vector2d(68,18))){
			right = new Vector2d(70,19);
		}
		else if (vector.equals(new Vector2d(70,19))){
			right = new Vector2d(71,17);
		}
		else if (vector.equals(new Vector2d(69,16))){
			right = new Vector2d(68,18);
		}
		else if (vector.equals(new Vector2d(71,17))){
			right = new Vector2d(69,16);
			if(blocked[68][18] && points[vector.getX()][vector.getY()].getDestination() == 11)
				points[vector.getX()][vector.getY()].setSpeed(0);
		}

		if(blocked[right.getX()][right.getY()])
			points[vector.getX()][vector.getY()].setSpeed(0);
		if(points[vector.getX()][vector.getY()].getDestination() < points[right.getX()][right.getY()].getDestination()){
			points[vector.getX()][vector.getY()].setSpeed(1);
		}
		else if(points[vector.getX()][vector.getY()].getDestination() == points[right.getX()][right.getY()].getDestination() &&
				points[vector.getX()][vector.getY()].getDestination() == 0){
			points[vector.getX()][vector.getY()].setSpeed(1);
		}

		for(int i = 0; i < rightCrossroads.size(); i++){
			Vector2d tmp = rightCrossroads.get(i);
			if(blocked[tmp.getX()][tmp.getY()]){
				points[vector.getX()][vector.getY()].moved = true;
				break;
			}
		}

		if(vector.equals(new Vector2d(70,19)) && points[70][19].getDestination() == 11 && points[69][16].getDestination() == 11){
			points[70][19].setSpeed(0);
		}

		if(points[68][18].getSpeed() == points[70][19].getSpeed() &&  points[71][17].getSpeed() == points[69][16].getSpeed()
				&& points[68][18].getSpeed() == points[71][17].getSpeed() && points[71][17].getSpeed() == 0){
			points[68][18].setSpeed(1);
		}
	}

	//single iteration
	public void iteration() {
		// RUCH PIESZYCH
		for (int x = 1; x < points.length-1; ++x) {
			for (int y = 1; y < points[x].length - 1; ++y) {
				for (int z = 0; z < points[x][y].pedestrians.size(); z++){
					points[x][y].pedestrians.get(z).moved=false;
				}
			}
		}
		for (int x = 1; x < points.length-1; ++x) {
			for ( int y = 1; y < points[x].length-1; ++y){
				points[x][y].movePedestrians();
				if (points[x][y].pedestrians.size() > 0){
					blocked[x][y] = true;
				}
			}
		}

		for (int x = 1; x < points.length-1; ++x) {
			for (int y = 1; y < points[x].length - 1; ++y) {
				if (points[x][y].getLength() == 0 && points[x][y].pedestrians.size() == 0){     //dokładnie ta linijka psuje
					blocked[x][y] = false;
				}
			}
		}
		//

		// ruch aut
		for (int x = 0; x < points.length; ++x) {
			for (int y = 0; y < points[x].length; ++y) {
				if(points[x][y].getLength() > 1 && points[x][y].getLength() <4 ){
					setBlocked(x,y,true);
				}
			}
		}

		for (int x = 0; x < points.length; ++x) {
			for ( int y = 0; y < points[x].length; ++y){
				if(points[x][y].getLength() > 0 ){
					points[x][y].moved = false;
					speedChanges(x,y);
				}
			}
		}

		for(int i = 0; i < reduceMaxSpeedVectors.size(); i++){
			Vector2d vector = reduceMaxSpeedVectors.get(i);
			if(points[vector.getX()][vector.getY()].getLength() > 1){
				points[vector.getX()][vector.getY()].setMaxSpeed(1);
			}
		}

		for(int i = 0; i < IncreaseMaxSpeedPoints.size(); i++){
			Vector2d vector = IncreaseMaxSpeedPoints.get(i);
			switch (points[vector.getX()][vector.getY()].getLength()) {
				case 2:
					points[vector.getX()][vector.getY()].setMaxSpeed(2);
					break;
				case 3:
					points[vector.getX()][vector.getY()].setMaxSpeed(2);
					break;
				default:
					break;
			}
		}

		enteringTheLeftCrossroads(new Vector2d(9, 18));
		enteringTheLeftCrossroads(new Vector2d(11, 19));
		enteringTheLeftCrossroads(new Vector2d(12, 17));
		enteringTheLeftCrossroads(new Vector2d(10, 16));

		enteringTheRightCrossroads(new Vector2d(68, 18));
		enteringTheRightCrossroads(new Vector2d(70, 19));
		enteringTheRightCrossroads(new Vector2d(71, 17));
		enteringTheRightCrossroads(new Vector2d(69, 16));

		moveOnStreet(street1);
		moveOnStreet(street2);
		moveOnStreet(street3);
		moveOnStreet(street4);
		moveOnStreet(street5);
		moveOnStreet(street6);
		moveOnStreet(street7);
		moveOnStreet(street8);
		moveOnStreet(street9);
		moveOnStreet(street10);
		moveOnStreet(street11);
		moveOnStreet(street12);
		moveOnStreet(street13);
		moveOnStreet(street14);
		moveOnStreet(leftCrossroads);
		moveOnStreet(rightCrossroads);

		addVehicle(generator1, 11);
		addVehicle(generator5, 13);
		addVehicle(generator10, 15);
		addVehicle(generator4, 14);
		addVehicle(generator14, 29);
		addVehicle(generator11, 15);

		clearVehicle(10, 35);
		clearVehicle(69,35);
		clearVehicle(75, 18);
		clearVehicle(70, 2);
		clearVehicle(11, 2);
		clearVehicle(2, 2);

		points[68][18].addNewDestination();
		points[12][17].addNewDestination();

		for (PedestrianGenerator generator: pedestrianGenerators){
			generator.generate();
		}

		time++;
		repaint();
	}

	public void clearVehicle(int x,int y){
		if(points[x][y].getLength() > 1){
			for(int i = 0 ; i < points[x][y].getTail().length; i ++){
				blocked[points[x][y].getTail()[i].getX()][points[x][y].getTail()[i].getY()] = false;
			}
		}
		points[x][y].setLength(0);
		points[x][y].setSpeed(0);
		points[x][y].setMaxSpeed(0);
		points[x][y].setAcceleration(0);
		points[x][y].setDeceleration(0);
		points[x][y].addNewDestination(0);
		points[x][y].setPosition(new Vector2d(-1,-1));
		points[x][y].setTail(); // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		blocked[x][y] = false;
	}

	// clearing board
	public void clear() {
		for (int x = 0; x < points.length; ++x)
			for (int y = 0; y < points[x].length; ++y) {
				clearVehicle(x, y);
				points[x][y].clearPedestrians();
			}

		calculateFirstField();
		calculateSecondField();
		calculateThirdField();
		calculateFourthField();
		calculateFifthField();
		calculateSixthField();
		calculateSeventhField();
		calculateEighthField();
		this.repaint();
	}

	private void initialize(int length, int height) {
		points = new Vehicles[length][height];
		directions = new List[length][height];
		blocked = new Boolean[length][height];

		generator1 = new Generator(new Vector2d(2,3),new Vector2d(-1,0));
		generator4 = new Generator(new Vector2d(11,35),new Vector2d(-1,0));
		generator5 = new Generator(new Vector2d(10,2),new Vector2d(-1,0));
		generator10 = new Generator(new Vector2d(70, 35),new Vector2d(-1,0));
		generator11 = new Generator(new Vector2d(69, 2),new Vector2d(-1,0));
		generator14 = new Generator(new Vector2d(75, 17),new Vector2d(-1,0));

		for (int x = 0; x < points.length; ++x)
			for (int y = 0; y < points[x].length; ++y){
				points[x][y] = new Vehicles(0,0,0,0,
						new Vector2d(-1,-1),new Vector2d(0,0));
				directions[x][y] = new ArrayList<>();
				blocked[x][y] = false;
			}

		directions[10][18].add(new Vector2d(10,19));
		directions[10][18].add(new Vector2d(11,18));

		directions[11][18].add(new Vector2d(12,18));
		directions[11][18].add(new Vector2d(11,17));

		directions[11][17].add(new Vector2d(11,16));
		directions[11][17].add(new Vector2d(10,17));

		directions[10][17].add(new Vector2d(9,17));
		directions[10][17].add(new Vector2d(10,18));

		directions[69][18].add(new Vector2d(69,19));
		directions[69][18].add(new Vector2d(70,18));

		directions[70][18].add(new Vector2d(71,18));
		directions[70][18].add(new Vector2d(70,17));

		directions[70][17].add(new Vector2d(70,16));
		directions[70][17].add(new Vector2d(69,17));

		directions[69][17].add(new Vector2d(68,17));
		directions[69][17].add(new Vector2d(69,18));

		leftCrossroads.add(new Vector2d(10,18));
		leftCrossroads.add(new Vector2d(11,18));
		leftCrossroads.add(new Vector2d(11,17));
		leftCrossroads.add(new Vector2d(10,17));
		rightCrossroads.add(new Vector2d(69,18));
		rightCrossroads.add(new Vector2d(70,18));
		rightCrossroads.add(new Vector2d(70,17));
		rightCrossroads.add(new Vector2d(69,17));

		//VEHICLES
		String localization = System.getProperty("user.dir");
		readFile(localization + "\\src\\main\\java\\org\\project\\street1.txt", street1);
		readFile(localization + "\\src\\main\\java\\org\\project\\street2.txt", street2);
		readFile(localization + "\\src\\main\\java\\org\\project\\street3.txt", street3);
		readFile(localization + "\\src\\main\\java\\org\\project\\street4.txt", street4);
		readFile(localization + "\\src\\main\\java\\org\\project\\street5.txt", street5);
		readFile(localization + "\\src\\main\\java\\org\\project\\street6.txt", street6);
		readFile(localization + "\\src\\main\\java\\org\\project\\street7.txt", street7);
		readFile(localization + "\\src\\main\\java\\org\\project\\street8.txt", street8);
		readFile(localization + "\\src\\main\\java\\org\\project\\street9.txt", street9);
		readFile(localization + "\\src\\main\\java\\org\\project\\street10.txt", street10);
		readFile(localization + "\\src\\main\\java\\org\\project\\street11.txt", street11);
		readFile(localization + "\\src\\main\\java\\org\\project\\street12.txt", street12);
		readFile(localization + "\\src\\main\\java\\org\\project\\street13.txt", street13);
		readFile(localization + "\\src\\main\\java\\org\\project\\street14.txt", street14);
		readFileWithoutDirections(localization + "\\src\\main\\java\\org\\project\\ReduceMaxSpeedPoints.txt",
				reduceMaxSpeedVectors);
		readFileWithoutDirections(localization + "\\src\\main\\java\\org\\project\\IncreaseMaxSpeedPoints.txt",
				IncreaseMaxSpeedPoints);



		//PEDESTRIANS
		for (int x = 1; x < points.length-1; ++x) {
			for (int y = 1; y < points[x].length-1; ++y) {
				points[x][y].addNeighbor(points[x][y+1]);
				points[x][y].downNeighbor = points[x][y+1];
				points[x][y].addNeighbor(points[x+1][y]);
				points[x][y].rightNeighbor = points[x+1][y];
				points[x][y].addNeighbor(points[x][y-1]);
				points[x][y].upNeighbor = points[x][y-1];
				points[x][y].addNeighbor(points[x-1][y]);
				points[x][y].leftNeighbor = points[x-1][y];
				points[x][y].addNeighbor(points[x+1][y+1]);
				points[x][y].addNeighbor(points[x-1][y-1]);
				points[x][y].addNeighbor(points[x-1][y+1]);
				points[x][y].addNeighbor(points[x+1][y-1]);
			}
		}
		//

		String borderPath = "\\src\\main\\java\\org\\project\\borders.txt";
		String buildingsPath = "\\src\\main\\java\\org\\project\\buildings.txt";

		String border = localization + borderPath;
		String buildings = localization + buildingsPath;
		String sidewalks = localization + "\\src\\main\\java\\org\\project\\sidewalks.txt";

		drawFromFile(border, 9, 0);
		drawFromFile(buildings, 8, 0);
		drawFromFile(sidewalks, 0, 1);

		pedestrianGenerators.add(new PedestrianGenerator(new int[]{25,0,0,0},new int[]{10,10,10,10,10,10,10},points[9][2]));
		pedestrianGenerators.add(new PedestrianGenerator(new int[]{25,0,0,0},new int[]{10,10,10,10,10,10,10},points[2][20]));
		pedestrianGenerators.add(new PedestrianGenerator(new int[]{25,0,0,0},new int[]{10,10,10,10,10,10,10},points[7][35]));
		pedestrianGenerators.add(new PedestrianGenerator(new int[]{25,0,0,0},new int[]{10,10,10,10,10,10,10},points[12][35]));
		pedestrianGenerators.add(new PedestrianGenerator(new int[]{25,0,0,0},new int[]{10,10,10,10,10,10,10},points[66][2]));
		pedestrianGenerators.add(new PedestrianGenerator(new int[]{25,0,0,0},new int[]{10,10,10,10,10,10,10},points[73][2]));
		pedestrianGenerators.add(new PedestrianGenerator(new int[]{25,0,0,0},new int[]{10,10,10,10,10,10,10},points[75][16]));
		pedestrianGenerators.add(new PedestrianGenerator(new int[]{25,0,0,0},new int[]{10,10,10,10,10,10,10},points[75][19]));
		pedestrianGenerators.add(new PedestrianGenerator(new int[]{25,0,0,0},new int[]{10,10,10,10,10,10,10},points[73][35]));

		points[9][1].which_exit = 1; // 0 na rysunku piesi.png
		points[9][1].isExit = true;
		points[7][36].which_exit = 2; // 1 na rysunku piesi.png
		points[7][36].isExit = true;
		points[12][36].which_exit = 3; // 2 na rysunku piesi.png
		points[12][36].isExit = true;
		points[66][1].which_exit = 4; // 3 na rysunku piesi.png
		points[66][1].isExit = true;
		points[73][1].which_exit = 5; // 4 na rysunku piesi.png
		points[73][1].isExit = true;
		points[76][16].which_exit = 6; // 5 na rysunku piesi.png
		points[76][16].isExit = true;
		points[76][19].which_exit = 7; // 6 na rysunku piesi.png
		points[76][19].isExit = true;
		points[73][36].which_exit = 8; // 7 na rysunku piesi.png
		points[73][36].isExit = true;

		calculateFirstField();
		calculateSecondField();
		calculateThirdField();
		calculateFourthField();
		calculateFifthField();
		calculateSixthField();
		calculateSeventhField();
		calculateEighthField();
	}

	public void readFile(String fileName, List<Vector2d> list) {
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			String line;
			Vector2d prev = new Vector2d(-1,-1);
			while ((line = br.readLine()) != null) {
				String[] numbers = line.split(" ");
				int x = Integer.parseInt(numbers[0]);
				int y = Integer.parseInt(numbers[1]);
				if(!prev.equals(new Vector2d(-1,-1))){
					directions[prev.getX()][prev.getY()].add(new Vector2d(x,y));
					list.add(prev);
				}
				prev = new Vector2d(x,y);
			}
		} catch (IOException e) {
			System.err.println("Błąd odczytu pliku: " + e.getMessage());
		}
	}

	public void readFileWithoutDirections(String fileName, List<Vector2d> list) {
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] numbers = line.split(" ");
				int x = Integer.parseInt(numbers[0]);
				int y = Integer.parseInt(numbers[1]);
				list.add(new Vector2d(x,y));
			}
		} catch (IOException e) {
			System.err.println("Błąd odczytu pliku: " + e.getMessage());
		}
	}

	public void drawFromFile(String fileName, int typetoSet, int mode){
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] numbers = line.split(" ");
				int x = Integer.parseInt(numbers[0]);
				int y = Integer.parseInt(numbers[1]);
				if (mode == 0){
					points[x][y].setLength(typetoSet);
				}
				else if (mode == 1){
					points[x][y].isSidewalk = true;
				}
			}
		} catch (IOException e) {
			System.err.println("Błąd odczytu pliku: " + e.getMessage());
		}
	}

	//PEDESTRIANS
	public void calculateFirstField(){
		ArrayList<Vehicles> toCheck = new ArrayList<>();
		for (int x = 1; x < points.length-1; ++x){
			for (int y = 1; y < points[x].length-1; ++y){
				if(points[x][y].isSidewalk && points[x][y].which_exit==1){
					points[x][y].staticFields.set(0, 0);
					toCheck.addAll(points[x][y].neighbors);
				}
			}
		}

		while(!toCheck.isEmpty()){
			// IMPROVED CALCULATION OF STATIC FLOOR FIELD
			Vehicles temp=toCheck.get(0);
			if(temp.isSidewalk && temp.calcStaticField(0)) {
				for (Vehicles neighbor : temp.neighbors) {
					if (neighbor.staticFields.get(0) > temp.staticFields.get(0)){
						toCheck.add(neighbor);
					}
				}
			}
			toCheck.remove(0);
		}
	}
	public void calculateSecondField(){
		ArrayList<Vehicles> toCheck = new ArrayList<>();
		for (int x = 1; x < points.length-1; ++x){
			for (int y = 1; y < points[x].length-1; ++y){
				if(points[x][y].isSidewalk && points[x][y].which_exit==2){
					points[x][y].staticFields.set(1, 0);
					toCheck.addAll(points[x][y].neighbors);
				}
			}
		}

		while(!toCheck.isEmpty()){
			// IMPROVED CALCULATION OF STATIC FLOOR FIELD
			Vehicles temp=toCheck.get(0);
			if(temp.isSidewalk && temp.calcStaticField(1)) {
				for (Vehicles neighbor : temp.neighbors) {
					if (neighbor.staticFields.get(1) > temp.staticFields.get(1)){
						toCheck.add(neighbor);
					}
				}
			}
			toCheck.remove(0);
		}
	}
	public void calculateThirdField(){
		ArrayList<Vehicles> toCheck = new ArrayList<>();
		for (int x = 1; x < points.length-1; ++x){
			for (int y = 1; y < points[x].length-1; ++y){
				if(points[x][y].isSidewalk && points[x][y].which_exit==3){
					points[x][y].staticFields.set(2, 0);
					toCheck.addAll(points[x][y].neighbors);
				}
			}
		}

		while(!toCheck.isEmpty()){
			// IMPROVED CALCULATION OF STATIC FLOOR FIELD
			Vehicles temp=toCheck.get(0);
			if(temp.isSidewalk && temp.calcStaticField(2)) {
				for (Vehicles neighbor : temp.neighbors) {
					if (neighbor.staticFields.get(2) > temp.staticFields.get(2)){
						toCheck.add(neighbor);
					}
				}
			}
			toCheck.remove(0);
		}
	}
	public void calculateFourthField(){
		ArrayList<Vehicles> toCheck = new ArrayList<>();
		for (int x = 1; x < points.length-1; ++x){
			for (int y = 1; y < points[x].length-1; ++y){
				if(points[x][y].isSidewalk && points[x][y].which_exit==4){
					points[x][y].staticFields.set(3, 0);
					toCheck.addAll(points[x][y].neighbors);
				}
			}
		}

		while(!toCheck.isEmpty()){
			// IMPROVED CALCULATION OF STATIC FLOOR FIELD
			Vehicles temp=toCheck.get(0);
			if(temp.isSidewalk && temp.calcStaticField(3)) {
				for (Vehicles neighbor : temp.neighbors) {
					if (neighbor.staticFields.get(3) > temp.staticFields.get(3)){
						toCheck.add(neighbor);
					}
				}
			}
			toCheck.remove(0);
		}
	}
	public void calculateFifthField(){
		ArrayList<Vehicles> toCheck = new ArrayList<>();
		for (int x = 1; x < points.length-1; ++x){
			for (int y = 1; y < points[x].length-1; ++y){
				if(points[x][y].isSidewalk && points[x][y].which_exit==5){
					points[x][y].staticFields.set(4, 0);
					toCheck.addAll(points[x][y].neighbors);
				}
			}
		}

		while(!toCheck.isEmpty()){
			// IMPROVED CALCULATION OF STATIC FLOOR FIELD
			Vehicles temp=toCheck.get(0);
			if(temp.isSidewalk && temp.calcStaticField(4)) {
				for (Vehicles neighbor : temp.neighbors) {
					if (neighbor.staticFields.get(4) > temp.staticFields.get(4)){
						toCheck.add(neighbor);
					}
				}
			}
			toCheck.remove(0);
		}
	}
	public void calculateSixthField(){
		ArrayList<Vehicles> toCheck = new ArrayList<>();
		for (int x = 1; x < points.length-1; ++x){
			for (int y = 1; y < points[x].length-1; ++y){
				if(points[x][y].isSidewalk && points[x][y].which_exit==6){
					points[x][y].staticFields.set(5, 0);
					toCheck.addAll(points[x][y].neighbors);
				}
			}
		}

		while(!toCheck.isEmpty()){
			// IMPROVED CALCULATION OF STATIC FLOOR FIELD
			Vehicles temp=toCheck.get(0);
			if(temp.isSidewalk && temp.calcStaticField(5)) {
				for (Vehicles neighbor : temp.neighbors) {
					if (neighbor.staticFields.get(5) > temp.staticFields.get(5)){
						toCheck.add(neighbor);
					}
				}
			}
			toCheck.remove(0);
		}
	}
	public void calculateSeventhField(){
		ArrayList<Vehicles> toCheck = new ArrayList<>();
		for (int x = 1; x < points.length-1; ++x){
			for (int y = 1; y < points[x].length-1; ++y){
				if(points[x][y].isSidewalk && points[x][y].which_exit==7){
					points[x][y].staticFields.set(6, 0);
					toCheck.addAll(points[x][y].neighbors);
				}
			}
		}

		while(!toCheck.isEmpty()){
			// IMPROVED CALCULATION OF STATIC FLOOR FIELD
			Vehicles temp=toCheck.get(0);
			if(temp.isSidewalk && temp.calcStaticField(6)) {
				for (Vehicles neighbor : temp.neighbors) {
					if (neighbor.staticFields.get(6) > temp.staticFields.get(6)){
						toCheck.add(neighbor);
					}
				}
			}
			toCheck.remove(0);
		}
	}
	public void calculateEighthField(){
		ArrayList<Vehicles> toCheck = new ArrayList<>();
		for (int x = 1; x < points.length-1; ++x){
			for (int y = 1; y < points[x].length-1; ++y){
				if(points[x][y].isSidewalk && points[x][y].which_exit==8){
					points[x][y].staticFields.set(7, 0);
					toCheck.addAll(points[x][y].neighbors);
				}
			}
		}

		while(!toCheck.isEmpty()){
			// IMPROVED CALCULATION OF STATIC FLOOR FIELD
			Vehicles temp=toCheck.get(0);
			if(temp.isSidewalk && temp.calcStaticField(7)) {
				for (Vehicles neighbor : temp.neighbors) {
					if (neighbor.staticFields.get(7) > temp.staticFields.get(7)){
						toCheck.add(neighbor);
					}
				}
			}
			toCheck.remove(0);
		}
	}
	//END OF PEDESTRIANS



	//paint background and separators between cells
	protected void paintComponent(Graphics g) {
		if (isOpaque()) {
			g.setColor(getBackground());
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
		}
		g.setColor(Color.GRAY);
		drawNetting(g, size);
	}

	private void drawRoads(Graphics g, int gridSpace,List<Vector2d> street){
		Vector2d vector;
		for(int i = 0; i < street.size(); i++){
			vector = street.get(i);
			g.setColor(new Color(0xD3D3D3));
			g.fillRect((vector.getX() * size) + 1, (vector.getY() * size) + 1, (size - 1), (size - 1));
		}
	}

	// draws the background netting
	private void drawNetting(Graphics g, int gridSpace) {
		Insets insets = getInsets();
		int firstX = insets.left;
		int firstY = insets.top;
		int lastX = this.getWidth() - insets.right;
		int lastY = this.getHeight() - insets.bottom;

		int x = firstX;
		while (x < lastX) {
			g.drawLine(x, firstY, x, lastY);
			x += gridSpace;
		}

		int y = firstY;
		while (y < lastY) {
			g.drawLine(firstX, y, lastX, y);
			y += gridSpace;
		}

		drawRoads(g,gridSpace,street1);
		drawRoads(g,gridSpace,street2);
		drawRoads(g,gridSpace,street3);
		drawRoads(g,gridSpace,street4);
		drawRoads(g,gridSpace,street5);
		drawRoads(g,gridSpace,street6);
		drawRoads(g,gridSpace,street7);
		drawRoads(g,gridSpace,street8);
		drawRoads(g,gridSpace,street9);
		drawRoads(g,gridSpace,street10);
		drawRoads(g,gridSpace,street11);
		drawRoads(g,gridSpace,street12);
		drawRoads(g,gridSpace,street13);
		drawRoads(g,gridSpace,street14);
		drawRoads(g,gridSpace,leftCrossroads);
		drawRoads(g,gridSpace,rightCrossroads);

		for (x = 0; x < points.length; ++x) {
			for (y = 0; y < points[x].length; ++y) {
				if(points[x][y].isSidewalk){
					g.setColor(new Color(0xAAAAAA));
					g.fillRect((x * size) + 1, (y * size) + 1, (size - 1), (size - 1));
					switch (points[x][y].pedestrians.size()){
						case 1 -> g.setColor(new Color(0xCCE5FF));
						case 2 -> g.setColor(new Color(0x99CCFF));
						case 3 -> g.setColor(new Color(0x6699FF));
						case 4 -> g.setColor(new Color(0x3366FF));
						case 5 -> g.setColor(new Color(0x0000FF));
					}
					g.fillRect((x * size) + 1, (y * size) + 1, (size - 1), (size - 1));
				}
				int type = points[x][y].getLength();
				if (type != 0) {
					switch (points[x][y].getLength()) {
						case 1 -> g.setColor(new Color(0xFFEA00));
						case 2 -> {switch (points[x][y].color){
							case 0 -> g.setColor(new Color(0xFFA40EF6, true));
							case 1 -> g.setColor(new Color(0xA45F0E));
							case 2 -> g.setColor(new Color(0x7BFF00));
							case 3 -> g.setColor(new Color(0x03D7B7));
							case 4 -> g.setColor(new Color(0xFFA40EF6, true));
							case 5 -> g.setColor(new Color(0x6B5D0A));
							case 6 -> g.setColor(new Color(0x980F6D));
							case 7 -> g.setColor(new Color(0xFF659A));
						}}
						case 3 -> g.setColor(new Color(0xff0000));
						case 5 -> g.setColor(new Color(0xff00ff));
						case 8 -> g.setColor(new Color(0x6F7DA1));
						case 9 -> g.setColor(new Color(0x000000));
					}
					g.fillRect((x * size) + 1, (y * size) + 1, (size - 1), (size - 1));
					if(type == 2 || type == 3){
						Vector2d[] tail = points[x][y].getTail();
						for(int j = 0; j < type- 1; j++ ){
							g.fillRect((tail[j].getX() * size) + 1, (tail[j].getY() * size) + 1, (size - 1), (size - 1));
						}
					}

				}
			}
		}

	}
    // 34 x 76
//	public void mouseClicked(MouseEvent e) {
//		int x = e.getX() / size;
//		int y = e.getY() / size;
//		if ((x < points.length) && (x > 0) && (y < points[x].length) && (y > 0)) {
//			if(editType<4) {
//				points[x][y].setLength(editType);
//				this.repaint();
//			}
//			if(editType == 5 && points[x][y].getLength() != editType){
//				String localization = System.getProperty("user.dir");
//				String str1 = "\\src\\main\\java\\org\\project\\sidewalks.txt";
//				String fileName = localization + str1;
//				try {
//					BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
//
//					writer.write(Integer.toString(x) + " " + Integer.toString(y));
//					writer.newLine();
//
//					writer.close();
//
//					System.out.println("Numbers have been added to the file.");
//				} catch (IOException exception) {
//					System.out.println("An error occurred: " + exception.getMessage());
//				}
//				points[x][y].setLength(editType);
//				this.repaint();
//			}
//		}
//	}


	public void mouseClicked(MouseEvent e) {
		int x = e.getX() / size;
		int y = e.getY() / size;
		if ((x < points.length) && (x > 0) && (y < points[x].length) && (y > 0)) {
			if(editType<4) {
				points[x][y].setLength(editType);
				System.out.println(String.valueOf(x) + " " + String.valueOf(y));
				this.repaint();
			}
			if(editType == 5 && points[x][y].getLength() != editType){
				String localization = System.getProperty("user.dir");
				String str1 = "\\src\\main\\java\\org\\project\\sidewalks.txt";
				String fileName = localization + str1;
				try {
					BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));

					writer.write(Integer.toString(x) + " " + Integer.toString(y));
					writer.newLine();

					writer.close();

					System.out.println("Numbers have been added to the file.");
				} catch (IOException exception) {
					System.out.println("An error occurred: " + exception.getMessage());
				}
				points[x][y].setLength(editType);
				this.repaint();
			}

		}
	}
	public void componentResized(ComponentEvent e) {
		int dlugosc = (this.getWidth() / size) + 1;
		int wysokosc = (this.getHeight() / size) + 1;
		initialize(dlugosc, wysokosc);
	}

	public void mouseDragged(MouseEvent e) {
		int x = e.getX() / size;
		int y = e.getY() / size;
		if ((x < points.length) && (x > 0) && (y < points[x].length) && (y > 0)) {
			if(editType == 5 && points[x][y].getLength() != editType){
				String localization = System.getProperty("user.dir");
				String str1 = "\\src\\main\\java\\org\\project\\sidewalks.txt";
				String fileName = localization + str1;
				try {
					BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));

					writer.write(Integer.toString(x) + " " + Integer.toString(y));
					writer.newLine();

					writer.close();

					System.out.println("Numbers have been added to the file.");
				} catch (IOException exception) {
					System.out.println("An error occurred: " + exception.getMessage());
				}
				points[x][y].setLength(editType);
				this.repaint();
			}
			this.repaint();
		}
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void componentShown(ComponentEvent e) {
	}

	public void componentMoved(ComponentEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseMoved(MouseEvent e) {
	}

	public void componentHidden(ComponentEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

}
