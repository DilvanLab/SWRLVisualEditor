package br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule;


import java.io.Serializable;
import java.util.List;


public interface Variable extends Serializable, Cloneable {
	public enum TYPE_VARIABLE {IVARIABLE, DVARIABLE, DATALITERAL, INDIVIDUALID, NULL};
	
	public void setSimpleID(String simpleID);
	public String getSimpleID();
	
	public void setSimpleLabel(List<String> simpleLabel);
	public String getSimpleLabel();
	
	public void setComment(List<String> simpleComment);
	public String getComment();
	
	public TYPE_VARIABLE getTypeVariable();
	public void setTypeVariable(TYPE_VARIABLE typeVariable);
	
	public String getFormatedID();
	public String getFormatedLabel();

	public Variable cloneOnlyID();
	
	public boolean equals(Object variable);
}
