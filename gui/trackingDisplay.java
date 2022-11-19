package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import components.MainOffice;

import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.ComponentOrientation;
import javax.swing.JPanel;

public class trackingDisplay implements Runnable {

	private JFrame frame;
	JScrollPane scrollPane_1;
	JTextArea textArea;
	boolean live = true;
	
//	/**
//	 * Launch the application.
//	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					trackingDisplay window = new trackingDisplay();
//					window.frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the application.
	 */
	public trackingDisplay() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 900, 700);
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(null);
		frame.setVisible(true);
		frame.addWindowListener(new WindowAdapter() {
		    public void windowClosing(WindowEvent e) {
		    	live = false;
		        frame.dispose();
		    }
		});
		
		JLabel lblNewLabel = new JLabel("~ Post office Tracking system ~");
		lblNewLabel.setFont(new Font("Arial Black", Font.BOLD, 16));
		lblNewLabel.setOpaque(true);
		lblNewLabel.setBackground(Color.WHITE);
		lblNewLabel.setBounds(257, 0, 299, 24);
		frame.getContentPane().add(lblNewLabel);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBounds(0, 0, 884, 26);
		frame.getContentPane().add(panel);
		
		scrollPane_1 = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane_1.setBounds(0, 24, 884, 637);
		frame.getContentPane().add(scrollPane_1);
		
		textArea = new JTextArea();
		textArea.setBackground(Color.WHITE);
		textArea.setFont(new Font("Arial Black", Font.BOLD, 14));
		scrollPane_1.setViewportView(textArea);
		textArea.setWrapStyleWord(true);
	}
	
	
	/**
	 * Reading from file "tracking.txt" into the value "textArea"
	 */
	private void readFromFile()
	{
		try {
			MainOffice.getRwlock().readLock().lock();
			File f = new File("tracking.txt");
			Scanner s = new Scanner(f);
			while(s.hasNextLine())
			{
				textArea.append(s.nextLine()+"\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			MainOffice.getRwlock().readLock().unlock();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(live)
		{
			if(!Simulator.suspended)
			{
				textArea.setText("");
				readFromFile();
				System.out.println("Exporting tracking report now!");
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}
		}
	
		
	}
}
