package br.usp.icmc.dilvan.swrlEditor.client;

//import br.usp.icmc.dilvan.swrlEditor.shared.FieldVerifier;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.SwrlEditorPortlet;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.activity.CompositionActivity;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class SwrlGWT implements EntryPoint {

	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	//	private final GreetingServiceAsync greetingService = GWT
	//			.create(GreetingService.class);

	/**
	 * This is the entry point method.
	 */
	@Override
	public void onModuleLoad() {

		final SwrlEditorPortlet port = new SwrlEditorPortlet();
		port.setSize("1024px", "512px");
		RootPanel.get("swrlFX").add(port);
		port.initialize();
		//		setSize("512px", "800px");

		//		port.setStylePrimaryName("div.swrlFX");
		//		port.setHeight(RootPanel.get("swrlFX").getOffsetHeight()+"px");
		//		port.setWidth(RootPanel.get("swrlFX").getOffsetWidth()+"px");


		// Set the focus on the widget after setup completes.


	}
}

