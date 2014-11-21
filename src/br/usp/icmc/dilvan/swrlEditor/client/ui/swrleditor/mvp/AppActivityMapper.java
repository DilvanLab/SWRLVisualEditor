package br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.mvp;

import java.util.ArrayList;
import java.util.List;

import br.usp.icmc.dilvan.swrlEditor.client.gwtWindow.Window;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.Filter;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.NameGroupAlgorithm;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.RuleEvents;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.RuleSet;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.ClientFactory;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.activity.CompositionActivity;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.activity.FilterActivity;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.activity.OptionsActivity;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.activity.VisualizationActivity;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.place.CompositionPlace;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.place.FilterPlace;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.place.OptionsPlace;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.place.VisualizationPlace;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;


public class AppActivityMapper implements ActivityMapper {

	private ClientFactory clientFactory;

	//TODO separar dados do ActivityMapper, criar uma classe que contenha todos eles
	private RuleSet rules = null;
	private Filter filter;
	
	private List<String> similarRulesAlgorithms;
	private List<String> suggestTermsAlgorithms;
	private List<NameGroupAlgorithm> groupsAlgorithms;
	private List<String> decisionTreeRulesAlgorithms;
	
	private Timer eventsTimer = null;	
	
	private boolean waitEventServer = false;
	
	
	private String tempAntecedent = "";
	
	/**
	 * AppActivityMapper associates each Place with its corresponding
	 * {@link Activity}
	 * 
	 * @param clientFactory
	 *            Factory to be passed to activities
	 */
	public AppActivityMapper(ClientFactory clientFactory) {
		super();
		this.clientFactory = clientFactory;
		filter = new Filter();
		getGroupsAlgorithmsList();
		getDecisionTreeAlgorithmsList();
		getSuggestTermsAlgorithmsList();
		getSimilarRulesAlgorithmsList();
	}

	public RuleSet getRules() {
		return rules;
	}

	public void setRules(RuleSet rules) {
		this.rules = rules;
		startGetSWRLEventsTimer();
	}

	public Filter getFilter() {
		return filter;
	}

	public void setFilter(Filter filter) {
		this.filter = filter;
	}
	
	public void getSimilarRulesAlgorithmsList() {
		clientFactory.getRpcService().getSimilarRulesAlgorithmsList(
				new AsyncCallback<ArrayList<String>>() {
					public void onSuccess(ArrayList<String> result) {
						similarRulesAlgorithms = result;
					}

					public void onFailure(Throwable caught) {
						Window.alert("Error fetching DecisionTree Algorithm List");
					}
				});
	}
	public void getSuggestTermsAlgorithmsList() {
		clientFactory.getRpcService().getSuggestTermsAlgorithmsList(
				new AsyncCallback<ArrayList<String>>() {
					public void onSuccess(ArrayList<String> result) {
						suggestTermsAlgorithms = result;
					}

					public void onFailure(Throwable caught) {
						Window.alert("Error fetching DecisionTree Algorithm List");
					}
				});
	}
	public void getGroupsAlgorithmsList() {
		clientFactory.getRpcService().getGroupAlgorithmsList(
				new AsyncCallback<ArrayList<NameGroupAlgorithm>>() {
					public void onSuccess(ArrayList<NameGroupAlgorithm> result) {
						groupsAlgorithms = result;
					}

					public void onFailure(Throwable caught) {
						Window.alert("Error fetching Group Algorithm List");
					}
				});
	}
	public void getDecisionTreeAlgorithmsList() {
		clientFactory.getRpcService().getDecisionTreeAlgorithmsList(
				new AsyncCallback<ArrayList<String>>() {
					public void onSuccess(ArrayList<String> result) {
						decisionTreeRulesAlgorithms = result;
					}

					public void onFailure(Throwable caught) {
						Window.alert("Error fetching DecisionTree Algorithm List");
					}
				});
	}
	
	public List<String> getSimilarRulesAlgorithms() {
		return similarRulesAlgorithms;
	}

	public List<String> getSuggestTermsAlgorithms() {
		return suggestTermsAlgorithms;
	}

	public List<NameGroupAlgorithm> getGroupsAlgorithms() {
		return groupsAlgorithms;
	}

	public List<String> getGroupsAlgorithmsStr() {
		List<String> result = new ArrayList<String>();
		for (NameGroupAlgorithm nga: getGroupsAlgorithms() )
			result.add(nga.getName());
		return result;
	}
	
	public List<String> getDecisionTreeAlgorithms(){
		return decisionTreeRulesAlgorithms;
	}

	@Override
	public Activity getActivity(Place place) {
		if (place instanceof VisualizationPlace)
			return new VisualizationActivity((VisualizationPlace) place, clientFactory, this);
		else if (place instanceof CompositionPlace)
			return new CompositionActivity((CompositionPlace) place, clientFactory, this);	
		else if (place instanceof OptionsPlace)
			return new OptionsActivity((OptionsPlace) place, clientFactory, this);
		else if (place instanceof FilterPlace)
			return new FilterActivity((FilterPlace) place, clientFactory, this);

		return null;
	}
	
	public void forcedSWRLEvents() {
		getSWRLEventsFromServer();
	}
	
	private void startGetSWRLEventsTimer() {
		if (eventsTimer == null) {
			eventsTimer = new Timer() {
				@Override
				public void run() {
					getSWRLEventsFromServer();
				}
			};
			eventsTimer.scheduleRepeating(5000);
		}
	}

	private void getSWRLEventsFromServer(){
		
		if (!waitEventServer){
			if (rules != null){
				waitEventServer = true;

				clientFactory.getRpcService().getRuleEvents(clientFactory.getProjectName(), rules.getVersionOntology(), new AsyncCallback<RuleEvents>() {
					public void onSuccess(RuleEvents result) {

						if (result.size() > 0)
							clientFactory.addEventsViews(rules, result);
						
						waitEventServer = false;
					}

					public void onFailure(Throwable caught) {
						Window.alert("Error fetching rule information details");
						waitEventServer = false;
					}
				});
				
			}
		}
	}
	
	public void setNewAntecedent(String antecedent) {
		this.tempAntecedent = antecedent;
	}

	public String getNewAntecedent() {
		return this.tempAntecedent;
	}
}
