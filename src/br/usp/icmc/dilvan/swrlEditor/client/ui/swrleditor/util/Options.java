package br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.util;

import java.util.Map;

public class Options {

	public static String getStringOption (Map<String, Object> config, String name, String defaultValue){
		try {
            String value = (String) config.get(name);
            if (value == null) {
                value = defaultValue;
            }
            return value;
        } catch (Exception e) {
            return defaultValue;
        }
	}
	public static Boolean getBooleanOption (Map<String, Object> config, String name, Boolean defaultValue){
		try {
			Boolean value = (Boolean) config.get(name);
            if (value == null) {
                value = defaultValue;
            }
            return value;
        } catch (Exception e) {
            return defaultValue;
        }
	}
	
	public static String removeCharInvalidForNameOptions(String nameDefault, String nameOriginal){
		String result = "";
		for (char c : nameOriginal.toCharArray()){
			int ic = (int) c;
			
			if ((ic >= 48 && ic <= 57) || (ic >= 65 && ic <= 90) || (ic >= 97 && ic <= 122))
				result = result + c;
		}
		return nameDefault+result;
	}
	
	
}
