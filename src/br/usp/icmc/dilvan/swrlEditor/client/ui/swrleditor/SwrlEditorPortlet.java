package br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor;

import br.usp.icmc.dilvan.swrlEditor.client.resources.Resources;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Atom.TYPE_ATOM;
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
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.ui.SimplePanel;

@SuppressWarnings("unchecked")
public class SwrlEditorPortlet extends SimplePanel {

    public static native void closeClsPropBrowsers() /*-{
	$wnd.app.closeClsPropBrowsers();
    }-*/;

    public static native void exportStaticMethod() /*-{
    $wnd.resourceChosenEvent =
       $entry(@br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.SwrlEditorPortlet::resourceChosenEvent(Ljava/lang/String;Ljava/lang/String;));
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

    private ClientFactory clientFactory;
    static ClientFactory clientFactory2;
    private Place defaultPlace;

    private String createHashURL(){

	String ontology = ClientFactory.DEFAULT_PROJECT_NAME.replace(" ", "+").replace("%20", "+");
	String tab = "SwrlEditorTab";

	String urlWebProtege = "ontology=" + ontology + "&tab="+tab;

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

    public void initialize() {
	Resources.INSTANCE.swrleditor().ensureInjected();

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
	clientFactory.setUserLogged(""); //GlobalSettings.getGlobalSettings().getUserName());

	clientFactory.setWritePermission(true);//project
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
}