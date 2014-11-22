package br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.impl;

import java.util.ArrayList;
import java.util.List;

import br.usp.icmc.dilvan.swrlEditor.client.gwtWindow.Window;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.Filter;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Atom.TYPE_ATOM;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.FilterView;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.OntologyView;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.UtilView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;


public class FilterViewImpl extends Composite implements FilterView{

	@UiField SimplePanel pnlProperties;
	
	@UiField HTML htmlFilter;
	@UiField TextBox txtAnd;
	@UiField TextBox txtOr;
	@UiField TextBox txtNot;
	@UiField CheckBox chkSelectAll;
	@UiField CheckBox chkDeselectAll;
	@UiField CheckBox chkRuleName;
	@UiField CheckBox chkClasses;
	@UiField CheckBox chkDatatype;
	@UiField CheckBox chkObject;
	@UiField CheckBox chkBuiltin;
	@UiField CheckBox chkComments;
	@UiField CheckBox chkSameDiferent;
	@UiField CheckBox chkDataRange;
	
	@UiField CheckBox chkAntecedent;
	@UiField CheckBox chkConsequent;
	
	@UiField CheckBox chkStartAtoms;
	@UiField CheckBox chkMiddleAtoms;
	@UiField CheckBox chkEndAtoms;
	
	@UiField Button btnSearch;
	
	private OntologyView ontologyView;
	private Filter filter;

	private static FilterViewImplUiBinder uiBinder = GWT
			.create(FilterViewImplUiBinder.class);

	private Presenter presenter;

	interface FilterViewImplUiBinder extends UiBinder<Widget, FilterViewImpl> {
	}

	public FilterViewImpl(OntologyView ontologyView) {
		initWidget(uiBinder.createAndBindUi(this));

		this.ontologyView = ontologyView;
		pnlProperties.add(this.ontologyView);
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
		ontologyView.setPresenter((OntologyView.Presenter) presenter);
	}

	@Override
	public void setFilter(Filter filter){
		this.filter = filter;
		
		txtAnd.setText(mountStringFilter(filter.getLstAnd()));
		txtOr.setText(mountStringFilter(filter.getLstOr()));
		txtNot.setText(mountStringFilter(filter.getLstNot()));

		chkAntecedent.setValue(filter.isQueryAntecedent());
		chkConsequent.setValue(filter.isQueryConsequent());
		
		chkBuiltin.setValue(filter.isQueryBuiltin());
		chkClasses.setValue(filter.isQueryClasses());
		chkComments.setValue(filter.isQueryComments());
		chkDataRange.setValue(filter.isQueryDataRange());
		chkDatatype.setValue(filter.isQueryDatatypeProperties());
		chkObject.setValue(filter.isQueryObjectProperties());
		chkRuleName.setValue(filter.isQueryRuleName());
	}

	private String mountStringFilter(List<String> list){
		String txt = "";
		for (String s :list){
			if (s.contains(" "))
				txt = txt + " \""+s+"\"";
			else
				txt = txt + " "+ s;
		}
		return txt.trim();
	}

	@Override
	public void setBuiltins(List<String> builtins) {
		ontologyView.setBuiltins(builtins);
	}

	@Override
	public void setSelectItemOntology(TYPE_ATOM typeAtom, String value, TYPE_FILTER typeFilter){
		
		if (typeAtom == TYPE_ATOM.CLASS)
			chkClasses.setValue(true);
		else if (typeAtom == TYPE_ATOM.DATAVALUE_PROPERTY)
			chkDatatype.setValue(true);
		else if (typeAtom == TYPE_ATOM.INDIVIDUAL_PROPERTY)
			chkObject.setValue(true);
		else if (typeAtom == TYPE_ATOM.BUILTIN)
			chkBuiltin.setValue(true);
		else if (typeAtom == TYPE_ATOM.DATARANGE)
			chkDataRange.setValue(true);
		else if (typeAtom == TYPE_ATOM.SAME_DIFERENT)
			chkSameDiferent.setValue(true);
		
		if (value.contains(" "))
			value = "\"" + value + "\"";

		if (typeFilter == TYPE_FILTER.AND){
			txtAnd.setText((txtAnd.getText()+" "+value).trim());
		}else if (typeFilter == TYPE_FILTER.OR){
			txtOr.setText((txtOr.getText()+" "+value).trim());
		}else if (typeFilter == TYPE_FILTER.NOT){
			txtNot.setText((txtNot.getText()+" "+value).trim());
		}

		mountHTMLFilter();
	}
	
	@UiHandler({"txtAnd", "txtOr", "txtNot"})
	void onTextBoxKeyUp(KeyUpEvent event) {
		mountHTMLFilter();
	}
	
	private void mountHTMLFilter(){
		List<String> lstAnd = separa(txtAnd.getText());
		List<String> lstOr = separa(txtOr.getText());
		List<String> lstNot = separa(txtNot.getText());
		
		htmlFilter.setHTML(UtilView.formatFilter(lstAnd, lstOr, lstNot));
	}
	
