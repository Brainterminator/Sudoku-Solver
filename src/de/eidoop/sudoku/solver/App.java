package de.eidoop.sudoku.solver;

import de.eidoop.sudoku.api.entities.Sudoku;
import de.eidoop.sudoku.api.loader.ExampleLoader;
import de.eidoop.sudoku.api.loader.SudokuLoader;
import de.eidoop.sudoku.solver.frames.SudokuFrame;

public class App {
    public static void main(String[] args) {
        Sudoku sudoku = new Sudoku();
        new SudokuFrame(sudoku);
    }
}
