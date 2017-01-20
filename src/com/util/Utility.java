package com.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JOptionPane;

import com.pathConstant.PathConstant;

public class Utility {

	public static Properties readProperties() {
		Properties p = new Properties();
		try (FileInputStream fi = new FileInputStream(
				PathConstant.propertiesPath)) {
			p.load(fi);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return p;
	}

	public static void warningPopup(String message) {
		int i = JOptionPane.showConfirmDialog(null, message, "Warning",
				JOptionPane.DEFAULT_OPTION);
		switch (i) {
		case JOptionPane.OK_OPTION:
			break;
		}
	}
}
