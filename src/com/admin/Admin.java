package com.admin;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import com.hospital.Hospital;
import com.medical.Medical;
import com.util.Utility;

import db.DBUtil;

public class Admin extends JFrame implements ActionListener {

	private JButton medical, hospital;
	JMenu menu;
	JMenuItem profile, create_account, manageAccount;

	public Admin() {
		int x_axes = 50, y_axes = 50, height = 150, width = 30;

		setSize(600, 350);
		setLocationRelativeTo(this);
		setTitle("Admin Panel");
		setLayout(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		medical = new JButton("Medical");
		medical.setBounds(x_axes + 50, y_axes + 50, height, width + 50);
		medical.addActionListener(this);
		add(medical);

		hospital = new JButton("Hospital");
		hospital.setBounds(x_axes + 280, y_axes + 50, height, width + 50);
		hospital.addActionListener(this);
		add(hospital);

		JMenuBar jBar = new JMenuBar();
		jBar.setBounds(x_axes + 90, y_axes + 50, height, width + 50);

		menu = new JMenu("Menu");

		profile = new JMenuItem("Profile");
		profile.addActionListener(this);
		menu.add(profile);

		create_account = new JMenuItem("Create Account");
		create_account.addActionListener(this);
		menu.add(create_account);

		manageAccount = new JMenuItem("Manage Account");
		manageAccount.addActionListener(this);
		menu.add(manageAccount);

		jBar.add(menu);
		// jBar.setBorder(new BevelBorder(BevelBorder.RAISED));
		setJMenuBar(jBar);

	}

//	public static void main(String[] args) {
//
//		Admin a = new Admin();
//		a.setVisible(true);
//	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (Utility.getLoginToken(DBUtil.getCon())) {
			if (e.getActionCommand().equalsIgnoreCase(hospital.getText())) {
				this.dispose();
				new Hospital().setVisible(true);
			} else if (e.getActionCommand().equalsIgnoreCase(medical.getText())) {
				this.dispose();
				new Medical().setVisible(true);
			} else if (e.getActionCommand().equalsIgnoreCase(profile.getText())) {
				new AdminProfile().setVisible(true);
			} else if (e.getActionCommand().equalsIgnoreCase(
					create_account.getText())) {
				new CreateAccount().setVisible(true);
			} else if (e.getActionCommand().equalsIgnoreCase(
					manageAccount.getText())) {
				new ManageAccount().setVisible(true);
			}
		} else {
			Utility.warningPopup("Please Login first to do this operation");
		}
	}
}
