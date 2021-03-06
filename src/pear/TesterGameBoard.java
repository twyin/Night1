package pear;

import java.util.Random;

class TesterGameBoard extends TesterBoard {
	private final int maxTurns = 3;   // max amount of turns allowed is set to 3

	private int patterns;
	private int unsolvedPairs;
	private int[] obstacles; // initialized to null

// constructor of empty board
	protected TesterGameBoard(int h, int w, int p) {
		super(h,w);
		this.patterns = p;
		this.unsolvedPairs = h*w/2;
		// obstacles is null
	}
	
	protected TesterGameBoard(int h, int w, int p, int[] ob) {
		super(h,w);
		this.patterns = p;
		this.unsolvedPairs = h*w/2 - ob.length;
		this.obstacles = ob;
	}
	
	protected TesterGameBoard(int h, int w, int p, int[] ob, int[][] b) {
		super(h,w,b);
		this.patterns = p;
		this.unsolvedPairs = h*w/2 - ob.length;
		this.obstacles = ob;
	}
	
// getter's
	protected int getMaxTurns() {
		return maxTurns;
	}
	
	protected int getPatterns() {
		return this.patterns;
	}
	
	protected int getUnsolvedPairs() {
		return this.unsolvedPairs;
	}
	
/* Obstacles is the "linear" INDEX at which they which
 * For example, if desired obstacle is like:
 *   [ 0 0 -1 ]
 *   [ 0 0 -1 ]
 *   [ 0 0 -1 ]
 *   Then the desired boardArray[] used later in fill method should be:
 *   {0, 0, -1, 0, 0, -1, 0, 0, -1}
 *   Thus obstacles[] is:
 *   {2,4,6}
 */
	protected int[] getObstacle() {
		return this.obstacles;
	}

// setter's
// the number of unsolved pairs is dependent on the state of the 
//   board; it's calculated, not set to some number
	protected void setPatterns(int p) {
		this.patterns = p;
	}
	
