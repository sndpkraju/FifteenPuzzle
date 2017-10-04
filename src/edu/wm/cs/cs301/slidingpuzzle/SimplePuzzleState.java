package edu.wm.cs.cs301.slidingpuzzle;

import java.util.Arrays;
import java.util.Random;

public class SimplePuzzleState implements PuzzleState {
	
	private Operation operation;
	private SimplePuzzleState parent;
	private int[][] puzzle;
	private int pathLength;
	private int temp;
	private Operation prevOp;
	
	public SimplePuzzleState() {
		super();
		this.parent = null;
		this.operation = null;
		this.puzzle = null;
	}

	@Override
	public void setToInitialState(int dimension, int numberOfEmptySlots) {
		// TODO Auto-generated method stub
		puzzle = new int[dimension][dimension];
		int n = 1;
		for (int row = 0; row < dimension; row++){
			for (int col = 0; col < dimension; col++) {
				puzzle[row][col] = n;
				n++;
				if (row == dimension - 1 && col >= dimension - numberOfEmptySlots) {
					puzzle[row][col] = 0;
				}
			}
		}
		

	}

	@Override
	public int getValue(int row, int column) {
		// TODO Auto-generated method stub
		if (row >= 0 && column >= 0 && row < puzzle.length && column < puzzle.length) {
			return puzzle[row][column];
		}
		return 0;
	}

	@Override
	public PuzzleState getParent() {
		// TODO Auto-generated method stub
		return parent;
	}

	@Override
	public Operation getOperation() {
		// TODO Auto-generated method stub
		return operation;
	}

	@Override
	public int getPathLength() {
		// TODO Auto-generated method stub
		return pathLength;
	}

	@Override
	public PuzzleState move(int row, int column, Operation op) {
		// TODO Auto-generated method stub
		if (getValue(row, column) == 0) {
			return null;
		}
		
		SimplePuzzleState newState = new SimplePuzzleState();
		newState.puzzle = new int[puzzle.length][puzzle.length];
		for (int r = 0; r < puzzle.length; r++){
			for (int c = 0; c < puzzle.length; c++){
				newState.puzzle[r][c] = this.getValue(r, c);
			}
		}
		
		//System.out.println("1im here ");
		if (op == Operation.MOVERIGHT) {
			//System.out.println("2im here right");
			newState = moveTileRight(row, column, 1, newState);
			
		}
		else if (op == Operation.MOVELEFT) {
			//System.out.println("3im here left");
			newState = moveTileLeft(row, column, 1, newState);
		}
		
		else if (op == Operation.MOVEUP) {
			//System.out.println("4im here up");
			newState = moveTileUp(row, column, 1, newState);
		}
		
		else if (op == Operation.MOVEDOWN) {
			//System.out.println("5im here down");
			newState = moveTileDown(row, column, 1, newState);
		}
		
		if (newState == null) {
			return null;
		}
		else {
			newState.operation = op;
			newState.parent = this;
			newState.pathLength = this.pathLength + 1;
		}
		
	return newState;
		
		
	}
	
	private SimplePuzzleState moveTileRight(int row, int column, int n, SimplePuzzleState newState){
		if (column + 1 < puzzle.length){
			if (isEmpty(row, column + 1)){
				int temp= newState.puzzle[row][column];
				newState.puzzle[row][column] = 0;
				newState.puzzle[row][column + 1] = temp;
			}
			else {
				return null;
			}
			
		}
		else {
			return null;
		}
		return newState;
	}
	
	private SimplePuzzleState moveTileLeft(int row, int column, int n, SimplePuzzleState newState){
		if (column - 1 >= 0){
			if (isEmpty(row, column - 1)){
				int temp= newState.puzzle[row][column];
				newState.puzzle[row][column] = 0;
				newState.puzzle[row][column - 1] = temp;
			}
			else {
				return null;
			}
			
		}
		else {
			return null;
		}
		return newState;
	}
	
