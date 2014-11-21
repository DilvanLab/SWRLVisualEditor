package br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.visualization.decisiontree;

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;

public interface NodeLabelListener {
	void onClick(Widget sender, Event event);
	void onRightClick(Widget sender, Event event);

	void onMouseMove(Widget sender, Event event);
	void onMouseOut(Widget sender, Event event);

	void onShowToolTip(Widget sender, Event event);
}
