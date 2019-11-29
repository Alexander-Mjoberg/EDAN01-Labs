import org.jacop.constraints.Constraint;
import org.jacop.constraints.Diff2;
import org.jacop.constraints.XgtY;
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
		// TODO Auto-generated method stub
		TestCase1 testCase = new TestCase1();
		AutoRegressionFilter(testCase.del_add,testCase.del_mul,testCase.number_add,testCase.number_mul,testCase.n,testCase.add,testCase.mul,testCase.dependencies);
	}
	
	public static void AutoRegressionFilter(int additionDuration,int multiplicationDuration,int resourcesAddition,int resourcesMultiplication,int nbrOfOperations, int[] additions, int[] multiplications,int[][] dependencies) {
		Store store = new Store();
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
			addDurations[i] = new IntVar(store,"AdditionD",1,additionDuration);
			addDurationsY[i] = new IntVar(store,"AdditionDY",1,1);
			allOperations[additions[i]-1] = op;
		}
		//Initiate multiplication values
		for (int i = 0; i < multiplications.length; i++) {
			IntVar op = new IntVar(store,"Multiplication at place "+multiplications[i],0,1500);
			mulOperations[i] = op;
			allOperations[multiplications[i]-1] = op;
		}
		//Build dependencies
		for (int i = 0; i<dependencies.length;i++) {
			for (int dep: dependencies[i]) {
				store.impose(new XgtY(allOperations[i],allOperations[dep-1]));
			}
		}
		//Add something so last ops are enforced to run last
		
		

		store.impose(new Diff2(addOperations,addOperationsY,addDurations,addDurationsY));
		
		Search<IntVar> label = new DepthFirstSearch<IntVar>();
		SelectChoicePoint<IntVar> select = new SimpleSelect<IntVar>(
                allOperations,new SmallestDomain<IntVar>(),new IndomainMin<IntVar>());
		boolean result = label.labeling(store, select);
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

}
