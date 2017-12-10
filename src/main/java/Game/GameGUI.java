package Game;

import Client.Client;
import Map.ColorEnum;
import Map.Field;
import Map.Map;
import Map.Star;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

import static java.lang.Math.*;

public class GameGUI extends JFrame {
    private Map map;
    private Client client;

    public GameGUI(Client client) {
        System.out.println("Creating game GUI");
        this.client = client;
        System.out.println("Getting the map...");
        map = new Star(500, 500);
        map.setFieldList(client.getMap());
        System.out.println("Got the map");
        setSize(new Dimension(800, 800));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        addMouseListener(new MouseAdapter());
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        for (Field f : map.getFieldList()) {
            g2d.setColor(f.getRGBColor());
            g2d.fill(f);
            g2d.draw(f);
        }
    }

    private Point getMiddle(Field field, Field f) {
        System.out.println(new Point(new Double((field.x + f.x) / 2).intValue(), new Double((field.y + f.y) / 2).intValue()));
        return new Point(abs(new Double((field.x + f.x + map.getFieldSize()) / 2).intValue()),
                abs(new Double((field.y + f.y + map.getFieldSize()) / 2).intValue()));
    }

    private double distance(Field field, Field f) {
        System.out.println(sqrt(pow(abs(field.x - f.x), 2) + pow(abs(field.y - f.y), 2)));
        return sqrt(pow(abs(field.x - f.x), 2) + pow(abs(field.y - f.y), 2));
    }

    class MouseAdapter extends java.awt.event.MouseAdapter {
        Field field;

        @Override
        public void mousePressed(MouseEvent e) {
            for (Field f : map.getFieldList()) {
                if (f.contains(e.getPoint()) && f.getColor() != ColorEnum.WHITE) {
                    field = f;
                    System.out.println(field.x + " " + field.y);
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            for (Field f : map.getFieldList()) {
                if (f.contains(e.getPoint()) && f.getColor() == ColorEnum.WHITE) {
                    if (distance(field, f) <= 45) {
                        f.setColor(field.getColor());
                        field.setColor(ColorEnum.WHITE);
                    } else if (distance(field, f) <= 90) {
                        Point middle = getMiddle(field, f);
                        for (Field f2 : map.getFieldList()) {
                            System.out.println(f2.x + " " + f2.y);
                            if (f2.contains(middle) && f2.getColor() != ColorEnum.WHITE) {
                                System.out.println("in");
                                f.setColor(field.getColor());
                                field.setColor(ColorEnum.WHITE);
                                break;
                            }
                        }
                    }
                    repaint();
                }

            }
        }
    }
}