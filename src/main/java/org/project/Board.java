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
	Generator generator3;
	Generator generator10;
	Generator generator11;
	Generator generator14;
	private List<Vector2d> moveDownList = new ArrayList<>();
	private List<Vector2d> moveUpList = new ArrayList<>();
	private List<Vector2d> moveStreet10 = new ArrayList<>();
	private List<Vector2d> moveStreet11 = new ArrayList<>();
	private List<Vector2d> moveStreet14 = new ArrayList<>();
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

	public void moveVehicles(int x, int y, Vector2d vector){
		points[vector.getX()][vector.getY()] = points[x][y];
		points[x][y].setPosition(vector);
		blocked[vector.getX()][vector.getY()] = true;
		points[vector.getX()][vector.getY()].moved = true;
		points[x][y] = new Vehicles(0,0,0,0,
				new Vector2d(-1,-1),new Vector2d(0,0));
		blocked[x][y] = false;
	}

	public void moveCarTemp(int x, int y){
		Vector2d vector;

		if(directions[x][y].size() > 0){
			vector = directions[x][y].get(0);
			if(directions[vector.getX()][vector.getY()].size() > 0){
				vector = directions[vector.getX()][vector.getY()].get(0);
				moveVehicles(x,y,vector);
			}
		}
	}

	//single iteration
	public void iteration() {
		for (int x = 0; x < points.length; ++x) {
			for ( int y = 0; y < points[x].length; ++y){
				if(blocked[x][y]) points[x][y].moved = false;
			}
		}

		// ruch na dół
		int start = moveDownList.size();
		for (int i = start - 1; i >= 0 ; --i) {
			int x = moveDownList.get(i).getX();
			int y = moveDownList.get(i).getY();
			if(points[x][y].getLength() > 0 && !points[x][y].moved) moveCarTemp(x,y);
		}

		// ruch na góre
		start = moveUpList.size();
		for (int i = start - 1; i >= 0 ; --i) {
			int x = moveUpList.get(i).getX();
			int y = moveUpList.get(i).getY();
			if(points[x][y].getLength() > 0 && !points[x][y].moved) moveCarTemp(x,y);
		}

		//temporary, jak chcesz to do zmiany?
		start = moveStreet10.size();
		for (int i = start - 1; i >= 0 ; --i) {
			int x = moveStreet10.get(i).getX();
			int y = moveStreet10.get(i).getY();
			if(points[x][y].getLength() > 0 && !points[x][y].moved) moveCarTemp(x,y);
		}
		start = moveStreet11.size();
		for (int i = start - 1; i >= 0 ; --i) {
			int x = moveStreet11.get(i).getX();
			int y = moveStreet11.get(i).getY();
			if(points[x][y].getLength() > 0 && !points[x][y].moved) moveCarTemp(x,y);
		}
		start = moveStreet14.size();
		for (int i = start - 1; i >= 0 ; --i) {
			int x = moveStreet14.get(i).getX();
			int y = moveStreet14.get(i).getY();
			if(points[x][y].getLength() > 0 && !points[x][y].moved) moveCarTemp(x,y);
		}

		if(!blocked[generator10.getPosition().getX()][generator10.getPosition().getY()]){
			Vehicles vehicle = generator10.generateVehicle();
			points[generator10.getPosition().getX()][generator10.getPosition().getY()] = vehicle;
			blocked[generator10.getPosition().getX()][generator10.getPosition().getY()] = true;
		}
		if(!blocked[generator11.getPosition().getX()][generator11.getPosition().getY()]){
			Vehicles vehicle = generator11.generateVehicle();
			points[generator11.getPosition().getX()][generator11.getPosition().getY()] = vehicle;
			blocked[generator11.getPosition().getX()][generator11.getPosition().getY()] = true;
		}
		if(!blocked[generator14.getPosition().getX()][generator14.getPosition().getY()]){
			Vehicles vehicle = generator14.generateVehicle();
			points[generator14.getPosition().getX()][generator14.getPosition().getY()] = vehicle;
			blocked[generator14.getPosition().getX()][generator14.getPosition().getY()] = true;
		}
		//

		if(!blocked[generator1.getPosition().getX()][generator1.getPosition().getY()]){
			Vehicles vehicle = generator1.generateVehicle();
			points[generator1.getPosition().getX()][generator1.getPosition().getY()] = vehicle;
			blocked[generator1.getPosition().getX()][generator1.getPosition().getY()] = true;
		}
		if(!blocked[generator3.getPosition().getX()][generator3.getPosition().getY()]){
			Vehicles vehicle = generator3.generateVehicle();
			points[generator3.getPosition().getX()][generator3.getPosition().getY()] = vehicle;
			blocked[generator3.getPosition().getX()][generator3.getPosition().getY()] = true;
		}

		this.repaint();
	}

	// clearing board
	public void clear() {
		for (int x = 0; x < points.length; ++x)
			for (int y = 0; y < points[x].length; ++y) {
				points[x][y] = new Vehicles(0,0,0,0,
						new Vector2d(-1,-1),new Vector2d(0,0));
				blocked[x][y] = false;
			}
		this.repaint();
	}

	private void initialize(int length, int height) {
		points = new Vehicles[length][height];
		directions = new List[length][height];
		blocked = new Boolean[length][height];
		generator1 = new Generator(new Vector2d(2,3), new Vector2d(0,-1));
		generator3 = new Generator(new Vector2d(11,35), new Vector2d(0,1));
		generator10 = new Generator(new Vector2d(70, 35), new Vector2d(0 ,1));
		generator11 = new Generator(new Vector2d(69, 2), new Vector2d(0 ,1));
		generator14 = new Generator(new Vector2d(75, 17), new Vector2d(0 ,1));
		for (int x = 0; x < points.length; ++x)
			for (int y = 0; y < points[x].length; ++y){
				points[x][y] = new Vehicles(0,0,0,0,
						new Vector2d(-1,-1),new Vector2d(0,0));
				directions[x][y] = new ArrayList<>();
				blocked[x][y] = false;
			}

		String localization = System.getProperty("user.dir");
		String str1 = "\\src\\main\\java\\org\\project\\street1.txt";
		String str2 = "";
		String str3 = "\\src\\main\\java\\org\\project\\street3.txt";
		String str4 = "";
		String str5 = "";
		String str6 = "";
		String str7 = "";
		String str8 = "";
		String str9 = "";
		String str10 = "\\src\\main\\java\\org\\project\\street10.txt";
		String str11 = "\\src\\main\\java\\org\\project\\street11.txt";
		String str12 = "";
		String str13 = "";
		String str14 = "\\src\\main\\java\\org\\project\\street14.txt";
		String borderPath = "\\src\\main\\java\\org\\project\\borders.txt";
		String buildingsPath = "\\src\\main\\java\\org\\project\\buildings.txt";

		String border = localization + borderPath;
		String buildings = localization + buildingsPath;
		String street1 = localization + str1;
		String street2 = localization + str2;
		String street3 = localization + str3;
		String street4 = localization + str4;
		String street5 = localization + str5;
		String street6 = localization + str6;
		String street7 = localization + str7;
		String street8 = localization + str8;
		String street9 = localization + str9;
		String street10 = localization + str10;
		String street11 = localization + str11;
		String street12 = localization + str12;
		String street13 = localization + str13;
		String street14 = localization + str14;


		readFile(street1, moveUpList);
		readFile(street3, moveDownList);
		readFile(street10, moveStreet10);
		readFile(street11, moveStreet11);
		readFile(street14, moveStreet14);

		drawFromFile(border, 9);
		drawFromFile(buildings, 8);
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

	public void drawFromFile(String fileName, int typetoSet){
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] numbers = line.split(" ");
				int x = Integer.parseInt(numbers[0]);
				int y = Integer.parseInt(numbers[1]);
				points[x][y].setLength(typetoSet);
			}
		} catch (IOException e) {
			System.err.println("Błąd odczytu pliku: " + e.getMessage());
		}
	}

	//paint background and separators between cells
	protected void paintComponent(Graphics g) {
		if (isOpaque()) {
			g.setColor(getBackground());
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
		}
		g.setColor(Color.GRAY);
		drawNetting(g, size);
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

		for (x = 0; x < points.length; ++x) {
			for (y = 0; y < points[x].length; ++y) {
				if (points[x][y].getLength() != 0) {
					switch (points[x][y].getLength()) {
						case 1 -> g.setColor(new Color(0xFFEA00));
						case 2 -> g.setColor(new Color(0x00ff00));
						case 3 -> g.setColor(new Color(0xff0000));
						case 5 -> g.setColor(new Color(0xff00ff));
						case 8 -> g.setColor(new Color(0x6F7DA1));
						case 9 -> g.setColor(new Color(0x000000));
					}
					g.fillRect((x * size) + 1, (y * size) + 1, (size - 1), (size - 1));
				}
			}
		}

	}
    // 34 x 76
	public void mouseClicked(MouseEvent e) {
		int x = e.getX() / size;
		int y = e.getY() / size;
		if ((x < points.length) && (x > 0) && (y < points[x].length) && (y > 0)) {
			if(editType<4) {
				points[x][y].setLength(editType);
				this.repaint();
			}
			if(editType == 5 && points[x][y].getLength() != editType){
				String localization = System.getProperty("user.dir");
				String str1 = "\\src\\main\\java\\org\\project\\buildings.txt";
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
				String str1 = "\\src\\main\\java\\org\\project\\buildings.txt";
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
