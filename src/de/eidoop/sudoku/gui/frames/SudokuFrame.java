package de.eidoop.sudoku.gui.frames;

import de.eidoop.sudoku.api.entities.Sudoku;
import de.eidoop.sudoku.api.enums.SudokuState;
import de.eidoop.sudoku.api.exceptions.*;
import de.eidoop.sudoku.api.solver.BruteForceSolver;
import de.eidoop.sudoku.api.solver.RandomSolver;
import de.eidoop.sudoku.api.solver.SaveSolver;
import de.eidoop.sudoku.api.solver.Solver;
import de.eidoop.sudoku.api.ui.ISudokuRenderer;
import de.eidoop.sudoku.gui.components.SudokuButton;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SudokuFrame extends Frame implements ISudokuRenderer {

    private String currentError = "";
    private int currentX, currentY, currentValue;
    private Solver solver;
    private final TextField text;
    private final Sudoku sudoku;

    public SudokuFrame(Sudoku sudoku) {
        super("Sudoku App");
        int height = 545;
        int width = 245;
        this.setSize(width, height);
        this.sudoku = sudoku;
        this.solver = new BruteForceSolver(sudoku);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        this.setLayout(null);
        this.setResizable(false);


        // Schaltflächen generieren und platzieren

        // Sudoku Felder
        int buttonGroesse = 25;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                // Neue Schaltfläche instanziieren
                SudokuButton b = new SudokuButton(i, j, sudoku);
                b.setLabel("");
                // ActionListener als anonyme Klasse mit Referenz auf lokale Variable
                b.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        currentX = ((SudokuButton) e.getSource()).x;
                        currentY = ((SudokuButton) e.getSource()).y;
                        text.requestFocus();
                    }
                });
                // Position auf dem Elternelement (x, y, breite, hoehe)
                b.setBounds(i * buttonGroesse + 10, j * buttonGroesse + 310, buttonGroesse, buttonGroesse);
                this.add(b);
            }
        }

        //Lösen Button

        Button loesenButton = new Button();
        loesenButton.setLabel("Lösen");
        // ActionListener als anonyme Klasse mit Referenz auf lokale Variable
        loesenButton.addActionListener(e -> {
            solver.solve();
            currentError = "";
            update();
        });
        loesenButton.setBounds(10, 50 + buttonGroesse + 10, buttonGroesse * 4, buttonGroesse);
        this.add(loesenButton);

        //Neu Button

        Button neuButton = new Button();
        neuButton.setLabel("Neu");
        // ActionListener als anonyme Klasse mit Referenz auf lokale Variable
        neuButton.addActionListener(e -> {
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    sudoku.setEmpty(i, j);
                }
            }
            sudoku.setState(SudokuState.LOADED);
            currentError = "";
            update();
        });
        neuButton.setBounds(10 + buttonGroesse * 4 + 10, 50 + buttonGroesse + 10, buttonGroesse * 4, buttonGroesse);
        this.add(neuButton);

        //Probier Button

        Button probierButton = new Button();
        probierButton.setLabel("Probieren");
        // ActionListener als anonyme Klasse mit Referenz auf lokale Variable
        probierButton.addActionListener(e -> setLoeser(new BruteForceSolver(sudoku)));
        probierButton.setBounds(10, 110 + buttonGroesse + 10, buttonGroesse * 4, buttonGroesse);
        this.add(probierButton);

        //Zufall Button

        Button zufallButton = new Button();
        zufallButton.setLabel("Zufall");
        // ActionListener als anonyme Klasse mit Referenz auf lokale Variable
        zufallButton.addActionListener(e -> setLoeser(new RandomSolver(sudoku)));
        zufallButton.setBounds(10, 140 + buttonGroesse + 10, buttonGroesse * 4, buttonGroesse);
        this.add(zufallButton);

        //Strategie Button

        Button strategieButton = new Button();
        strategieButton.setLabel("Strategie");
        // ActionListener als anonyme Klasse mit Referenz auf lokale Variable
        strategieButton.addActionListener(e -> setLoeser(new SaveSolver(sudoku)));
        strategieButton.setBounds(10, 170 + buttonGroesse + 10, buttonGroesse * 4, buttonGroesse);
        this.add(strategieButton);


        // Fehleranzeige

        Label fehlerAnzeige = new Label() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                setText(currentError);
            }
        };
        fehlerAnzeige.setBounds(10, 80 + buttonGroesse + 10, buttonGroesse * 8, buttonGroesse);
        this.add(fehlerAnzeige);

        // Zustand

        Label zustand = new Label() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                setText(sudoku.getState().getStateName());
            }
        };
        zustand.setBounds(10, 230 + buttonGroesse + 10, buttonGroesse * 4, buttonGroesse);
        this.add(zustand);


        // Text Feld
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
                currentError = "Quadrant belegt!";
            } catch (ColumnOccupiedException ex) {
                currentError = "Spalte belegt!";
            } catch (RowOccupiedException ex) {
                currentError = "Zeile belegt!";
            } catch (ValueOutOfRangeException ex) {
                currentError = "Ungültiger Wertebereich!";
            } catch (FieldFixedException ex) {
                currentError = "Feld belegt!";
            } catch (CoordinatesOutOfRangeException ex) {
                currentError = "Coordinates out of Range!";
            }
            text.setText("");
            update();

        });
        text.setBounds(10, 50, buttonGroesse * 9, buttonGroesse);
        this.add(text);


        this.setVisible(true);
    }

    void update() {
        this.setVisible(false);
        this.setVisible(true);
    }

    void setLoeser(Solver solver){
        this.solver= solver;
    }
    @Override
    public void print(Sudoku sudoku) {

    }
}

