package PJ3;

import java.util.*;
import java.io.*;

// You may add new functions or data in this class 
// You may modify any functions or data members here
// You must use Customer, Teller and ServiceArea classes
// to implement Bank simulator

class BankSimulator {

	// input parameters
	private int numTellers, customerQLimit, chancesOfArrival;
	private int simulationTime, maxTransactionTime;
	private int dataSource;

	// statistical data
	private int numTurnedAway, numServed, totalWaitingTime;

	// internal data
	private int customerIDCounter; 	// customer ID counter
	private ServiceArea servicearea; 	// service area object
	private static Scanner dataFile; 	// get customer data from file
	private Random dataRandom; 		// get customer data using random function
	
	// most recent customer arrival info, see getCustomerData()
	private boolean anyNewArrival;
	private int transactionTime;
	private Customer customer;

	// initialize data fields
	private BankSimulator() {
		// statistical
		numTurnedAway = 0;
		numServed = 0;
		totalWaitingTime = 0;
		// internal
		customerIDCounter = 0;
	}

	// -----------------------------------------------------------------------------------
	/*
	Step 1) Enter simulation time (positive integer)       				: 10
	Step 2) Enter the number of tellers                    						: 3
	Step 3) Enter chances (0% < & <= 100%) of new customer 	: 75
	Step 4) Enter maximum transaction time of customers    		: 5
	Step 5) Enter customer queue limit                     						: 2
	Step 6) Enter 0/1 to get data from Random/file         				: 1
	Step 7) Enter filename                                 								: DataFile
	 */
	// -----------------------------------------------------------------------------------
	// Maximum simulation parameters
	/*
	- Maximum number of tellers 10 
	- Maximum simulation length 10000 
	- Maximum transaction time 500 
	- Maximum customer queue limit 50 
	- Probability of a new customer 1% - 100%
	 */
	// -----------------------------------------------------------------------------------
	private void setupParameters() {
		Scanner in = new Scanner(System.in);
		boolean pass = false;
		int userInputInt;
		// read input parameters
		// 1
		System.out.println("\t***  Get Simulation Parameters  ***\n\n");
		while (!pass) {
			System.out.print("Enter simulation time (0 < integer <= 10000)						: ");
			if (in.hasNextInt()) {
				userInputInt = in.nextInt();
				if (userInputInt > 10000) {
					System.out.println(
							"Error Code 101: Maximum simulation length allowed is 10000, enter a smaller integer.");
					in.nextLine();
				} else if (userInputInt < 1) {
					System.out
							.println("Error Code 102: Minimum simulation length allowed is 1, enter a larger integer.");
				} else {
					simulationTime = userInputInt;
					pass = true;
				}
			} else {
				System.out.println("Error Code 103: Enter an integer.");
				in.nextLine();
			}
		}
		pass = false;																									// clean up code
		// 2
		while (!pass) {
			System.out.print("Enter the number (0 < integer <= 10) of tellers						: ");
			if (in.hasNextInt()) {
				userInputInt = in.nextInt();
				if (userInputInt > 10) {
					System.out.println(
							"Error Code 201: Maximum numbers of allowed tellers is 10, enter a smaller integer.");
				} else if (userInputInt < 1) {
					System.out.println(
							"Error Code 202: Minimum numbers of allowed tellers is 1, enter a larger integer.");
				} else {
					numTellers = userInputInt;
					pass = true;
				}
			} else {
				System.out.println("Error Code 203: Enter an integer.");
				in.nextLine();
			}
		}
		pass = false;																									// clean up code
		// 3
		while (!pass) {
			System.out.print("Enter chances (0% < integer <= 100%) of new customer 				: ");
			if (in.hasNextInt()) {
				userInputInt = in.nextInt();
				if (userInputInt > 100) {
					System.out.println(
							"Error Code 301: Maximum percentage allowed is 100, enter a smaller integer.");
				} else if (userInputInt < 1) {
					System.out.println(
							"Error Code 302: Minimum percentage allowed is 1, enter a larger integer.");
				} else {
					chancesOfArrival = userInputInt;
					pass = true;
				}
			} else {
				System.out.println("Error Code 303: Enter an integer.");
				in.nextLine();
			}
		}
		pass = false;																									// clean up code
		// 4
		while (!pass) {
			System.out.print("Enter maximum transaction time (0 < integer <= 500) of customers	: ");
			if (in.hasNextInt()) {
				userInputInt = in.nextInt();
				if (userInputInt > 500) {
					System.out.println(
							"Error Code 401: Maximum transaction time allowed is 500, enter a smaller integer.");
				} else if (userInputInt < 1) {
					System.out
							.println("Error Code 402: Minimum transaction time allowed is 1, enter a larger integer.");
				} else {
					maxTransactionTime = userInputInt;
					pass = true;
				}
			} else {
				System.out.println("Error Code 403: Enter an integer.");
				in.nextLine();
			}
		}
		pass = false;																									// clean up code
		// 5
		while (!pass) {
			System.out.print("Enter customer queue size (0 < integer <= 50)						: ");
			if (in.hasNextInt()) {
				userInputInt = in.nextInt();
				if (userInputInt > 50) {
					System.out.println(
							"Error Code 501: Maximum queue size allowed is 50, enter a smaller integer.");
				} else if (userInputInt < 1) {
					System.out.println(
							"Error Code 502: Minimum queue size allowed is 1, enter a larger integer.");
				} else {
					customerQLimit = userInputInt;
					pass = true;
				}
			} else {
				System.out.println("Error Code 503: Enter an integer.");
				in.nextLine();
			}
		}
		pass = false;																									// clean up code
		// setup dataFile or dataRandom
		// 6
		while (!pass) {
			System.out.print("Enter 0/1 to get data from Random/file							: ");
			if (in.hasNextInt()) {
				userInputInt = in.nextInt();
				if (userInputInt == 1) {
					dataSource = userInputInt;
					pass = true;
				} else if (userInputInt == 0) {
					dataSource = userInputInt;
					pass = true;
				} else {
					System.out.println("Error Code 601: Enter either 0 or 1. ");
				}
			} else {
				System.out.println("Error Code 603: Enter an integer.");
				in.nextLine();
			}
		}
		pass = false;																									// clean up code
		// 7
		if (dataSource == 1) {
			getDataFile();
		}
		in.close();
	}
	
