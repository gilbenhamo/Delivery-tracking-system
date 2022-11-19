package components;

import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;
/**
 * Represents a sorting center
 * @version 3.0, 14/6/2021
 * @author Itzik Rahamim - 312202351
 * @author Gil Ben Hamo - 315744557
 * @see Branch
 */
public class Hub extends Branch {
	private ArrayList<Branch> branches;
	//ADDITIONAL
	private int deliverIndex;
	private Vector<Integer> exitYPoints;
	
	/**
	 * Constructs and initializes a Hub by default
	 */
	public Hub()
	{
		super("HUB");
		this.setWorking(true);
		branches = new ArrayList<Branch>();
		deliverIndex=0;
		exitYPoints = new Vector<Integer>();
		super.setX_cord(1140);
		super.setY_cord(225);
	}
	
	@Override
	public String toString() {
		return super.toString();
		
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof Hub))
			return false;
		Hub other = (Hub) obj;
		if (branches == null) {
			if (other.branches != null)
				return false;
		} else if (!branches.equals(other.branches))
			return false;
		if (deliverIndex != other.deliverIndex)
			return false;
		return true;
	}
	
	/**
	 * Get an access to the branches list
	 * @return The branches list
	 */
	public ArrayList<Branch> getBranches() {
		return branches;
	}
	
	/**
	 * Get the current index of branch deliver order
	 * @return the current index of the next branch destination
	 */
	public int getDeliverIndex() {
		return deliverIndex;
	}
	
	/**
	 * Change the branches list
	 * @param branches The new branches list
	 */
	public void setBranches(ArrayList<Branch> branches) {
		this.branches = branches;
	}
	
	/**
	 * Change the current deliver index
	 * @param deliverIndex int
	 */
	public void setDeliverIndex(int deliverIndex) {
		this.deliverIndex = deliverIndex;
	}
	
	/**
	 * This function performs a work unit in each beat according to the following requirements:
	 * <ul>
	 * <li> Sends all available trucks to local branches in the order of the branch numbers.</li>
	 * <li> The truck will load all the packages waiting for it to be transferred to the branch to which it is traveling.</li>
	 * <li> If the non-standard truck is available, and there is a non-standard package in the sorting center that is waiting to </li>
	 * </ul>
	 * be collected and its dimensions fit the truck, the truck will be sent to collect the package.
	 */
	@Override
	public void work() {
		for(Truck t : this.getListTrucks())
		{
			ArrayList<Package> temp = new ArrayList<Package>(this.getListPackages());		//clone to prevent problem with iteration
			if(t.isAvailable())
			{
				if(t instanceof StandardTruck)
				{
					System.out.println(t.getName() + " loaded packages at " + this.getName());
					((StandardTruck) t).setDestination(getBranchByZip(deliverIndex));
					for(Package p : temp)
					{
						if(!(p instanceof NonStandardPackage))
								if(p.getDestinationAddress().getZip()==deliverIndex  && ((StandardTruck) t).isCanFit(p))
									{
									handlePackage(p, this, t, Status.BRANCH_TRANSPORT);	
									setChanged();
									notifyObservers(p.getLatestTraking());
									}
					}
					t.setAvailable(false);
					t.setTimeLeft((new Random().nextInt(10)+1)*10);
					System.out.println(t.getName() + " is on it's way to " +
							((StandardTruck)t).getDestination().getName() + ", time to arrive: " + t.getTimeLeft());
					setTripCords(deliverIndex, t);
					t.wakeUp();
					deliverIndex++;
					if(deliverIndex == this.branches.size())
						deliverIndex = 0;
				}
				else if(t instanceof NonStandardTruck)
				{
					for(Package p : temp)
					{
						if(p instanceof NonStandardPackage && t.isAvailable())
							{
								if(p.getStatus().equals(Status.COLLECTION))
								{
									handlePackage(p, this, t, Status.COLLECTION);
									setChanged();
									notifyObservers(p.getLatestTraking());
									t.setAvailable(false);
									t.setTimeLeft((new Random().nextInt(10)+1)*10);
									setNonStandardTrip(p,t);
									System.out.println(t.getName() + " is collecting package " + p.getPackageID() + ", time to arrive: " + t.getTimeLeft());
									t.wakeUp();
								}
							}
					}
				}
			}
		}
		this.setWorking(false);
	}
	
	@Override
	public String getName()
	{
		return "HUB";
	}
	
	/**
	 * Search for the right branch according to the zip
	 * @param zip The zip of the branch we want to find
	 * @return Branch or null
	 */
	public Branch getBranchByZip(int zip)
	{
		for(Branch b : this.getBranches())
			if(b.getBranchId() == zip)
				return b;
		System.out.println("There is no branch with this id!");
		return null;
	}
	
	/**
	 * Get the number of branches
	 * @return Number of branches
	 */
	public int numOfBranches()
	{
		return this.branches.size();
	}
	
	/**
	 * If branch is not exist on branches, add him
	 * @param branch The branch we want to add
	 */
	public void addBranch(Branch branch)
	{
		if(!(branches.contains(branch)))
		{
			branches.add(branch);
		}
	}

	/**
	 * @return list of exit y points from hub
	 */
	public Vector<Integer> getExitYPoints() {
		return exitYPoints;
	}

	/**
	 * Sets new list of exit y points from hub
	 * @param exitYPoints list of exit y points from hub
	 */
	public void setExitYPoints(Vector<Integer> exitYPoints) {
		this.exitYPoints = exitYPoints;
	}
	
	/**
	 * Sets the trip coordinates from source to destination
	 * @param exitPoint Specific Y exit point
	 * @param t Truck
	 */
	private void setTripCords(int exitPoint, Truck t)
	{
		Branch dest = this.getBranchByZip(exitPoint);
		t.setX_cord(this.getX_cord());
		t.setY_cord(this.exitYPoints.get(exitPoint));
		t.setX_speed((dest.getEnterX()- t.getX_cord())/t.getTimeLeft());
		t.setY_speed((dest.getEnterY()-t.getY_cord())/t.getTimeLeft());
	}
	
	/**
	 * Sets NonStandardTruck trip from source to destination
	 * @param p Package
	 * @param t Truck
	 */
	public void setNonStandardTrip(Package p, Truck t)
	{
		t.setX_cord(this.getCenterX()-16); //-15
		t.setY_cord(this.getY_cord());
		t.setX_speed((p.getCenterX()-16- t.getX_cord())/t.getTimeLeft());
		t.setY_speed((Package.getCollectY()-t.getY_cord())/t.getTimeLeft());
	}
	
	/**
	 * Checking if trucks needs to work
	 */
	public boolean needToWork()
	{
		for(Truck t:getListTrucks())
			if(t.getTimeLeft()==0)
				return true;
		return false;
	}
	
	/**
	 * Creates a perfect clone of this branch (copy lists also)
	 */
	public Hub mementoClone()
	{
		Hub clone = (Hub)super.mementoClone();
		ArrayList<Branch> save = clone.branches;
		clone.branches = new ArrayList<Branch>();
		for(Branch b: save)
			clone.branches.add(b.mementoClone());			
		return clone;
	}
	
	/**
	 * Copy all values from another hub to this hub
	 * @param h Hub
	 */
	public void copyFrom(Hub h)
	{
		super.copyFrom(h);
		branches.remove(branches.size()-1);				// Remove the last clone branch
		for(int i=0;i<branches.size();i++)
			this.branches.get(i).copyFrom(h.branches.get(i));   		
		exitYPoints.remove(exitYPoints.size()-1);
	}
}
