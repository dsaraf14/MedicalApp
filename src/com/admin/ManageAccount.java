package com.admin;

import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import db.DBUtil;

public class ManageAccount extends JFrame {

	JTable table;

	public ManageAccount() {

		int x_axes = 50, y_axes = 50, height = 150, width = 30;

		setSize(700, 400);
		setLocationRelativeTo(this);
		setTitle("Manage Account");
		setLayout(new BorderLayout());
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		initValues();
	}

	private void initValues() {
		Connection con = DBUtil.getCon();
		try (PreparedStatement ps = con
				.prepareStatement("SELECT name, email, mobile_no, role, active FROM sign_up");) {
			ResultSet rs = ps.executeQuery();
			String[] tableColumn = new String[] { "Name", "Email", "Mobile No",
					"Role", "Active" };
			int rowCount = 0;
			while (rs.next()) {
				rowCount++;
			}
			String[][] tableValues = new String[rowCount][tableColumn.length];
			rs.first();
			for (int i = 0; i < rowCount; i++) {
				for (int j = 0; j < tableColumn.length; j++) {
					tableValues[i][j] = rs.getString(j + 1);
				}
				rs.next();
			}

			table = new JTable(tableValues, tableColumn);
			this.add(table);
			JScrollPane scrollPane = new JScrollPane(table);
			this.add(scrollPane);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
//
//	public static void main(String[] args) {
//		new ManageAccount().setVisible(true);
//	}
}