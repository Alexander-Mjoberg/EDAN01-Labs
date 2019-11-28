package lab3_ChoicePoints;
import org.jacop.core.IntVar;
import org.jacop.core.Store;

public class LargestDomain extends ChoicePoint {
	
	public LargestDomain (IntVar[] v, Store store) {
		super(v, store);
	}

	@Override
	IntVar selectVariable(IntVar[] v) {
		
		if (v.length != 0) {
			//System.out.println(java.util.Arrays.asList(v));
			//Locate the largest domain
			int largestDomainIndex = 0;
			for (int i = 0; i < v.length-1; i++) {
				if ((v[i].max()-v[i].min()) > v[largestDomainIndex].max() - v[largestDomainIndex].min())
					largestDomainIndex = i;
			}
			
			if (v[0].min() == v[0].max()) { // Vi tar bara bort den första noden om den bara hara ett värde i domän
				searchVariables = new IntVar[v.length - 1];
				for (int i = 0; i < v.length - 1; i++) {
					searchVariables[i] = v[i + 1];
				}
			} else { // Men annars så låter vi inputten vara
				searchVariables = new IntVar[v.length];
				for (int i = 0; i < v.length; i++) {
					searchVariables[i] = v[i];
				}
			}
		return v[largestDomainIndex];

	    }
	    else {
		System.err.println("Zero length list of variables for labeling");
		return new IntVar(store);
	    }
	}


}
