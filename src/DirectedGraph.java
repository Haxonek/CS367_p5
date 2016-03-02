///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Main Class File:  TheGame.java
// File:             DirectedGraph.java
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
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * The Directed Graph class houses the implementation of GraphADT used for the
 * game, as well as manages the main hashmap which holds a significant portion
 * of the game information.
 * 
 * @author Thomas Hansen
 */
public class DirectedGraph<V> implements GraphADT<V>{
	private HashMap<V, ArrayList<V>> hashmap;
	//DO NOT ADD ANY OTHER DATA MEMBERS

	/**
	 * Creates an empty graph.
	 */
	public DirectedGraph() {
		// runs the main constructor with empty values
		this(new HashMap<V, ArrayList<V>>());
	}

	/**
	 * Creates a graph from a preconstructed hashmap. This method will be used
	 * for testing your submissions.
	 * 
	 * @param hashmap pre-existing lists and node
	 */
	public DirectedGraph(HashMap<V, ArrayList<V>> hashmap) {
		// throws an error if the hashmap is empty/null
		if (hashmap == null) {
			throw new IllegalArgumentException();
		}
		// assigns the hashmap
		this.hashmap = hashmap;
	}

	@Override
	/**
	 * Adds the specified vertex to this graph if not already present. More
	 * formally, adds the specified vertex v to this graph if this graph
	 * contains no vertex u such that u.equals(v). If this graph already
	 * contains such vertex, the call leaves this graph unchanged and returns
	 * false.
	 * 
	 * @param vertex the vertex to be added
	 * @return true if the Vertex is properly added
	 */
	public boolean addVertex(V vertex) {
		if (vertex == null) {
			throw new IllegalArgumentException();
		}
		// checks to make sure it doesn't already exist
		if (hashmap.containsKey(vertex)) {
			// returns false if it exists
			return false;
		}
		// adds to the list
		hashmap.put(vertex, new ArrayList<V>());
		return true;
	}

	@Override
	/**
	 * Creates a new edge from vertex v1 to v2 and returns true, if v1 and v2
	 * are not the same vertex and an edge does not already exist from v1 to
	 * v2. Returns false otherwise.  Vertices v1 and v2 must already exist in
	 * this graph. If they are not found in the graph IllegalArgumentException
	 * is thrown.
	 * 
	 * @param v1 The Room the edge comes form (vertex)
	 * @param v2 The Room the edge leads to (the edges endpoint)
	 * @return true if the edge is added
	 */
	public boolean addEdge(V v1, V v2) {
		if (v1 == null || v2 == null) {
			throw new IllegalArgumentException();
		} else if (v1.equals(v2)) {
			return false;
		}
		ArrayList<V> edges = hashmap.get(v1);
		if (edges.contains(v2)) {
			return false;
		}

		// add value to arraylist
		edges.add(v2);
		// push through as new List below
		hashmap.put(v1, edges);
		return true;
	}

	@Override
	/**
	 * Returns a set of all vertices to which there are outward edges from v.
	 * Vertex v must already exist in this graph. If it is not found in the
	 * graph IllegalArgumentException is thrown.
	 * 
	 * @param vertex the vertex in which the neighbors will surround
	 * @return The list of neighboring vertices to 'vertex'
	 */
	public Set<V> getNeighbors(V vertex) {
		if (vertex == null) {
			throw new IllegalArgumentException();
		} else if (!hashmap.containsKey(vertex)) {
			throw new IllegalArgumentException();
		}
		
		ArrayList<V> edges = hashmap.get(vertex);
		Set<V> neighbors = new HashSet<V>();
		// copy ArrayList into Set<V>
		Iterator<V> itr = edges.iterator();
		while (itr.hasNext()) {
			// adds each V (Room) to the new list
			neighbors.add(itr.next());
		}
		return neighbors;
	}

	@Override
	/**
	 * If both v1 and v2 exist in the graph, and an edge exists from v1 to v2,
	 * remove the edge from this graph. Otherwise, do nothing.
	 * 
	 * @param v1 the V the values coming from
	 * @param v2 the edge being removed
	 */
	public void removeEdge(V v1, V v2) {
		// throws an error if the arguments are null
		if (v1 == null || v2 == null) {
			throw new IllegalArgumentException();
		}
		// removes the edge from the list
		hashmap.get(v1).remove(v2);
	}

	@Override
	/**
	 * Returns a set of all the vertices in the graph.
	 * 
	 * @return all the Verticies in the Set
	 */
	public Set<V> getAllVertices() {
		return hashmap.keySet();
	}

	@Override
	//Returns a String that depicts the Structure of the Graph
	//This prints the adjacency list
	//This has been done for you
	//DO NOT MODIFY
	public String toString() {
		StringWriter writer = new StringWriter();
		for (V vertex: this.hashmap.keySet()) {
			writer.append(vertex + " -> " + hashmap.get(vertex) + "\n");
		}
		return writer.toString();
	}
}






