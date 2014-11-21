package br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.usp.icmc.dilvan.swrlEditor.client.gwtWindow.Window;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.util.Options;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.OptionsView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;


public class OptionsViewImpl extends Composite implements OptionsView {

	private static OptionsViewImplUiBinder uiBinder = GWT
			.create(OptionsViewImplUiBinder.class);

	
	@UiField ListBox lstIDorLabel;

	@UiField CheckBox chkEditorComposition;
	@UiField CheckBox chkSWRLComposition;
	@UiField CheckBox chkAutismComposition;
	@UiField ListBox lstDefaultTabComposition;

	@UiField ListBox lstTypeViewSimilarRules;

	@UiField ListBox lstAlgorithmSuggestedTerms;


	@UiField CheckBox chkListVisualization;
	@UiField CheckBox chkTextVisualization;
	@UiField CheckBox chkSWRLVisualization;
	@UiField CheckBox chkAutismVisualization;
	@UiField CheckBox chkGroupsVisualization;
	@UiField CheckBox chkDecisionVisualization;
	@UiField ListBox lstDefaultTabVisualization;

	@UiField VerticalPanel pnlVisibleAlgorithmGroups;
	@UiField ListBox lstDefaultAlgorithmGroups;

	@UiField VerticalPanel pnlVisibleAlgorithmDecisionTree;
	@UiField ListBox lstDefaultAlgorithmDecisionTree;

	@UiField Button btnSave;
	@UiField Button btnCancel;

	private List<CheckBox> lstCheckAlgorithmGroups;
	private List<CheckBox> lstCheckAlgorithmDecisionTree;


	private Presenter presenter;
	private boolean writePermission;

	interface OptionsViewImplUiBinder extends UiBinder<Widget, OptionsViewImpl> {
	}