	protected void setObstacles(int[] o) {
		this.obstacles = o;
	}
	
// toString
	public String toString() {
		int h = getHeight();
		int w = getWidth();
		
		// for personal esthetics
		// precise formula: (width*3+4 - "xGameboardx".length()) in half, then + "xGameboardx".length()
		
		String drawBoard = String.format("%"+ ((w*3-7)/2+11) +"s", 
				"xGameboardx").replace(' ', '=');
		drawBoard = String.format("%-" + (w*3+4) + "s\n", 
				drawBoard).replace(' ', '=').replace('x',' ');
		
		for (int i=0; i<h; i++) {
			drawBoard += ("[ ");
			for (int j=0; j<w; j++) {
				drawBoard += (String.format("%2d ", getValue(i,j)));
				//drawBoard += (getValue(i,j) + " ");
			}
			drawBoard+=" ]\n";
		}
		return drawBoard;
	}


// ============================================================================
// ============================================================================
/* fill-1 that can handle obstacles
 * First it makes an int array called boardArray with size equal to
 *   the total amount of spaces in the board INCLUDING the obstacles.
 *   For example, let a 3x3 board have obstacles[] of {2,5,8}
 *   Then boardArray[] will be {0,0,-1,0,0,-1,0,0,-1}
 *   Then the -1's from obstacle[] will be put into their respective
 *   spot in boardArray. Afterwards, program will generate a random 
 *   pattern and put it into two random but available locations in 
 *   boardArray[]. After boardArray[] is filled, its contents are 
 *   copied over to board[][] wherever there is no obstacle
 *   if 1st and 2nd random pattern generated is 2 then 1, and their 
 *   two random locations generated are 0,2 and 1,2 respectively
 *   -> {2,1,-1,2,1,-1,0,0,-1} 2 @ 0,3 and 1 @ 1,4
 * fill1 "shuffles" with boardArray before board is filled
 *   
 *   !! Maybe assert that both board[][] boardArray[] reach their ends
 *   
 * 
 */
	protected void fill1() {
		int w = getWidth();
		int h = getHeight();
		int total = h*w;
		// total is the number of spaces on the board INCLUDING OBSTACLES
		int obsLen;
		int[] boardArray = new int[total];	
		
		if (obstacles==null) {
			obsLen=0;
		} else {
			obsLen = obstacles.length;
		}

		// add obstacles into boardArray
		// should work even if obstacle[] isn't in ascending order
		for (int i=0; i<obsLen; i++) {
			boardArray[obstacles[i]]=-1;
		}
		
		int p = patterns;
		Random rand = new Random();
		
		int r;  
		int pos1;
		int pos2;
		
		int counter = (total-obsLen)/2;
	
		for (; counter>0; counter--) {
			r = rand.nextInt(p)+1;        // return 1-5 inclusive
			pos1 = rand.nextInt(total);   // generate[0:19]

			if (boardArray[pos1]==0) {
				boardArray[pos1]=r;
			} else {                      // else boardArray[pos1] is filled
				while(boardArray[++pos1%total]!=0) {
				// pos1%total is because ++pos1 might eventually
				//   increment it to bigger than total
				//   (imagine the last few are filled, would 
				//   need pos1 to re-route back to beginning)
				// this while loop only stops when it finds a 0, which
				//   is the next empty and let it = r
				}
				boardArray[pos1%total] = r;
			}
			
			pos2 = rand.nextInt(total);
			
			if (boardArray[pos2]==0) {
				boardArray[pos2]=r;
			} else {
				while(boardArray[++pos2%total]!=0) {
				// this will find the next empty (which is 0)
				}
				boardArray[pos2%total] = r;
			}
		}
			
		for (int i=0; i<h; i++) {
			for (int j=0; j<w; j++) {
				setValue(i,j, boardArray[i*w+j]);
//				board[i][j]=boardArray[i*w+j];
			}
		}
	}
// ============================================================================
// ============================================================================


// ============================================================================
// ============================================================================
/* fill-2 that can handle obstacles
 * Logically it's still the same as before, visual example of new fill-2
 *   given obstacles[] = {2,4,6} on a 3x3 board
 *   [ 0 0 -1 ]
 *   [ 0 0 -1 ] + obstacles={2,4,6}  --> {0,0,-1,0,0,-1,0,0,-1}
 *   [ 0 0 -1 ]
 *   create array same size as empty spaces; so boardArray[6]
 *   for every obstacle, put -1 in boardArray where it should go,
 *   which would be boardArray[2], [4], and [6] for the above example
 *   put a pattern in boardArry[i] based on j; what they are is 
 *   explained where they are declared below
 *   j/2%patterns+1 explained : "+ 1" is so that the patterns would 
 *   start at 1 and not 0
 *   AS LONG AS THE AMOUNT OF EMPTY SPACES ON BOARD IS EVEN,
 *   this formula also assures that they would appear in pairs!
 *   That's because since i/2 = (i+1)/2 if i is even and the first
 *   index is 0 which is even
 *   (if it was only i%patterns, program wouldn't be able to confirm 
 *   they're in even amounts)
 *   {0,0,-1,0,0,-1,0,0,-1} -> {1,1,-1,2,2,-1,3,3,-1}
 *   afterwards, swap every pattern with another and put into board
 * fill2 has a loop at the end that swaps elements of boardArray before
 *   putting it in the board. Therefore it is shuffle before board is
 *   filled. 
 *   
 */
	protected void fill2() {
		int h = getHeight();
		int w = getWidth();
		int total = h*w;
		int[] boardArray = new int[total];
		
		int i;               // general variable used in for loops
		int j=0;             // counts the empty spaces in boardArray
		int obsLen;          // length of obstacle[]
		int pos;
		int temp;
		boolean swapped;
		Random rand = new Random();
		
		if (this.obstacles==null) {
		// if obstacles[] = null, its length is undefined -> program crashes
			obsLen=0;
		} else {
			obsLen = obstacles.length;
		}
		
		for (i=0; i<obsLen; i++) {
		// obstacles holds the indexes in boardArray that will be -1
			boardArray[obstacles[i]] = -1;
		}
		
		for (i=0; i<total; i++) {
		// reason for two counters, j to go down boardArray, j to count
		//   empty spaces. This is so no new value is assigned to obstacle
			if (boardArray[i] != -1) {
				boardArray[i] = j/2%patterns+1;
				j++;
			}
		}
		// for a 5x4 board with 4 obstacles, j=16 by end of ^ loop
		
		for (i=0; i<total; i++) {
			// swap boardArray[counter] and [pos]
			swapped=false;
			if (boardArray[i]!=-1) {
				while (swapped==false) {
					pos = rand.nextInt(total);
					if (boardArray[pos]!=-1) {
						temp = boardArray[i];
						boardArray[i] = boardArray[pos];
						boardArray[pos] = temp;
						swapped=true;
					}
				}
			}
		}
		
		for (i=0; i<h; i++) {
			for (j=0; j<w; j++) {
				setValue(i, j, boardArray[i*w+j]);
//				board[i][j]=boardArray[i*w+j];
			}
		}
	}
// ============================================================================
// ============================================================================

	
// ============================================================================
// ============================================================================
// shuffle the integers in board[][]
/* Note: if for some reason there are 0's on the board, current 
 *   shuffle is allow to swap pattern with a 0
 */
	protected void shuffle() {
		int h = getHeight();
		int w = getWidth();
		
		int posH;
		int posW;
		int temp;
		Random rand = new Random();
		System.out.println("\nfrom shuffle()");
		
		boolean swapped;
		for (int i=0; i<h; i++) {
			for (int j=0; j<w; j++) {
				swapped = false;
				// swap with a random if neither are obstacles, else
				//   pick another place to swap the original with
				
				if (getValue(i,j) != -1) {
//					if (board[i][j]!=-1) {
				//if (board[i][j]>0) {
				// ^ switch to this if condition if shuffle must keep
				//  the "shape" of occupied spaces!
				// with (board[i][j]!=-1), shuffle is allowed to swap
				// patterns with 0's
					try {
						while (swapped==false) {
							posW = rand.nextInt(w);
							posH = rand.nextInt(h);
							
							if (getValue(posH, posW) != -1) {
//								if (board[posH][posW]!= -1 ) {
							//if (board[posH][posW] > 0 ) {
							// ^ switch to this if condition if shuffle must keep
							//  the "shape" of occupied spaces
								temp = getValue(i, j);
//									temp = board[i][j];
								setValue(i, j, getValue(posH, posW));
//									board[i][j] = board[posH][posW];
								setValue(posH, posW, temp);
//									board[posH][posW] = temp;
								swapped=true;
							}
						}
					} catch (Exception e) {
						System.out.println(e.getMessage());
						System.out.println("Error from shuffle while "
								+ "attempting to swap at (i,j): "
								+ "(" + i + "," + j + ")");
					}
				}
			}
		}		
	}
// ============================================================================
// ============================================================================

		

// minTurns should be a method of GameBoard that return minimum
//   number of turns from A to A'
	protected int minTurns(int y1, int x1, int y2, int x2) {
		// (y1,x1) is A, (y2,x2) is A'
		int height = this.getHeight() + 2;
		int width = this.getWidth() + 2;
		int posH = y1++;        // correct offset from outside border
		int posW = x1++;
		int minTurn = 0;
		int addedTurns = 0;
		boolean found = false;
// make an empty board with extra zero's around first, and -1 
//   in place of patterns
		TesterPathBoard pb = new TesterPathBoard(height, width);
		pb.pave(this, y1, x1, y2, x2);
		
		
		// shouldn't have anything other than -1/0/1's on it so 
		//   not necessary to make it a new Game board
		

// index for paths board is different than that of gameboard:
// -1 is obstacles and patterns, things that a path can't travel thru
// 0 is open for travel
// 1 marks those within reach of x turns
		
// first mark board with coordinates that are reachable within 0 turn,
//   then check if there's a 1 at (y2, x2)
// repeat 1 turn
// repeat 2 turns
// repeat 3 turns
		
//		pb.addTurn();
		
		int turnMarker = 1;  
		// turnMarker marks turns reachable within 0 turn with 1, 1 turn with 2, etc
		int maxT = this.getMaxTurns();
// remove comment indicator when addTurn() works properly;
// SHOULD be able to actual minimum of turns needed
		while (found==false && addedTurns <= maxT) {
//			System.out.println("turnMarker = " + turnMarker);
			pb.addTurn(turnMarker);
//
			//System.out.println("With " + addedTurns + " turns");
			//System.out.println(pb);
			if (pb.getValue(y2+1, x2+1) > 0 ) {
				found=true;
			} else {
				++addedTurns;
			}
			turnMarker++;
		}

		if (found==true) {
			return addedTurns;
		}
		// else
		return maxT+1;
	
	}
}




