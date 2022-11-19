package components;
/**
 * Represents a package with varying weights over one unit
 * @version 3.0, 14/6/2021
 * @author Itzik Rahamim - 312202351
 * @author Gil Ben Hamo - 315744557
 * @see Package
 */
public class StandardPackage extends Package {
	private double weight;
	
	/**
	 * Constructs and initializes a SmallPackage with values<br>
	 * Example: StandardPackage(Priority.LOW, new Address(3,123456), new Address(4,123456), 4.33)
	 * @param priority - The priority of the package
	 * @param senderAddress	- The address of the sender
	 * @param destinationAddress - The address of the destination
	 * @param weight - The weight of the package
	 */
	public StandardPackage(Priority priority, Address senderAddress, Address destinationAddress, double weight) {
		super(priority, senderAddress, destinationAddress);
		this.weight = weight;
		System.out.println("Creating " + this.toString());
	}
	
	@Override
	public String toString() {
		return "StandardPackage [" + super.toString() + ", weight=" + weight + "]";
	}
	
	/**
	 * Get the weight of the package
	 * @return The weight of the package
	 */
	public double getWeight() {
		return this.weight;
	}
	
	/**
	 * Change the weight of the package
	 * @param weight The weight of the package
	 */
	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof StandardPackage))
			return false;
		StandardPackage other = (StandardPackage) obj;
		if (Double.doubleToLongBits(weight) != Double.doubleToLongBits(other.weight))
			return false;
		return true;
	}
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "StandardPackage";
	}
	
	public StandardPackage mementoClone()
	{
		StandardPackage p = (StandardPackage)super.mementoClone();
		return p;
	}
}