	private void getDataFile() {
		boolean pass = false;
		Scanner in = new Scanner(System.in);
		while (!pass) {
			System.out.print("Enter filename: ");
			try {
				File file = new File(in.next());
				dataFile = new Scanner(file); 																// open file
				pass = true;
			} catch (Exception fileNotFoundException) {
				System.out.println("Error Code 701: File not found. Enter new file name.");
			}
		}
		in.close(); 																										// clean up code
	}

	// Refer to step 1 in doSimulation()
	private void getCustomerData() {
		// get next customer data : from file or random number generator
		// set anyNewArrival and transactionTime
		// see readme file for more info

		if (dataSource == 1) { // file																			// 0/1 random or file input
					if (dataFile.hasNextInt()) {																	// checks for a line of information
						int data1 = dataFile.nextInt();														// every line has 2 numbers of info
						int data2 = dataFile.nextInt();
						anyNewArrival 	= (((data1%100)+1)<= chancesOfArrival);		// computes anyNewArrival boolean
				        transactionTime = (data2%maxTransactionTime)+1;					// computes transactionTime integer
					}
		} else { // random
			dataRandom = new Random();																	// generates random boolean and int
			anyNewArrival 	= ((dataRandom.nextInt(100) + 1) <= chancesOfArrival);
			transactionTime = dataRandom.nextInt(maxTransactionTime) + 1;
		}
	}

