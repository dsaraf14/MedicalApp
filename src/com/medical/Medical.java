/**
 *
 * @author Deepak saraf
 */
package com.medical;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import com.util.Utility;

import db.DBUtil;

public class Medical extends JFrame implements ActionListener {

	private static final long serialVersionUID = -1161411112922594814L;

	private JPanel stockPanel, topPanel;
	private JButton addMoreRows, removeRows, addToDatabase;
	private JMenu stockMenu, topMenu;
	private JMenuItem stockList;
	private static long serialNo = 1;
	private JTable stockTable;
	private DefaultTableModel dm;

	public Medical() {
		setSize(900, 400);
		setLocationRelativeTo(this);
		setTitle("Medical");
		getContentPane().setLayout(new BorderLayout());
		setResizable(true);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		// topPanel = new JPanel();
		// topPanel.setLayout(new BorderLayout());
		// topMenu = new JMenu();
		// JMenuItem topMenuItem = new JMenuItem("Profile");
		// topMenu.add(topMenuItem);
		// JMenuBar topMenuBar = new JMenuBar();
		// topMenuBar.add(topMenu);
		// setJMenuBar(topMenuBar);

		// JButton j = new JButton("hskdhsdhi");
		// topPanel.add(j);

		// --------------------------------------------------------------

		JTabbedPane medicalStockPane = new JTabbedPane();
		stockPanel = new JPanel();
		stockPanel.setLayout(new BorderLayout());
		stockPanel();

		// -------------------- Bill Generation--------
		medicalStockPane.add("Stock Management", stockPanel);

		JPanel billPanel = new JPanel();
		medicalStockPane.add("Generate Bill", billPanel);
		getContentPane().add(medicalStockPane, BorderLayout.CENTER);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				int i = JOptionPane.showConfirmDialog(null, "You will lost Unsaved changes, Do You Really Want To Close ?", "Confirm Exit",
						JOptionPane.OK_CANCEL_OPTION);
				switch (i) {
				case JOptionPane.OK_OPTION:
					System.exit(0);
					break;
				case JOptionPane.CANCEL_OPTION:
					break;
				}
			}
		});
	}

	private void stockPanelNorth() {
		JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		stockMenu = new JMenu("Tools");
		stockMenu.setPreferredSize(new Dimension(50, 30));
		stockList = new JMenuItem("Stock List");
		stockList.addActionListener(this);
		stockMenu.add(stockList);
		stockMenu.setBorder(new BevelBorder(BevelBorder.RAISED));

		JMenuBar menuBar = new JMenuBar();
		menuBar.add(stockMenu);
		northPanel.add(menuBar);
		northPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		stockPanel.add(northPanel, BorderLayout.NORTH);
	}

	private void stockPanelEast() {
		JPanel eastPanel = new JPanel();
		eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.Y_AXIS));

		addMoreRows = new JButton("+");// new
										// ImageIcon("resources/images/plus.png"));
		addMoreRows.setToolTipText("Add More Rows");
		addMoreRows.setContentAreaFilled(false);
		addMoreRows.setBorderPainted(true);

		addMoreRows.addActionListener(this);
		eastPanel.add(addMoreRows);

		removeRows = new JButton("--");// new
										// ImageIcon("resources/images/minus.png"));
		removeRows.setToolTipText("Remove Rows");
		removeRows.setContentAreaFilled(false);
		removeRows.setBorderPainted(true);
		removeRows.addActionListener(this);
		eastPanel.add(removeRows);

		stockPanel.add(eastPanel, BorderLayout.EAST);
	}

	private void stockPanelSouth() {
		JPanel southPanel = new JPanel();
		addToDatabase = new JButton("Add To Database");
		addToDatabase.addActionListener(this);
		southPanel.add(addToDatabase);
		stockPanel.add(southPanel, BorderLayout.SOUTH);
	}

	private void stockPanel() {
		stockPanelEast();
		stockPanelNorth();
		stockPanelSouth();

		JPanel tablePanel = new JPanel();
		tablePanel.setLayout(new GridLayout());

		Object[][] data = new Object[][] { { serialNo } };
		Object[] column = new Object[] { "S.N.", "Name of Medicine", "Today", "MFG Date", "EXP Date", "Total Packets", "Tablet in a Packet", "Total Medicines" };

		dm = new DefaultTableModel(data, column) {
			private static final long serialVersionUID = -3082894031868827075L;

			@Override
			public boolean isCellEditable(int row, int column) {
				// Only 1st and 7th columns are noneditable
				return !(column == 0 || column == 7);
			}

			// @Override
			// public Class<? extends Object> getColumnClass(int column) {
			// if (column == 2) {
			// return Date.class;
			// }
			// return getValueAt(0, column).getClass();
			// }
		};

		stockTable = new JTable(dm);
		stockTable.setRowHeight(20);
		stockTable.getTableHeader().setReorderingAllowed(false);
		Font myFont = new Font("Serif", Font.BOLD, 12);
		stockTable.getTableHeader().setFont(myFont);

		tablePanel.add(stockTable);

		JScrollPane tableScroll = new JScrollPane(stockTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		stockTable.setPreferredScrollableViewportSize(new Dimension(400, 280));
		TableColumn colummn = stockTable.getColumnModel().getColumn(2);
		// colummn.setCellEditor(new d);
		// colummn.setCellRenderer(stockTable.getDefaultRenderer(LocalDateTime.class));

		tablePanel.add(tableScroll);
		stockPanel.add(tablePanel, BorderLayout.CENTER);
	}

	private boolean isValidData() {
		for (int i = 0; i < stockTable.getRowCount(); i++) {
			for (int j = 0; j < stockTable.getColumnCount(); j++) {
				if (String.valueOf(stockTable.getValueAt(i, j)).trim().equals("") || stockTable.getValueAt(i, j) == null) {
					return false;
				}
			}
		}
		return true;
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Medical().setVisible(true);
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent arg) {
		if (arg.getActionCommand().equalsIgnoreCase(addMoreRows.getText())) {
			dm.addRow(new Object[] { ++serialNo });
		} else if (arg.getActionCommand().equalsIgnoreCase(removeRows.getText())) {
			if (serialNo > 1) {
				dm.removeRow((int) --serialNo);
			}
		} else if (arg.getActionCommand().equalsIgnoreCase(stockList.getText())) {
			new StockList().setVisible(true);
		}

		else if (arg.getActionCommand().equalsIgnoreCase(addToDatabase.getText())) {

			if (isValidData()) {
				Connection con = DBUtil.getCon();
				try (PreparedStatement ps = con
						.prepareStatement("INSERT INTO medical_stock(nameOfMedicine, dateOfInsert, dateOfMFG, dateOfEXP, totalPackets, tabletInAPacket) VALUES(?,?,?,?,?,?)");) {
					int status = 0;
					for (int i = 0; i < stockTable.getRowCount(); i++) {
						ps.setObject(1, stockTable.getValueAt(i, 1));
						ps.setObject(2, stockTable.getValueAt(i, 2));
						ps.setObject(3, stockTable.getValueAt(i, 3));
						ps.setObject(4, stockTable.getValueAt(i, 4));
						ps.setObject(5, stockTable.getValueAt(i, 5));
						ps.setObject(6, stockTable.getValueAt(i, 6));
						if ((status = ps.executeUpdate()) == 0) {
							Utility.warningPopup("Adding to Database is failed on S.N. : " + (i + 1) + "...Try Again Later");
						}
					}
					if (status != 0) {
						Utility.warningPopup("Stock(s) has been succesfully added to Database");
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}

			} else {
				Utility.warningPopup("Please enter all the Manadatary Data or Remove the empty Row(s)");
			}
		}
	}
}
