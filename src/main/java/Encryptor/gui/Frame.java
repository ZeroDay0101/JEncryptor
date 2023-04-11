package Encryptor.gui;

import Encryptor.Encryptor;
import Encryptor.FileAccesser;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Frame extends JFrame {
    public final JTextArea jTextArea = new JTextArea();
    public final JTextArea num = new JTextArea();

    private final FileAccesser fileAccesser = new FileAccesser();
    private final Encryptor encryptor = new Encryptor();

    private File chosenFile;

    public File getChosenFile() {
        return chosenFile;
    }

    public void setChosenFile(File chosenFile) {
        this.chosenFile = chosenFile;
    }

    public void setSALTVALUE(String SALTVALUE) {
        this.SALTVALUE = SALTVALUE;
    }

    private String SALTVALUE = "*W%8X$jy]5036{#I"; // Salts are useless in local-file encryption and therefore don't need to be randomized and stored (some random chars for encryption to work)

    public String getSALTVALUE() {
        return SALTVALUE;
    }

    private String SECRET_KEY;

    public void setSECRET_KEY(String SECRET_KEY) {
        this.SECRET_KEY = SECRET_KEY;
    }

    public String getSECRET_KEY() {
        return SECRET_KEY;
    }


    public int getItterationNum() {
        return itterationNum;
    }

    public void setItterationNum(int itterationNum) {
        this.itterationNum = itterationNum;
    }

    private int itterationNum = 100000;

    NotificationPanel notificationPanel = new NotificationPanel(Color.PINK);

    private boolean encryptionFailed = false;


    public Frame() {
        this.setBounds(300, 100, 600, 600);
        this.setLayout(new BorderLayout());
        num.setEditable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(jTextArea, BorderLayout.CENTER);
        this.add(num, BorderLayout.WEST);
        this.add(new Options(this), BorderLayout.NORTH);
        this.add(notificationPanel, BorderLayout.SOUTH);
        this.setTitle("Java encryptor");
        updateNotifications();
        this.setVisible(true);
    }

    public void decryptInGUI() {
        jTextArea.setText("");
        num.setText("");
        if (getSALTVALUE() == null)
            setSALTVALUE(SALTVALUE);

        ArrayList<String> list = fileAccesser.read(chosenFile);
        for (int i = 0; i < list.size(); i++) {

            String decryptedval = encryptor.decrypt(list.get(i), getSECRET_KEY(), getSALTVALUE(),getItterationNum());
            encryptionFailed = decryptedval == null;
            updateNotifications();
            jTextArea.append(decryptedval + System.getProperty("line.separator"));
        }

        repaint();
    }

    public void saveFile() {
        try {
            fileAccesser.clearTheFile(chosenFile);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        ArrayList<String> list = new ArrayList<>();
        int lines = 0;
        for (String line : jTextArea.getText().split("\\n")) {
            line = line.replaceAll("\r", "");
            list.add(line);
            lines++;
        }
        for (int i = 0; i < lines; i++) {
            String encryptedval = encryptor.encrypt(list.get(i), getSECRET_KEY(), getSALTVALUE(),getItterationNum());
            fileAccesser.write(chosenFile, encryptedval);
        }

    }

    public void encryptInGui() {
        jTextArea.setText("");
        num.setText("");
        ArrayList<String> list = fileAccesser.read(chosenFile);
        for (int i = 0; i < list.size(); i++) {
            String decryptedval = list.get(i);
            encryptionFailed = decryptedval == null;
            updateNotifications();
            jTextArea.append(decryptedval + System.getProperty("line.separator"));
        }

        repaint();
    }

    public void updateNotifications() {
        notificationPanel.notifications.clear();

        if (chosenFile == null)
            notificationPanel.addNotification("File not selected",Color.RED);
        else
            this.setTitle("Java encryptor - " +  "File: " + chosenFile.getPath());

        if (getSECRET_KEY() == null)
            notificationPanel.addNotification("Key not provided",Color.RED);
        if (encryptionFailed) {
            notificationPanel.addNotification("Provided key (or salt and iterationNum if changed) threw an exception and is probably wrong",Color.RED);
        }


        if (notificationPanel.notifications.isEmpty())
            notificationPanel.setBackground(Color.WHITE);
        else
            notificationPanel.setBackground(Color.PINK);


        notificationPanel.updatePanel();


    }


}
