/**
 *
 * @author Deepak saraf
 */
package com.admin;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.util.Utility;

import db.DBUtil;

public class ManageAccount extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JTable table;
	private JPanel tablePanel = new JPanel();
	private JPanel buttonPanel = new JPanel();
	private JButton refresh, update;
	private DefaultTableModel dm;

	public ManageAccount() {
		setSize(800, 400);
		setLocationRelativeTo(this);
		setTitle("Manage Account");
		setLayout(new BorderLayout());
		setResizable(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		refresh = new JButton("Refresh");
		refresh.addActionListener(this);
		buttonPanel.add(refresh);

		update = new JButton("Update Details");
		update.addActionListener(this);
		buttonPanel.add(update);

		add(buttonPanel, BorderLayout.SOUTH);

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				int i = JOptionPane.showConfirmDialog(null, "You'll lose unsaved Details. Still Want To Close ?", "Confirm Exit", JOptionPane.OK_CANCEL_OPTION);
				switch (i) {
				case JOptionPane.OK_OPTION:
					System.exit(0);
					break;
				case JOptionPane.CANCEL_OPTION:
					break;
				}
			}
		});

		initValues();

	}

	private void initValues() {
		Connection con = DBUtil.getCon();
		try (PreparedStatement ps = con.prepareStatement("SELECT name, email, mobile_no, role, active FROM sign_up");) {
			ResultSet rs = ps.executeQuery();
			Object[] tableColumn = new String[] { "Name", "Email", "Mobile No", "Role", "Active" };
			int rowCount = 0;
			while (rs.next()) {
				rowCount++;
			}
			Object[][] tableValues = new Object[rowCount][tableColumn.length];
			rs.first();
			for (int i = 0; i < rowCount; i++) {
				for (int j = 0; j < tableColumn.length; j++) {
					tableValues[i][j] = rs.getObject(j + 1);
				}
				rs.next();
			}

			dm = new DefaultTableModel(tableValues, tableColumn) {
				private static final long serialVersionUID = 1L;

				@Override
				public Class<? extends Object> getColumnClass(int column) {
					return getValueAt(0, column).getClass();
				}

				@Override
				public boolean isCellEditable(int row, int column) {
					// Only the third & forth column is editable

					int tempRow = 0;
					for (int i = 0; i < dm.getRowCount(); i++) {
						String role = (String) table.getValueAt(i, 3);
						if (role.equals(Utility.readProperties().getProperty("AdminRole"))) {
							tempRow = i;
						}
					}
					if (row == tempRow) {
						return false;
					} else {
						return column == 3 || column == 4;
					}
				}
			};

			table = new JTable(dm);
			table.setRowHeight(20);
			table.getTableHeader().setReorderingAllowed(false);
			Font myFont = new Font("Serif", Font.BOLD, 14);
			table.getTableHeader().setFont(myFont);

			tablePanel.add(table);
			JScrollPane scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			tablePanel.add(scrollPane);
			table.setPreferredScrollableViewportSize(new Dimension(400, 310));
			table.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(generateBox()));
			table.setColumnSelectionAllowed(true);
			tablePanel.setLayout(new GridLayout());
			this.add(tablePanel, BorderLayout.NORTH);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private JComboBox<String> generateBox() {
		Properties ps = Utility.readProperties();
		JComboBox<String> bx = new JComboBox<String>();
		bx.addItem(ps.getProperty("MedicalRole"));
		bx.addItem(ps.getProperty("HospitalRole"));
		return bx;
	}

	// public static void main(String[] args) {
	// SwingUtilities.invokeLater(new Runnable() {
	// @Override
	// public void run() {
	// new ManageAccount().setVisible(true);
	// }
	// });
	// }

	@Override
	public void actionPerformed(ActionEvent arg) {
		if (arg.getActionCommand().equalsIgnoreCase(refresh.getText())) {
			this.dispose();
			new ManageAccount().setVisible(true);
		} else if (arg.getActionCommand().equalsIgnoreCase(update.getText())) {
			updateDetails();
			refresh.doClick();
		}
	}

	private void updateDetails() {
		Connection con = DBUtil.getCon();
		try (PreparedStatement ps = con.prepareStatement("UPDATE sign_up SET role = ?, active = ? WHERE email = ?");) {

			int status = 0;
			for (int i = 0; i < table.getRowCount(); i++) {
				ps.setObject(1, table.getValueAt(i, 3));
				ps.setObject(2, table.getValueAt(i, 4));
				ps.setObject(3, table.getValueAt(i, 1));

				if ((status = ps.executeUpdate()) == 0) {
					Utility.warningPopup("Updation is failed after all Email of: " + table.getValueAt(i, 1) + "...Try Again Later");
					break;
				}
			}
			if (status != 0) {
				Utility.warningPopup("Details have been Updated successfully");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}