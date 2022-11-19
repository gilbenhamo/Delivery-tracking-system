package gui;

import java.awt.Button;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.*;
import javax.swing.text.AbstractDocument.BranchElement;

import components.Branch;
import components.Customer;
import components.Hub;
import components.MainOffice;
import components.Memento;
import components.Package;
import components.StandardTruck;
import components.Truck;

/**
 * Panel of all system buttons
 * @version 2.0, 8/5/2021
 * @author ItzikRahamim - 312202351
 * @author GilBenHamo - 315744557
 */
public class buttonsPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	private static boolean started = false;
	private JButton createSystem, start, stop, resume, allPackInfo, branchInfo, cloneBrunch, report;
	private static JButton restore;
	private JScrollPane s_pane;
	private JScrollPane savePane;
	private static JScrollPane bSavePane;
	private boolean infoTableFlag = false, branchInfoFlag = false;

	 /** 
	  * Constructs and initializes a the buttonsPanel by default<br>
	  */
	public buttonsPanel()
	{
		// Create Buttons
		createSystem = new JButton("Create system");
		start = new JButton("Start");
		stop = new JButton("Stop");
		resume = new JButton("Resume");
		allPackInfo = new JButton("All packages info");
		branchInfo = new JButton("Branch info");
		cloneBrunch = new JButton("Clone Branch");
		restore = new JButton("Restore");
		report = new JButton("Report");
		
		// Set buttons disable mode
		start.setEnabled(false);
		stop.setEnabled(false);
		resume.setEnabled(false);
		cloneBrunch.setEnabled(false);
		restore.setEnabled(false);
		report.setEnabled(false);

		// Set the size and layout
		this.setBounds(0,650, 1200, 20);
		this.setLayout(new GridLayout(1,9));
		
		// Add Listeners
		createSystem.addActionListener(this);
		start.addActionListener(this);
		stop.addActionListener(this); 
		resume.addActionListener(this); 
		allPackInfo.addActionListener(this);
		branchInfo.addActionListener(this);
		cloneBrunch.addActionListener(this);
		restore.addActionListener(this);
		report.addActionListener(this);
		
		// Set color
		this.createSystem.setBackground(Color.LIGHT_GRAY);
		this.start.setBackground(Color.LIGHT_GRAY);
		this.stop.setBackground(Color.LIGHT_GRAY);
		this.resume.setBackground(Color.LIGHT_GRAY);
		this.allPackInfo.setBackground(Color.LIGHT_GRAY);
		this.branchInfo.setBackground(Color.LIGHT_GRAY);
		this.cloneBrunch.setBackground(Color.LIGHT_GRAY);
		this.restore.setBackground(Color.LIGHT_GRAY);
		this.report.setBackground(Color.LIGHT_GRAY);
		
		// Add the buttons to the panel
		this.add(createSystem);
		this.add(start);
		this.add(stop);
		this.add(resume);
		this.add(allPackInfo);
		this.add(branchInfo);
		this.add(cloneBrunch);
		this.add(restore);
		this.add(report);
	}

	/**
	 * Manage actions
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// User pressed on "CREATE SYSTEM" button
		if(e.getSource().equals(createSystem))
		{
			// Open new dialog
			new Dialog();
			
			// Enable press "START" and run system
			//Simulator.setSuspended(false);
			start.setEnabled(true);
			cloneBrunch.setEnabled(true);
		}
		
		// User pressed on "START" button
		if(e.getSource().equals(start))
		{
			started = true;
			// Start to run the simulator system on screen
			System.out.println("\n======================= START ========================\n");
			Simulator.setSuspended(false);
			Simulator.getMain_off().setLive(true);
			new Thread(Simulator.getMain_off()).start();
			Simulator.getMain_off().doStart();
			
			// Disable "START" button
			((JButton)e.getSource()).setEnabled(false);
			
			// Enable "STOP" button
			stop.setEnabled(true);
			report.setEnabled(true);
		}
		
		// User pressed "STOP"
		if(e.getSource().equals(stop))
		{	
			// Suspend system
			Simulator.setSuspended(true);
			
			// Enable "RESUME" and disable "STOP" buttons
			resume.setEnabled(true);
			stop.setEnabled(false);
		}
		
		// User pressed "RESUME"
		if(e.getSource().equals(resume))
		{

			wakeUpAllThreads();
			// Enable "STOP" and disable "RESUME" buttons
			resume.setEnabled(false);
			stop.setEnabled(true);
		}
		
		// User pressed "AllPackaInfo"
		if(e.getSource().equals(allPackInfo))
		{
			if(Simulator.getMain_off()!=null)
			{
				// If info table is not on screen already 
				if(!infoTableFlag) 
				{
					// If branches info table is on screen already turn it off
					if(bSavePane!=null && branchInfoFlag) 
					{
						// Delete old branch table
						Simulator.getSimuPanel().remove(bSavePane);
						branchInfoFlag = false;
					}
					
					// Create and set info table
					JTable packTable = Simulator.getMain_off().createAllPacksTable();
					s_pane = new JScrollPane(packTable);
					s_pane.setBounds(0,0,450
							,packTable.getRowCount()*packTable.getRowHeight() + 24);
					savePane=s_pane;
					Simulator.getSimuPanel().add(s_pane);
					infoTableFlag = true;
				}
				// If info table is on screen already 
				else
				{
					// Turn in off
					infoTableFlag = false;
					if(savePane!=null)
					{
						// Delete old branch table
						Simulator.getSimuPanel().remove(savePane);	
					}

				}
				Simulator.getSimuPanel().validate();
				Simulator.getSimuPanel().repaint();
			}	
		}
		
		// User pressed "BranchInfo" button
		if(e.getSource().equals(branchInfo))
		{
			if(Simulator.getMain_off()!=null)
			{	
				// If branch table is not on screen already 
				if(!branchInfoFlag)
				{
					if(savePane!=null)
					{	
						// Remove it
						Simulator.getSimuPanel().remove(savePane);
						infoTableFlag = false;
					}
					
					// Create a branch info dialog
					new BranchInfoDialog();
					branchInfoFlag = true;
				}
				// If branch table is on screen already 
				else
				{
					//Turn the exists table off
					if(bSavePane!=null)
					{
						Simulator.getSimuPanel().remove(bSavePane);
						infoTableFlag = false;
					}
					branchInfoFlag = false;
				}
				Simulator.getSimuPanel().validate();
				Simulator.getSimuPanel().repaint();
			}
		}
		
		if(e.getSource().equals(cloneBrunch))
		{
			new BranchCloneDialog();	
			Simulator.setSuspended(true);

		}
		
		if(e.getSource().equals(report))
		{
			new Thread(new trackingDisplay()).start();
		}
		
		if(e.getSource().equals(restore))
		{	
			if(!started) 
			{
				MainOffice m = Simulator.getMain_off();
				Hub h = Simulator.getMain_off().getHub();
				h.getBranches().remove(h.getBranches().size()-1);
				h.getExitYPoints().remove(h.getExitYPoints().size()-1);
				Simulator.getMain_off().setCurrent_cord(m.getCurrent_cord() -( 30 + m.getSpaces()));
				Simulator.getMain_off().setEnd_y(m.getEnd_y() - m.getExit_spaces());
				Branch.setSerialBranchID(Branch.getSerialBranchID()-1);
				Truck.setTruckSerialId(Truck.getTruckSerialId()-m.getTRUCKS_NUM());
				Simulator.getSimuPanel().repaint();				
			}
			else
			{
				Simulator.caretaker.restoreMemento();
				if(Simulator.caretaker.getStateList().isEmpty())
					restore.setEnabled(false);
				wakeUpAllThreads();					//maybe add flag when its not started yet;
			}
		}
	}

	/**
	 * @return Temporary instance of table panel 
	 */
	public static JScrollPane getbSavePane() {
		return bSavePane;
	}

	/**
	 * Sets the Temporary instance of table panel 
	 * @param bSavePane Temporary instance of table panel 
	 */
	public static void setbSavePane(JScrollPane bSavePane) {
		buttonsPanel.bSavePane = bSavePane;
	}

	/**
	 * @return Branch info button
	 */
	public JButton getBranchInfo() {
		return branchInfo;
	}

	/**
	 * Sets the Branch info button
	 * @param branchInfo Branch info button
	 */
	public void setBranchInfo(JButton branchInfo) {
		this.branchInfo = branchInfo;
	}
	
	/**
	 * Waking up all sleeping treads (Tracks, Branches, Costumers)
	 */
	public static void wakeUpAllThreads()
	{
		// Notify all threads
		Simulator.setSuspended(false);
		
		// Wake up all threads (Hub, Branches, Trucks)
		Simulator.getMain_off();
		Hub h = MainOffice.getHub();
		h.wakeUp();
		for(Truck t : h.getListTrucks())
			t.wakeUp();
		for(Branch b : h.getBranches())
		{
			b.wakeUp();
			for(Truck t :b.getListTrucks())
				t.wakeUp();
		}
		for(Customer c : MainOffice.getInstance().getCustomers())
			c.wakeUp();
	}

	/**
	 * @return Restore button
	 */
	public static JButton getRestore() {
		return restore;
	}

	/**
	 * Set a new restore button
	 * @param restore Restore button
	 */
	public static void setRestore(JButton restore) {
		buttonsPanel.restore = restore;
	}

	/**
	 * Checking if the system started to work (user clicked "Start")
	 * @return true if started
	 */
	public static boolean isStarted() {
		return started;
	}

	/**
	 * Set a new started value (if the system started)
	 * @param started
	 */
	public static void setStarted(boolean started) {
		buttonsPanel.started = started;
	}
	
	
	
}






