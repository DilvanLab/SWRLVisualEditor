package br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.composition;

import java.util.ArrayList;
import java.util.List;

import br.usp.icmc.dilvan.swrlEditor.client.resources.Resources;
import br.usp.icmc.dilvan.swrlEditor.client.resources.UtilResource;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Atom;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.AtomImpl;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Rule;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.RuleImpl;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Variable;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.VariableImpl;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Atom.TYPE_ATOM;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Variable.TYPE_VARIABLE;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.UtilView;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.VisualizationView.TYPE_VIEW;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;


public class CompositionTabEditorView extends Composite implements CompositionTabView {

	private enum MODE {EDIT, NEW_ANTECEDENT, NEW_CONSEQUENT}

	private Presenter presenter;
	private ClickHandler handler;

	private Rule rule;

	private Atom atomEdit = null;
	private MODE modeEditAtom = null;

	private List<TextBox> lstTxtVariables;


	private static final Binder binder = GWT.create(Binder.class);
	@UiField VerticalPanel pnlVariables;
	@UiField VerticalPanel pnlAntecedent;
	@UiField Label lblAddAntecent;

	@UiField VerticalPanel pnlConsequent;
	@UiField Label lblAddConsequent;

	@UiField VerticalPanel pnlProperties;
	@UiField ListBox lstAtomTypes;
	
	
	private final MultiWordSuggestOracle predicateSuggestions = new MultiWordSuggestOracle();
	@UiField(provided = true) 
	SuggestBox sugPredicate;
	private String lastFindSuggestions = "";


	@UiField VerticalPanel pnlAddVariables;
	@UiField Label lblAddParameter;
	@UiField Label lblSave;
	@UiField Label lblCancel;
	@UiField Label lblDelete;

	private TYPE_VIEW typeView;
	private String styleNameTxtPredicate = "";

	interface Binder extends UiBinder<Widget, CompositionTabEditorView> {
	}

	public CompositionTabEditorView() {
		
		sugPredicate = new SuggestBox (predicateSuggestions);
		initWidget(binder.createAndBindUi(this));

		lstTxtVariables = new ArrayList<TextBox>();

		String listItens[] = new String[] { "Class", "Object Property", "Datatype Property", "Builtin", "Datatype", "Same/Different" };
		TYPE_ATOM listValues[] = new TYPE_ATOM[] { TYPE_ATOM.CLASS, TYPE_ATOM.INDIVIDUAL_PROPERTY, TYPE_ATOM.DATAVALUE_PROPERTY, TYPE_ATOM.BUILTIN, TYPE_ATOM.DATARANGE, TYPE_ATOM.SAME_DIFERENT };
		lstAtomTypes.addItem("Atom Type", "");
		for (int i = 0; i < listItens.length; i++) {
			lstAtomTypes.addItem(listItens[i], listValues[i].name());
		}
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
		pnlProperties.setVisible(false);
	}

	@Override
	public void setTypeView(TYPE_VIEW typeView) {
		this.typeView = typeView;
	}


	@Override
	public void setRule(Rule r) {
		this.rule = r;
		presenter.getErrors();
		loadAndShowRule();
	}
	
	@Override
	public void setNewRule(Rule r) {
		this.rule = r;
		pnlVariables.clear();
		pnlAntecedent.clear();
		pnlConsequent.clear();
	}


	private void loadAndShowRule() {
		System.out.println("passou");
		pnlProperties.setVisible(false);

		pnlVariables.clear();
		pnlAntecedent.clear();
		pnlConsequent.clear();

		// List of variables
		ArrayList<String> list = new ArrayList<String>();
		ArrayList<String> formatedList = new ArrayList<String>();

		for (Atom a : rule.getAtoms()) {
			for (Variable p : a.getVariables()) {
				if (p.getTypeVariable() == TYPE_VARIABLE.DATALITERAL
						|| p.getTypeVariable() == TYPE_VARIABLE.INDIVIDUALID)
					continue;
				String var;


				if (typeView == TYPE_VIEW.ID)
					var = "<span class=\""+ UtilResource.getCssTypeVariableEditor(p.getTypeVariable()) + "\">"
							+ p.getFormatedID() + "</span>";
				else
					var = "<span class=\""+ UtilResource.getCssTypeVariableEditor(p.getTypeVariable()) + "\">"
							+ p.getFormatedLabel() + "</span>";


				if (formatedList.contains(var))
					continue;
				if (typeView == TYPE_VIEW.ID)
					list.add(p.getFormatedID());
				else
					list.add(p.getFormatedLabel());


				formatedList.add(var);
			}
		}
		// Collections.sort(formatedList);
		for (String p : formatedList) {
			pnlVariables.add(new HTML(p));
		}

		// Antecedent visualization
		VerticalPanel vp = new VerticalPanel();
		vp.add(UtilView.getAtomsVisualizationPanel(rule.getAntecedent(), getClickHandler(), "antecedent", typeView));
		pnlAntecedent.add(vp);

		// Consequent visualization
		vp = new VerticalPanel();
		vp.add(UtilView.getAtomsVisualizationPanel(rule.getConsequent(), getClickHandler(), "consequent", typeView));
		pnlConsequent.add(vp);

		presenter.getErrors();
	}



