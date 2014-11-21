package br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class AtomImpl implements Atom {
	private String predicateID;
	private String predicateLabel; 
	private String predicateComment;
	private TYPE_ATOM atomType;
	private List<Variable> variables;

	public AtomImpl(){
		predicateID = "";
		predicateLabel = ""; 
		predicateComment = "";
		atomType = TYPE_ATOM.NULL;
		variables = new ArrayList<Variable>();
	}

	@Override
	public void setPredicateID(String predicateID) {
		this.predicateID  = predicateID;
	}
	@Override
	public String getPredicateID() {
		return predicateID;
	}

	@Override
	public void setPredicateLabel(List<String> predicateLabel) {
		// TODO How to display more than a label?
		if (predicateLabel.size() > 0)
			this.predicateLabel = predicateLabel.get(0);
		else
			this.predicateLabel = "";
	}
	@Override
	public String getPredicateLabel() {
		return (predicateLabel.isEmpty()) ? predicateID : predicateLabel;  
	}

	@Override
	public void setPredicateComment(List<String> predicateComment) {
		this.predicateComment = "";
		// TODO Define a separator comments.
		for (String comment : predicateComment)
			this.predicateComment += "\\n" + comment;

		this.predicateComment = this.predicateComment.trim();
	}
	@Override
	public String getPredicateComment() {
		return predicateComment;
	}

	@Override
	public void setAtomType(TYPE_ATOM atomType) {
		this.atomType = atomType;
	}
	@Override
	public TYPE_ATOM getAtomType() {
		return atomType;
	}

	@Override
	public String getAtomID() {
		String description = "";
		for( Variable v:getVariables() ){
			description += v.getFormatedID()+",";
		}
		
		return (getVariables().size() > 0) ? getPredicateID()+"("+description.substring(0,description.length()-1)+")" : getPredicateID();
	}

	@Override
	public String getAtomLabel() {
		String description = "";
		for( Variable v:getVariables() ){
			description += v.getFormatedLabel()+",";
		}
		
		return (getVariables().size() > 0) ? getPredicateLabel()+"("+description.substring(0,description.length()-1)+")" : getPredicateLabel();
	}

	@Override
	public void addVariable(Variable v) {
		variables.add(v);
	}

	@Override
	public List<Variable> getVariables() {
		return variables;
	}

	@Override
	public int getCountVariables() {
		return variables.size();
	}
	
	@Override
	public Atom cloneOnlyID(){
		Atom a = new AtomImpl();
	
		a.setPredicateID(predicateID);
		a.setAtomType(atomType);
		
		for (Variable v: variables)
			a.addVariable(v.cloneOnlyID());
		
		return a;
	
	}

	@Override
	public boolean equals(Object atom) {
		Atom a = ((Atom)atom);
		boolean result = this.predicateID.equals( a.getPredicateID()) || this.predicateLabel.equals(a.getPredicateID());
		
		if (result){
			if (a.getCountVariables() != this.getCountVariables())
				result = false;
			else{
				for (int i = 0; i < this.getCountVariables(); i++)
					if (!a.getVariables().get(i).equals(this.getVariables().get(i))){
						result = false;
						break;
					}
						
			}
				
		}
		
		return result;
	}
}
