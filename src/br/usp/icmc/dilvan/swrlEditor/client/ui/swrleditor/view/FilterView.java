package br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view;

import java.util.List;

import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.Filter;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Atom.TYPE_ATOM;

import com.google.gwt.user.client.ui.IsWidget;


/**
 *
 * @author Joao Paulo Orlando
 */
public interface FilterView extends IsWidget
{
	
	public enum TYPE_FILTER {AND, OR, NOT};

	
	void setPresenter(Presenter presenter);
	void setFilter(Filter filter);
	void setBuiltins(List<String> builtins);
	
	void setSelectItemOntology(TYPE_ATOM typeAtom, String value, TYPE_FILTER typeFilter);
	
	public interface Presenter
	{
		void goToVisualization();
		
		void search(Filter f);
	}

	

	
}