package components;
/**
 * Represents address (of sender or recipient)
 * @version 1.0, 9/4/2021
 * @author Itzik Rahamim - 312202351
 * @author Gil Ben Hamo - 315744557
 */
public class Address {
	private int zip;
	private int street;
	
	/**
	 * Constructs and initializes a Address by values
	 * <br>Example: Address(3,123456)
	 * @param zip - The branch to which the address belongs
	 * @param street - 6 digit number that representing a street
	 */
	public Address(int zip, int street)
	{
		this.zip = zip;
		this.street = street;
	}
	
	/**
	 * Copy constructor - Constructs and initializes a Address by another Address
	 * @param other (Address object)
	 */
	public Address(Address other)
	{
		this.zip = other.zip;
		this.street = other.street;
	}
	
	/** 
	 * Get the address zip
	 * @return address zip
	 */
	public int getZip() {
		return zip;
	}
	
	/** 
	 * Get the address street
	 * @return Address street number
	 */
	public int getStreet() {
		return street;
	}
	
	/**
	 * Change the zip number of the address
	 * @param zip The new zip number
	 */
	public void setZip(int zip) {
		this.zip = zip;
	}
	
	/**
	 * Change the street number
	 * @param street The new street number
	 */
	public void setStreet(int street) {
		this.street = street;
	}
	
	@Override
	public String toString() {
		return zip + "-" + street;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Address))
			return false;
		Address other = (Address) obj;
		if (street != other.street)
			return false;
		if (zip != other.zip)
			return false;
		return true;
	}
}
