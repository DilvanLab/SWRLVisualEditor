package br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.place;

import com.google.gwt.place.shared.Place;

public abstract class DefaultPlace extends Place{
	
	public final static String ID_ONTOLOGY = "ontology";
	public final static String ID_TAB = "tab";

	public final static String ID_RULE_NAME = "rule";
	public final static String ID_MODE = "mode";
	
	protected String token;
	
	public DefaultPlace(String token)
	{
		if (token == null) throw new RuntimeException("Null token in DefaultPlacer.");
		this.token = token;
	}
	
	public String getToken() {
		return token;
	}	
	
	public String getParameter(String id){
		return getParameter(token, id);
	}
	
	public static String getParameter(String token, String id){
		String test = id+"=";
		
		if (token.contains(test)){
			String result = token.substring(token.indexOf(test)+test.length());

			if (result.indexOf("&") > 0)
				result = result.substring(0, result.indexOf("&"));
			
			return result;
		}else
			return "";
	}
	
	
}
