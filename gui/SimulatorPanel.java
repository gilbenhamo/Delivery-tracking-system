package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.util.Vector;
import javax.swing.JPanel;

import components.Branch;
import components.Hub;
import components.MainOffice;
import components.NonStandardPackage;
import components.Package;
import components.StandardTruck;
import components.Status;
import components.Truck;
/**
 * Manage the whole graphical system
 * @version 2.0, 8/5/2021
 * @author ItzikRahamim - 312202351
 * @author GilBenHamo - 315744557
 * @see JPanel
 */
public class SimulatorPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private static final Color HUB_COLOR = new Color(0X2A6500); //Hub color
	
	/**
	 * Constructs and initializes a the simulator panel by default<br>
	 */
	public SimulatorPanel()
	{
		this.setSize(1200,640);
		this.setLayout(null);
		this.setBackground(Color.WHITE);
	}

	/**
	 * Manages all on-screen drawings
	 * @param g Graphical tool
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		//Main office was created
		if(Simulator.getMain_off()!=null)
		{
			// Paint branches 
			paintBranches(g);
			
			// Paint hub
			g.setColor(HUB_COLOR);
			g.fillRect(MainOffice.getHub().getX_cord(), MainOffice.getHub().getY_cord(), 40, 200);		
			
			// Paint branches lines
			paintBranchesLines(g);
			
			// Paint packages
			paintPackages(g);
			
			// Paint StandardTruck and NonStandardTruck
			for(Truck t : MainOffice.getHub().getListTrucks())
				if (!t.isAvailable() )
					paintTruks(g,t);
			
			// Paint vans
			for(Branch b : MainOffice.getHub().getBranches())
				for(Truck t : b.getListTrucks())
					if (!t.isAvailable() )
						paintTruks(g,t);
		}   
	}
	
	/**
	 * Draw all the branches on the panel
	 * @param g Graphical tool
	 */
	private void paintBranches(Graphics g)
	{
		for(int i=0;i<(MainOffice.getHub().getBranches()).size();i++)
		{
			g.setColor(MainOffice.getHub().getBranches().get(i).getBranch_color());
			
			// Place the branch on the exactly y coordinate
			int y = MainOffice.getHub().getBranches().get(i).getY_cord();
			g.fillRect(15, y, 40 , 30);	
		}
	}
	/**
	 * Draw all the lines from branches to hub
	 * @param g Graphical tool
	 */
	private void paintBranchesLines(Graphics g)
	{
		Branch curr_branch;
		
		// Get x coordinate of the exit of standard trucks from hub
		int hub_x_cord = MainOffice.getHub().getX_cord();
		
		// Get all y coordinates of the exit of standard trucks from hub
		Vector<Integer> hub_y_exits =  MainOffice.getHub().getExitYPoints();	
		
		for(int i=0;i<(MainOffice.getHub().getBranches()).size();i++)
		{	// Draw the line for the relevant branch
			curr_branch = MainOffice.getHub().getBranches().get(i);
			g.setColor(HUB_COLOR);
			g.drawLine(curr_branch.getEnterX(), curr_branch.getEnterY(), hub_x_cord, hub_y_exits.get(i));
		}
	}
	
	/**
	 * Draw all the packages (source and destination). <br>
	 * Also draw all the lines from the branches and hub to the package.
	 * @param g Graphical tool
	 */
	private void paintPackages(Graphics g)
	{
		for(Package p : Simulator.getMain_off().getPackages())
		{
			// Draw source package (with right color)
			g.setColor(p.getStatus()==Status.COLLECTION ? Color.RED : Color.pink);
			g.fillOval(p.getX_cord(), Package.getStartY(), 30, 30);
			
			// Draw NonStandardPackages lines from hub to packages (source and destination).
			if(p instanceof NonStandardPackage)
			{
				Hub h = MainOffice.getHub();
				g.setColor(Color.red);
				// Hub to source
				g.drawLine(h.getCenterX(), h.getY_cord(), p.getCenterX(), Package.getCollectY());
				// Source to destination
				g.drawLine(p.getCenterX(), Package.getCollectY(), p.getCenterX(), Package.getEndY());
			}
			// Draw Van lines from branch to packages (source and destination).
			else
			{
				// SETUP
				Branch b = MainOffice.getHub().getBranchByZip(p.getSenderAddress().getZip());
				g.setColor(Color.BLUE);
				
				// Branch to source
				g.drawLine(b.getCenterX(), b.getY_cord(), p.getCenterX(), Package.getCollectY());
				
				// Branch to destination
				b = MainOffice.getHub().getBranchByZip(p.getDestinationAddress().getZip());
				g.drawLine(b.getCenterX(), b.getBottomY(), p.getCenterX(), Package.getEndY());
			}
			
			// Draw destination (with right color)
			g.setColor(p.getStatus()==Status.DELIVERED ? Color.RED : Color.pink);
			g.fillOval(p.getX_cord(),  Package.getEndY() , 30, 30);
		}
	}
	
	/**
	 * Draw all the trucks on the panel.
	 * @param g Graphical tool
	 * @param t Truck to draw
	 */
	private void paintTruks(Graphics g, Truck t)
	{
		Graphics2D g2D = (Graphics2D)g; // Use Grapichs2D tool to be able draw on DOUBLE coordinates
		
		// Draw track buddy
		Shape square = new Rectangle.Double(t.getX_cord()+8, t.getY_cord()-8, 16, 16);
		g2D.setColor(t.getTruckColor());
		g2D.fill(square);
		g2D.setColor(Color.black);
		
		// Draw 4 wheels for the truck
		Shape wheel = new Ellipse2D.Double(t.getX_cord()+8-4, t.getY_cord()-8-4, 10, 10);
		g2D.fill(wheel);
		wheel = new Ellipse2D.Double(t.getX_cord()+8+9, t.getY_cord()-8-4, 10, 10);
		g2D.fill(wheel);
		wheel = new Ellipse2D.Double(t.getX_cord()+8-4, t.getY_cord()-8+9, 10, 10);
		g2D.fill(wheel);
		wheel = new Ellipse2D.Double(t.getX_cord()+8+9, t.getY_cord()-8+9, 10, 10);
		g2D.fill(wheel);
		
		// Print number of packages that the truck carrying right now on top of StandardTrucks
		if(t instanceof StandardTruck)
		{
			int loaded = t.getPackages().size();
			g2D.drawString(loaded > 0 ? String.valueOf(loaded) : "", (float)t.getX_cord()+12, (float)t.getY_cord()-11);			
		}
	}
}
