/*
 * Restaurant Booking System: example code to accompany
 *
 * "Practical Object-oriented Design with UML"
 * Mark Priestley
 * McGraw-Hill (2004)
 */

package persistency ;

import domain.* ;

interface PersistentBooking extends Booking
{
  int getId() ;
}
