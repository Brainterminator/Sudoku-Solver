package de.eidoop.sudoku.frames;

import de.eidoop.sudoku.app.enums.SudokuZustand;
import de.eidoop.sudoku.app.lader.BeispielLader;
import de.eidoop.sudoku.app.lader.SudokuLader;
import de.eidoop.sudoku.app.sudokus.Sudoku;
import de.eidoop.sudoku.components.SudokuButton;
import de.eidoop.sudoku.interfaces.ISudokuAnzeige;
import de.eidoop.sudoku.util.StateHandler;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SudokuFrame extends Frame implements ISudokuAnzeige {

    private int currentError = 0;
    private int currentX, currentY, currentValue, currentMethod;
    private final TextField text;
    private final Sudoku sudoku;

    public SudokuFrame() {
        super("Sudoku App");
        int fensterHoehe = 545;
        int fensterBreite = 245;
        this.setSize(fensterBreite, fensterHoehe);
        // ShutdownAdapter als anonyme Klasse
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        // Layout ohne Sonderverhalten setzten (siehe später LayoutManager)
        this.setLayout(null);
        this.setResizable(false);


        // Sudoku erstellen

        SudokuLader lader = new BeispielLader();
        sudoku = new Sudoku();
        lader.ladeSudoku(sudoku);


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
            sudoku.loesen(currentMethod);
            currentError = 0;
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
            sudoku.setZustand(SudokuZustand.GELADEN);
            currentError = 0;
            update();
        });
        neuButton.setBounds(10 + buttonGroesse * 4 + 10, 50 + buttonGroesse + 10, buttonGroesse * 4, buttonGroesse);
        this.add(neuButton);

        //Probier Button

        Button probierButton = new Button();
        probierButton.setLabel("Probieren");
        // ActionListener als anonyme Klasse mit Referenz auf lokale Variable
        probierButton.addActionListener(e -> currentMethod = 1);
        probierButton.setBounds(10, 110 + buttonGroesse + 10, buttonGroesse * 4, buttonGroesse);
        this.add(probierButton);

        //Zufall Button

        Button zufallButton = new Button();
        zufallButton.setLabel("Zufall");
        // ActionListener als anonyme Klasse mit Referenz auf lokale Variable
        zufallButton.addActionListener(e -> currentMethod = 2);
        zufallButton.setBounds(10, 140 + buttonGroesse + 10, buttonGroesse * 4, buttonGroesse);
        this.add(zufallButton);

        //Strategie Button

        Button strategieButton = new Button();
        strategieButton.setLabel("Strategie");
        // ActionListener als anonyme Klasse mit Referenz auf lokale Variable
        strategieButton.addActionListener(e -> currentMethod = 3);
        strategieButton.setBounds(10, 170 + buttonGroesse + 10, buttonGroesse * 4, buttonGroesse);
        this.add(strategieButton);


        // Fehleranzeige

        Label fehlerAnzeige = new Label() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                setText(StateHandler.getError(currentError));
            }
        };
        fehlerAnzeige.setBounds(10, 80 + buttonGroesse + 10, buttonGroesse * 8, buttonGroesse);
        this.add(fehlerAnzeige);

        // Zustand

        Label zustand = new Label() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                setText(StateHandler.getZustandFromEnum(sudoku.getZustand()));
            }
        };
        zustand.setBounds(10, 230 + buttonGroesse + 10, buttonGroesse * 4, buttonGroesse);
        this.add(zustand);


        // Text Feld
        text = new TextField();
        text.addActionListener(e -> {
            currentValue = Integer.parseInt(((TextField) e.getSource()).getText());
            if (sudoku.setFixedValue(currentY + 1, currentX + 1, currentValue)) {
                currentError = 0;
                if (currentX < 8) currentX++;
                else {
                    currentX = 0;
                    if (currentY < 8) currentY++;
                    else {
                        currentX = 0;
                        currentY = 0;
                    }
                }
            } else currentError = 1;
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
}
