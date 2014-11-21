package br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor;

import org.eclipse.jetty.util.log.Log;

import br.usp.icmc.dilvan.swrlEditor.client.resources.Resources;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Atom.TYPE_ATOM;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.activity.CompositionActivity;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.mvp.AppActivityMapper;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.mvp.AppPlaceHistoryMapper;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.place.CompositionPlace;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.place.FilterPlace;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.place.OptionsPlace;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.place.VisualizationPlace;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.composition.PopUpLocationView;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.filter.PopUpLocationItemFilter;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
//import edu.stanford.bmir.protege.web.client.rpc.AbstractAsyncHandler;

//import edu.stanford.bmir.protege.web.client.model.GlobalSettings;
//import edu.stanford.bmir.protege.web.client.model.Project;
//import edu.stanford.bmir.protege.web.client.rpc.AbstractAsyncHandler;
//import edu.stanford.bmir.protege.web.client.rpc.ProjectConfigurationServiceManager;
//import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
//import edu.stanford.bmir.protege.web.client.rpc.data.layout.PortletConfiguration;
//import edu.stanford.bmir.protege.web.client.rpc.data.layout.ProjectConfiguration;
//import edu.stanford.bmir.protege.web.client.rpc.data.layout.TabColumnConfiguration;
//import edu.stanford.bmir.protege.web.client.rpc.data.layout.TabConfiguration;
//import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractEntityPortlet;
//import edu.stanford.bmir.protege.web.client.ui.util.UIUtil;

@SuppressWarnings("unchecked")
public class SwrlEditorPortlet extends SimplePanel { //AbstractEntityPortlet {

	//	class GetProjectConfigurationHandler extends AbstractAsyncHandler<ProjectConfiguration> {
	//		//private final Project project;
	//
	//		public GetProjectConfigurationHandler() { //Project project) {
	//			//this.project = project;
	//		}
	//
	//		@Override
	//		public void handleFailure(Throwable caught) {
	//			//GWT.log("There were errors at loading project configuration for " + project.getProjectName(), caught);
	//			UIUtil.hideLoadProgessBar();
	//			//com.google.gwt.user.client.Window.alert("Load project configuration for " + project.getProjectName()
	//			//		+ " failed. " + " Message: " + caught.getMessage());
	//		}
	//
	//		@Override
	//		public void handleSuccess(ProjectConfiguration config) {
	//
	//			for (TabConfiguration tab: config.getTabs()){
	//				if (tab.getName().equals(SwrlEditorTab.class.getName())){
	//
	//					for(TabColumnConfiguration colTab : tab.getColumns()){
	//						for(PortletConfiguration portletConfig : colTab.getPortlets()){
	//							if(portletConfig.getName().equals(SwrlEditorPortlet.class.getName())){
	//								clientFactory.setConfigOnLogin(portletConfig.getProperties());
	//							}
	//						}
	//					}
	//				}
	//
	//			}
	//		}
	//	}

	public static native void exportStaticMethod() /*-{
    $wnd.resourceChosenEvent =
       $entry(@br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.SwrlEditorPortlet::resourceChosenEvent(Ljava/lang/String;Ljava/lang/String;));
    }-*/;

	public static native void closeClsPropBrowsers() /*-{
		$wnd.app.closeClsPropBrowsers();
	}-*/;
	
	public static native void openClsPropBrowsers() /*-{
		$wnd.app.openClsPropBrowsers();
	}-*/;
	
	public static void resourceChosenEvent(String type, String name) {
		TYPE_ATOM atomType;
		if (type.equals("Class")) atomType = TYPE_ATOM.CLASS;
		else if (type.equals("ObjectProperty")) atomType = TYPE_ATOM.INDIVIDUAL_PROPERTY;
		else if (type.equals("DatatypeProperty")) atomType = TYPE_ATOM.DATAVALUE_PROPERTY;
		else return;
		
		// TODO: NOT good, it shouldn't be an if
		if (SwrlEditorPortlet.clientFactory2.getPlaceController().getWhere() instanceof FilterPlace)
			setSelectedPredicateFilter(atomType, name, 50,50);
		else
			setSelectedPredicate(atomType, name, 50,50);
	}
	
	public static void setSelectedPredicate(TYPE_ATOM typeAtom, String predicate,
			int left, int top) {
		PopUpLocationView panel = new PopUpLocationView(
				SwrlEditorPortlet.clientFactory2.getCompositionView());
		panel.setItemName(predicate, typeAtom);
		panel.setAutoHideEnabled(true);
		panel.setPopupPosition(left, top);
		panel.show();
	}
	
