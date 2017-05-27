package PJ4;
import java.util.*;

/*
 * Ref: http://en.wikipedia.org/wiki/Video_poker
 *      http://www.freeslots.com/poker.htm
 *
 *
 * Short Description and Poker rules:
 *
 * Video poker is also known as draw poker. 
 * The dealer uses a 52-card deck, which is played fresh after each playerHand. 
 * The player is dealt one five-card poker playerHand. 
 * After the first draw, which is automatic, you may hold any of the cards and draw 
 * again to replace the cards that you haven't chosen to hold. 
 * Your cards are compared to a table of winning combinations. 
 * The object is to get the best possible combination so that you earn the highest 
 * payout on the bet you placed. 
 *
 * Winning Combinations
 *  
 * 1. One Pair: one pair of the same card
 * 2. Two Pair: two sets of pairs of the same card denomination. 
 * 3. Three of a Kind: three cards of the same denomination. 
 * 4. Straight: five consecutive denomination cards of different suit. 
 * 5. Flush: five non-consecutive denomination cards of the same suit. 
 * 6. Full House: a set of three cards of the same denomination plus 
 * 	a set of two cards of the same denomination. 
 * 7. Four of a kind: four cards of the same denomination. 
 * 8. Straight Flush: five consecutive denomination cards of the same suit. 
 * 9. Royal Flush: five consecutive denomination cards of the same suit, 
 * 	starting from 10 and ending with an ace
 *
 */


/* This is the video poker game class.
 * It uses Decks and Card objects to implement video poker game.
 * Please do not modify any data fields or defined methods
 * You may add new data fields and methods
 * Note: You must implement defined methods
 */



public class VideoPoker {

    // default constant values
    private static final int startingBalance=100;
    private static final int numberOfCards=5;

    // default constant payout value and playerHand types
    private static final int[] multipliers={1,2,3,5,6,10,25,50,1000};
    private static final String[] goodHandTypes={ 
	  "One Pair" , "Two Pairs" , "Three of a Kind", "Straight", "Flush", 
	  "Full House", "Four of a Kind", "Straight Flush", "Royal Flush" };

    // must use only one deck
    private final Decks oneDeck;

    // holding current poker 5-card hand, balance, bet    
    private List<Card> playerHand;
    private int playerBalance;
    private int playerBet;

    /** default constructor, set balance = startingBalance */
    public VideoPoker()
    {
	this(startingBalance);
    }

    /** constructor, set given balance */
    public VideoPoker(int balance)
    {
	this.playerBalance= balance;
        oneDeck = new Decks(1, false);
    }

    /** Displays the payout table based on multipliers and goodHandTypes arrays */
    private void showPayoutTable()
    { 
	System.out.println("\n\n");
	System.out.println("Payout Table   	      Multiplier   ");
	System.out.println("=======================================");
	int size = multipliers.length;
	for (int i=size-1; i >= 0; i--) {
		System.out.println(goodHandTypes[i]+"\t|\t"+multipliers[i]);
	}
	System.out.println("\n\n");
    }

    /** Checks current playerHand using multipliers and goodHandTypes arrays
     *  Must print yourHandType (default is "Sorry, you lost") at the end of function.
     *  This can be checked by testCheckHands() and main() method.
     */
    private void checkHands()
    {
    	// ----------------------------organizational code----------------------------------
        ArrayList<Card> orderedHand = new ArrayList<Card>(playerHand);
        Collections.sort(orderedHand, new CompareTwoCards());
    	// -----------------------------------------------------------------------------------
        if (isRoyalFlush(orderedHand)) {
        	System.out.println(goodHandTypes[8]+'!');
    		playerBet *= multipliers[8];
        } else if (isStraightFlush(orderedHand)) {
        	System.out.println(goodHandTypes[7]+'!');
    		playerBet *= multipliers[7];
        } else if (isFourOfAKind(orderedHand)) {
        	System.out.println(goodHandTypes[6]+'!');
    		playerBet *= multipliers[6];
        } else if (isFullHouse(orderedHand)) {
        	System.out.println(goodHandTypes[5]+'!');
    		playerBet *= multipliers[5];
        } else if (isFlush(orderedHand)) {
        	System.out.println(goodHandTypes[4]+'!');
    		playerBet *= multipliers[4];
        } else if (isStraight(orderedHand)) {
        	System.out.println(goodHandTypes[3]+'!');
    		playerBet *= multipliers[3];
        } else if (isThreeOfAKind(orderedHand)) {
        	System.out.println(goodHandTypes[2]+'!');
    		playerBet *= multipliers[2];
        } else if (isTwoPair(orderedHand)) {
        	System.out.println(goodHandTypes[1]+'!');
    		playerBet *= multipliers[1];
        } else if (isOnePair(orderedHand)) {
        	System.out.println(goodHandTypes[0]+'!');
    		playerBet *= multipliers[0];
        } else {
        	System.out.println("Sorry, you lost!");
            playerBet = 0;
        }
    }

    /*************************************************
     *   Private Methods
     *************************************************/
    
