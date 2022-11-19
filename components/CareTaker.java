package components;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
/**
 * Represents a class that keeps all the mementos memory
 * @version 3.0, 14/6/2021
 * @author Itzik Rahamim - 312202351
 * @author Gil Ben Hamo - 315744557
 */
public class CareTaker {
	private ArrayList<Memento> stateList = new ArrayList<Memento>();
	
	/**
	 * Adding a memento to the list
	 * @param m Memento
	 */
	public void addMemento(Memento m)
	{
		stateList.add(m);
	}
	
	/**
	 * Returns the last memento of the list and remove it
	 * @return memento
	 */
	public Memento getMemento()
	{
		Memento m;
		if(stateList.size()>0) {
			m = stateList.get(stateList.size()-1);
			stateList.remove(stateList.size()-1);
			return m;
		}
		return null;	
	}
	
	/**
	 * Restore the system to the last memento
	 */
	public void restoreMemento()
	{
		Memento m = getMemento();
		if(m!=null)
		{
			MainOffice.getInstance().copyFrom(m.getState().main);
			MainOffice.getHub().copyFrom(m.getState().hub);
			MainOffice.setClock(m.getState().clock);
			MainOffice.setLine_counter(m.getState().line_counter);
			Truck.setTruckSerialId(m.getState().truckSerialID);
			Package.setSerialId(m.getState().packSerialID);
			Customer.setCustomerIdCounter(m.getState().customerID);
			Customer.setPack_x_cord(m.getState().pack_x_cord);
			Customer.setFinishedCustomerCounter(m.getState().finishedCustomers);
			Branch.setSerialBranchID(m.getState().branchSerialID);
			writeBackup(m);
		}
	}
	
	/**
	 * Returns the state list
	 * @return State list
	 */
	public ArrayList<Memento> getStateList() {
		return stateList;
	}

	/**
	 * Taking care about the "tracking.txt" file backup
	 * @param m memento
	 */
	public void writeBackup(Memento m) {

		PrintWriter PW = null;
		try {
			MainOffice.getRwlock().writeLock().lock();
			PW = new PrintWriter(new FileWriter("tracking.txt"));
			PW.print("");
			PW = new PrintWriter(new FileWriter("tracking.txt",true));
			PW.print(m.getState().saveTrackingsFile);
		} catch (IOException e) {
			System.out.println("ERROR with opening the file");
			e.printStackTrace();
		}
		finally
		{
			if(PW!=null)
				PW.close();
			MainOffice.getRwlock().writeLock().unlock();
		}
	}
}