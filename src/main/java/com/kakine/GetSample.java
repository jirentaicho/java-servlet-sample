package com.kakine;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class GetSample
 */
public class GetSample extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetSample() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		List<String> result = new ArrayList<>();
		
		try {
			Context context = new InitialContext();
			DataSource dataSource = (DataSource) context.lookup("java:comp/env/jdbc/kakine");
			try(
				Connection con = dataSource.getConnection();
				Statement statement = con.createStatement();
				ResultSet resultSet = statement.executeQuery("select * from items");
				){
				while (resultSet.next()) {
					result.add(resultSet.getString("name"));//カラム名
				}
			}
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		request.setAttribute("items", result);
		getServletContext().getRequestDispatcher("/kakine/get.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// データベース登録時の文字化けを防ぐため文字コードを指定する
		request.setCharacterEncoding("UTF-8");
		// postしたパラメータを取得する
		String item = request.getParameter("item");
		//　データベースに登録する
		try {
			Context context = new InitialContext();
			DataSource dataSource = (DataSource) context.lookup("java:comp/env/jdbc/kakine");
			try(
				Connection con = dataSource.getConnection();
				// プリペアドステートメントを利用します。
				PreparedStatement statement = con.prepareStatement("INSERT INTO items(name) VALUES (?);");
				){
				statement.setString(1, item);
				statement.execute();
			}
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		doGet(request, response);
	}
}
