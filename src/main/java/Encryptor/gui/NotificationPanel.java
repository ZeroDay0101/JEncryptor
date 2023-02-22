package Encryptor.gui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class NotificationPanel extends JPanel {
    ArrayList<Notification> notifications = new ArrayList<>();
    public NotificationPanel(Color backgroundColor) {
        this.setBackground(backgroundColor);
        this.setVisible(true);
    }

    public void addNotification(String text,Color color) {
        notifications.add(new Notification(text,color));
    }



    public void updatePanel() {
        this.removeAll();
        for (Notification notification:notifications) {
            JLabel label = new JLabel(notification.getText());
            label.setForeground(notification.getColor());
            this.add(label);
        }

    }

    public class Notification {
        private final String text;
        private final Color color;

        public String getText() {
            return text;
        }

        public Color getColor() {
            return color;
        }

        Notification(String text, Color color) {
            this.text = text;
            this.color = color;
        }
    }
}
