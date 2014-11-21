package br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.visualization.decisiontree;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;

public class NodeToolTip  {

	private Tooltip tooltip;

	public NodeToolTip() {}

	public void show(String text, int delay, String styleName, int offsetX, int offsetY) {
		if (tooltip != null) {
			tooltip.hide();
		}

		tooltip = new Tooltip(offsetX, offsetY, text, delay, styleName);
		tooltip.show();
	}

	public void hide() {
		if (tooltip != null) {
			tooltip.hide();
		}
	}

	
	
	private static class Tooltip extends PopupPanel {
		private int delay;

		public Tooltip(int offsetX, int offsetY, 
				final String text, final int delay, final String styleName) {
			super(true);
			this.delay = delay;

			HTML contents = new HTML(text);
			add(contents);

			setPopupPosition(offsetX, offsetY);
			setStyleName(styleName);

		}

		public void show() {
			super.show();

			Timer t = new Timer() {

				public void run() {
					Tooltip.this.hide();
				}

			};
			t.schedule(delay);
		}
	}
}