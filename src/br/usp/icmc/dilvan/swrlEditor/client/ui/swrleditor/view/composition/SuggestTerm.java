package br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.composition;

import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Atom;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.UtilView;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.VisualizationView.TYPE_VIEW;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;


public class SuggestTerm extends Composite {

	public interface Presenter {
		void addAtom(Atom atom, boolean isAntecedent);
	}
	
	@UiField HTML htmlAtom;
	@UiField Label lblAddAntecedent;
	@UiField Label lblAddConsequent;
	
	private static SuggestTermUiBinder uiBinder = GWT
			.create(SuggestTermUiBinder.class);

	interface SuggestTermUiBinder extends UiBinder<Widget, SuggestTerm> {
	}

	
	private Presenter presenter;
	private Atom atom;
	
	public SuggestTerm(Atom atom, Presenter presenter, TYPE_VIEW typeView) {
		initWidget(uiBinder.createAndBindUi(this));
		this.atom = atom;
		this.presenter = presenter;
		this.htmlAtom.setHTML(UtilView.getAtomHightlights(atom, false, typeView));
	}

	@UiHandler("lblAddAntecedent")
	void onLblAddAntecedentClick(ClickEvent event) {
		presenter.addAtom(atom, true);
	}

	@UiHandler("lblAddConsequent")
	void onLblAddConsequentClick(ClickEvent event) {
		presenter.addAtom(atom, false);
	}

}
