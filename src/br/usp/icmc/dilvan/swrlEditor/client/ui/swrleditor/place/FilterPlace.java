package br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.place;

import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.SwrlEditorPortlet;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class FilterPlace extends DefaultPlace{
	
	public FilterPlace(String token)
	{
		super(token);
		SwrlEditorPortlet.openClsPropBrowsers();
	}
	
	public static String getNamePlace() {
		return "filter";
	}
	
	@Prefix (value="filter")
	public static class Tokenizer implements PlaceTokenizer<FilterPlace>
	{

		@Override
		public String getToken(FilterPlace place)
		{
			return place.getToken();
		}

		@Override
		public FilterPlace getPlace(String token)
		{
			return new FilterPlace(token);
		}

	}
	
}
