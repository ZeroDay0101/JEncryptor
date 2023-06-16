package Encryptor.gui;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;

public class Options extends JPanel {

    private final Frame frame;

    private final JButton open = new JButton("Select");
    private final JButton save = new JButton("Encrypt and Save");

    private final JButton decrypt = new JButton("Show encrypted");

    private JSlider randomPasswdLengthSlider = new JSlider(1, 50);
    private final JButton generateRandomPassword = new JButton("Generate random password");

    private final JLabel generateRandomPasswordValue = new JLabel(String.valueOf(randomPasswdLengthSlider.getValue()));
    private boolean showingEncrypted = false;


    public Options(Frame frame) {
        this.frame = frame;
        this.setLayout(new GridLayout());


        open.setFocusPainted(false);

        save.setFocusPainted(false);
        save.setEnabled(false);

        decrypt.setFocusPainted(false);
        decrypt.setEnabled(false);


        this.add(open);
        this.add(save);
        this.add(decrypt);


        this.add(generateRandomPassword);
        this.add(randomPasswdLengthSlider);
        this.add(generateRandomPasswordValue);


        open.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                choseAFile();
                if (frame.getChosenFile() != null)
                    new KeyWindow(frame, false);

            }
        });

        decrypt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (frame.getChosenFile() != null && frame.getSECRET_KEY() != null) {
                    showingEncrypted = !showingEncrypted;
                    if (!showingEncrypted) {
                        frame.decryptInGUI();
                        save.setEnabled(true);
                    } else {
                        frame.encryptInGui();
                        save.setEnabled(false);
                    }
                }

                frame.repaint();
            }
        });
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (frame.getChosenFile() != null && frame.getSECRET_KEY() != null)
                    frame.saveFile();
            }

        });
        generateRandomPassword.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final String LOWERCASE_LETTERS = "abcdefghijklmnopqrstuvwxyz";
                final String UPPERCASE_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
                final String DIGITS = "0123456789";
                final String SPECIAL_CHARACTERS = "!@#$%^&*()-_=+\\|[{]};:'\",<.>/?";

                String validCharacters = LOWERCASE_LETTERS + UPPERCASE_LETTERS + DIGITS + SPECIAL_CHARACTERS;
                SecureRandom random = new SecureRandom();

                int passwdLength = randomPasswdLengthSlider.getValue();
                StringBuilder passwordBuilder = new StringBuilder(passwdLength);

                for (int i = 0; i < passwdLength; i++) {
                    int randomIndex = random.nextInt(validCharacters.length());
                    char randomChar = validCharacters.charAt(randomIndex);
                    passwordBuilder.append(randomChar);
                }
                frame.getjTextArea().append(passwordBuilder + System.lineSeparator());
            }
        });
        randomPasswdLengthSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                Options.this.generateRandomPasswordValue.setText(String.valueOf(randomPasswdLengthSlider.getValue()));
                repaint();
            }

        });

    }

    public void choseAFile() {
        final JFileChooser fc = new JFileChooser();

        fc.setCurrentDirectory(new File(Options.class.getProtectionDomain().getCodeSource().getLocation().getPath()));

        FileNameExtensionFilter filter = new FileNameExtensionFilter("JAE FILES", "jae");
        fc.setFileFilter(filter);
        int res = fc.showOpenDialog(Options.this);

        if (fc.getSelectedFile() == null || res == JFileChooser.CANCEL_OPTION)
            return;

        frame.setChosenFile(fc.getSelectedFile());
        int result = 0;
        if (!frame.getChosenFile().exists())
            result = JOptionPane.showConfirmDialog(fc, "The file doesn't exists, create?", "Non-Existing file", JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                frame.getChosenFile().createNewFile();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } else {
            frame.setChosenFile(null);
            return;
        }
        save.setEnabled(true);
        decrypt.setEnabled(true);
    }

}
