/**
 * 
 */
package com.mvc.model;

/**
 * @author gjsimpso
 *
 */
public class Horse implements Comparable {

	private int number;
	private String name;
	private int odds;
	private String result;

	/**
	 * 
	 */
	public Horse(int p_number, String p_name, int p_odds) {
		// TODO Auto-generated constructor stub
		this.number = p_number;
		this.name = p_name;
		this.odds = p_odds;
		if (p_number == 1)
			this.result = "won";
		else
			this.result = "lost";
	}

	public String toString() {
		return (number + "," + name + "," + odds + "," + result);
	}

	public int getNumber() {
		return number;
	}

	protected void setNumber(int p_number) {
		this.number = p_number;
	}

	public String getName() {
		return name;
	}

	protected void setName(String p_name) {
		this.name = p_name;
	}

	public int getOdds() {
		return odds;
	}

	protected void setOdds(int p_odds) {
		this.odds = p_odds;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String p_result) {
		this.result = p_result;
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

		final Horse that = (Horse) p_aThat;

		// primitive numbers follow this form
		if (this.getNumber() < that.getNumber())
			return BEFORE;
		if (this.getNumber() > that.getNumber())
			return AFTER;

		return EQUAL;
	}
}
