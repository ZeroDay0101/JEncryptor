package Encryptor.gui;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class Options extends JPanel {

    private final Frame frame;

    private final JButton open = new JButton("Select");
    private final JButton save = new JButton("Encrypt and Save");

    private final JButton decrypt = new JButton("Show encrypted");

    private boolean showingEncrypted = false;


    public Options(Frame frame) {
        this.frame = frame;
        this.setLayout(new GridLayout());
        this.setPreferredSize(new Dimension(0, 20));


        open.setFocusPainted(false);

        save.setFocusPainted(false);
        save.setEnabled(false);

        decrypt.setFocusPainted(false);
        decrypt.setEnabled(false);


        this.add(open);
        this.add(save);
        this.add(decrypt);


        open.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                choseAFile();
                if (frame.getChosenFile() != null)
                    new ProvideKeyWindow(frame, false);

            }
        });

        decrypt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (frame.getChosenFile() != null && frame.getSECRET_KEY() != null) {
                    showingEncrypted = !showingEncrypted;
                    if (!showingEncrypted) {
                        frame.decryptInGUI();
                        save.setEnabled(true);
                    }
                    else {
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
