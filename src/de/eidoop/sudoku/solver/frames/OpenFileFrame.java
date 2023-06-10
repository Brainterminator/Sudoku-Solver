package de.eidoop.sudoku.solver.frames;

import de.eidoop.sudoku.api.exceptions.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class OpenFileFrame extends Frame {
    private int width = 720;
    private int height = 400;

    private String path;

    public OpenFileFrame(SudokuFrame sudokuFrame) {
        this.setSize(width, height);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                hide();
            }


        });
        this.setLayout(null);
        this.setResizable(false);

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.addActionListener(e -> {
            this.path = fileChooser.getSelectedFile().getAbsolutePath();
            hide();
        });
        fileChooser.setBounds(0, 31, width, height - 31);
        this.add(fileChooser);
    }

    public String getPath() {
        return path;
    }
}
