package domain;

import java.sql.Date;
import java.sql.Time;

public class WaitingList {

	int covers;
	Date date;
	Time t;
	Table tab;
	Customer cust;
	Time arr;

	public WaitingList(int c, Date d, Time t, Table tab, Customer cust, Time arr)
	{
		this.covers = c;
		this.date = d;
		this.t = t;
		this.tab = tab;
		this.cust = cust;
		this.arr = arr;
	}
	
	

}