/**
 * The dialog window of choosing the branch we want to see his info
 * @version 2.0, 8/5/2021
 * @author ItzikRahamim - 312202351
 * @author GilBenHamo - 315744557
 */
class BranchInfoDialog extends JDialog implements ActionListener
{

	private static final long serialVersionUID = 1L;
	
	JComboBox<String> b_info_cbox;
	JButton ok,cancel;
	Vector<String> branches_names;
	private JScrollPane b_s_pane;

	/**
	 * Constructs and initializes a the BranchInfoDialog by default<br>
	 */
	public BranchInfoDialog()
	{
		//SETUP
		ok = new JButton("OK");
		cancel = new JButton("Cancel");
		branches_names = new Vector<String>();
		fillCbox();
		this.setLocationRelativeTo(null);
		this.setSize(300,150);
		this.setLayout(null);
		b_info_cbox.setBounds(20,10,240,30);
		ok.setBounds(55,60,80,30);
		ok.addActionListener(this);
		cancel.addActionListener(this);
		cancel.setBounds(145,60,80,30);
		
		// Add buttons to dialog
		this.add(b_info_cbox);
		this.add(ok);
		this.add(cancel);
		
		this.setVisible(true);
	}
	
	/**
	 * Fill the ComboBox with values to choose (Branches)
	 */
	public void fillCbox()
	{
		// Get number of branches
		Simulator.getMain_off();
		int size = MainOffice.getHub().getBranches().size();
		
		// Create a list of branches names
		branches_names.add("Sorting center");
		for(int i=0;i<size;i++)
		{
			branches_names.add("Branch " + (i+1));
		}
		
		// Initialize the ComboBox with values
		b_info_cbox = new JComboBox<>(branches_names.toArray(new String[size]));
	}

