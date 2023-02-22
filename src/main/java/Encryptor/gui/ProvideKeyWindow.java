package Encryptor.gui;

import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Objects;

public class ProvideKeyWindow extends JFrame {
    NotificationPanel notificationPanel = new NotificationPanel(Color.WHITE);

    public ProvideKeyWindow(Frame frame, boolean saveFile) {
        this.setResizable(false);
        this.setVisible(true);


        JTextArea key = new JTextArea("Enter password");
        JTextArea salt = new JTextArea("Enter salt (Optional if you don't want to use hard-coded)");
        JTextArea iterationNum = new JTextArea("Enter iteration number (min. 65565 highly recommended) !!! (default = 100000)");
        iterationNum.setText("100000 (Number of iterations)");
        JCheckBox advancedCheckbox = new JCheckBox("Show advanced");
        advancedCheckbox.setBackground(Color.WHITE);

        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);


        panel.add(key);
        panel.add(advancedCheckbox);
        panel.setLayout(new GridLayout(panel.getComponentCount(),0));

        this.add(panel, BorderLayout.CENTER);


        JButton button = new JButton("Submit");
        this.add(button, BorderLayout.EAST);


        notificationPanel.addNotification("Encryption key is based on a password that is typed in this form,this password should have at least 12 random characters", Color.GREEN);

        this.add(notificationPanel, BorderLayout.SOUTH);

        notificationPanel.updatePanel();

        this.pack();


        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.setSECRET_KEY(key.getText());
                if (advancedCheckbox.isSelected() && salt.getText() != null && isCorrectNumber(iterationNum.getText())) {
                    if (!Objects.equals(salt.getText(), "Enter salt (Optional if you don't want to use hard-coded)"))
                        frame.setSALTVALUE(salt.getText());
                    frame.setItterationNum(Integer.parseInt(iterationNum.getText()));
                }

                ProvideKeyWindow.this.setVisible(false);
                if (saveFile)
                    frame.saveFile();
                frame.decryptInGUI();
            }
        });
        advancedCheckbox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                notificationPanel.notifications.clear();
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    panel.remove(advancedCheckbox);
                    panel.add(salt);
                    panel.add(iterationNum);
                    panel.add(advancedCheckbox);

                    notificationPanel.addNotification("WARNING! IF YOU CHANGE ITERATION OR SALT DOING ENCRYPTION YOU WILL HAVE TO ENTER IT EVERY TIME YOU DECRYPT.", Color.RED);
                } else {
                    panel.remove(iterationNum);
                    panel.remove(salt);
                    notificationPanel.addNotification("Encryption key is based on a password that is typed in this form,this password should have at least 12 random characters", Color.GREEN);
                }
                notificationPanel.updatePanel();
                ((GridLayout) panel.getLayout()).setRows(panel.getComponentCount());
                ProvideKeyWindow.this.pack();
                panel.repaint();
                panel.revalidate();
            }
        });
    }
    private boolean isCorrectNumber(String input) {
        try {
            Integer.parseInt(input);
        }
        catch(Exception e){
            return false;
        }
        return true;
    }

}