	private SimplePuzzleState moveTileUp(int row, int column, int n, SimplePuzzleState newState){
		if (row - 1 >= 0){
			if (isEmpty(row - 1, column)){
				int temp = newState.puzzle[row][column];
				newState.puzzle[row][column] = 0;
				newState.puzzle[row - 1][column] = temp;
			}
			else {
				return null;
			}
			
		}
		else {
			return null;
		}
		return newState;
	}
	
	private SimplePuzzleState moveTileDown(int row, int column, int n, SimplePuzzleState newState){
		if (row + 1 < puzzle.length){
			if (isEmpty(row + 1, column)){
				int temp = newState.puzzle[row][column];
				newState.puzzle[row][column] = 0;
				newState.puzzle[row + 1][column] = temp;
			}
			else {
				return null;
			}
			
		}
		else {
			return null;
		}
		return newState;
	}

	@Override
	public PuzzleState drag(int startRow, int startColumn, int endRow, int endColumn) {
		// TODO Auto-generated method stub
		

		
		if (isEmpty(startRow, startColumn)){
			return null;
		}
		if (!isEmpty(endRow, endColumn)){
			return null;
		}
		
		int rowSpace = endRow - startRow;
		int colSpace = endColumn - startColumn;
		
		PuzzleState newState = new SimplePuzzleState();
		newState = this;
		
		while (rowSpace != 0 || colSpace != 0){
			
			if (isEmpty(startRow, startColumn + 1) && colSpace > 0) {
				newState = newState.move(startRow, startColumn, Operation.MOVERIGHT);
				startColumn = startColumn + 1;
				colSpace = colSpace - 1;
				
			}
			if (isEmpty(startRow, startColumn - 1) && colSpace < 0) {
				newState =  newState.move(startRow, startColumn, Operation.MOVELEFT);
				startColumn = startColumn- 1;
				colSpace = colSpace + 1;
				
			}
			if (isEmpty(startRow + 1, startColumn) && rowSpace > 0) {
				newState = newState.move(startRow, startColumn, Operation.MOVEDOWN);
				startRow = startRow + 1;
				rowSpace = rowSpace - 1;
				
			}
			if (isEmpty(startRow - 1, startColumn) && rowSpace < 0) {
				newState =  newState.move(startRow, startColumn, Operation.MOVEUP);
				startRow = startRow - 1;
				rowSpace = rowSpace + 1;
				
			}
			
			
		}
		
		return newState;
	}

	@Override
	public PuzzleState shuffleBoard(int pathLength) {
		//int n = 0;
		SimplePuzzleState newState = new SimplePuzzleState();
		Operation tempOp = null;
		Boolean flag = null;
		
		newState.puzzle = new int[puzzle.length][puzzle.length];
		for (int i = 0; i < puzzle.length; i++){
			for (int j = 0; j < puzzle.length; j++){
				newState.puzzle[i][j] = this.getValue(i, j);
			}
		}

		//while(n < pathLength){
		for(int n = 0; n < pathLength;) {
			Operation op = newState.randomOp();
			flag = previousMove(tempOp, op);
			while(flag == false) {
				op = newState.randomOp();
				flag = previousMove(tempOp, op);
			}
			
			//System.out.println(prevOp);
			int[] emptySpot = newState.randomEmpty();
			int r = emptySpot[0];
			int c = emptySpot[1];
			
			if(op == Operation.MOVERIGHT) {
				c--;
			}
			if(op == Operation.MOVELEFT) {
				c++;
			}
			if(op == Operation.MOVEUP) {
				r++;
			}
			if(op == Operation.MOVEDOWN) {
				r--;
			}
			if((r >= 0 && r < puzzle.length) && (c >= 0 && c < puzzle.length)){
				if (newState.getValue(r, c) > 0){
				newState = (SimplePuzzleState) newState.move(r, c, op);
				//newState.operation = op;
				tempOp = op;
				n = n + 1;	
				}
			}
		
		}
	return newState;
	
	}
	
