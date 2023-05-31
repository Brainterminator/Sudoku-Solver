package de.eidoop.sudoku;

import de.eidoop.sudoku.frames.SudokuFrame;

public class App {
    public static void main(String[] args) {
        SudokuFrame sudokuFrame = new SudokuFrame();
        sudokuFrame.validate();
    }
}
