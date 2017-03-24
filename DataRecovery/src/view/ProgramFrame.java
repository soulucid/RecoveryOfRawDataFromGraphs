/***
 * 
 * @author Yingyi Zhang
 * @username yxz559
 * @studentID 1544458
 * 
 */

package view;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import java.awt.GridLayout;
import java.awt.Font;

import processGraph.GraphDataRecover;
import processGraph.GraphProcessor;
import processImage.ImageProcessor;
import processPDF.PDFProcessor;

/**
 * 
 * The frame for the whole program process
 * 
 */
public class ProgramFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lblProcess;

	/**
	 * Create the frame.
	 */
	public ProgramFrame(String pdfDir) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(1, 0, 0, 0));
		
		lblProcess = new JLabel();
		lblProcess.setText("Done");
		lblProcess.setFont(new Font("Arial", Font.PLAIN, 22));
		lblProcess.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblProcess);
		
		JOptionPane.showMessageDialog(null, "Start to extract images from PDFs.", 
				"Info", JOptionPane.INFORMATION_MESSAGE);
		
		extractImages(pdfDir);
		
		JOptionPane.showMessageDialog(null, "All images are extracted.\nStart to divide images.", 
				"Info", JOptionPane.INFORMATION_MESSAGE);
		
		divideImages();
		
		JOptionPane.showMessageDialog(null, "All images are divided.\nStart to recognise graphs.", 
				"Info", JOptionPane.INFORMATION_MESSAGE);
		
		classifyGraphs();
		
		JOptionPane.showMessageDialog(null, "All classified graphs are saved.", 
				"Info", JOptionPane.INFORMATION_MESSAGE);
		
		recoverData();
		
		JOptionPane.showMessageDialog(null, "All recovered data and original graph images are saved.", 
				"Info", JOptionPane.INFORMATION_MESSAGE);
		
	}
	
	public void extractImages(String pdfDir){
		
		PDFProcessor pdfp = new PDFProcessor(pdfDir);
		int state = pdfp.start();
		switch (state){
		
			case 0:
				break;
			case 1:
				JOptionPane.showMessageDialog(null, "This directory doesn't contain any PDF files.", 
						"Error", JOptionPane.ERROR_MESSAGE);
				System.exit(1);
				break;
			case 2:
				JOptionPane.showMessageDialog(null, "Failed to extract images.", 
						"Error", JOptionPane.ERROR_MESSAGE);
				System.exit(1);
				break;
		
		}
		
	}
	
	public void divideImages(){
		
		ImageProcessor imgp = new ImageProcessor();
		int state = imgp.start();
		switch (state){
		
			case 0:
				break;
			case 1:
				JOptionPane.showMessageDialog(null, "The image division failed.", 
						"Error", JOptionPane.ERROR_MESSAGE);
				System.exit(1);
				break;
		
		}
		
	}
	
	public void classifyGraphs(){
		
		GraphProcessor gp = new GraphProcessor();
		int state = gp.start();
		switch (state){
		
			case 0:
				break;
			case 1:
				JOptionPane.showMessageDialog(null, "The graph classification failed.", 
						"Error", JOptionPane.ERROR_MESSAGE);
				System.exit(1);
				break;
		
		}
		
	}
	
	public void recoverData(){
		
		GraphDataRecover gdr = new GraphDataRecover();
		int state = gdr.start();
		switch (state){
		
		case 0:
			break;
		case 1:
			JOptionPane.showMessageDialog(null, "The data recovery failed.", 
					"Error", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
			break;
	
		}
		
	}

}
