package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class GameGUI extends JFrame {
    private Gamer gamer;

    public GameGUI(Gamer gamer) {
        this.gamer = gamer;

        setResizable(false);
        setSize(new Dimension(800, 800));
        getContentPane().setBackground(Color.WHITE);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        getContentPane().add(new Canvas());
        setVisible(true);
    }

    class Canvas extends JPanel {
        Canvas() {
            setBackground(Color.LIGHT_GRAY);
            setSize(new Dimension(500, 510));
            addMouseListener(new MouseAdapter());
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);

            Graphics2D g2d = (Graphics2D) g;
            gamer.map.forEach((k, v) -> {
                g2d.setColor(v.getRGBColor());
                g2d.draw(k);
                g2d.fill(k);
            });
        }
    }

    class MouseAdapter extends java.awt.event.MouseAdapter {
        Point moveFrom;
        Point moveTo;

        @Override
        public void mousePressed(MouseEvent e) {
            moveFrom = e.getPoint();

        }

        @Override
        public void mouseReleased(MouseEvent e) {
            moveTo = e.getPoint();
            gamer.sendMove(moveFrom, moveTo);
            repaint();
        }
    }
}
