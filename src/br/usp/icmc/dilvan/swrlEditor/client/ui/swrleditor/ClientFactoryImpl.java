package br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor;

import java.util.HashMap;
import java.util.Map;

import br.usp.icmc.dilvan.swrlEditor.client.javafx.AbstractAsyncHandler;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.RuleEvent;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.RuleEvent.TYPE_EVENT;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.RuleEvents;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.RuleSet;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.place.CompositionPlace;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.place.OptionsPlace;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.place.VisualizationPlace;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.CompositionView;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.FilterView;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.InfoView;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.OntologyView;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.OptionsView;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.VisualizationView;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.composition.CompositionTabEditorView;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.composition.CompositionTabSWRLView;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.impl.CompositionViewImpl;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.impl.FilterViewImpl;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.impl.InfoViewImpl;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.impl.OntologyViewImpl;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.impl.OptionsViewImpl;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.impl.VisualizationViewImpl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.place.shared.PlaceController;

//import edu.stanford.bmir.protege.web.client.rpc.AbstractAsyncHandler;
//import edu.stanford.bmir.protege.web.client.model.Project;
//import edu.stanford.bmir.protege.web.client.rpc.AbstractAsyncHandler;
//import edu.stanford.bmir.protege.web.client.rpc.ProjectConfigurationServiceManager;
//import edu.stanford.bmir.protege.web.client.rpc.data.layout.ProjectConfiguration;
//import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractEntityPortlet;
//import edu.stanford.bmir.protege.web.client.ui.util.UIUtil;

public class ClientFactoryImpl implements ClientFactory{

	private final Map<String,Object> _properties = new HashMap<String,Object>();

	private String urlWebProtege;

	private String userLogged;

	private EventBus eventBus;
	private PlaceController placeController;
	private SWRLServiceAsync rpcService;

	private VisualizationView visualizationView;
	private CompositionView compositionView;
	private OptionsView optionsView;
	private InfoView infoView;
	private FilterView filterView;

	private OntologyView ontologyViewFilter;
	private OntologyView ontologyViewComposition;

	private CompositionTabEditorView compositionEditorTab;
	private CompositionTabSWRLView compositionSWRLTab;

	private boolean hasWritePermission;


	//private final Project project = null;
	//private final AbstractEntityPortlet portlet = null;

	@Override
	public void addEventsViews(RuleSet rules, RuleEvents events) {
		if (visualizationView == null){
			for (RuleEvent re : events){
				if (re.getTypeEvent() == TYPE_EVENT.INSERT){
					int index = rules.getIndexToInsertRule(re.getRule().getNameRule());
					rules.add(index, re.getRule());
				}else if (re.getTypeEvent() == TYPE_EVENT.EDIT){
					int index = rules.getRule(re.getRule().getNameRule());
					if (index >= 0) {
						rules.remove(index);
					} else {
						System.out.println("Erro: Index invalido");
					}

					int newIndex = rules.getIndexToInsertRule(re.getRule().getNameRule());
					rules.add(newIndex, re.getRule());
				}
				else if (re.getTypeEvent() == TYPE_EVENT.DELETE){
					int index = rules.getRule(re.getRule().getNameRule());

					if (index >= 0) {
						rules.remove(index);
					} else {
						System.out.println("Erro: Index invalido");
					}
				}
			}
			rules.setVersionOntology(events.getVersionOntology());
		}else{
			for (RuleEvent re : events){
				if (re.getTypeEvent() == TYPE_EVENT.INSERT){
					int index = rules.getIndexToInsertRule(re.getRule().getNameRule());
					rules.add(index, re.getRule());

					visualizationView.addRuleEvent(index, rules.get(index));

				}else if (re.getTypeEvent() == TYPE_EVENT.EDIT){

					int index = rules.getRule(re.getOldRuleName());
					if (index >= 0){
						visualizationView.deleteRuleEvent(index, rules.get(index));
						rules.remove(index);
					} else {
						System.out.println("Erro: Index invalido");
					}


					int newIndex = rules.getIndexToInsertRule(re.getRule().getNameRule());
					rules.add(newIndex, re.getRule());
					visualizationView.addRuleEvent(newIndex, rules.get(newIndex));
				}
				else if (re.getTypeEvent() == TYPE_EVENT.DELETE){
					int index = rules.getRule(re.getOldRuleName());

					if (index >= 0){
						visualizationView.deleteRuleEvent(index, rules.get(index));
						rules.remove(index);
					} else {
						System.out.println("Erro: Index invalido");
					}

				}
			}
			rules.setVersionOntology(events.getVersionOntology());
		}
	}

	private  CompositionTabEditorView getCompositionTabEditorView(){
		if (compositionEditorTab == null) {
			compositionEditorTab = new CompositionTabEditorView();
		}

		return compositionEditorTab;
	}

	private  CompositionTabSWRLView getCompositionTabSWRLView(){
		if (compositionSWRLTab == null) {
			compositionSWRLTab = new CompositionTabSWRLView();
		}

		return compositionSWRLTab;
	}

