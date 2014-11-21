package br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.impl;

import java.util.List;

import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.activity.info.RuleInfo;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.InfoView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;


import com.google.gwt.user.client.ui.Button;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.event.dom.client.ClickEvent;


public class InfoViewImpl extends DialogBox implements InfoView{

	private static InfoViewImplUiBinder uiBinder = GWT
			.create(InfoViewImplUiBinder.class);

	@UiField SimplePanel tabInformation;
	@UiField(provided=true) CellTable<RuleInfo> cellTable = new CellTable<RuleInfo>();
	@UiField Button btnOK1;
	@UiField Button btnOK2;
	
	private Presenter presenter;
	
	
	interface InfoViewImplUiBinder extends UiBinder<Widget, InfoViewImpl> {
	}

	public InfoViewImpl() {
		setWidget(uiBinder.createAndBindUi(this));
		MontInformationTab();
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void setRuleInfo(List<RuleInfo> ruleInfo) {
		// Set the total row count. This isn't strictly necessary, but it affects
		// paging calculations, so its good habit to keep the row count up to date.
		cellTable.setRowCount(ruleInfo.size(), true);

		// Push the data into the widget.
		cellTable.setRowData(0, ruleInfo);
	}
	
	
	private void MontInformationTab() {
		// Add a info column to show the information of table.
		TextColumn<RuleInfo> infoColumn = new TextColumn<RuleInfo>() {
			@Override
			public String getValue(RuleInfo object) {
				return object.info;
			}
		};
		cellTable.addColumn(infoColumn, "Information");

		// Add a total column to show the number of total elements.
		TextColumn<RuleInfo> ruleColumn = new TextColumn<RuleInfo>() {
			@Override
			public String getValue(RuleInfo object) {
				if (object.total >= 0)
					return String.valueOf(object.total);
				return "-";
			}
		};
		cellTable.addColumn(ruleColumn, "Total");

		// Add a antecedent column to show the number of antecedent elements.
		TextColumn<RuleInfo> antecedentColumn = new TextColumn<RuleInfo>() {
			@Override
			public String getValue(RuleInfo object) {
				if (object.antecedent >= 0)
					return String.valueOf(object.antecedent);
				return "-";
			}
		};
		cellTable.addColumn(antecedentColumn, "Antecedent");

		// Add a consequent column to show the number of consequent elements.
		TextColumn<RuleInfo> consequentColumn = new TextColumn<RuleInfo>() {
			@Override
			public String getValue(RuleInfo object) {
				if (object.consequent >= 0)
					return String.valueOf(object.consequent);
				return "-";
			}
		};
		cellTable.addColumn(consequentColumn, "Consequent");
	}
	
	@UiHandler({"btnOK1", "btnOK2"})
	void onBtnOKClick(ClickEvent event) {
		presenter.goToVisualization();
		
	}
}
