package lc3sim.ui;

import java.awt.*;
import javax.swing.*;

public class UIMain {

  public static void main(String[] args) {
    SwingUtilities.invokeLater(
        new Runnable() {
          public void run() {
            UIMain obj = new UIMain();
            obj.display();
          }
        }
    );
  }
  
  public void display() {
    JFrame jfrm = new JFrame("My Frame");
    jfrm.setSize(800, 600);
    jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JLabel jlab = new JLabel("LC3-Sim");
    jfrm.add(jlab);
    jfrm.setVisible(true);
    jfrm.setLayout(new FlowLayout());
  }

}
