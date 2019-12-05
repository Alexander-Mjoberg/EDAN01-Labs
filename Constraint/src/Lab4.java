import java.util.TreeMap;
import org.jacop.constraints.Diff2;
import org.jacop.constraints.Max;
import org.jacop.constraints.XgteqY;
import org.jacop.constraints.XplusYeqZ;
import org.jacop.core.IntVar;
import org.jacop.core.Store;
import org.jacop.search.DepthFirstSearch;
import org.jacop.search.IndomainMin;
import org.jacop.search.Search;
import org.jacop.search.SelectChoicePoint;
import org.jacop.search.SimpleSelect;
import org.jacop.search.SmallestDomain;

public class Lab4 {

	public static void main(String[] args) {
		//Change the testcase here.
		TestCase6 testCase = new TestCase6();
		AutoRegressionFilter(testCase.del_add,testCase.del_mul,testCase.number_add,testCase.number_mul,testCase.n,testCase.add,testCase.mul,testCase.dependencies,testCase.last);
	}
	
	public static void AutoRegressionFilter(int additionDuration,int multiplicationDuration,int resourcesAddition,int resourcesMultiplication,int nbrOfOperations, int[] additions, int[] multiplications,int[][] dependencies,int[] lastOperations) {
		Store store = new Store();
		long start = System.currentTimeMillis();
		IntVar[] allOperations = new IntVar[nbrOfOperations];
		//Addition squares
		IntVar[] addOperations = new IntVar[additions.length];
		IntVar[] addOperationsY = new IntVar[additions.length];
		IntVar[] addDurations = new IntVar[additions.length];
		IntVar[] addDurationsY = new IntVar[additions.length];
		//Multiplication squares
		IntVar[] mulOperations = new IntVar[multiplications.length];
		IntVar[] mulOperationsY = new IntVar[multiplications.length];
		IntVar[] mulDurations = new IntVar[multiplications.length];
		IntVar[] mulDurationsY = new IntVar[multiplications.length];
		//Initiate addition variables
		for (int i = 0; i < additions.length; i++) {
			IntVar op = new IntVar(store,"Addition at place "+additions[i],0,1500);
			addOperations[i] = op;
			addOperationsY[i] = new IntVar(store,"Addition resource used",1,resourcesAddition);
			addDurations[i] = new IntVar(store,"AdditionD",additionDuration,additionDuration);
			addDurationsY[i] = new IntVar(store,"AdditionDY",1,1);
			allOperations[additions[i]-1] = op;
		}
		//Initiate multiplication values
		for (int i = 0; i < multiplications.length; i++) {
			IntVar op = new IntVar(store,"Multiplication at place "+multiplications[i],0,1500);
			mulOperations[i] = op;
			mulOperationsY[i] = new IntVar(store,"Multiplication resource used",1,resourcesMultiplication);
			mulDurations[i] = new IntVar(store,"MultiplicationD",multiplicationDuration,multiplicationDuration);
			mulDurationsY[i] = new IntVar(store,"MultiplicationDY",1,1);
			allOperations[multiplications[i]-1] = op;
		}
		//Calculate endtimes for operations. 
		IntVar[] allOperationsEndTime = new IntVar[allOperations.length];
		for (int i = 0; i < allOperations.length; i++) {
			allOperationsEndTime[i] = new IntVar(store,"endtime op " + i,0,1500);
			if (allOperations[i].id.contains("Add")) {
				store.impose(new XplusYeqZ(allOperations[i],addDurations[0],allOperationsEndTime[i]));
			}
			else {
				store.impose(new XplusYeqZ(allOperations[i],mulDurations[0],allOperationsEndTime[i]));
			}
		}
		//Build dependencies in graph
		for (int i = 0; i<dependencies.length;i++) {
			for (int dep: dependencies[i]) {
				store.impose(new XgteqY(allOperationsEndTime[dep-1],allOperations[i]));
			}
		}
		
		//Impose squares
		store.impose(new Diff2(addOperations,addOperationsY,addDurations,addDurationsY));
		store.impose(new Diff2(mulOperations,mulOperationsY,mulDurations,mulDurationsY));
		
		IntVar finish = new IntVar(store,"finish",0,1500);
		store.impose(new Max(allOperationsEndTime,finish));
		Search<IntVar> label = new DepthFirstSearch<IntVar>();
		SelectChoicePoint<IntVar> select = new SimpleSelect<IntVar>(
                allOperations,new SmallestDomain<IntVar>(),new IndomainMin<IntVar>());
		boolean result = label.labeling(store, select,finish);
		if(result) {
			long end = System.currentTimeMillis();
			TreeMap<Integer,IntVar> sortedOperations = new TreeMap<Integer,IntVar>();
			for (IntVar op : allOperations) {
				sortedOperations.put(op.value(),op);
			}
			System.out.println("Printing sorted schedule....");
			System.out.println(sortedOperations.values());
			System.out.println("Clock cycles at finish: "+finish.value());
			System.out.println("Finished in " + (end-start)+ " ms");
			System.out.println("Optimal add time : " + (additions.length*additionDuration)/resourcesAddition + " Optimal mul time "+ (multiplications.length*multiplicationDuration)/resourcesMultiplication);
			
		}
	}

	
	static public class TestCase1 {
		int del_add = 1;
		int del_mul = 2;

