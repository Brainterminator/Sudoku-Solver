package de.eidoop.sudoku.api.loader;

import de.eidoop.sudoku.api.entities.Sudoku;
import de.eidoop.sudoku.api.enums.SudokuState;
import de.eidoop.sudoku.api.handler.SudokuContentHandler;

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
    private final String path;
    public XMLLoader(String path) {
        this.path = path;
    }

    @Override
    public void loadSudoku(Sudoku sudoku) {
        sudoku.reset();

        // Validate the XML-Document against the XML-Scheme
        if (!validateXml())
            return;

        // Load the Sudoku from the XML-File
        try {
            InputStream xmlInputStream = new FileInputStream(path);
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            parser.parse(xmlInputStream, new SudokuContentHandler(sudoku));
        } catch(Exception e){
            sudoku.reset();
            sudoku.setState(SudokuState.EMPTY);
        }
    }

    private boolean validateXml() {
        try {
            // Create a Scheme-Object base on the XML-Scheme
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(new File("sudoku.xsd"));

            // Validate the XML-Document against the XML-Scheme
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File(path)));
            return true; // The XML-Document is valid
        } catch (Exception e) {
            return false; // The XML-Document is not valid
        }
    }
}