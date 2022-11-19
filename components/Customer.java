package components;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;
import java.util.Scanner;

import gui.Simulator;

/**
 * Represents a customer that creates packages to deliver
 * @version 3.0, 14/6/2021
 * @author Itzik Rahamim - 312202351
 * @author Gil Ben Hamo - 315744557
 */
public class Customer extends Observable implements Node,Runnable,Cloneable { 
	
	private static int customerIdCounter=1;
	static int finishedCustomerCounter=0;
	private static final int packsPerCustomer = 5;
	
	private int customerId; 
	private int packsCounter = 0;
	private Address address;
	private ArrayList<Package> packages;
	private ArrayList<String> packsID;
	private String[] trackingStrings;
	private boolean live = true;

	
	private static int num_of_packs; 
	private static int pack_x_cord = 60;
	private static int pack_spaces;

	/**
	 * Constructs and initializes a customer by default
	 */
	public Customer()
	{
		customerId= customerIdCounter++;
		packages = new ArrayList<Package>();
		packsID = new ArrayList<String>();
		Random rand = new Random();
		address =  createRandomAddress();
		num_of_packs = MainOffice.CUSTOMER_NUM * packsPerCustomer;
		pack_spaces =  0; 
	}

	
	/**
	 * A package lottery (random type and values)<br>
	 * Creates it and associates it with the appropriate branch.
	 */
	public  void addPackage()
	{
		Hub hub = MainOffice.getHub();
		if(packages.size()==0)
			pack_x_cord += pack_spaces;
		Package newPack = createRandomPackage();
		packsID.add(String.valueOf(newPack.getPackageID()));
		newPack.setX_cord(pack_x_cord);
		pack_x_cord+=21+pack_spaces;
		this.packages.add(newPack);
		MainOffice.getInstance().getPackages().add(newPack);
		setChanged();
		notifyObservers(newPack.getLatestTraking());
		newPack.setStatus(Status.COLLECTION);
		if(newPack instanceof NonStandardPackage)
			hub.collectPackage(newPack);
		else
		{
			int zip = newPack.getSenderAddress().getZip();
			for(int i=0;i<hub.getBranches().size();i++)
				if(hub.getBranches().get(i).getBranchId() == zip)
					hub.getBranches().get(i).collectPackage(newPack);
		}
	}
	
	
	
	/**
	 * Creates an address with random values
	 * @return Random address
	 */
	private Address createRandomAddress()
	{
		Random rand = new Random();
		return new Address(rand.nextInt(MainOffice.getHub().numOfBranches()),rand.nextInt(900000)+100000);
	}
	
	/**
	 * Creates random priority
	 * @return Random priority
	 */
	private Priority getRandomPririty()
	{
		Random rand = new Random();
		return Priority.values()[rand.nextInt(Priority.values().length)];
	}
	
	/**
	 * Creates package (random type and values)
	 * @return Random package
	 */
	private Package createRandomPackage()
	{
		Random rand = new Random();
		Package newPack;
		switch(rand.nextInt(3)) 			//random number to choose package type
		{
		case 0:
			newPack = new StandardPackage(
					getRandomPririty(),		// generate priority
					address,				//	sender address
					createRandomAddress(),	// create random destination address
					rand.nextDouble()*9+1);	// generate weight
			break;
		case 1:
			newPack = new NonStandardPackage(
					getRandomPririty(),
					address,				//	sender address
					createRandomAddress(),	// create random destination address
					rand.nextInt(500)+1,	// create random width
					rand.nextInt(1000)+1,	// create random length
					rand.nextInt(400)+1);	// create random height
			break;	
		default:
			newPack = new SmallPackage(
					getRandomPririty(),		// generate priority
					address,				//	sender address
					createRandomAddress(),  // create random destination address
					rand.nextBoolean()); 	// package acknowledge
			break;
		}
		return newPack;
	}
	
	@Override
	public void collectPackage(Package p) {}

	@Override
	public void deliverPackage(Package p) {}

	@Override
	public void work() {}

	@Override
	public String getName() {
		return "Customer " + customerId;
	}

