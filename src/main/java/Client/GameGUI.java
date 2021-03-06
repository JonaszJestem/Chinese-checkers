package Client;

import Map.ColorEnum;
import Map.Field;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;

class GameGUI extends JFrame {
    private final Gamer gamer;
    private final MoveAdapter moveAdapter = new MoveAdapter();
    private Point moveFrom;
    private final Object lock = new Object();
    private final GameInfo gameInfo;

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
        final JLabel myColor = new JLabel("My Color");
        final JLabel currentColor = new JLabel("Current Color");
        final JLabel currentPlayers = new JLabel("-/-");
        final JButton addBotPlayer = new JButton("Add bot");
        final JButton ready = new JButton("Ready");

        GameInfo() {
            setBackground(Color.DARK_GRAY);
            setSize(200,100);
            myColor.setOpaque(true);
            myColor.setBackground(gamer.myColor.getRGBColor());
            currentColor.setOpaque(true);
            if(gamer.currentColor!=null)currentColor.setBackground(gamer.currentColor.getRGBColor());

            addBotPlayer.addActionListener(e -> gamer.gameWriter.println("ADDBOT"));
            ready.addActionListener(e -> gamer.gameWriter.println("READY"));

            add(myColor);
            add(currentColor);
            add(currentPlayers);
            add(addBotPlayer);
            add(ready);
            setVisible(true);
        }

        void setCurrentColor() {
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
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            Point moveTo = e.getPoint();

            synchronized (lock) {
                if (moveFrom != null) {
                    disableMoving();
                    sendMove(moveFrom, moveTo);
                }
                moveFrom = null;
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
