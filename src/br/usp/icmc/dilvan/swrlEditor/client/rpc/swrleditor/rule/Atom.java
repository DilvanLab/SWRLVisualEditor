package br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule;

import java.io.Serializable;
import java.util.List;

public interface Atom extends Serializable, Cloneable {
	public enum TYPE_ATOM {CLASS, INDIVIDUAL_PROPERTY, SAME_DIFERENT, DATAVALUE_PROPERTY, BUILTIN, DATARANGE, NULL};

	public void setPredicateID(String predicateID);
	public String getPredicateID();
	
	public void setPredicateLabel(List<String> predicateLabel);
	public String getPredicateLabel();

	public void setPredicateComment(List<String> predicateComment);
	public String getPredicateComment();
	
	public void setAtomType(TYPE_ATOM atomType);
	public TYPE_ATOM getAtomType();
	
	public String getAtomID();
	public String getAtomLabel();

	public void addVariable(Variable v);
	public List<Variable> getVariables();
	public int getCountVariables();
	
	public Atom cloneOnlyID();
	
	public boolean equals(Object atom);

}
