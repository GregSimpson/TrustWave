/**
 * 
 */
package com.mvc.model;

/**
 * @author gjsimpso
 *
 */
public class Cash implements Comparable {

	private String denomination;
	private int inventory;

	/**
	 * 
	 */
	public Cash(String p_denomination, int p_inventory) {
		// TODO Auto-generated constructor stub
		this.denomination = p_denomination;
		this.inventory = p_inventory;
	}

	public String toString() {
		return denomination + "," + inventory;
	}

	public String getDenomination() {
		return denomination;
	}

	protected void setDenomination(String p_denomination) {
		this.denomination = p_denomination;
	}

	public int getInventory() {
		return inventory;
	}

	protected void setInventory(int p_inventory) {
		this.inventory = p_inventory;
	}

	protected int subtractInventory(int p_numberOfBills) {
		if (p_numberOfBills <= this.getInventory()) {
			this.setInventory(this.getInventory() - p_numberOfBills);
		}
		return this.getInventory();
	}

	public void addInventory(int p_numberOfBills) {
		this.setInventory(this.getInventory() + p_numberOfBills);
	}

	public int getTotal() {
		return (this.getInventory() * this.getOneBillValue());
	}

	public int getOneBillValue() {
		return new Integer(this.getDenomination().substring(1, getDenomination().length())).intValue();
	}

	public int payOneBill() {
		if (this.getInventory() > 0) {
			this.subtractInventory(1);
			return this.getOneBillValue();
		} else {
			return 0;
		}
	}

	@Override
	public int compareTo(Object p_aThat) {
		// http://www.javapractices.com/topic/TopicAction.do?Id=10

		// TODO Auto-generated method stub
		// return 0;
		final int BEFORE = -1;
		final int EQUAL = 0;
		final int AFTER = 1;

		// this optimization is usually worthwhile, and can
		// always be added
		if (this == p_aThat)
			return EQUAL;

		final Cash that = (Cash) p_aThat;

		int comparison = BEFORE;

		int thisInt = new Integer(this.getDenomination().substring(1, this.getDenomination().length())).intValue();

		int thatInt = new Integer(that.getDenomination().substring(1, that.getDenomination().length())).intValue();

		// primitive numbers follow this form
		if (thisInt < thatInt)
			return BEFORE;
		if (thisInt > thatInt)
			return AFTER;

		return EQUAL;

	}
}
