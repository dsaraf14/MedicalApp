package com.admin;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.util.Utility;

import db.DBUtil;

public class CreateAccount extends JFrame implements ActionListener {

	JLabel email, password, role;
	JTextField emailField;
	JPasswordField passField;
	JComboBox<String> roleDropdown;
	JButton createAccount, reset;

	public CreateAccount() {
		int x_axes = 50, y_axes = 50, height = 150, width = 30;

		setSize(500, 300);
		setLocationRelativeTo(this);
		setTitle("Create Account");
		setLayout(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JPanel panel = new JPanel();
		panel.setSize(330, 160);
		panel.setLocation(90, 60);
		panel.setLayout(new GridLayout(4, 2, 0, 20));

		email = new JLabel("Email:*");
		panel.add(email);

		emailField = new JTextField(30);
		panel.add(emailField);

		password = new JLabel("Password:*");
		panel.add(password);

		passField = new JPasswordField(30);
		panel.add(passField);

		role = new JLabel("Role:*");
		panel.add(role);

		String[] roles = new String[] { "Medical", "Hospital" };

		roleDropdown = new JComboBox<String>(roles);
		panel.add(roleDropdown);

		createAccount = new JButton("Create Account");
		panel.add(createAccount);
		createAccount.addActionListener(this);

		reset = new JButton("Reset");
		panel.add(reset);
		reset.addActionListener(this);
		add(panel);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getActionCommand().equalsIgnoreCase(reset.getText())) {
			emailField.setText(null);
			passField.setText(null);
		}
		if (Utility.getLoginToken(DBUtil.getCon())) {
			if (arg0.getActionCommand().equalsIgnoreCase(
					createAccount.getText())) {
				if (emailField.getText().equalsIgnoreCase("")
						|| emailField.getText() == null
						|| passField.getText().equalsIgnoreCase("")
						|| passField.getText() == null) {
					Utility.warningPopup("Please fill out the Mandatory fields");
				} else {
					Connection con = DBUtil.getCon();
					try (PreparedStatement ps = con
							.prepareStatement("INSERT INTO sign_up(email, password, role) VALUES(?, ?, ?)");) {
						ps.setString(1, emailField.getText().toLowerCase());
						ps.setString(2, passField.getText());
						ps.setString(3, (String) roleDropdown.getSelectedItem());
						if (ps.executeUpdate() != 0) {
							Utility.warningPopup("Account is Created Successfully");
							emailField.setText(null);
							passField.setText(null);
						} else {
							Utility.warningPopup("Some error occurred in Creating Account");
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	// public static void main(String[] args) {
	// new CreateAccount().setVisible(true);
	// }
}
