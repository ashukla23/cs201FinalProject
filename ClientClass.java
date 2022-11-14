package csci201_groupProject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.BufferUnderflowException;
import java.util.Scanner;


//import javax.lang.model.element.NestingKind;

public class ClientClass extends Thread {
	
	private PrintWriter printWriter = null;
	private BufferedReader bufferedReader = null;
	private boolean status;
	
	
	public ClientClass() {
		Scanner scanner = new Scanner(System.in);
		String hostname = null;
		int portNum = 0;
		Socket socket = null;
		status = false;
		
		//intial message and prompt for hostname and port, make connection
		System.out.println("Welcome to BlackJack");
		
		boolean connection = false;
		
		while(!connection) {
		
			try {
				System.out.println("Enter the server hostname:");
				hostname = scanner.nextLine();
		
				System.out.println("Enter the server port:");
				portNum = Integer.parseInt(scanner.nextLine());
				
				socket = new Socket(hostname, portNum);
				
				bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				printWriter = new PrintWriter(socket.getOutputStream());
					
								
				connection = true;
				
			} catch(Exception e) {
				System.out.println("Server connection issue.");
			}
			
		}
		
		this.start();
		
		
		//ask for user input
		try {
			while(true) {
				if(status == true) {
					String lineString = scanner.nextLine();
					sendMessage(lineString);
					
				}		
			}
				
			
		} catch (Exception e) {
			System.out.println(e);
		}	
		
		scanner.close();
	}
	
	
	public static void main(String[] args) {
		ClientClass clientClass = new ClientClass();
		
	}
	
	
	
	public void run() {
		
		try {
			String lineString = bufferedReader.readLine();
			while(!lineString.equals("")) {
				if(lineString != "I") {
					System.out.println(lineString);
					lineString = bufferedReader.readLine();
				} else {
					status = true;
				}
			}
		} catch (Exception e) {
		}	
	}
	
	
	public void sendMessage(String message) {
		printWriter.println(message);
		printWriter.flush();	
	}

}
