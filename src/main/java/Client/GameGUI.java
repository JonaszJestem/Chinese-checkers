package Client;

import Map.ColorEnum;
import Map.Field;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import static java.lang.Math.abs;
import static java.lang.StrictMath.pow;
import static java.lang.StrictMath.sqrt;

public class GameGUI extends JFrame {
    private Gamer gamer;
    private MoveAdapter moveAdapter = new MoveAdapter();
    Point moveFrom;
    Point moveTo;
    Object lock = new Object();
    GameInfo gameInfo;

    GameGUI(Gamer gamer) {
        this.gamer = gamer;

        setResizable(false);
        setSize(new Dimension(800, 800));
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        addWindowListener(new ExitListener());

        getContentPane().add(new Canvas(), BorderLayout.CENTER);

        gameInfo = new GameInfo();
        getContentPane().add(gameInfo, BorderLayout.PAGE_END);
        setVisible(true);
    }

    void allowMoving() {
        addMouseListener(moveAdapter);
    }

    void disableMoving() {
        removeMouseListener(moveAdapter);
    }

    class GameInfo extends JPanel {
        JLabel myColor = new JLabel("My Color");
        JLabel currentColor = new JLabel("Current Color");;

        GameInfo() {
            setBackground(Color.DARK_GRAY);
            setSize(200,100);
            myColor.setOpaque(true);
            myColor.setBackground(gamer.myColor.getRGBColor());
            currentColor.setOpaque(true);
            if(gamer.currentColor!=null)currentColor.setBackground(gamer.currentColor.getRGBColor());

            add(myColor);
            add(currentColor);
            setVisible(true);
        }

        public void setCurrentColor() {
            if(gamer.currentColor!=null) currentColor.setBackground(gamer.currentColor.getRGBColor());
        }
    }

    class Canvas extends JPanel {
        Canvas() {
            setBackground(Color.LIGHT_GRAY);
            setSize(new Dimension(500, 560));
            //addMouseListener(new MoveAdapter());
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            Graphics2D g2d = (Graphics2D) g;

            gamer.map.forEach((k, v) -> {
                g2d.setColor(v.getRGBColor());
                g2d.draw(k);
                g2d.fill(k);

                //Draws coordinates of fields
                /*
                g2d.setColor(Color.BLACK);
                g2d.setFont(new Font("Dialog", Font.PLAIN, 12));
                g2d.drawString(Integer.toString(k.x_int), k.x_int+10, k.y_int+10);
                g2d.drawString(Integer.toString(k.y_int), k.x_int+10, k.y_int+25);
                */
            });
            gameInfo.setCurrentColor();
        }
    }

    class MoveAdapter extends java.awt.event.MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            moveFrom = e.getPoint();
            System.out.println(moveFrom);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            moveTo = e.getPoint();
            System.out.println(moveTo);

            synchronized (lock) {
                if (moveTo != null && moveFrom != null) {
                    System.out.println("In send move");
                    disableMoving();
                    sendMove(moveFrom, moveTo);
                }
                moveFrom = null;
                moveTo = null;
            }
        }

        private void sendMove(Point moveFrom, Point moveTo) {
            moveFrom.y -= 30;
            moveTo.y -=30;

            moveFrom.x -= 5;
            moveTo.x -=5;
            synchronized (gamer.map) {
                for(Field f: gamer.map.keySet()) {
                    if (f.middle.distance(moveFrom) < f.size/2 && gamer.map.get(f) != ColorEnum.WHITE) gamer.from = f;
                    else if (f.middle.distance(moveTo) < f.size/2 && gamer.map.get(f) == ColorEnum.WHITE) gamer.to = f;
                }

                if (gamer.from != null && gamer.to != null) {
                    System.out.println("Sending move: " + gamer.from.x_int + " " + gamer.from.y_int + " " + gamer.to.x_int + " " + gamer.to.y_int + " " + gamer.myColor );
                    gamer.gameWriter.println("MOVE " + gamer.from.x_int + " " + gamer.from.y_int + " " + gamer.to.x_int + " " + gamer.to.y_int + " " + gamer.myColor);
                }
            }
            allowMoving();
        }

    }

    private class ExitListener extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            int confirm = JOptionPane.showOptionDialog(
                    null, "Are You Sure to quit game?",
                    "Exit Confirmation", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, null, null);
            if (confirm == 0) {
                gamer.gameWriter.println("QUIT");
                dispose();
            }
        }
    }
}
