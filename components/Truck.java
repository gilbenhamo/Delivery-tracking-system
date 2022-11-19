package components;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;

import gui.Simulator;
/**
 * Represents a vehicle that used for delivering packages.
 * @version 3.0, 14/6/2021
 * @author Itzik Rahamim - 312202351
 * @author Gil Ben Hamo - 315744557
 */
public abstract class Truck extends Observable implements Runnable,Node,Cloneable{
	private static int truckSerialId = 2000;	//CONSISTENTS NUMBER FOR truckSerialId
	
	private int truckID;
	private String licensePlate;
	private String truckModel;
	private boolean available;
	private int timeLeft;
	private ArrayList<Package> packages;
	
	//ADDITIONAL FIELDS
	private Branch belongTo;		// Reference to which branch the Truck belong to
	private double x_cord,y_cord,x_speed, y_speed;  	// Current location and speed
	private Color truckColor;
	

	/**
	 * Constructs and initializes a Truck by default<br>
	 * Random License plate and Model
	 */
	public Truck()
	{
		this.truckID = truckSerialId++;
		this.licensePlate = createLicensePlate();
		this.truckModel = createModel();
		this.available = true;
		this.timeLeft = 0; 
		this.packages = new ArrayList<Package>();
	}
	/**
	 * Constructs and initializes a Truck with values<br>
	 * Example: StandardTruck("123-45-678", "M3")
	 * @param licensePlate Vehicle ID number
	 * @param truckModel Vehicle model
	 */
	public Truck(String licensePlate, String truckModel)
	{
		this.truckID = truckSerialId++;
		this.licensePlate = new String(licensePlate);
		this.truckModel = new String(truckModel);
		this.available = true;
		this.timeLeft = 0;
		this.packages = new ArrayList<Package>();
	}

	/**
	 * Get the Truck ID
	 * @return truckID
	 */
	public int getTruckID() {
		return truckID;
	}
	/**
	 * Get the the license plate
	 * @return licensePlate
	 */
	public String getLicensePlate() {
		return licensePlate;
	}
	/**
	 * Get the Truck model
	 * @return truckModel
	 */
	public String getTruckModel() {
		return truckModel;
	}
	/**
	 * Checking if the truck is available
	 * @return available
	 */
	public boolean isAvailable() {
		return available;
	}
	/**
	 * Get the time left to reach the destination
	 * @return timeLeft
	 */
	public int getTimeLeft() {
		return timeLeft;
	}
	/**
	 * Get access to the packages list
	 * @return packages 
	 */
	public ArrayList<Package> getPackages() {
		return packages;
	}
	/**
	 * Get access to the branch that the vehicle belong to
	 * @return The branch that the truck belong to
	 */
	public Branch getBelongTo() {
		return belongTo;
	}

	/**
	 * 
	 * @return current Trucks serialID starting number
	 */
	public static int getTruckSerialId() {
		return truckSerialId;
	}

