package br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.usp.icmc.dilvan.swrlEditor.client.resources.Resources;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.Filter;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.NameGroupAlgorithm;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.RuleSet;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.decisiontree.NodeDecisionTree;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Rule;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.WaitingCreateToRun;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.util.Options;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.util.UtilLoading;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.OptionsView;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.UtilView;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.VisualizationView;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.similarrules.SimilarRulePanel;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.similarrules.SimilarRulePanel.TYPE_VIEW_SIMILAR;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.visualization.SimpleRuleView;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.visualization.SimpleRuleViewAutism;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.visualization.SimpleRuleViewList;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.visualization.SimpleRuleViewSWRL;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.visualization.SimpleRuleViewText;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.visualization.VisualizationViewDecisionTree;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class VisualizationViewImpl extends Composite implements
		VisualizationView {

	private static VisualizationViewUiBinder uiBinder = GWT
			.create(VisualizationViewUiBinder.class);

	@UiField
	HorizontalPanel listFilterPanel;
	@UiField
	InlineLabel filterDescription;
	@UiField
	Button btnNewRule;
	@UiField
	Button btnInfo;
	@UiField
	Button btnOptions;
	@UiField
	Button btnEditFilter;
	@UiField
	Button btnRun;
	@UiField 
	Image imgWaiting;
	
	@UiField
	TabLayoutPanel tabVisualization;

	@UiField
	Label lblViewRule;
	@UiField
	Label lblTotalRule;

	@UiField
	ScrollPanel scrollList;
	@UiField
	ScrollPanel scrollText;
	@UiField
	ScrollPanel scrollSWRL;
	@UiField
	ScrollPanel scrollAutism;

	@UiField
	VerticalPanel pnlList;
	@UiField
	VerticalPanel pnlText;
	@UiField
	VerticalPanel pnlSWRL;
	@UiField
	VerticalPanel pnlAutism;
	

	// TODO fazer o pnlGroups autoHeigth
	@UiField
	ScrollPanel scrGroups;
	@UiField
	VerticalPanel pnlGroups;
	@UiField
	ListBox cboAlgorithmsGroups;
	@UiField
	ListBox cboNumberGroups;
	@UiField
	Button btnRefreshGroups;
	private VerticalPanel treeGroup;

	@UiField
	SimplePanel pnlDecisionTree;
	private VisualizationViewDecisionTree viewDecisionTree;
	
	private List<SimpleRuleView> simpleRulesList = null;
	private List<SimpleRuleView> simpleRulesText = null;
	private List<SimpleRuleView> simpleRulesSWRL = null;
	private List<SimpleRuleView> simpleRulesAutism = null;

	private List<SimpleRuleView> simpleRulesListRefresh = null;
	private List<SimpleRuleView> simpleRulesTextRefresh = null;
	private List<SimpleRuleView> simpleRulesSWRLRefresh = null;
	private List<SimpleRuleView> simpleRulesAutismRefresh = null;

	private List<SimpleRuleView> simpleRulesListGroups = null;

	private List<NameGroupAlgorithm> groupAlgorithmListAlgorithms = null;
	private String lastGroupAlgorithmSelected = "";
	private int lastNumberGroupsSelected = -1;

	private RuleSet rules;
	private int totalFilteredRules;

	private String ruleSelected = "";
	
	private boolean firstSetConfigs = false;

	private TYPE_VIEW typeView;
	public static String[] nameTabs = { "List", "Text", "SWRL", "Autism", "Groups", "Decision Tree" };

	interface VisualizationViewUiBinder extends
			UiBinder<Widget, VisualizationViewImpl> {
	}

	private Presenter presenter;
	private boolean writePermission;

	public VisualizationViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));

		simpleRulesList = null;
		simpleRulesText = null;
		simpleRulesSWRL = null;
		simpleRulesAutism = null;
		simpleRulesListGroups = null;

		simpleRulesListRefresh = new ArrayList<SimpleRuleView>();
		simpleRulesTextRefresh = new ArrayList<SimpleRuleView>();
		simpleRulesSWRLRefresh = new ArrayList<SimpleRuleView>();
		simpleRulesAutismRefresh = new ArrayList<SimpleRuleView>();

		lblTotalRule.setVisible(false);
		lblViewRule.setVisible(false);

		treeGroup = new VerticalPanel();
		treeGroup.setSize("100%", "100%");

		pnlGroups.add(treeGroup);
			
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void setWritePermission(boolean permission) {
		this.writePermission = permission;
		btnNewRule.setEnabled(writePermission);
		btnRun.setEnabled(writePermission);

		if (simpleRulesList != null)
			for (SimpleRuleView rw : simpleRulesList)
				rw.setWritePermission(permission);
		if (simpleRulesText != null)
			for (SimpleRuleView rw : simpleRulesText)
				rw.setWritePermission(permission);
		if (simpleRulesSWRL != null)
			for (SimpleRuleView rw : simpleRulesSWRL)
				rw.setWritePermission(permission);
		if (simpleRulesAutism != null)
			for (SimpleRuleView rw : simpleRulesAutism)
				rw.setWritePermission(permission);
					
		if (viewDecisionTree != null)
			viewDecisionTree.setWritePermission(writePermission);
	}

	@Override
	public void setTypeView(TYPE_VIEW typeView) {

		this.typeView = typeView;
		
		if (simpleRulesList != null)
			for (SimpleRuleView rw : simpleRulesList)
				rw.setTypeView(typeView);
		if (simpleRulesText != null)
			for (SimpleRuleView rw : simpleRulesText)
				rw.setTypeView(typeView);
		if (simpleRulesSWRL != null)
			for (SimpleRuleView rw : simpleRulesSWRL)
				rw.setTypeView(typeView);
		if (simpleRulesAutism != null)
			for (SimpleRuleView rw : simpleRulesAutism)
				rw.setTypeView(typeView);
	}

	@Override
	public void setConfiguration(Map<String, Object> config) {
		if (config != null){
			tabVisualization.getTabWidget(0).getParent().setVisible(Options.getBooleanOption(config, OptionsView.ListVisualizationBool, true));
			tabVisualization.getTabWidget(1).getParent().setVisible(Options.getBooleanOption(config, OptionsView.TextVisualizationBool, true));
			tabVisualization.getTabWidget(2).getParent().setVisible(Options.getBooleanOption(config, OptionsView.SWRLVisualizationBool, true));
			tabVisualization.getTabWidget(3).getParent().setVisible(false); //Options.getBooleanOption(config, OptionsView.AutismVisualizationBool, true));
			tabVisualization.getTabWidget(4).getParent().setVisible(Options.getBooleanOption(config, OptionsView.GroupsVisualizationBool, true));
			tabVisualization.getTabWidget(5).getParent().setVisible(Options.getBooleanOption(config, OptionsView.DecisionVisualizationBool, true));
			
			
			if (!firstSetConfigs){
				String tabselected = Options.getStringOption(config, OptionsView.DefaultTabVisualizationStr, "");
				if (tabselected.equals(OptionsView.tabVisualizationList))
					tabVisualization.selectTab(0);
				else if (tabselected.equals(OptionsView.tabVisualizationText))
					tabVisualization.selectTab(1);
				else if (tabselected.equals(OptionsView.tabVisualizationSWRL))
					tabVisualization.selectTab(2);
				else if (tabselected.equals(OptionsView.tabVisualizationAutism))
					tabVisualization.selectTab(3);
				else if (tabselected.equals(OptionsView.tabVisualizationGroups))
					tabVisualization.selectTab(4);
				else if (tabselected.equals(OptionsView.tabVisualizationDecisionTree))
					tabVisualization.selectTab(5);
				else
					tabVisualization.selectTab(0);
				
				if (writePermission)
					firstSetConfigs = true;
					
			}
			
		}
	}
	
	private void setNumberRulesView(int RuleView) {
		lblViewRule.setText(Integer.toString(RuleView));
		lblViewRule.setVisible(true);
		totalFilteredRules = RuleView;
	}

	private void setNumberRulesTotal(int RuleTotal) {
		lblTotalRule.setText(Integer.toString(RuleTotal));
		lblTotalRule.setVisible(true);

		int maxGroups = RuleTotal / 10;
		cboNumberGroups.clear();

		cboNumberGroups.addItem("Default", "0");
		for (int i = 2; i <= maxGroups; i++) {
			cboNumberGroups.addItem(i + " groups", String.valueOf(i));
		}
		cboNumberGroups.setSelectedIndex(0);
	}

	@Override
	public void setRuleSelected(String nameRule) {
		if (!nameRule.equals("")) {

			VerticalPanel pnlBase = null;
			List<SimpleRuleView> simpleRulesBase = null;
			ScrollPanel scrollBase = null;

			switch (tabVisualization.getSelectedIndex()) {
			case 0:
				pnlBase = pnlList;
				scrollBase = scrollList;
				simpleRulesBase = simpleRulesList;
				break;
			case 1:
				pnlBase = pnlText;
				scrollBase = scrollText;
				simpleRulesBase = simpleRulesText;
				break;
			case 2:
				pnlBase = pnlSWRL;
				scrollBase = scrollSWRL;
				simpleRulesBase = simpleRulesSWRL;
				break;
			case 3:
				pnlBase = pnlAutism;
				scrollBase = scrollAutism;
				simpleRulesBase = simpleRulesAutism;
				break;
			default:
				break;
			}

			SimpleRuleView panelSelect = null;

			if (pnlBase != null && simpleRulesBase != null	&& scrollBase != null) {
				for (SimpleRuleView panel : simpleRulesBase) {
					if (panel.getRuleName().equals(nameRule)) {
						panelSelect = panel;
						break;
					}

				}
			}
			if (panelSelect != null)
				WaitingForSetPositionScrool(panelSelect, scrollBase);

		}
	}

	private void WaitingForSetPositionScrool(final SimpleRuleView panelSelect,	final ScrollPanel scrollBase) {
		Timer timer = new Timer() {
			@Override
			public void run() {
				if (((panelSelect.getOffsetHeight() > 0) || (panelSelect.getOffsetWidth() > 0))) {

					scrollBase.setVerticalScrollPosition(0);

					int position = panelSelect.getAbsoluteTop() - (scrollBase.getAbsoluteTop());
					scrollBase.setVerticalScrollPosition(position);


				} else
					schedule(10);
			}
		};
		timer.schedule(10);
	}

	@Override
	public void setRuleSet(RuleSet rules) {
		this.rules = rules;

		setNumberRulesTotal(rules.size());
		setNumberRulesView(rules.size());

		switch (tabVisualization.getSelectedIndex()) {
		case 0:
			mountTabList();
			break;
		case 1:
			mountTabText();
			break;
		case 2:
			mountTabSWRL();
			break;
		case 3:
			mountTabAutism();
			break;
		case 4:
			mountTabGroups();
			break;
		case 5:
			mountTabDecisionTree();
			break;
		}
	}

	private void mountTabList() {
		if (simpleRulesList == null) {
			UtilLoading.hide();
			UtilLoading.showLoadRules();
			simpleRulesList = new ArrayList<SimpleRuleView>();

			for (Rule rl : rules) {
				SimpleRuleViewList rlView = new SimpleRuleViewList(rl,
						presenter, typeView);

				rlView.setWritePermission(writePermission);
				simpleRulesList.add(rlView);
			}

			waitingShowRules(simpleRulesList, pnlList);
		}
		refreshRulesView();
	}

	private void mountTabText() {
		if (simpleRulesText == null) {
			UtilLoading.hide();
			UtilLoading.showLoadRules();
			simpleRulesText = new ArrayList<SimpleRuleView>();

			for (Rule rl : rules) {
				SimpleRuleViewText rlView = new SimpleRuleViewText(rl,
						presenter, typeView);

				rlView.setWritePermission(writePermission);
				simpleRulesText.add(rlView);
			}

			waitingShowRules(simpleRulesText, pnlText);
		}
		refreshRulesView();
	}

	private void mountTabSWRL() {
		if (simpleRulesSWRL == null) {
			UtilLoading.hide();
			UtilLoading.showLoadRules();
			simpleRulesSWRL = new ArrayList<SimpleRuleView>();

			for (Rule rl : rules) {
				SimpleRuleViewSWRL rlView = new SimpleRuleViewSWRL(rl,
						presenter, typeView);

				rlView.setWritePermission(writePermission);
				simpleRulesSWRL.add(rlView);
			}

			waitingShowRules(simpleRulesSWRL, pnlSWRL);
		}
		refreshRulesView();
	}

	private void mountTabAutism() {
		if (simpleRulesAutism == null) {
			UtilLoading.hide();
			UtilLoading.showLoadRules();
			simpleRulesAutism = new ArrayList<SimpleRuleView>();

			for (Rule rl : rules) {
				SimpleRuleViewAutism rlView = new SimpleRuleViewAutism(rl,
						presenter, typeView);

				rlView.setWritePermission(writePermission);
				simpleRulesAutism.add(rlView);
			}

			waitingShowRules(simpleRulesAutism, pnlAutism);
		}
		refreshRulesView();

	}

	private void mountTabGroups() {
		UtilLoading.hide();
		UtilLoading.showLoadGroups();
		
		String algorithm = cboAlgorithmsGroups.getValue(cboAlgorithmsGroups
				.getSelectedIndex());
		int number = 0;
		if (cboNumberGroups.getSelectedIndex() >= 0)
			number = Integer.parseInt(cboNumberGroups.getValue(cboNumberGroups	
				.getSelectedIndex()));
		if (number == 0)
			number = Presenter.DEFAULT_NUMBER_GROUPS;

		if (tabVisualization.getOffsetHeight() > 0)
			if (cboNumberGroups.isEnabled()) {
				lastNumberGroupsSelected = number;
				presenter.getGroups(algorithm, number);
			} else {
				lastNumberGroupsSelected = 0;
				presenter.getGroups(algorithm, 0);
			}

		lastGroupAlgorithmSelected = algorithm;

	}

	@Override
	public void refreshRulesView() {
		switch (tabVisualization.getSelectedIndex()) {
		case 0:
			if (simpleRulesList != null) {
				filterRulesViews(simpleRulesList);
				refreshHeigthRuleView(simpleRulesListRefresh, pnlList);
				positionRuleView(pnlList);
			}
			break;
		case 1:
			if (simpleRulesText != null) {
				filterRulesViews(simpleRulesText);
				refreshHeigthRuleView(simpleRulesTextRefresh, pnlText);
				positionRuleView(pnlText);
			}
			break;
		case 2:
			if (simpleRulesSWRL != null) {
				filterRulesViews(simpleRulesSWRL);
				refreshHeigthRuleView(simpleRulesSWRLRefresh, pnlSWRL);
				positionRuleView(pnlSWRL);
			}
			break;
		case 3:
			if (simpleRulesAutism != null) {
				filterRulesViews(simpleRulesAutism);
				refreshHeigthRuleView(simpleRulesAutismRefresh, pnlAutism);
				positionRuleView(pnlAutism);
			}
			break;
		case 4:
			break;
		case 5:
			break;
		}
	}

	private void refreshHeigthRuleView(
			final List<SimpleRuleView> simpleRulesRefresh, VerticalPanel pnlBase) {

		WaitingCreateToRun waiting = new WaitingCreateToRun(pnlBase, 10) {
			@Override
			public void run() {
				for (SimpleRuleView rlView : simpleRulesRefresh)
					ajustRuleView(rlView);
				simpleRulesRefresh.clear();
			}
		};
		waiting.start();

	}
	
	private void positionRuleView(VerticalPanel pnlBase) {
		if (!ruleSelected.equals("")){
			WaitingCreateToRun waiting = new WaitingCreateToRun(pnlBase, 10) {
				@Override
				public void run() {
					setRuleSelected(ruleSelected);
				}
			};
			waiting.start();
		}
	}

	private void filterRulesViews(List<SimpleRuleView> simpleRules) {
		Filter filter = presenter.getFilter();

		int totalRules = rules.size();
		
		for (SimpleRuleView ruleView : simpleRules) {
			if (filterRulesView(ruleView.getRule(), filter)) {
				ruleView.setVisible(true);
			} else {
				ruleView.setVisible(false);
				totalRules--;
			}
		}
		setNumberRulesView(totalRules);
		mountFilters(filter);
	}

	private boolean filterRulesView(Rule rule, Filter filter) {
		if (!filter.contains(rule)) {
			return false;
		}
		return true;
	}

	private void mountFilters(Filter filter) {

		listFilterPanel.clear();

		if (filter.isEmpty()) {
			filterDescription.setVisible(true);
		} else {
			filterDescription.setVisible(false);
		}

		HTML lbl = new HTML(UtilView.formatFilter(filter.getLstAnd(), filter.getLstOr(), filter.getLstNot()));
		lbl.addStyleName(Resources.INSTANCE.swrleditor().itemFilter());
		listFilterPanel.add(lbl);
	}

	private void mountTabDecisionTree() {
		createViewDecisionTree();
		viewDecisionTree.setWritePermission(writePermission);
		presenter.getDecisionTree(viewDecisionTree.getAlgorithmName());
	}

	private void createViewDecisionTree() {
		if (viewDecisionTree == null) {
			viewDecisionTree = new VisualizationViewDecisionTree(presenter);
			pnlDecisionTree.add(viewDecisionTree);
		}
	}
	
	private void waitingShowRules(final List<SimpleRuleView> painelRules,
			final VerticalPanel pnlBase) {

		WaitingCreateToRun waiting = new WaitingCreateToRun(pnlBase, 10) {
			@Override
			public void run() {
				VisualizationViewImpl.this.showRules(painelRules, pnlBase);
			}
		};
		waiting.start();
	}

	private void showRules(List<SimpleRuleView> painelRules,
			VerticalPanel pnlBase) {

		// Clean the container and add the rules objects that
		pnlBase.clear();

		for (SimpleRuleView panel : painelRules) {
			pnlBase.add(panel);
			ajustRuleView(panel);
		}
		
		UtilLoading.hide();

	}

	@Override
	public void setGroupAlgorithmList(List<NameGroupAlgorithm> algorithms, Map<String, Object> config) {
		this.groupAlgorithmListAlgorithms = algorithms;
		cboAlgorithmsGroups.clear();
		
		String defaultAlg = Options.getStringOption(config, OptionsView.DefaultAlgorithmGroupsStr, "");
		int count = 0;
		for (NameGroupAlgorithm alg : groupAlgorithmListAlgorithms) {
			if (Options.getBooleanOption(config, Options.removeCharInvalidForNameOptions(OptionsView.AlgorithmGroupsStr_, alg.getName()), true)){
				cboAlgorithmsGroups.addItem(alg.getName());
				
				if (defaultAlg.equals(alg.getName())){
					cboAlgorithmsGroups.setSelectedIndex(count);
					cboNumberGroups.setEnabled(alg.isCanSetNumberOfGroups());
				}
				
				count++;
			}
		}
		if (defaultAlg.equals("")){
			cboAlgorithmsGroups.setSelectedIndex(0);
			if (algorithms.size()>0)
				cboNumberGroups.setEnabled(algorithms.get(0).isCanSetNumberOfGroups());
		}
		
		if (tabVisualization.getSelectedIndex() == 4){
			if (tabVisualization.getOffsetHeight() > 0){
				mountTabGroups();
			}
		}
	}

	@Override
	public void setGroups(ArrayList<ArrayList<String>> groups) {
		
		if (tabVisualization.getOffsetHeight() > 0)
			scrGroups.setSize(Integer.toString(tabVisualization.getOffsetWidth()-15)+"px", Integer.toString(tabVisualization.getOffsetHeight()-70)+"px");

		

		if (groups == null)
			return;

		if (simpleRulesListGroups == null) {
			simpleRulesListGroups = new ArrayList<SimpleRuleView>();
			for (Rule rl : rules) {
				SimpleRuleViewList rlView = new SimpleRuleViewList(rl,
						presenter, typeView);
				simpleRulesListGroups.add(rlView);
			}
		}

		int count = 1;
		treeGroup.clear();

		for (ArrayList<String> group : groups) {
			DisclosurePanel pnlClo = new DisclosurePanel("Group"
					+ Integer.toString(count++) + " (" + group.size()
					+ " rules)");
			pnlClo.getHeader().setStyleName(Resources.INSTANCE.swrleditor().headerNameClass());

			VerticalPanel pnlItens = new VerticalPanel();
			pnlClo.add(pnlItens);

			for (String ruleName : group) {

				SimpleRuleView ruleViewSelected = null;
				for (SimpleRuleView ruleView : simpleRulesListGroups)
					if (ruleView.getRuleName().equals(ruleName)) {
						ruleViewSelected = ruleView;
						break;
					}
				if (ruleViewSelected != null) {
					ruleViewSelected.show();
					ruleViewSelected.setPanelButtonVisible(false);
					ruleViewSelected.closeRule();
					pnlItens.add(ruleViewSelected);

				} else {
					pnlItens.add(new Label("Not found: Simple Rule --> "
							+ ruleName));
				}
			}
			treeGroup.add(pnlClo);
		}
		UtilLoading.hide();
	}

	@Override
	public void setDecisionTreeAlgorithmList(List<String> algorithms, Map<String, Object> config) {
		createViewDecisionTree();
		viewDecisionTree.setListAlgorithm(algorithms, config);
		
		if (tabVisualization.getSelectedIndex() == 5)
			if (tabVisualization.getOffsetHeight() > 0)
				viewDecisionTree.loadDecisionTree();
	}

	@Override
	public void setDecisionTree(NodeDecisionTree root) {
		createViewDecisionTree();
		viewDecisionTree.setTree(root);
	}

	@UiHandler("btnNewRule")
	void onBtnNewRuleClick(ClickEvent event) {
		presenter.goToNewRule();
	}

	@UiHandler("btnOptions")
	void onBtnOptionsClick(ClickEvent event) {
		presenter.goToOptions();
	}

	@UiHandler("btnInfo")
	void onBtnInfoClick(ClickEvent event) {
		presenter.goToInfo();
	}

	@UiHandler("btnEditFilter")
	void onBtnEditFilterClick(ClickEvent event) {
		presenter.goToFilter();
		
	}

	@UiHandler("btnRun")
	void onBtnRunClick(ClickEvent event) {
		imgWaiting.setVisible(true);
		presenter.runRules();
	}

	@UiHandler("tabVisualization")
	void onTabVisualizationSelection(SelectionEvent<Integer> event) {
		switch (event.getSelectedItem()) {
		case 0:
			if (rules == null)
				presenter.getRuleSet();
			else
				mountTabList();
			break;
		case 1:
			if (rules == null)
				presenter.getRuleSet();
			else
				mountTabText();
			break;
		case 2:
			if (rules == null)
				presenter.getRuleSet();
			else
				mountTabSWRL();
			break;
		case 3:
			if (rules == null)
				presenter.getRuleSet();
			else
				mountTabAutism();
			break;
		case 4:
			if (groupAlgorithmListAlgorithms != null){
				mountTabGroups();
			}

			break;
		case 5:
			if (viewDecisionTree == null)
				mountTabDecisionTree();
			else{
				viewDecisionTree.loadDecisionTree();
			}
			break;
		}
	}

	@UiHandler("tabVisualization")
	void onTabVisualizationBeforeSelection(BeforeSelectionEvent<Integer> event) {
		final int marginAcceptance = 0;
		switch (tabVisualization.getSelectedIndex()) {
		case 0:
			if (simpleRulesList != null)
				for (SimpleRuleView panel : simpleRulesList) {
					if ((panel.getAbsoluteTop() - marginAcceptance) >= scrollList
							.getAbsoluteTop()) {
						ruleSelected = panel.getRuleName();
						break;
					}
				}
			break;
		case 1:
			if (simpleRulesText != null)
				for (SimpleRuleView panel : simpleRulesText) {
					if ((panel.getAbsoluteTop() - marginAcceptance) >= scrollText
							.getAbsoluteTop()) {
						ruleSelected = panel.getRuleName();
						break;
					}
				}
			break;
		case 2:
			if (simpleRulesSWRL != null)
				for (SimpleRuleView panel : simpleRulesSWRL) {
					if ((panel.getAbsoluteTop() - marginAcceptance) >= scrollSWRL
							.getAbsoluteTop()) {
						ruleSelected = panel.getRuleName();
						break;
					}
				}
			break;
		case 3:
			if (simpleRulesAutism != null)
				for (SimpleRuleView panel : simpleRulesAutism) {
					if ((panel.getAbsoluteTop() - marginAcceptance) >= scrollAutism
							.getAbsoluteTop()) {
						ruleSelected = panel.getRuleName();
						break;
					}
				}
			break;
		default:
			ruleSelected = "";
			break;
		}
	}

	@UiHandler("btnRefreshGroups")
	void onBtnRefreshGroupsClick(ClickEvent event) {

		String algorithm = cboAlgorithmsGroups.getValue(cboAlgorithmsGroups
				.getSelectedIndex());
		int number = Integer.parseInt(cboNumberGroups.getValue(cboNumberGroups
				.getSelectedIndex()));
		if (number == 0)
			number = Presenter.DEFAULT_NUMBER_GROUPS;

		if (!cboNumberGroups.isEnabled())
			number = 0;

		if ((!lastGroupAlgorithmSelected.equals(algorithm))
				|| (lastNumberGroupsSelected != number)) {
			mountTabGroups();
		}

	}

	@UiHandler("cboAlgorithmsGroups")
	void onCboAlgorithmsGroupsChange(ChangeEvent event) {
		cboNumberGroups.setEnabled(groupAlgorithmListAlgorithms.get(
				cboAlgorithmsGroups.getSelectedIndex())
				.isCanSetNumberOfGroups());
	}

	@Override
	public void showSimilarRules(String nameRule, List<Rule> result) {
		DialogBox panel = new DialogBox(true);
		SimilarRulePanel similarRulePanel = new SimilarRulePanel(result);
		similarRulePanel.setPopUp(panel);

		if (tabVisualization.getSelectedIndex() == 0)
			similarRulePanel.showListRules(nameRule, typeView, null,
					TYPE_VIEW_SIMILAR.EDITOR);
		else if (tabVisualization.getSelectedIndex() == 1)
			similarRulePanel.showListRules(nameRule, typeView, null,
					TYPE_VIEW_SIMILAR.TEXT);
		else if (tabVisualization.getSelectedIndex() == 2)
			similarRulePanel.showListRules(nameRule, typeView, null,
					TYPE_VIEW_SIMILAR.SWRL);
		else if (tabVisualization.getSelectedIndex() == 3)
			similarRulePanel.showListRules(nameRule, typeView, presenter,
					TYPE_VIEW_SIMILAR.AUTISM);

		similarRulePanel.setPopUpConfiguration(panel);
	}

	@Override
	public void addRuleEvent(int index, Rule rule) {
		Filter filter = presenter.getFilter();
		boolean isVisible = filterRulesView(rule, filter);

		if (simpleRulesList != null) {
			SimpleRuleViewList rlView = new SimpleRuleViewList(rule, presenter,
					typeView);
			createRuleViewInIndex(index, pnlList, rlView, simpleRulesList,
					isVisible, simpleRulesListRefresh);
		}
		if (simpleRulesText != null) {
			SimpleRuleViewText rlView = new SimpleRuleViewText(rule, presenter,
					typeView);
			createRuleViewInIndex(index, pnlText, rlView, simpleRulesText,
					isVisible, simpleRulesTextRefresh);
		}
		if (simpleRulesSWRL != null) {
			SimpleRuleViewSWRL rlView = new SimpleRuleViewSWRL(rule, presenter,
					typeView);
			createRuleViewInIndex(index, pnlSWRL, rlView, simpleRulesSWRL,
					isVisible, simpleRulesSWRLRefresh);
		}
		if (simpleRulesAutism != null) {
			SimpleRuleViewAutism rlView = new SimpleRuleViewAutism(rule,
					presenter, typeView);
			createRuleViewInIndex(index, pnlAutism, rlView, simpleRulesAutism,
					isVisible, simpleRulesAutismRefresh);
		}

		setNumberRulesTotal(rules.size());
		if (isVisible)
			setNumberRulesView(totalFilteredRules + 1);
	}

	private void createRuleViewInIndex(int index, VerticalPanel pnlBase,
			SimpleRuleView rlView, List<SimpleRuleView> simpleRules,
			boolean isVisible, List<SimpleRuleView> simpleRulesRefresh) {
		rlView.setWritePermission(writePermission);
		rlView.setVisible(isVisible);
		simpleRules.add(index, rlView);
		pnlBase.insert(rlView, index);
		ajustRuleView(rlView);
		simpleRulesRefresh.add(rlView);
	}

	@Override
	public void deleteRuleEvent(int index, Rule rule) {

		Filter filter = presenter.getFilter();
		boolean isVisible = filterRulesView(rule, filter);

		if (simpleRulesList != null) {
			simpleRulesList.remove(index);
			pnlList.remove(index);
		}
		if (simpleRulesText != null) {
			simpleRulesText.remove(index);
			pnlText.remove(index);
		}
		if (simpleRulesSWRL != null) {
			simpleRulesText.remove(index);
			pnlSWRL.remove(index);
		}
		if (simpleRulesAutism != null) {
			simpleRulesText.remove(index);
			pnlAutism.remove(index);
		}

		setNumberRulesTotal(rules.size() - 1);
		if (isVisible)
			setNumberRulesView(totalFilteredRules - 1);
	}

	private void ajustRuleView(SimpleRuleView ruleview) {
		ruleview.show();
	}

	@Override
	public void finishedRun() {
		imgWaiting.setVisible(false);
	}

}
