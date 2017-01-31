/**
 *
 * @author Deepak saraf
 */
package com.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.swing.JOptionPane;

import com.admin.AdminLogin;
import com.pathConstant.PathConstant;

public class Utility {

	public static Properties readProperties() {
		Properties p = new Properties();
		try (FileInputStream fi = new FileInputStream(
				PathConstant.propertiesPath)) {
			p.load(fi);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return p;
	}

	public static void warningPopup(String message) {
		int i = JOptionPane.showConfirmDialog(null, message, "",
				JOptionPane.DEFAULT_OPTION);
		switch (i) {
		case JOptionPane.OK_OPTION:
			break;
		}
	}

	public static boolean setLoginToken(boolean token, Connection con) {
		try (PreparedStatement ps = con
				.prepareStatement("UPDATE sign_up SET token = ? WHERE email = ?")) {
			ps.setBoolean(1, token);
			ps.setString(2, AdminLogin.loggedInEmail);
			if (ps.executeUpdate() != 0) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean getLoginToken(Connection con) {
		try (PreparedStatement ps = con
				.prepareStatement("SELECT token FROM sign_up WHERE email = ?")) {
			ps.setString(1, AdminLogin.loggedInEmail);
			ResultSet rs;
			rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getBoolean(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
