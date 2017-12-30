package Client;

import Map.ColorEnum;
import Map.Field;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class GameGUI extends JFrame {
    private Gamer gamer;
    private MoveAdapter moveAdapter = new MoveAdapter();

    GameGUI(Gamer gamer) {
        this.gamer = gamer;

        setResizable(false);
        setSize(new Dimension(800, 800));
        getContentPane().setBackground(Color.WHITE);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        getContentPane().add(new Canvas());
        setVisible(true);
    }

    void allowMoving() {
        addMouseListener(moveAdapter);
    }

    void disableMoving() {
        removeMouseListener(moveAdapter);
    }

    class Canvas extends JPanel {
        Canvas() {
            setBackground(Color.LIGHT_GRAY);
            setSize(new Dimension(500, 510));
            //addMouseListener(new MoveAdapter());
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            Graphics2D g2d = (Graphics2D) g;

            gamer.getFieldList().forEach((k, v) -> {
                g2d.setColor(v.getRGBColor());
                g2d.draw(k);
                g2d.fill(k);
            });
        }
    }

    class MoveAdapter extends java.awt.event.MouseAdapter {
        Point moveFrom = null;
        Point moveTo = null;

        @Override
        public void mousePressed(MouseEvent e) {
            moveFrom = e.getPoint();
            System.out.println(moveFrom);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            moveTo = e.getPoint();
            System.out.println(moveTo);

            System.out.println("In send move");
            if(moveTo != null && moveFrom != null) sendMove(moveFrom, moveTo);
            moveFrom = null; moveTo = null;
        }

        private void sendMove(Point moveFrom, Point moveTo) {
            synchronized (gamer.map) {
                for(Field f: gamer.map.keySet()) {
                    if (f.contains(moveFrom)) gamer.from = f;
                    if (f.contains(moveTo)) gamer.to = f;
                }

                if (gamer.from != null && gamer.to != null) {
                    System.out.println(gamer.from + " " + gamer.to);
                    System.out.println(gamer.map.get(gamer.to) == ColorEnum.WHITE);
                    System.out.println(gamer.map.get(gamer.from));
                    System.out.println("Sending move");
                    gamer.gameWriter.println("MOVE " + gamer.from.x_int + " " + gamer.from.y_int + " " + gamer.to.x_int + " " + gamer.to.y_int + " " + gamer.myColor);
                }
            }
        }
    }
}