	private List<String> separa(String text){
		List<String> result = new ArrayList<String>();
		text = removeDoubleQuotes(text.trim());
		
		while (text.contains("\"")){
			int pos1 = text.indexOf("\"");
			int pos2 = text.substring(pos1+1).indexOf("\"");

			if (pos2 <= 0)
				pos2 = text.length()-1;
			else
				pos2 += pos1+1;
			
			String [] andSplit = text.substring(0, pos1).split(" ");
			for (String s : andSplit){
				if (!s.trim().isEmpty())
					result.add(s.trim());
			}

			if (!(pos1+1 > pos2))
				result.add(text.substring(pos1+1, pos2).trim());
			
			text = text.substring(pos2+1, text.length()).trim();
		}
		
		String [] andSplit = text.split(" ");
		for (String s : andSplit){
			if (!s.trim().isEmpty())
				result.add(s.trim());
		}
				
		return result;
	}
	
	private String removeDoubleQuotes(String s){
		String result = "";
		int count = 0;
		for (int i = 0; i < s.length(); i++){
			if (s.charAt(i) == '"') 
				count++;
			else
				count = 0;
			if (count <= 2)
				result = result + s.charAt(i);
		}
		return result;
	}
	
	@UiHandler("btnSearch")
	void onBtnSearchClick(ClickEvent event) {
		
		if (!chkAntecedent.getValue() && !chkConsequent.getValue()){
			Window.alert("Select one of the items in Rule Part!");
			return;
		}
		if (!chkBuiltin.getValue() && !chkClasses.getValue() && !chkComments.getValue() && !chkDataRange.getValue() && !chkDatatype.getValue() && !chkObject.getValue() && !chkRuleName.getValue()){
			Window.alert("Select one of the items in Filter In!");
			return;
		}
		if (!chkStartAtoms.getValue() && !chkMiddleAtoms.getValue() && !chkEndAtoms.getValue()){
			Window.alert("Select one of the items in Where to Search!");
			return;
		}
		
		filter.setLstAnd(separa(txtAnd.getText()));
		filter.setLstOr(separa(txtOr.getText()));
		filter.setLstNot(separa(txtNot.getText()));
		
		filter.setQueryAntecedent(chkAntecedent.getValue());
		filter.setQueryConsequent(chkConsequent.getValue());
		
		filter.setQueryBuiltin(chkBuiltin.getValue());
		filter.setQueryClasses(chkClasses.getValue());
		filter.setQueryComments(chkComments.getValue());
		filter.setQueryDataRange(chkDataRange.getValue());
		filter.setQueryDatatypeProperties(chkDatatype.getValue());
		filter.setQueryObjectProperties(chkObject.getValue());
		filter.setQueryRuleName(chkRuleName.getValue());
		
		filter.setQueryStartAtoms(chkStartAtoms.getValue());
		filter.setQueryMiddleAtoms(chkMiddleAtoms.getValue());
		filter.setQueryEndAtoms(chkEndAtoms.getValue());

		presenter.search(filter);
	}
	
	@UiHandler("chkSelectAll")
	void onChkSelectAllClick(ClickEvent event) {
		chkDeselectAll.setValue(!chkSelectAll.getValue());

		chkRuleName.setValue(chkSelectAll.getValue());
		chkClasses.setValue(chkSelectAll.getValue());
		chkDatatype.setValue(chkSelectAll.getValue());
		chkObject.setValue(chkSelectAll.getValue());
		chkBuiltin.setValue(chkSelectAll.getValue());
		chkComments.setValue(chkSelectAll.getValue());
		chkSameDiferent.setValue(chkSelectAll.getValue());
		chkDataRange.setValue(chkSelectAll.getValue());
	}
	
	@UiHandler("chkDeselectAll")
	void onChkDeselectAllClick(ClickEvent event) {
		chkSelectAll.setValue(!chkDeselectAll.getValue());

		chkRuleName.setValue(!chkDeselectAll.getValue());
		chkClasses.setValue(!chkDeselectAll.getValue());
		chkDatatype.setValue(!chkDeselectAll.getValue());
		chkObject.setValue(!chkDeselectAll.getValue());
		chkBuiltin.setValue(!chkDeselectAll.getValue());
		chkComments.setValue(!chkDeselectAll.getValue());
		chkSameDiferent.setValue(!chkDeselectAll.getValue());
		chkDataRange.setValue(!chkDeselectAll.getValue());
	}
	
	@UiHandler({"chkRuleName", "chkClasses", "chkDatatype", "chkObject", "chkBuiltin", "chkComments", "chkSameDiferent", "chkDataRange"})
	void onChksFilterInClick(ClickEvent event) {
		
		chkSelectAll.setValue(chkRuleName.getValue() && chkClasses.getValue() && 
				chkDatatype.getValue() && chkObject.getValue() && 
				chkBuiltin.getValue() && chkComments.getValue() && 
				chkSameDiferent.getValue() && chkDataRange.getValue());

		chkDeselectAll.setValue(!chkRuleName.getValue() && !chkClasses.getValue() && 
				!chkDatatype.getValue() && !chkObject.getValue() && 
				!chkBuiltin.getValue() && !chkComments.getValue() && 
				!chkSameDiferent.getValue() && !chkDataRange.getValue());
	}
}
