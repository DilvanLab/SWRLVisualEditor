package br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.Filter;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.NameGroupAlgorithm;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.RuleSet;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.decisiontree.NodeDecisionTree;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Rule;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.activity.visualization.AutismModel;

import com.google.gwt.user.client.ui.IsWidget;


/**
 * @author Joao Paulo Orlando
 */
public interface VisualizationView extends IsWidget
{
	public enum TYPE_VIEW {ID, LABEL}
	
	void setPresenter(Presenter presenter);
	void setWritePermission(boolean permission);
	void setConfiguration(Map<String, Object> portletConfiguration);
	void setTypeView(TYPE_VIEW typeView);
	void setRuleSelected(String ruleName);
	
	void setRuleSet(RuleSet rules);

	void setGroupAlgorithmList(List<NameGroupAlgorithm> algorithms, Map<String, Object> config);
	public void setGroups(ArrayList<ArrayList<String>> groups);

	void setDecisionTreeAlgorithmList(List<String> algorithms, Map<String, Object> config);
	void setDecisionTree(NodeDecisionTree root);
	
	void refreshRulesView();
	
	void showSimilarRules(String nameRule, List<Rule> result);
	
	void addRuleEvent(int index, Rule rule);
	void deleteRuleEvent(int index, Rule rule);
	
	void finishedRun();
	
	public interface Presenter
	{
		public static final int DEFAULT_NUMBER_GROUPS = 8;

		void getRuleSet();
		
		void getGroups(String algorithmName, int numberGroups);

		void getDecisionTree(String algorithmName);
		
		Rule getRuleByName(String ruleName);		

		AutismModel getAutismModel(Rule rule, TYPE_VIEW typeView);
		
		Filter getFilter();
		
		void getSimilarRules(Rule rule, boolean isNew);
		
		void deleteRule(String nameRule);
		
		void goToNewRule();
		void goToNewRule(String antecedent);
		
		void goToOptions();
		void goToInfo();
		void goToFilter();
		void goToEditRule(String ruleName);
		void goToDuplicateAndEditRule(String ruleName);

		void runRules();
		
	}

}

