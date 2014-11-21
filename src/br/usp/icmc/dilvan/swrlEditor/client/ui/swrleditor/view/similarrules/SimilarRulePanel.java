package br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.similarrules;


import java.util.ArrayList;
import java.util.List;

import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Rule;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.VisualizationView.Presenter;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.VisualizationView.TYPE_VIEW;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.visualization.SimpleRuleView;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.visualization.SimpleRuleViewAutism;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.visualization.SimpleRuleViewList;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.visualization.SimpleRuleViewSWRL;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.visualization.SimpleRuleViewText;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HTMLPanel;



public class SimilarRulePanel extends Composite {
	private static SimilarRulePanelUiBinder uiBinder = GWT
			.create(SimilarRulePanelUiBinder.class);
	
	public enum TYPE_VIEW_SIMILAR {EDITOR, SWRL, TEXT, AUTISM}
	
	@UiField VerticalPanel listRules;
	@UiField Label lblTitle;
	@UiField InlineLabel lblClose;
	@UiField HorizontalPanel toolbar;
	@UiField HTMLPanel panelMain;
	private List<Rule> rules;
	private PopupPanel panel;
	private String ruleName;

	interface SimilarRulePanelUiBinder extends
			UiBinder<Widget, SimilarRulePanel> {
	}

	public SimilarRulePanel(List<Rule> rules) {
		initWidget(uiBinder.createAndBindUi(this));
		if(rules==null)
			this.rules = new ArrayList<Rule>();
		else
			this.rules = rules;
		
		panelMain.setSize("650px", "450px");
	}

	public void showListRules(final String ruleName, TYPE_VIEW typeView, Presenter presenter, TYPE_VIEW_SIMILAR TypeViewSimilar) {
		this.ruleName = ruleName;
		toolbar.setVisible(true);

		listRules.clear();
		for(final Rule rule : rules){
			SimpleRuleView pnlRule = null;
			
			if (TypeViewSimilar == TYPE_VIEW_SIMILAR.EDITOR)
				pnlRule = new SimpleRuleViewList(rule, presenter, typeView);
			else if (TypeViewSimilar == TYPE_VIEW_SIMILAR.SWRL)
				pnlRule = new SimpleRuleViewSWRL(rule, presenter, typeView);
			else if (TypeViewSimilar == TYPE_VIEW_SIMILAR.TEXT)
				pnlRule = new SimpleRuleViewText(rule, presenter, typeView);
			else if (TypeViewSimilar == TYPE_VIEW_SIMILAR.AUTISM)
				pnlRule = new SimpleRuleViewAutism(rule, presenter, typeView);
			
			
			pnlRule.setPanelButtonVisible(false);
			pnlRule.show();
			pnlRule.setClickHandlerAtom(null);

			listRules.add(pnlRule);
			pnlRule.closeRule();	
			
		}
		if(rules.size()==0){
			listRules.add(new Label("No similar rules"));
		}
	}
	
	public void showRulePopUp(SimpleRuleView htmlRule) {
		DialogBox popUp = new DialogBox();
		SimilarRulePanel pnl = new SimilarRulePanel(null); 
		pnl.setHeader("Similar rule");
		pnl.setPopUp(popUp);
		pnl.showRule(htmlRule);
		setPopUpConfiguration(popUp);
	}
	
	private void setHeader(String text) {
		lblTitle.setText(text);
	}

	private void showRule(SimpleRuleView htmlRule) {
		toolbar.setVisible(true);
		listRules.add(htmlRule);
	}

	public void setPopUp(DialogBox panel){
		this.panel = panel;
		panel.add(this);
	}
	
	public void setPopUpConfiguration(DialogBox panel) {
		panel.setAutoHideEnabled(true);
		panel.setText(ruleName);
		panel.show();
		panel.center();
	}

	@UiHandler("lblClose")
	void onLblCloseClick(ClickEvent event) {
		panel.hide();
	}
}
