package UI.UtilitisUI;

import javax.swing.*;

public class Utilitis {
    public Utilitis() {
    }

    public static void showMessageToUser(String message, String type, int jOptionPane) {
        JOptionPane optionPane = new JOptionPane(message, jOptionPane);
        JDialog dialog = optionPane.createDialog(type);
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);
    }
}