package br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.place;

import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.SwrlEditorPortlet;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class VisualizationPlace extends DefaultPlace {

    @Prefix (value="visualization")
    public static class Tokenizer implements PlaceTokenizer<VisualizationPlace> {

	@Override
	public VisualizationPlace getPlace(String token) {
	    return new VisualizationPlace(token);
	}

	@Override
	public String getToken(VisualizationPlace place) {
	    return place.getToken();
	}
    }

    public static String getNamePlace() {
	return "visualization";
    }

    public VisualizationPlace(String token) {
	super(token);
	SwrlEditorPortlet.closeClsPropBrowsers();
    }

    public String getRuleSelected() {
	return getParameter(ID_RULE_NAME);
    }
}
