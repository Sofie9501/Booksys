/*
 * Restaurant Booking System: example code to accompany
 *
 * "Practical Object-oriented Design with UML"
 * Mark Priestley
 * McGraw-Hill (2004)
 */

package persistency ;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.Vector ;

import domain.Booking ;
import domain.Customer ;
import domain.Reservation ;
import domain.Table ;
import storage.Database;

public class BookingMapper
{
  // Singleton:
  
  private static BookingMapper uniqueInstance ;

  public static BookingMapper getInstance()
  {
    if (uniqueInstance == null) {
      uniqueInstance = new BookingMapper() ;
    }
    return uniqueInstance ;
  }
  
  public Table getFreeTables(Date date, Time time, int covers){
	  try {
		Statement stmt = Database.getInstance().getConnection().createStatement() ;
		ResultSet result = stmt.executeQuery("select * from `table` where oid not in (select reservation.table_id from"
				+ " reservation natural join `table` where date = " + date + " and time = '" + time + "' ) and places >= " + covers + ";") ;
		while(result.next()){
			System.out.println(result.getInt(2));
			return new Table(result.getInt(2), result.getShort(3));
		}
		
	} catch (SQLException e) {
		e.printStackTrace();
	}
	  
	  return null;
	  
  }

  public Vector getBookings(Date date)
  {
    Vector v = new Vector() ;
    try
    {
      Statement stmt
	= Database.getInstance().getConnection().createStatement() ;
      ResultSet rset
	= stmt.executeQuery("SELECT * FROM Reservation WHERE date='"
			    + date + "'") ;
      while (rset.next()) {
	int oid = rset.getInt(1) ;
	int covers = rset.getInt(2) ;
	Date bdate = rset.getDate(3) ;
	Time btime = rset.getTime(4) ;
	int table = rset.getInt(5) ;
	int cust = rset.getInt(6) ;
	Time atime = rset.getTime(7) ;
	PersistentTable t = TableMapper.getInstance().getTableForOid(table) ;
	PersistentCustomer c =
	  CustomerMapper.getInstance().getCustomerForOid(cust) ;
	PersistentReservation r
	  = new PersistentReservation(oid, covers, bdate, btime, t, c, atime) ;
	v.add(r) ;
      }
      rset.close() ;
      rset = stmt.executeQuery("SELECT * FROM WalkIn WHERE date='"
			       + date + "'") ;
      while (rset.next()) {
	int oid = rset.getInt(1) ;
	int covers = rset.getInt(2) ;
	Date bdate = rset.getDate(3) ;
	Time btime = rset.getTime(4) ;
	int table = rset.getInt(5) ;
	PersistentTable t = TableMapper.getInstance().getTableForOid(table) ;
	PersistentWalkIn w
	  = new PersistentWalkIn(oid, covers, bdate, btime, t) ;
	v.add(w) ;
      }
      rset.close() ;
     stmt.close() ;
    }
    catch (SQLException e) {
      e.printStackTrace() ;
    }
    return v ;
  }
  
  public PersistentReservation createReservation(int covers,
						 Date date,
						 Time time,
						 Customer customer,
						 Time arrivalTime)
  {
	  Table table = getFreeTables(date, time, covers);
	  
	  if(table == null){
		  return null;
	  }

    int oid = Database.getInstance().getId() ;
    performUpdate("INSERT INTO Reservation " + "VALUES ('"
		  + oid + "', '"
		  + covers + "', '"
		  + date + "', '"
		  + time + "', '"
		  + table.getNumber() + "', '"
		  + ((PersistentCustomer) customer).getId() + "', "
		  + (arrivalTime == null ? "NULL" :
		     ("'" + arrivalTime.toString() + "'"))
		  + ")" ) ;
    return new PersistentReservation(oid,
				     covers,
				     date,
				     time,
				     table,
				     customer,
				     arrivalTime) ;
  } 
  
  public PersistentWalkIn createWalkIn(int covers,
				       Date date,
				       Time time,
				       Table table)
  {
    int oid = Database.getInstance().getId() ;
    performUpdate("INSERT INTO WalkIn " + "VALUES ('"
		  + oid + "', '"
		  + covers + "', '"
		  + date + "', '"
		  + time + "', '"
		  + ((PersistentTable) table).getId() + "')" ) ;
    return new PersistentWalkIn(oid, covers, date, time, table) ;
  } 

  public void updateBooking(Booking b)
  {
    PersistentBooking pb = (PersistentBooking) b ;
    boolean isReservation = b instanceof Reservation ;
    StringBuffer sql = new StringBuffer(128) ;
    
    sql.append("UPDATE ") ;
    sql.append(isReservation ? "Reservation" : "WalkIn") ;
    sql.append(" SET ") ;
    sql.append(" covers = ") ;
    sql.append(pb.getCovers()) ;
    sql.append(", date = '") ;
    sql.append(pb.getDate().toString()) ;
    sql.append("', time = '") ;
    sql.append(pb.getTime().toString()) ;
    sql.append("', table_id = ") ;
    sql.append(((PersistentTable) pb.getTable()).getId()) ;
    if (isReservation) {
      PersistentReservation pr = (PersistentReservation) pb ;
      sql.append(", customer_id = ") ;
      sql.append(((PersistentCustomer) pr.getCustomer()).getId()) ;
      sql.append(", arrivalTime = ") ;
      Time atime = pr.getArrivalTime() ;
      if (atime == null) {
	sql.append("NULL") ;
      }
      else {
	sql.append("'" + atime + "'") ;
      }
    }
    sql.append(" WHERE oid = ") ;
    sql.append(pb.getId()) ;

    performUpdate(sql.toString()) ;
  }
  
  public void deleteBooking(Booking b)
  {
    String table = b instanceof Reservation ? "Reservation" : "WalkIn" ;
    performUpdate("DELETE FROM " + table + " WHERE oid = '"
		  + ((PersistentBooking) b).getId() + "'" ) ;
  }

  private void performUpdate(String sql)
  {
    try {
      Statement stmt
	= Database.getInstance().getConnection().createStatement() ;
      int updateCount
	= stmt.executeUpdate(sql) ;
      stmt.close() ;
    }
    catch (SQLException e) {
      e.printStackTrace() ;
    }
  }

}
