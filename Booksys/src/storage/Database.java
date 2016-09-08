package storage ;

import java.sql.* ;
import java.util.Date ;
import java.util.Vector ;

public class Database
{
	private static Connection con;
	private static Database database;

	public static Database getInstance()
	{
		if (database == null) {
			database = new Database();
		}
		return database;
	}

	private Database()
	{
		// Get a connection
		con = Connectivity.getConnection("properties");
	}

	public static Connection getConnection() {
		return con;
	}

	// Get a fresh object ID
	public int getId()
	{
		int id = 0;
		try {
			Statement stmt = con.createStatement();

			ResultSet rset = stmt.executeQuery("SELECT * FROM Oid");
			while (rset.next()) {
				id = rset.getInt(1);
			}
			rset.close();

			id++;

			int updateCount = stmt.executeUpdate("UPDATE Oid SET last_id = '" + id + "'");
			stmt.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return id ;
	}
}
