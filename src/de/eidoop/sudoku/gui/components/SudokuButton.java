package de.eidoop.sudoku.gui.components;

import de.eidoop.sudoku.api.entities.Sudoku;

import java.awt.*;

public class SudokuButton extends Button {
    public int x,y;
    private final Sudoku sudoku;
    public SudokuButton(int x, int y, Sudoku sudoku){
        super();
        this.x = x;
        this.y = y;
        this.sudoku = sudoku;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if(sudoku.getFeld(x,y).getWert()==0)setLabel("");
        else setLabel(Integer.toString(sudoku.getFeld(x,y).getWert()));
    }
}
