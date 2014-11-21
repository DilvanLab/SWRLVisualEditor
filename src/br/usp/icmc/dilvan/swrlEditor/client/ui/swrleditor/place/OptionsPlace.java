package br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.place;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class OptionsPlace extends DefaultPlace{
	
	public OptionsPlace(String token)
	{
		super(token);
	}
	
	public static String getNamePlace() {
		return "options";
	}
	
	@Prefix (value="options")
	public static class Tokenizer implements PlaceTokenizer<OptionsPlace>
	{

		@Override
		public String getToken(OptionsPlace place)
		{
			return place.getToken();
		}

		@Override
		public OptionsPlace getPlace(String token)
		{
			return new OptionsPlace(token);
		}

	}
	
}
