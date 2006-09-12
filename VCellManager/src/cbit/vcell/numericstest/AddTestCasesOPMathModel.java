package cbit.vcell.numericstest;

import cbit.util.KeyValue;
/**
 * Insert the type's description here.
 * Creation date: (11/10/2004 12:01:58 PM)
 * @author: Frank Morgan
 */
public class AddTestCasesOPMathModel extends AddTestCasesOP {

		private KeyValue mathModelKey;
		private AddTestCriteriaOPMathModel[] addTestCriteriaOPsMathModel;

/**
 * AddTestCasesOpMathModel constructor comment.
 * @param argMMKey cbit.sql.KeyValue
 * @param argType java.lang.String
 * @param argAnnot java.lang.String
 * @param argAddTestCriteriaOPs cbit.vcell.numericstest.AddTestCriteriaOP[]
 */
public AddTestCasesOPMathModel(cbit.util.KeyValue argMMKey, String argType, String argAnnot, cbit.vcell.numericstest.AddTestCriteriaOPMathModel[] argAddTestCriteriaOPs) {
	this(null,argMMKey,argType,argAnnot,argAddTestCriteriaOPs);
}
/**
 * AddTestCasesOpMathModel constructor comment.
 * @param argTSKey java.math.BigDecimal
 * @param argMMKey cbit.sql.KeyValue
 * @param argType java.lang.String
 * @param argAnnot java.lang.String
 * @param argAddTestCriteriaOPs cbit.vcell.numericstest.AddTestCriteriaOP[]
 */
public AddTestCasesOPMathModel(
    java.math.BigDecimal argTSKey,
    cbit.util.KeyValue argMMKey,
    String argType,
    String argAnnot,
    AddTestCriteriaOPMathModel[] argAddTestCriteriaOPsMathModel) {
    super(argTSKey, argType, argAnnot);
    if (argMMKey == null) {
        throw new IllegalArgumentException(
            this.getClass().getName() + " mathModelKey cannot be null");
    }
    // argAddTestCriteriaOPs can be null,but if not must have all TestSuiteKeys null
    if (argAddTestCriteriaOPsMathModel != null) {
        if (argAddTestCriteriaOPsMathModel.length == 0) {
            throw new IllegalArgumentException(
                this.getClass().getName() + " TestCriteriaOPs are not null but are empty");
        }
        for (int i = 0; i < argAddTestCriteriaOPsMathModel.length; i += 1) {
            if (argAddTestCriteriaOPsMathModel[i].getTestSuiteKey() != null) {
                throw new IllegalArgumentException(
                    this.getClass().getName() + " overrides TestSuiteKeys in children");
            }
        }
    }

    addTestCriteriaOPsMathModel = argAddTestCriteriaOPsMathModel;
    mathModelKey = argMMKey;
}
/**
 * Insert the method's description here.
 * Creation date: (11/11/2004 5:24:15 PM)
 * @return cbit.vcell.numericstest.AddTestCriteriaOPMathModel[]
 */
public cbit.vcell.numericstest.AddTestCriteriaOPMathModel[] getAddTestCriteriaOPsMathModel() {
	return addTestCriteriaOPsMathModel;
}
/**
 * Insert the method's description here.
 * Creation date: (11/10/2004 12:03:14 PM)
 */
public cbit.util.KeyValue getMathModelKey() {
	return mathModelKey;
}
}