	/**
	 * Manage actions
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		ArrayList<Package> arr; //Temporary packages list
		
		// User pressed "OK"
		if(e.getSource().equals(ok) )
		{
			// Check what user chose 
			JTable table;
			String choosen_branch = b_info_cbox.getItemAt(b_info_cbox.getSelectedIndex());
			if(choosen_branch.equals("Sorting center"))
				arr = Simulator.getMain_off().getCurrentHubPacks();
			else
				arr = Simulator.getMain_off().getCurrentPacksByBranch(b_info_cbox.getSelectedIndex()-1);
			
			// Create specific table
			table = Simulator.getMain_off().createTabelByArray(arr);
			new JPanel();
			b_s_pane = new JScrollPane(table);
			b_s_pane.setBounds(0,0,450 ,arr.size() *table.getRowHeight() + 24);
			b_s_pane.getViewport().add(table);
			buttonsPanel.setbSavePane(b_s_pane);
			Simulator.getSimuPanel().add(b_s_pane);
			
			Simulator.getSimuPanel().validate();
			Simulator.getSimuPanel().repaint();
			dispose();
		}
		
		// User pressed "CANCEL"
		if(e.getSource().equals(cancel) )
		{
			dispose();
		}
	}

	/**
	 * @return Buttons scroll pane
	 */
	public JScrollPane getB_s_pane() {
		return b_s_pane;
	}

