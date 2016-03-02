///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Main Class File:  TheGame.java
// File:             Room.java
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * The Room contains all the variables used to describe the room plus some
 * modifying methods
 * 
 * @author Thomas Hansen
 */
public class Room {
	//name of the room
	private	String	name;
	//description of the room
	private	String	description;
	//whether the room is lit or dark
	private	boolean	visibility;
	//whether the room is habitable
	private	boolean habitability;
	//reason for room not being habitable (only relevant when habitability is false)
	private String habitableMsg;
	//items in the room
	private	Set<Item> items;
	// message handlers
	private	List<MessageHandler> handlers;
	//locked rooms and the reason for their being locked
	private HashMap<Room, String> lockedPassages;	
	//Do not add anymore data members

	/**
	 * initializes the Room object
	 * 
	 * @param name The name of the room
	 * @param description the Room's description
	 * @param visibility whether the room is visible
	 * @param habitability whether the room is habitable
	 * @param habMsg The message about it's habitibility
	 * @param items the items in the room
	 * @param handlers The handlers in the room
	 */
	public Room(String name, String description, boolean visibility, boolean habitability,
			String habMsg, Set<Item> items, List<MessageHandler> handlers){
		if (name == null || items == null || handlers == null ||
				description == null) {
			throw new IllegalArgumentException();
		}
		this.name = name;
		this.description = description;
		this.visibility = visibility;
		this.habitability = habitability;
		this.habitableMsg = habMsg;
		this.items = items;
		this.handlers = handlers;
		// initialize empty lists
		lockedPassages = new HashMap<Room,String>();
	}

	/**
	 * returns the Room name
	 * 
	 * @return name of the room
	 */
	public String getName(){
		return name;
	}

	/**
	 * returns the visibility attribute
	 * 
	 * @return true if visible
	 */
	public boolean isVisible(){
		return visibility;
	}

	/**
	 * returns whether the Room is habitable
	 * 
	 * @return true if habitable
	 */
	public boolean isHabitable(){
		return habitability;
	}

	/**
	 * returns the message detailing the rooms habitability
	 * 
	 * @return the habitability message
	 */
	public String getHabitableMsg(){
		return habitableMsg;
	}

	/**
	 * returns a list of all the locked passages
	 * 
	 * @return A HashMap of the Rooms and their descriptions
	 */
	public HashMap<Room, String> getLockedPassages(){
		return lockedPassages;
	}

	/**
	 * Adds a (RoomName, ReasonWhyLocked) pair to the list of locked passages
	 * for a room.
	 * 
	 * @param passage The Room name
	 * @param whyLocked reasons why
	 */
	public void addLockedPassage(Room passage, String whyLocked){
		// catches null arguments
		if (passage == null || whyLocked == null) {
			throw new IllegalArgumentException();
		}
		lockedPassages.put(passage, whyLocked);
	}

	/**
	 * If it finds the Item "itemName" in the Room's items, it returns that
	 * Item. Otherwise it returns null 
	 * 
	 * @param itemName The name of the item
	 * @return The item if found
	 */
	public Item findItem(String itemName){
		// ensures a null value wasn't passed though
		if (itemName == null) {
			throw new IllegalArgumentException();
		}
		Iterator<Item> itr = items.iterator();
		Item tmp;
		while (itr.hasNext()) {
			tmp = itr.next();
			if (tmp.getName().equals(itemName)) {
				return tmp;
			}
		}
		// itemName has no equal/DNE
		return null;
	}

	/**
	 * Adds an the "item" to the Room's items. Duplicate items are not allowed.
	 * Java Sets do not allow duplicate items to be added. Hence we use a Set
	 * to store the items in the Room. Returns true if item is added, returns
	 * false otherwise
	 * 
	 * @param item The item being added
	 * @return true if it's successfully added
	 */
	public boolean addItem(Item item){
		if (item == null) {
			throw new IllegalArgumentException();
		}
		return items.add(item);
	}

	/**
	 * Removes the "item" from the Rooms Set of items. Returns true if item was
	 * removed, false otherwise.
	 * 
	 * @param item The item object to be removed
	 * @return true if the item is removed
	 */
	public boolean removeItem(Item item){
		if (item == null) {
			throw new IllegalArgumentException();
		}
		// removes item from items List
		return items.remove(item);
	}

	/***
	 * Receives messages from items used by the player and executes the 
	 * appropriate action stored in a message handler
	 * @param message is the "message" sent by the item
	 * @return null, this Room or Unlocked Room depending on MessageHandler
	 * DO NOT MODIFY THIS METHOD
	 */
	public Room receiveMessage(String message){
		Iterator<MessageHandler> itr = handlers.iterator();
		MessageHandler msg = null;
		while(itr.hasNext()){
			msg = itr.next();
			if(msg.getExpectedMessage().equalsIgnoreCase(message))
				break;
			msg = null;
		}
		if(msg == null)
			return null;
		switch(msg.getType()){
		case("visibility") :
			this.visibility = true;
		return this;
		case("habitability") :
			this.habitability = true;
		return this;
		case("room") :
			for(Room rm : this.lockedPassages.keySet()){
				if(rm.getName().equalsIgnoreCase(msg.getRoomName())){
					this.lockedPassages.remove(rm);
					return rm;
				}
			}
		default:
			return null;
		}
	}

	@Override
	//Returns a String consisting of the Rooms name, its description,
	//its items and locked rooms.
	// DO NOT MODIFY THIS METHOD
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("Welcome to the " + name + "!");
		sb.append(System.getProperty("line.separator"));
		if(!this.visibility){
			sb.append("Its too dark to see a thing!");
			sb.append(System.getProperty("line.separator"));
			sb.append("Places that can be reached from here :");
			sb.append(System.getProperty("line.separator"));
			for(Room rm :this.lockedPassages.keySet()){
				sb.append(rm.getName());
				sb.append(System.getProperty("line.separator"));
			}
			return sb.toString();
		}
		sb.append(description);
		sb.append(System.getProperty("line.separator"));
		if(this.items.size() >0){ 
			sb.append("Some things that stand out from the rest :");
			sb.append(System.getProperty("line.separator"));
		}
		Iterator<Item> itr = this.items.iterator();
		while(itr.hasNext()){
			sb.append(itr.next().getName());
			sb.append(System.getProperty("line.separator"));
		}
		sb.append("Places that can be reached from here :");
		sb.append(System.getProperty("line.separator"));
		for(Room rm :this.lockedPassages.keySet()){
			sb.append(rm.getName());
			sb.append(System.getProperty("line.separator"));
		}
		return sb.toString();
	}
}
