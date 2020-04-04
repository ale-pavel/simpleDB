package studentClient.simpledb;

import java.sql.*;
import simpledb.remote.SimpleDriver;

public class FirstTable {
	public static void main(String[] args) {

		Connection conn = null;

		try {
			Driver d = new SimpleDriver();
			conn = d.connect("jdbc:simpledb://localhost", null);
			Statement stmt = conn.createStatement();
			// transaction 1
			String s = "create table FIRSTTABLE(id int, name varchar(25))";
			stmt.executeUpdate(s);
			System.out.println("Table FIRSTTABLE created.");
			// transaction 2
			s = "insert into FIRSTTABLE(id, name) values ";
			String[] firstvals = { "(10, 'ten')", "(20, 'twenty')", "(30, 'thirty')" };
			for (int i = 0; i < firstvals.length; i++)
				stmt.executeUpdate(s + firstvals[i]);
			System.out.println("FIRSTTABLE records inserted.");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}
}
