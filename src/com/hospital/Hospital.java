package com.hospital;

import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class Hospital extends JFrame implements WindowListener {
	public Hospital() {
		int x_axes = 50, y_axes = 50, height = 150, width = 30;

		setSize(600, 350);
		setLocationRelativeTo(this);
		setTitle("Hospital");
		setLayout(new BorderLayout());
		setResizable(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		addWindowListener(this);

		// --------------------------------------------------

		JTabbedPane hospitalPane = new JTabbedPane();
		JPanel inDoor = new JPanel();
		hospitalPane.add("In Door", inDoor);

		JPanel outDoor = new JPanel();
		hospitalPane.add("Out Door", outDoor);
		add(hospitalPane);
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		int i = JOptionPane.showConfirmDialog(null,
				"Do You Really Want To Close ?", "Confirm Exit",
				JOptionPane.OK_CANCEL_OPTION);
		switch (i) {
		case JOptionPane.OK_OPTION:
			System.exit(0);
			break;
		case JOptionPane.CANCEL_OPTION:
			break;
		}
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}
}
