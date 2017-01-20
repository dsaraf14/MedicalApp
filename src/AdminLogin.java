import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.hospital.Hospital;
import com.medical.Medical;
import com.util.Utility;

import db.DBUtil;

public class AdminLogin extends JFrame implements ActionListener {

	JLabel email, password;
	JTextField emailField, passField;
	JButton login, reset;
	Connection con = DBUtil.getCon();
	Properties p = Utility.readProperties();

	public AdminLogin() {
		int x_axes = 50, y_axes = 70, height = 160, width = 25;

		setSize(600, 300);
		setLocationRelativeTo(this);
		setTitle("Login");
		setLayout(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// ------------------------------------------

		email = new JLabel("Email:*");
		email.setBounds(x_axes + 100, y_axes, height, width);
		add(email);

		emailField = new JTextField(10);
		emailField.setBounds(x_axes + 210, y_axes, height, width);
		add(emailField);

		password = new JLabel("Password:*");
		password.setBounds(x_axes + 100, y_axes + 50, height, width);
		add(password);

		passField = new JTextField(10);
		passField.setBounds(x_axes + 210, y_axes + 50, height, width);
		add(passField);

		login = new JButton("Login");
		login.setBounds(x_axes + 210, y_axes + 100, height - 80, width);
		add(login);
		login.addActionListener(this);

		reset = new JButton("Reset");
		reset.setBounds(x_axes + 290, y_axes + 100, height - 80, width);
		add(reset);
		reset.addActionListener(this);
	}

	public static void main(String[] args) {
		AdminLogin al = new AdminLogin();
		al.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getActionCommand().equals(login.getText())) {
			if (emailField.getText() == null || emailField.getText().equals("")
					|| passField.getText() == null
					|| passField.getText().equals("")) {
				Utility.warningPopup("Please fill all the Mendatory fields....");
			} else {
				if (validateUser()) {
					String role = getRole();
					if (role.equalsIgnoreCase(p.getProperty("AdminRole"))) {
						this.dispose();
						new Admin().setVisible(true);
					} else if (role.equalsIgnoreCase(p
							.getProperty("MedicalRole"))) {
						this.dispose();
						new Medical().setVisible(true);
					} else if (role.equalsIgnoreCase(p
							.getProperty("HospitalRole"))) {
						this.dispose();
						new Hospital().setVisible(true);
					}
				} else {
					Utility.warningPopup("You are not a Valid user...");
				}
			}
		} else if (arg0.getActionCommand().equals(reset.getText())) {
			emailField.setText("");
			passField.setText("");
		}

	}

	private boolean validateUser() {
		boolean status = false;
		try (PreparedStatement ps = con
				.prepareStatement("SELECT * from sign_up where email = ? and password = ? ")) {
			ps.setString(1, emailField.getText());
			ps.setString(2, passField.getText());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				status = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return status;
	}

	private String getRole() {
		try (PreparedStatement ps = con
				.prepareStatement("SELECT role from sign_up where email = ?")) {
			ps.setString(1, emailField.getText());
			ResultSet rs;
			rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
