package components;
/**
 * Represents a package with non standard size<br>
 * A non standard package is a Package with a specific width, length and height
 * @version 3.0, 14/6/2021
 * @author Itzik Rahamim - 312202351
 * @author Gil Ben Hamo - 315744557
 * @see Package
 */
public class NonStandardPackage extends Package {
	private int width;
	private int length;
	private int height;
	
	/**
	 * Constructs and initializes a NonStandardPackage(priority, sender address, destination address, width, length, height)<br>
	 * Example: NonStandardPackage(Priority.LOW, new Address by values
	 * @param priority - The priority of the package
	 * @param senderAddress	- The address of the sender
	 * @param destinationAddress - The address of the destination
	 * @param width - The width of the package
	 * @param length - The length of the package
	 * @param height - The height of the package 
	 */
	public NonStandardPackage(Priority priority, Address senderAddress, Address destinationAddress, int width,
			int length, int height) {
		super(priority, senderAddress, destinationAddress);
		this.width = width;
		this.length = length;
		this.height = height;
		System.out.println("Creating " + this.toString());
	}
	@Override
	public String toString() {
		return "NonStandardPackage [" + super.toString() + ", width=" + width + ", length=" + length + ", height=" + height + "]";
	}
	
	/**
	 * Get the width of the package
	 * @return The width of the package
	 */
	public int getWidth() {
		return this.width;
	}
	
	/**
	 * Get the length of the package
	 * @return The length of the package
	 */
	public int getLength() {
		return this.length;
	}
	
	/**
	 * Get the height of the package
	 * @return The height of the package
	 */
	public int getHeight() {
		return this.height;
	}
	
	/**
	 * Change the width of the package
	 * @param width the width of the package
	 */
	public void setWidth(int width) {
		this.width = width;
	}
	
	/**
	 * Change the length of the package
	 * @param length the length of the package
	 */
	public void setLength(int length) {
		this.length = length;
	}
	
	/**
	 * Change the height of the package
	 * @param height the height of the package
	 */
	public void setHeight(int height) {
		this.height = height;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof NonStandardPackage))
			return false;
		NonStandardPackage other = (NonStandardPackage) obj;
		if (height != other.height)
			return false;
		if (length != other.length)
			return false;
		if (width != other.width)
			return false;
		return true;
	}
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "NonStandardPackage";
	}
	
	/**
	 * Creates a perfect clone (copy lists also)
	 */
	public NonStandardPackage mementoClone()
	{
		NonStandardPackage p = (NonStandardPackage)super.mementoClone();
		return p;
	}
}
