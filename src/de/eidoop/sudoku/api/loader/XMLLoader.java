package de.eidoop.sudoku.api.loader;

import de.eidoop.sudoku.api.entities.Sudoku;

public class XMLLoader extends SudokuLoader{
    private String path;
    public XMLLoader(String path) {
        this.path = path;
    }

    @Override
    public void loadSudoku(Sudoku sudoku) {
        sudoku.reset();
        //Code here that places the Values inside the sudoku
    }
}