	public static void setSelectedPredicateFilter(TYPE_ATOM typeAtom, String predicate,
			int left, int top) {
		PopUpLocationItemFilter panel = new PopUpLocationItemFilter(clientFactory2.getFilterView());
		panel.setItemName(predicate, typeAtom);
		panel.setAutoHideEnabled(false);
		panel.setPopupPosition(left, top);
		panel.show();
	}

	//	SimplePanel pnl2;

	private ClientFactory clientFactory;
	static ClientFactory clientFactory2;
	//	private SimplePanel swrlEditorWidget;

	//	@Override
	//	public Collection<EntityData> getSelection() {
	//		return null;
	//	}

	private Place defaultPlace;
	//	HorizontalPanel page;
	//	SimplePanel pnl1;

	//	public SwrlEditorPortlet(final Project project) {
	//		super(project, true);
	//	}
	private String createHashURL(){

		//String ontology = project.getProjectName().replace(" ", "+").replace("%20", "+");
		String ontology = ClientFactory.DEFAULT_PROJECT_NAME.replace(" ", "+").replace("%20", "+");
		String tab = "SwrlEditorTab";

		String urlWebProtege = "ontology="
				+ ontology
				+ "&tab="+tab;

		String newURL;


		newURL = "#visualization:" + urlWebProtege;

		String hash = Window.Location.getHash();

		UrlBuilder builder = Location.createUrlBuilder();

		if (!hash.contains(ontology) || !hash.contains(tab)){
			builder.setHash(newURL);
			Window.Location.replace(builder.buildString());

		} else if (hash.trim().contains("#"+CompositionPlace.getNamePlace()+":") ||
				hash.trim().contains("#"+FilterPlace.getNamePlace()+":") ||
				hash.trim().contains("#"+OptionsPlace.getNamePlace()+":")){

		} else if (!hash.trim().contains(newURL)){
			builder.setHash(newURL);
			Window.Location.replace(builder.buildString());

		} else {
			builder.setHash(newURL);
			Window.Location.replace(builder.buildString());
		}
		return urlWebProtege;
	}


	//@Override
	public void initialize() {
		Resources.INSTANCE.swrleditor().ensureInjected();

		//		setSize("512px", "800px");
		//		setSize("100%", "100%");

		//		swrlEditorWidget = new SimplePanel();
		//		add(swrlEditorWidget);

		WaitingCreateToRun waiting = new WaitingCreateToRun(this, 1000) { //swrlEditorWidget, 1000) {
			@Override
			public void run() {
				SwrlEditorPortlet.this.loadSWRLEditor();
			}
		};
		waiting.start();
	}

	private void loadSWRLEditor() {

		String urlWebProtege = createHashURL();


		defaultPlace = new VisualizationPlace("");

		// Create ClientFactory using deferred binding so we can replace with
		// different
		// impls in gwt.xml
		clientFactory = GWT.create(ClientFactory.class);
		clientFactory2 = clientFactory;
		//clientFactory.setProject(project);
		clientFactory.setUserLogged(""); //GlobalSettings.getGlobalSettings().getUserName());

		clientFactory.setWritePermission(true);//project
		//.hasWritePermission(GlobalSettings.getGlobalSettings()
		//		.getUserName()));

		//clientFactory.setPortlet(this);
		clientFactory.setURLWebProtege(urlWebProtege);


		EventBus eventBus = clientFactory.getEventBus();
		PlaceController placeController = clientFactory.getPlaceController();

		// Start ActivityManager for the main widget with our ActivityMapper
		ActivityMapper activityMapper = new AppActivityMapper(clientFactory);
		ActivityManager activityManager = new ActivityManager(activityMapper,
				eventBus);
		activityManager.setDisplay(this); //swrlEditorWidget);

		// Start PlaceHistoryHandler with our PlaceHistoryMapper
		AppPlaceHistoryMapper historyMapper = GWT
				.create(AppPlaceHistoryMapper.class);
		PlaceHistoryHandler historyHandler = new PlaceHistoryHandler(
				historyMapper);
		historyHandler.register(placeController, eventBus, defaultPlace);

		// Goes to place represented on URL or default place
		historyHandler.handleCurrentHistory();

		exportStaticMethod();
		
		

	}
	


	//	@Override
	//	public void onLogin(String userName) {
	//		ProjectConfigurationServiceManager.getInstance().getProjectConfiguration(project.getProjectName(),
	//				userName, new GetProjectConfigurationHandler(project));
	//	}

	//	@Override
	//	public void onLogout(String userName) {}

	//	@Override
	//	public void onPermissionsChanged(Collection<String> permissions) {
	//		if (clientFactory != null){
	//			clientFactory.setUserLogged(GlobalSettings.getGlobalSettings().getUserName());
	//			clientFactory.setWritePermission(project
	//					.hasWritePermission(GlobalSettings.getGlobalSettings()
	//							.getUserName()));
	//		}
	//	}

	//	@Override
	//	public void reload() {}
}