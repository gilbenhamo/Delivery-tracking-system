package components;
/**
 * A record in the package transfer history
 * @version 3.0, 14/6/2021
 * @author Itzik Rahamim - 312202351
 * @author Gil Ben Hamo - 315744557
 */
public class Tracking implements Cloneable{
	private int time;
	private Node node;
	private Status status;
	
	/**
	 * Constructs and initializes a Tracking with values
	 * <br>Example: Tracking(1,new Branch(), Status.COLLECTION)
	 * @param time - Current time
	 * @param node - Current location of the package
	 * @param status - Current status of the package
	 */
	public Tracking(int time, Node node, Status status) {
		this.time = time;
		this.node = node;
		this.status = status;
	}
	
	/**
	 * Access to time
	 * @return The time of this record
	 */
	public int getTime() {
		return time;
	}
	
	/**
	 * Access to node
	 * @return The location of the package in this record
	 */
	public Node getNode() {
		return node;
	}
	
	/**
	 * Access to status
	 * @return The status of the package in this record
	 */
	public Status getStatus() {
		return status;
	}
	
	/**
	 * Change the time of this record 
	 * @param time The time of this record
	 */
	public void setTime(int time) {
		this.time = time;
	}
	
	/**
	 * Change the location of this record
	 * @param node The location of the package in this record
	 */
	public void setNode(Node node) {
		this.node = node;
	}
	
	/**
	 * Change the status of this record
	 * @param status The status of the package in this record
	 */
	public void setStatus(Status status) {
		this.status = status;
	}
	
	@Override 
	public String toString() {
		if(node==null)
			return time + ": " + "Customer" + ", status=" + status;
		return time + ": " + node.getName() + ", status=" + status;
	}
	
	@Override 
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Tracking))
			return false;
		Tracking other = (Tracking) obj;
		if (node == null) {
			if (other.node != null)
				return false;
		} else if (!node.equals(other.node))
			return false;
		if (status != other.status)
			return false;
		if (time != other.time)
			return false;
		return true;
	}
	
	/**
	 * @return TrackString
	 */
	public String getTrackString() {
		if(node==null)
			return  " Time="+time+ ", Location="+ "Customer" + ", status= " + status;
		return " Time="+time+ ", Location="+ node.getName() + ", status= " + status;
	}
	
	public Tracking clone()
	{
		Tracking clone = null;
		try {
			clone = (Tracking)super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			e.printStackTrace();
		}
		
		return clone;
	}
}
