package br.usp.icmc.dilvan.swrlEditor.client.gwtWindow;

import br.usp.icmc.dilvan.swrlEditor.client.gwtWindow.ConfirmDialog.Binder;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

class AlertDialog extends DialogBox {
	@UiField Label label;
	@UiField Button okButton;
	
	private static final Binder binder = GWT.create(Binder.class);
    
    interface Binder extends UiBinder<Widget, AlertDialog> {
    }
    
    Runnable callback;
    
    public AlertDialog() {
    	super(false);
        setWidget(binder.createAndBindUi(this));
    }
    
    @UiHandler("okButton")
	void onYesClick(ClickEvent event) {
    	hide();
	}
    
    public void setMsg(String msg) {
    	label.setText(msg);
    }
}

