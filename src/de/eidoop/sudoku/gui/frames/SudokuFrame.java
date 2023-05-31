package de.eidoop.sudoku.gui.frames;

import de.eidoop.sudoku.api.entities.Sudoku;
import de.eidoop.sudoku.api.enums.SudokuZustand;
import de.eidoop.sudoku.api.exceptions.*;
import de.eidoop.sudoku.api.lader.BeispielLader;
import de.eidoop.sudoku.api.lader.SudokuLader;
import de.eidoop.sudoku.api.loeser.Loeser;
import de.eidoop.sudoku.api.loeser.ProbierSudoku;
import de.eidoop.sudoku.api.loeser.StrategieSudoku;
import de.eidoop.sudoku.api.loeser.ZufallSudoku;
import de.eidoop.sudoku.api.ui.ISudokuAnzeige;
import de.eidoop.sudoku.gui.components.SudokuButton;
import de.eidoop.sudoku.gui.util.StateHandler;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SudokuFrame extends Frame implements ISudokuAnzeige {

    private String currentError = "Kein Fehler";
    private int currentX, currentY, currentValue;
    private Loeser loeser;
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
        loeser=new ProbierSudoku(sudoku);


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
            loeser.loesen();
            currentError = "Kein Fehler";
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
            currentError = "Kein Fehler";
            update();
        });
        neuButton.setBounds(10 + buttonGroesse * 4 + 10, 50 + buttonGroesse + 10, buttonGroesse * 4, buttonGroesse);
        this.add(neuButton);

        //Probier Button

        Button probierButton = new Button();
        probierButton.setLabel("Probieren");
        // ActionListener als anonyme Klasse mit Referenz auf lokale Variable
        probierButton.addActionListener(e -> setLoeser(new ProbierSudoku(sudoku)));
        probierButton.setBounds(10, 110 + buttonGroesse + 10, buttonGroesse * 4, buttonGroesse);
        this.add(probierButton);

        //Zufall Button

        Button zufallButton = new Button();
        zufallButton.setLabel("Zufall");
        // ActionListener als anonyme Klasse mit Referenz auf lokale Variable
        zufallButton.addActionListener(e -> setLoeser(new ZufallSudoku(sudoku)));
        zufallButton.setBounds(10, 140 + buttonGroesse + 10, buttonGroesse * 4, buttonGroesse);
        this.add(zufallButton);

        //Strategie Button

        Button strategieButton = new Button();
        strategieButton.setLabel("Strategie");
        // ActionListener als anonyme Klasse mit Referenz auf lokale Variable
        strategieButton.addActionListener(e -> setLoeser(new StrategieSudoku(sudoku)));
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
                setText(StateHandler.getZustandFromEnum(sudoku.getZustand()));
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
                currentError = "Kein Fehler";
                if (currentX < 8) currentX++;
                else {
                    currentX = 0;
                    if (currentY < 8) currentY++;
                    else {
                        currentX = 0;
                        currentY = 0;
                    }
                }
            } catch (WertInQuadrantVorhandenException ex) {
                currentError = "Quadrant belegt!";
            } catch (WertInSpalteVorhandenException ex) {
                currentError = "Spalte belegt!";
            } catch (WertInZeileVorhandenException ex) {
                currentError = "Zeile belegt!";
            } catch (WertebereichUngueltigException ex) {
                currentError = "Ungültiger Wertebereich!";
            } catch (FeldBelegtException ex) {
                currentError = "Feld belegt!";
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

    void setLoeser(Loeser loeser){
        this.loeser= loeser;
    }

    @Override
    public void druckeSudoku(Sudoku sudoku) {

    }
}
