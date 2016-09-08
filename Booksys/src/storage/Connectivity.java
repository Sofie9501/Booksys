/*
 * Restaurant Booking System: example code to accompany
 *
 * "Practical Object-oriented Design with UML"
 * Mark Priestley
 * McGraw-Hill (2004)
 */

package storage ;

import java.io.* ;
import java.sql.* ;
import java.util.* ;

class Connectivity{

	static Connection getConnection(String propFile)
	{
		Properties props = new Properties() ;
		InputStream pfile = null ;
		Connection con = null ;

		// Load in the property file
		try {
			pfile = new FileInputStream(propFile) ;
			props.load(pfile) ;
			pfile.close() ;
		}
		catch (IOException e) {
			e.printStackTrace() ;
			return con ;
		}
		finally {
			if (pfile != null) {
				try {
					pfile.close() ;
				}
				catch (IOException ignored) {}
			}
		}

		// Load the Driver class

		String driver = props.getProperty("jdbc.driver") ;
		try {
			Class.forName(driver) ;
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace() ;
			return con ;
		}

		// Try to make a connection

		String dbURL = props.getProperty("jdbc.url") ;
		try{
			con = DriverManager.getConnection(dbURL, "root", "") ;
		}
		catch (SQLException e) {
			e.printStackTrace() ;
		}
		return con ;
	}

}
