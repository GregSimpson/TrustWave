package com.mvc.services;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.TreeSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.mvc.model.Horse;

public class StableServices {

	public StableServices() {
		// TODO Auto-generated constructor stub
	}

	public TreeSet<Horse> loadStable(TreeSet<Horse> p_stableSet) {
		p_stableSet.clear();

		String stableFile = "./bin/Stable.xml";

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();

			// Load the input XML document, parse it and return an instance of
			// the
			// Document class.
			Document document = (Document) builder.parse(new File(stableFile));

			NodeList nodeList = document.getDocumentElement().getChildNodes();

			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);

				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element elem = (Element) node;

					// Get the value of the NUMBER attribute.
					Integer NUMBER = Integer.parseInt(
							elem.getElementsByTagName("number").item(0).getChildNodes().item(0).getNodeValue());

					// Get the value of all sub-elements.
					String NAME = elem.getElementsByTagName("name").item(0).getChildNodes().item(0).getNodeValue();

					// Get the value of the NUMBER attribute.
					Integer ODDS = Integer
							.parseInt(elem.getElementsByTagName("odds").item(0).getChildNodes().item(0).getNodeValue());

					p_stableSet.add(new Horse(NUMBER.intValue(), NAME, ODDS.intValue()));
				}
			}

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return p_stableSet;
	}

	public void showStable(TreeSet<Horse> p_stableSet) {
		System.out.println("Horses:");
		Iterator<Horse> itr = p_stableSet.iterator();
		while (itr.hasNext()) {
			System.out.println(itr.next());
		}
	}

	public void setWinner(int p_winner, TreeSet<Horse> p_stableSet) {

		Iterator<Horse> stableSetITR = p_stableSet.iterator();
		while (stableSetITR.hasNext()) {

			Horse horseElem = stableSetITR.next();
			if (horseElem.getNumber() == p_winner) {
				horseElem.setResult("won");
			} else {
				horseElem.setResult("lost");
			}
		}
	}
}
