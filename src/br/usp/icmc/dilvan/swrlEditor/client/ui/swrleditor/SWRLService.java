package br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor;

import java.util.ArrayList;

import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.Errors;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.NameGroupAlgorithm;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.RuleEvents;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.RuleSet;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.decisiontree.NodeDecisionTree;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Atom;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Rule;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Atom.TYPE_ATOM;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("swrlEditor")
public interface SWRLService extends RemoteService {
	
	ArrayList<String> getListBuiltins(String projectName);

	RuleSet getRules(String projectName);
	RuleEvents getRuleEvents(String projectName, Long fromVersion);
	
	Rule getRule(String projectName, String ruleName);
	String getRuleName(String projectName, String antecedent, String consequent);
	Rule getStringToRule(String projectName, String rule);

	Errors getErrorsList(String projectName, Rule rule, boolean isNew);
	
	ArrayList<Rule> getSimilarRules(String projectName, Rule base, float distance, boolean isNew);
	ArrayList<Atom> getSuggestTerms(String projectName, Rule rule);
	
	ArrayList<NameGroupAlgorithm> getGroupAlgorithmsList();
	ArrayList<ArrayList<String>> getGroups(String projectName, String algorithmName, int numGroups);
	
	ArrayList<String> getDecisionTreeAlgorithmsList();
	NodeDecisionTree getDecisionTree(String projectName, String algorithmName);

	ArrayList<String> getSimilarRulesAlgorithmsList();
	ArrayList<String> getSuggestTermsAlgorithmsList();
	
	boolean deleteRule(String projectName, String ruleName);
	boolean saveNewRule(String projectName, String ruleName, Rule rule);
	boolean saveEditRule(String projectName, String ruleName, String oldRuleName, Rule rule);

	ArrayList<String> runRules(String projectName);
	
	
	ArrayList<String> getSelfCompletion(String projectName, String text, int maxTerms, TYPE_ATOM typeAtom);
	

}
