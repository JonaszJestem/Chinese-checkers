package Client;

import javax.swing.*;
import javax.swing.border.Border;
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
            setSize(new Dimension(500, 610));
            //addMouseListener(new MoveAdapter());
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            Graphics2D g2d = (Graphics2D) g;

            synchronized (gamer.map) {
                gamer.map.forEach((k, v) -> {
                    g2d.setColor(v.getRGBColor());
                    g2d.draw(k);
                    g2d.fill(k);
                });
            }
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
            if (moveFrom != null && moveTo != null) gamer.sendMove(moveFrom, moveTo);
        }
    }
}
