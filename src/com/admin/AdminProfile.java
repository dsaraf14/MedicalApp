/**
 *
 * @author Deepak saraf
 */
package com.admin;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.util.Utility;

import db.DBUtil;

public class AdminProfile extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1652431577218424776L;
	private JLabel name, email, phoneNo, password;
	private JTextField nameField, emailField, phField, passField;
	private JButton logout, update;

	public AdminProfile() {

		setSize(450, 350);
		setLocationRelativeTo(this);
		setTitle("Admin Profile");
		setLayout(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JPanel panel = new JPanel();
		panel.setSize(300, 200);
		panel.setLocation(80, 50);
		panel.setLayout(new GridLayout(5, 2, 0, 20));

		name = new JLabel("Name:");
		panel.add(name);
		nameField = new JTextField(20);
		panel.add(nameField);

		email = new JLabel("Email:*");
		panel.add(email);
		emailField = new JTextField(20);
		// emailField.setEnabled(false);
		emailField.setEditable(false);
		panel.add(emailField);

		phoneNo = new JLabel("Mobile No:");
		panel.add(phoneNo);
		phField = new JTextField(20);
		panel.add(phField);

		password = new JLabel("Password:*");
		panel.add(password);
		passField = new JTextField(20);
		panel.add(passField);

		update = new JButton("Update");
		update.addActionListener(this);
		panel.add(update);

		logout = new JButton("Logout");
		logout.addActionListener(this);
		panel.add(logout);

		add(panel);
		initValues();
	}

	// public static void main(String[] args) {
	// AdminProfile ap = new AdminProfile();
	// ap.setVisible(true);
	// }

	private void initValues() {
		Connection con = DBUtil.getCon();
		PreparedStatement ps;
		try {
			ps = con.prepareStatement("SELECT name , mobile_no, password FROM sign_up WHERE email = ?");
			ps.setString(1, AdminLogin.loggedInEmail);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				nameField.setText(rs.getString(1));
				emailField.setText(AdminLogin.loggedInEmail);
				phField.setText(rs.getString(2));
				passField.setText(rs.getString(3));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getActionCommand().equalsIgnoreCase(logout.getText())) {
			inValidate();
		} else if (arg0.getActionCommand().equalsIgnoreCase(update.getText())) {
			if (passField.getText().equalsIgnoreCase("")
					|| passField.getText() == null) {
				Utility.warningPopup("Password field must not be empty");
			} else {
				Connection con = DBUtil.getCon();
				try (PreparedStatement ps = con
						.prepareStatement("UPDATE sign_up SET name = ?, mobile_no = ?, password = ? WHERE email = ?")) {
					ps.setString(1, nameField.getText());
					ps.setString(2, phField.getText());
					ps.setString(3, passField.getText());
					ps.setString(4, AdminLogin.loggedInEmail);
					if (ps.executeUpdate() != 0) {
						Utility.warningPopup("Profile has been Successfully Updated");
						this.dispose();
					} else {
						Utility.warningPopup("Some error occurred while updating profile");
						this.dispose();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void inValidate() {
		if (Utility.setLoginToken(false, DBUtil.getCon())) {
			this.dispose();
			Utility.warningPopup("You are successfully Logged Out");
			System.exit(0);
		} else {
			Utility.warningPopup("There is something wrong...! Try again");
		}
	}
}
