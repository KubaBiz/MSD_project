package org.project;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestBoard extends JComponent implements MouseInputListener, ComponentListener {

    private Vehicles[][] points;
    private List<Vector2d>[][] directions;
    private List<Vector2d> moveRight = new ArrayList<>();
    private List<Vector2d> moveLeft = new ArrayList<>();
    private Boolean[][] blocked;
    Generator generator1;
    Generator generator2;
    private int size = 12;
    public static Integer []types ={0,1,2,3,4,5,6,7,8,9};
    public int editType = 0;
    private int time = 0;
    public int carInFront(int x, int y, int maxSpeed){
        int sum = 0;
        int i = 0;
        while(i < maxSpeed && directions[x][y].size() > 0){
            int newX = directions[x][y].get(0).getX();
            int newY = directions[x][y].get(0).getY();
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

    public void addVehicle(Generator generator, int frequency){
        if(!blocked[generator.getPosition().getX()][generator.getPosition().getY()] && time % frequency == 0){
            Vehicles vehicle = generator.generateVehicle();
            points[generator.getPosition().getX()][generator.getPosition().getY()] = vehicle;
            blocked[generator.getPosition().getX()][generator.getPosition().getY()] = true;
        }
    }
    public void iteration() {
        for (int x = 0; x < points.length; ++x) {
            for ( int y = 0; y < points[x].length; ++y){
                if(blocked[x][y]) points[x][y].moved = false;
                if(points[x][y].getLength() > 0 ){
                    int obstacle = carInFront(x,y,points[x][y].getMaxSpeed());
                    points[x][y].speedBoost();
                    points[x][y].speedReduction(obstacle);
                }
            }
        }

        for(int i = 0; i < moveRight.size(); i ++){
            Vector2d vector = moveRight.get(i);
            if(points[vector.getX()][vector.getY()].getLength() > 0 && !points[vector.getX()][vector.getY()].moved)
                {moveVehicles(vector.getX(), vector.getY());}
        }
        for(int i = 0; i < moveLeft.size(); i ++){
            Vector2d vector = moveLeft.get(i);
            if(points[vector.getX()][vector.getY()].getLength() > 0 && !points[vector.getX()][vector.getY()].moved)
            {moveVehicles(vector.getX(), vector.getY());}
        }

        addVehicle(generator1, 7);
        addVehicle(generator2, 5);
        clearVehicle(3,7);
        clearVehicle(75,4);
        time++;
        this.repaint();
    }

    public void moveVehicles(int x, int y){
        int speed = points[x][y].getSpeed();
        int i = 0;
        Vector2d vector = new Vector2d(x,y);

        while(i < speed && directions[vector.getX()][vector.getY()].size()>0){
            points[vector.getX()][vector.getY()].moveTail(vector);
            vector = directions[vector.getX()][vector.getY()].get(0);
            i++;
        }

        points[vector.getX()][vector.getY()] = points[x][y];
        blocked[vector.getX()][vector.getY()] = true;
        points[vector.getX()][vector.getY()].moved = true;
        points[x][y] = new Vehicles(0,0,0,0,
                new Vector2d(-1,-1),new Vector2d(0,0));
        blocked[x][y] = false;

    }

    public void clearVehicle(int x,int y){
        if(points[x][y].getLength() > 1){
            for(int i = 0 ; i < points[x][y].getTail().length-1; i ++){
                blocked[points[x][y].getTail()[i].getY()][points[x][y].getTail()[i].getY()] = false;
            }
        }
        points[x][y] = new Vehicles(0,0,0,0,
                new Vector2d(-1,-1),new Vector2d(0,0));
        blocked[x][y] = false;
    }
    public TestBoard(int length, int height) {
        initialize(length, height);
        addMouseListener(this);
        addComponentListener(this);
        addMouseMotionListener(this);
        setBackground(Color.WHITE);
        setOpaque(true);
    }

    private void initialize(int length, int height) {
        points = new Vehicles[length][height];
        directions = new List[length][height];
        blocked = new Boolean[length][height];

        for (int x = 0; x < points.length; ++x)
            for (int y = 0; y < points[x].length; ++y){
                points[x][y] = new Vehicles(0,0,0,0,
                        new Vector2d(-1,-1),new Vector2d(0,0));
                directions[x][y] = new ArrayList<>();
                blocked[x][y] = false;
            }
        readFile("C:\\Users\\fuska\\MSD_project\\src\\main\\java\\org\\project\\test1.txt", moveRight);
        readFile("C:\\Users\\fuska\\MSD_project\\src\\main\\java\\org\\project\\test2.txt", moveLeft);

        generator1 = new Generator(new Vector2d(3,4), new Vector2d(-1,0));
        generator2 = new Generator(new Vector2d(75,7), new Vector2d(1,0));

        Vector2d vector = new Vector2d(3,4);

    }


    public void clear() {
        for (int x = 0; x < points.length; ++x)
            for (int y = 0; y < points[x].length; ++y) {
                points[x][y] = new Vehicles(0,0,0,0,
                        new Vector2d(-1,-1),new Vector2d(0,0));
                blocked[x][y] = false;
            }
        this.repaint();
    }

    protected void paintComponent(Graphics g) {
        if (isOpaque()) {
            g.setColor(getBackground());
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
        }
        g.setColor(Color.GRAY);
        drawNetting(g, size);
    }

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

    @Override
    public void componentResized(ComponentEvent componentEvent) {

    }

    @Override
    public void componentMoved(ComponentEvent componentEvent) {

    }

    @Override
    public void componentShown(ComponentEvent componentEvent) {

    }

    @Override
    public void componentHidden(ComponentEvent componentEvent) {

    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {

    }
}
