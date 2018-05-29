/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.feel.questxml;

import java.io.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import org.apache.log4j.Logger;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

/**
 *
 * @author Anton
 */
public class XmlAccess {
	private final DocumentBuilderFactory factory;
	private final DocumentBuilder builder;
	private final Document document;
	private final XPathFactory xFactory;
	private final XPath xPath;
	private static Logger log;
	
	public XmlAccess() throws ParserConfigurationException, IOException, XPathExpressionException, SAXException{
		log = Logger.getLogger(XmlAccess.class.getSimpleName());
		this.factory = DocumentBuilderFactory.newInstance();
		this.builder = factory.newDocumentBuilder();
		this.document = builder.parse(new File("src/source_file.xml"));
		this.xFactory = XPathFactory.newInstance();
		this.xPath = xFactory.newXPath();	
		
	}
	
	public void updateNode(String nameOfNodeEdited, String xPathQueryForNode) throws XPathExpressionException{
		log.info("updateNode method is started");
		try{
			NodeList nodeList = (NodeList) xPath.evaluate(xPathQueryForNode, document, XPathConstants.NODESET);
			if(nodeList.getLength() != 0){
				int changedElementCount = 0;
				int addedElementCount = 0;
				Node neutralNode;
				Node actorColor;
				Element neutralElement;
				Element actorColorElement;
				NamedNodeMap atributes;
				for(int i = 0; i < nodeList.getLength(); i++){
					neutralElement = (Element) nodeList.item(i);
					actorColor  = neutralElement.getElementsByTagName(nameOfNodeEdited).item(0);
					if(actorColor != null){
						atributes = actorColor.getAttributes();
						atributes.item(0).setNodeValue("255");
						atributes.item(1).setNodeValue("255");
						atributes.item(2).setNodeValue("0");
						changedElementCount++;
					}else{
						neutralNode = nodeList.item(i);
						actorColorElement = document.createElement(nameOfNodeEdited);
						neutralNode.appendChild(actorColorElement);
						actorColorElement.setAttribute("B", "0");
						actorColorElement.setAttribute("G", "0");
						actorColorElement.setAttribute("R", "255");	
						addedElementCount++;
					}	

				}
				log.info("Colors added and updated successfully. Was changed " + changedElementCount + " elements, and "
						+ addedElementCount + " elements added");
			}else{
				log.warn("Result of XPath query is empty, element is not changed and is not added ");
			}
		}catch(XPathExpressionException | DOMException t){
			log.error("can't update or add node", t);
		}
		
	}
	
	public void writeResult(String nameFile) throws IOException{
		log.info("writeResult method is started");
		try {
		  Transformer t = TransformerFactory.newInstance().newTransformer();
		  DocumentType dt = document.getDoctype();
		  if (dt != null) {
			String pub = dt.getPublicId();
			if (pub != null) {
			  t.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, pub);
			}
			t.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, dt.getSystemId());
		  }
		  t.setOutputProperty(OutputKeys.ENCODING, "UTF-8"); // NOI18N
		  t.setOutputProperty(OutputKeys.INDENT, "yes"); // NOI18N
		  t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4"); // NOI18N
		  Source source = new DOMSource(document);
		  Result result = new StreamResult(nameFile);
		  t.transform(source, result);
		} catch (IllegalArgumentException | TransformerException | TransformerFactoryConfigurationError e) {
			log.error("Result not write", e);
			throw new IOException(e);
		}
		log.info("Result write in file successfully");
	}
}