	@Override
	public void run() {
		Random rand = new Random();
		while(live)
		{
			while(Simulator.isSuspended())
			{
				try {					
					synchronized (this) {wait();}	
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if(packages.size()<5)
			{
				synchronized (this) {
					this.addPackage();
				}
				try {Thread.sleep(rand.nextInt(3000)+2000);} 
				catch (InterruptedException e) {
					e.printStackTrace();
					}
			}
			else
			{
				readFromFile();
				try {Thread.sleep(10000);} 
				catch (InterruptedException e) {
					e.printStackTrace();
					}
			}
		}
		System.out.println("~~~~~~~~ All the packages of Customer " + this.customerId + " has been DELIVERED! ~~~~~~~~~~" );
		synchronized (this.getClass()) {
			finishedCustomerCounter++;
		}
		
	}
	
	/**
	 * Scan the file "tracking.txt" into "trackingStrings"
	 */
	private void readFromFile()
	{
		try {
			MainOffice.getRwlock().readLock().lock();
			File f = new File("tracking.txt");
			Scanner s = new Scanner(f);
			while(s.hasNextLine())
			{
				trackingStrings = s.nextLine().split(" ");
				if(trackingStrings[trackingStrings.length-1].equals("DELIVERED") && packsID.contains(trackingStrings[2]))
					packsCounter++;
			}
			if(packsCounter==packsPerCustomer)
				live = false;
			else
				packsCounter=0;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			MainOffice.getRwlock().readLock().unlock();
		}
	}
	
	/**
	 * Waking up the tread
	 */
	public synchronized void wakeUp()
	{
		notify();
	}
	
	/**
	 * Creates a clone of this customer
	 */
	public Customer clone()
	{
		Customer clone = null;
		try {
			clone = (Customer)super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			e.printStackTrace();
		}
		
		return clone;
	}
	
	/**
	 * Creates a clean clone of customer (copy lists)
	 * @return Customer
	 */
	public Customer mementoClone()
	{
		Customer clone = clone();
		ArrayList<Package> save = clone.packages;
		clone.packages = new ArrayList<Package>();
		for(Package p: save)							//change maybe its not the same package 
			clone.packages.add(p.mementoClone());
		clone.packsID = (ArrayList<String>) this.packsID.clone();
		return clone;
	}
	
	/**
	 * Copy values from another customer to this customer
	 * @param cust costumer
	 */
	public void copyFrom(Customer cust)
	{
		this.live = cust.live;
		this.packsID = cust.packsID;
		this.packsCounter = cust.packsCounter;
		this.packages.clear();
		for(Package p : cust.packages)
			this.packages.add(p);
		this.packsID = cust.packsID;
	}

	/**
	 * @return packages
	 */
	public ArrayList<Package> getPackages() {
		return packages;
	}

	/**
	 * Set a new packages list
	 * @param packages Packages list
	 */
	public void setPackages(ArrayList<Package> packages) {
		this.packages = packages;
	}

	/**
	 * @return customerIdCounter
	 */
	public static int getCustomerIdCounter() {
		return customerIdCounter;
	}

	/**
	 * Set a new customerIdCounter
	 * @param customerIdCounter 
	 */
	public static void setCustomerIdCounter(int customerIdCounter) {
		Customer.customerIdCounter = customerIdCounter;
	}

	/**
	 * @return finishedCustomerCounter
	 */
	public static int getFinishedCustomerCounter() {
		return finishedCustomerCounter;
	}

	/**
	 * Set a new finishedCustomerCounter
	 * @param finishedCustomerCounter 
	 */
	public static void setFinishedCustomerCounter(int finishedCustomerCounter) {
		Customer.finishedCustomerCounter = finishedCustomerCounter;
	}

	/**
	 * @return Package x coordinate
	 */
	public static int getPack_x_cord() {
		return pack_x_cord;
	}

	/**
	 * Set a new Package x coordinate
	 * @param pack_x_cord 
	 */
	public static void setPack_x_cord(int pack_x_cord) {
		Customer.pack_x_cord = pack_x_cord;
	}
}
