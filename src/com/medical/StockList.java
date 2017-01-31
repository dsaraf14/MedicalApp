/**
 *
 * @author Deepak saraf
 */
package com.medical;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class StockList extends JFrame {

	public StockList() {
		setSize(900, 400);
		setLocationRelativeTo(this);
		setTitle("Stock Detail");
		setLayout(new BorderLayout());
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

	}

//	public static void main(String[] args) {
//		SwingUtilities.invokeLater(new Runnable() {
//
//			@Override
//			public void run() {
//				new StockList().setVisible(true);
//
//			}
//		});
	}

}
