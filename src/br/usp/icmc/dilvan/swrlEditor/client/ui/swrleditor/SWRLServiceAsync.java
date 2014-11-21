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

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The client side stub for the RPC service.
 */
public interface SWRLServiceAsync {
	
	void getListBuiltins(String projectName, AsyncCallback<ArrayList<String>> callback);

	void getRules(String projectName, AsyncCallback<RuleSet> callback);
	void getRuleEvents(String projectName, Long fromVersion, AsyncCallback<RuleEvents> callback);
	
	void getRule(String projectName, String ruleName, AsyncCallback<Rule> callback);
	void getRuleName(String projectName, String antecedent, String consequent, AsyncCallback<String> callback);
	void getStringToRule(String projectName, String rule, AsyncCallback<Rule> callback);

	void getErrorsList(String projectName, Rule rule, boolean isNew, AsyncCallback<Errors> callback);
	
	void getSimilarRules(String projectName, Rule base, float distance, boolean isNew, AsyncCallback<ArrayList<Rule>> callback);
	void getSuggestTerms(String projectName, Rule rule, AsyncCallback<ArrayList<Atom>> callback);
	
	void getGroupAlgorithmsList(AsyncCallback<ArrayList<NameGroupAlgorithm>> callback);
	void getGroups(String projectName, String algorithmName, int numGroups, AsyncCallback<ArrayList<ArrayList<String>>> callback);
	
	void getDecisionTreeAlgorithmsList(AsyncCallback<ArrayList<String>> callback);
	void getDecisionTree(String projectName, String algorithmName, AsyncCallback<NodeDecisionTree> callback);

	void getSimilarRulesAlgorithmsList(AsyncCallback<ArrayList<String>> callback);
	void getSuggestTermsAlgorithmsList(AsyncCallback<ArrayList<String>> callback);
	
	
	void deleteRule(String projectName, String ruleName, AsyncCallback<Boolean> callback);
	void saveNewRule(String projectName, String ruleName, Rule rule, AsyncCallback<Boolean> callback);
	void saveEditRule(String projectName, String ruleName, String oldRuleName, Rule rule, AsyncCallback<Boolean> callback);

	void runRules(String projectName, AsyncCallback<ArrayList<String>> callback);

	void getSelfCompletion(String projectName, String text, int maxTerms, TYPE_ATOM typeAtom, AsyncCallback<ArrayList<String>> callback);
}
