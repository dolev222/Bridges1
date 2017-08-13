import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class Board {
	String [][] board;
	int [][] data;
	int [][] dataValues;
	String [][] dataIndexes;
	int [][] neighbours;
	ArrayList<int[]>[] directionList;

	public Board(int [][] data, int size) {
		this.directionList = new ArrayList[data.length];
		this.neighbours = new int [data.length][4];
		for(int i=0; i<data.length; i++) {
			for(int j=0; j<4; j++) {
				this.neighbours[i][j] = -1;
			}
		}
		this.data = data;
		Arrays.sort(this.data, (a, b) -> Integer.compare(b[2], a[2]));

		this.board = new String [size][size];
		this.dataValues = new int [size][size];

		for(int i=0; i<this.board.length; i++) {
			for(int j=0; j<this.board.length; j++) {
				this.board[i][j] = " ";
			}
		}

		for(int i=0; i<data.length; i++) {
			this.board[data[i][0]][data[i][1]] = data[i][2]+"";
			this.dataValues[data[i][0]][data[i][1]] = data[i][2];
		}



		this.dataIndexes = new String [size][size];


		for(int i=0; i<this.dataIndexes.length; i++) {
			for(int j=0; j<this.dataIndexes.length; j++) {
				this.dataIndexes[i][j] = " ";
			}
		}

		for(int i=0; i<data.length; i++) {
			this.dataIndexes[this.data[i][0]][this.data[i][1]] = i+"";
		}
		printDataIndexes();

		pp();
		printMatchesBridges();
		//System.out.println("-----------------AND----------------");
		//System.out.println("--------------CROSSES:-------------");
		findCross();
		//printDataIndexes();


	}

	public void printBoard() {
		for(int i=0; i<this.board.length; i++) {
			for(int j=0; j<this.board.length; j++) {
				System.out.print(this.board[i][j] + " ");
			}
			System.out.println();
		}
	}

	public void printData() {
		for(int i=0; i<this.dataValues.length; i++) {
			for(int j=0; j<this.dataValues.length; j++) {
				System.out.print(this.dataValues[i][j] + " ");
			}
			System.out.println();
		}
	}

	public void printDataIndexes() {
		for(int i=0; i<this.dataIndexes.length; i++) {
			for(int j=0; j<this.dataIndexes.length; j++) {
				System.out.print(this.dataIndexes[i][j] + " ");
			}
			System.out.println();
		}
	}

	public void safeBridges() {

		boolean hasChanged = true;
		int isLeft, isRight, isUp, isDown;
		while(hasChanged) {
			hasChanged = false;
			for(int i=0; i<this.data.length; i++) {
				if(this.dataValues[this.data[i][0]][this.data[i][1]] > 0) {

					isLeft = isLeft(i);
					isRight = isRight(i);
					isUp = isUp(i);
					isDown = isDown(i);


					int directions = (isLeft + isRight + isUp + isDown);

					//					if(this.dataValues[this.data[i][0]][this.data[i][1]] - directions > 0) {
					//						System.out.println("No Solution");
					//						return;
					//					}

					if(directions != 0) {
						if(this.dataValues[this.data[i][0]][this.data[i][1]] - directions == 0) {
							this.dataValues[this.data[i][0]][this.data[i][1]] -= directions;
							setBridges(i, isLeft, isRight, isUp, isDown);

							hasChanged = true;
						}

						else {
							int counter = 0;
							if(this.dataValues[this.data[i][0]][this.data[i][1]] - isRight - isLeft - isDown > 0) {
								setBridgesUp(i, 1);
								counter++;
							}
							if(this.dataValues[this.data[i][0]][this.data[i][1]] - isUp - isLeft - isDown > 0) {
								setBridgesRight(i, 1);
								counter++;
							}
							if(this.dataValues[this.data[i][0]][this.data[i][1]] - isRight - isUp - isDown > 0) {
								setBridgesLeft(i, 1);
								counter++;
							}
							if(this.dataValues[this.data[i][0]][this.data[i][1]] - isRight - isLeft - isUp > 0) {
								setBridgesDown(i, 1);
								counter++;
							}

							this.dataValues[this.data[i][0]][this.data[i][1]] -= counter;
							if(counter != 0) hasChanged = true;

						}



						//						else {
						//							if(isLeft != 0) isLeft = 1;
						//							if(isRight != 0) isRight = 1;
						//							if(isUp != 0) isUp = 1;
						//							if(isDown != 0) isDown = 1;
						//							int newDirections = (isLeft + isRight + isUp + isDown);
						//							if ((this.dataValues[this.data[i][0]][this.data[i][1]] == 1 && newDirections == 1) ||
						//									(this.dataValues[this.data[i][0]][this.data[i][1]] == 3 && newDirections == 2) ||	
						//									(this.dataValues[this.data[i][0]][this.data[i][1]] == 5 && newDirections == 3) ||
						//									(this.dataValues[this.data[i][0]][this.data[i][1]] == 7 && newDirections == 4)) {
						//
						//								this.dataValues[this.data[i][0]][this.data[i][1]] -= newDirections;
						//								setBridges(i, isLeft, isRight, isUp, isDown);
						//								hasChanged = true;
						//							}
						//						}
					}
				}		
			}
		}
		updateData();
	}

	public int chooseBridge() {



		safeBridges();


		if(checkSolution()) {
			printBoard();
			System.out.println("DONE!");
			System.exit(0);
			return 0;
		}

		else {


			//System.out.println("abbbbbbbbbbbbbbbbbb");
			int size = this.board.length;
			String [][] backUpBoard = new String [size][size];
			int [][] backUpDataValues = new int [size][size];
			int [][] backUpData = new int [this.data.length][3];
			backUps(backUpBoard, backUpDataValues, backUpData);





			Arrays.sort(this.data, (a, b) -> Integer.compare(a[2], b[2]));
			int i=0;
			while(data[i][2] == 0) { 
				i++;
				if(i == this.data.length) return 0;
			}

			int isLeft, isRight, isUp, isDown;
			//for(int j=0; j<4; j++) {
			//switch(j) {
			//case 2:
			isLeft = isLeft(i);
			if(isLeft > 0) {
				this.dataValues[this.data[i][0]][this.data[i][1]] -= 1;
				setBridgesLeft(i, 1);
				chooseBridge();

			}
			//case 1:
			isRight = isRight(i);
			if(isRight > 0) {
				this.dataValues[this.data[i][0]][this.data[i][1]] -= 1;
				setBridgesRight(i, 1);
				chooseBridge();

			}
			//case 0:
			isUp = isUp(i);
			if(isUp > 0) {
				this.dataValues[this.data[i][0]][this.data[i][1]] -= 1;
				setBridgesUp(i, isUp);
				chooseBridge();

			}
			//case 3:
			isDown = isDown(i);
			if(isDown > 0) {
				this.dataValues[this.data[i][0]][this.data[i][1]] -= 1;
				setBridgesDown(i, isDown);
				chooseBridge();

			}
			//default:
			restores(backUpBoard, backUpDataValues, backUpData);
			return 0;

			//}
			//safeBridges();
			//updateData();
			//			if(checkSolution()) {
			//				printBoard();
			//				System.out.println("DONE!");
			//				return;
			//			}
			//			else {
			//				restores(backUpBoard, backUpDataValues, backUpData);
			//			}
			//}
		}

	}


	public void solve() {
		printBoard();
		//safeBridges();
		//printBoard();
		System.out.println();
		chooseBridge();

		if(!checkSolution()) System.out.println("NO SOLUTION!");
	}


	public void backUps(String [][] backUpBoard, int [][] backUpDataValues, int [][] backUpData) {
		int size = this.board.length;
		for(int i=0; i<size; i++) {
			for(int j=0; j<size; j++) {
				backUpBoard[i][j] = this.board[i][j];
				backUpDataValues[i][j] = this.dataValues[i][j];
			}
		}

		for(int i=0; i<this.data.length; i++) {
			for(int j=0; j<3; j++) {
				backUpData[i][j] = this.data[i][j];
			}
		}
	}

	public void restores(String [][] backUpBoard, int [][] backUpDataValues, int [][] backUpData) {
		int size = this.board.length;
		for(int i=0; i<size; i++) {
			for(int j=0; j<size; j++) {
				this.board[i][j] = backUpBoard[i][j];
				this.dataValues[i][j] = backUpDataValues[i][j];
			}
		}

		for(int i=0; i<this.data.length; i++) {
			for(int j=0; j<3; j++) {
				this.data[i][j] = backUpData[i][j];
			}
		}
	}

	public void updateData() {
		for(int z=0; z<this.data.length; z++) {
			this.data[z][2] = this.dataValues[this.data[z][0]][this.data[z][1]];
		}
	}


	public boolean checkSolution() {
		for(int i=0; i<this.data.length; i++) {
			if(this.data[i][2] != 0) return false;
		}
		int[] check = new int[this.data.length];
		checkConnections(check,0);
		for(int i=0; i<check.length; i++) {
			if(check[i] == 0) return false;
		}
		return true;
	}

	public void checkConnections(int [] check, int i) {

		check[i] = 1;

		int Up = whoConnectedUp(this.data[i][0], this.data[i][1]);
		if(Up != -1 && check[Up] ==0) checkConnections(check, Up);

		int Right = whoConnectedRight(this.data[i][0], this.data[i][1]);
		if(Right != -1 && check[Right] ==0) checkConnections(check, Right);

		int Down = whoConnectedDown(this.data[i][0], this.data[i][1]);
		if(Down != -1 && check[Down] ==0) checkConnections(check, Down);

		int Left = whoConnectedLeft(this.data[i][0], this.data[i][1]);
		if(Left != -1 && check[Left] ==0) checkConnections(check, Left);

		return;

	}

	public int whoConnectedUp(int line, int row) {
		line--;
		while(line != -1 && (this.board[line][row] == "|" || this.board[line][row] == "u")) {
			line--;
		}
		return getDataIndex(line, row);
	}

	public int whoConnectedRight(int line, int row) {
		row++;
		while(row != this.board.length && (this.board[line][row] == "=" || this.board[line][row] == "-")) {
			row++;
		}
		return getDataIndex(line, row);
	}

	public int whoConnectedDown(int line, int row) {
		line++;
		while(line != this.board.length && (this.board[line][row] == "|" || this.board[line][row] == "u")) {
			line++;
		}
		return getDataIndex(line, row);
	}

	public int whoConnectedLeft(int line, int row) {
		row--;
		while(row !=-1 && (this.board[line][row] == "=" || this.board[line][row] == "-")) {
			row--;
		}
		return getDataIndex(line, row);
	}

	public int isLeft(int i) {
		int line = this.data[i][0];
		int row = this.data[i][1];
		row--;
		int numberOfAvailableBridges = 0;
		if(row>=0) {
			if(this.board[line][row] == "-")
				numberOfAvailableBridges = 1;
			else if(this.board[line][row] == "=") return 0;
			else if(this.board[line][row] == " ")
				numberOfAvailableBridges = 2;
		}

		while(row>=0) {

			if(this.board[line][row] == " " || this.board[line][row] == "-") 
				row--;
			else if(this.board[line][row] == "|" || this.board[line][row] == "u")
				return 0;
			else if(this.dataValues[line][row] !=0) 
				return Math.min(this.dataValues[line][row], numberOfAvailableBridges);
			else return 0;
		}
		return 0;
	}


	public int isRight(int i) {
		int line = this.data[i][0];
		int row = this.data[i][1];
		row++;
		int numberOfAvailableBridges = 0;
		if(row<this.board.length) {
			if(this.board[line][row] == "-")
				numberOfAvailableBridges = 1;
			else if(this.board[line][row] == "=") return 0;
			else if(this.board[line][row] == " ")
				numberOfAvailableBridges = 2;
		}
		while(row<this.board.length) {
			if(this.board[line][row] == " " || this.board[line][row] == "-") 
				row++;
			else if(this.board[line][row] == "|" || this.board[line][row] == "u")
				return 0;
			else if(this.dataValues[line][row] !=0) 
				return Math.min(this.dataValues[line][row], numberOfAvailableBridges);
			else return 0;
		}
		return 0;
	}

	public int isUp(int i) {
		int line = this.data[i][0];
		int row = this.data[i][1];
		line--;
		int numberOfAvailableBridges = 0;
		if(line>=0) {
			if(this.board[line][row] == "|")
				numberOfAvailableBridges = 1;
			else if(this.board[line][row] == "u") return 0;
			else if(this.board[line][row] == " ")
				numberOfAvailableBridges = 2;
		}
		while(line>=0) {

			if(this.board[line][row] == " " || this.board[line][row] == "|") 
				line--;
			else if(this.board[line][row] == "-" || this.board[line][row] == "=")
				return 0;
			else if(this.dataValues[line][row] !=0) 
				return Math.min(this.dataValues[line][row], numberOfAvailableBridges);
			else return 0;
		}
		return 0;
	}

	public int isDown(int i) {
		int line = this.data[i][0];
		int row = this.data[i][1];
		line++;
		int numberOfAvailableBridges = 0;
		if(line<this.board.length) {
			if(this.board[line][row] == "|")
				numberOfAvailableBridges = 1;
			else if(this.board[line][row] == "u") return 0;
			else if(this.board[line][row] == " ")
				numberOfAvailableBridges = 2;
		}
		while(line<this.board.length) {
			if(this.board[line][row] == " " || this.board[line][row] == "|") 
				line++;
			else if(this.board[line][row] == "-" || this.board[line][row] == "=")
				return 0;
			else if(this.dataValues[line][row] !=0) 
				return Math.min(this.dataValues[line][row], numberOfAvailableBridges);
			else return 0;
		}
		return 0;
	}

	public void setBridges(int i, int isLeft, int isRight, int isUp, int isDown) {
		if(isLeft > 0) setBridgesLeft(i, isLeft);
		if(isRight > 0) setBridgesRight(i, isRight);
		if(isUp > 0) setBridgesUp(i, isUp);
		if(isDown > 0) setBridgesDown(i, isDown);
	}

	public void setBridgesLeft(int i, int numberOfBridges) {
		String bridge = "=";
		if(numberOfBridges == 1) bridge = "-";
		int line = this.data[i][0];
		int row = this.data[i][1];
		row--;
		while(row >= 0) {
			if(this.board[line][row] == " ") { 
				this.board[line][row] = bridge;
				row--;
			}
			else if(this.board[line][row] == "-") {
				this.board[line][row] = "=";
				row--;
			}
			else if(this.board[line][row] != "=") {
				this.dataValues[line][row] -= numberOfBridges;
				break;
			}
			else break;
		}
	}

	public void setBridgesRight(int i, int numberOfBridges) {
		String bridge = "=";
		if(numberOfBridges == 1) bridge = "-";
		int line = this.data[i][0];
		int row = this.data[i][1];
		row++;
		while(row < this.board.length) {
			if(this.board[line][row] == " ") { 
				this.board[line][row] = bridge;
				row++;
			}
			else if(this.board[line][row] == "-") {
				this.board[line][row] = "=";
				row++;
			}
			else if(this.board[line][row] != "=") {
				this.dataValues[line][row] -= numberOfBridges;
				break;
			}
			else break;
		}
	}

	public void setBridgesUp(int i, int numberOfBridges) {
		String bridge = "u";
		if(numberOfBridges == 1) bridge = "|";
		int line = this.data[i][0];
		int row = this.data[i][1];
		line--;
		while(line >= 0) {
			if(this.board[line][row] == " ") { 
				this.board[line][row] = bridge;
				line--;
			}
			else if(this.board[line][row] == "|") {
				this.board[line][row] = "u";
				line--;
			}
			else if(this.board[line][row] != "u") {
				this.dataValues[line][row] -= numberOfBridges;
				break;
			}
			else break;
		}
	}

	public void setBridgesDown(int i, int numberOfBridges) {
		String bridge = "u";
		if(numberOfBridges == 1) bridge = "|";
		int line = this.data[i][0];
		int row = this.data[i][1];
		line++;
		while(line < this.board.length) {
			if(this.board[line][row] == " ") { 
				this.board[line][row] = bridge;
				line++;
			}
			else if(this.board[line][row] == "|") {
				this.board[line][row] = "u";
				line++;
			}
			else if(this.board[line][row] != "u") {
				this.dataValues[line][row] -= numberOfBridges;
				break;
			}
			else break;
		}
	}




	public void pp() {
		int isLeft, isRight, isUp, isDown, neighborLeft=-1, neighborRight=-1, neighborUp=-1, neighborDown=-1, firstFlag;
		int [] args; 
		int [] chosenArgs;

		for(int i=0; i<this.data.length; i++) {



			ArrayList<int[]> params = new ArrayList<int[]>();


			firstFlag = 1;

			neighborLeft=-1; neighborRight=-1; neighborUp=-1; neighborDown=-1;

			args = new int [12];
			isLeft = isLeft(i);
			isRight = isRight(i);
			isUp = isUp(i);
			isDown = isDown(i);

			int line = this.data[i][0];
			int row = this.data[i][1];

			if(isUp > 0) {
				neighborUp = getUpNeighborIndex(line, row);
				this.neighbours[i][0] = neighborUp;
				args[1] = 1;
				if(isUp == 2) args[2] = 2;
			}

			if(isRight > 0) {
				neighborRight = getRightNeighborIndex(line, row);
				this.neighbours[i][1] = neighborRight;
				args[4] = 1;
				if(isRight == 2) args[5] = 2;
			}

			if(isDown > 0) {
				neighborDown = getDownNeighborIndex(line, row);
				this.neighbours[i][2] = neighborDown;
				args[7] = 1;
				if(isDown == 2) args[8] = 2;
			}

			if(isLeft > 0) {
				neighborLeft = getLeftNeighborIndex(line, row);
				this.neighbours[i][3] = neighborLeft;
				args[10] = 1;
				if(isLeft == 2) args[11] = 2;
			}

			for(int a=0; a<=2; a++) {
				chosenArgs = new int [12];
				int directions = this.data[i][2];

				if(args[1] == 0 && args[2] == 0) a=2;

				directions -= args[a];

				//if(args[a] != 0) chosenArgs[a] = 1;
				//else chosenArgs[0] = 1;


				for(int b=3; b<=5; b++) {

					if(args[4] == 0 && args[5] == 0) b=5;

					directions -=args[b];
					//if(args[b] != 0) chosenArgs[b] = 1;
					//else chosenArgs[3] = 1;


					for(int c=6; c<=8; c++) {

						if(args[7] == 0 && args[8] == 0) c=8;

						directions -=args[c];
						//if(args[c] != 0) chosenArgs[c] = 1;
						//else chosenArgs[6] = 1;


						for(int d=9; d<=11; d++) {

							if(args[10] == 0 && args[11] == 0) d=11;

							directions -=args[d];
							//if(args[d] != 0) chosenArgs[d] = 1;
							//else chosenArgs[9] = 1;

							if(directions == 0) {


								params.add(new int []{i, args[a], args[b], args[c], args[d]});
								//printChosens(chosenArgs, i, neighborUp, neighborRight, neighborDown, neighborLeft);
							}
							directions += args[d];
							//chosenArgs[d] = 0;
						}
						directions+= args[c];
						//chosenArgs[c] = 0;
					}
					directions+=args[b];
					//chosenArgs[b] = 0;
				}
				directions+=args[a];
				//chosenArgs[a] = 0;
			}

			this.directionList[i] = params;
			printListParams(params);


			System.out.println();

			if(i+1 != this.data.length) {
				System.out.println("-----------------AND----------------");
			}
		}
	}

	public void printListParams(ArrayList<int[]> params) {
		Iterator<int[]> it = params.iterator();

		System.out.print("[");
		while(it.hasNext()) {
			int [] curr = it.next();
			System.out.print("X(" + curr[0] + ", " + curr[1] + ", " + curr[2] + ", " + curr[3] + ", " + curr[4] + ")");
			if(it.hasNext()) System.out.print(" OR ");
			else System.out.println("]");
		}


		for(int i=0; i<params.size(); i++) {
			int [] curr = params.get(i);
			for(int j=i+1; j<params.size(); j++) {

				int [] curr2 = params.get(j);

				System.out.println("-----AND-----");
				System.out.println("[NOT-X(" + curr[0] + ", " + curr[1] + ", " + curr[2] + ", " + curr[3] + ", " + curr[4] + ")" + 
						" OR NOT-X(" + curr2[0] + ", " + curr2[1] + ", " + curr2[2] + ", " + curr2[3] + ", " + curr2[4] + "]");
			}
		}
	}



	public void printMatchesBridges() {
		for(int i=0; i<this.data.length; i++) {
			ArrayList<int[]> curr = this.directionList[i];

			for(int j=0; j<2; j++) {
				for(int z=1; z<3; z++) {
					int neigbourIndex = this.neighbours[i][j];

					if(neigbourIndex != -1) {
						System.out.println("Island " + i +" and " + neigbourIndex + ":");
						ArrayList<int[]> matches = new ArrayList<int[]>();
						ArrayList<int[]> curr2 = this.directionList[neigbourIndex];
						for(int a=0; a<curr.size(); a++) {
							int inserted = 0;
							for(int b=0; b<curr2.size(); b++) {
								int[] aa = curr.get(a);
								int[] bb = curr2.get(b);

								if(aa[j+1] == bb[j+3] && aa[j+1] == z) {
									if(inserted == 0) {
										matches.add(aa);
										inserted = 1;
									}
									if(!matches.contains(bb)) matches.add(curr2.get(b));
								}
							}
						}
						printMatches(matches);
					}
				}
			}
		}
	}


	public void printMatches(ArrayList<int[]> matches) {
		if(matches.isEmpty()) return;


		for(int j=0; j<matches.size(); j++) {
			int i=0;
			Iterator<int[]> it = matches.iterator();
			System.out.print("[");
			while(it.hasNext()) {
				int [] curr = it.next();
				if(i == j ) System.out.print("NOT-");
				System.out.print("X(" + curr[0] + ", " + curr[1] + ", " + curr[2] + ", " + curr[3] + ", " + curr[4] + ")");

				i++;

				if(it.hasNext()) System.out.print(" OR ");
				else {
					System.out.println("]");
					System.out.println("-----AND-----");
				}
			}
		}

	}


	//	public void printChosens(int [] chosenArgs, int index, int neighborUp, int neighborRight, int neighborDown, int neighborLeft) {
	//
	//		for(int i=0; i<chosenArgs.length; i++) {
	//			int directionNum = i/3;
	//
	//			switch(directionNum) {
	//			case 0:
	//				directionNum = neighborUp;
	//				break;
	//			case 1:
	//				directionNum = neighborRight;
	//				break;
	//			case 2:
	//				directionNum = neighborDown;
	//				break;
	//			case 3:
	//				directionNum = neighborLeft;
	//				break;
	//			}
	//
	//
	//			if(directionNum != -1) {
	//				if(chosenArgs[i] == 0) System.out.print("NOT ");
	//				System.out.print("X("+Math.min(index, directionNum)+","+Math.max(index, directionNum)+","+i%3+")");
	//				System.out.println(" AND ");
	//			}
	//
	//		}
	//	}

	public int getDataIndex(int line, int row) {
		for(int i=0; i<this.data.length; i++) {
			if(this.data[i][0] == line && this.data[i][1] == row) return i;
		}
		return -1;
	}

	public int getLeftNeighborIndex(int line, int row) {
		//int line = this.data[i][0];
		//int row = this.data[i][1];
		row--;

		while(row>=0) {

			if(this.board[line][row] == " ")
				row--;
			else return getDataIndex(line, row);

		}
		return -1;
	}

	public int getRightNeighborIndex(int line, int row) {
		//int line = this.data[i][0];
		//int row = this.data[i][1];
		row++;

		while(row<this.board.length) {

			if(this.board[line][row] == " ")
				row++;
			else return getDataIndex(line, row);

		}
		return -1;
	}

	public int getUpNeighborIndex(int line, int row) {
		//int line = this.data[i][0];
		//int row = this.data[i][1];
		line--;

		while(line>=0) {

			if(this.board[line][row] == " ")
				line--;
			else return getDataIndex(line, row);

		}
		return -1;
	}

	public int getDownNeighborIndex(int line, int row) {
		//int line = this.data[i][0];
		//int row = this.data[i][1];
		line++;

		while(line<this.board.length) {

			if(this.board[line][row] == " ")
				line++;
			else return getDataIndex(line, row);

		}
		return -1;
	}

	public void findCross() {
		
		System.out.println("-----------CROSSES-----------");
		int firstFlag = 1;
		for(int i=0; i<this.board.length; i++) {
			for(int j=0; j<this.board.length; j++) {
				if(this.dataIndexes[i][j] == " ") {
					int index = getDataIndex(i, j), neighborUp, neighborRight, neighborDown, neighborLeft;
					if((neighborUp = getUpNeighborIndex(i,j)) != -1 && (neighborRight = getRightNeighborIndex(i,j)) != -1 && (neighborDown = getDownNeighborIndex(i,j)) != -1 && (neighborLeft = getLeftNeighborIndex(i,j)) != -1) {
						
						ArrayList<int[]> up = this.directionList[neighborUp];
						ArrayList<int[]> right = this.directionList[neighborRight];
						ArrayList<int[]> down = this.directionList[neighborDown];
						ArrayList<int[]> left = this.directionList[neighborLeft];
						
						
						ArrayList<int[]>[] currLists = new ArrayList [4];
						currLists[0] = down;
						currLists[1] = left;
						currLists[2] = up;
						currLists[3] = right;
						
						ArrayList<int[]> relDirectionLst = new ArrayList<int[]>();
						
						Iterator<int[]> it;
						
						for(int z=0; z<2; z++) {
							it = currLists[z].iterator();
							while(it.hasNext()) {
								int [] curr = it.next();
								if(curr[z+1] != 0) {	
									relDirectionLst.add(curr);
								}
							}
						}
						
						
						if(firstFlag == 1) {
							firstFlag = 0;
						}
						else {
							System.out.println("-----AND-----");
						}
						
//						System.out.print("[");
//						
//						it = relDirectionLst.iterator();
//						while(it.hasNext()) {
//							int [] curr = it.next();
//							System.out.print("X(" + curr[0] + ", " + curr[1] + ", " + curr[2] + ", " + curr[3] + ", " + curr[4] + ")");
//							if(it.hasNext()) System.out.print(" OR ");
//						}
//
//						System.out.println("]");
//						
					

						for(int a=0; a<relDirectionLst.size(); a++) {
							int [] curr = relDirectionLst.get(a);
							for(int b=a+1; b<relDirectionLst.size(); b++) {

								int [] curr2 = relDirectionLst.get(b);

								System.out.println("-----AND-----");
								System.out.println("[NOT-X(" + curr[0] + ", " + curr[1] + ", " + curr[2] + ", " + curr[3] + ", " + curr[4] + ")" + 
										" OR NOT-X(" + curr2[0] + ", " + curr2[1] + ", " + curr2[2] + ", " + curr2[3] + ", " + curr2[4] + "]");
							}
						}

						
						
					}
					

				}
			}
		}
	}


}
