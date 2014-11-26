package br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view;

import java.util.List;
import java.util.Map;

import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Atom;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Atom.TYPE_ATOM;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Rule;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.VisualizationView.TYPE_VIEW;

import com.google.gwt.user.client.ui.IsWidget;

/**
 *
 * @author Joao Paulo Orlando
 */
public interface CompositionView extends IsWidget {

    public interface Presenter {
	void getSimilarRules();
	void goToVisualization();

	void saveRule();
	void setRuleName(String text);
    }

    void addAtom(Atom atom, boolean isAntecedent);
    void setBuiltins(List<String> builtins);

    void setConfiguration(Map<String, Object> config);
    void setErrosAndWarnings(List<String> listErros, List<String> listWarnings);
    void setNewRule(Rule rule);
    void setPresenter(Presenter presenter);
    void setRule(Rule rule);

    void setSelectedOntologyItem(TYPE_ATOM typeAtom, String predicate, boolean isAntecedent);
    void setSelfCompletion(List<String> suggest);

    void setTypeView(TYPE_VIEW typeView);
    void setWritePermission(boolean permission);

    void showSimilarRules(String ruleName, List<Rule> rules);

    void showSimilarTerms(List<Atom> listTerms);
}