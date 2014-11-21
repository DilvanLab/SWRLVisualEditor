package br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule;


import java.util.List;

import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Atom.TYPE_ATOM;

@SuppressWarnings("serial")
public class VariableImpl implements Variable {
	private String simpleID;
	private String simpleLabel;
	private TYPE_VARIABLE typeVariable = null;
	private String comment;
	
	public VariableImpl(){
		this.simpleID = ""; 
		this.simpleLabel = "";
		this.comment = "";
		this.typeVariable = TYPE_VARIABLE.NULL;
	}

	@Override
	public void setSimpleID(String simpleID) { 
		this.simpleID = simpleID; 
	}
	@Override
	public String getSimpleID() {
		return simpleID;
	}

	@Override
	public void setSimpleLabel(List<String> simpleLabel) {
		// TODO How to display more than a label?
		if (simpleLabel.size() > 0)
			this.simpleLabel = simpleLabel.get(0);
		else
			this.simpleLabel = "";
	}
	@Override
	public String getSimpleLabel() {
		return (simpleLabel.isEmpty()) ? simpleID : simpleLabel;  
	}

	@Override
	public TYPE_VARIABLE getTypeVariable() {
		return typeVariable;
	}
	@Override
	public void setTypeVariable(TYPE_VARIABLE typeVariable) {
		this.typeVariable = typeVariable;
	}

	@Override
	public void setComment(List<String> comment) {
		this.comment = "";
		// TODO Define a separator comments.
		for (String commentAux : comment)
			this.comment += "\\n" + commentAux;

		this.comment = this.comment.trim();

	}
	@Override
	public String getComment() {
		return comment;
	}

	@Override
	public String getFormatedID() {
		if (getTypeVariable() == TYPE_VARIABLE.DVARIABLE || getTypeVariable() == TYPE_VARIABLE.IVARIABLE)
			return "?"+getSimpleID();
		else if (getTypeVariable() == TYPE_VARIABLE.DATALITERAL && !(isNumeric()))
			return "\"" +getSimpleID()+"\"";
		else
			return getSimpleID();  
	}

	@Override
	public String getFormatedLabel() {
		if (getTypeVariable() == TYPE_VARIABLE.DVARIABLE || getTypeVariable() == TYPE_VARIABLE.IVARIABLE)
			return "?"+getSimpleLabel();
		else if (getTypeVariable() == TYPE_VARIABLE.DATALITERAL && !(isNumeric()))
			return "\"" +getSimpleLabel()+"\"";
		else
			return getSimpleLabel();  	
	}

	private boolean isNumeric() {
		String charNumeric = "-0123456789.,";
		
		for (int i = 0; i < simpleID.length(); i++)
			if (!(charNumeric.indexOf(simpleID.charAt(i))>=0))
				return false;
		
		return true;
	}
	
	@Override
	public Variable cloneOnlyID(){
		Variable v = new VariableImpl();
		v.setSimpleID(simpleID);
		v.setTypeVariable(getTypeVariable());
		return v;
	}
	
	
	
	public static TYPE_VARIABLE getTYPE_VARIABLE(TYPE_ATOM typeAtom, String atomID, String variableID, int orderVariable){

		if (typeAtom == null)
			return TYPE_VARIABLE.NULL;
		
		if(typeAtom  == Atom.TYPE_ATOM.CLASS || typeAtom == Atom.TYPE_ATOM.INDIVIDUAL_PROPERTY || typeAtom == Atom.TYPE_ATOM.SAME_DIFERENT || 
				(typeAtom == Atom.TYPE_ATOM.DATAVALUE_PROPERTY && orderVariable == 1) || 
				(typeAtom == Atom.TYPE_ATOM.BUILTIN && orderVariable == 1 && (atomID.equalsIgnoreCase("swrlx:createOWLThing")||atomID.equalsIgnoreCase("swrlx:makeOWLThing")) ) ){

			if(variableID.trim().startsWith("?"))
				return TYPE_VARIABLE.IVARIABLE;
			else 
				return TYPE_VARIABLE.INDIVIDUALID;
		} else {
			if(variableID.trim().startsWith("?"))
				return TYPE_VARIABLE.DVARIABLE;
			else 
				return TYPE_VARIABLE.DATALITERAL;
		}

	}

	@Override
	public boolean equals(Object variable) {
		return this.simpleID.equals( ((Variable)variable).getSimpleID()) || this.simpleLabel.equals( ((Variable)variable).getSimpleID());
	}
	
}
