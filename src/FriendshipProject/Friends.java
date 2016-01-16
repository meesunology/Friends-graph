package FriendshipProject;

import java.io.*;

//Matthew Freeman Section 14
//Meesun Park Section 21

public class Friends {
	
	public static void main(String[] args)throws IOException{
		BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
		
		System.out.println("Enter graph input file name: ");
		String file = keyboard.readLine();
		graph graph = new graph();
		graph.build(file);
		
		int choice = -1;
		while (true){
			choice = -1;
			System.out.println();
			System.out.println("Menu:");
			System.out.println("1. Shortest Intro Chain");
			System.out.println("2. Cliques at School");
			System.out.println("3. Connectors.");
			System.out.println("4. Quit");
			
			System.out.println("Enter your choice:");
			choice = Integer.parseInt(keyboard.readLine());
			
			switch(choice){
				case 1:
					//Shortest Intro Chain
					String person1, person2;
					while (true){
						try{
							System.out.println("Enter the name of the person.");
							person1 = keyboard.readLine();
							System.out.println("Enter the name of the other person.");
							person2 = keyboard.readLine();
							
							graph.shortestPath(person1, person2);
							System.out.println();
							break;
						} catch(IOException e){
								System.out.println("Enter the right name.");
						}
					}
					break;
				
				case 2:
					//Cliques at School
					String school;
					while (true){
						try{
							//gets school input
							System.out.println("Enter a school name.");
							school = keyboard.readLine();
							
							//gets school
							graph.cliques(school);
							break;
						} catch(IOException e){
							System.out.println("That school doesn't exist here.");
					}
					}
					break;
			
				case 3:
					//Connectors
					graph.printConnectors();
					break;
			
				case 4:
					//quit
					System.exit(0);
					break;
			
				default:
					System.out.println("Please choose a number between 1 and 4.");
			}
			
		}	
	}
}