package de.eidoop.sudoku.api.handler;

import de.eidoop.sudoku.api.entities.Sudoku;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class SudokuContentHandler extends DefaultHandler {
    private StringBuilder cellValue;
    private final Sudoku sudoku;
    private int row = 1;
    private int col = 1;

    public SudokuContentHandler(Sudoku sudoku) {
        this.sudoku = sudoku;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if (qName.equals("cell")) {
            cellValue = new StringBuilder();
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        if (cellValue != null) {
            cellValue.append(ch, start, length);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        try {
            if (!qName.equals("cell"))
                return;

            if (col > 9) {
                col = 1;
                row++;
            }

            int value = cellValue.length() > 0 ? Integer.parseInt(cellValue.toString().trim()) : 0;
            if (value != 0)
                sudoku.setFixedValue(row, col, value);
            
            col++;
        } catch (Exception e) {
            System.err.println("END ELEMENT ERROR");
        }
    }
}