package Map;
/*
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;

public class Editor extends JFrame {
    private Map m;

    Editor() {
        setSize(new Dimension(800, 800));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        m = new Star();
        //addMouseListener(new MouseAdapter());
        add(new EditorPanel());
    }

    public static void main(String[] args) {
        try {
            EventQueue.invokeAndWait(new Runnable() {
                public void run() {
                    new Editor();
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paintComponents(g);
        Graphics2D g2d = (Graphics2D) g;

        for (Field f : m.getFieldList()) {
            g2d.setColor(f.getRGBColor());
            g2d.fill(f);
            g2d.draw(f);
        }
    }

    class MouseEditorAdapter extends java.awt.event.MouseAdapter {
        Field field;

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }
    }

    class EditorPanel extends JPanel {
        JRadioButton markAsStartingPoint;
        JRadioButton markAsEndingPoint;
        ButtonGroup actions;
        JList colorList = new JList(ColorEnum.values());

        EditorPanel() {
            markAsStartingPoint = new JRadioButton("Starting");
            markAsEndingPoint = new JRadioButton("Ending");
            actions = new ButtonGroup();
            actions.add(markAsEndingPoint);
            actions.add(markAsStartingPoint);

            colorList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
            colorList.setLayoutOrientation(JList.VERTICAL);
            colorList.setVisibleRowCount(-1);

            add(markAsStartingPoint);
            add(markAsEndingPoint);
            add(colorList);

            setVisible(true);
        }

    }
}
*/