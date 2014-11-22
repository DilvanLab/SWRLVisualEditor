package br.usp.icmc.dilvan.swrlEditor.client;

import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.SwrlEditorPortlet;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class SwrlGWT implements EntryPoint {
    /**
     * This is the entry point method.
     */
    @Override
    public void onModuleLoad() {

	final SwrlEditorPortlet port = new SwrlEditorPortlet();

	port.setSize("100%", "100%");
	RootPanel.get("swrlEditor").add(port);
	port.initialize();
    }
}

