package components;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import gui.Simulator;
/**
 * Represents a vehicle that used for delivering small packages and standard packages
 * <ul>
 * <li>Transfer many packages per trip.</li>
 * <li>Collects only SmallPackage and StandardPackage.</li>
 * <li>Got a weight limit.</li></ul>
 * @version 3.0, 14/6/2021
 * @author Itzik Rahamim - 312202351
 * @author Gil Ben Hamo - 315744557
 * @see Truck
 */
public class StandardTruck extends Truck{
	private int maxWeight;
	private Branch destination;
	
	//ADDITIONAL
	private double loadedWeight;
	private final Color loadedColor = new Color(0,100,0);
	
	 /**
	 * Constructs and initializes a StandardTruck by default<br>
	 * Random License plate, Model and maxWeight
	 */
	public StandardTruck() {
		super();
		super.setTruckColor(Color.GREEN);
		this.maxWeight = new Random().nextInt(201)+100; 
		this.destination = null;
		this.loadedWeight = 0;
		System.out.println("Creating "+ this.toString());
	}
	
	/**
	 * Constructs and initializes a StandardTruck by values<br>
	 * Example: StandardTruck("123-45-678", "M3", 500)
	 * @param licensePlate Vehicle ID number
	 * @param truckModel Vehicle model
	 * @param maxWeight Maximum weight that the truck can carry
	 */
	public StandardTruck(String licensePlate,String truckModel,int maxWeight) {
		super(licensePlate,truckModel);
		this.maxWeight = maxWeight;
		this.destination = null;
		this.loadedWeight = 0;
		System.out.println("Creating "+ this.toString());
	}
	
	/**
	 * Performs work according to the purpose of the trip
	 * <ul>
	 * <li>If the vehicle is on its way, the clock shortens by a second</li>
	 * <li>When time left to arrive is 0 do the job according to the purpose of the trip:</li>
	 * </ul>
	 * <br>Options:
	 * <ul>
	 * <li>Deliver the packages to the HUB.</li>
	 * <li>Deliver the packages to their branch, and collect packages to deliver to HUB</li>
	 * <li>Trip ends when arriving at HUB</li>
	 * </ul>
	 */
	//here2
	@Override
	public void work()
	{
		if(!this.isAvailable() /*&& !Simulator.isSuspended()*/)
		{
			this.setX_cord(this.getX_cord()+this.getX_speed());			//move on line
			this.setY_cord(this.getY_cord()+this.getY_speed());
			this.setTimeLeft(this.getTimeLeft()-1);
			if(this.getTimeLeft() == 0)
				
			{	//ARRIVED
				ArrayList<Package> temp = new ArrayList<Package>(this.getPackages());		//copy to prevent errors in loop
				System.out.println(this.getName() + " has arrived to " + this.destination.getName());
				
				if(destination.getBranchId() == -1)		
				{	//DESTINATION IS HUB
					for(Package p : temp )
					{
						handlePackage(p,this,this.destination,Status.HUB_STORAGE);
						setChanged();
						notifyObservers(p.getLatestTraking());						
					}
					this.setAvailable(true);
					System.out.println(this.getName() + " unloaded packes at " + this.destination.getName());
					hubCallBack();
				}
				else 								
				{	//DESTINATION IS BRANCH
					//Deliver packages to the branch
					temp = new ArrayList<Package>(this.getPackages());//CLONE
					synchronized(destination)			//don't let another truck do work in same time
					{
						for(Package p : temp)
						{
							handlePackage(p,this,this.destination,Status.DELIVERY);	
							setChanged();
							notifyObservers(p.getLatestTraking());							
						}
						System.out.println(this.getName() + " unloaded packes at " + this.destination.getName());						
					}
					
					//Collect packages to transfer to HUB
					temp = new ArrayList<Package>(destination.getListPackages());//CLONE
					synchronized(destination)
					{
					for(Package p : temp)
						if(p.getStatus().equals(Status.BRANCH_STORAGE) && this.isCanFit(p))
							{
							handlePackage(p,this.destination,this,Status.HUB_TRANSPORT);
							setChanged();
							notifyObservers(p.getLatestTraking());
							}
					}
					
					//PRINTINGS & UPDATES
					System.out.println(this.getName() + " loaded packes at " + this.destination.getName());
					int b_id = destination.getBranchId();
					this.destination = this.getBelongTo();
					this.setTimeLeft((new Random().nextInt(6)+1)*10);
					this.setTripToHub(b_id); 	//Calculate the new speed and cords trip to hub
					System.out.println(this.getName() + " is on it's way to the " + this.destination.getName() + 
							", time to arrive:" + this.getTimeLeft());
				}
			}
		}
	}
	@Override
	public String toString() {
		if (destination == null)
			return "StandardTruck [" + super.toString() + ", maxWeight=" + maxWeight + "]";
		return "StandardTruck [" + super.toString() + ", maxWeight=" + maxWeight + ", destination=" + destination + "]";
	}

	
	/**
	 * Get the maximum weight that the vehicle can carry
	 * @return maxWeight
	 */
	public int getMaxWeight() {
		return maxWeight;
	}
	
