package br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.filter;

import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Atom.TYPE_ATOM;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.FilterView;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.FilterView.TYPE_FILTER;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;


public class PopUpLocationItemFilter extends DialogBox {
  
	FilterView filterView;
	TYPE_ATOM type;
	
	@UiField Label lblCancel;
	@UiField InlineLabel lblItemName;
	@UiField Button btnAnd;
	@UiField Button btnOr;
	@UiField Button btnNot;
	
	
	private static final Binder binder = GWT.create(Binder.class);
    
    interface Binder extends UiBinder<Widget, PopUpLocationItemFilter> {
    }
    
    public PopUpLocationItemFilter(FilterView filterView) {
    	super(true);
        setWidget(binder.createAndBindUi(this));
        this.filterView = filterView;
    }
    
    
	private void setType(TYPE_ATOM type) {
		this.type = type;
	}

	public void setItemName(String name, TYPE_ATOM type){
		lblItemName.setText("  "+name+"  ");
		setType(type);
	}

	@UiHandler("lblCancel")
	void onLblCancelClick(ClickEvent event) {
		hide();
	}	
	
	@UiHandler("btnAnd")
	void onBtnAndClick(ClickEvent event) {
		filterView.setSelectItemOntology(type, lblItemName.getText().trim(), TYPE_FILTER.AND);
		hide();
	}
	@UiHandler("btnOr")
	void onBtnOrClick(ClickEvent event) {
		filterView.setSelectItemOntology(type, lblItemName.getText().trim(), TYPE_FILTER.OR);
		hide();
	}
	@UiHandler("btnNot")
	void onBtnNotClick(ClickEvent event) {
		filterView.setSelectItemOntology(type, lblItemName.getText().trim(), TYPE_FILTER.NOT);
		hide();
	}
}