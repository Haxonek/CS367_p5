///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Title:            p5
// Files:            DirectedGraph.java GraphADT.java Item.java
//					 MessageHandler.java Player.java Room.java TheGame.java
// Semester:         CS367 Fall 2015
//
// Author:           Thomas Hansen
// Email:            thansen8@wisc.edu
// CS Login:         thansen
// Lecturer's Name:  Jim Skrentny
// Lab Section:      none
//
//////////////////// PAIR PROGRAMMERS COMPLETE THIS SECTION ////////////////////
//
//                   CHECK ASSIGNMENT PAGE TO see IF PAIR-PROGRAMMING IS ALLOWED
//                   If pair programming is allowed:
//                   1. Read PAIR-PROGRAMMING policy (in cs302 policy) 
//                   2. choose a partner wisely
//                   3. REGISTER THE TEAM BEFORE YOU WORK TOGETHER 
//                      a. one partner creates the team
//                      b. the other partner must join the team
//                   4. complete this section for each program file.
//
// Pair Partner:     N/A
// Email:            N/A
// CS Login:         N/A
// Lecturer's Name:  N/A
// Lab Section:      N/A
//
//////////////////// STUDENTS WHO GET HELP FROM OTHER THAN THEIR PARTNER //////
//                   must fully acknowledge and credit those sources of help.
//                   Instructors and TAs do not have to be credited here,
//                   but tutors, roommates, relatives, strangers, etc do.
//
// Persons:          N/A
//
// Online sources:   N/A
//////////////////////////// 80 columns wide //////////////////////////////////
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

/**
 * This is the main class for the game, which reads in a text file and compiles
 * and runs the majority of the interactive portion of the game
 * 
 * @author Thomas Hansen
 *
 */
public class TheGame {
	private static String gameIntro; // initial introduction to the game
	private static String winningMessage; //winning message of game
	private static String gameInfo; //additional game info
	private static boolean gameWon = false; //state of the game
	private static Scanner ioscanner = null; //for reading standard input
	private static Player player; //object for player of the game
	private static Room location; //current room in which player is located
	private static Room winningRoom; //Room which player must reach to win
	private static Item winningItem; //Item which player must find
	private static DirectedGraph<Room> layout; //Graph structure of the Rooms
	
