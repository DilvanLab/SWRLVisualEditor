package br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view;

import java.util.List;

import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.activity.info.RuleInfo;

import com.google.gwt.user.client.ui.IsWidget;

/**
 *
 * @author Joao Paulo Orlando
 */
public interface InfoView extends IsWidget{
    public interface Presenter {
	void goToVisualization();
    }

    void setPresenter(Presenter presenter);

    void setRuleInfo(List<RuleInfo> ruleInfo);
}