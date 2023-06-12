package de.eidoop.sudoku.solver.frames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class OpenFileFrame extends Frame {

    private String path;

    public OpenFileFrame() {
        int width = 720;
        int height = 400;
        this.setSize(width, height);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                setVisible(false);
            }


        });
        this.setLayout(null);
        this.setResizable(false);

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.addActionListener(e -> {
            this.path = fileChooser.getSelectedFile().getAbsolutePath();
            setVisible(false);
        });
        fileChooser.setBounds(0, 31, width, height - 31);
        this.add(fileChooser);
    }

    public String getPath() {
        return path;
    }
}
