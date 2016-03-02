///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Main Class File:  TheGame.java
// File:             Player.java
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
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;

/**
 * The Play contains all the variables used to describe the player plus some
 * modifying methods
 * 
 * @author Thomas Hansen
 */
public class Player {
	// player name
	private String name;
	// the magic sack held by the player that contains all his/her items
	private Set<Item> magicSack;
	//Do not add anymore private data members

	public Player(String name, Set<Item> startingItems){
		this.name = name;
		this.magicSack = startingItems;
	}

	public String getName(){
		return name;
	}

	//Returns a String consisting of the items in the sack
	//DO NOT MODIFY THIS METHOD
	public String printSack(){
		//neatly printed items in sack
		StringBuilder sb = new StringBuilder();
		sb.append("Scanning contents of your magic sack");
		sb.append(System.getProperty("line.separator"));
		for(Item itm : magicSack){
			sb.append(itm.getName());
			sb.append(System.getProperty("line.separator"));
		}
		return sb.toString();
	}

	/**
	 * Iterate through the sack, and find the items whose status is activated.
	 * This is used in TheGame class when a user enters a new room, so that all
	 * active items work in the new room.
	 * 
	 * @return a set of all active items
	 */
	public Set<Item> getActiveItems(){
		Set<Item> activeItems = new HashSet<Item>();
		Item tmp;
		Iterator<Item> itr = magicSack.iterator();
		while (itr.hasNext()) {
			tmp = itr.next();
			if (tmp.activated()) {
				activeItems.add(tmp);
			}
		}
		return activeItems;
	}

	/**
	 * Find the Item in the sack whose name is "itemName". Return the item if
	 * you find it, otherwise return null.
	 * 
	 * @param itemName
	 * @return
	 */
	public Item findItem(String itemName){
		Item tmp;
		Iterator<Item> itr = magicSack.iterator();
		while (itr.hasNext()) {
			tmp = itr.next();
			if (tmp.getName().equals(itemName)) {
				return tmp;
			}
		}
		return null;
	}

	/**
	 * Checks if the player has the "item" in his sack. Returns true if he
	 * does, otherwise returns false.
	 * 
	 * @param item
	 * @return
	 */
	public boolean hasItem(Item item){
		return null != this.findItem(item.getName());
	}

	/**
	 * Adds the "item" to the Player's sack. Duplicate items are not allowed.
	 * (Read the bullet above to see how to handle this.) Returns true if item
	 * successfully added, else returns false.
	 * 
	 * @param item
	 * @return
	 */
	public boolean addItem(Item item){
		if (item == null) {
			throw new IllegalArgumentException();
		}
		
		return magicSack.add(item);
	}

	public boolean removeItem(Item item){
		if (magicSack.remove(item)) {
			return true;
		}
		return false;
	}
}