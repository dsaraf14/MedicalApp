import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import com.hospital.Hospital;
import com.medical.MedicalStock;

public class Admin extends JFrame implements ActionListener {

	private JButton medical, hospital;

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

	}

	public static void main(String[] args) {

		Admin a = new Admin();
		a.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equalsIgnoreCase(hospital.getText())) {
			this.dispose();
			new Hospital().setVisible(true);
		} else if (e.getActionCommand().equalsIgnoreCase(medical.getText())) {
			this.dispose();
			new MedicalStock().setVisible(true);
		}

	}
}
