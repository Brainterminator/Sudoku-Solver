package de.eidoop.sudoku.solver.components;

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
        if(sudoku.getField(x,y).getValue()==0)setLabel("");
        else setLabel(Integer.toString(sudoku.getField(x,y).getValue()));
    }
}
