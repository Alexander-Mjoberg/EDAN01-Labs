

import org.jacop.constraints.Not;
import org.jacop.core.FailException;
import org.jacop.core.IntDomain;
import org.jacop.core.IntVar;
import org.jacop.core.Store;

import lab3_ChoicePoints.ChoicePoint;
import lab3_ChoicePoints.LargestDomain;
import lab3_ChoicePoints.SmallestDomain;
import lab3_ChoicePoints.Splitsearch1;
import lab3_ChoicePoints.Splitsearch2;

/**
 * Implements Simple Depth First Search .
 * 
 * @author Krzysztof Kuchcinski
 * @version 4.1
 */

public class Lab3_Search  {

    boolean trace = false;

    /**
     * Store used in search
     */
    Store store;

    /**
     * Defines varibales to be printed when solution is found
     */
    IntVar[] variablesToReport;

    /**
     * It represents current depth of store used in search.
     */
    int depth = 0;

    /**
     * It represents the cost value of currently best solution for FloatVar cost.
     */
    public int costValue = IntDomain.MaxInt;

    /**
     * It represents the cost variable.
     */
    public IntVar costVariable = null;
    /**
     * Add variables for statistics
     */
	public int nbrOfNodes = 0;
	public int wrongDecisions = 0;
    /**
     * 
     */
	int searchSelection;

    public Lab3_Search(Store s, int searchSelection) {
	store = s;
	this.searchSelection = searchSelection;
    }


    /**
     * This function is called recursively to assign variables one by one.
     */
    public boolean label(IntVar[] vars) {

	if (trace) {
	    for (int i = 0; i < vars.length; i++) 
		System.out.print (vars[i] + " ");
	    System.out.println ();
	}

	ChoicePoint choice = null;
	boolean consistent;
	nbrOfNodes++;

	// Instead of imposing constraint just restrict bounds
	// -1 since costValue is the cost of last solution
	if (costVariable != null) {
	    try {
		if (costVariable.min() <= costValue - 1)
		    costVariable.domain.in(store.level, costVariable, costVariable.min(), costValue - 1);
		else
		    return false;
	    } catch (FailException f) {
		return false;
	    }
	}

	consistent = store.consistency();
		
	if (!consistent) {
	    // Failed leaf of the search tree
		wrongDecisions++;
	    return false;
	} else { // consistent

	    if (vars.length == 0) {
		// solution found; no more variables to label

		// update cost if minimization
		if (costVariable != null)
		    costValue = costVariable.min();

		reportSolution();

		return costVariable == null; // true is satisfiability search and false if minimization
	    }
	    switch (searchSelection) {
	    case 1 : choice = new Splitsearch1(vars,store);
	    break;
	    case 2 : choice = new Splitsearch2(vars,store);
	    break;
	    case 3 : choice = new SmallestDomain(vars,store);
	    break;
	    case 4 : choice = new LargestDomain(vars,store);
	    break;
	    default : choice = new ChoicePoint(vars,store);
	    }
 	    

	    levelUp();

	    store.impose(choice.getConstraint());

	    // choice point imposed.
			
	    consistent = label(choice.getSearchVariables());

            if (consistent) {
		levelDown();
		return true;
	    } else {

		restoreLevel();

		store.impose(new Not(choice.getConstraint()));

		// negated choice point imposed.
		
		consistent = label(vars);

		levelDown();

		if (consistent) {
		    return true;
		} else {
		    return false;
		}
	    }
	}
    }

    void levelDown() {
	store.removeLevel(depth);
	store.setLevel(--depth);
    }

    void levelUp() {
	store.setLevel(++depth);
    }

    void restoreLevel() {
	store.removeLevel(depth);
	store.setLevel(store.level);
    }

    public void reportSolution() {
	if (costVariable != null)
	    System.out.println ("Cost is " + costVariable);

	for (int i = 0; i < variablesToReport.length; i++) 
	    System.out.print (variablesToReport[i] + " ");
	System.out.println("\nSolutions stats: "+ nbrOfNodes + " nodes, " + wrongDecisions + " wrong decisions");
	System.out.println ("\n---------------");
    }

    public void setVariablesToReport(IntVar[] v) {
	variablesToReport = v;
    }

    public void setCostVariable(IntVar v) {
	costVariable = v;
    }

}
