/**
 *
 * @author Deepak saraf
 */
package com.admin;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.PasswordAuthentication;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.util.Utility;

import db.DBUtil;

public class CreateAccount extends JFrame implements ActionListener {
	private static final long serialVersionUID = 4780468050316415150L;
	private JLabel email, password, role;
	private JTextField emailField;
	private JPasswordField passField;
	private JComboBox<String> roleDropdown;
	private JButton createAccount, reset;

	public CreateAccount() {

		setSize(510, 300);
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
		} else if (true) {
			if (arg0.getActionCommand().equalsIgnoreCase(createAccount.getText())) {
				if (emailField.getText().equalsIgnoreCase("") || emailField.getText() == null || passField.getText().equalsIgnoreCase("")
						|| passField.getText() == null) {
					Utility.warningPopup("Please Enter the Mandatory Details");
				} else {
					Connection con = DBUtil.getCon();
					try (PreparedStatement ps = con.prepareStatement("INSERT INTO sign_up(email, password, role) VALUES(?, ?, ?)");) {
						ps.setString(1, emailField.getText().toLowerCase());
						ps.setString(2, passField.getText());
						ps.setString(3, (String) roleDropdown.getSelectedItem());
						if (ps.executeUpdate() != 0) {
							Utility.warningPopup("Account is Created Successfully and Mail is sent to corresponding user");
							emailField.setText(null);
							passField.setText(null);

							sendMail("myhospital0@gmail.com", "P@ssword1!.", "myhospital0@gmail.com", "HMS", "How r u?");

						} else {
							Utility.warningPopup("Some error occurred in Creating Account");
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			Utility.warningPopup("Please Login first to Do this operation");
		}
	}

	private static void sendMail(String from, String password, String to, String sub, String msg) {
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
		// get Session
		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected javax.mail.PasswordAuthentication getPasswordAuthentication() {

				return new javax.mail.PasswordAuthentication(from, password);
			}
		});
		// compose message
		try {
			MimeMessage message = new MimeMessage(session);
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setSubject(sub);
			message.setText(msg);
			// send message
			Transport.send(message);
			System.out.println("message sent successfully");
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}

	}

	public static void main(String[] args) {
		sendMail("myhospital0@gmail.com", "P@ssword1!.", "myhospital0@gmail.com", "HMS", "How r u?");
//		SwingUtilities.invokeLater(new Runnable() {
//
//			@Override
//			public void run() {
//				new CreateAccount().setVisible(true);
//			}
//		});
	}
}
