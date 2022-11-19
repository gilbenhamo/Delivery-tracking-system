package components;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Observable;
import java.util.Scanner;

/**
 * Represents a state to keep and return later
 * @version 3.0, 14/6/2021
 * @author Itzik Rahamim - 312202351
 * @author Gil Ben Hamo - 315744557
 */
public class State {
	MainOffice main;
	Hub hub;
	int clock;
	int line_counter;
	int truckSerialID;
	int packSerialID;
	int customerID; 
	int pack_x_cord;
	int finishedCustomers;
	int branchSerialID;
	StringBuffer saveTrackingsFile = new StringBuffer();
	
	/**
	 * Construct and initialize this state
	 */
	public State()
	{
		main = MainOffice.getInstance().mementoClone();
		hub = MainOffice.getHub().mementoClone();
		clock = MainOffice.getClock();
		line_counter = MainOffice.getLine_counter();
		truckSerialID = Truck.getTruckSerialId();
		packSerialID = Package.getSerialId();
		customerID = Customer.getCustomerIdCounter();
		pack_x_cord = Customer.getPack_x_cord();
		finishedCustomers = Customer.getFinishedCustomerCounter();
		branchSerialID = Branch.getSerialBranchID();
		readFromFile();
		
	}
	
	/**
	 * Reading the tracking from "tracking.txt"
	 */
	private void readFromFile()
	{
		try {
			MainOffice.getRwlock().readLock().lock();
			File f = new File("tracking.txt");
			Scanner s = new Scanner(f);
			while(s.hasNextLine())
				saveTrackingsFile.append(s.nextLine()+"\n");
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
}