	/**
	 * Get access to the destination node
	 * @return destination
	 */
	public Branch getDestination() {
		return destination;
	}
	
	/**
	 * Get the weight of all the packages that are on the vehicle
	 * @return loadedWeight
	 */
	public double getLoadedWeight() {
		return loadedWeight;
	}
	
	/**
	 * Change the maximum weight that the vehicle can carry
	 * @param maxWeight The maximum weight that the vehicle can carry
	 */
	public void setMaxWeight(int maxWeight) {
		this.maxWeight = maxWeight;
	}
	
	/**
	 * Change the destination of the vehicle
	 * @param destination The destination of the vehicle
	 */
	public void setDestination(Branch destination) {
		this.destination = destination;
	}
	
	/**
	 * Change the weight of all the packages that are on the vehicle
	 * @param loadedWeight Weight of all the packages that are on the vehicle
	 */
	public void setLoadedWeight(int loadedWeight) {
		this.loadedWeight = loadedWeight;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof StandardTruck))
			return false;
		StandardTruck other = (StandardTruck) obj;
		if (destination == null) {
			if (other.destination != null)
				return false;
		} else if (!destination.equals(other.destination))
			return false;
		if (maxWeight != other.maxWeight)
			return false;
		return true;
	}
	
	/**
	 * Check if the package can fit in the Truck
	 * in cases of over weight
	 * @param p Package object
	 * @return true if the package can fit
	 */
	public boolean isCanFit(Package p)
	{
		if(! (p instanceof NonStandardPackage))
		{
			double weight=0;
			if(p instanceof SmallPackage)
				weight=1.0;
			else if(p instanceof StandardPackage)
				weight=((StandardPackage)p).getWeight();
			if(weight <= this.maxWeight-this.loadedWeight)
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Collecting the package to the vehicle<br>
	 * -Can only collect small and standard packages<br>
	 * -Can not add a package if there is not enough space <br>
	 * @param p The package we want to collect
	 */
	@Override
	public void collectPackage(Package p) 
	{
		if(this.isCanFit(p) && !(this.getPackages().contains(p)))
		{
			if(this.getPackages().size()==0)
				setTruckColor(loadedColor);
			this.getPackages().add(p);
			this.loadedWeight += ((p instanceof SmallPackage) ? 1 : ((StandardPackage)p).getWeight());
		}

	}
	
	/**
	 * Remove the package from the vehicle
	 * @param p The package we want to remove
	 */
	@Override
	public void deliverPackage(Package p) {
		if(this.getPackages().contains(p))
		{
			this.getPackages().remove(p);
			if(p instanceof SmallPackage)
				loadedWeight -=1;
			else if (p instanceof StandardPackage)
				loadedWeight -= ((StandardPackage) p).getWeight();
			if(this.getPackages().size() == 0)
				setTruckColor(Color.green);
		}
		else
			System.out.println("The StnadardTruck don't contain any packages");
	}
	
	/**
	 * Waking up the branch, and sets working attribute to true
	 */
	public void hubCallBack()
	{
		this.getBelongTo().wakeUp();
		this.getBelongTo().setWorking(true);
	}

	@Override
	public String getName() {
		return "StandardTruck " + super.getTruckID();
	}
	
	@Override
	public void run() {
		while(true)
		{
			while(Simulator.isSuspended())
			{
				try {					
					synchronized (this) {wait();}	
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
	 * Creates a perfect clone (copy lists also)
	 */
	public StandardTruck mementoClone()
	{
		StandardTruck p = (StandardTruck)super.mementoClone();
		return p;
	}
	
	/**
	 * Copy values from another truck to this one
	 */
	public void copyFrom(Truck t)
	{
		super.copyFrom(t);
		this.destination = ((StandardTruck)t).destination;
		this.loadedWeight = ((StandardTruck)t).loadedWeight;
	}
}
