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
    public interface Presenter
    {
	public static final int DEFAULT_NUMBER_GROUPS = 8;

	void deleteRule(String nameRule);

	AutismModel getAutismModel(Rule rule, TYPE_VIEW typeView);

	void getDecisionTree(String algorithmName);

	Filter getFilter();

	void getGroups(String algorithmName, int numberGroups);

	Rule getRuleByName(String ruleName);

	void getRuleSet();

	void getSimilarRules(Rule rule, boolean isNew);

	void goToDuplicateAndEditRule(String ruleName);
	void goToEditRule(String ruleName);

	void goToFilter();
	void goToInfo();
	void goToNewRule();
	void goToNewRule(String antecedent);
	void goToOptions();

	void runRules();
    }

    public enum TYPE_VIEW {ID, LABEL}
    void addRuleEvent(int index, Rule rule);
    void deleteRuleEvent(int index, Rule rule);
    void finishedRun();
    void refreshRulesView();

    void setConfiguration(Map<String, Object> portletConfiguration);

    void setDecisionTree(NodeDecisionTree root);
    void setDecisionTreeAlgorithmList(List<String> algorithms, Map<String, Object> config);

    void setGroupAlgorithmList(List<NameGroupAlgorithm> algorithms, Map<String, Object> config);
    public void setGroups(ArrayList<ArrayList<String>> groups);

    void setPresenter(Presenter presenter);

    void setRuleSelected(String ruleName);

    void setRuleSet(RuleSet rules);
    void setTypeView(TYPE_VIEW typeView);

    void setWritePermission(boolean permission);

    void showSimilarRules(String nameRule, List<Rule> result);
}

