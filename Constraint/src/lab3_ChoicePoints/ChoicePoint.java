package lab3_ChoicePoints;
import org.jacop.constraints.PrimitiveConstraint;
import org.jacop.constraints.XeqC;
import org.jacop.core.IntVar;
import org.jacop.core.Store;

public class ChoicePoint {
	Store store;
	IntVar var;
	IntVar[] searchVariables;
	int value;
	
	public ChoicePoint (IntVar[] v, Store store) {
	    var = selectVariable(v);
	    value = selectValue(var);
	    store = this.store;
	}

	public IntVar[] getSearchVariables() {
	    return searchVariables;
	}

	/**
	 * example variable selection; input order
	 */ 
	IntVar selectVariable(IntVar[] v) {
		//System.out.println(java.util.Arrays.asList(v));
	    if (v.length != 0) {
		searchVariables = new IntVar[v.length-1];
		for (int i = 0; i < v.length-1; i++) {
		    searchVariables[i] = v[i+1]; 
		}
		return v[0];

	    }
	    else {
		System.err.println("Zero length list of variables for labeling");
		return new IntVar(store);
	    }
	}

	/**
	 * example value selection; indomain_min
	 */ 
	int selectValue(IntVar v) {
	    return v.min();
	}

	/**
	 * example constraint assigning a selected value
	 */
	public PrimitiveConstraint getConstraint() {
		return new XeqC(var, value);
	}

}
