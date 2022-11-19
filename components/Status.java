package components;
/**
 * List of statuses corresponding to the delivery stages
 * @version 1.0, 9/4/2021
 * @author Itzik Rahamim - 312202351
 * @author Gil Ben Hamo - 315744557
 */
public enum Status {
	
	/**
	 * Package creation
	 */
	CREATION,
	
	/**
	 * Package collection from customer by Truck
	 */
	COLLECTION,
	
	/**
	 * Package is stored in the sender Branch
	 */
	BRANCH_STORAGE,
	
	/**
	 * Package on its way to HUB
	 */
	HUB_TRANSPORT,
	
	/**
	 * Package is stored in the HUB
	 */
	HUB_STORAGE, 
	
	/**
	 * Package on its way to destination Branch
	 */
	BRANCH_TRANSPORT, 
	
	/**
	 * Package is stored in the destination Branch and waiting for delivery
	 */
	DELIVERY, 
	
	/**
	 * deliver the Package to the customer by Truck
	 */
	DISTRIBUTION, 
	
	/**
	 * Package was delivered
	 */
	DELIVERED
}