	private void clearVariables(){
		pnlAddVariables.clear();
		lstTxtVariables.clear();
	}

	private List<TextBox> createVariableTextBox(int num){
		List<TextBox> result = new ArrayList<TextBox>();
		TextBox newTextBox;
		for (int i = 1; i <= num; i++){
			newTextBox = new TextBox();
			newTextBox.setText("?p"+i);
			newTextBox.setWidth("100%");
			newTextBox.setStyleName(Resources.INSTANCE.swrleditor().swrlRule());

			result.add(newTextBox);
		}
		return result;
	}

	private int getNumVariables(String atomType){
		if(atomType.isEmpty())
			return 0;
		else if (atomType.equals(TYPE_ATOM.CLASS.name()) || atomType.equals(TYPE_ATOM.DATARANGE.name()))
			return 1;
		else if(atomType.equals(TYPE_ATOM.BUILTIN.name()))
			return 2;
		else
			return 2;
	}

	private ClickHandler getClickHandler() {
		// Load the atom clicked in the properties box
		if (handler == null) {
			handler = new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					if (event.getRelativeElement() != null) {
						String str = event.getRelativeElement().getTitle();
						for (Atom a : rule.getAtoms()) {
							if (typeView == TYPE_VIEW.ID){
								if (a.getAtomID().equals(str)) {
									editAtom(a);
									break;
								}
							}else{
								if (a.getAtomLabel().equals(str)) {
									editAtom(a);
									break;
								}
							}
						}
					}
				}
			};
		}
		return handler;
	}

	@UiHandler("lstAtomTypes")
	void onListAtomTypesChange(ChangeEvent event) {
		mountPnlProperties(false, 0);
	}

	@UiHandler("lblAddAntecent")
	void onLblAddAntecentClick(ClickEvent event) {
		newAtom(true);
	}

	@UiHandler("lblAddConsequent")
	void onLblAddConsequentClick(ClickEvent event) {
		newAtom(false);

	}

	@UiHandler("lblSave")
	void onLblSaveClick(ClickEvent event) {
		pnlProperties.setVisible(false);
		Atom savedAtom;

		if (modeEditAtom == MODE.EDIT){
			savedAtom = atomEdit;
		}else{
			savedAtom = new AtomImpl();
			if (modeEditAtom == MODE.NEW_ANTECEDENT)
				rule.addAntecedent(savedAtom);
			else if (modeEditAtom == MODE.NEW_CONSEQUENT)
				rule.addConsequent(savedAtom);
		}
		savedAtom.setPredicateID(sugPredicate.getText());
		savedAtom.setPredicateLabel(new ArrayList<String>());
		savedAtom.setAtomType(TYPE_ATOM.valueOf(lstAtomTypes.getValue(lstAtomTypes.getSelectedIndex())));
		savedAtom.getVariables().clear();

		int i = 1;
		Variable newVar;
		for (TextBox txtVariable : lstTxtVariables){
			newVar = new VariableImpl();
			if (txtVariable.getText().startsWith("?"))
				newVar.setSimpleID(txtVariable.getText().substring(1));
			else
				newVar.setSimpleID(txtVariable.getText());

			newVar.setSimpleLabel(new ArrayList<String>());

			newVar.setTypeVariable(VariableImpl.getTYPE_VARIABLE(savedAtom.getAtomType(), savedAtom.getPredicateID(), txtVariable.getText(), i++));
			savedAtom.addVariable(newVar);
		}

		loadAndShowRule();
		presenter.getErrors();
		presenter.setModifyRule();

	}
	@UiHandler("lblCancel")
	void onLblCancelClick(ClickEvent event) {
		pnlProperties.setVisible(false);
	}
	@UiHandler("lblDelete")
	void onLblDeleteClick(ClickEvent event) {
		pnlProperties.setVisible(false);
		if (modeEditAtom == MODE.EDIT)
			rule.removeAtom(this.atomEdit);
		loadAndShowRule();
		presenter.setModifyRule();
	}

	@UiHandler("lblAddParameter")
	void onLblAddParameterClick(ClickEvent event) {
		mountPnlProperties(false, lstTxtVariables.size()+1);
	}

	private void newAtom(boolean isAntecedent){
		if (rule == null)
			setRule(presenter.getRule());
		if (rule == null)
			rule = new RuleImpl();
		
		if (isAntecedent)
			this.modeEditAtom = MODE.NEW_ANTECEDENT;
		else
			this.modeEditAtom = MODE.NEW_CONSEQUENT;

		lstAtomTypes.setSelectedIndex(0);
		sugPredicate.setText("Predicate");
		pnlProperties.setVisible(true);	
		clearVariables();

		lblAddParameter.setVisible(false);
		setStyleNameInSWRLComponents();
	}

	private void editAtom(Atom atomEdit){

		this.modeEditAtom = MODE.EDIT;
		this.atomEdit = atomEdit;
		lstAtomTypes.setSelectedIndex(0);
		for (int i = 0; i < lstAtomTypes.getItemCount(); i++) {
			if (lstAtomTypes.getValue(i).equals(atomEdit.getAtomType().name())) {
				lstAtomTypes.setItemSelected(i, true);
				break;
			}
		}
		mountPnlProperties(true, atomEdit.getCountVariables());

		if (lstTxtVariables.size() == atomEdit.getCountVariables()){
			for (int i = 0; i < lstTxtVariables.size(); i++)
				if (typeView == TYPE_VIEW.ID)
					lstTxtVariables.get(i).setText(atomEdit.getVariables().get(i).getFormatedID());
				else
					lstTxtVariables.get(i).setText(atomEdit.getVariables().get(i).getFormatedLabel());

		}

		if (typeView == TYPE_VIEW.ID)
			sugPredicate.setText(atomEdit.getPredicateID());
		else
			sugPredicate.setText(atomEdit.getPredicateLabel());

		pnlProperties.setVisible(true);		
	}

	private void mountPnlProperties(boolean forcedClear, int forcedNumVariables){
		setStyleNameInSWRLComponents();

		//Builtins
		lblAddParameter.setVisible((lstAtomTypes.getSelectedIndex() == 4));

		if (forcedClear)
			clearVariables();

		if (lstAtomTypes.getSelectedIndex() > 0 ){
			int numVar;
			if (forcedNumVariables > 0)
				numVar = forcedNumVariables;
			else
				numVar = getNumVariables(lstAtomTypes.getValue(lstAtomTypes.getSelectedIndex()));

			List<TextBox> temp = createVariableTextBox(numVar);
			if (lstTxtVariables.size() > 0){
				if (temp.size() <= lstTxtVariables.size()){
					for (int i = 0; i < temp.size(); i++)
						temp.get(i).setText(lstTxtVariables.get(i).getText());
				}else{
					for (int i = 0; i < lstTxtVariables.size(); i++)
						temp.get(i).setText(lstTxtVariables.get(i).getText());
				}
			}else{
				if (modeEditAtom == MODE.EDIT){

					if (temp.size() <= atomEdit.getVariables().size()){
						for (int i = 0; i < temp.size(); i++)
							if (typeView == TYPE_VIEW.ID)
								temp.get(i).setText(atomEdit.getVariables().get(i).getFormatedID());
							else
								temp.get(i).setText(atomEdit.getVariables().get(i).getFormatedLabel());

					}else{
						for (int i = 0; i < atomEdit.getVariables().size(); i++)
							if (typeView == TYPE_VIEW.ID)
								temp.get(i).setText(atomEdit.getVariables().get(i).getFormatedID());
							else
								temp.get(i).setText(atomEdit.getVariables().get(i).getFormatedLabel());

					}
				}
			}
			clearVariables();
			lstTxtVariables = temp;

			int i = 1;
			HorizontalPanel pnlLineVariable;
			for (TextBox txtBox : lstTxtVariables){

				txtBox.addChangeHandler(new ChangeHandler() {
					@Override
					public void onChange(ChangeEvent arg0) {
						setStyleNameInSWRLComponents();
					}
				});

				pnlLineVariable = new HorizontalPanel();
				pnlLineVariable.setWidth("95%");
				pnlLineVariable.add(new InlineLabel((i++) + ": "));
				pnlLineVariable.add(txtBox);
				pnlAddVariables.add(pnlLineVariable);
			}


		}else{
			clearVariables();
		}

	}

	private void setStyleNameInSWRLComponents(){

		if (lstAtomTypes.getSelectedIndex() > 0){
			sugPredicate.setStyleName(Resources.INSTANCE.swrleditor().swrlRule());
			if (!styleNameTxtPredicate.equals(""))
				sugPredicate.removeStyleName(styleNameTxtPredicate);

			//styleNameTxtPredicate = "atom_" + lstAtomTypes.getValue(lstAtomTypes.getSelectedIndex());

			styleNameTxtPredicate = UtilResource.getCssTypeAtom(TYPE_ATOM.valueOf(lstAtomTypes.getValue(lstAtomTypes.getSelectedIndex())));


			sugPredicate.addStyleName(styleNameTxtPredicate);

			int i = 1;
			for (TextBox txtBox : lstTxtVariables){

				String aux = UtilResource.getCssTypeVariableEditor(VariableImpl.getTYPE_VARIABLE(TYPE_ATOM.valueOf(lstAtomTypes.getValue(lstAtomTypes.getSelectedIndex())), 
						sugPredicate.getText(), txtBox.getText(), i++));

				txtBox.addStyleName(aux);
			}
		}else{
			if (!styleNameTxtPredicate.equals(""))
				sugPredicate.removeStyleName(styleNameTxtPredicate);
			styleNameTxtPredicate = "";
		}


	}

	@Override
	public void addAtom(Atom atom, boolean isAntecedent) {
		newAtom(isAntecedent);

		lstAtomTypes.setSelectedIndex(0);
		for (int i = 0; i < lstAtomTypes.getItemCount(); i++) {
			if (lstAtomTypes.getValue(i).equals(atom.getAtomType().name())) {
				lstAtomTypes.setItemSelected(i, true);
				break;
			}
		}

		if (typeView == TYPE_VIEW.ID){
			sugPredicate.setText(atom.getPredicateID());
			mountPnlProperties(true, atom.getCountVariables());
			for (int i = 0; i < atom.getCountVariables(); i++)
				lstTxtVariables.get(i).setText(atom.getVariables().get(i).getFormatedID());
		}else{
			sugPredicate.setText(atom.getPredicateID());
			mountPnlProperties(true, atom.getCountVariables());
			for (int i = 0; i < atom.getCountVariables(); i++)
				lstTxtVariables.get(i).setText(atom.getVariables().get(i).getFormatedLabel());
		}


	}

	@Override
	public void addPredicate(TYPE_ATOM type, String predicate, boolean isAntecedent) {

		newAtom(isAntecedent);

		lstAtomTypes.setSelectedIndex(0);
		for (int i = 0; i < lstAtomTypes.getItemCount(); i++) {
			if (lstAtomTypes.getValue(i).equals(type.name())) {
				lstAtomTypes.setItemSelected(i, true);
				break;
			}
		}

		sugPredicate.setText(predicate);
		mountPnlProperties(true, 0);

	}

	public void setSelfCompletion(List<String> suggest) {
		predicateSuggestions.clear();
		for (String s : suggest)
			predicateSuggestions.add(s);
				
		sugPredicate.showSuggestionList();
	}
	
	@UiHandler("sugPredicate")
	void onSugPredicateKeyUp(KeyUpEvent event) {
		//if ((event.getNativeKeyCode() >= 32) && (event.getNativeKeyCode() <= 126)){
		if (event.getNativeKeyCode() != 13){
			if (!sugPredicate.getText().isEmpty() && !lastFindSuggestions.equals(sugPredicate.getText())){
				lastFindSuggestions = sugPredicate.getText();
				presenter.getSelfCompletionInEditor(sugPredicate.getText(), sugPredicate.getText().length(), 
						10, TYPE_ATOM.valueOf(lstAtomTypes.getValue(lstAtomTypes.getSelectedIndex())));
	
				setStyleNameInSWRLComponents();
			}
		}
	}

}
