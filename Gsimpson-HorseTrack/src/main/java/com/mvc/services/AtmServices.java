package com.mvc.services;

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

		// System.out.println("\nLoading Cash on Hand from XML file\n");
		p_cashSet.clear();

		String walletFile = "./bin/ATM.xml";

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

					// System.out.println("Denom :" + DENOMINATION);
					// System.out.println("Inven :" + INVENTORY.intValue());

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
		// Iterator<Cash> cashSetITR = cashSet.descendingIterator();

		while (cashSetITR.hasNext()) {
			Cash cashElem = cashSetITR.next();
			runningTotal += cashElem.getTotal();
		}
		return runningTotal;
	}

	public void payBills(int cost, TreeSet<Cash> p_cashSet) {

		TreeSet<Cash> l_proposedPayout = new TreeSet<Cash>();
		int billNumber = 0;

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
				l_proposedPayout.add(new Cash(cashElem.getDenomination(), billNumber));
			}
			if (runningTotal == cost) {
				// System.out.println("FINAL runningTotal = " + runningTotal);
				System.out.println("Dispensing:");
				// Iterator<Cash> payoutITR =
				// l_proposedPayout.descendingIterator();
				Iterator<Cash> payoutITR = l_proposedPayout.iterator();
				while (payoutITR.hasNext()) {
					Cash payoutElem = payoutITR.next();
					// if (payoutElem.getInventory() > 0) {
					System.out.println(payoutElem.getDenomination() + "," + payoutElem.getInventory());
					// }
				}
			} else {
				// we did not have the correct combination of bills to make a
				// payment
				// in this case, we replace the proposed bills to the wallet
				// and send this message
				System.out.println("DO NOT have the correct bills on hand to make :  $" + cost);
				// System.out.println("Unwinding l_proposedPayout - BEGIN");
				Iterator<Cash> payoutITR = l_proposedPayout.descendingIterator();
				while (payoutITR.hasNext()) {
					Cash payoutElem = payoutITR.next();

					/// find the matching cashSet element to replenish
					cashSetITR = p_cashSet.descendingIterator();
					while ((cashSetITR.hasNext()) && payoutElem.getInventory() > 0) {
						Cash cashElem = cashSetITR.next();
						if (cashElem.getDenomination().equals(payoutElem.getDenomination())) {
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
