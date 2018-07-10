import java.util.ArrayList;

public class Bingo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int n = 7;
		int[][] Bingo_arr = new int[n][n];
		ArrayList<Integer> Bingo_list = new ArrayList<Integer>();
		//n*n to list
		for(int i=0; i<n*n; i++){
				Bingo_list.add(i+1);
		}
		//random get
		System.out.println(n + "x" + n + "»«ªG½L:");
		for(int j=0; j<Bingo_arr.length; j++){
			for(int k=0; k<Bingo_arr.length; k++){
				int Bingo_loc = (int)(Math.random()*Bingo_list.size());
				Bingo_arr[j][k] = Bingo_list.get(Bingo_loc);
				Bingo_list.remove(Bingo_loc);
				System.out.print(Bingo_arr[j][k] + "\t");
			}
			System.out.println("");
		}
	}

}
