package de.eidoop.sudoku.api.saver;

import de.eidoop.sudoku.api.entities.Sudoku;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class XMLSaver {
    private final Sudoku sudoku;

    public XMLSaver(Sudoku sudoku) {
        this.sudoku = sudoku;
    }

    public void save(String name){
        try {
            // Create a new XML document
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            // Create the root element
            Element rootElement = doc.createElement("sudoku");
            doc.appendChild(rootElement);

            // Iterate over the Sudoku grid and create XML elements for each cell
            for (int i = 0; i < 9; i++) {
                Element rowElement = doc.createElement("row");
                rootElement.appendChild(rowElement);

                for (int j = 0; j < 9; j++) {
                    Element cellElement = doc.createElement("cell");
                    cellElement.appendChild(doc.createTextNode(String.valueOf(sudoku.getField(j,i).getValue())));
                    rowElement.appendChild(cellElement);
                }
            }

            // Save the XML document to a file
            File file = new File(name + ".xml");
            javax.xml.transform.TransformerFactory transformerFactory = javax.xml.transform.TransformerFactory.newInstance();
            javax.xml.transform.Transformer transformer = transformerFactory.newTransformer();
            javax.xml.transform.dom.DOMSource source = new javax.xml.transform.dom.DOMSource(doc);
            javax.xml.transform.stream.StreamResult result = new javax.xml.transform.stream.StreamResult(file);
            transformer.transform(source, result);

            System.out.println("Sudoku saved to " + name + ".xml");

        } catch (Exception e) {
            e.printStackTrace();
        }











    }
}
