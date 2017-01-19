package com.medical;

import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class MedicalStock extends JFrame implements WindowListener {

	public MedicalStock() {

		int x_axes = 10, y_axes = 10, height = 150, width = 50;

		setSize(600, 350);
		setLocationRelativeTo(this);
		setTitle("Medical");
		setLayout(new BorderLayout());
		setResizable(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		addWindowListener(this);

		// --------------------------------------------------------------

		JTabbedPane medicalStockPane = new JTabbedPane();
		JPanel stockPanel = new JPanel();
		medicalStockPane.add("Stock Management", stockPanel);

		JPanel billPanel = new JPanel();
		medicalStockPane.add("Generate Bill", billPanel);
		add(medicalStockPane);
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