	/**
	 * Sets new Buttons scroll pane
	 * @param b_s_pane Buttons scroll pane
	 */
	public void setB_s_pane(JScrollPane b_s_pane) {
		this.b_s_pane = b_s_pane;
	}

	/**
	 * @return Branch info ComboBox
	 */
	public JComboBox<String> getB_info_cbox() {
		return b_info_cbox;
	}

	/**
	 * Sets new Branch info ComboBox
	 * @param b_info_cbox Branch info ComboBox
	 */
	public void setB_info_cbox(JComboBox<String> b_info_cbox) {
		this.b_info_cbox = b_info_cbox;
	}
}

class BranchCloneDialog extends JDialog implements ActionListener
{

	private static final long serialVersionUID = 1L;
	
	JComboBox<String> b_info_cbox;
	JButton ok,cancel;
	Vector<String> branches_names;
	private JScrollPane b_s_pane;

	/**
	 * Constructs and initializes a the BranchInfoDialog by default<br>
	 */
	public BranchCloneDialog()
	{
		//SETUP
		ok = new JButton("OK");
		cancel = new JButton("Cancel");
		branches_names = new Vector<String>();
		fillCbox();
		this.setLocationRelativeTo(null);
		this.setSize(300,150);
		this.setLayout(null);
		b_info_cbox.setBounds(20,10,240,30);
		ok.setBounds(55,60,80,30);
		ok.addActionListener(this);
		cancel.addActionListener(this);
		cancel.setBounds(145,60,80,30);
		
		// Add buttons to dialog
		this.add(b_info_cbox);
		this.add(ok);
		this.add(cancel);
		
		this.setVisible(true);
	}
	
	/**
	 * Fill the ComboBox with values to choose (Branches)
	 */
	public void fillCbox()
	{
		// Get number of branches
		Simulator.getMain_off();
		int size = MainOffice.getHub().getBranches().size();
		
		// Create a list of branches names
		for(int i=0;i<size;i++)
		{
			branches_names.add("Branch " + (i+1));
		}
		
		// Initialize the ComboBox with values
		b_info_cbox = new JComboBox<>(branches_names.toArray(new String[size]));
	}

	/**
	 * Manage actions
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// User pressed "OK"
		//create new clone branch and reset it by protoBranch
		
		if(e.getSource().equals(ok) )
		{
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// Check what user chose 	
			MainOffice m = Simulator.getMain_off();
			if(buttonsPanel.isStarted())
				Simulator.caretaker.addMemento(new Memento());
			Hub h = MainOffice.getHub();
			Branch b = h.getBranchByZip(b_info_cbox.getSelectedIndex()).clone();
			b.protoBranch();
			
			//init cords
			b.setY_cord(m.getCurrent_cord());
			m.setCurrent_cord(m.getCurrent_cord() + 30 + m.getSpaces());
			h.getExitYPoints().add(m.getEnd_y());
			m.setEnd_y(m.getEnd_y() + m.getExit_spaces());
			
			h.getBranches().add(b);
			Thread Tb = new Thread(b);
		//	MainOffice.getInstance().getAllThreads().add(Tb);
			Tb.start();
			

			buttonsPanel.wakeUpAllThreads();
			buttonsPanel.getRestore().setEnabled(true);
			Simulator.getSimuPanel().validate();
			Simulator.getSimuPanel().repaint();
			dispose();
		}
		
		// User pressed "CANCEL"
		if(e.getSource().equals(cancel) )
		{
			buttonsPanel.wakeUpAllThreads();
			dispose();
		}
	}

	/**
	 * @return Buttons scroll pane
	 */
	public JScrollPane getB_s_pane() {
		return b_s_pane;
	}

	/**
	 * Sets new Buttons scroll pane
	 * @param b_s_pane Buttons scroll pane
	 */
	public void setB_s_pane(JScrollPane b_s_pane) {
		this.b_s_pane = b_s_pane;
	}

	/**
	 * @return Branch info ComboBox
	 */
	public JComboBox<String> getB_info_cbox() {
		return b_info_cbox;
	}

	/**
	 * Sets new Branch info ComboBox
	 * @param b_info_cbox Branch info ComboBox
	 */
	public void setB_info_cbox(JComboBox<String> b_info_cbox) {
		this.b_info_cbox = b_info_cbox;
	}
}
