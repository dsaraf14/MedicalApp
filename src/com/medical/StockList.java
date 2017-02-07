/**
 *
 * @author Deepak saraf
 */
package com.medical;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.util.Utility;

import db.DBUtil;

public class StockList extends JFrame implements ActionListener {

	private static final long serialVersionUID = -6312810958723284545L;
	private JPanel tablePanel = new JPanel();
	private JTable table;
	private long serialNo = 1;
	private JButton exportToExcel;

	public StockList() {
		setSize(900, 400);
		setLocationRelativeTo(this);
		setTitle("Stock Detail");
		setLayout(new BorderLayout());
		setResizable(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		initValues();
		stockPanelSouth();
	}

	private void stockPanelSouth() {
		JPanel southPanel = new JPanel();
		exportToExcel = new JButton("Export To Excel");
		exportToExcel.addActionListener(this);
		southPanel.add(exportToExcel);
		this.add(southPanel, BorderLayout.SOUTH);
	}

	private void initValues() {
		Connection con = DBUtil.getCon();
		try (PreparedStatement ps = con
				.prepareStatement("SELECT nameOfMedicine, dateOfInsert, dateOfMFG, dateOfEXP, totalPackets, tabletInAPacket FROM medical_stock");) {
			ResultSet rs = ps.executeQuery();
			Object[] tableColumn = new String[] { "S.N.", "Name of Medicine", "Added Date", "MFG Date", "EXP Date", "Total Packets", "Tablet in a Packet",
					"Total Medicines" };
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
						tableValues[i][j] = Integer.parseInt(String.valueOf(rs.getObject(j - 2))) * Integer.parseInt(String.valueOf(rs.getObject(j - 1)));
					} else {
						tableValues[i][j] = rs.getObject(j);
					}

				}
				rs.next();
			}

			DefaultTableModel dm = new DefaultTableModel(tableValues, tableColumn) {
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
			JScrollPane scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
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

	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				new StockList().setVisible(true);
			}
		});
	}

	private long[] parameterPopup() {
		JTextField redField = new JTextField(5);
		JTextField yellowField = new JTextField(5);

		JPanel myPanel = new JPanel();
		myPanel.add(new JLabel("Red Zone Value:* "));
		myPanel.add(redField);
		myPanel.add(Box.createVerticalStrut(15)); // a spacer
		myPanel.add(new JLabel("Yellow Zone Value:* "));
		myPanel.add(yellowField);

		int result = JOptionPane.showConfirmDialog(null, myPanel, "Set Parameter", JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			if (redField.getText().trim().equals("") || redField.getText() == null || yellowField.getText().trim().equals("") || yellowField.getText() == null) {
				Utility.warningPopup("Field(s) can't be empty");
				parameterPopup();
			} else {
				return new long[] { Long.parseLong(redField.getText().trim()), Long.parseLong(yellowField.getText().trim()) };
			}

		} else if (result == JOptionPane.CANCEL_OPTION) {
			return null;
		}
		return null;
	}

	@Override
	public void actionPerformed(ActionEvent action) {
		if (action.getActionCommand().equalsIgnoreCase(exportToExcel.getText())) {

			long[] values = parameterPopup();
			if (values != null) {
				new ExportToExcel().excel(values[0], values[1]);
				Utility.warningPopup("File successfully Exported to " + System.getProperty("user.home") + "\\Downloads");
			}
		}
	}
}

class ExportToExcel {

	public void excel(Long redZoneValue, Long yellowZoneValue) {
		XSSFWorkbook excelWorkBook = new XSSFWorkbook();
		try (FileOutputStream out = new FileOutputStream(new File(System.getProperty("user.home") + "\\Downloads" + "\\createworkbook.xlsx"));
				BufferedOutputStream bOut = new BufferedOutputStream(out);) {

			XSSFSheet spreadsheet = excelWorkBook.createSheet("Stock Detail");
			XSSFRow row = spreadsheet.createRow(5);

			XSSFFont font, redFont, yellowFont;

			font = excelWorkBook.createFont();
			font.setBold(true);

			redFont = excelWorkBook.createFont();
			redFont.setColor(IndexedColors.RED.getIndex());

			yellowFont = excelWorkBook.createFont();
			yellowFont.setColor(IndexedColors.YELLOW.getIndex());

			XSSFCellStyle style, redZoneStyle, yellowZoneStyle;

			style = excelWorkBook.createCellStyle();
			style.setFont(font);

			redZoneStyle = excelWorkBook.createCellStyle();
			redZoneStyle.setFont(redFont);

			yellowZoneStyle = excelWorkBook.createCellStyle();
			yellowZoneStyle.setFont(yellowFont);

			XSSFCell cell = null;
			myCreateCell(row, cell, 1, style).setCellValue("S.N.");
			myCreateCell(row, cell, 2, style).setCellValue("Name of Medicine");
			myCreateCell(row, cell, 3, style).setCellValue("Added Date");
			myCreateCell(row, cell, 4, style).setCellValue("MFG Date");
			myCreateCell(row, cell, 5, style).setCellValue("EXP Date");
			myCreateCell(row, cell, 6, style).setCellValue("Total Packets");
			myCreateCell(row, cell, 7, style).setCellValue("Tablets in Packet");
			myCreateCell(row, cell, 8, style).setCellValue("Total Medicines");

			Connection con = DBUtil.getCon();
			PreparedStatement ps = con
					.prepareStatement("SELECT nameOfMEdicine, dateOfInsert, dateofMFG, dateOfEXP, totalPackets, tabletInAPacket FROM medical_stock");
			ResultSet rs = ps.executeQuery();

			int rowNo = 6;
			int temp = rowNo;

			while (rs.next()) {

				XSSFCellStyle localStyle = null;
				long colorCriteria = rs.getLong(5) * rs.getLong(6);

				if (colorCriteria <= redZoneValue) {
					localStyle = redZoneStyle;
				} else if (colorCriteria <= yellowZoneValue && colorCriteria > redZoneValue) {
					localStyle = yellowZoneStyle;
				}

				row = spreadsheet.createRow(rowNo);
				myCreateCell(row, cell, 1, localStyle).setCellValue(rowNo - temp + 1);
				myCreateCell(row, cell, 2, localStyle).setCellValue(rs.getString(1));
				myCreateCell(row, cell, 3, localStyle).setCellValue(rs.getString(2));
				myCreateCell(row, cell, 4, localStyle).setCellValue(rs.getDate(3));
				myCreateCell(row, cell, 5, localStyle).setCellValue(rs.getDate(4));
				myCreateCell(row, cell, 6, localStyle).setCellValue(rs.getLong(5));
				myCreateCell(row, cell, 7, localStyle).setCellValue(rs.getLong(6));
				myCreateCell(row, cell, 8, localStyle).setCellValue(rs.getLong(5) * rs.getLong(6));
				rowNo++;
			}

			// write operation workbook using file out object
			excelWorkBook.write(bOut);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("createworkbook.xlsx written successfully");
	}

	private XSSFCell myCreateCell(XSSFRow row, XSSFCell cell, int cellNo, XSSFCellStyle style) {
		cell = row.createCell(cellNo);
		cell.setCellStyle(style);
		return cell;
	}
}
