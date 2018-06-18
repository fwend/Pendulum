package pendulum;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import static java.lang.Math.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class Pendulum extends JPanel {
    int w, h;
    PendulumObj p;

    public Pendulum() {
        Dimension dim = new Dimension(700, 500);
        setPreferredSize(dim);
        setBackground(Color.white);

        w = dim.width;
        h = dim.height;

        p = new PendulumObj(w / 2, 0, 275);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                p.reset();
                repaint();
            }
        });

        new Timer(17, (ActionEvent e) -> {
            repaint();
        }).start();
    }

    @Override
    public void paintComponent(Graphics gg) {
        super.paintComponent(gg);
        Graphics2D g = (Graphics2D) gg;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        p.update();
        p.draw(g);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame();
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setTitle("Pendulum");
            f.setResizable(false);
            f.add(new Pendulum(), BorderLayout.CENTER);
            f.pack();
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }
}

class PendulumObj {
    int ox, oy, ballRadius;
    double armLength, angle, aVelocity, aAcceleration, damping;

    PendulumObj(int x, int y, double len) {
        ox = x;
        oy = y;
        armLength = len;
        reset();
    }

    final void reset() {
        angle = PI / 4;
        aVelocity = 0.0;
        aAcceleration = 0.0;
        damping = 0.995;
        ballRadius = 48;
    }

    void update() {
        double gravity = 0.4;

        aAcceleration = (-1 * gravity / armLength) * sin(angle); // ?????
        aVelocity += aAcceleration;
        aVelocity *= damping;
        angle += aVelocity;
    }

    void draw(Graphics2D g) {
        int x = ox + (int) round(armLength * sin(angle));
        int y = oy + (int) round(armLength * cos(angle));

        g.setStroke(new BasicStroke(2));

        g.setColor(Color.black);
        g.drawLine(ox, oy, x, y);

        g.setColor(Color.lightGray);
        int shift = ballRadius / 2;
        g.fillOval(x - shift, y - shift, ballRadius, ballRadius);

        g.setColor(Color.black);
        g.drawOval(x - shift, y - shift, ballRadius, ballRadius);
    }
}