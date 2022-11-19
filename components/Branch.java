package components;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Observable;

import gui.Simulator;
/**
 * Represents a local branch
 * @version 3.0, 14/6/2021
 * @author Itzik Rahamim - 312202351
 * @author Gil Ben Hamo - 315744557
 */
public class Branch extends Observable implements Runnable,Node,Cloneable {
	private static int serialBranchID = -1;	//CONSISTENTS NUMBER FOR branchId
	
	private int branchId;
	private String branchName;
	private ArrayList<Truck> listTrucks;
	private ArrayList<Package> listPackages;

	final private int X_CORD = 15;
	private int x_cord, y_cord;
	private Color branch_color;
	private boolean working = false;
	
	/**
	 * Constructs and initializes a NonStandardTruck by default
	 */
	public Branch()
	{
		this.branchId = serialBranchID++;
		this.branchName = "branch " + String.format("%d", branchId);
		this.listTrucks = new ArrayList<Truck>();
		this.listPackages = new ArrayList<Package>();
		System.out.println("Creating " + this.toString());
		branch_color = Color.CYAN;
		this.setX_cord(X_CORD);

	}
	
	/**
	 * @return Branch color
	 */
	public Color getBranch_color() {
		return branch_color;
	}

	/**
	 * Sets new branch color
	 * @param branch_color Branch color
	 */
	public void setBranch_color(Color branch_color) {
		this.branch_color = branch_color;
	}

	/**
	 * Constructs and initializes a NonStandardTruck with a value<br>
	 * Example: Branch("SCE")
	 * @param branchName - The name of the branch
	 */
	public Branch(String branchName) {
		this.branchName = new String(branchName);
		this.branchId = serialBranchID++;
		this.listTrucks = new ArrayList<Truck>();
		this.listPackages = new ArrayList<Package>();
		System.out.println("Creating " + this.toString());
	}
	
	@Override
	public String toString() {
		return "Branch " + branchId + ", branch name: " + branchName + ", packages: " 
				+ listPackages.size() + ", trucks: " + listTrucks.size();
	}
	
	/**
	 * @return X-Coordinate
	 */
	public int getX_cord() {
		return x_cord;
	}

	/**
	 * @return Y-Coordinate
	 */
	public int getY_cord() {
		return y_cord;
	}

	/**
	 * Sets new X-Coordinate
	 * @param x_cord X-Coordinate
	 */
	public void setX_cord(int x_cord) {
		this.x_cord = x_cord;
	}

	/**
	 * Sets new Y-Coordinate
	 * @param y_cord Y-Coordinate
	 */
	public void setY_cord(int y_cord) {
		this.y_cord = y_cord;
	}

	
	/**
	 * Get the branch id
	 * @return branchId
	 */
	public int getBranchId() {
		return branchId;
	}
	
	/**
	 * Get the branch name
	 * @return branchName
	 */
	public String getBranchName() {
		return branchName;
	}
	
	/**
	 * Get an access to the trucks list
	 * @return getListTrucks
	 */
	public ArrayList<Truck> getListTrucks() {
		return listTrucks;
	}
	
	/**
	 * Get an access to the packages list
	 * @return getListPackages
	 */
	public ArrayList<Package> getListPackages() {
		return listPackages;
	}
	
	/**
	 * Get the next serial number of branch id
	 * @return Current serialBranchID
	 */
	public static int getSerialBranchID() {
		return serialBranchID;
	}
	
