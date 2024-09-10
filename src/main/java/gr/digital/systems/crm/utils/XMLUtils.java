package gr.digital.systems.crm.utils;

import gr.digital.systems.crm.exception.CrmException;
import java.io.File;
import java.util.Map;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLUtils {

	private XMLUtils() {}

	/**
	 * Transforms the map to xml document and saves the file at the given path.
	 *
	 * @param filePath The path to save the generated xml document.
	 * @param fileName The name of the XML document.
	 * @param fileStructure The structure of the xml document.
	 * @return Document
	 */
	public static Document createXmlDocument(
			final String filePath,
			final String fileName,
			final Map<String, Map<String, String>> fileStructure) {

		final var document = buildXml(fileStructure);
		saveXMLDocumentToFile(document, filePath, fileName);
		return document;
	}

	/**
	 * Create a new Document.
	 *
	 * @return Created Document.
	 */
	private static Document createDocument() {
		var docFactory = DocumentBuilderFactory.newInstance();

		DocumentBuilder docBuilder;
		try {
			docBuilder = docFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new CrmException("Cannot build a new Document", e);
		}

		return docBuilder.newDocument();
	}

	/**
	 * Creates the documents xml structure.
	 *
	 * @param fileStructure The map structure to be transformed to xml structure.
	 * @return The Transformed document with the xml structure.
	 */
	private static Document buildXml(final Map<String, Map<String, String>> fileStructure) {

		/* Create an XML document */
		final var document = createDocument();

		/* Fill the document based on input fileStructure */
		fileStructure.forEach(
				(k, v) -> {
					var element = document.createElement(k);
					document.appendChild(element);
					createKVNode(element, document, v);
				});

		return document;
	}

	/**
	 * Transforms all the key value entries from map to child xml elements and writes them to the
	 * document.
	 *
	 * @param element The main element to append the child elements .
	 * @param document The document to edit and add all the child elements.
	 * @param entrySet The set of child elements.
	 */
	private static void createKVNode(
			final Element element, final Document document, final Map<String, String> entrySet) {

		entrySet.forEach(
				(k, v) -> {
					var parameter = document.createElement("parameter");
					var key = document.createElement("key");
					var value = document.createElement("value");
					key.appendChild(document.createTextNode(k));
					value.appendChild(document.createTextNode(v));
					parameter.appendChild(key);
					parameter.appendChild(value);
					element.appendChild(parameter);
				});
	}

	/**
	 * Transforms the given Document into an xml document and saves it into the given directory if
	 * exists otherwise creates the directory too.
	 *
	 * @param document The document to be transformed and saved into the given directory.
	 * @param directoryPath The path to the directory to save the document, it is created if it
	 *     doesn't exist.
	 * @param fileName The name of the file to be saved into the directory.
	 */
	private static void saveXMLDocumentToFile(
			final Document document, final String directoryPath, String fileName) {

		/* Create directory if it does not already exist */
		FileUtils.createDirectory(directoryPath);

		/* Create XML file result */
		var outputFile = new File(directoryPath + fileName);
		var result = new StreamResult(outputFile);

		/* Create XML source of document to be saved in XML file */
		var source = new DOMSource(document);

		/* Create transformer to process the XML source and write the transformation output to the XML file */
		var transformerFactory = TransformerFactory.newInstance();
		transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
		transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");

		try {
			var transformer = transformerFactory.newTransformer();
			transformer.transform(source, result);
		} catch (TransformerException e) {
			throw new CrmException("Cannot transform and save the document", e);
		}
	}
}
