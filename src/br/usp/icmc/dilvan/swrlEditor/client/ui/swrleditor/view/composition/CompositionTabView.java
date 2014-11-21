package br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.composition;

import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Atom;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Rule;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Atom.TYPE_ATOM;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.VisualizationView.TYPE_VIEW;

public interface CompositionTabView {

	void setPresenter(Presenter presenter);
	void setRule(Rule r);
	void setNewRule(Rule r);
	void setTypeView(TYPE_VIEW typeView);
	void addAtom(Atom atom, boolean isAntecedent);
	void addPredicate(TYPE_ATOM type, String predicate, boolean isAntecedent);

	public interface Presenter {
		void getErrors();
		void setModifyRule();
		boolean isModifiedRule();
		void setRuleString(String newRule);
		Rule getRule();
		boolean isBlockedAlterRule();
		
		void getSelfCompletionInEditor(String text, int cursor, int maxTerms, TYPE_ATOM typeAtom);
	}

	
}
