
package kurvasplinekel3;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class BezierSpline extends JPanel {
    
    // Arraylist untuk menyimpan titik-titik kontrol
    private ArrayList<Point> controlPoints;
    // Titik yang sedang digeser
    private Point dragPoint;

    // Konstruktor untuk inisialisasi
    public BezierSpline () {
        controlPoints = new ArrayList<>();
        
        // Tambahkan titik kontrol awal
        controlPoints.add(new Point(50, 150));
        controlPoints.add(new Point(100, 50));
        controlPoints.add(new Point(200, 250));
        controlPoints.add(new Point(300, 150));
        
        // Mouse listener untuk menggeser titik kontrol
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                for (Point point : controlPoints) {
                    if (Math.abs(e.getX() - point.x) <= 5 && Math.abs(e.getY() - point.y) <= 5) {
                        dragPoint = point;
                        break;
                    }
                }
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                dragPoint = null;
                repaint();
            }
        });
        
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (dragPoint != null) {
                    dragPoint.x = e.getX();
                    dragPoint.y = e.getY();
                    repaint();
                }
            }
        });
    }
    
    // Metode untuk menggambar komponen
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLUE);
        g2d.setStroke(new BasicStroke(2));
        
        int n = controlPoints.size() - 1;
        // Gambar garis lurus antara titik kontrol
        for (int i = 0; i < n; i++) {
            Point p0 = controlPoints.get(i);
            Point p1 = controlPoints.get(i + 1);
            g2d.drawLine(p0.x, p0.y, p1.x, p1.y);
        }
        
        // Gambar kurva Bezier
        g2d.setColor(Color.RED);
        for (double t = 0.0; t <= 1.0; t += 0.01) {
            int x = (int) Math.round(calculateBezierPoint(controlPoints, t).x);
            int y = (int) Math.round(calculateBezierPoint(controlPoints, t).y);
            g2d.drawLine(x, y, x, y);
        }
        
        // Gambar titik kontrol
        g2d.setColor(Color.BLACK);
        for (Point point : controlPoints) {
            g2d.fillRect(point.x - 3, point.y - 3, 6, 6);
        }
    }
    
    // Metode untuk menghitung titik pada kurva Bezier
    private Point calculateBezierPoint(ArrayList<Point> points, double t) {
        int n = points.size() - 1;
        double x = 0;
        double y = 0;
        for (int i = 0; i <= n; i++) {
            double bi = bernstein(n, i, t);
            x += points.get(i).x * bi;
            y += points.get(i).y * bi;
        }
        return new Point((int) Math.round(x), (int) Math.round(y));
    }
    
    // Metode untuk menghitung koefisien binomial
    private double bernstein(int n, int i, double t) {
        return binomialCoefficient(n, i) * Math.pow(t, i) * Math.pow(1 - t, n - i);
    }
    
    // Metode untuk menghitung koefisien binomial
    private int binomialCoefficient(int n, int k) {
        return factorial(n) / (factorial(k) * factorial(n - k));
    }
    
    // Metode untuk menghitung faktorial
    private int factorial(int n) {
        if (n <= 1) {
            return 1;
        }
        return n * factorial(n - 1);
    }
}
