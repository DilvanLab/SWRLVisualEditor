package br.usp.icmc.dilvan.swrlEditor.server.swrleditor.decisiontree;

import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.RuleSet;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.decisiontree.NodeDecisionTree;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLFactory;

/**
 * Interface extends Runnable and uses the run method to call the Decision Tree
 * algorithm. Before run is called, there is no tree (getRootNode will
 * return null).
 */

public interface GenerateNodeRootDecisionTree extends Runnable {

	/**
	 * Returns the algorithm name that the user will see
	 * @return The name of the algorithm
	 */
	public String getAlgorithmName();

	/**
	 * Sets the SWRLFactory. From this factory, all SWRL rules are accessible. 
	 * This call is dependent on using Protege API 3.X.
	 * For the OWL API, that should be changed.
	 * @param factory the SWRL factory object for the set of rules
	 */
	public void setSWRLFactory(SWRLFactory factory);
	
	/**
	 * Sets the RuleSet. 
	 * @param rules the RuleSet object
	 */
	public void setRuleSet(RuleSet rules);

	/**
	 * Gets the Decision Tree root node. If there is no tree yet 
	 * (method Runnable.run() hasn't been called yet), it returns null.
	 */
	public NodeDecisionTree getRootNode();		
}
