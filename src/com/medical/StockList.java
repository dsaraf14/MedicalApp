/**
 *
 * @author Deepak saraf
 */
package com.medical;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import db.DBUtil;

public class StockList extends JFrame {

	private static final long serialVersionUID = -6312810958723284545L;
	private JPanel tablePanel = new JPanel();
	private JTable table;
	private long serialNo = 1;

	public StockList() {
		setSize(900, 400);
		setLocationRelativeTo(this);
		setTitle("Stock Detail");
		setLayout(new BorderLayout());
		setResizable(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		initValues();
	}

	private void initValues() {
		Connection con = DBUtil.getCon();
		try (PreparedStatement ps = con
				.prepareStatement("SELECT nameOfMedicine, dateOfInsert, dateOfMFG, dateOfEXP, totalPackets, tabletInAPacket FROM medical_stock");) {
			ResultSet rs = ps.executeQuery();
			Object[] tableColumn = new String[] { "S.N.", "Name of Medicine",
					"Added Date", "MFG Date", "EXP Date", "Total Packets",
					"Tablet in a Packet", "Total Medicines" };
			int rowCount = 0;
			while (rs.next()) {
				rowCount++;
			}
			Object[][] tableValues = new Object[rowCount][tableColumn.length];
			rs.first();
			for (int i = 0; i < rowCount; i++) {
				for (int j = 0; j < tableColumn.length; j++) {

					if (j == 0) {
						tableValues[i][j] = serialNo++;
					} else if (j == 7) {
						tableValues[i][j] = Integer.parseInt(String.valueOf(rs
								.getObject(j - 2)))
								* Integer.parseInt(String.valueOf(rs
										.getObject(j - 1)));
					} else {
						tableValues[i][j] = rs.getObject(j);
					}

				}
				rs.next();
			}

			DefaultTableModel dm = new DefaultTableModel(tableValues,
					tableColumn) {
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}

				@Override
				public Class<? extends Object> getColumnClass(int column) {
					return getValueAt(0, column).getClass();
				}
			};

			table = new JTable(dm);
			table.setRowHeight(20);
			table.getTableHeader().setReorderingAllowed(false);
			Font myFont = new Font("Serif", Font.BOLD, 14);
			table.getTableHeader().setFont(myFont);

			tablePanel.add(table);
			tablePanel.setLayout(new GridLayout());
			JScrollPane scrollPane = new JScrollPane(table,
					JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
					JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			tablePanel.add(scrollPane);
			table.setPreferredScrollableViewportSize(new Dimension(400, 310));
			// table.getColumnModel().getColumn(3)
			// .setCellEditor(new DefaultCellEditor(generateBox()));
			table.setColumnSelectionAllowed(true);
			this.add(tablePanel, BorderLayout.CENTER);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// public static void main(String[] args) {
	// SwingUtilities.invokeLater(new Runnable() {
	//
	// @Override
	// public void run() {
	// new StockList().setVisible(true);
	//
	// }
	// });
	// }

}
