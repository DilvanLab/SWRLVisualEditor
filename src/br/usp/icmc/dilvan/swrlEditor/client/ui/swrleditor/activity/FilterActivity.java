package br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.activity;

import java.util.ArrayList;

import br.usp.icmc.dilvan.swrlEditor.client.gwtWindow.Window;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.Filter;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Atom.TYPE_ATOM;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.ClientFactory;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.mvp.AppActivityMapper;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.place.FilterPlace;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.place.VisualizationPlace;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.util.UtilLoading;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.FilterView;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.OntologyView;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.filter.PopUpLocationItemFilter;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;


public class FilterActivity extends AbstractActivity implements FilterView.Presenter, OntologyView.Presenter  {
	private ClientFactory clientFactory;
	private AppActivityMapper activityMapper;
	
	public FilterActivity(FilterPlace place, ClientFactory clientFactory, AppActivityMapper activityMapper) {
		this.clientFactory = clientFactory;
		this.activityMapper = activityMapper;
	}

	/**
	 * Invoked by the ActivityManager to start a new Activity
	 */
	@Override
	public void start(AcceptsOneWidget containerWidget, EventBus eventBus) {
		UtilLoading.showLoadSWRLEditor();
		FilterView filterView = clientFactory.getFilterView();
		filterView.setPresenter(this);
		filterView.setFilter(activityMapper.getFilter());
		containerWidget.setWidget(filterView.asWidget());
		UtilLoading.hide();
	}

	@Override
	public void goToVisualization() {
		clientFactory.getPlaceController().goTo(new VisualizationPlace(clientFactory.getURLWebProtege()));
	}
	
	@Override
	public void search(Filter f) {
		activityMapper.setFilter(f);
		goToVisualization();
		
	}

	@Override
	public void setSelectedPredicate(TYPE_ATOM typeAtom, String value, int left, int top) {
		PopUpLocationItemFilter panel = new PopUpLocationItemFilter(clientFactory.getFilterView());
		panel.setItemName(value, typeAtom);
		panel.setAutoHideEnabled(true);
		panel.setPopupPosition(left, top);
		panel.show();
	}

	@Override
	public void getBuiltins() {
		clientFactory.getRpcService().getListBuiltins(clientFactory.getProjectName(), new AsyncCallback<ArrayList<String>>() {
			
			@Override
			public void onSuccess(ArrayList<String> result) {
				clientFactory.getFilterView().setBuiltins(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error fetching builtins");
			}
		});		
	}
}
