package br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.composition;

import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Atom.TYPE_ATOM;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.CompositionView;

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


public class PopUpLocationView extends DialogBox {
  
	CompositionView compositionView;
	TYPE_ATOM type;
	
	@UiField Label lblCancel;
	@UiField InlineLabel lblItemName;
	@UiField Button btnAntecedent;
	@UiField Button btnConsequent;
	
	
	private static final Binder binder = GWT.create(Binder.class);
    
    interface Binder extends UiBinder<Widget, PopUpLocationView> {
    }
    
    public PopUpLocationView(CompositionView compositionView) {
    	super(true);
        setWidget(binder.createAndBindUi(this));
        this.compositionView = compositionView;
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
	
	@UiHandler("btnAntecedent")
	void onBtnAntecedentClick(ClickEvent event) {
		compositionView.setSelectedOntologyItem(type, lblItemName.getText().trim(), true);
		hide();
	}
	@UiHandler("btnConsequent")
	void onBtnConsequentClick(ClickEvent event) {
		compositionView.setSelectedOntologyItem(type, lblItemName.getText().trim(), false);
		hide();
	}
}