package org.project;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JComponent;
import javax.swing.event.MouseInputListener;

import static java.lang.Math.random;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Board with Points that may be expanded (with automatic change of cell
 * number) with mouse event listener
 */

public class Board extends JComponent implements MouseInputListener, ComponentListener {
	private static final long serialVersionUID = 1L;
	private Vehicles[][] points;
	private List<Vector2d>[][] directions;
	private Boolean[][] blocked;
	Generator generator;
	Generator generator2;
	private List<Vector2d> moveDownList = new ArrayList<>();
	private List<Vector2d> moveUpList = new ArrayList<>();
	private int length;
	private int height;
	private int size = 14;
	Vector2d start;
	public Board(int length, int height) {
		addMouseListener(this);
		addComponentListener(this);
		addMouseMotionListener(this);
		setBackground(Color.WHITE);
		setOpaque(true);
		this.length = length;
		this.height = height;
	}

	public void swapVehicles(int x, int y, Vector2d vector){
		points[vector.getX()][vector.getY()] = points[x][y];
		points[x][y].setPosition(vector);
		blocked[vector.getX()][vector.getY()] = true;
		points[vector.getX()][vector.getY()].moved = true;
		points[x][y] = new Vehicles(0,0,0,0,
				new Vector2d(-1,-1),new Vector2d(0,0));
		blocked[x][y] = false;
	}

	public void move(int x, int y){
		Vector2d vector;

		if(directions[x][y].size() > 0){
			vector = directions[x][y].get(0);
			if(directions[vector.getX()][vector.getY()].size() > 0){
				vector = directions[vector.getX()][vector.getY()].get(0);
				swapVehicles(x,y,vector);
			}
		}
	}

	// single iteration
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
			if(points[x][y].getLength() > 0 && !points[x][y].moved) move(x,y);
		}

		// ruch na góre
		start = moveUpList.size();
		for (int i = start - 1; i >= 0 ; --i) {
			int x = moveUpList.get(i).getX();
			int y = moveUpList.get(i).getY();
			if(points[x][y].getLength() > 0 && !points[x][y].moved) move(x,y);
		}


		if(!blocked[generator.getPosition().getX()][generator.getPosition().getY()]){
			Vehicles vehicle = generator.generateVehicle();
			points[generator.getPosition().getX()][generator.getPosition().getY()] = vehicle;
			blocked[generator.getPosition().getX()][generator.getPosition().getY()] = true;
		}
		if(!blocked[generator2.getPosition().getX()][generator2.getPosition().getY()]){
			Vehicles vehicle = generator2.generateVehicle();
			points[generator2.getPosition().getX()][generator2.getPosition().getY()] = vehicle;
			blocked[generator2.getPosition().getX()][generator2.getPosition().getY()] = true;
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
		generator = new Generator(new Vector2d(30,4), new Vector2d(0,-1));
		generator2 = new Generator(new Vector2d(14,28), new Vector2d(0,1));
		for (int x = 0; x < points.length; ++x)
			for (int y = 0; y < points[x].length; ++y){
				points[x][y] = new Vehicles(0,0,0,0,
						new Vector2d(-1,-1),new Vector2d(0,0));
				directions[x][y] = new ArrayList<Vector2d>();
				blocked[x][y] = false;
			}


		readFile("C:\\Users\\fuska\\MSD_project\\src\\main\\java\\org\\project\\droga2.txt", moveDownList);
		readFile("C:\\Users\\fuska\\MSD_project\\src\\main\\java\\org\\project\\droga1.txt", moveUpList);

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
						case 1:
							g.setColor(new Color(0xFFEA00));
							break;
						case 2:
							g.setColor(new Color(0x00ff00));
							break;
						case 3:
							g.setColor(new Color(0xff0000));
							break;
					}
					g.fillRect((x * size) + 1, (y * size) + 1, (size - 1), (size - 1));
				}
			}
		}

	}

	public void mouseClicked(MouseEvent e) {
		int x = e.getX() / size;
		int y = e.getY() / size;
		if ((x < points.length) && (x > 0) && (y < points[x].length) && (y > 0)) {
//			points[x][y].clicked();

			this.repaint();
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
//			points[x][y].setState(1);
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
