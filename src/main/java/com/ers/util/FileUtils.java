package com.ers.util;

import com.ers.exception.TransformDataException;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.StringReader;

/**
 * File utility class.
 *
 * @author Roman Batygin
 */
public class FileUtils {

    private FileUtils() {
    }

    /**
     * Saves xml string to file.
     *
     * @param xml     - xml string
     * @param xmlFile - xml file
     */
    public static void saveXmlToFile(String xml, File xmlFile) {
        try (StringReader reader = new StringReader(xml)) {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(
                    new InputSource(reader));
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(new DOMSource(document), new StreamResult(xmlFile));
        } catch (Exception ex) {
            throw new TransformDataException(ex.getMessage());
        }
    }
}
