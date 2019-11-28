package lab3_ChoicePoints;
import org.jacop.constraints.PrimitiveConstraint;
import org.jacop.constraints.XeqC;
import org.jacop.constraints.XgteqC;
import org.jacop.constraints.XlteqC;
import org.jacop.core.IntVar;
import org.jacop.core.Store;

public class Splitsearch2 extends ChoicePoint {
	
	public Splitsearch2 (IntVar[] v, Store store) {
		super(v, store);
	}

	@Override
	IntVar selectVariable(IntVar[] v) {
		if (v.length != 0) {
			// System.out.println(java.util.Arrays.asList(v));
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

			return v[0];

		} else {
			System.err.println("Zero length list of variables for labeling");
			return new IntVar(store);
		}
	}

	@Override
	//Integer division needs to round up when there is a remainder, otherwise you will get stuck in splitsearch2
	int selectValue(IntVar v) {
		return ((v.max() + v.min()) % 2 == 0) ? ((v.max() + v.min()) / 2) : ((v.max() + v.min() + 1) / 2);
	}

	@Override
	public PrimitiveConstraint getConstraint() {
		return new XgteqC(var, value);
	}

}
