package com.mvc.services;

/**
 * @author gjsimpso
 * The Services provide the utilities that are used by the Controllers
 */

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.mvc.model.Cash;

public class AtmServices {

	public AtmServices() {
		// TODO Auto-generated constructor stub
	}

	public SortedSet<Cash> loadWallet(SortedSet<Cash> p_cashSet) {

		// remove any exiting elements in the set
		p_cashSet.clear();

		String walletFile = "./bin/ATM.xml";

		// load the document above into a TreeSet
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();

			// Load the input XML document, parse it and return an instance of
			// the
			// Document class.
			Document document = (Document) builder.parse(new File(walletFile));

			NodeList nodeList = document.getDocumentElement().getChildNodes();

			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);

				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element elem = (Element) node;

					// Get the value of all sub-elements.
					String DENOMINATION = elem.getElementsByTagName("denomination").item(0).getChildNodes().item(0)
							.getNodeValue();

					// Get the value of the NUMBER attribute.
					Integer INVENTORY = Integer.parseInt(
							elem.getElementsByTagName("inventory").item(0).getChildNodes().item(0).getNodeValue());

					p_cashSet.add(new Cash(DENOMINATION, INVENTORY.intValue()));
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

		return p_cashSet;

	}

	public void showWallet(TreeSet<Cash> p_cashSet) {
		System.out.println("Inventory:");
		Iterator<Cash> cashSetITR = p_cashSet.iterator();

		while (cashSetITR.hasNext()) {
			System.out.println(cashSetITR.next());
		}
	}

	public int getWalletTotal(TreeSet<Cash> p_cashSet) {
		int runningTotal = 0;
		Iterator<Cash> cashSetITR = p_cashSet.iterator();

		while (cashSetITR.hasNext()) {
			Cash cashElem = cashSetITR.next();
			runningTotal += cashElem.getTotal();
		}
		return runningTotal;
	}

	public void payBills(int cost, TreeSet<Cash> p_cashSet) {

		TreeSet<Cash> l_proposedPayout = new TreeSet<Cash>();
		int billNumber = 0;

		// this decides if there is enough money on hand to pay the amount
		// this does not guarantee that we have the correct combination of bills
		// on hand
		if (cost <= this.getWalletTotal(p_cashSet)) {
			int runningTotal = 0;

			Iterator<Cash> cashSetITR = p_cashSet.descendingIterator();
			while ((cashSetITR.hasNext()) && (runningTotal < cost)) {
				Cash cashElem = cashSetITR.next();
				billNumber = 0;

				while ((cashElem.getInventory() > 0) && (runningTotal < cost)
						&& ((cost - runningTotal) >= cashElem.getOneBillValue())) {
					runningTotal += cashElem.payOneBill();
					billNumber += 1;
				}
				// add the proposed payout bills to a treeSet
				// they are proposed until we are certain that we can meet the
				// amount
				l_proposedPayout.add(new Cash(cashElem.getDenomination(), billNumber));
			}
			if (runningTotal == cost) {
				System.out.println("Dispensing:");

				Iterator<Cash> payoutITR = l_proposedPayout.iterator();
				while (payoutITR.hasNext()) {
					Cash payoutElem = payoutITR.next();
					System.out.println(payoutElem.getDenomination() + "," + payoutElem.getInventory());
				}
			} else {
				// we did not have the correct combination of bills to make a
				// payment
				// in this case, we replace the proposed bills to the wallet
				// and send this message
				System.out.println("DO NOT have the correct bills on hand to make :  $" + cost);
				Iterator<Cash> payoutITR = l_proposedPayout.descendingIterator();
				while (payoutITR.hasNext()) {
					Cash payoutElem = payoutITR.next();

					/// find the matching cashSet element to replenish
					cashSetITR = p_cashSet.descendingIterator();
					while ((cashSetITR.hasNext()) && payoutElem.getInventory() > 0) {
						Cash cashElem = cashSetITR.next();
						if (cashElem.getDenomination().equals(payoutElem.getDenomination())) {
							// return the number of bills that we had proposed
							cashElem.addInventory(payoutElem.getInventory());
						}
					}
				}
			}
		} else {
			System.out.println("Cannot dispense more than : " + this.getWalletTotal(p_cashSet));
		}
	}
}