	/**
	 * Change the branch id
	 * @param branchId The new branch id
	 */
	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}
	
	/**
	 * Change the branch name
	 * @param branchName The new branch name
	 */
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	
	/**
	 * Change the trucks list
	 * @param listTrucks The new trucks list
	 */
	public void setListTrucks(ArrayList<Truck> listTrucks) {
		this.listTrucks = listTrucks;
	}
	
	/**
	 * Change the packages list
	 * @param listPackages The new packages list
	 */
	public void setListPackages(ArrayList<Package> listPackages) {
		this.listPackages = listPackages;
	}
	
	/**
	 * Change the current serialID of branches
	 * @param serialBranchID int
	 */
	public static void setSerialBranchID(int serialBranchID) {
		Branch.serialBranchID = serialBranchID;
	}

	/**
	 * Collect the package to listPackages.
	 * @param p - The package we want to add to package list 
	 */
	@Override
	public void collectPackage(Package p) {
		if(!(this.listPackages.contains(p)))
		{
			this.listPackages.add(p);
			branch_color = Color.BLUE;
		}
		if(p.getStatus().equals(Status.COLLECTION) || p.getStatus().equals(Status.DELIVERY))
		{
			this.wakeUp();
			this.setWorking(true);	
			
		}
	}
	
	/**
	 * Remove the package from listPackages.
	 * <br>If package not exist in listPackages print massage.
	 */
	@Override
	public void deliverPackage(Package p) {
		if(listPackages.contains(p))
			listPackages.remove(p);
		else
			System.out.println("This branch do not contains this package");
		if(listPackages.size()==0)
		{
			branch_color = Color.CYAN;
		}
	}
	
	/**
	 * This function performs a work unit in each beat according to the following requirements:
	 * <ul>
	 * <li> For each package whose status is COLLECTION, if there is a vehicle available, he goes out to collect the package.</li>
	 * <li> For each package whose status is DELIVERY, if there is a vehicle available, he is sent to deliver the package.</li>
	 * </ul>
	 */
	@Override
	public void work() {
		ArrayList<Package> temp = new ArrayList<Package>(listPackages);
		for(Package p : temp)
		{
			//FOR ALL PACKAGES
			if(p.getStatus().equals(Status.COLLECTION))	//produce
			{	
				//Package is ready to collect
				Truck t = getAvailableTruck();
				if(t != null) 
				{
					//Got an available truck
					handlePackage(p, this, t, Status.COLLECTION);
					setChanged();
					notifyObservers(p.getLatestTraking());
					t.setTimeLeft((p.getSenderAddress().getStreet()%10 + 1)*10);
					t.setAvailable(false);
					setCollectCords(p, t);
					System.out.println(t.getName() + " is collecting package " + p.getPackageID() + ", time to arrive: " + t.getTimeLeft());
					t.wakeUp();	//consume
				}
			}
			else if(p.getStatus().equals(Status.DELIVERY))	//produce
			{	
				//Package is ready to deliver
				Truck t = getAvailableTruck();
				if(t != null) 
				{
					//Got an available truck
					handlePackage(p, this, t, Status.DISTRIBUTION);
					setChanged();
					notifyObservers(p.getLatestTraking());
					t.setTimeLeft((p.getDestinationAddress().getStreet()%10 + 1)*10);
					t.setAvailable(false);
					setDeliverCords(p,t);
					System.out.println(t.getName() + " is delivering package " + p.getPackageID() + ", time left: " + t.getTimeLeft());
					t.wakeUp();	//consume
				}
			}	
		}
		if(!needToWork())
			this.working=false;
	}
	
	@Override
	public String getName()
	{
		return "Branch "+ this.branchId;	
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Branch))
			return false;
		Branch other = (Branch) obj;
		if (branchId != other.branchId)
			return false;
		if (branchName == null) {
			if (other.branchName != null)
				return false;
		} else if (!branchName.equals(other.branchName))
			return false;
		if (listPackages == null) {
			if (other.listPackages != null)
				return false;
		} else if (!listPackages.equals(other.listPackages))
			return false;
		if (listTrucks == null) {
			if (other.listTrucks != null)
				return false;
		} else if (!listTrucks.equals(other.listTrucks))
			return false;
		return true;
	}
	
	/**
	 * Search for an available truck
	 * <br>If found return Truck, else return <b>null</b>
	 */
	private Truck getAvailableTruck()
	{
		for(Truck t : listTrucks)
			if(t.isAvailable())
				return t;
		return null;
	}
	
	/**
	 * Add truck to listTrucks and set BelongTo.
	 * <br>(If truck already exist on listTruck do nothing).
	 * @param truck A truck to add to the list
	 */
	public void addTruck(Truck truck)
	{
		if(!(this.listTrucks.contains(truck)))
		{
			this.listTrucks.add(truck);
			truck.setBelongTo(this);
		}
	}
	
	/**
	 * @return Center X-Coordinate of the branch
	 */
	public int getCenterX()
	{
		return x_cord+20; 
	}
	
	/**
	 * @return Bottom Y-Coordinate of the branch
	 */
	public int getBottomY()
	{
		return y_cord+30;
	}
	
	/**
	 * @return Left-Center X-Coordinate of the branch
	 */
	public int getEnterX()
	{
		return x_cord+40; 
	}
	
	/**
	 * @return Left-Center Y-Coordinate of the branch
	 */
	public int getEnterY()
	{
		return y_cord+15;
	}
	
	@Override
	public void run() {
		while(true)
		{
			while(Simulator.isSuspended() || !working )
			{
				try {
					synchronized(this){wait();}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			this.work();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Sets new coordinates for collection from source
	 * @param p Package
	 * @param t Truck
	 */
	private void setCollectCords(Package p, Truck t)
	{
		t.setX_cord(this.getCenterX()-16);
		t.setY_cord(this.getY_cord());
		t.setX_speed((p.getCenterX()-16- t.getX_cord())/t.getTimeLeft());//-8
		t.setY_speed((Package.getCollectY()-t.getY_cord())/t.getTimeLeft());
	}
	
	/**
	 * Sets new coordinates for deliver to destination
	 * @param p Package
	 * @param t Truck
	 */
	private void setDeliverCords(Package p, Truck t)
	{
		t.setX_cord(this.getCenterX()-16);
		t.setY_cord(this.getBottomY());
		t.setX_speed((p.getCenterX()-16- t.getX_cord())/t.getTimeLeft());//-8
		t.setY_speed((Package.getEndY()-t.getY_cord())/t.getTimeLeft());
	}
	
	/**
	 * Lets the branch start work
	 */
	public synchronized void wakeUp()
	{
		notify();
	}

	/**
	 * Check if branch is working right now
	 * @return true if branch is working
	 */
	public boolean isWorking() {
		return working;
	}

	/**
	 * Sets branch working True/False
	 * @param working is the branch working
	 */
	public void setWorking(boolean working) {
		this.working = working;
	}
	
	/**
	 * Check if branch is need to work
	 * @return True if branch needs to work
	 */
	public boolean needToWork()
	{
		for(Package p: listPackages)
			if(p.getStatus().equals(Status.COLLECTION) || p.getStatus().equals(Status.DELIVERY))
					return true;
		return false;
	}
	
	/**
	 * Returns Clone of the branch
	 */
	public Branch clone()
	{
		Branch clone = null;
		try {
			clone = (Branch)super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			e.printStackTrace();
		}
		
		return clone;
	}

	public void protoBranch()
	{
		int id = MainOffice.getHub().getBranches().size();
		setBranchName("branch "+id);
		setBranchId(id);
		setBranch_color(Color.cyan);
		setListPackages(new ArrayList<Package>());
		Truck save = listTrucks.get(0), v = null;
		listTrucks = new ArrayList<Truck>();
		for(int i=0;i<MainOffice.TRUCKS_NUM;i++)
		{
			v = save.clone();
			v.protoTruck();
			listTrucks.add(v);
			Thread t = new Thread(v);
			t.start();
		}
	}
	
	/**
	 * Returns a clean clone - all lists restore
	 * @return Clone of the branch
	 */
	public Branch mementoClone()
	{
		Branch clone = clone();
		ArrayList<Package> save = clone.listPackages;
		clone.listPackages = new ArrayList<Package>();
		for(Package p: save)
			clone.listPackages.add(p.mementoClone());
		ArrayList<Truck> truckSave = clone.listTrucks;
		clone.listTrucks = new ArrayList<Truck>();
		for(Truck t: truckSave)
			clone.listTrucks.add(t.mementoClone()); 
		return clone;
	}

	/**
	 * Copy values from another branch to this branch
	 * @param b Branch
	 */
	public void copyFrom(Branch b)
	{
		this.listPackages = new ArrayList<Package>();
		for(Package p : b.listPackages)
		{
			this.listPackages.add(p);  		
			MainOffice.getInstance().getPackages().add(p);	
		}

		for(int i=0;i<listTrucks.size();i++)
			this.listTrucks.get(i).copyFrom(b.getListTrucks().get(i));   		
		this.branch_color = b.branch_color;
		this.working = b.working;
	}
}
