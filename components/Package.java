package components;
import java.util.ArrayList;
/**
 * Represents a package
 * @version 3.0, 14/6/2021
 * @author Itzik Rahamim - 312202351
 * @author Gil Ben Hamo - 315744557
 * @see Tracking
 */
public abstract class Package implements Cloneable {
	private static int serialId = 1000; //CONSISTENTS NUMBER FOR PackageID
	private final static int START_Y = 5;
	private final static int COLLECT_Y = 35;
	private final static int END_Y = 600;

	private int packageID;					 
	private Priority priority;
	private Status status;
	private Address senderAddress;
	private Address destinationAddress;
	private ArrayList<Tracking> tracking;
	private int x_cord;	

	/**
	 * Constructs and initializes a package by values<br>
	 * Example: Package(Priority.LOW, new Address(3,123456), new Address(4,123456))
	 * @param priority - The priority of the package
	 * @param senderAddress	- The address of the sender
	 * @param destinationAddress - The address of the destination
	 */
	public Package(Priority priority, Address senderAddress, Address destinationAddress) {
		this.packageID = serialId++;
		this.priority = priority;
		this.senderAddress = new Address(senderAddress);
		this.destinationAddress = new Address(destinationAddress);
		this.tracking = new ArrayList<Tracking>();
		addTracking(null,Status.CREATION);
	}
	
	/**
	 * Add a new Tracking to the package and change its status<br>
	 * Example: Package(new HUB(),Status.COLLECTION)
	 * @param node - New location
	 * @param status - New status
	 */
	public void addTracking(Node node,Status status)
	{
		tracking.add(new Tracking(MainOffice.getClock(),node,status)); 
		this.status = status;
	}
	
	/**
	 * Print all the tracking of the package on the console 
	 */
	public void printTracking()
	{
		for (Tracking x : tracking)
			System.out.println(x);
	}
	
	@Override
	public String toString() {
		return "packageID=" + packageID + ", priority=" + priority + ", status=" + status + ", senderAddress="
				+ senderAddress + ", destinationAddress=" + destinationAddress ;
	}

	/**
	 * Get the package ID  
	 * @return packageID
	 */
	public int getPackageID() {
		return packageID;
	}
	
	/**
	 * Get the priority of the package
	 * @return priority
	 */
	public Priority getPriority() {
		return priority;
	}
	
	/**
	 * Get the status of the package
	 * @return Status of the package
	 */
	public Status getStatus() {
		return status;
	}
	
	/**
	 * Get the sander address of the package
	 * @return senderAddress
	 */
	public Address getSenderAddress() {
		return senderAddress;
	}
	
	/**
	 * Get the destination of the package
	 * @return destinationAddress
	 */
	public Address getDestinationAddress() {
		return destinationAddress;
	}
	
	/**
	 * Get an access to the tracking list
	 * @return tracking list
	 */
	public ArrayList<Tracking> getTracking() {
		return tracking;
	}
	
	/**
	 * 
	 * @return packages current serialID
	 */
	public static int getSerialId() {
		return serialId;
	}

	/**
	 * Change the package ID
	 * @param packageID The package id
	 */
	public void setPackageID(int packageID) {
		this.packageID = packageID;
	}
	
	/**
	 * Change the priority
	 * @param priority Priority of the package
	 */
	public void setPriority(Priority priority) {
		this.priority = priority;
	}
	
	/**
	 * Change the status
	 * @param status The status of the package
	 */
	public void setStatus(Status status) {
		this.status = status;
	}
	
	/**
	 * Change sender address
	 * @param senderAddress The sander location
	 */
	public void setSenderAddress(Address senderAddress) {
		this.senderAddress = senderAddress;
	}
	
	/**
	 * Change destination address
	 * @param destinationAddress The destination location
	 */
	public void setDestinationAddress(Address destinationAddress) {
		this.destinationAddress = destinationAddress;
	}
	
	/**
	 * Change tracking list
	 * @param tracking The tracking list
	 */
	public void setTracking(ArrayList<Tracking> tracking) {
		this.tracking = tracking;
	}
	
	/**
	 * Change the current staring serialID
	 * @param serialId - int number
	 */
	public static void setSerialId(int serialId) {
		Package.serialId = serialId;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Package))
			return false;
		Package other = (Package) obj;
		if (destinationAddress == null) {
			if (other.destinationAddress != null)
				return false;
		} else if (!destinationAddress.equals(other.destinationAddress))
			return false;
		if (packageID != other.packageID)
			return false;
		if (priority != other.priority)
			return false;
		if (senderAddress == null) {
			if (other.senderAddress != null)
				return false;
		} else if (!senderAddress.equals(other.senderAddress))
			return false;
		if (status != other.status)
			return false;
		if (tracking == null) {
			if (other.tracking != null)
				return false;
		} else if (!tracking.equals(other.tracking))
			return false;
		return true;
	}
	
	/**
	 * @return Source entry Y-Coordinate
	 */
	public static int getStartY() {
		return START_Y;
	}

	/**
	 * @return Destination Y-Coordinate
	 */
	public static int getEndY() {
		return END_Y;
	}

	/**
	 * @return X-Coordinate of package
	 */
	public int getX_cord() {
		return x_cord;
	}

	/**
	 * Sets the X-Coordinate of package
	 * @param x_cord X-Coordinate of package
	 */
	public void setX_cord(int x_cord) {
		this.x_cord = x_cord;
	}
	
	/**
	 * @return Center X-Coordinate of package
	 */
	public int getCenterX() {
		return x_cord+15;
	}

	/**
	 * @return Y-Coordinate of collecting package from source
	 */
	public static int getCollectY() {
		return COLLECT_Y;
	}
	
	public abstract String getName();
	
	
	//return strign in format for write in file
	public String getLatestTraking()
	{
		return getName() +" "+ this.packageID + tracking.get(tracking.size()-1).getTrackString();
	}
	
	/**
	 * Returns a clone of this package
	 */
	public Package clone()
	{
		Package clone = null;
		try {
			clone = (Package)super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			e.printStackTrace();
		}
		
		return clone;
	}
	
	/**
	 * Create a perfect clone (copy lists also)
	 * @return package
	 */
	public Package mementoClone()
	{
		Package clone = clone();
		ArrayList<Tracking> save = clone.tracking;
		clone.tracking = new ArrayList<Tracking>();
		for (Tracking t : save)
			clone.tracking.add(t.clone());
		return clone;
	}
	
	/**
	 * Copy values from another package to this one
	 * @param p package
	 */
	public void copyFrom(Package p)
	{
		this.status = p.status;
		this.tracking.clear();
		for(Tracking t : p.tracking)
			this.tracking.add(t.clone());	//maybe change to t;
	}
}
