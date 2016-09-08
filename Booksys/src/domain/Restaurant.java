/*
 * Restaurant Booking System: example code to accompany
 *
 * "Practical Object-oriented Design with UML"
 * Mark Priestley
 * McGraw-Hill (2004)
 */

package domain ;

import persistency.* ;

import java.sql.Date ;
import java.sql.Time ;
import java.util.Vector ;

class Restaurant
{
  BookingMapper  bm = BookingMapper.getInstance() ;
  CustomerMapper cm = CustomerMapper.getInstance() ;
  TableMapper    tm = TableMapper.getInstance() ;
  
  Vector getBookings(Date date)
  {
    return bm.getBookings(date) ;
  }

  Customer getCustomer(String name, String phone)
  {
    return cm.getCustomer(name, phone) ;
  }
  
  Table getTable(int n)
  {
    return tm.getTable(n) ;
  }

  static Vector getTableNumbers()
  {
    return TableMapper.getInstance().getTableNumbers() ;
  }

  public Booking makeReservation(int covers, Date date, Time time,
				     String name, String phone)
  {
    Customer c = getCustomer(name, phone) ;
    Booking b = bm.createReservation(covers, date, time, c, null) ;
    
	  if(b != null){
		  return b;
	  }
	  
    return null;
    
  }

  public Booking makeWalkIn(int covers, Date date,Time time)
  {
	  Booking b = bm.createWalkIn(covers, date, time) ;
	  
	  if(b != null){
		  return b;
	  }
	  
    return null;
  }

  public void updateBooking(Booking b)
  {
    bm.updateBooking(b) ;
  }
  
  public void removeBooking(Booking b) {
    bm.deleteBooking(b) ;
  }
}