	/**
	 * Change the Truck ID
	 * @param truckID New truck serial ID 
	 */
	public void setTruckID(int truckID) {
		this.truckID = truckID;
	}
	/**
	 * Change the license plate
	 * @param licensePlate New vehicle ID number
	 */
	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}
	/**
	 * Change the truck model
	 * @param truckModel New vehicle model
	 */
	public void setTruckModel(String truckModel) {
		this.truckModel = truckModel;
	}
	/**
	 * Change the availability of the vehicle
	 * @param available is the vehicle available
	 */
	public void setAvailable(boolean available) {
		this.available = available;
	}
	/**
	 * Update the time left to arrive
	 * @param timeLeft New time left to arrive the destination
	 */
	public void setTimeLeft(int timeLeft) {
		this.timeLeft = timeLeft;
	}
	/**
	 * Change the packages list
	 * @param packages New Packages list
	 */
	public void setPackages(ArrayList<Package> packages) {
		this.packages = packages;
	}
	/**
	 * Change the branch that the vehicle belong to
	 * @param belongTo New branch that the vehicle belong to
	 */
	public void setBelongTo(Branch belongTo) {
		this.belongTo = belongTo;
	}
	
	/**
	 * Change current serial id starting number
	 * @param truckSerialId - int number
	 */
	public static void setTruckSerialId(int truckSerialId) {
		Truck.truckSerialId = truckSerialId;
	}
	
	@Override 
	public String toString() {
		return "truckID=" + truckID + ", licensePlate=" + licensePlate + ", truckModel=" + truckModel
				+ ", available=" + available;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Truck))
			return false;
		Truck other = (Truck) obj;
		if (available != other.available)
			return false;
		if (licensePlate == null) {
			if (other.licensePlate != null)
				return false;
		} else if (!licensePlate.equals(other.licensePlate))
			return false;
		if (packages == null) {
			if (other.packages != null)
				return false;
		} else if (!packages.equals(other.packages))
			return false;
		if (timeLeft != other.timeLeft)
			return false;
		if (truckID != other.truckID)
			return false;
		if (truckModel == null) {
			if (other.truckModel != null)
				return false;
		} else if (!truckModel.equals(other.truckModel))
			return false;
		return true;
	}

	/**
	 * Remove the package from the truck<br>
	 * Will print a massage if the package is not exists on packages list
	 */
	@Override
	public void deliverPackage(Package p) {
		if(this.getPackages().contains(p))
			this.getPackages().remove(p);
		else
			System.out.println("The van don't contain any packages");
	}
	
	//Additional methods// 
	/**
	 * Create a random Model<br>
	 * @return The model that was created
	 */
	private String createModel()
	{
		String model = "M";
		Random r = new Random();
		model += (char)(r.nextInt(5)+'0');
		return model;
	}
	
	/**
	 * Create a random LicensePlate
	 * @return the license plate that was created
	 */
	private String createLicensePlate()
	{
		String newLicensePlate = "";
		Random r = new Random();
		newLicensePlate += String.format("%d",r.nextInt(900)+100)+
				"-" +String.format("%d",r.nextInt(90)+10)+
				"-" + String.format("%d",r.nextInt(900)+100);
		return newLicensePlate;
	}
	
	/**
	 * Adding a package to the packages list of the vehicle<br>
	 * There are no duplicate packages on the list
	 * @param pack A package to add to the packages list
	 */
	public void addPackage(Package pack)
	{
		if(!(this.packages.contains(pack)))
			(this.packages).add(pack);
	}
	
	/**
	 * @return X Coordinate
	 */
	public double getX_cord() {
		return x_cord;
	}
	
	/**
	 * @return Y Coordinate
	 */
	public double getY_cord() {
		return y_cord;
	}
	
	/**
	 * @return X speed
	 */
	public double getX_speed() {
		return x_speed;
	}
	
	/**
	 * @return Y speed
	 */
	public double getY_speed() {
		return y_speed;
	}
	
	/**
	 * Sets new X-Coordinate
	 * @param x_cord X-Coordinate
	 */
	public void setX_cord(double x_cord) {
		this.x_cord = x_cord;
	}
	
	/**
	 * Sets new Y-Coordinate
	 * @param y_cord Y-Coordinate
	 */
	public void setY_cord(double y_cord) {
		this.y_cord = y_cord;
	}
	
	/**
	 * Sets new X-Speed
	 * @param x_speed X-Speed
	 */
	public void setX_speed(double x_speed) {
		this.x_speed = x_speed;
	}
	
	/**
	 * Sets new Y-Speed
	 * @param y_speed Y-Speed
	 */
	public void setY_speed(double y_speed) {
		this.y_speed = y_speed;
	}
	
	/**
	 * Sets trip data to hub, initialize x and y speed
	 * @param exitPoint The exit point of hub
	 */
	public void setTripToHub(int exitPoint)
	{
		Hub h = MainOffice.getHub();
		this.setX_speed((h.getX_cord() - this.getX_cord())/this.getTimeLeft());
		this.setY_speed((h.getExitYPoints().get(exitPoint) -this.getY_cord())/this.getTimeLeft());
	}
	
	/**
	 * @return Truck color
	 */
	public Color getTruckColor() {
		return truckColor;
	}
	
	/**
	 * Sets new truck color
	 * @param truckColor Truck color
	 */
	public void setTruckColor(Color truckColor) {
		this.truckColor = truckColor;
	}
	
	/**
	 * Lets the truck start work again
	 */
	public synchronized void wakeUp()
	{
		notify();
	}
	
	@Override
	public void run() {
		while(true)
		{
			//System.out.println(this.getName());
			while(this.getTimeLeft() == 0 || Simulator.isSuspended())
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
	
	
	
	public Truck clone()
	{
		Truck clone = null;
		try {
			clone = (Truck)super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			e.printStackTrace();
		}
		
		return clone;
	}
	
	
	public void protoTruck()
	{
		this.truckID = truckSerialId++;
		this.licensePlate = createLicensePlate();
		this.available = true;
		this.timeLeft = 0; 
		this.packages = new ArrayList<Package>();
	}
	
	/**
	 * Creates a perfect clone (copy lists also)
	 * @return Truck
	 */
	public Truck mementoClone()
	{
		Truck clone = clone();
		ArrayList<Package> save = clone.packages;
		clone.packages = new ArrayList<Package>();
		for(Package p: save)
			clone.packages.add(p.mementoClone());
		return clone;
	}
	
	/**
	 * Copy values from another Truck to this one
	 * @param t Truck
	 */
	public void copyFrom(Truck t)
	{
		this.available = t.available;
		this.timeLeft = t.timeLeft;
		this.packages.clear();
		for(Package p : t.packages)
		{
			this.packages.add(p);  
			MainOffice.getInstance().getPackages().add(p);
		}
		this.truckColor = t.truckColor;
		this.x_cord = t.x_cord;
		this.y_cord = t.y_cord;
		this.x_speed = t.x_speed;
		this.y_speed = t.y_speed;
	}
}
