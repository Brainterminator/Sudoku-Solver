package de.eidoop.sudoku.util;

import de.eidoop.sudoku.app.enums.SudokuZustand;

public class StateHandler {
    public static String getError(int i){
        switch (i){
            case 0:
                return "Kein Fehler";
            case 1:
                return "Ungueltige Eingabe";
            default:
                return "Unbekannter Fehler";
        }
    }
    public static String getZustandFromEnum(SudokuZustand zustand){
        switch (zustand){
            case LEER:
                return "Leer";
            case GELADEN:
                return "Geladen";
            case LOESUNGSVERSUCH:
                return "Loesungsversuch";
            case GELOEST:
                return "Gelöst";
            case UNLOESBAR:
                return "Unlösbar";
            default:
                return "Zustands-Fehler";
        }
    }
}
