/*
 * Restaurant Booking System: example code to accompany
 *
 * "Practical Object-oriented Design with UML"
 * Mark Priestley
 * McGraw-Hill (2004)
 */

package presentation ;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Time ;
import java.util.Enumeration;

import domain.Booking;
import domain.BookingSystem;

abstract class BookingDialog extends Dialog
{
  protected Choice    tableNumber ;
  protected TextField covers ;
  protected TextField time ;
  protected Label     tableNumberLabel ;
  protected Label     coversLabel ;
  protected Label     timeLabel ;
  protected boolean   confirmed ;
  protected Button    ok ;
  protected Button    cancel ;
  
  BookingDialog(Frame owner, String title)
  {
    this(owner, title, null) ;
  }

  // This constructor initializes fields with data from an existing booking.
  // This is useful for completing Exercise 7.6.
  
  BookingDialog(Frame owner, String title, Booking booking)
  {
    super(owner, title, true) ;
    
    addWindowListener(new WindowAdapter() {
	public void windowClosing(WindowEvent e) {
	  confirmed = false ;
	  BookingDialog.this.hide() ;
	}
      }) ;
    
    tableNumberLabel = new Label("Table number:", Label.RIGHT) ;
    tableNumber = new Choice() ;
    Enumeration enumeration = BookingSystem.getTableNumbers().elements() ;
    while (enumeration.hasMoreElements()) {
      tableNumber.add(((Integer) enumeration.nextElement()).toString()) ;
    }
    if (booking != null) {
      tableNumber.select(Integer.toString(booking.getTable().getNumber())) ;
    }

    coversLabel = new Label("Covers:", Label.RIGHT) ;
    covers = new TextField(4) ;
    if (booking != null) {
      covers.setText(Integer.toString(booking.getCovers())) ;
    }
    
    timeLabel = new Label("Time:", Label.RIGHT) ;
    time = new TextField("HH:MM:SS", 8) ;
    if (booking != null) {
      time.setText(booking.getTime().toString()) ;
    }
    
    ok = new Button("Ok") ;
    ok.addActionListener(new ActionListener() {
	public void actionPerformed(ActionEvent e) {
	  confirmed = true ;
	  BookingDialog.this.hide() ;
	}
      }) ;
    
    cancel = new Button("Cancel") ;
    cancel.addActionListener(new ActionListener() {
	public void actionPerformed(ActionEvent e) {
	  confirmed = false ;
	  BookingDialog.this.hide() ;
	}
      }) ;
  }
  
  int getTableNumber()
  {
    return Integer.parseInt(tableNumber.getSelectedItem()) ;
  }

  int getCovers()
  {
    return Integer.parseInt(covers.getText()) ;
  }

  Time getTime()
  {
    return Time.valueOf(time.getText()) ;
  }

  boolean isConfirmed()
  {
    return confirmed ;
  }
}
