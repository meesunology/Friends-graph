package FriendshipProject;

import java.io.*;
import java.util.*;

//Meesun Park Section 21

//person
class Person{
	String name;
	String college;
	Boolean student;
	
	//person constructor
	public Person(String name, String st, String college){
		this.name = name;
		this.college = college;
		
		//is the person a student or nah?
		if (st == "y"){
			student = true;
		} else{
			student = false;
		}
	}

}

//node
class Node{
	String data;
	Node next;
	
	//node constructor
	public Node(String data, Node next){
		this.data = data;
		this.next = next;
	}
}

public class graph {
	//Hashmap variables
	//neighbors <name of person, node>
	public HashMap<String, Node> neighborPairs = new HashMap<String, Node>();
	//neighbors with same schools <name of school, person>
	public HashMap<String, Node> sameSchool = new HashMap<String, Node>();
	//names of all people <name, actual person object>
	public HashMap<String, Person> names = new HashMap<String, Person>();
	//who connects to who
	public ArrayList<String> connectors = new ArrayList<String>();
	public int count = 1;
	
	//build method
	public void build(String fileName) throws FileNotFoundException{
		//scanner and num of people is the first line
		Scanner sc = new Scanner(new File(fileName));
		int numPeople = sc.nextInt();
		
		sc.nextLine();
		
		//reads just the people (num of vertices)
		for (int i = 0; i < numPeople; i++){
			String personInfo = sc.nextLine().toLowerCase();
			
			//creates the person
			String [] personInfoP = personInfo.split("\\|", 3);
			Person x;
			if (personInfoP.length == 3){
				x = new Person(personInfoP[0], "y", personInfoP[2]);
			} else{
				x = new Person(personInfoP[0], "n", null);
			}
			
			//adds them into the two hashmaps
			neighborPairs.put(personInfoP[0], null);
			names.put(personInfoP[0], x);
			
		}
		
		//relationships
		while (sc.hasNextLine()){
			String pair = sc.nextLine().toLowerCase();
			
			String [] pair1 = pair.split("\\|", 3);
			Node neigh1 = neighborPairs.get(pair1[0]);
			Node neigh2 = neighborPairs.get(pair1[1]);
			
			//swaps
			neighborPairs.put(pair1[0], new Node(pair1[1], neigh1));
			neighborPairs.put(pair1[1], new Node(pair1[0], neigh2));
			
			//if both students - do they go to the same school or nah?
			if(names.get(pair1[0]).student && names.get(pair1[1]).student){
				if(names.get(pair1[0]).college.equals(names.get(pair1[1]).college)){
					Node temp = sameSchool.get(names.get(pair1[0]).college);
					sameSchool.put(names.get(pair1[0]).college, new Node(pair, temp));
				}
			}
			
		}
		
		for (String person : names.keySet()){
			String collegeName = names.get(person).college;
			boolean f = false;
			Node current = neighborPairs.get(person);
			
			for (current = neighborPairs.get(person); current != null; current = current.next){
				if (names.get(current.data).student && names.get(current.data).college.equals(collegeName)){
					f = true;
					break;
				}
			}
			
			if (!f){
				Node temp = sameSchool.get(collegeName);
				sameSchool.put(collegeName, new Node(person, temp));
				
			}
			
		}
		
		sc.close();
	}
	
	public void shortestPath(String person1, String person2) throws FileNotFoundException{
		//case sensitive
		person1 = person1.toLowerCase();
		person2 = person2.toLowerCase();
		
		//same name
		if (person1.equals(person2)){
			System.out.println("Cannot be friends with themselves.");
			return;
		}
		
		//not there
		if (names.get(person1) == null || names.get(person2) == null){
			System.out.println("One or both of names were not found, sorry.");
			return;
		}
		
		//initializing ma hashmaps
		HashMap<String, String> prev = new HashMap<String, String>();
		HashMap<String, Boolean> visited = new HashMap<String, Boolean>();
		Queue<String> queue = new LinkedList<String>();
		
		//making all names and saying that they weren't visited yet.
		for (String s : names.keySet()) {
			visited.put(s, false);
		}
		
		queue.add(person1);
		visited.put(person1, true);
		prev.put(person1, person1);
		
		while(!queue.isEmpty()){
			String a = queue.poll();
			Node current = neighborPairs.get(a);
			while (current != null){
				if (visited.get(current.data) == false){
					visited.put(current.data, true);
					prev.put(current.data,  a);
					queue.add(current.data);
				}
				
				if(current.data.equals(person2)){
					ArrayList<String> path = new ArrayList<String>();
					path.add(person2);
					
					String next = prev.get(person2);
					while (true){
						path.add(0, next);
						
						if (next.equals(prev.get(next))){
							break;
						}
						next = prev.get(next);
					}
					
					System.out.print(person1);
					for (int i = 1; i < path.size(); i ++){
						System.out.print("--");
						System.out.print(path.get(i));
					}
					
					return;
				}
				current = current.next;
			}
		}
		if (visited.get(person2) == false){
			System.out.println("No path from "+person1+" and "+ person2);
			return;
		}
		
	}
	
	//clique method
	public void cliques(String school){
		//case sensitivity
		school = school.toLowerCase();
		
		if (sameSchool.get(school) == null){
			System.out.println("No one goes here.");
			return;
		}
		
		Node current = sameSchool.get(school);
		System.out.println("Clique:");
		while (current != null){
			System.out.println(current.data);
			current = current.next;
		}
	}
	
	//helper method for connectors
	private void DFS(String name, HashMap<String,Boolean> visited, HashMap<String,Integer> num, HashMap<String, Integer> back){
		//count is for giving the dfsnum and back
		//this is normal dfs being done
		visited.put(name, true);
		num.put(name, count);
		back.put(name, count);
		count++;
		Node current = neighborPairs.get(name);
		
		while(current != null){
			// if we didnt visit
			if(visited.get(current.data) == null) {
				DFS(current.data, visited, num, back);
				if(num.get(name) != 1) {
					if(back.get(name) > back.get(current.data)){
						back.put(name, back.get(current.data));
						} if(!connectors.contains(name) && num.get(name) <= back.get(current.data)){
							connectors.add(name);
							}
						}
				} else { //if we did visit
					if(back.get(name) > num.get(current.data)){//want back(v) = min(back(v),dfsnum(w))
						back.put(name, num.get(current.data));
						}
					}
			current = current.next;
			}
		
		if(num.get(name) == 1){
			return;
		}
	}
	
	//Prints anything in the array
	public void printConnectors(){
		//no edges/relationships between any of the people
		if (connectors.size() == 0){
			for (String person : names.keySet()){
				count = 1;
				DFS(person, new HashMap<String, Boolean>(), new HashMap<String, Integer>(), new HashMap<String, Integer> ());
			}
		}
		
		//no connectors
		System.out.println("Connectors:");
		if (connectors.size() == 0){
			System.out.println("Just Kidding. There's none.");
		}
		
		//prints out all connections
		int i = 0;
		while (i < connectors.size()){
			System.out.print(connectors.get(i));
			if (i != connectors.size()-1){
				System.out.print(",");
			}
			i++;
		}
	}
}
