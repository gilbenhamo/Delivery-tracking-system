package components;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.swing.JTable;

import gui.Simulator;



/**
 * Manages the entire system, operates a clock, branches and vehicles, 
 * creates packages and transfers them to the appropriate branches.
 * @version 3.0, 14/6/2021
 * @author Itzik Rahamim - 312202351
 * @author Gil Ben Hamo - 315744557
 */
public class MainOffice implements Observer,Runnable, Cloneable{
	
	private static int clock;
	static int BRUNCH_NUM = 1, TRUCKS_NUM = 1;
	private static int line_counter = 1;
	private static Hub hub;
	private ArrayList<Package> packages;
	private ArrayList<Customer> customers;
	
	static int CUSTOMER_NUM = 10;
	private int num_of_packs;
	private int pack_x_cord = 100;
	private int pack_spaces;
	final int FRAME_SIZE = 600;
	final int X_CORD = 15;
	private int end_y;
	private int spaces,exit_spaces,current_cord;
	private boolean live = false;
	
	static final ReadWriteLock rwlock = new ReentrantReadWriteLock();
	FileWriter trackWriter = null;
	File trackFile;
	PrintWriter trackPW;
	
	ExecutorService e = Executors.newFixedThreadPool(2);
	
	private static volatile MainOffice mainInstance = null;
	
	private ArrayList<Thread> allThreads;
	/**
	 * Constructs and initializes a MainOffice by values<br>
	 * Example: MainOffice(6,3)
	 * @param branches A list of all the branches in the system.
	 * @param trucksForBranch - A list of all the trucks in the system.
	 */
	private MainOffice(int branches, int trucksForBranch)
	{
		MainOffice.clock = 0;
		spaces = (FRAME_SIZE - 30*(branches+2))/(branches+3); 		
		this.packages = new ArrayList<Package>();	// Save exit point from hub to each branch
		MainOffice.hub = new Hub();
		for(int i=0;i<trucksForBranch;i++)
			hub.addTruck(new StandardTruck());
		hub.addTruck(new NonStandardTruck());
		allThreads = new ArrayList<Thread>();

		System.out.println();
		initFileWriter();
		
		current_cord = spaces+20;	// Start index of branch
		exit_spaces = 200 / (branches+3);	
		end_y = MainOffice.getHub().getY_cord() + exit_spaces;
		for(int i=0;i<branches;i++)
		{
			hub.getExitYPoints().add(end_y);
			Branch newBranch = new Branch();
			newBranch.setX_cord(X_CORD);
			newBranch.setY_cord(current_cord);
			for(int j=0;j<trucksForBranch;j++)
				newBranch.addTruck(new Van());
			hub.addBranch(newBranch);
			System.out.println();
			current_cord += 30+spaces;
			end_y += exit_spaces;
		}
		
		customers = new ArrayList<Customer>();
		for(int i=0;i<CUSTOMER_NUM;i++)
			customers.add(new Customer());
		
	}
	
	/**
	 * Change the main office instance to another
	 * @param mainInstance
	 */
	public static void setMainInstance(MainOffice mainInstance) {
		MainOffice.mainInstance = mainInstance;
	}
	
	/**
	 * Sets the branches number and trucks number
	 * @param b Branches
	 * @param t Trucks
	 */
	public static void setParamsForCtor(int b, int t)
	{
		BRUNCH_NUM = b;
		TRUCKS_NUM = t;
	}
	
	/**
	 * @return Instance of the main office (singletone)
	 */
	public static MainOffice getInstance()
	{
		if(mainInstance==null)
			synchronized (MainOffice.class) {
				if(mainInstance == null)
					mainInstance = new MainOffice(BRUNCH_NUM, TRUCKS_NUM);
			}
		return mainInstance;
	}

