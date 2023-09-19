package Encryptor.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileCreator extends JFrame implements ActionListener {

    private JTextField fileNameField;
    private JButton chooseDirButton;
    private JButton createButton;

    private JFileChooser fileChooser;

    private final Frame frame;

    public FileCreator(Frame frame) {
        this.frame = frame;
        setTitle("File Creator");
        setSize(400, 200);
       // setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create components
        fileNameField = new JTextField();
        chooseDirButton = new JButton("Choose Directory");
        createButton = new JButton("Create");
        createButton.addActionListener(this);

        // Create layout
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(fileNameField, BorderLayout.NORTH);
        panel.add(chooseDirButton, BorderLayout.CENTER);
        panel.add(createButton, BorderLayout.SOUTH);

        // Add panel to the frame
        getContentPane().add(panel);

        // Initialize file chooser
        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        // Add action listener to choose directory button
        chooseDirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnVal = fileChooser.showOpenDialog(FileCreator.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File selectedDir = fileChooser.getSelectedFile();
                    fileNameField.setText(selectedDir.getAbsolutePath() + "/");
                }
            }
        });

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == createButton) {
            String fileName = fileNameField.getText() + ".JAE";

            // Create the file
            try {
                File file = new File(fileName);
                if (file.createNewFile()) {
                    JOptionPane.showMessageDialog(this, "File created successfully.");
                    frame.setChosenFile(file);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "File already exists.");
                }
            } catch (IOException exception) {
                JOptionPane.showMessageDialog(this, "An error occurred while creating the file.");
                exception.printStackTrace();
            }
        }
    }


}
