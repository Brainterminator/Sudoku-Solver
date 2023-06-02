package de.eidoop.sudoku.solver.frames;

import de.eidoop.sudoku.api.entities.Sudoku;
import de.eidoop.sudoku.api.enums.SudokuState;
import de.eidoop.sudoku.api.exceptions.*;
import de.eidoop.sudoku.api.loader.ExampleLoader;
import de.eidoop.sudoku.api.loader.SudokuLoader;
import de.eidoop.sudoku.api.solver.BruteForceSolver;
import de.eidoop.sudoku.api.solver.SaveSolver;
import de.eidoop.sudoku.api.ui.ISudokuRenderer;
import de.eidoop.sudoku.solver.components.SudokuButton;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SudokuFrame extends Frame implements ISudokuRenderer {

    private String currentError = "";
    private int currentX, currentY, currentValue;
    private final TextField text;
    private final Sudoku sudoku;

    public SudokuFrame(Sudoku sudoku) {
        super("Sudoku App");
        int buttonSize = 25;
        int elementWidth = 9*buttonSize;
        int height = 545;
        int width = elementWidth + 20;
        int offset = 31;
        int gridSize = 2;
        this.setSize(width, height);
        this.sudoku = sudoku;
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        this.setLayout(null);
        this.setResizable(false);


        // Generate elements and assign positions


        // Text Field

        text = new TextField();
        text.addActionListener(e -> {
            currentValue = Integer.parseInt(((TextField) e.getSource()).getText());
            try {
                sudoku.setFixedValue(currentY + 1, currentX + 1, currentValue);
                currentError = "";
                if (currentX < 8) currentX++;
                else {
                    currentX = 0;
                    if (currentY < 8) currentY++;
                    else {
                        currentX = 0;
                        currentY = 0;
                    }
                }
            } catch (QuadrantOccupiedException ex) {
                currentError = "Quadrant occupied!";
            } catch (ColumnOccupiedException ex) {
                currentError = "Column occupied!";
            } catch (RowOccupiedException ex) {
                currentError = "Row occupied!";
            } catch (ValueOutOfRangeException ex) {
                currentError = "Value out of range!";
            } catch (FieldFixedException ex) {
                currentError = "Field already occupied!";
            } catch (CoordinatesOutOfRangeException ex) {
                currentError = "Coordinates out of range!";
            }
            text.setText("");
            print(sudoku);
        });
        text.setBounds(10, offset, buttonSize * 9, buttonSize);
        this.add(text);


        // Error display

        Label errorDisplay = new Label() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                setText(currentError);
            }
        };
        errorDisplay.setBackground(Color.LIGHT_GRAY);
        errorDisplay.setBounds(10, offset + (buttonSize + gridSize), buttonSize * 9, buttonSize);
        this.add(errorDisplay);


        // New Button

        Button emptyButton = new Button();
        emptyButton.setLabel("New Empty");
        // ActionListener als anonyme Klasse mit Referenz auf lokale Variable
        emptyButton.addActionListener(e -> {
            sudoku.reset();
            currentError = "";
            print(sudoku);
        });
        emptyButton.setBounds(10, offset + 2*(buttonSize + gridSize), buttonSize * 9, buttonSize);
        this.add(emptyButton);


        // Example Button

        Button exampleButton = new Button();
        exampleButton.setLabel("New Example");
        // ActionListener als anonyme Klasse mit Referenz auf lokale Variable
        exampleButton.addActionListener(e -> {
            new ExampleLoader().loadSudoku(sudoku);
            currentError = "";
            print(sudoku);
        });
        exampleButton.setBounds(10, offset + 3*(buttonSize + gridSize), buttonSize * 9, buttonSize);
        this.add(exampleButton);



        // Zustand

        Label zustand = new Label() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                setText(sudoku.getState().getStateName());
            }
        };
        zustand.setBounds(10, 180 + offset + buttonSize + 10, buttonSize * 9, buttonSize);
        this.add(zustand);


        //Probier Button

        Button probierButton = new Button();
        probierButton.setLabel("Probieren");
        // ActionListener als anonyme Klasse mit Referenz auf lokale Variable
        probierButton.addActionListener(e -> {
            new BruteForceSolver(sudoku).solve();
            print(sudoku);
        });
        probierButton.setBounds(10, 60 + offset + buttonSize + 10, buttonSize * 4, buttonSize);
        this.add(probierButton);

        //Strategie Button

        Button strategieButton = new Button();
        strategieButton.setLabel("Strategie");
        // ActionListener als anonyme Klasse mit Referenz auf lokale Variable
        strategieButton.addActionListener(e -> {
            new SaveSolver(sudoku).solve();
            print(sudoku);
        });
        strategieButton.setBounds(10, 120 + offset + buttonSize + 10, buttonSize * 4, buttonSize);
        this.add(strategieButton);


        // Sudoku Fields

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                // Generate single buttons
                SudokuButton b = new SudokuButton(i, j, sudoku);
                b.setLabel("");
                b.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        currentX = ((SudokuButton) e.getSource()).x;
                        currentY = ((SudokuButton) e.getSource()).y;
                        text.requestFocus();
                    }
                });
                b.setBounds(i * buttonSize + 10, j * buttonSize + 260 + offset, buttonSize, buttonSize);
                this.add(b);
            }
        }




        this.setVisible(true);
    }

    @Override
    public void print(Sudoku sudoku) {
        this.setVisible(false);
        this.setVisible(true);
    }
}

