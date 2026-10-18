package Cafe;

import javax.swing.SwingUtilities;

public class Main {

        public static void main (String[] args) {

            SwingUtilities.invokeLater(() -> {

                CafeGUI cafeGUI = new CafeGUI();

                cafeGUI.setVisible(true);

            });
        }
}