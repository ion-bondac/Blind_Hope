package PaooGame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu {
    public static void main(String[] args) {
        // Creăm un frame (fereastră)
        JFrame fereastra = new JFrame("Meniu Simplu");
        fereastra.setSize(800, 480);
        fereastra.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Creăm un buton
        JButton buton = new JButton("Apasă-mă");

        // Adăugăm un ActionListener pentru buton
        buton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ce se întâmplă când butonul este apăsat
                JOptionPane.showMessageDialog(fereastra, "Butonul a fost apăsat!");
            }
        });

        // Adăugăm butonul la fereastră
        fereastra.add(buton);

        // Setăm fereastra să fie vizibilă
        fereastra.setVisible(true);
    }
}
