package com.mvc.controller;

/**
 * @author gjsimpso
 *  This is the main functional class of the project.
 *  It handles the user input and manages the functionality
 *  
 */

import java.util.Scanner;
import java.util.TreeSet;

import com.mvc.model.Bet;
import com.mvc.model.Cash;
import com.mvc.model.Horse;
import com.mvc.services.AtmServices;
import com.mvc.services.BetServices;
import com.mvc.services.StableServices;

public class TrackController {

	static String choice;
	public static int ArraySize = 3;

	TreeSet<Horse> stableSet = new TreeSet<Horse>();
	TreeSet<Cash> cashSet = new TreeSet<Cash>();
	TreeSet<Bet> betSet = new TreeSet<Bet>();

	public TrackController() {
		// TODO Auto-generated constructor stub
	}

	public void trackATM() {
		AtmServices atmSvcs = new AtmServices();
		atmSvcs.loadWallet(cashSet);
		// atmSvcs.showWallet(cashSet);

		StableServices stableSvcs = new StableServices();
		stableSet = stableSvcs.loadStable(stableSet);
		// stableSvcs.showStable(stableSet);

		BetServices betSvcs = new BetServices();

		Scanner sc = new Scanner(System.in);

		while (true) {
			atmSvcs.showWallet(cashSet);
			stableSvcs.showStable(stableSet);

			choice = sc.nextLine();

			String[] inputCharArray = choice.split(" ");
			String part1 = inputCharArray[0];

			if (part1.matches("\\d+$")) {

				if (inputCharArray.length > 1) {
					try {
						int part2 = new Integer(inputCharArray[1]).intValue();
						Integer inputInt = new Integer(part1);
						switch (inputInt) {
						case 1:
						case 2:
						case 3:
						case 4:
						case 5:
						case 6:
						case 7: {
							betSvcs.placeBet(new Integer(part1).intValue(), part2, betSet, stableSet, cashSet);
							break;
						}
						default: {
							System.out.println("Invalid Bet: " + part1);
						}
						}
					} catch (NumberFormatException e) {
						System.out.println("Invalid Bet: " + inputCharArray[1]);
					}
				} else {
					// System.out.println("you did not provide a bet amount");
				}

			} else {
				switch (part1) {
				case "Q":
				case "q": {
					sc.close();
					System.exit(0);
				}
				case "R":
				case "r": {
					//reload the ATM
					atmSvcs.loadWallet(cashSet);
					//reload the stable
					stableSvcs.loadStable(stableSet);
					// remove all existing bets
					betSet.clear();
					break;
				}
				case "W":
				case "w": {

					if (inputCharArray.length > 1) {
						String part2 = inputCharArray[1];

						try {
							Integer inputInt2 = new Integer(part2);
							switch (inputInt2) {
							case 1:
							case 2:
							case 3:
							case 4:
							case 5:
							case 6:
							case 7: {

								stableSvcs.setWinner(inputInt2.intValue(), stableSet);
								stableSvcs.showStable(stableSet);

								// check the current betSet each time you set a
								// new winner
								Horse winner = null;
								for (Horse node : stableSet) {
									if (node.getNumber() == inputInt2.intValue()) {
										winner = node;
									}
								}

								// check the bets for a winner
								int payoutAmount = betSvcs.showResults(betSet, stableSet, cashSet);

								break;
							}
							default: {
								System.out.println("Invalid input" + part1);
							}
							}
						} catch (NumberFormatException e) {
							System.out.println("not an integer");
						}
					}
					break;
				}
				default:
					System.out.println("Invalid char input" + part1);
				}
			}
		}
	}
}
