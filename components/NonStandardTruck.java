package components;
import java.awt.Color;
import java.util.Random;

import gui.Simulator;
/**
 * Represents a vehicle for transferring packages from the sorting center to branches and back
 * <br>Carry only NonStandardPackage
 * <br>Only one package per trip.
 * @version 3.0, 14/6/2021
 * @author Itzik Rahamim - 312202351
 * @author Gil Ben Hamo - 315744557
 * @see Truck
 */

public class NonStandardTruck extends Truck{
	private int width;
	private int length;
	private int height;
	
	/**
	 * Constructs and initializes a NonStandardTruck by default
	 * Random License plate, Model, and size
	 */
	public NonStandardTruck()
	{
		super();
		super.setTruckColor(Color.pink);
		Random rand = new Random();
		this.width = rand.nextInt(500)+250;			//RANDOM VALUES FOR FIELDS
		this.length = rand.nextInt(1000)+550;		//
		this.height = rand.nextInt(500)+250;		//
		System.out.println("Creating " + this.toString());
	}
	

	/**
	 * Constructs and initializes a NonStandardTruck by values<br>
	 * Example: StandardTruck("123-45-678", "M3", 500)
	 * @param licensePlate Vehicle ID number
	 * @param truckModel Vehicle model
	 * @param length Truck length
	 * @param width Truck width
	 * @param height Truck height
	 */
	public NonStandardTruck(String licensePlate, String truckModel, int length, int width, int height)
	{
		super(licensePlate,truckModel);
		this.width = width;
		this.length = length;
		this.height = height;
		System.out.println("Creating " + this.toString());
	}
	@Override
	public String toString() 
	{
		return "NonStandardTruck [" + super.toString() + ", width=" + width + ", length=" + length + ", height=" + height + "]";
	}
	@Override
	public String getName() 
	{
		return "NonStandardTruck " + super.getTruckID();
	}
	
	/**
	 * Collect the package to the vehicle.<br>
	 * Can not add if the size of the package is not 
	 * much to the vehicle.
	 * @param p The package we want to collect.
	 */
	@Override
	public void collectPackage(Package p) {
		if(this.getPackages().size()==0 && !(this.getPackages().contains(p)))
		{
			this.addPackage(p);		
		}
		else
			System.out.println("NonStandardTruck allready contain a Package!");
	}
	
	/**
	 * Collect package from sander customer and deliver it to the destination.
	 * <ul>
	 * <li>If the vehicle is on its way, the clock shortens by a second.</li>
	 * <li>When time left to arrive is 0 do the job according to the purpose of the trip.</li>
	 * </ul>
	 * Options:
	 * <ul>
	 * <li>Collect the package from the sander costumer and switched to distribution.</li>
	 * <li>Deliver the package to the destination.</li>
	 * </ul>
	 */
	public void work()
	{
		if(!this.isAvailable()&& !Simulator.isSuspended())
		{
			// Move on line
			this.setX_cord(this.getX_cord()+this.getX_speed());		
			this.setY_cord(this.getY_cord()+this.getY_speed());
			
			this.setTimeLeft(this.getTimeLeft()-1);
			if(this.getTimeLeft() == 0)
			{	
				//ARRIVED
				Package p = this.getPackages().get(0); 
				if(p.getStatus().equals(Status.COLLECTION))				
				{
					//Collect the package and start new trip to customer.
					p.addTracking(this, Status.DISTRIBUTION);
					setChanged();
					notifyObservers(p.getLatestTraking());
					
					//PRINTS & UPDATES
					System.out.println(this.getName() + " has collected package " + p.getPackageID());
					this.setTimeLeft( ((Math.abs(p.getSenderAddress().getStreet() - p.getDestinationAddress().getStreet()))%10 +1)*10 );
					setTripToCustomer(p);
					System.out.println(this.getName() + " is delivering package " + p.getPackageID() + ", time to arrive: " + this.getTimeLeft());
					this.setTruckColor(Color.red);
				}
				else if(p.getStatus().equals(Status.DISTRIBUTION))
				{	
					//Deliver the package to customer
					p.addTracking(null, Status.DELIVERED);
					setChanged();
					notifyObservers(p.getLatestTraking());
					this.deliverPackage(p);
					
					//PRINTS & UPDATES
					System.out.println(this.getName() + 
							" has delivered package " + p.getPackageID() + " to the destination ");
					this.setTruckColor(Color.pink);
					this.setAvailable(true);
					hubCallBack();
				}
			}
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof NonStandardTruck))
			return false;
		NonStandardTruck other = (NonStandardTruck) obj;
		if (height != other.height)
			return false;
		if (length != other.length)
			return false;
		if (width != other.width)
			return false;
		return true;
	}
	
	/**
	 * Get the width of the vehicle
	 * @return The width of the vehicle
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * Get the length of the vehicle
	 * @return The length of the vehicle
	 */
	public int getLength() {
		return length;
	}
	
	/**
	 * Get the height of the vehicle
	 * @return The height of the vehicle
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * Change the width of the vehicle
	 * @param width The width of the vehicle
	 */
	public void setWidth(int width) {
		this.width = width;
	}
	
	/**
	 * Change the length of the vehicle
	 * @param length The length of the vehicle
	 */
	public void setLength(int length) {
		this.length = length;
	}
	
	/**
	 * Change the height of the vehicle
	 * @param height The height of the vehicle
	 */
	public void setHeight(int height) {
		this.height = height;
	}
	
	/**
	 * Check if the vehicle can fit the package
	 * @param pack The package we want to check
	 * @return true if the package can fit else false
	 */
	public boolean isCanFit(Package pack)
	{
		if(pack instanceof NonStandardPackage)
		{
			
			if(
					((NonStandardPackage)pack).getHeight() <= this.getHeight() &&
					((NonStandardPackage)pack).getWidth() <= this.getWidth() && 
					((NonStandardPackage)pack).getLength() <= this.getLength()
				)
				return true;
		}
		return false;
	}
	
	/**
	 * Update the trip from source of package to the destination
	 * @param p Package to deliver
	 */
	public void setTripToCustomer(Package p)
	{
		this.setX_cord(p.getCenterX()-15);
		this.setY_cord(Package.getCollectY());
		this.setX_speed(0);
		this.setY_speed((Package.getEndY()-this.getY_cord())/this.getTimeLeft());
	}
	
	/**
	 * Waking up the HUB, and sets working attribute to true
	 */
	public void hubCallBack()
	{
		this.getBelongTo().wakeUp();
		this.getBelongTo().setWorking(true);
	}
	
	/**
	 * Creates a perfect clone (copy lists also)
	 */
	public NonStandardTruck mementoClone()
	{
		NonStandardTruck p = (NonStandardTruck)super.mementoClone();
		return p;
	}
}
