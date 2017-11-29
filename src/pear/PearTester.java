package pear;


/* Notes:
 *       -1 = obstacle
 *        0 = empty
 *     1-20 = pattern; becomes 0 after being paired up
 *       at least 1 of width and height must be even, or else WxH is odd
 *        
 */

/* Idea for Pear: 
 *       get random int[0:19], put them on board pair by pair
 *       give 3 shuffle chances, each will shuffle every icon left
 * 
 */


//compare two board filling methods and see which is faster
// fill1: get random int[0:19], put in boardArray[random1] and boardArray[random2], 
//		  then put into board
// fill2: given int[total], shuffle every icon, then put into board

/* Conclusion:
 * 		Nov 10, 2017 3am
 * 					on average fill-1 takes 0-10 millis, while fill-2 takes 0
 * 					  shuffling 10 times takes about 0 millis
 * 		Still need to do
 * 					assert input is valid, and in other forms besides 
 * 					  just 2 int's separated by space..
 * 					make path finder that limit turns to maximum of 3
 * 
 * 		Nov 10, 2017 7pm
 * 					realized that since paths can only be created over 0's,
 * 					  therefore need to create ring of 0's around board.
 * 					Adding obstacle-handling is easier to do for fill-1
 * 					  than fill-2 so fill-1 now has the new feature. But
 * 					  since fill-2 is proven to be faster, that will need
 * 					  to be done (will probably time them against each other
 * 					  gain after finishing obstacle-handling for both)
 * 					this also makes shuffling more complicated..
 * 					game ends when all the pairs have been found. Can make
 * 					  program check if all coord's are 0's and -1's after
 * 					  every valid move, or have number of pairs as 
 * 					  instance variable of board class
 * 					assert somewhere length of obstacles[] is even and no repeats
 * 					  (might be unnecessary?)
 * 					
 * 					NEEDS IMMEDIATE FIX:
 * 					how to stop game from running; right now posA_H 
 * 					  runs out of bound before while condition becomes
 * 					  false
 * 		
 * 		Nov 11, 2017 
 * 					shuffle is fixed - wont move -1's, and can throw/catch
 * 					  exception if it runs into error or is stuck in loop 
 * 		Nov 12, 2017
 * 					two possible ideas to find shortest path:
 * 					1) Breadth First Search
 * 						 - selected node
 * 						 - visited nodes
 * 						 - queue nodes
 * 					2) A* algorithm with
 * 						 a) Manhattan Distance Heuristic
 * 							h = |x_start - x_destination| + |y_s - y_d|
 * 						 b) Euclidean Distance Heuristic
 * 							h = sqrt((x_s - x_d)^2 + (y_s - y_d)^2)
 * 							slightly more accurate and favours straight lines,
 * 							  but slower due to larger area to explore
 * 					problem with both is that neither minimize the number
 * 					  of "turns" made when it's on a grid
 * 
 * 					current shuffle() is allowed to swap a pattern with 0.
 * 					with the end result in mind, this should be allowed in 
 * 					  order to open door for players if they are stuck
 * 
 * 					new fill-1 is slower than fill-2, this is evident when
 * 					  board dimensions are in the hundreds
 * 
 * 		Nov 14, 2017
 * 					make a method that finds the minimum number of turns
 * 					  from A to A'. With that information, utilize A* 
 * 					  algorithm such that h is some max number when program
 * 					  detects that it is making more turns than necessary
 * 
 * 					final version will probably need a Board as base class,
 * 					  and then GameBorard and PathBoard as derived classes.
 * 					  Additionally, unsure of what type of objects to use 
 * 					  to implement A* algorithm yet. This is because the 
 * 					  method to find minimum of turns from A to A' requires
 * 					  writing on a new board, since overwriting the 
 * 					  gameboard itself isn't suitable, new one must be 
 * 					  made. By nature it would have different purposes and 
 * 					  therefore methods, it would be better to made two 
 * 					  derived classes from one base Board class
 * 
 * 		Nov 27, 2017
 * 					idea for marking coordinates reachable within x amount
 * 					  of turns: from the gameboard, a new board will be
 * 					  initialized with 0's and -1's, with a single 1 at the
 * 					  starting position A. Then a method called addTurn will 
 * 					  take every 1 on the board, and replace every 0 with 
 * 					  same x or y position with 1. As a result, every 1 
 * 					  indicates which coordinates can be reached with turns 
 * 					  allowed so far
 * 					
 * 		
 * 			
 * 
 */
		
	
import java.util.Scanner;

// protected
class PearTester{
	

	public static void main(String[] args) {
		
		Scanner keyboard = new Scanner(System.in);
		
		TesterBoard b1 = new TesterBoard(2, 3);
		System.out.println(b1);
		
		int[] ob1 = {4,9,14,19};
		TesterGameBoard tbSmall = new TesterGameBoard(4, 5, 5, ob1);
		
// checking if new fill can add obstacles properly
		tbSmall.fill2();
		System.out.println(tbSmall);
		tbSmall.shuffle();
		System.out.println(tbSmall);
		
// Need proper format or else minTurns must be 
// static method of main here
		
		int i = tbSmall.minTurns(0, 0, 0, 5);
		System.out.println("i = " + i);
		
		
		
		
		
		
		
// Measuring time ===================================================
/*
		long time1, time2;
		
		TesterBoard tb1 = new TesterBoard(16,12,20);
		time1 = System.currentTimeMillis();		
		tb1.fill1();
		time2 = System.currentTimeMillis();
		System.out.println("time from fill-1: " + (time2-time1));
		//System.out.println(tb1);
		

		TesterBoard tb2 = new TesterBoard(16,12,20);
		time1 = System.currentTimeMillis();
		tb2.fill2();
		time2 = System.currentTimeMillis();
		System.out.println("time from fill-2: " + (time2-time1));
		//System.out.println(tb2);
*/
// ===================================================================
		
		
/* looping shuffle()
		System.out.println("shuffle times? ");
		int shufTimes = keyboard.nextInt();
		
		time1 = System.currentTimeMillis();
		while (shufTimes>0) {
			tb2.shuffle();
			shufTimes--;
		}
		time2 = System.currentTimeMillis();
		System.out.println("time for shuffling " + shufTimes + 
				           " times: " + (time2-time1));
*/
		
//		System.out.println(tb1);
		

// ======================================================================= PLAY
/*
		System.out.println("Please enter integers separated by a space");
		System.out.println("Enter negative integer to quit");
		int intA, intB;
		int posA_H, posA_W, posB_H, posB_W;
		do {
			System.out.println("Enter coordinate pair =================");
			System.out.print("A: ");
			posA_H = keyboard.nextInt();
			posA_W = keyboard.nextInt();
			intA = tbSmall.getValue(posA_H, posA_W);
			
			System.out.print("B: ");
			posB_H = keyboard.nextInt();
			posB_W = keyboard.nextInt();
			intB = tbSmall.getValue(posB_H, posB_W);
			
			if(intA==intB) {
				tbSmall.setToEmpty(posA_H, posA_W);
				tbSmall.setToEmpty(posB_H, posB_W);
			}
			System.out.print(tbSmall);
		} while (posA_H>=0);
*/
		
		

		
		
		
		
		keyboard.close();
	}




}
