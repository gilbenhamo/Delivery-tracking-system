package components;

/**
 * Represents a small package<br>
 * A small package is a package that weighs only one unit
 * @version 3.0, 14/6/2021
 * @author Itzik Rahamim - 312202351
 * @author Gil Ben Hamo - 315744557
 * @see Package
 */
public class SmallPackage extends Package {
	private boolean acknowledge;	
	
	/**
	 * Constructs and initializes a SmallPackage with values<br>
	 * Example: SmallPackage(Priority.LOW, new Address(3,123456), new Address(4,123456), true)
	 * @param priority - The priority of the package
	 * @param senderAddress	- The address of the sender
	 * @param destinationAdress - The address of the destination
	 * @param acknowledge - If the package requires sending a confirmation of delivery after delivery to the recipient
	 */
	public SmallPackage(Priority priority, Address senderAddress, Address destinationAdress, boolean acknowledge)
	{
		super(priority,senderAddress,destinationAdress);
		this.acknowledge = acknowledge;
		System.out.println("Creating " + this.toString());
	}
	
	/**
	 * Check If the package requires sending a confirmation of 
	 * delivery after delivery to the recipient. 
	 * @return Message requirement.
	 */
	public boolean isAcknowledge() {
		return acknowledge;
	}
	
	/**
	 * Change the requirement to sand a message
	 * @param acknowledge Message requirement
	 */
	public void setAcknowledge(boolean acknowledge) {
		this.acknowledge = acknowledge;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof SmallPackage))
			return false;
		SmallPackage other = (SmallPackage) obj;
		if (acknowledge != other.acknowledge)
			return false;
		return true;
	}
	
	
	@Override
	public String toString() {
		return "SmallPackage [" + super.toString() + ", acknowledge=" + acknowledge + "]";
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "SmallPackage";
	}
	
	public SmallPackage mementoClone()
	{
		SmallPackage p = (SmallPackage)super.mementoClone();
		return p;
	}
	
}