    /** Compares two cards and returns the difference
     * Used in checkHands() to sort ArrayList orderedHand
     * cards from lowest to highest 
     */
	private class CompareTwoCards implements Comparator<Card> 
	{
		public int compare(Card one, Card two) {
			return one.getRank() - two.getRank();
		}
	}
	
    /** Finds the number of a same card rank in a hand and returns the number of repetitions
     * Max hand = 5 Cards, Max # of pairs = 2, Card a, b track pairs for possible three or four of a kinds
     * @return 0 = "Bad Hand", 1 = "One Pair", 2 = "Two Pairs", 3 = "Three of a Kind", 4 = "Four of a Kind"
     */	
	private boolean isOnePair(ArrayList<Card>orderedHand) 
	{
		for (int i = 0; i < 4; i++) {
			if (orderedHand.get(i).getRank() == orderedHand.get(i + 1).getRank()) {
				return true;
			}
		}
		return false;
	}
	
     private boolean isTwoPair(ArrayList<Card>orderedHand)
	{
		for (int i = 0; i < orderedHand.size() - 3; i++) {
			if (orderedHand.get(i).getRank() == orderedHand.get(i + 1).getRank()) {
				for (int j = i + 2; j < orderedHand.size() - 1; j++) {
					if (orderedHand.get(j).getRank() == orderedHand.get(j + 1).getRank()) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
     private boolean isThreeOfAKind(ArrayList<Card>orderedHand)
	{
		for (int i = 0; i < 3; i++) {
			if (orderedHand.get(i).getRank() == orderedHand.get(i + 1).getRank()
					&& orderedHand.get(i).getRank() == orderedHand.get(i + 2).getRank()) {
				return true;
			}
		}
		return false;
	}
     
     private boolean isStraight(ArrayList<Card>orderedHand)
	{
		for (int i = 1; i < 5; i++) {
			if (orderedHand.get(0).getRank() != orderedHand.get(i).getRank() - i) { // Proves false if orderedHand is a straight
				return isAceHighStraight(orderedHand);
			}
		}
		return true;
	}
     
     private boolean isAceHighStraight(ArrayList<Card>orderedHand)
	{
		for (int j = 0; j < 5; j++) {																			// If orderedHand fails (by proving true) initial test
			if (j == 0) {																								// this checks for an "Ace High Straight" scenario.
				if (orderedHand.get(j).getRank() != 1) {
					return false;
				}
			} else if (orderedHand.get(j).getRank() != j + 9) {
				return false;
			}
		}
		return true;
	}
     
     private boolean isFlush(ArrayList<Card>orderedHand)
	{
		for (int i = 0; i < 5; i++) {
			if (orderedHand.get(0).getSuit() != orderedHand.get(i).getSuit()) {
				return false;
			}
		}
		return true;
	}
     
     private boolean isFullHouse(ArrayList<Card>orderedHand)
	{
    	 if (orderedHand.get(0).getRank() == orderedHand.get(1).getRank()) {
    		 if (orderedHand.get(1).getRank() == orderedHand.get(2).getRank()) {
    			 if (orderedHand.get(3).getRank() == orderedHand.get(4).getRank()) {
    				 return true;
    			 }
    		 } else if (orderedHand.get(2).getRank() == orderedHand.get(3).getRank()) {
    			 if (orderedHand.get(3).getRank() == orderedHand.get(4).getRank()) {
    				 return true;
    			 }
    		 }
    	 }
		return false;
	}
     
     private boolean isFourOfAKind(ArrayList<Card>orderedHand)
	{
		for (int i = 0; i < 2; i++) {
			if (orderedHand.get(i).getRank() == orderedHand.get(i + 1).getRank()
					&& orderedHand.get(i).getRank() == orderedHand.get(i + 2).getRank()
					&& orderedHand.get(i).getRank() == orderedHand.get(i + 3).getRank()) {
				return true;
			}
		}
		return false;
	}
     
     private boolean isStraightFlush(ArrayList<Card>orderedHand) 
	{
		return isStraight(orderedHand) && isFlush(orderedHand);
	}
     
     private boolean isRoyalFlush(ArrayList<Card>orderedHand)
	{
		return isAceHighStraight(orderedHand) && isFlush(orderedHand);
	}
     
    public void play() 
    {
    /** The main algorithm for single player poker game 
     *
     * Steps:
     * 		showPayoutTable()
     *
     * 		++	
     * 	1	show balance, get bet
     *		verify bet value, update balance
     *	2	reset deck, shuffle deck, 
     *	3	deal cards and display cards
     *	4	ask for positions of cards to replace 
     *          get positions in one input line
     *		update cards
     *	5	check hands, display proper messages
     *	6	update balance if there is a payout
     *		if balance = O:
     *			end of program 
     *		else
     *	7		ask if the player wants to play a new game
     *			if the answer is "no" : end of program
     *	8		else : showPayoutTable() if user wants to see it
     *			goto ++
     */

    	Scanner in = new Scanner(System.in);
    	boolean userCheck = true;
    	
    	showPayoutTable();  
    	while (userCheck) {
    		System.out.println("======================================="
    				+ "\nBalance: $" + playerBalance);
    		while (userCheck) {
    			System.out.print("Enter wager: $");
    			if (in.hasNextInt()) {
    				playerBet = in.nextInt();
    				if (playerBet >= 0 || playerBet <= playerBalance) {
    					playerBalance -= playerBet;
    					userCheck = false;
    				} else {
    					System.out.println("*** 0 <= Wager (integer) <= Balance ***");
    				}
    			} else {
    				System.out.println("*** 0 <= Wager (integer) <= Balance ***");
    			}
    		}
    		
    		oneDeck.reset();
    		oneDeck.shuffle();
    		
    		try {
    			playerHand = new ArrayList<Card>(oneDeck.deal(numberOfCards));
    		} catch (PlayingCardException e) {
    			System.out.println("*** Fatal error while dealing cards. Exiting program! ***");
    			e.printStackTrace();
    			in.close();
    			System.exit(-1);
    		} finally {
    			System.out.println("Hand: " + playerHand);
    		}
    		
			while (!userCheck) {
				Scanner input = new Scanner(System.in);
				System.out.print("Enter positions of cards to replace (e.g. 1 4 5 ): ");
				if (input.hasNext()) {
					String indexes = input.nextLine();
					String[] replaceIndex = indexes.trim().split("\\s+");
					for (int i = 0; i < replaceIndex.length; i++) {
						try {
							int number = Integer.parseInt(replaceIndex[i]);
							if (number > 0 && number < 6)
								playerHand.set(number - 1, oneDeck.deal(1).get(0));
						} catch (NumberFormatException e) {
							continue;
						} catch (PlayingCardException e) {
							System.out.println("*** Fatal error while dealing cards. Exiting program! ***");
							e.printStackTrace();
							in.close();
							input.close();
							System.exit(-1);
						}
					}
					userCheck = true;
				} else {
					System.out.println("*** Try again ***");
				}
			}
			
			System.out.println("Hand: " + playerHand);
			checkHands();
			playerBalance += playerBet;
			
			if (playerBalance <= 0) {
				System.out.println("You have run out of funds, you lose. Goodbye!");
				in.close();
				System.exit(0);
			}
			
			char userInput;
			while (userCheck) {
				System.out.print("Current balance: $" + playerBalance + ", continue? (y or n): ");
				if (in.hasNext()) {
					userInput = in.next().charAt(0);
					userInput = Character.toLowerCase(userInput);
					if (userInput == 'n') {
						System.out.println("Goodbye!");
						in.close();
						System.exit(0);
					} else if (userInput == 'y') {
						userCheck = false;
					} else {
						System.out.println("*** Enter either \"y\" or \"n\" ***");
					}
				} else {
					System.out.println("*** Try again ***");
					System.exit(0);
				}
			}
			
			while (!userCheck) {
				System.out.print("Would you like to reference the payout table? (y or n): ");
				if (in.hasNextLine()) {
					userInput = in.next().charAt(0);
					userInput = Character.toLowerCase(userInput);
					if (userInput == 'n') {
						userCheck = true;
					} else if (userInput == 'y') {
						showPayoutTable();
						userCheck = true;
					} else {
						System.out.println("*** Enter either \"y\" or \"n\" ***");
					}
				} else {
					System.out.println("*** Try again ***");
				}
			}
    	}
	}

    /*************************************************
     *   Do not modify methods below
    /*************************************************

    /** testCheckHands() is used to test checkHands() method 
     *  checkHands() should print your current hand type
     */ 

    public void testCheckHands()
    {
      	try {
    		playerHand = new ArrayList<Card>();

		// set Royal Flush
		playerHand.add(new Card(3,1));
		playerHand.add(new Card(3,10));
		playerHand.add(new Card(3,12));
		playerHand.add(new Card(3,11));
		playerHand.add(new Card(3,13));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");

		// set Straight Flush
		playerHand.set(0,new Card(3,9));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");

		// set Straight
		playerHand.set(4, new Card(1,8));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");

		// set Flush 
		playerHand.set(4, new Card(3,5));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");

		// "Royal Pair" , "Two Pairs" , "Three of a Kind", "Straight", "Flush	", 
	 	// "Full House", "Four of a Kind", "Straight Flush", "Royal Flush" };

		// set Four of a Kind
		playerHand.clear();
		playerHand.add(new Card(4,8));
		playerHand.add(new Card(1,8));
		playerHand.add(new Card(4,12));
		playerHand.add(new Card(2,8));
		playerHand.add(new Card(3,8));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");

		// set Three of a Kind
		playerHand.set(4, new Card(4,11));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");

		// set Full House
		playerHand.set(2, new Card(2,11));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");

		// set Two Pairs
		playerHand.set(1, new Card(2,9));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");

		// set One Pair
		playerHand.set(0, new Card(2,3));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");

		// set One Pair
		playerHand.set(2, new Card(4,3));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");

		// set no Pair
		playerHand.set(2, new Card(4,6));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");
      	}
      	catch (Exception e)
      	{
		System.out.println(e.getMessage());
      	}
    }

    /* Quick testCheckHands() */
    public static void main(String args[]) 
    {
	VideoPoker pokergame = new VideoPoker();
	pokergame.testCheckHands();
    }
}
