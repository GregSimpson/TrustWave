package com.mvc.model;

/**
 * @author gjsimpso 
 * The Bet class. 
 * 
 */

public class Bet implements Comparable {

	private int horseNumber;
	private String horseName;
	private int amount;

	public Bet(int p_horseNumber, String p_horseName, int p_amount) {
		// TODO Auto-generated constructor stub
		this.horseNumber = p_horseNumber;
		this.horseName = p_horseName;
		this.amount = p_amount;
	}

	public int getHorseNumber() {
		return horseNumber;
	}

	protected void setHorseNumber(int p_horseNumber) {
		this.horseNumber = p_horseNumber;
	}

	public String getHorseName() {
		return horseName;
	}

	protected void setHorseName(String p_horseName) {
		this.horseName = p_horseName;
	}

	public int getAmount() {
		return amount;
	}

	protected void setAmount(int p_amount) {
		this.amount = p_amount;
	}

	@Override
	public int compareTo(Object p_aThat) {

		final int BEFORE = -1;
		final int EQUAL = 0;
		final int AFTER = 1;

		// this optimization is usually worthwhile, and can
		// always be added
		if (this == p_aThat)
			return EQUAL;

		final Bet that = (Bet) p_aThat;

		// primitive numbers follow this form
		if (this.getHorseNumber() < that.getHorseNumber())
			return BEFORE;
		if (this.getHorseNumber() > that.getHorseNumber())
			return AFTER;

		return EQUAL;
	}
}
