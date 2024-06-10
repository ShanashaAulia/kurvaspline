package kurvasplinekel3; //Menandakan berada di package kurvasplinekel13

import javax.swing.JFrame;  //Import class JFrame dari library Swing 

public class framespline {  //kelas framespline

  public static void main(String[] args) {  //Mengeksekusi ketika program dijalankan.
   
    JFrame frame = new JFrame(); // Membuat objek JFrame untuk menampilkan frame
    frame.setTitle("Komputer Grafik"); // judul frame
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Menampilkan saat close aplikasi)


    BezierSpline mainPanel = new BezierSpline();  // Membuat objek panel yang akan menampilkan kurva Bezier
    frame.getContentPane().add(mainPanel);   // Menambahkan panel yang berisi kurva ke window
    frame.pack();  // Sesuaikan ukuran window agar pas dengan panel
    frame.setVisible(true); // Menampilkan window aplikasi
  }
}
