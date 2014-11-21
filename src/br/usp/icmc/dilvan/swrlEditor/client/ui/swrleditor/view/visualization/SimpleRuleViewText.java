package br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.visualization;

import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Rule;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.VisualizationView.Presenter;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.VisualizationView.TYPE_VIEW;

public class SimpleRuleViewText extends SimpleRuleView {

	public SimpleRuleViewText(Rule rule, Presenter presenter, TYPE_VIEW typeView) {
		super(rule, presenter, typeView);
	}
	
	@Override
	public void show() {
		contentRule.clear();
		contentRule.getElement().setInnerHTML(rule.getParaphrase());
	}

}
