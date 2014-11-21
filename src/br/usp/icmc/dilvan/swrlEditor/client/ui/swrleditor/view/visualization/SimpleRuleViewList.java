package br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.visualization;

import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Rule;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.UtilView;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.VisualizationView.Presenter;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.VisualizationView.TYPE_VIEW;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;


public class SimpleRuleViewList extends SimpleRuleView {

	public SimpleRuleViewList(Rule rule, Presenter presenter, TYPE_VIEW typeView) {
		super(rule, presenter, typeView);
	}

	@Override
	public void show() {
		contentRule.clear();
		contentRule.add(getSWRLVisualizationPanel(rule, clickHandlerAtom));
	}

	private VerticalPanel getSWRLVisualizationPanel(Rule rl, ClickHandler handler){
		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.add(new HTML("<h2>IF</h2>"));
		verticalPanel.add(UtilView.getAtomsVisualizationPanel(rl.getAntecedent(), handler, "antecedent", typeView));
		verticalPanel.add(new HTML("<h2>THEN</h2>"));
		verticalPanel.add(UtilView.getAtomsVisualizationPanel(rl.getConsequent(), handler, "consequent", typeView));
		return verticalPanel;
	}
}
