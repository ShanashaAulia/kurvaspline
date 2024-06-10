
package kurvasplinekel3;

import java.awt.BasicStroke; // Import kelas BasicStroke dari paket java.awt
import java.awt.Color; // Import kelas Color dari paket java.awt
import java.awt.Graphics; // Import kelas Graphics dari paket java.awt
import java.awt.Graphics2D; // Import kelas Graphics2D dari paket java.awt
import java.awt.Point; // Import kelas Point dari paket java.awt
import java.awt.event.MouseAdapter; // Import kelas MouseAdapter dari paket java.awt.event
import java.awt.event.MouseEvent; // Import kelas MouseEvent dari paket java.awt.event
import java.util.ArrayList; // Import kelas ArrayList dari paket java.util
import javax.swing.JFrame; // Import kelas JFrame dari paket javax.swing
import javax.swing.JPanel; // Import kelas JPanel dari paket javax.swing

public class BezierSpline extends JPanel { // Deklarasi kelas BezierSpline yang merupakan turunan dari kelas JPanel
    
    // Arraylist untuk menyimpan titik-titik kontrol
    private ArrayList<Point> controlPoints; // Deklarasi variabel controlPoints sebagai ArrayList dari objek Point
    // Titik yang sedang digeser
    private Point dragPoint; // Deklarasi variabel dragPoint dari tipe data Point untuk menunjukkan titik yang sedang digeser

    // Konstruktor untuk inisialisasi
    public BezierSpline () { // Deklarasi konstruktor BezierSpline tanpa parameter
        controlPoints = new ArrayList<>(); // Inisialisasi objek controlPoints sebagai ArrayList kosong
        
        // Tambahkan titik kontrol awal
        controlPoints.add(new Point(50, 150));
        controlPoints.add(new Point(100, 50));
        controlPoints.add(new Point(200, 250));
        controlPoints.add(new Point(300, 150));
        
        // Mouse listener untuk menggeser titik kontrol
        addMouseListener(new MouseAdapter() { // Menambahkan MouseAdapter sebagai listener untuk mouse events
            @Override  // Anotasi @Override menunjukkan bahwa metode ini meng-override metode dari superclass
            public void mousePressed(MouseEvent e) { // Override metode mousePressed dari MouseAdapter
                for (Point point : controlPoints) { // Iterasi melalui setiap titik kontrol
                    if (Math.abs(e.getX() - point.x) <= 5 && Math.abs(e.getY() - point.y) <= 5) { 
                        // Periksa apakah klik mouse dalam jarak 5 piksel dari titik kontrol

                        dragPoint = point; // Jika ya, tentukan titik yang sedang digeser
                        break; //berhenti
                    }
                }
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                dragPoint = null; // Set titik yang sedang digeser menjadi null
                repaint(); // Minta komponen untuk menggambar ulang

            }
        });
        
        // Mouse motion listener untuk menggeser titik kontrol
        addMouseMotionListener(new MouseAdapter() { // Menambahkan MouseAdapter sebagai listener untuk mouse motion events
            @Override // Anotasi @Override menunjukkan bahwa metode ini meng-override metode dari superclass
            // Jika ada titik yang sedang digeser, perbarui posisinya
            public void mouseDragged(MouseEvent e) { // Override metode mouseDragged dari MouseAdapter
                if (dragPoint != null) { // Periksa apakah ada titik yang sedang digeser
                    dragPoint.x = e.getX(); // Perbarui posisi x dari titik yang sedang digeser
                    dragPoint.y = e.getY(); // Perbarui posisi y dari titik yang sedang digeser
                    repaint(); // Minta komponen untuk menggambar ulang
                }
            }
        });
    }
    
    // Metode untuk menggambar komponen
   @Override
protected void paintComponent(Graphics g) {
    super.paintComponent(g); // Memanggil metode paintComponent dari kelas induk
    
    Graphics2D g2d = (Graphics2D) g; // Mengonversi objek Graphics menjadi Graphics2D
    g2d.setColor(Color.BLUE); // Mengatur warna gambar menjadi biru
    g2d.setStroke(new BasicStroke(2)); // Mengatur ketebalan garis menjadi 2 pixel

    int n = controlPoints.size() - 1; // Jumlah titik kontrol dikurangi 1
    // Gambar garis lurus antara titik kontrol
    for (int i = 0; i < n; i++) {
        Point p0 = controlPoints.get(i); // Titik awal
        Point p1 = controlPoints.get(i + 1); // Titik akhir
        g2d.drawLine(p0.x, p0.y, p1.x, p1.y); // Menggambar garis antara dua titik kontrol
    }

    // Gambar kurva Bezier
    g2d.setColor(Color.RED); // Mengatur warna gambar menjadi merah
    for (double t = 0.0; t <= 1.0; t += 0.01) { // Iterasi dari t = 0 hingga t = 1
        int x = (int) Math.round(calculateBezierPoint(controlPoints, t).x); // Koordinat x titik pada kurva Bezier
        int y = (int) Math.round(calculateBezierPoint(controlPoints, t).y); // Koordinat y titik pada kurva Bezier
        g2d.drawLine(x, y, x, y); // Menggambar titik pada kurva Bezier
    }

    // Gambar titik kontrol
    g2d.setColor(Color.BLACK); // Mengatur warna gambar menjadi hitam
    for (Point point : controlPoints) { // Iterasi melalui setiap titik kontrol
        g2d.fillRect(point.x - 3, point.y - 3, 6, 6); // Menggambar kotak kecil sebagai titik kontrol
    }
}

// Metode untuk menghitung titik pada kurva Bezier
private Point calculateBezierPoint(ArrayList<Point> points, double t) {
    int n = points.size() - 1; // Jumlah titik kontrol dikurangi 1
    double x = 0; // Inisialisasi koordinat x
    double y = 0; // Inisialisasi koordinat y
    for (int i = 0; i <= n; i++) {
        double bi = bernstein(n, i, t); // Menghitung koefisien bernstein
        x += points.get(i).x * bi; // Menambahkan kontribusi koordinat x titik kontrol
        y += points.get(i).y * bi; // Menambahkan kontribusi koordinat y titik kontrol
    }
    return new Point((int) Math.round(x), (int) Math.round(y)); // Mengembalikan titik hasil perhitungan
}

// Metode untuk menghitung koefisien binomial
private double bernstein(int n, int i, double t) {
    return binomialCoefficient(n, i) * Math.pow(t, i) * Math.pow(1 - t, n - i); // Rumus bernstein
}

// Metode untuk menghitung koefisien binomial
private int binomialCoefficient(int n, int k) {
    return factorial(n) / (factorial(k) * factorial(n - k)); // Rumus koefisien binomial
}

// Metode untuk menghitung faktorial
private int factorial(int n) {
    if (n <= 1) {
        return 1; // Faktorial dari 0 atau 1 adalah 1
    }
    return n * factorial(n - 1); // Rekursi untuk menghitung faktorial
}

}
