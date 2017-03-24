/***
 * 
 * @author Yingyi Zhang
 * @username yxz559
 * @studentID 1544458
 * 
 */

package view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JButton;

import java.io.File;

/**
 * 
 * The program entry
 * 
 */
public class DataPlagiarismDetect extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	private JButton btnSelect;
	private JButton btnStart;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		DataPlagiarismDetect dpd = new DataPlagiarismDetect();
		dpd.setVisible(true);
		
	}

	/**
	 * Create the frame.
	 */
	public DataPlagiarismDetect() {
		
		// Initialise the frame
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0};
		gbl_panel.rowHeights = new int[]{0};
		gbl_panel.columnWeights = new double[]{Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.CENTER);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[]{0, 0, 0};
		gbl_panel_1.rowHeights = new int[]{0, 0, 0, 0, 0};
		gbl_panel_1.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gbl_panel_1.rowWeights = new double[]{1.0, 1.0, 0.0, 1.0, Double.MIN_VALUE};
		panel_1.setLayout(gbl_panel_1);
		
		// The title label
		JLabel label = new JLabel("Data Plagiarism Detect");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("Arial", Font.PLAIN, 24));
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.gridwidth = 2;
		gbc_label.insets = new Insets(0, 0, 5, 5);
		gbc_label.gridx = 0;
		gbc_label.gridy = 0;
		panel_1.add(label, gbc_label);
		
		// The text field for PDF files directory path
		textField = new JTextField();
		textField.setFont(new Font("Arial", Font.PLAIN, 12));
		textField.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.gridx = 0;
		gbc_textField.gridy = 1;
		panel_1.add(textField, gbc_textField);
		textField.setColumns(10);
		textField.setEditable(false);
		
		// The button which is used to open the file chooser
		btnSelect = new JButton("Select");
		btnSelect.setFont(new Font("Arial", Font.PLAIN, 12));
		GridBagConstraints gbc_btnSelect = new GridBagConstraints();
		gbc_btnSelect.insets = new Insets(0, 0, 5, 0);
		gbc_btnSelect.gridx = 1;
		gbc_btnSelect.gridy = 1;
		panel_1.add(btnSelect, gbc_btnSelect);
		
		// The button which is used to start the program
		btnStart = new JButton("Start");
		GridBagConstraints gbc_btnStart_1 = new GridBagConstraints();
		gbc_btnStart_1.gridwidth = 2;
		gbc_btnStart_1.gridx = 0;
		gbc_btnStart_1.gridy = 3;
		panel_1.add(btnStart, gbc_btnStart_1);
		
		btnSelect.addActionListener(this);
		btnStart.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		// Actions for the button Select
		if (e.getSource() == btnSelect){
	
			JFileChooser jf = new JFileChooser();
			jf.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			jf.showDialog(new JLabel(), "Select");
			File file = jf.getSelectedFile();
			String pdfDir = "";
			if (file != null){
				pdfDir = file.getAbsolutePath() + '\\';
			}
			textField.setText(pdfDir);
		
		}
		// Actions for the button Start
		else if (e.getSource() == btnStart){
			
			String pdfDir = textField.getText();
			if (pdfDir.length() == 0){
				
				JOptionPane.showMessageDialog(null, "Please select a directory.", 
						"Error", JOptionPane.ERROR_MESSAGE);
				
			}
			else {
			
				this.dispose();
				ProgramFrame pf = new ProgramFrame(pdfDir);
				pf.setVisible(true);
				
			}
			
		}
		
	}

}
