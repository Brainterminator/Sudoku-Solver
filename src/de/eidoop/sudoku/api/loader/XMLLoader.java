package de.eidoop.sudoku.api.loader;

import de.eidoop.sudoku.api.entities.Sudoku;

import javax.xml.XMLConstants;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class XMLLoader extends SudokuLoader{
    private String path;
    public XMLLoader(String path) {
        this.path = path;
    }

    @Override
    public void loadSudoku(Sudoku sudoku) {
        sudoku.reset();

        // Validiere das XML-Dokument gegen das XML-Schema
        if (!validateXml())
            return;

        // Lade das Sudoku aus der XML-Datei
        try {
            InputStream xmlInputStream = new FileInputStream(path);
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            parser.parse(xmlInputStream, new SudokuContentHandler(sudoku));
        } catch(Exception e){ }
    }

    private boolean validateXml() {
        try {
            // Erstelle ein Schema-Objekt basierend auf dem XML-Schema
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(new File("sudoku.xsd"));

            // Validiere das XML-Dokument gegen das Schema
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File(path)));
            return true; // Das XML-Dokument ist valide
        } catch (Exception e) {
            return false; // Das XML-Dokument ist nicht valide
        }
    }
}