package br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.place;

import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.SwrlEditorPortlet;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class FilterPlace extends DefaultPlace{

    @Prefix (value="filter")
    public static class Tokenizer implements PlaceTokenizer<FilterPlace> {

	@Override
	public FilterPlace getPlace(String token) {
	    return new FilterPlace(token);
	}

	@Override
	public String getToken(FilterPlace place) {
	    return place.getToken();
	}
    }

    public static String getNamePlace() {
	return "filter";
    }

    public FilterPlace(String token) {
	super(token);
	SwrlEditorPortlet.openClsPropBrowsers();
    }
}