	public OptionsViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));

		mountLstDefaultTabComposition();
		mountLstDefaultTabVisualization();

		lstTypeViewSimilarRules.addItem(tabVisualizationList);
		lstTypeViewSimilarRules.addItem(tabVisualizationText);
		lstTypeViewSimilarRules.addItem(tabVisualizationSWRL);
		lstTypeViewSimilarRules.addItem(tabVisualizationAutism);
		
		lstIDorLabel.addItem(viewUsingLabel);
		lstIDorLabel.addItem(viewUsingID);

		lstCheckAlgorithmGroups = new ArrayList<CheckBox>();
		lstCheckAlgorithmDecisionTree = new ArrayList<CheckBox>();
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;

	}

	@Override
	public void setWritePermission(boolean permission) {
		this.writePermission = permission;
	}

	private void mountLstDefaultTabComposition(){

		String tabSelected = "";
		if (lstDefaultTabComposition.getSelectedIndex() >= 0)
			tabSelected = lstDefaultTabComposition.getValue(lstDefaultTabComposition.getSelectedIndex());

		lstDefaultTabComposition.clear();

		if (chkEditorComposition.getValue())
			lstDefaultTabComposition.addItem(tabCompositionEditor);
		if (chkSWRLComposition.getValue())
			lstDefaultTabComposition.addItem(tabCompositionSWRL);
		if (chkAutismComposition.getValue())
			lstDefaultTabComposition.addItem(tabCompositionAutism);

		setSelectItem(lstDefaultTabComposition, tabSelected);
	}

	private void mountLstDefaultTabVisualization(){

		String tabSelected = "";
		if (lstDefaultTabVisualization.getSelectedIndex() >= 0)
			tabSelected = lstDefaultTabVisualization.getValue(lstDefaultTabVisualization.getSelectedIndex());

		lstDefaultTabVisualization.clear();

		if (chkListVisualization.getValue())
			lstDefaultTabVisualization.addItem(tabVisualizationList);
		if (chkTextVisualization.getValue())
			lstDefaultTabVisualization.addItem(tabVisualizationText);
		if (chkSWRLVisualization.getValue())
			lstDefaultTabVisualization.addItem(tabVisualizationSWRL);
		if (chkAutismVisualization.getValue())
			lstDefaultTabVisualization.addItem(tabVisualizationAutism);
		if (chkGroupsVisualization.getValue())
			lstDefaultTabVisualization.addItem(tabVisualizationGroups);
		if (chkDecisionVisualization.getValue())
			lstDefaultTabVisualization.addItem(tabVisualizationDecisionTree);


		setSelectItem(lstDefaultTabVisualization, tabSelected);
	}

	@UiHandler({"chkEditorComposition", "chkSWRLComposition", "chkAutismComposition"})
	void onChkCompositionClick(ClickEvent event) {
		mountLstDefaultTabComposition();
	}

	@UiHandler({"chkListVisualization", "chkTextVisualization", "chkSWRLVisualization", "chkAutismVisualization", "chkGroupsVisualization", "chkDecisionVisualization"})
	void onChkVisualizationClick(ClickEvent event) {
		mountLstDefaultTabVisualization();
	}
	
	@UiHandler("btnSave")
	void onBtnSaveClick(ClickEvent event) {
		if (!this.writePermission)
			Window.alert("The options are valid only for that session. You must log in to save it for future sessions!");
		
		saveOptions();
	}
	@UiHandler("btnCancel")
	void onBtnCancelClick(ClickEvent event) {
			Window.confirm("Do you really want to discard the changes?",
					new Runnable() {
						@Override
						public void run() {
							//SwrlEditorPortlet.closeClsPropBrowsers();
							presenter.goToVisualization();}
					});

	}
	
	@Override
	public void setOptions(Map<String, Object> config, List<String> GroupAlgorithm, 
			List<String> DecisionTreeAlgorithm, List<String> SimilarRulesAlgorithm, List<String> SuggestTermsAlgorithm) {

		addItens(lstAlgorithmSuggestedTerms, SuggestTermsAlgorithm);

		lstCheckAlgorithmGroups.clear();
		pnlVisibleAlgorithmGroups.clear();
		for (String alg : GroupAlgorithm){
			CheckBox chk = new CheckBox();
			chk.setText(alg);

			chk.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent arg0) {
					mountLstDefaultAlgorithms(lstDefaultAlgorithmGroups, lstCheckAlgorithmGroups);
				}
			});
			lstCheckAlgorithmGroups.add(chk);
			pnlVisibleAlgorithmGroups.add(chk);
		}
		
		lstCheckAlgorithmDecisionTree.clear();
		pnlVisibleAlgorithmDecisionTree.clear();

		for (String alg : DecisionTreeAlgorithm){
			CheckBox chk = new CheckBox();
			chk.setText(alg);

			chk.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent arg0) {
					mountLstDefaultAlgorithms(lstDefaultAlgorithmDecisionTree, lstCheckAlgorithmDecisionTree);
				}
			});
			lstCheckAlgorithmDecisionTree.add(chk);
			pnlVisibleAlgorithmDecisionTree.add(chk);
		}
		
		setOptions(config);

	}

	@Override
	public void setOptions(Map<String, Object> config) {

		setSelectItem(lstIDorLabel, Options.getStringOption(config, UsingIDorLabelStr, ""));

		
		chkEditorComposition.setValue(Options.getBooleanOption(config, EditorCompositionBool, true));
		chkSWRLComposition.setValue(Options.getBooleanOption(config, SWRLCompositionBool, true));
		chkAutismComposition.setValue(Options.getBooleanOption(config, AutismCompositionBool, true));
		
		mountLstDefaultTabComposition();

		setSelectItem(lstDefaultTabComposition, Options.getStringOption(config, DefaultTabCompositionStr, ""));
		
		setSelectItem (lstAlgorithmSuggestedTerms, Options.getStringOption(config, AlgorithmSuggestedTermsStr, ""));

		setSelectItem (lstTypeViewSimilarRules, Options.getStringOption(config, TypeViewSimilarRulesStr, ""));

		chkListVisualization.setValue(Options.getBooleanOption(config, ListVisualizationBool, true));
		chkTextVisualization.setValue(Options.getBooleanOption(config, TextVisualizationBool, true));
		chkSWRLVisualization.setValue(Options.getBooleanOption(config, SWRLVisualizationBool, true));
		chkAutismVisualization.setValue(Options.getBooleanOption(config, AutismVisualizationBool, true));
		chkGroupsVisualization.setValue(Options.getBooleanOption(config, GroupsVisualizationBool, true));
		chkDecisionVisualization.setValue(Options.getBooleanOption(config, DecisionVisualizationBool, true));
		
		mountLstDefaultTabVisualization();

		setSelectItem(lstDefaultTabVisualization, Options.getStringOption(config, DefaultTabVisualizationStr, ""));

		
		for (CheckBox chk : lstCheckAlgorithmGroups)
			chk.setValue(Options.getBooleanOption(config, Options.removeCharInvalidForNameOptions(AlgorithmGroupsStr_, chk.getText()), true));
		
		mountLstDefaultAlgorithms(lstDefaultAlgorithmGroups, lstCheckAlgorithmGroups);
		setSelectItem(lstDefaultAlgorithmGroups, Options.getStringOption(config, DefaultAlgorithmGroupsStr, ""));

		
		for (CheckBox chk  : lstCheckAlgorithmDecisionTree)
			chk.setValue(Options.getBooleanOption(config, Options.removeCharInvalidForNameOptions(AlgorithmDecisionTreeStr_, chk.getText()), true));

		mountLstDefaultAlgorithms(lstDefaultAlgorithmDecisionTree, lstCheckAlgorithmDecisionTree);
		setSelectItem(lstDefaultAlgorithmDecisionTree, Options.getStringOption(config, DefaultAlgorithmDecisionTreeStr, ""));
	}
		
		
	private void addItens(ListBox lstBox, List<String> values){
		lstBox.clear();
		for (String value :values)
			lstBox.addItem(value);
	}

	private void setSelectItem(ListBox lstBox, String value){
		lstBox.setSelectedIndex(0);
		for (int i = 0; i < lstBox.getItemCount(); i++){
			if (lstBox.getValue(i).equals(value)){
				lstBox.setSelectedIndex(i);
				break;
			}
		}
	}

	private void mountLstDefaultAlgorithms(ListBox lstBox, List<CheckBox> chks){

		String algSelected = "";
		if (lstBox.getSelectedIndex() >= 0)
			algSelected = lstBox.getValue(lstBox.getSelectedIndex());

		lstBox.clear();

		for (CheckBox chk : chks)
			if (chk.getValue())
				lstBox.addItem(chk.getText());
				setSelectItem(lstBox, algSelected);
	}
	
	private void saveOptions (){
		presenter.setStringOption(UsingIDorLabelStr, getValueLstBox(lstIDorLabel));

		presenter.setBooleanOption(EditorCompositionBool, chkEditorComposition.getValue());
		presenter.setBooleanOption(SWRLCompositionBool, chkSWRLComposition.getValue());
		presenter.setBooleanOption(AutismCompositionBool, chkAutismComposition.getValue());
		
		presenter.setStringOption(DefaultTabCompositionStr, getValueLstBox(lstDefaultTabComposition));
		presenter.setStringOption(AlgorithmSuggestedTermsStr, getValueLstBox(lstAlgorithmSuggestedTerms));

		presenter.setStringOption(TypeViewSimilarRulesStr, getValueLstBox(lstTypeViewSimilarRules));
		presenter.setBooleanOption(ListVisualizationBool, chkListVisualization.getValue());
		presenter.setBooleanOption(TextVisualizationBool, chkTextVisualization.getValue());
		presenter.setBooleanOption(SWRLVisualizationBool, chkSWRLVisualization.getValue());
		presenter.setBooleanOption(AutismVisualizationBool, chkAutismVisualization.getValue());
		presenter.setBooleanOption(GroupsVisualizationBool, chkGroupsVisualization.getValue());
		presenter.setBooleanOption(DecisionVisualizationBool, chkDecisionVisualization.getValue());

		presenter.setStringOption(DefaultTabVisualizationStr, getValueLstBox(lstDefaultTabVisualization));

		for (CheckBox chk : lstCheckAlgorithmGroups)
			presenter.setBooleanOption(Options.removeCharInvalidForNameOptions(AlgorithmGroupsStr_, chk.getText()), chk.getValue());
			
		for (CheckBox chk : lstCheckAlgorithmDecisionTree)
			presenter.setBooleanOption(Options.removeCharInvalidForNameOptions(AlgorithmDecisionTreeStr_, chk.getText()), chk.getValue());
				
				
		presenter.setStringOption(DefaultAlgorithmGroupsStr, getValueLstBox(lstDefaultAlgorithmGroups));
		presenter.setStringOption(DefaultAlgorithmDecisionTreeStr, getValueLstBox(lstDefaultAlgorithmDecisionTree));

		presenter.saveOptions(writePermission);
	}	
	private String getValueLstBox(ListBox lstBox){
		if (lstBox.getSelectedIndex() > -1)
			return lstBox.getItemText(lstBox.getSelectedIndex());
		else
			return "";
	}

}
