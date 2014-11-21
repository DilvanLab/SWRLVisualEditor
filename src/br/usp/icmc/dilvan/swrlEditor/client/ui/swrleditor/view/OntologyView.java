package br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view;

import java.util.List;

import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Atom.TYPE_ATOM;

import com.google.gwt.user.client.ui.IsWidget;

//import edu.stanford.bmir.protege.web.client.model.Project;

/**
 *
 * @author Joao Paulo Orlando
 */
public interface OntologyView extends IsWidget
{
	public interface Presenter
	{
		void getBuiltins();
		void setSelectedPredicate(TYPE_ATOM typeAtom, String predicate, int left, int top);
	}

	void setBuiltins(List<String> builtins);

	void setPresenter(Presenter presenter);

	//void setProjectProtege();//Project project);
}