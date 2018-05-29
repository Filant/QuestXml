/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.feel.questxml;

import java.io.*;
import javax.xml.parsers.*;
import javax.xml.xpath.*;
import org.xml.sax.SAXException;

/**
 *
 * @author Anton
 */
public class Main {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) throws ParserConfigurationException, IOException, XPathExpressionException, SAXException {
		XmlAccess xmlAccess = new XmlAccess();
		xmlAccess.updateNode("Actor.Color", "//Neutral[*//Origin]");
		xmlAccess.writeResult("src/target_file.xml");
		
	}
}	
