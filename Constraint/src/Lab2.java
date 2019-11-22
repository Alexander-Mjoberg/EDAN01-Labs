
public class Lab2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	
	
	
	static public class TestCase {
	    int graph_size;
	    int start;
	    int n_dests;
	    int n_edges;
	    int[] dest;
	    int[] from;
	    int[] to;
	    int[] cost;

	    int[] dest1 = {6};
	    int[] from1 = {1, 1, 2, 2, 3, 4, 4};
	    int[] to1 = {2, 3, 3, 4, 5, 5, 6};
	    int[] cost1 = {4, 2, 5, 10, 3, 4, 11};
	    int[] dest2 = {5, 6};
	    int[] from2 = {1, 1, 2, 2, 3, 4, 4};
	    int[] to2 = {2, 3, 3, 4, 5, 5, 6};
	    int[] cost2 = {4, 2, 5, 10, 3, 4, 11};
	    int[] dest3 = {5, 6};
	    int[] from3 = {1, 1, 1, 2, 2, 3, 3, 3, 4};
	    int[] to3 = {2, 3, 4, 3, 5, 4, 5, 6, 6};
	    int[] cost3 = {6, 1, 5, 5, 3, 5, 6, 4, 2};

		public TestCase(int testCase) {
			switch (testCase) {
			case 1:
			    graph_size = 6;
			    start = 1;
			    n_dests = 1;
			    n_edges = 7;
				dest = dest1;
				from = from1;
				to = to1;
				cost = cost1;
				break;
			case 2:
			    graph_size = 6;
			    start = 1;
			    n_dests = 2;
			    n_edges = 7;
				dest = dest2;
				from = from2;
				to = to2;
				cost = cost2;
				break;
			case 3:
			    graph_size = 6;
			    start = 1;
			    n_dests = 2;
			    n_edges = 9;
				dest = dest3;
				from = from3;
				to = to3;
				cost = cost3;
				break;
			}
		}
	}
	




}