		int number_add = 1;
		int number_mul = 1;
		int n = 28;

		int[] last = {27,28};

		int[] add = {9,10,11,12,13,14,19,20,25,26,27,28};

		int[] mul = {1,2,3,4,5,6,7,8,15,16,17,18,21,22,23,24};

		int[][] dependencies = {
		{9},
		{9},
		{10},
		{10},
		{11},
		{11},
		{12},
		{12},
		{27},
		{28},
		{13},
		{14},
		{16,17},
		{15,18},
		{19},
		{19},
		{20},
		{20},
		{22,23},
		{21,24},
		{25},
		{25},
		{26},
		{26},
		{27},
		{28},
		{},
		{},
		};
	}
	
	static public class TestCase2{
		int del_add = 1;
		int del_mul = 2;

		int number_add = 1;
		int number_mul = 2;
		int n = 28;

		int[] last = {27,28};

		int[] add = {9,10,11,12,13,14,19,20,25,26,27,28};

		int[] mul = {1,2,3,4,5,6,7,8,15,16,17,18,21,22,23,24};

		int[][] dependencies = {
		{9},
		{9},
		{10},
		{10},
		{11},
		{11},
		{12},
		{12},
		{27},
		{28},
		{13},
		{14},
		{16,17},
		{15,18},
		{19},
		{19},
		{20},
		{20},
		{22,23},
		{21,24},
		{25},
		{25},
		{26},
		{26},
		{27},
		{28},
		{},
		{},
		};
	}
	
	static public class TestCase3{
		int del_add = 1;
		int del_mul = 2;

		int number_add = 1;
		int number_mul = 3;
		int n = 28;

		int[] last = {27,28};

		int[] add = {9,10,11,12,13,14,19,20,25,26,27,28};

		int[] mul = {1,2,3,4,5,6,7,8,15,16,17,18,21,22,23,24};

		int[][] dependencies = {
		{9},
		{9},
		{10},
		{10},
		{11},
		{11},
		{12},
		{12},
		{27},
		{28},
		{13},
		{14},
		{16,17},
		{15,18},
		{19},
		{19},
		{20},
		{20},
		{22,23},
		{21,24},
		{25},
		{25},
		{26},
		{26},
		{27},
		{28},
		{},
		{},
		};
	}
	
	static public class TestCase4{
		int del_add = 1;
		int del_mul = 2;

		int number_add = 2;
		int number_mul = 2;
		int n = 28;

		int[] last = {27,28};

		int[] add = {9,10,11,12,13,14,19,20,25,26,27,28};

		int[] mul = {1,2,3,4,5,6,7,8,15,16,17,18,21,22,23,24};

		int[][] dependencies = {
		{9},
		{9},
		{10},
		{10},
		{11},
		{11},
		{12},
		{12},
		{27},
		{28},
		{13},
		{14},
		{16,17},
		{15,18},
		{19},
		{19},
		{20},
		{20},
		{22,23},
		{21,24},
		{25},
		{25},
		{26},
		{26},
		{27},
		{28},
		{},
		{},
		};

	}
	
	static public class TestCase5{
		int del_add = 1;
		int del_mul = 2;

		int number_add = 2;
		int number_mul = 3;
		int n = 28;

		int[] last = {27,28};

		int[] add = {9,10,11,12,13,14,19,20,25,26,27,28};

		int[] mul = {1,2,3,4,5,6,7,8,15,16,17,18,21,22,23,24};

		int[][] dependencies = {
		{9},
		{9},
		{10},
		{10},
		{11},
		{11},
		{12},
		{12},
		{27},
		{28},
		{13},
		{14},
		{16,17},
		{15,18},
		{19},
		{19},
		{20},
		{20},
		{22,23},
		{21,24},
		{25},
		{25},
		{26},
		{26},
		{27},
		{28},
		{},
		{},
		};
	}
	
	static public class TestCase6{
		int del_add = 1;
		int del_mul = 2;

		int number_add = 2;
		int number_mul = 4;
		int n = 28;

		int[] last = {27,28};

		int[] add = {9,10,11,12,13,14,19,20,25,26,27,28};

		int[] mul = {1,2,3,4,5,6,7,8,15,16,17,18,21,22,23,24};

		int[][] dependencies = {
		{9},
		{9},
		{10},
		{10},
		{11},
		{11},
		{12},
		{12},
		{27},
		{28},
		{13},
		{14},
		{16,17},
		{15,18},
		{19},
		{19},
		{20},
		{20},
		{22,23},
		{21,24},
		{25},
		{25},
		{26},
		{26},
		{27},
		{28},
		{},
		{},
		};
	}

}