	private boolean previousMove(Operation op, Operation ops) {
		if (op != null) {
			if((op.equals(Operation.MOVERIGHT)) && (ops.equals(Operation.MOVELEFT))){
				return false;
			}
			if((op.equals(Operation.MOVELEFT)) && (ops.equals(Operation.MOVERIGHT))){
				return false;
			}
			if((op.equals(Operation.MOVEUP)) && (ops.equals(Operation.MOVEDOWN))){
				return false;
			}
			if((op.equals(Operation.MOVEDOWN)) && (ops.equals(Operation.MOVEUP))){
				return false;
			}
		}
		return true;
	}
	
	private Operation randomOp(){
		
		Operation op = null;
		
		Random randomGenerator = new Random();
		int randomNumber = randomGenerator.nextInt(4);
		
		
		
		if(randomNumber == 0) {
			//System.out.println(prevOp + "right");
			op = Operation.MOVERIGHT;
		}
		if(randomNumber == 1) {
			//System.out.println(prevOp + "left");
			op = Operation.MOVELEFT;
		}
		if(randomNumber == 2 ) {
			//System.out.println(prevOp + "up");
			op = Operation.MOVEUP;
		}
		if(randomNumber == 3) {
			//System.out.println(prevOp + "down");
			op = Operation.MOVEDOWN;
		}
		
		
		return op;
		
	}
	
	private int[] randomEmpty(){

		int[] empty1 = null;
		int[] empty2 = null;
		int[] empty3 = null;
		
		
		int n = 0;
		Random randomGenerator = new Random();
		int randomNumber = 0;
		
		for (int r = 0; r < puzzle.length; r++){
			for (int c = 0; c < puzzle.length; c++){
				if (isEmpty(r, c)){
					if (n == 0){
						empty1 = new int[2];
						empty1[0] = r;
						empty1[1] = c;
					}
					else if (n == 1){
						empty2 = new int[2];
						empty2[0] = r;
						empty2[1] = c;
					}
					else if (n == 2){
						empty3 = new int[2];
						empty3[0] = r;
						empty3[1] = c;
					}
					n++;
				}
			}
		}
	if (n == 1){
		return empty1;
	}
	else if (n == 2){
		randomNumber = randomGenerator.nextInt(2);
	}
	else {
		randomNumber = randomGenerator.nextInt(3);	
	}
	
	if(randomNumber == 0) {
		return empty1;
	}
	if(randomNumber == 1) {
		return empty2;
	}
	if(randomNumber == 2) {
		return empty3;
	}
	return null;
	}


	@Override
	public boolean isEmpty(int row, int column) {
		// TODO Auto-generated method stub
		if (row >= 0 && column >= 0 && row < puzzle.length && column < puzzle.length){	
			if (this.puzzle[row][column] == 0) {
				return true;
			}
		}
		
			return false;
		
	}
	
	@Override
	public PuzzleState getStateWithShortestPath() {
		// TODO Auto-generated method stub
		return this;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		//result = prime * result + ((operation == null) ? 0 : operation.hashCode());
		//result = prime * result + ((parent == null) ? 0 : parent.hashCode());
		//result = prime * result + pathLength;
		result = prime * result + Arrays.deepHashCode(puzzle);
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	/*public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SimplePuzzleState other = (SimplePuzzleState) obj;
		//if (operation != other.operation)
		//	return false;
		//if (parent == null) {
		//	if (other.parent != null)
		//		return false;
		//} else if (!parent.equals(other.parent))
		//	return false;
		//if (pathLength != other.pathLength)
		//	return false;
		if (!Arrays.deepEquals(puzzle, other.puzzle))
			return false;
		return true;
	}*/
	
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SimplePuzzleState other = (SimplePuzzleState) obj;
		if (!Arrays.deepEquals(puzzle, other.puzzle))
			return false;
			
		return true;
	}

}

