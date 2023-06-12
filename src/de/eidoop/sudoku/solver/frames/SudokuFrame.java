package de.eidoop.sudoku.solver.frames;

import de.eidoop.sudoku.api.entities.Sudoku;
import de.eidoop.sudoku.api.exceptions.*;
import de.eidoop.sudoku.api.loader.XMLLoader;
import de.eidoop.sudoku.api.saver.XMLSaver;
import de.eidoop.sudoku.api.solver.BruteForceSolver;
import de.eidoop.sudoku.api.ui.ISudokuRenderer;
import de.eidoop.sudoku.solver.components.SudokuButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SudokuFrame extends Frame implements ISudokuRenderer {

    private String currentError = "";
    private int currentX, currentY, currentValue;
    private final TextField text;

    private final OpenFileFrame fileOpener;

    public SudokuFrame(Sudoku sudoku) {
        super("Sudoku App");
        fileOpener = new OpenFileFrame();
        int buttonSize = 25;
        int elementWidth = 9 * buttonSize;
        int height = 482;
        int width = elementWidth + 20;
        int offset = 31;
        int gridSize = 2;
        this.setSize(width, height);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((screenSize.width-width)/2, (screenSize.height-height)/2);

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
        errorDisplay.setAlignment(Label.CENTER);
        errorDisplay.setBounds(10, offset + (buttonSize + gridSize), buttonSize * 9, buttonSize);
        this.add(errorDisplay);


        // New Button

        Button emptyButton = new Button();
        emptyButton.setLabel("New Empty");
        emptyButton.addActionListener(e -> {
            sudoku.reset();
            currentError = "";
            print(sudoku);
        });
        emptyButton.setBounds(10, offset + 2 * (buttonSize + gridSize), buttonSize * 9, buttonSize);
        this.add(emptyButton);


        // XML Select Button

        Button xmlSelectButton = new Button();
        xmlSelectButton.setLabel("Select Path");
        xmlSelectButton.addActionListener(e -> fileOpener.setVisible(true));
        xmlSelectButton.setBounds(10, offset + 3 * (buttonSize + gridSize), buttonSize * 9, buttonSize);
        this.add(xmlSelectButton);

        // XML Load Button

        Button xmlLoadButton = new Button();
        xmlLoadButton.setLabel("Load XML");
        xmlLoadButton.addActionListener(e -> {
            new XMLLoader(fileOpener.getPath()).loadSudoku(sudoku);
            currentError = "";
            print(sudoku);
        });
        xmlLoadButton.setBounds(10, offset + 4 * (buttonSize + gridSize), buttonSize * 9, buttonSize);
        this.add(xmlLoadButton);

        // XML Load Button

        Button xmlSaveButton = new Button();
        xmlSaveButton.setLabel("Save XML");
        xmlSaveButton.addActionListener(e -> new XMLSaver(sudoku).save(JOptionPane.showInputDialog("Enter Name:")));
        xmlSaveButton.setBounds(10, offset + 5 * (buttonSize + gridSize), buttonSize * 9, buttonSize);
        this.add(xmlSaveButton);


        // State display

        Label state = new Label() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                setText(sudoku.getState().getStateName());
            }
        };
        state.setBackground(Color.LIGHT_GRAY);
        state.setAlignment(Label.CENTER);
        state.setBounds(10, offset + 6 * (buttonSize + gridSize), buttonSize * 9, buttonSize);
        this.add(state);


        //Solve Button

        Button solveButton = new Button();
        solveButton.setLabel("Solve");
        solveButton.addActionListener(e -> {
            new BruteForceSolver(sudoku).solve();
            print(sudoku);
        });
        solveButton.setBounds(10, offset + 7 * (buttonSize + gridSize), buttonSize * 9, buttonSize);
        this.add(solveButton);

        // Sudoku Fields

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                // Generate single buttons
                SudokuButton b = new SudokuButton(i, j, sudoku);
                b.setLabel("");
                b.addActionListener(e -> {
                    currentX = ((SudokuButton) e.getSource()).x;
                    currentY = ((SudokuButton) e.getSource()).y;
                    text.requestFocus();
                });
                b.setBounds(i * buttonSize + 10, j * buttonSize + offset + 8 * (buttonSize + gridSize), buttonSize, buttonSize);
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

