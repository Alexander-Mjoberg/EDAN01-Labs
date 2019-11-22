import org.jacop.core.IntVar;
import org.jacop.core.Store;
import org.jacop.search.*;
import org.jacop.constraints.*;


public class Lab1 {

	public static void main(String[] args) {
		TestCase testCase = new TestCase(1);
		photo(testCase);
		photoModified(testCase);
	}
	
	public static void photo(TestCase testCase) {
		//Local and meaningful variables for readability
		int[][] prefs = testCase.prefs;
		int nbrOfPeople = testCase.n;
		
		//Set up store and variables for people, constrain so no person is reused
	    Store store = new Store();
	    IntVar[] people = new IntVar[nbrOfPeople];
	    for (int i = 0; i < nbrOfPeople; i++) {
	      people[i] = new IntVar(store, "spot_" + i, 1, nbrOfPeople);
	    }
	    store.impose(new Alldiff(people));
	    IntVar[] prefsGiven = new IntVar[prefs.length];
	    
	    //For each preference, link the subjects and check if distance is greater than one
	    for (int i = 0; i <prefs.length; i++) {
	    	//Because people list starts at 1, indexes must be subtracted from
	    	int from = prefs[i][0]-1;
	    	int to = prefs[i][1]-1;
	    	prefsGiven[i] = new IntVar(store,0,1);
	    	IntVar dist = new IntVar(store, "dist",1,nbrOfPeople);
	    	Constraint cdist = new Distance(people[from],people[to],dist);
	    	store.impose(cdist);
	    	Constraint ref = new Reified(new XeqC(dist,1),prefsGiven[i]);
	    	store.impose(ref);
	    }
	    
	    //Sum up preferences given, then make negative as the search algorhitm will minimize the cost.
	    IntVar given = new IntVar(store, "given",0,prefs.length);
	    Constraint csum = new SumInt(prefsGiven,"==",given);
	    store.impose(csum);
	    IntVar negCost = new IntVar(store, "score", -prefs.length, 0);
	    store.impose(new XmulCeqZ(given, -1, negCost));
	    
	    //Search and print the result
		Search<IntVar> label = new DepthFirstSearch<IntVar>();
        SelectChoicePoint<IntVar> select = new SimpleSelect<IntVar>(
                people,new LargestDomain<IntVar>(),new IndomainMin<IntVar>());
        System.out.println("Searching for solution to the original photo problem....");
        boolean result = label.labeling(store, select,negCost);
		if (result) {
			System.out.println(negCost.value()*-1 + " preferences were successfully granted by this solution\n");
		}	
	}
	
	public static void photoModified(TestCase testCase) {
		//Local and meaningful variables for readability
		int[][] prefs = testCase.prefs;
		int nbrOfPeople = testCase.n;
		
		//Set up store and variables for people
	    Store store = new Store();
	    IntVar[] people = new IntVar[nbrOfPeople];
	    for (int i = 0; i < nbrOfPeople; i++) {
	      people[i] = new IntVar(store, "spot_" + i, 1, nbrOfPeople);
	    }
	    store.impose(new Alldiff(people));
	    
	    //Set up distance variables
	    IntVar[] distanceGiven = new IntVar[prefs.length];
	    Distance[] distances = new Distance[prefs.length];
	    IntVar max = new IntVar(store, "max",1,1000);
	    
	    
	    for (int i = 0; i <prefs.length; i++) {
	    	int from = prefs[i][0]-1;
	    	int to = prefs[i][1]-1;
	    	distanceGiven[i] = new IntVar(store,0,nbrOfPeople);
	    	distances[i] =new Distance(people[from],people[to],distanceGiven[i]);
	    	store.impose(distances[i]);
	    }
		store.impose(new Max(distanceGiven, max));
		
		
		Search<IntVar> label = new DepthFirstSearch<IntVar>();
        SelectChoicePoint<IntVar> select = new SimpleSelect<IntVar>(
                people,new SmallestDomain<IntVar>(),new IndomainMin<IntVar>());
        System.out.println("Searching for solution to the modified photo problem....");
        boolean result = label.labeling(store, select,max);
		if (result) {
			System.out.println("The maximum distance between preferences is "+ max.value() + " using this solution");
		}	
	}
	
	
	
	
	
	static public class TestCase {
		public int n;
		public int[][] prefs;
		int prefs1[][] = { { 1, 3 }, { 1, 5 }, { 1, 8 }, { 2, 5 }, { 2, 9 }, { 3, 4 }, { 3, 5 }, { 4, 1 },
				{ 4, 5 }, { 5, 6 }, { 5, 1 }, { 6, 1 }, { 6, 9 }, { 7, 3 }, { 7, 8 }, { 8, 9 }, { 8, 7 } };
		int prefs2[][] = { { 1, 3 }, { 1, 5 }, { 2, 5 }, { 2, 8 }, { 2, 9 }, { 3, 4 }, { 3, 5 }, { 4, 1 },
				{ 4, 5 }, { 4, 6 }, { 5, 1 }, { 6, 1 }, { 6, 9 }, { 7, 3 }, { 7, 5 }, { 8, 9 }, { 8, 7 },
				{ 8, 10 }, { 9, 11 }, { 10, 11 } };
		int prefs3[][] = { { 1, 3 }, { 1, 5 }, { 2, 5 }, { 2, 8 }, { 2, 9 }, { 3, 4 }, { 3, 5 }, { 4, 1 },
				{ 4, 15 }, { 4, 13 }, { 5, 1 }, { 6, 10 }, { 6, 9 }, { 7, 3 }, { 7, 5 }, { 8, 9 }, { 8, 7 },
				{ 8, 14 }, { 9, 13 }, { 10, 11 } };

		public TestCase(int testCase) {
			switch (testCase) {
			case 1:
				n = 9;
				prefs = prefs1;
				break;
			case 2:
				n = 11;
				prefs = prefs2;
				break;
			case 3:
				n = 15;
				prefs = prefs3;
				break;
			}
		}
	}

}
