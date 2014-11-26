package br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.ui.IsWidget;

/**
 *
 * @author Joao Paulo Orlando
 */
public interface OptionsView extends IsWidget {

    public interface Presenter {

	void goToVisualization();
	public void saveOptions(boolean writePermission);

	public void setBooleanOption(String name, Boolean value);


	public void setStringOption(String name, String value);
    }

    public final String SWRLEditor = "SWRLEditor_";

    public static String UsingIDorLabelStr = SWRLEditor + "UsingIDorLabelStr";
    public static String EditorCompositionBool = SWRLEditor + "EditorComposition";
    public static String SWRLCompositionBool = SWRLEditor + "SWRLComposition";

    public static String AutismCompositionBool = SWRLEditor + "AutismComposition";

    public static String DefaultTabCompositionStr = SWRLEditor + "DefaultTabComposition";
    public static String TypeViewSimilarRulesStr = SWRLEditor + "TypeViewSimilarRules";

    public static String AlgorithmSimilarRulesStr = SWRLEditor + "AlgorithmSimilarRules";

    public static String AlgorithmSuggestedTermsStr = SWRLEditor + "AlgorithmSuggestedTerms";
    public static String ListVisualizationBool = SWRLEditor + "ListVisualization";
    public static String TextVisualizationBool = SWRLEditor + "TextVisualization";
    public static String SWRLVisualizationBool = SWRLEditor + "SWRLVisualization";
    public static String AutismVisualizationBool = SWRLEditor + "AutismVisualization";
    public static String GroupsVisualizationBool = SWRLEditor + "GroupsVisualization";
    public static String DecisionVisualizationBool = SWRLEditor + "DecisionVisualization";

    public static String DefaultTabVisualizationStr = SWRLEditor + "DefaultTabVisualization";
    public static String AlgorithmGroupsStr_  = SWRLEditor + "AlgorithmGroups_";

    public static String DefaultAlgorithmGroupsStr = SWRLEditor + "DefaultAlgorithmGroups";
    public static String AlgorithmDecisionTreeStr_  = SWRLEditor + "AlgorithmDecisionTree_";

    public static String DefaultAlgorithmDecisionTreeStr = SWRLEditor + "DefaultAlgorithmDecisionTree";
    public static String tabVisualizationList = "List";
    public static String tabVisualizationText = "Text";
    public static String tabVisualizationSWRL = "SWRL";
    public static String tabVisualizationAutism = "Autism";
    public static String tabVisualizationGroups = "Groups";

    public static String tabVisualizationDecisionTree = "Decision Tree";
    public static String tabCompositionEditor = "Editor";
    public static String tabCompositionSWRL = "SWRL";

    public static String tabCompositionAutism = "Autism";
    public static String viewUsingID = "rdf:ID";

    public static String viewUsingLabel = "rdfs:Label";
    void setOptions(Map<String, Object> config);
    void setOptions(Map<String, Object> config, List<String> GroupAlgorithm,
	    List<String> DecisionTreeAlgorithm, List<String> SimilarRulesAlgorithm, List<String> SuggestTermsAlgorithm);
    void setPresenter(Presenter presenter);

    void setWritePermission(boolean permission);
}