	/**
	 * The main class for TheGame
	 * 
	 * @param args contains the Game details in a text file
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("Bad invocation! Correct usage: "
					+ "java AppStore <gameFile>");
			System.exit(1);
		}
		
		boolean didInitialize = initializeGame(args[0]);

		if (!didInitialize) {
			System.err.println("Failed to initialize the application!");
			System.exit(1);
		}

		System.out.println(gameIntro); // game intro

		processUserCommands();
	}

	/**
	 * intializes the Game files and reads everything
	 * 
	 * @param gameFile the txt file with the game details
	 * @return whether the game was propely compiled
	 */
	private static boolean initializeGame(String gameFile) {
		// initializes the layout object to DirectedGraph
		layout = new DirectedGraph<Room>();

		// try block to catch Scanner/File exception
		try {
			// reads player name
			System.out.println("Welcome worthy squire! What might be your name?");
			ioscanner = new Scanner(System.in);
			String playerName = ioscanner.nextLine();
			// initializes player
			player = new Player(playerName, new HashSet<Item>());
			// initialize temporary String
			String line = new String();

			Scanner file = new Scanner(new File(gameFile));
			// Standard initial game values
			if (file.hasNextLine()) gameIntro = file.nextLine();
			if (file.hasNextLine()) winningMessage = file.nextLine();
			if (file.hasNextLine()) gameInfo = file.nextLine();

			if (file.hasNextLine()) line = file.nextLine();
			if (line.contains("#player")) {
				if (file.hasNextLine()) line = file.nextLine();
				while (line.contains("#item")) {
					// Grabs the Item objects
					// Item name
					// Item description
					// Boolean denoting whether item is activated already
					// Item message
					// Boolean denoting if item is one-time-use
					// Message to print when item is used
					String name, description, message, messageToPrint;
					boolean activated, oneTimeUse;

					name = file.nextLine().trim();
					description = file.nextLine().trim();
					activated = file.nextLine().contains("true");
					message = file.nextLine().trim();
					oneTimeUse = file.nextLine().contains("true");
					messageToPrint = file.nextLine().trim();

					player.addItem(new Item(name, description, activated, message, oneTimeUse, messageToPrint));

					if (file.hasNextLine()) line = file.nextLine();
				}
			}
			// while loop reruns for each room
			while (line.contains("#room")) {
				// #room variables
				String roomName, roomDescription, habMsg = null;
				boolean isVisible, isHospitable, isWinningRoom;
				Set<Item> roomItems = new HashSet<Item>();
				List<MessageHandler> roomHandlers = new ArrayList<MessageHandler>();

				isWinningRoom = line.contains("#win");

				roomName = file.nextLine().trim();
				roomDescription = file.nextLine().trim();
				isVisible = file.nextLine().trim().equalsIgnoreCase("true");
				isHospitable = file.nextLine().trim().equalsIgnoreCase("true");
				// so if it's not habitable then then we grab the message after
				if (!isHospitable) {
					habMsg = file.nextLine().trim();
				}

				line = file.nextLine();
				while (line.contains("#item") || line.contains("#messageHandler")) {
					if (line.contains("#item")) {
						// An item inside the room
						String itemName, itemDescription;
						boolean activated, oneTimeUse;
						String itemMessage, itemUsed;

						itemName = file.nextLine().trim();
						itemDescription = file.nextLine().trim();
						activated = file.nextLine().contains("true");
						itemMessage = file.nextLine().trim();
						oneTimeUse = file.nextLine().contains("true");
						itemUsed = file.nextLine().trim();

						if (line.contains("#win")) {
							Item itm = new Item(itemName, itemDescription, activated, itemMessage, oneTimeUse, itemUsed);
							winningItem = itm;
							roomItems.add(itm);
						} else {
							roomItems.add(new Item(itemName, itemDescription, activated, itemMessage, oneTimeUse, itemUsed));
						}
						line = file.nextLine().trim();
					} else if (line.contains("#messageHandler")) {
						// A handler for the room
						String msg, type, hrn; // hnr = handler room name

						msg = file.nextLine().trim();
						type = file.nextLine().trim();
						line = new String(file.nextLine().trim());
						if (line.contains("#")) {
							hrn = roomName.trim();
						} else {
							hrn = line.trim();
							line = file.nextLine().trim();
						}
						// Adds each handler to each room as they're read
						roomHandlers.add(new MessageHandler(msg, type, hrn));
					}
				}
				Room curRoom = new Room(roomName, roomDescription, isVisible, isHospitable, habMsg, roomItems, roomHandlers);
				layout.addVertex(curRoom);
				// adds the first room on the list to the occupied room
				if (location == null) {
					location = curRoom;
				}
				// adds the winning room to the correct variable
				if (isWinningRoom) {
					winningRoom = curRoom;
				}
			}
			if (line.contains("#locked passages")) {
				Scanner names;
				while (!line.contains("#Adjacency List")) { // could be true...
					// Initialize the variables
					String fromName, lockedName, msg;
					Room lockedRoom = null, tmp;
					
					// reads first line, grabs room locations
					line = file.nextLine().trim();
					if (line.contains("#Adjacency List")) {
						break;
					}
					names = new Scanner(line);
					fromName = names.next().trim();
					lockedName = names.next().trim();
					
					// gets vertices, finds start and adds locked room to it
					Set<Room> passages = layout.getAllVertices();
					Iterator<Room> itr = passages.iterator();
					while (itr.hasNext()) {
						tmp = itr.next();
						if (tmp.getName().equals(lockedName)) {
							lockedRoom = tmp;
						}
					}
					
					// why it's locked, second line from the file
					msg = file.nextLine();
					
					itr = layout.getAllVertices().iterator();
					while (itr.hasNext()) {
						tmp = itr.next();
						if (tmp.getName().equals(fromName)) {
							// add to all neighbors????
							// so add to locked passages, remove from passages
							tmp.addLockedPassage(lockedRoom, msg);
							break;
						}
					}
					names.close();
				}
			}
			if (line.contains("#Adjacency List")) {
				// A B C == A => B && A => C
				// c == no edges
				while (file.hasNextLine()) {
					// creates a new string to hold the Verticy name
					String VFrom = new String("");
					// a stack to hold all the edges incase there is more
					// then one
					Stack<Room> edges = new Stack<Room>();
					Scanner cur = new Scanner(file.nextLine().trim());
					if (cur.hasNext()) VFrom = cur.next();
					while (cur.hasNext()) {
						edges.push(findRoom(cur.next()));
					}
					while (!edges.isEmpty()) {
						// Grabs each room that the first room points to
						Room tmp = edges.pop();
						layout.addEdge(findRoom(VFrom), tmp);
					}
					cur.close();
				}
			}
			file.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * reads the user commands primarily through a switch case block
	 * 
	 */
	private static void processUserCommands() {
		String command = null;
		do {

			System.out.print("\nPlease Enter a command ([H]elp):");
			command = ioscanner.next();
			switch (command.toLowerCase()) {
			case "p": // pick up
				processPickUp(ioscanner.nextLine().trim());
				goalStateReached();
				break;
			case "d": // put down item
				processPutDown(ioscanner.nextLine().trim());
				break;
			case "u": // use item
				processUse(ioscanner.nextLine().trim());
				break;
			case "lr":// look around
				processLookAround();
				break;
			case "lt":// look at
				processLookAt(ioscanner.nextLine().trim());
				break;
			case "ls":// look at sack
				System.out.println(player.printSack());
				break;
			case "g":// goto room
				processGoTo(ioscanner.nextLine().trim());
				goalStateReached();
				break;
			case "q":
				System.out.println("You Quit! You, " + player.getName() + ", are a loser!!");
				break;
			case "i":
				System.out.println(gameInfo);
				break;
			case "h":
				System.out
				.println("\nCommands are indicated in [], and may be followed by \n"+
						"any additional information which may be needed, indicated within <>.\n"
						+ "\t[p]  pick up item: <item name>\n"
						+ "\t[d]  put down item: <item name>\n"
						+ "\t[u]  use item: <item name>\n"
						+ "\t[lr] look around\n"
						+ "\t[lt] look at item: <item name>\n"
						+ "\t[ls] look in your magic sack\n"
						+ "\t[g]  go to: <destination name>\n"
						+ "\t[q]  quit\n"
						+ "\t[i]  game info\n");
				break;
			default:
				System.out.println("Unrecognized Command!");
				break;
			}
		} while (!command.equalsIgnoreCase("q") && !gameWon);
		ioscanner.close();
	}

	/**
	 * processes the command activated by 'lr'
	 */
	private static void processLookAround() {
		System.out.print(location.toString());
		for(Room rm : layout.getNeighbors(location)){
			System.out.println(rm.getName());
		}
	}

	/**
	 * processes the look at command
	 * 
	 * @param item the item being viewed
	 */
	private static void processLookAt(String item) {
		Item itm = player.findItem(item);
		if(itm == null){
			itm = location.findItem(item);
		}
		if(itm == null){
			System.out.println(item + " not found");
		}
		else
			System.out.println(itm.toString());
	}

	/**
	 * picks up an item
	 * 
	 * @param item the item being picked up
	 */
	private static void processPickUp(String item) {
		if(player.findItem(item) != null){
			System.out.println(item + " already in sack");
			return;
		}
		Item newItem = location.findItem(item);
		if(newItem == null){
			System.out.println("Could not find " + item);
			return;
		}
		player.addItem(newItem);
		location.removeItem(newItem);
		System.out.println("You picked up ");
		System.out.println(newItem.toString());
	}

	/**
	 * puts an item down
	 * 
	 * @param item the item being put down
	 */
	private static void processPutDown(String item) {
		if(player.findItem(item) == null){
			System.out.println(item + " not in sack");
			return;
		}
		Item newItem = player.findItem(item);
		location.addItem(newItem);
		player.removeItem(newItem);
		System.out.println("You put down " + item);
	}

	/**
	 * checks to see if the item exists and uses it
	 * 
	 * @param item the item being used
	 */
	private static void processUse(String item) {
		Item newItem = player.findItem(item);
		if(newItem == null){
			System.out.println("Your magic sack doesn't have a " + item);
			return;
		}
		if (newItem.activated()) {
			System.out.println(item + " already in use");
			return;
		}
		if(notifyRoom(newItem)){
			if (newItem.isOneTimeUse()) {
				player.removeItem(newItem);
			}
		}
	}

	/**
	 * processes the 'g' command for a room
	 * 
	 * @param destination The Room destination (err Vertex...)
	 */
	private static void processGoTo(String destination) {
		Room dest = findRoomInNeighbours(destination);
		if(dest == null) {
			for(Room rm : location.getLockedPassages().keySet()){
				if(rm.getName().equalsIgnoreCase(destination)){
					System.out.println(location.getLockedPassages().get(rm));
					return;
				}
			}
			System.out.println("Cannot go to " + destination + " from here");
			return;
		}
		Room prevLoc = location;
		location = dest;
		if(!player.getActiveItems().isEmpty())
			System.out.println("The following items are active:");
		for(Item itm:player.getActiveItems()){
			notifyRoom(itm);
		}
		if(!dest.isHabitable()){
			System.out.println("Thou shall not pass because");
			System.out.println(dest.getHabitableMsg());
			location = prevLoc;
			return;
		}

		System.out.println();
		processLookAround();
	}

	/**
	 * 
	 * 
	 * @param item
	 * @return
	 */
	private static boolean notifyRoom(Item item) {
		Room toUnlock = location.receiveMessage(item.on_use());
		if (toUnlock == null) {
			if(!item.activated())
				System.out.println("The " + item.getName() + " cannot be used here");
			return false;
		} else if (toUnlock == location) {
			System.out.println(item.getName() + ": " + item.on_useString());
			item.activate();
		} else {
			// add edge from location to to Unlock
			layout.addEdge(location, toUnlock);
			if(!item.activated())
				System.out.println(item.on_useString());
			item.activate();
		}
		return true;
	}

	/**
	 * Finds a room that neighbors your current one
	 * 
	 * @param room the room you're looking for
	 * @return the Room object you need
	 */
	private static Room findRoomInNeighbours(String room) {
		Set<Room> neighbours = layout.getNeighbors(location);
		for(Room rm : neighbours){
			if(rm.getName().equalsIgnoreCase(room)){
				return rm;
			}
		}
		return null;
	}

	/**
	 * prints the final message once the end is reached
	 */
	private static void goalStateReached() {
		if ((location == winningRoom && player.hasItem(winningItem)) 
				|| (location == winningRoom && winningItem == null)){
			System.out.println("Congratulations, " + player.getName() + "!");
			System.out.println(winningMessage);
			System.out.println(gameInfo);
			gameWon = true;
		}
	}

	///////////////////////////////////////////////////////////////////////////
	/**
	 * A short method used to find the room given a name (String)
	 * 
	 * @param name the name of the Vertex/Room
	 * @return the Room object (this disregards the V generality since it
	 * makes no sense in this context)
	 */
	private static Room findRoom(String name) {
		name = name.trim();
		Set<Room> rooms = layout.getAllVertices();
		Room tmp;
		Iterator<Room> itr = rooms.iterator();
		while (itr.hasNext()) {
			tmp = itr.next();
			if (tmp.getName().trim().equalsIgnoreCase(name)) {
				return tmp;
			}
		}
		// if room not found
		throw new IllegalArgumentException();
	}
}