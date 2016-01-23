package com.mvc.services;

/**
 * @author gjsimpso
 * The Services provide the utilities that are used by the Controllers
 */

import java.util.Iterator;
import java.util.TreeSet;

import com.mvc.model.Bet;
import com.mvc.model.Cash;
import com.mvc.model.Horse;

public class BetServices {

	AtmServices atmSvcs = new AtmServices();
	StableServices stableSvcs = new StableServices();

	public BetServices() {
		// TODO Auto-generated constructor stub
	}

	public void placeBet(int p_horseNumber, int p_amount, TreeSet<Bet> p_betSet, TreeSet<Horse> p_stableSet,
			TreeSet<Cash> p_cashSet) {

		// retrieve the winning horse, we need his name for the bet
		// FOR loop version
		Horse winner = null;
		for (Horse node : p_stableSet) {
			if (node.getNumber() == p_horseNumber) {
				winner = node;
			}
		}

		// set the attributes of the bet
		Bet newBet = new Bet(winner.getNumber(), winner.getName(), p_amount);
		if (p_betSet.contains(newBet)) {
			p_betSet.remove(newBet);
		}
		p_betSet.add(new Bet(winner.getNumber(), winner.getName(), p_amount));

		int payoutAmount = this.showResults(p_betSet, p_stableSet, p_cashSet);
	}

	public int showResults(TreeSet<Bet> p_betSet, TreeSet<Horse> p_stableSet, TreeSet<Cash> p_cashSet) {
		Horse winningHorse = null;
		int winningTotal = 0;

		// iterate over the stable - find the winning horse number
		//  WHILE loop version
		Iterator<Horse> stableSetITR = p_stableSet.iterator();
		while (stableSetITR.hasNext()) {
			Horse stableElem = stableSetITR.next();
			if (stableElem.getResult() == "won") {
				winningHorse = stableElem;
			}
		}

		Bet betToRemove = null;
		if (winningHorse != null) {

			// iterate over the bets - see if any bets match the winner
			Iterator<Bet> betSetITR = p_betSet.iterator();
			while (betSetITR.hasNext()) {
				Bet betElem = betSetITR.next();

				if (betElem.getHorseNumber() == winningHorse.getNumber()) {
					// there is a matching bet
					winningTotal = (betElem.getAmount() * winningHorse.getOdds());
					System.out.println("Payout: " + winningHorse.getName() + ",$" + winningTotal);
					// we only pay the winners one time, but we cannot remove them 
					//  from inside the iterator
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
		// here we actually remove the paid bet
		if ((betToRemove != null) && (p_betSet.contains(betToRemove))) {
			p_betSet.remove(betToRemove);
		}
		return winningTotal;
	}
}
