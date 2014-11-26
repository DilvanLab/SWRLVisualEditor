package br.usp.icmc.dilvan.swrlEditor.client.javafx.data;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class ConditionSuggestion implements Serializable {

    private List<EntityData> suggestions;
    private String message;
    private boolean isValid;

    public ConditionSuggestion() {}

    public String getMessage() {return message;}

    public List<EntityData> getSuggestions() {return suggestions;}

    public boolean isValid() {return isValid;}

    public void setMessage(String message) {this.message = message;}

    public void setSuggestions(List<EntityData> suggestions) {
	this.suggestions = suggestions;
    }

    public void setValid(boolean isValid) {this.isValid = isValid;}
}