	@Override
	public CompositionView getCompositionView() {
		if (compositionView == null){
			compositionView = new CompositionViewImpl(getOntologyViewComposition(), getCompositionTabEditorView(), getCompositionTabSWRLView());
			compositionView.setWritePermission(hasWritePermission);
		}
		return compositionView;
	}

	@Override
	public EventBus getEventBus()
	{
		if (eventBus == null) {
			eventBus = new SimpleEventBus();
		}
		return eventBus;
	}

	@Override
	public FilterView getFilterView() {
		if (filterView == null){
			filterView = new FilterViewImpl(getOntologyViewFilter());
		}
		return filterView;
	}

	@Override
	public InfoView getInfoView() {
		if (infoView == null){
			infoView = new InfoViewImpl();

			((InfoViewImpl)infoView).setAutoHideEnabled(true);

			VisualizationViewImpl visua = (VisualizationViewImpl) getVisualizationView();

			((InfoViewImpl)infoView).setPopupPosition((visua.getAbsoluteLeft()-700)+ (visua.getOffsetWidth()/2), visua.getAbsoluteTop());


		}
		return infoView;
	}

	private OntologyView getOntologyViewComposition() {
		if (ontologyViewComposition == null){
			ontologyViewComposition = new OntologyViewImpl();
			//ontologyViewComposition.setProjectProtege();//project);
		}

		return ontologyViewComposition;
	}


	private OntologyView getOntologyViewFilter() {
		if (ontologyViewFilter == null){
			ontologyViewFilter = new OntologyViewImpl();
			//ontologyViewFilter.setProjectProtege();//project);
		}

		return ontologyViewFilter;
	}

	@Override
	public OptionsView getOptionsView() {
		if (optionsView == null){
			optionsView = new OptionsViewImpl();
			optionsView.setWritePermission(hasWritePermission);
		}
		return optionsView;
	}

	@Override
	public PlaceController getPlaceController()
	{
		if (placeController == null) {
			placeController = new PlaceController(eventBus);
		}
		return placeController;
	}

	@Override
	public Map<String, Object> getPortletConfiguration(){
		//return portlet.getPortletConfiguration().getProperties();
		return _properties;
	}

	//	@Override
	//	public Project getProject() {
	//		return project;
	//	}

	@Override
	public String getProjectName() {
		//return project.getProjectName();
		return DEFAULT_PROJECT_NAME;
	}

	@Override
	public SWRLServiceAsync getRpcService() {
		if (rpcService == null) {
			rpcService = GWT.create(SWRLService.class);
		}
		return rpcService;
	}

	@Override
	public String getURLWebProtege() {
		return urlWebProtege;
	}

	@Override
	public String getUserLogged() {
		return userLogged;
	}

	@Override
	public VisualizationView getVisualizationView() {
		if (visualizationView == null){
			visualizationView = new VisualizationViewImpl();
			visualizationView.setWritePermission(hasWritePermission);
		}
		return visualizationView;
	}

	@Override
	public void saveOptions(AbstractAsyncHandler<Void> saveHandler) {
		//		ProjectConfiguration config = project.getProjectConfiguration();
		//		config.setOntologyName(project.getProjectName());
		//
		//		ProjectConfigurationServiceManager.getInstance().saveProjectConfiguration(
		//				getProjectName(), getUserLogged(), config, saveHandler);
	}


	@Override
	public void setConfigOnLogin(Map<String, Object> properties) {
		if (getPlaceController().getWhere().getClass().getName().equals(VisualizationPlace.class.getName()))
			if (visualizationView != null) {
				visualizationView.setConfiguration(properties);
			}

		if (getPlaceController().getWhere().getClass().getName().equals(CompositionPlace.class.getName()))
			if (compositionView != null) {
				compositionView.setConfiguration(properties);
			}

		if (getPlaceController().getWhere().getClass().getName().equals(OptionsPlace.class.getName()))
			if (optionsView != null) {
				optionsView.setOptions(properties);
			}
	}

	@Override
	public void setOption(String name, Object value) {
		//UIUtil.setConfigurationPropertyValue(portlet.getPortletConfiguration(), name, value);
	}

	//	@Override
	//	public void setPortlet(AbstractEntityPortlet portlet) {
	//		this.portlet = portlet;
	//
	//		if (optionsView != null) {
	//			optionsView.setOptions(portlet.getPortletConfiguration().getProperties());
	//		}
	//	}

	//	@Override
	//	public void setProject(Project project) {
	//		this.project = project;
	//	}

	@Override
	public void setURLWebProtege(String urlWebProtege) {
		this.urlWebProtege = urlWebProtege;
	}

	@Override
	public void setUserLogged(String User) {
		userLogged = User;
		if (userLogged == null) {
			userLogged = "";
		}
	}

	@Override
	public void setWritePermission(boolean hasWritePermission) {
		this.hasWritePermission = hasWritePermission;

		if (visualizationView != null) {
			visualizationView.setWritePermission(this.hasWritePermission);
		}

		if (compositionView != null) {
			compositionView.setWritePermission(this.hasWritePermission);
		}

		if (optionsView != null) {
			optionsView.setWritePermission(this.hasWritePermission);
		}

	}

}
