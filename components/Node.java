package components;
/**
 * Represents a location (vehicle or branch)
 * @version 1.0, 9/4/2021
 * @author Itzik Rahamim - 312202351
 * @author Gil Ben Hamo - 315744557
 */
public interface Node {
	
	/**
	 * Collect the package to the current node
	 * @param p - The package we want to collect
	 */
	 public void collectPackage(Package p); 
	 
	 /**
	  * Remove the package from the current node
	  * @param p - The package we want to deliver
	  */
	 public void deliverPackage(Package p);
	 
	 /**
	  * Current node makes 1 unit of his specific work
	  */
	 public void work();
	 
	 //HELP FUNCTIONS
	 
	 /**
	  * Get the name of the current node
	  * @return The name of this location
	  */
	 public String getName();  
	 
	 /**
	  * Take care to transfer the package and add a record to the package tracking
	  * @param p - The package
	  * @param source - Who delivers the package 
	  * @param dest - Who collects the package
	  * @param status - New status of the package
	  */
	 public default void handlePackage(Package p,Node source,Node dest, Status status)	
		{
			p.addTracking(dest, status);
			source.deliverPackage(p);
			dest.collectPackage(p);	
		} 
}
