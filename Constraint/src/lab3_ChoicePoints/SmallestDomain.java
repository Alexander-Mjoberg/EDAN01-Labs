package lab3_ChoicePoints;
import java.util.ArrayList;

import org.jacop.core.IntVar;
import org.jacop.core.Store;

public class SmallestDomain extends ChoicePoint {
	int prevIndex;
	
	public SmallestDomain (IntVar[] v, Store store) {
		super(v, store);
		prevIndex = 0;
	}

	@Override
	IntVar selectVariable(IntVar[] v) {
		
		int smallestDomainIndex = 0;
		int smallestValue = Integer.MAX_VALUE;
		int checkedValue;
		for (int i = 0; i < v.length; i++) {
			checkedValue = v[i].max() - v[i].min();
			
			if (checkedValue < smallestValue) {
				smallestDomainIndex = i;
				smallestValue = checkedValue;
			}
		}
		if (v.length != 0) {
			searchVariables = new IntVar[v.length - 1];
			int j = 0;
			for (int i = 0; i < v.length - 1; i++) {
				if (smallestDomainIndex == j) {
					i--;
				} else {
					searchVariables[i] = v[j];
				}
				j++;
			}
			return v[smallestDomainIndex];
		}
	    else {
		System.err.println("Zero length list of variables for labeling");
		return new IntVar(store);
	    }
	}


}
