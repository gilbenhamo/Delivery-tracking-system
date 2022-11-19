package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import components.Branch;
import components.Hub;
import components.MainOffice;
import components.Package;
import components.Truck;

/**
 * The dialog of choosing the system details<br>
 * Number of branches, How many trucks for each branch, Number of packages.
 * @version 2.0, 8/5/2021
 * @author ItzikRahamim - 312202351
 * @author GilBenHamo - 315744557
 */
public class Dialog extends JDialog implements ActionListener{
	private static final long serialVersionUID = 1L;
	private JButton ok, cancel;
	private int branch_amount=-1, trucks_amount=-1, packages_amount;
	private JSlider branch_slider, trucks_slider, packages_slider;
	private JLabel b_label, t_label, p_label;
	private JPanel ok_canel_panel,sliders_panel;
	
	 /** 
	  * Constructs and initializes a the dialog by default<br>
	  */
	public Dialog()
	{
		// SETUP
		setTitle("Create post system");
		setSize(600, 400);
		setResizable(false);
		
		// Buttons
		ok = new JButton("OK");
		cancel = new JButton("Cancel");
		ok.setBackground(Color.lightGray);
		ok.addActionListener(this);
		cancel.setBackground(Color.lightGray);
		cancel.addActionListener(this);
		
		// Sliders
		branch_slider = new JSlider(1, 10, 5);
		branch_slider.setPaintTicks(true);
		branch_slider.setMajorTickSpacing(1);
		branch_slider.setPaintTrack(true);
		branch_slider.setPaintLabels(true);
		trucks_slider = new JSlider(1, 10, 5);
		trucks_slider.setPaintTicks(true);
		trucks_slider.setMajorTickSpacing(1);
		trucks_slider.setPaintTrack(true);
		trucks_slider.setPaintLabels(true);
		packages_slider = new JSlider(2, 20, 10);
		packages_slider.setPaintTicks(true);
		packages_slider.setMajorTickSpacing(2);
		packages_slider.setMinorTickSpacing(1);
		packages_slider.setPaintTrack(true);
		packages_slider.setPaintLabels(true);
		
		// Labels
		b_label = new JLabel("Number of branches");
		b_label.setHorizontalAlignment(JLabel.CENTER);
		t_label = new JLabel("Number of trucks per branch");
		t_label.setHorizontalAlignment(JLabel.CENTER);
		p_label = new JLabel("Number of packages");
		p_label.setHorizontalAlignment(JLabel.CENTER);
		
		// Panel
		sliders_panel = new JPanel();
		sliders_panel.setLayout(new GridLayout(0,1));
		ok_canel_panel = new JPanel();
		ok_canel_panel.setLayout(new GridLayout(0,2));

		
		// Add buttons to buttons panel
		ok_canel_panel.add(ok);
		ok_canel_panel.add(cancel);
		
		
		// Add sliders to slider panel
		sliders_panel.add(b_label);
		sliders_panel.add(branch_slider);
		sliders_panel.add(t_label);
		sliders_panel.add(trucks_slider);
		sliders_panel.add(p_label);
		sliders_panel.add(packages_slider);
		
		// Add the panels to the dialog
		add(sliders_panel);
		add(ok_canel_panel,BorderLayout.SOUTH);
		this.setLocationRelativeTo(null);
		setVisible(true);
	}
	
	/**
	 * Manage the actions on dialog window
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// If user pressed "OK" save the details
		if(e.getSource().equals(ok))
		{
			// Save values
			branch_amount = branch_slider.getValue();
			trucks_amount = trucks_slider.getValue();
			packages_amount = packages_slider.getValue();
			
			// Set the system components
			if(Simulator.getMain_off()!=null)
			{
				Hub.setSerialBranchID(0);
				Branch.setSerialBranchID(-1);
				Package.setSerialId(1000);
				Truck.setTruckSerialId(2000);
				MainOffice.setClock(0);
				stopAll();
				Simulator.getMain_off().setAllThreads(new ArrayList<Thread>());
				Simulator.getMain_off().setLive(false);
			}
			
			// Create and set values for the main office of the system
			MainOffice.setMainInstance(null);
			MainOffice.setParamsForCtor(branch_amount, trucks_amount);
			Simulator.setMain_off(MainOffice.getInstance());
			Simulator.getMain_off().setNum_of_packs(packages_amount);
			Simulator.setSuspended(false);
			Simulator.getSimuPanel().repaint(); //remove later
			dispose();
		}
		
		// If user pressed on "CANCEL"
		if(e.getSource().equals(cancel))
			dispose();
	}

	/**
	 * @return number of branches
	 */
	public int getBranch_amount() {
		return branch_amount;
	}

	/**
	 * @return number of trucks for each branch
	 */
	public int getTrucks_amount() {
		return trucks_amount;
	}

	/**
	 * @return number of packages
	 */
	public int getPackages_amount() {
		return packages_amount;
	}
	
	/**
	 * Suspend the system
	 */
	public void stopAll()
	{
		Simulator.setSuspended(true);
	}
}