	private void doSimulation() {
		System.out.println("\n------------------------------------------------------\n\n");
		System.out.println("\t\t  *** Start Simulation ***\n\n");
		
		servicearea = new ServiceArea(numTellers, customerQLimit);													// Initialize ServiceArea
		boolean pass = false;																													// flow control variable
		
		for (int currentTime = 1; currentTime <= simulationTime; currentTime++) {							// time driver simulation loop
			System.out.println("------------------------------------------------------\nTime: " + currentTime);

			getCustomerData();																													// grabs next customer info/ anyNewArrival
			System.out.println("\t*** Status @ beginning of time unit ***");
			System.out.println("\t\tThere are " + servicearea.numWaitingCustomers() + " customers in line.");
			System.out.println("\t\tThere are " + servicearea.numFreeTellers() + " free tellers.");
			System.out.println("\t\tThere are " + servicearea.numBusyTellers() + " busy tellers.\n");
			
			// -----------------------------------------------------------------------------------
			// Step 1: any new customer enters the bank?
			// Step 1.1: setup customer data
			// Step 1.2: check customer waiting queue too long?
			// if it is too long, update numTurnedAway
			// else enter customer queue
			
			// Step 2: free busy tellers that are done at currentTime, add to
			// freeTellerQ
			// Step 3: get free tellers to serve waiting customers at
			// currentTime
			// -----------------------------------------------------------------------------------
			if (anyNewArrival) {																													// if a customer arrives
				customerIDCounter++;
				customer = new Customer(customerIDCounter, transactionTime, currentTime);				// create new customer object
				System.out.println("Customer #" + customerIDCounter + " arrives with transaction time " + customer.getTransactionTime() + ".");
				if (servicearea.isCustomerQTooLong()) {																				// checks if customerQLimit is reached
					System.out.println("Customer #" + customer.getCustomerID() + " leaves because the line was too long.");
					numTurnedAway++;																											// if so, doesn't add new customer
				} else {																																		// if there is space in customerQ
					System.out.println("Customer #" + customer.getCustomerID() + " gets in line.");
					servicearea.insertCustomerQ(customer);																			// push into customerQ
				}
			} else {																																			// if a customer doesn't arrive
				System.out.println("No new customer!");																				// if so, do nothing
			}
			
			while (!pass) {
				if (servicearea.emptyBusyTellerQ()) {																					// checks if busyTellerQ is empty
					System.out.println("No busy tellers!");																				// if so, do nothing
					pass = true;
				} else if (servicearea.getFrontBusyTellerQ().getEndBusyTime() <= currentTime) {			// checks if a busy teller is finished
					servicearea.getFrontBusyTellerQ().busyToFree();																// swaps time counter from busyTime to freeTime
					System.out.println("Teller #" + servicearea.getFrontBusyTellerQ().getTellerID()
							+ " finishes helping customer #"
							+ servicearea.getFrontBusyTellerQ().getCustomer().getCustomerID() + ".");
					servicearea.insertFreeTellerQ(servicearea.removeBusyTellerQ());									// transfers teller from busyTellerQ to freeTellerQ
					numServed++;
				} else {
					pass = true;
				}
			}
			pass = false;																																// clean up code
			
			while (!pass) {
				if (servicearea.emptyCustomerQ()) {																						// checks if customerQ is empty
					System.out.println("No customer's in line!");
					pass = true;																														// exits out of while loop if no more functions to perform
				} else if (servicearea.emptyFreeTellerQ()) {																			// checks if freeTellerQ is empty
					System.out.println("No teller available!");
					pass = true;
				} else {																																		// if freeTeller available and customer waiting
					Teller nextTeller = servicearea.removeFreeTellerQ();														// temporary objects
					Customer nextCustomer = servicearea.removeCustomerQ();
					System.out.println("Customer #" + nextCustomer.getCustomerID() + " gets a teller.");
					System.out.println("Teller #" + nextTeller.getTellerID() + " starts helping customer #"
							+ nextCustomer.getCustomerID() + " for " + nextCustomer.getTransactionTime() + ".");
					nextTeller.freeToBusy(nextCustomer, currentTime);														// freeTeller helps out next customer
					servicearea.insertBusyTellerQ(nextTeller);																		// freeTeller is pushed into busyTellerQ
				}
			}
			pass = false;																																// clean up code
			
			System.out.println("\n\t*** Status @ end of time unit ***");
			System.out.println("\t\tThere are " + servicearea.numWaitingCustomers() + " customers in line.");
			System.out.println("\t\tThere are " + servicearea.numFreeTellers() + " free tellers.");
			System.out.println("\t\tThere are " + servicearea.numBusyTellers() + " busy tellers.");
			
		} // end simulation loop
		System.out.println("------------------------------------------------------\n\n");
		System.out.println("\t\t  *** End Simulation ***\n\n\n------------------------------------------------------\n");
	}

	private void printStatistics() {
		// add statements into this method!
		// print out simulation results
		// see the given example in project statement
		// you need to display all free and busy bank tellers

		// need to free up all customers in queue to get extra waiting time.
		// need to free up all tellers in free/busy queues to get extra free &
		// busy time.
		
		System.out.println("End of simulation report\n");
		System.out.println("\t# total arrival customers  			: " + customerIDCounter);
		System.out.println("\t# customers turned-away        		: " + numTurnedAway);
		System.out.println("\t# customers served         			: " + numServed + "\n");
		System.out.println("\t*** Current Tellers Info ***\n\n");
		servicearea.printStatistics();
		System.out.println("\n\n\tTotal waiting time         				: " + totalWaitingTime);
		System.out.println("\tAverage waiting time       			: "
				+ (double) totalWaitingTime / (customerIDCounter - numTurnedAway));
		System.out.println("\n\n\tBusy Tellers (" + servicearea.numBusyTellers() + ") Info\n\n");
		for (int i = 0; i < servicearea.numBusyTellers();) {																		// loops through busyTellerQ
			Teller nextBusyTeller = servicearea.removeBusyTellerQ();														// empties queue and prints
			nextBusyTeller.printStatistics();																									// teller statistics
		}
		System.out.println("\n\tFree Tellers (" + servicearea.numFreeTellers() + ") Info\n\n");
		for (int i = 0; i < servicearea.numFreeTellers();) {																		// loops through freeTellerQ
			Teller nextFreeTeller = servicearea.removeFreeTellerQ();														// empties queue and prints
			nextFreeTeller.printStatistics();																									// teller statistics
			System.out.println("\n");
		}
	}

	// *** main method to run simulation ****

	public static void main(String[] args) {
		BankSimulator runBankSimulator = new BankSimulator();
		runBankSimulator.setupParameters();
		runBankSimulator.doSimulation();
		runBankSimulator.printStatistics();
	}

}
