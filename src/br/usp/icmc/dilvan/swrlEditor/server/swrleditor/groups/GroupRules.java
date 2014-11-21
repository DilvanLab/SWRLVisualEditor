package br.usp.icmc.dilvan.swrlEditor.server.swrleditor.groups;

import java.util.List;

import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.RuleSet;

import edu.stanford.smi.protegex.owl.model.OWLModel;

/**
 * Interface extends Runnable and uses the run method to call the clustering
 * algorithm. Before run is called, there are no groups formed (getGroups will
 * return am empty list).
 */
public interface GroupRules extends Runnable {

	/**
	 * Returns the algorithm name that the user will see
	 * @return The name of the algorithm
	 */
	public String getAlgorithmName();

	/**
	 * Returns true if the algorithm allows the definition of the number of 
	 * groups, returns false otherwise.
	 * @return true if the number of groups can be set, false if that number 
	 * cannot be changed.
	 */
	public boolean canSetNumberOfGroups();

	/**
	 * Sets the number of groups, this method should only be called if the 
	 * method canSetNumberOfGroups() returns true. 
	 * It will throw a RuntimeException if Number of Groups cannot be set.
	 * @param numGroups the number of groups that will be returned
	 */
	public void setNumberOfGroups(int numGroups);

	/**
	 * Sets the OWLModel. From this model, all ontology itens are accessible. 
	 * This call is dependent on using Protege API 3.X.
	 * For the OWL API, that should be changed.
	 * @param model the OWL model object
	 */
	public void setOWLModel(OWLModel model);

	/**
	 * Sets the RuleSet. 
	 * @param model the RuleSet object
	 */
	public void setRuleSet(RuleSet rules);

	
	/**
	 * Returns the number of groups.
	 * @return The number of groups generated
	 */
	public int getNumberOfGroups();

	/**
	 * Gets the list of groups. Each element of the list is another list 
	 * containing the rdf:IDs (as Strings) of each
	 * rule in the group. If necessary, we can change this to include: a 
	 * Group Name and sub-groups. 
	 * @return The List of groups
	 */
	public List<List<String>> getGroups();

	//	Do you think we need the following method?
	//
	//   /**
	//    * Gets one group, a list containing the rdf:IDs (as Strings) of each
	//    * rule in the group. If necessary, we can change this to include: a
	//    * Group Name and sub-groups. 
	//    * @return A list representing one group
	//    */
	//   public List<String> getGroup(int i);
}