package br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.visualization;

import java.util.List;

import br.usp.icmc.dilvan.swrlEditor.client.resources.Resources;
import br.usp.icmc.dilvan.swrlEditor.client.resources.UtilResource;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Atom;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Rule;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.UtilView;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.VisualizationView.Presenter;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.VisualizationView.TYPE_VIEW;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;


public class SimpleRuleViewSWRL extends SimpleRuleView {

	public SimpleRuleViewSWRL(Rule rule, Presenter presenter, TYPE_VIEW typeView) {
		super(rule, presenter, typeView);
	}

	@Override
	public void show() {
		contentRule.clear();
		contentRule.add(getSWRLHighlightPanel(rule, clickHandlerAtom));
	}


	private VerticalPanel getSWRLHighlightPanel(Rule rl, ClickHandler handler){
		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.add(getAtomsHightlightsPanel(rl.getAntecedent(), handler, "antecedent"));
		verticalPanel.add(new HTML("<div class=\""+Resources.INSTANCE.swrleditor().separador()+"\">-></div>"));
		verticalPanel.add(getAtomsHightlightsPanel(rl.getConsequent(), handler, "consequent"));
		return verticalPanel;
	}


	// rulePart parameter is add with class in the atoms, because when the user clicked and click handler use this 
	public VerticalPanel getAtomsHightlightsPanel(List<Atom> listAtoms, ClickHandler handler, String rulePart){
		VerticalPanel verticalPanel = new VerticalPanel();

		int totalAtoms = listAtoms.size();
		int numAtoms = 1;
		for(Atom at : listAtoms){
			HTML line = new HTML(UtilView.getAtomHightlights(at, numAtoms != totalAtoms, typeView));

			if (typeView == TYPE_VIEW.ID)
				line.setTitle(at.getAtomID());
			else 
				line.setTitle(at.getAtomLabel());

			line.addStyleName(UtilResource.getCssRulePart(rulePart));
			if(handler != null){
				line.addStyleName(Resources.INSTANCE.swrleditor().vtlink());
				line.addClickHandler(handler);
			}
			verticalPanel.add(line);
			numAtoms++;
		}
		return verticalPanel;
	}

}
