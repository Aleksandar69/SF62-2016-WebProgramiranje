package dao;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSourceFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class ConnectionManager {

	public static final String DATABASE_NAZIV = "Bioskop.db";
	public static final String FILE_SEPARATOR = System.getProperty("file.separator");

	/*
	 * public static final String WINDOWS_PATH = "C:" + FILE_SEPARATOR + "gitWeb" +
	 * FILE_SEPARATOR + "SF-62-2016-WebProgramiranje" + FILE_SEPARATOR +
	 * "SF-62-2016-Bioskop" + FILE_SEPARATOR + "sql-script" + FILE_SEPARATOR +
	 * DATABASE_NAZIV;
	 */
	
	public static final String WINDOWS_PATH = "C:" + FILE_SEPARATOR + DATABASE_NAZIV;
	private static final String LINUX_PATH = "SQLite" + FILE_SEPARATOR + DATABASE_NAZIV;

	private static final String PATH = WINDOWS_PATH;

	private static DataSource dataSource;

	public static void open() {
		try {
			System.out.println("Konekcija s bazom otvorena");
			System.out.println(PATH);
			Properties dataSourceProps = new Properties();
			dataSourceProps.setProperty("driverClassName", "org.sqlite.JDBC");
			dataSourceProps.setProperty("url", "jdbc:sqlite:" + PATH);
			
			dataSource = BasicDataSourceFactory.createDataSource(dataSourceProps);

		} catch (Exception e) {
			System.out.println("Greska pri otvaranju konekcije s bazom");
			e.printStackTrace();
		}
	}

	public static Connection getConnection() {
		try {
			System.out.println("Pokusaj konekcije");
			return dataSource.getConnection();
		} catch (Exception e) {
			System.out.println("Konekcija neuspjesna");
			e.printStackTrace();
		}
		return null;
	}
	
	public static void close(Connection myConn, Statement myStmnt, ResultSet myRS) {
		try {
			if (myRS != null) {
				myRS.close();
			}
			if (myStmnt != null) {
				myStmnt.close();
			}
			if (myConn != null) {
				myConn.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

