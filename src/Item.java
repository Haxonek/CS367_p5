///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Main Class File:  TheGame.java
// File:             Item.java
// Semester:         CS367 Fall 2015
//
// Author:           Thomas Hansen thansen8@wisc.edu
// CS Login:         thansen
// Lecturer's Name:  Jim Skrentny
// Lab Section:      none
//
//////////////////// PAIR PROGRAMMERS COMPLETE THIS SECTION ////////////////////
//
// Pair Partner:     N/A
// Email:            N/A
// CS Login:         N/A
// Lecturer's Name:  N/A
// Lab Section:      N/A
//
//////////////////// STUDENTS WHO GET HELP FROM OTHER THAN THEIR PARTNER //////
//                   fully acknowledge and credit all sources of help,
//                   other than Instructors and TAs.
//
// Persons:          none
//
// Online sources:   none
//////////////////////////// 80 columns wide //////////////////////////////////

/**
 * The Item class contains all the variables used to describe the items plus
 * some modifying methods (i.e. toString())
 * 
 * @author Thomas Hansen
 */
public class Item {
	// name of item
	private String	name;
	// description of item
	private String	description;
	// whether the item's activated
	private boolean activated;
	// the item message
	private String message;
	// true if the item may only be used once
	private boolean oneTimeUse;
	// The description for if the items in use/used
	private String usedString;

	/**
	 * The main initialization method for the Item class
	 * <br>
	 * Constructs an Item Object. The parameters message and on_useString are 
	 * as explained above. If activated is true, the item is active and has 
	 * been used. If oneTimeUse is true, the item can be used only once. Throw
	 * an IllegalArgumentException if parameters are missing or invalid.
	 * 
	 * @param name the name of the item
	 * @param description the item's description
	 * @param activated whether the items activated
	 * @param message the message describing the item
	 * @param oneTimeUse whether you many only use the item once
	 * @param usedString The message for if the item's in use
	 */
	public Item(String name, String description, boolean activated, 
			String message,boolean oneTimeUse, String usedString){
		if (name == null || description == null || message == null ||
				usedString == null) {
			throw new IllegalArgumentException();
		}
		// catches if the item has already been used; invalid
		if (oneTimeUse && activated) {
			throw new IllegalArgumentException();
		}
		// assign the variables
		this.name = name;
		this.description = description;
		this.activated = activated;
		this.message = message;
		this.oneTimeUse = oneTimeUse;
		this.usedString = usedString;
	}

	/**
	 * Getter method for the Name of the Item
	 * 
	 * @return the item name
	 */
	public String getName(){
		return name;
	}

	/**
	 * Getter method for the Description of the Item
	 * 
	 * @return the item description
	 */
	public String getDescription(){
		return description;
	}

	/**
	 * Checks if the item is activated. Returns true if it is, else return
	 * false.
	 * 
	 * @return if the item is activated
	 */
	public boolean activated(){
		return activated;
	}

	/**
	 * Returns the "message" that the item wants to send to the room. This is
	 * used in the notifyRoom() function in TheGame class
	 * 
	 * @return the Item message
	 */
	public String on_use(){
		return message;
	}

	/**
	 * Activates the object. This changes the activation status to true.
	 */
	public void activate(){
		activated = true;
	}

	/**
	 * Returns the "on_useString" for the Item. This is printed in the
	 * notifyRoom() function in TheGame class after an item has been used and
	 * is active.
	 * 
	 * @return the predetermined message if the item is in use
	 */
	public String on_useString(){
		return usedString;
	}

	/**
	 * Returns true if the item can only be used once. Else returns false.
	 * This is used in TheGame to remove single-time use items after they are
	 * used.
	 * 
	 * @return Whether the item may only be used once
	 */
	public boolean isOneTimeUse(){
		return oneTimeUse;
	}

	@Override
	//This returns a String consisting of the name and description of the Item
	//This has been done for you.
	//DO NOT MODIFY
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("Item Name: " + this.name);
		sb.append(System.getProperty("line.separator"));
		sb.append("Description: " + this.description);
		return sb.toString();
	}
}