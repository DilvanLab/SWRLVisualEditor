package br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.place;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class OptionsPlace extends DefaultPlace{

    @Prefix (value="options")
    public static class Tokenizer implements PlaceTokenizer<OptionsPlace> {

	@Override
	public OptionsPlace getPlace(String token) {
	    return new OptionsPlace(token);
	}

	@Override
	public String getToken(OptionsPlace place) {
	    return place.getToken();
	}
    }

    public static String getNamePlace() {
	return "options";
    }

    public OptionsPlace(String token) {
	super(token);
    }
}
