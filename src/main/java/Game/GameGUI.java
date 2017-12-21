package Game;

import Client.Client;
import Map.Field;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class GameGUI extends JFrame {
    private Client client;

    public GameGUI(Client client) {
        this.client = client;
        client.getMap();

        setResizable(false);
        setSize(new Dimension(800, 800));
        //TODO Closing operation on GameGUI ends up with closing ClientGUI as well! Don't know how to call GameGUIController.shutdown()
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        addMouseListener(new MouseAdapter());
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        for (Field f : client.map.getFieldList()) {
            g2d.setColor(f.getRGBColor());
            g2d.fill(f);
            g2d.draw(f);
        }
    }

    class MouseAdapter extends java.awt.event.MouseAdapter {
        Point moveFrom;
        Point moveTo;

        @Override
        public void mousePressed(MouseEvent e) {
            client.getMap();
            repaint();
            moveFrom = e.getPoint();

        }

        @Override
        public void mouseReleased(MouseEvent e) {
            moveTo = e.getPoint();
            client.move(moveFrom, moveTo);
            repaint();
        }
    }
}