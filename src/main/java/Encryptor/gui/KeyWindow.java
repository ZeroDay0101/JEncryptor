package Encryptor.gui;


import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Objects;

public class KeyWindow extends JFrame {

    private final NotificationPanel notificationPanel = new NotificationPanel(Color.WHITE);

    public KeyWindow(Frame frame, boolean saveFile) {
        this.setResizable(false);
        this.setVisible(true);


        JPasswordField key = new JPasswordField();
        key.setText("Enter password");
        key.setEchoChar((char) 0);

        JTextArea salt = new JTextArea("Enter salt (Optional if you don't want to use hard-coded)");
        JTextArea iterationNum = new JTextArea("Enter iteration number (min. 65565 highly recommended) !!! (default = 100000)");
        iterationNum.setText("100000 (Number of iterations)");
        JCheckBox advancedCheckbox = new JCheckBox("Show advanced");
        advancedCheckbox.setBackground(Color.WHITE);

        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);

        JPanel keyPanel = new JPanel();
        keyPanel.setLayout(new BorderLayout());
        keyPanel.add(key, BorderLayout.CENTER);
        JButton showPassword = new JButton("V");

        keyPanel.add(showPassword, BorderLayout.EAST);

        panel.add(keyPanel);
        panel.add(advancedCheckbox);
        panel.setLayout(new GridLayout(panel.getComponentCount(), 0));

        this.add(panel, BorderLayout.CENTER);


        JButton button = new JButton("Submit");

        this.add(button, BorderLayout.EAST);


        notificationPanel.addNotification("Encryption key is based on a password that is typed in this form,this password should have at least 12 random characters", Color.GREEN);

        this.add(notificationPanel, BorderLayout.SOUTH);

        notificationPanel.updatePanel();


        this.pack();


        DocumentListener Ls = new DocumentListener() {

            public void changedUpdate(DocumentEvent e) {
            }

            public void removeUpdate(DocumentEvent e) {

            }

            public void insertUpdate(DocumentEvent e) {
                JTextComponent text = null;
                if (e.getDocument() == salt.getDocument())
                    text = salt;
                else if (e.getDocument() == iterationNum.getDocument())
                    text = iterationNum;
                else {
                    text = key;
                    key.setEchoChar('*');
                }
                r(text);
                text.getDocument().removeDocumentListener(this);

            }

            private void r(JTextComponent type) {
                Runnable doHighlight = new Runnable() {
                    @Override
                    public void run() {
                        type.setText(type.getText().substring(type.getText().length() - 1));
                    }
                };
                SwingUtilities.invokeLater(doHighlight);
            }

        };
        key.getDocument().addDocumentListener(Ls);
        salt.getDocument().addDocumentListener(Ls);
        iterationNum.getDocument().addDocumentListener(Ls);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.setSECRET_KEY(key.getText());
                if (advancedCheckbox.isSelected() && salt.getText() != null && isCorrectNumber(iterationNum.getText())) {
                    if (!Objects.equals(salt.getText(), "Enter salt (Optional if you don't want to use hard-coded)"))
                        frame.setSALTVALUE(salt.getText());
                    if (!iterationNum.getText().isEmpty())
                        frame.setItterationNum(Integer.parseInt(iterationNum.getText()));
                }

                KeyWindow.this.setVisible(false);
                if (saveFile)
                    frame.saveFile();
                frame.decryptInGUI();
            }
        });
        showPassword.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (key.getEchoChar() == '*')
                    key.setEchoChar((char) 0);
                else
                    key.setEchoChar('*');

            }
        });
        advancedCheckbox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                notificationPanel.getNotifications().clear();
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
                KeyWindow.this.pack();
                panel.repaint();
                panel.revalidate();
            }
        });
    }

    private boolean isCorrectNumber(String input) {
        try {
            Integer.parseInt(input);
        } catch (Exception e) {
            return false;
        }
        return true;
    }


}
