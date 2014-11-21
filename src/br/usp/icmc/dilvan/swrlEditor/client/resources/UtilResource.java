package br.usp.icmc.dilvan.swrlEditor.client.resources;

import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.decisiontree.NodeDecisionTree.ATOM_TYPE;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Atom.TYPE_ATOM;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Variable.TYPE_VARIABLE;

public class UtilResource {

	public static String getCssTypeAtom(TYPE_ATOM type){
		if (type == TYPE_ATOM.CLASS)
			return Resources.INSTANCE.swrleditor().atom_CLASS();
		else if (type == TYPE_ATOM.BUILTIN)
			return Resources.INSTANCE.swrleditor().atom_BUILTIN();
		else if (type == TYPE_ATOM.DATARANGE)
			return Resources.INSTANCE.swrleditor().atom_DATA_RANGE();
		else if (type == TYPE_ATOM.DATAVALUE_PROPERTY)
			return Resources.INSTANCE.swrleditor().atom_DATAVALUE_PROPERTY();
		else if (type == TYPE_ATOM.INDIVIDUAL_PROPERTY)
			return Resources.INSTANCE.swrleditor().atom_INDIVIDUAL_PROPERTY();
		else if (type == TYPE_ATOM.SAME_DIFERENT)
			return Resources.INSTANCE.swrleditor().atom_SAME_DIFERENT();
		else
			return "";
	}
	
	public static String getCssTypeVariableEditor(TYPE_VARIABLE type){
		if (type == TYPE_VARIABLE.DATALITERAL)
			return Resources.INSTANCE.swrleditor().editor_DVARIABLE();
		else if (type == TYPE_VARIABLE.DVARIABLE)
			return Resources.INSTANCE.swrleditor().editor_DVARIABLE();
		else if (type == TYPE_VARIABLE.INDIVIDUALID)
			return Resources.INSTANCE.swrleditor().editor_IVARIABLE();
		else if (type == TYPE_VARIABLE.IVARIABLE)
			return Resources.INSTANCE.swrleditor().editor_IVARIABLE();
		else
			return "";
	}
	
	public static String getCssTypeVariableView(TYPE_VARIABLE type){
		if (type == TYPE_VARIABLE.DATALITERAL)
			return Resources.INSTANCE.swrleditor().param_DATALITERAL();
		else if (type == TYPE_VARIABLE.DVARIABLE)
			return Resources.INSTANCE.swrleditor().param_DVARIABLE();
		else if (type == TYPE_VARIABLE.INDIVIDUALID)
			return Resources.INSTANCE.swrleditor().param_INDIVIDUALID();
		else if (type == TYPE_VARIABLE.IVARIABLE)
			return Resources.INSTANCE.swrleditor().param_IVARIABLE();
		else
			return "";
	}
	
	public static String getCssRulePart(String rulePart){
		if (rulePart.equals("antecedent")){
			return Resources.INSTANCE.swrleditor().antecedent();
		}else if (rulePart.equals("consequent")){
			return Resources.INSTANCE.swrleditor().consequent();
		}else
			return "";
	}
	
	public static String getCssTypeNodeAtom(ATOM_TYPE type){
		if (type == ATOM_TYPE.CLASS)
			return Resources.INSTANCE.swrleditor().atom_CLASS();
		else if (type == ATOM_TYPE.BUILTIN)
			return Resources.INSTANCE.swrleditor().atom_BUILTIN();
		else if (type == ATOM_TYPE.DATARANGE)
			return Resources.INSTANCE.swrleditor().atom_DATA_RANGE();
		else if (type == ATOM_TYPE.DATAVALUE_PROPERTY)
			return Resources.INSTANCE.swrleditor().atom_DATAVALUE_PROPERTY();
		else if (type == ATOM_TYPE.INDIVIDUAL_PROPERTY)
			return Resources.INSTANCE.swrleditor().atom_INDIVIDUAL_PROPERTY();
		else if (type == ATOM_TYPE.SAME_DIFERENT)
			return Resources.INSTANCE.swrleditor().atom_SAME_DIFERENT();
		else
			return "";
	}
}
