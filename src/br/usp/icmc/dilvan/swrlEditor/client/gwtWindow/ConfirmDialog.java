package br.usp.icmc.dilvan.swrlEditor.client.gwtWindow;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

class ConfirmDialog extends DialogBox {
	@UiField Label label;
	@UiField Button yesButton;
	@UiField Button cancelButton;
	
	private static final Binder binder = GWT.create(Binder.class);
    
    interface Binder extends UiBinder<Widget, ConfirmDialog> {
    }
    
    Runnable callback;
    
    public ConfirmDialog() {
    	super(false);
        setWidget(binder.createAndBindUi(this));
    }
    
    @UiHandler("yesButton")
	void onYesClick(ClickEvent event) {
    	hide();
		callback.run();
	}
    
    @UiHandler("cancelButton")
	void onCancelClick(ClickEvent event) {
    	hide();
	}
    
    public void setMsg(String msg) {
    	label.setText(msg);
    }

	public void setCallback(Runnable cbk) {
		callback = cbk;	
	}
}
