package br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor;

import java.util.Map;

import br.usp.icmc.dilvan.swrlEditor.client.javafx.AbstractAsyncHandler;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.RuleEvents;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.RuleSet;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.CompositionView;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.FilterView;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.InfoView;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.OptionsView;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.VisualizationView;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;

//import edu.stanford.bmir.protege.web.client.rpc.AbstractAsyncHandler;


public interface ClientFactory
{
	static final String DEFAULT_PROJECT_NAME = "swrlFX";

	void addEventsViews(RuleSet rules, RuleEvents events);
	CompositionView getCompositionView();
	EventBus getEventBus();

	FilterView getFilterView();
	InfoView getInfoView();
	OptionsView getOptionsView();
	PlaceController getPlaceController();
	//void setPortlet(AbstractEntityPortlet portlet);
	Map<String, Object> getPortletConfiguration();

	//void setProject(Project project);
	//Project getProject();
	String getProjectName();
	SWRLServiceAsync getRpcService();


	String getURLWebProtege();
	String getUserLogged();

	VisualizationView getVisualizationView();
	void saveOptions(AbstractAsyncHandler<Void> saveHandler);


	void setConfigOnLogin(Map<String, Object> properties);
	void setOption(String name, Object value);

	void setURLWebProtege(String urlWebProtege);
	void setUserLogged(String User);
	void setWritePermission(boolean hasWritePermission);
}