	/**
	 * Initialize the file writer
	 */
	private void initFileWriter()
	{
		
		try {
			rwlock.writeLock().lock();
			trackFile = new File("tracking.txt");
			trackWriter = new FileWriter(trackFile);
			trackPW = new PrintWriter(trackWriter);
			trackPW.print("");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			if(trackPW != null)
				trackPW.close();
			rwlock.writeLock().unlock();
		}
	}

	/**
	 * Receives a number of beats that the system will perform, 
	 * and executes the tick() function several times.
	 * @param playTime Number of beats.
	 */
	public void play(int playTime)
	{
		System.out.println("\n======================= START ========================\n");
		for(int i=0;i<playTime;i++)
			this.tick();
		System.out.println("\n======================= STOP ========================\n");
		printReport();
	}
	
	/**
	 * Starts all the system Threads, and add main office observer to every branch and truck
	 */
	public void doStart()
	{
		for(Truck t : hub.getListTrucks())
		{
			t.addObserver(getInstance());
			allThreads.add(new Thread(t));		
		}
		allThreads.add(new Thread(hub));
		hub.addObserver(getInstance());
		for(Branch b : hub.getBranches())		
		{
			b.addObserver(getInstance());
			for(Truck t : b.getListTrucks())
			{
				allThreads.add(new Thread(t));		
				t.addObserver(getInstance());
			}
			allThreads.add(new Thread(b));					
		}
		for(Customer c: customers)
		{
			c.addObserver(getInstance());
			e.execute(new Thread(c));
		}
		for(Thread t : allThreads)
			t.start();
	}
	
	/**
	 * Prints a follow-up report for all packages in the system.
	 */
	public void printReport()
	{
		System.out.println("\n======================= STOP ========================\n");
		for(int i=0;i<this.packages.size();i++)
		{
			System.out.println("TRACKING " + this.packages.get(i));
			this.packages.get(i).printTracking();
			System.out.println();
		}
	}
	
	/**
	 * Get a string of the clock in this current moment.
	 * @return The value of the clock in MM: SS format.
	 */
	public String clockString()
	{
		if(clock<60)
			return String.format("%02d:%02d", 0, MainOffice.clock);
		return String.format("%02d:%02d", MainOffice.clock/60, (MainOffice.clock%60));
	}
	
	/**
	 * Activate one beat in the clock, in every beat the following actions are performed:
	 * <ul>
	 * <li> Printing the time and add 1 to the clock.</li>
	 * <li> All branches, sorting center and vehicles perform one work unit.</li>
	 * <li> Every 5 beats a random new package is created.</li>
	 * <li> At the end of the run, prints a message and all history for the created packages.</li>
	 * </ul>
	 */
	public void tick()
	{	
		if(!Simulator.isSuspended())
		{
			System.out.println(this.clockString());
		}
	}
	
	/**
	 * @return customers list
	 */
	public ArrayList<Customer> getCustomers()
	{
		return customers;
	}
	
	/**
	 * Get the current time
	 * @return The clock time
	 */
	public static int getClock() {
		return clock;
	}
	
	/**
	 * Get an access to the HUB
	 * @return HUB
	 */
	public static Hub getHub() {
		return hub;
	}
	
	/**
	 * Get an access to the packages list
	 * @return Packages list
	 */
	public ArrayList<Package> getPackages() {
		return packages;
	}
	
	/**
	 * Change the time
	 * @param ck The new time
	 */
	public static void setClock(int ck) {
		clock = ck;
	}
	
	/**
	 * Change the hub
	 * @param new_hub new HUB
	 */
	public static void setHub(Hub new_hub) {
		hub = new_hub;
	}
	
	/**
	 * Change the packages list
	 * @param packages new list of packages
	 */
	public void setPackages(ArrayList<Package> packages) {
		this.packages = packages;
	}

