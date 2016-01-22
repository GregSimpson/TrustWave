package com.mvc.services;

import java.util.Iterator;
import java.util.TreeSet;

import com.mvc.model.Bet;
import com.mvc.model.Cash;
import com.mvc.model.Horse;

public class BetServices {

	AtmServices atmSvcs = new AtmServices();
	StableServices stableSvcs = new StableServices();
	// BetServices betSvcs = new BetServices();

	public BetServices() {
		// TODO Auto-generated constructor stub
	}

	// public void placeBet(int p_horseNumber, String p_saveHorseName, int
	// p_amount, TreeSet<Bet> p_betSet, TreeSet<Horse> p_stableSet) {
	public void placeBet(int p_horseNumber, int p_amount, TreeSet<Bet> p_betSet, TreeSet<Horse> p_stableSet,
			TreeSet<Cash> p_cashSet) {

		Horse winner = null;
		for (Horse node : p_stableSet) {
			if (node.getNumber() == p_horseNumber) {
				winner = node;
			}
		}

		Bet newBet = new Bet(winner.getNumber(), winner.getName(), p_amount);
		if (p_betSet.contains(newBet)) {
			p_betSet.remove(newBet);
		}
		p_betSet.add(new Bet(winner.getNumber(), winner.getName(), p_amount));

		int payoutAmount = this.showResults(p_betSet, p_stableSet, p_cashSet);
	}

	public int showResults(TreeSet<Bet> p_betSet, TreeSet<Horse> p_stableSet, TreeSet<Cash> p_cashSet) {
		// TODO Auto-generated method stub
		Horse winningHorse = null;
		int winningTotal = 0;
		// iterate over the stable - find the winning horse number
		Iterator<Horse> stableSetITR = p_stableSet.iterator();
		while (stableSetITR.hasNext()) {
			Horse stableElem = stableSetITR.next();
			if (stableElem.getResult() == "won") {
				winningHorse = stableElem;
			}
		}

		Bet betToRemove = null;
		if (winningHorse != null) {

			Iterator<Bet> betSetITR = p_betSet.iterator();
			while (betSetITR.hasNext()) {
				Bet betElem = betSetITR.next();
				if (betElem.getHorseNumber() == winningHorse.getNumber()) {
					winningTotal = (betElem.getAmount() * winningHorse.getOdds());
					System.out.println("Payout: " + winningHorse.getName() + ",$" + winningTotal);
					betToRemove = betElem;
					// pay the winning amount
					if (winningTotal > 0) {
						atmSvcs.payBills(winningTotal, p_cashSet);
					}
				} else {
					System.out.println("No Payout: " + betElem.getHorseName());
				}
			}
		}
		if ((betToRemove != null) && (p_betSet.contains(betToRemove))) {
			p_betSet.remove(betToRemove);
		}
		return winningTotal;
	}
}
