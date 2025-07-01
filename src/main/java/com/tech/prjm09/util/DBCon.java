package com.tech.prjm09.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBCon {
	static Connection con=null;
	public static Connection getConnection() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			String url="jdbc:oracle:thin:@localhost:1521:xe";
			String user="blue";
			String pw="123456";
			con=DriverManager.getConnection(url,user,pw);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return con;
	}

}