	@Override
	public String toString() {
		return "MainOffice [hub=" + hub + ", packages=" + packages + "]";
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof MainOffice))
			return false;
		MainOffice other = (MainOffice) obj;
		if (hub == null) {
			if (MainOffice.hub != null)
				return false;
		} else if (!hub.equals(MainOffice.hub))
			return false;
		if (packages == null) {
			if (other.packages != null)
				return false;
		} else if (!packages.equals(other.packages))
			return false;
		return true;
	}
	
	/**
	 * @return Number of packages on system
	 */
	public int getNum_of_packs() {
		return num_of_packs;
	}

	/**
	 * Sets the number of packages on the system
	 * @param num_of_packs Number of packages
	 */
	public void setNum_of_packs(int num_of_packs) {
		this.num_of_packs = num_of_packs;
	}

	@Override
	public void run() {
		while(live)
		{
			tick();
			Simulator.getSimuPanel().repaint();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(!Simulator.isSuspended())
				MainOffice.clock++;	
			if(CUSTOMER_NUM == Customer.finishedCustomerCounter)
				Simulator.setSuspended(true);
		}
	}
	
	/**
	 * Create all packages info table
	 * @return AllPackagesInfo table
	 */
	public JTable createAllPacksTable()
	{
		return createTabelByArray(this.getPackages());
	}
	
	/**
	 * Create a packages list of all packages that the specific branch working with
	 * @param id Branch id
	 * @return Packages list of packages that the specific branch working with
	 */
	public ArrayList<Package> getCurrentPacksByBranch(int id)
	{
		ArrayList<Package> temp = new ArrayList<Package>();
		for(Package p : this.getPackages())
		{	
			if(p.getSenderAddress().getZip() == id && !(p instanceof NonStandardPackage))
				temp.add(p);
		}
		return temp;
	}
	
	/**
	 * Create packages list of all NonStandardPackages that the hub working with
	 * @return Packages list of NonStandardPackages that the hub working with
	 */
	public ArrayList<Package> getCurrentHubPacks()
	{
		ArrayList<Package> temp = new ArrayList<Package>();
		for(Package p : this.getPackages())
		{
			if(p instanceof NonStandardPackage)
				temp.add(p);
		}
		return temp;
	}
	
	/**
	 * Create JTable of packages list data
	 * @param arr list of packages we want to show on table
	 * @return JTable of packages
	 */
	public JTable createTabelByArray(ArrayList<Package> arr)
	{
		String [] columns = {"Package ID", "Sender", "Destination", "Prority", "Status"};
		String[][] data = new String[arr.size()][5];
		for(int i=0;i<arr.size();i++)
		{
			data[i][0] =  String.valueOf(arr.get(i).getPackageID());
			data[i][1] =  String.valueOf(arr.get(i).getSenderAddress().getZip()+1)
					+ "-" + String.valueOf(arr.get(i).getSenderAddress().getStreet()) ;
			data[i][2] =  String.valueOf(arr.get(i).getDestinationAddress().getZip()+1)
					+ "-" + String.valueOf(arr.get(i).getDestinationAddress().getStreet());
			data[i][3] =  String.valueOf(arr.get(i).getPriority());
			data[i][4] =  String.valueOf(arr.get(i).getStatus());
		}
		return new JTable(data,columns); 	
	}

	/**
	 * @return All threads
	 */
	public ArrayList<Thread> getAllThreads() {
		return allThreads;
	}

	/**
	 * Sets all treads
	 * @param allThreads All treads
	 */
	public void setAllThreads(ArrayList<Thread> allThreads) {
		this.allThreads = allThreads;
	}
	
	/**
	 * Check if thread is alive
	 * @return True if thread is alive
	 */
	public boolean isLive() {
		return live;
	}

	/**
	 * Sets true/false on isAlive value
	 * @param live The thread is alive
	 */
	public void setLive(boolean live) {
		this.live = live;
	}
	
	/**
	 * @return end_y
	 */
	public int getEnd_y() {
		return end_y;
	}

	/**
	 * Set a new end_y
	 * @param end_y
	 */
	public void setEnd_y(int end_y) {
		this.end_y = end_y;
	}

	/**
	 * @return spaces
	 */
	public int getSpaces() {
		return spaces;
	}


	/**
	 * Set a new spaces value
	 * @param spaces
	 */
	public void setSpaces(int spaces) {
		this.spaces = spaces;
	}


	/**
	 * @return exit_spaces
	 */
	public int getExit_spaces() {
		return exit_spaces;
	}


	/**
	 * Sets a new exit_spaces
	 * @param exit_spaces
	 */
	public void setExit_spaces(int exit_spaces) {
		this.exit_spaces = exit_spaces;
	}

	/**
	 * @return current_cord
	 */
	public int getCurrent_cord() {
		return current_cord;
	}


	/**
	 * Set a new current_cord
	 * @param current_cord
	 */
	public void setCurrent_cord(int current_cord) {
		this.current_cord = current_cord;
	}

	@Override
	public void update(Observable o, Object arg) {
		try {
			rwlock.writeLock().lock();
			trackPW = new PrintWriter(new FileWriter("tracking.txt",true));
			trackPW.println(line_counter++ + ") " + (String)arg);
		} catch (IOException e) {
			System.out.println("ERROR with opening the file");
			e.printStackTrace();
		}
		finally
		{
			if(trackPW!=null)
				trackPW.close();
			rwlock.writeLock().unlock();
		}
	}
	
	/**
	 * @return Track PrintWriter
	 */
	public PrintWriter getPW()
	{
		return trackPW;
	}

	/**
	 * @return rwlock
	 */
	public static ReadWriteLock getRwlock() {
		return rwlock;
	}

	/**
	 * Set a new customers list
	 * @param customers
	 */
	public void setCustomers(ArrayList<Customer> customers) {
		this.customers = customers;
	}
	
	/**
	 * Returns a clone of the main office
	 */
	public MainOffice clone()
	{
		MainOffice clone = null;
		try {
			clone = (MainOffice)super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			e.printStackTrace();
		}
		
		return clone;
	}
	
	/**
	 * Returns a perfect clone of the main office
	 * @return MainOffice
	 */
	public MainOffice mementoClone()
	{
		MainOffice clone = clone();
		clone.packages = new ArrayList<Package>();
		for(Package p : packages)
		{
			if(p.getStatus().equals(Status.DELIVERED))
				clone.packages.add(p);
		}
		ArrayList<Customer> save = clone.customers;
		clone.customers = new ArrayList<Customer>();
		for(Customer c: save)
			clone.customers.add(c.mementoClone());	
		return clone;
	}
	
	/**
	 * Copy values from another main office to this one
	 * @param m main office
	 */
	public void copyFrom(MainOffice m)
	{
		this.packages.clear();
		for(Package p : m.packages)
			this.packages.add(p);
		for(int i=0;i<customers.size();i++)
			this.customers.get(i).copyFrom(m.customers.get(i));   		//maybe change to only t
		this.num_of_packs = m.num_of_packs;
		this.pack_x_cord  = m.pack_x_cord;
		this.pack_spaces = m.pack_spaces;
		this.end_y = m.end_y;
		this.spaces = m.spaces;
		this.exit_spaces = m.exit_spaces;
		this.live = m.live;
		this.current_cord = m.current_cord;
	
	}

	/**
	 * @return line_counter
	 */
	public static int getLine_counter() {
		return line_counter;
	}
	
	/**
	 * Set a new line_counter
	 * @param line_counter
	 */
	public static void setLine_counter(int line_counter) {
		MainOffice.line_counter = line_counter;
	}

	/**
	 * @return TRUCKS_NUM
	 */
	public static int getTRUCKS_NUM() {
		return TRUCKS_NUM;
	}

	/**
	 * Sets a new TRUCKS_NUM
	 * @param tRUCKS_NUM
	 */
	public static void setTRUCKS_NUM(int tRUCKS_NUM) {
		TRUCKS_NUM = tRUCKS_NUM;
	}



	public ExecutorService getE() {
		return e;
	}



	public void setE(ExecutorService e) {
		this.e = e;
	}	
}
