package com.admin;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class AdminLogin extends JFrame {

	private JLabel j = new JLabel("Unexpected Behaviour of a \"Team Member\" can causes unexpected behavior of APPLICATION...");

	public AdminLogin() {
		// int x_axes = 45, y_axes = 70, height = 160, width = 25;

		setSize(710, 300);
		setLocationRelativeTo(this);
		setTitle("Login");
		setLayout(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		j.setBounds(100, 100, 1000, 100);
		add(j);

		// ------------------------------------------
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new AdminLogin().setVisible(true);
			}
		});
	}
}
