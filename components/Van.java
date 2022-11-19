package components;

import java.awt.Color;

/**
 * Represents a vehicle that used for delivering small packages and standard packages
 * <ul>
 * <li>Transfer only one package per trip.</li>
 * <li>Collects only SmallPackage and StandardPackage.</li>
 * </ul>
 * @version 1.0, 9/4/2021
 * @author Itzik Rahamim - 312202351
 * @author Gil Ben Hamo - 315744557
 * @see Truck
 */
public class Van extends Truck{
	
	/**
	 * Constructs and initializes a Van by default<br>
	 * Random License plate and Model
	 */
	public Van()
	{
		super();
		super.setTruckColor(Color.BLUE);
		System.out.println("Creating " + toString());
	}
	
	/**
	 * Constructs and initializes a Van with values<br>
	 * Example: Van("123-45-678", "M3")
	 * @param licensePlate Vehicle ID number
	 * @param truckModel Vehicle model
	 */
	public Van(String licensePlate, String truckModel)
	{
		super(licensePlate,truckModel);
		System.out.println("Creating " + toString());
	}
	
	@Override
	public String toString()
	{
		return "Van [" + super.toString() + "]";
	}
	
	@Override
	public String getName() {
		return "Van " + super.getTruckID();
	}
	
	/**
	 * Performs work according to the purpose of the trip
	 * <ul>
	 * <li>If the vehicle is on its way, the clock shortens by a second</li>
	 * <li>When time left to arrive is 0 do the job according to the purpose of the trip:</li>
	 * </ul>
	 * <br>Options:
	 * <ul>
	 * <li>Deliver it at the branch.</li>
	 * <li>Deliver it to costumer.</li>
	 * </ul>
	 */
	public void work()
	{
		if(!this.isAvailable())
		{
			this.setX_cord(this.getX_cord()+this.getX_speed());			//move on line
			this.setY_cord(this.getY_cord()+this.getY_speed());
			this.setTimeLeft(this.getTimeLeft()-1);
			if(this.getTimeLeft() == 0)
			{
				//ARRIVED TO DESTINATION
				Package p = this.getPackages().get(0); 
				if(p.getStatus().equals(Status.COLLECTION))
				{	//Deliver the package from customer to the branch
					handlePackage(p, this, getBelongTo(), Status.BRANCH_STORAGE);
					setChanged();
					notifyObservers(p.getLatestTraking());
					System.out.println(this.getName() + 
							" has collected package " + p.getPackageID() + 
							" and arrived back to branch " + p.getSenderAddress().getZip());
				}
				else if(p.getStatus().equals(Status.DISTRIBUTION))
				{	//Deliver the package from the branch to the customer
					this.deliverPackage(p);
					p.addTracking(null, Status.DELIVERED);
					setChanged();
					notifyObservers(p.getLatestTraking());
					System.out.println(this.getName() + 
							" has delivered package " + p.getPackageID() + " to the destination ");
					//Print if confirmation has Sent
					if(p instanceof SmallPackage && ((SmallPackage) p).isAcknowledge()) 
						System.out.println("Delivery confirmation has been sent");
				}
				this.setAvailable(true);
			}
		}
		
	}
	
	/**
	 * Adding the package to the packages list<br>
	 * Can only add a SmallPackage or StandardPackage
	 * Only one Package each time
	 */
	@Override
	public void collectPackage(Package p) {
		if(this.getPackages().size()==0 && !(this.getPackages().contains(p)))
		{
			if(p instanceof SmallPackage || p instanceof StandardPackage)
				this.getPackages().add(p);
			else
				System.out.println("Van cannot take a NonStandardPackage!");			
		}
		else
			System.out.println("Van allready contain a Package!");	
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof Van))
			return false;
		return true;
	}
	
	public Van mementoClone()
	{
		Van p = (Van)super.mementoClone();
		return p;
	}
}
