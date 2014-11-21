package br.usp.icmc.dilvan.swrlEditor.server.swrleditor.manager.protege3;

import java.util.ArrayList;
import java.util.List;

import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.Errors;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.RuleSet;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Atom;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Rule;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Variable;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Atom.TYPE_ATOM;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Variable.TYPE_VARIABLE;
import br.usp.icmc.dilvan.swrlEditor.server.swrleditor.groups.MatrizAtomCharacteristic;



public class ErrorsAnalises {
	private Errors err;
	private RuleSet rules;
	private OntologyManagerProtege3 ontologyManager;
	private SWRLManagerProtege3 swrlManager;

	public ErrorsAnalises(RuleSet rules, OntologyManagerProtege3 ontologyManager, SWRLManagerProtege3 swrlManager){
		this.rules = rules;
		this.err = new Errors();
		this.ontologyManager = ontologyManager;
		this.swrlManager = swrlManager;
	}

	public Errors getErrors(Rule rule, boolean newRule){

		if (rule.getNameRule() == null || rule.getNameRule().trim().isEmpty())
			err.addErrors("Was not informed a name for the rule");
		
		// Sintatic Errors
		//Predicates and Atom Types
		isPredicatesWrong(rule);
		isNumParametersWrong(rule);
		isAtomParameterWrong(rule);
		//TODO duplicated atoms

		//Parameters (Variables and Datatypes)
		isParametersWrong(rule);	

		//Rule
		isVariableInconsistent(rule);

		//Rule set
		if(newRule){
			isDuplicateName(rule.getNameRule());
			isDuplicateRule(rule);
		} else {
			//TODO test if the rule is the same the original name 
		}

		// Semantic Errors
		//The rule is in conflict with the associated ontologies
		//The rule is inconsistent with the [rulename]
		//Circular dependencies between rules
		//Redundancies between rules

		// Warnings
		isEmptySomePart(rule);
		isSameAntecedent(rule);
		isBuiltinInConsequent(rule);

		return err;
	}

	private boolean isParametersWrong(Rule rule) {
		//The [param] parameter is used with [ptype] and [ptype] types in different atoms of same rule
		boolean ret = false;
		ArrayList<String> list = new ArrayList<String>();
		ArrayList<String> formatedList = new ArrayList<String>();
		for (Atom a : rule.getAtoms()) {
			if (a.getAtomType() != TYPE_ATOM.NULL)
				for (Variable p : a.getVariables()) {
					String fParam = p.getFormatedID()+p.getTypeVariable().name();
					if (list.contains(p.getFormatedID())){
						if(formatedList.contains(fParam))
							continue;
						else {
							String ptype = "";
							for(String aux : formatedList){
								if(aux.startsWith(p.getFormatedID())){
									ptype = aux.substring(p.getFormatedID().length());
								}
							}
							err.addErrors("The <b>"+p.getSimpleID()+"</b> parameter is used with <b>"+getNameParam(p.getTypeVariable().name())+"</b> and <b>"+getNameParam(ptype)+"</b> types in different atoms of same rule");
							ret = true;
						}
					}
					list.add(p.getFormatedID());
					formatedList.add(fParam);
				}
		}
		return ret;
	}

	private String getNameParam(String ptype) {
		if(ptype.equals("DVARIABLE")){
			return "Data variable";
		} else if(ptype.equals("IVARIABLE")){
			return "Individual variable";
		}else if(ptype.equals("INDIVIDUALID")){
			return "Individual ID";
		}else if(ptype.equals("DATALITERAL")){
			return "Data literal";
		}
		return "";
	}

	private boolean isAtomParameterWrong(Rule rule) {
		//TODO modificar a frase de erro 
		//Incorrect parameter type used in [atype] atom type, the [position] parameter must be of [ptype] type instead of [ptype] type
		for (Atom a : rule.getAtoms()) {
			if (a.getAtomType() != TYPE_ATOM.NULL)
				switch(a.getAtomType()){
				case CLASS:
					if(a.getCountVariables() == 1){
						TYPE_VARIABLE p = a.getVariables().get(0).getTypeVariable();
						if(p != TYPE_VARIABLE.INDIVIDUALID && p != TYPE_VARIABLE.IVARIABLE){
							err.addErrors("Incorrect parameter type used in <b>Class</b> atom type, the parameter "+getNameParam(p.name()));
						}
					}
					break;
				case DATARANGE:
					if(a.getCountVariables() == 1){
						TYPE_VARIABLE p = a.getVariables().get(0).getTypeVariable();
						if(p != TYPE_VARIABLE.DATALITERAL && p != TYPE_VARIABLE.DVARIABLE){
							err.addErrors("Incorrect parameter type used in <b>Data range</b> atom type, the parameter "+getNameParam(p.name()));
						}
					}
					break;
				case DATAVALUE_PROPERTY:
					if(a.getCountVariables() == 2){
						TYPE_VARIABLE p1 = a.getVariables().get(0).getTypeVariable();
						TYPE_VARIABLE p2 = a.getVariables().get(1).getTypeVariable();
						if(p1 != TYPE_VARIABLE.INDIVIDUALID && p1 != TYPE_VARIABLE.IVARIABLE){
							err.addErrors("Incorrect parameter type used in <b>Data value property</b> atom type, the <b>first<b> parameter "+getNameParam(p1.name()));
						}
						if(p2 != TYPE_VARIABLE.DATALITERAL && p2 != TYPE_VARIABLE.DVARIABLE){
							err.addErrors("Incorrect parameter type used in <b>Data value property</b> atom type, the <b>second<b> parameter "+getNameParam(p2.name()));
						}
					}
					break;
				case INDIVIDUAL_PROPERTY:
					if(a.getCountVariables() == 2){
						TYPE_VARIABLE p1 = a.getVariables().get(0).getTypeVariable();
						TYPE_VARIABLE p2 = a.getVariables().get(1).getTypeVariable();
						if(p1 != TYPE_VARIABLE.INDIVIDUALID && p1 != TYPE_VARIABLE.IVARIABLE){
							err.addErrors("Incorrect parameter type used in <b>Data value property</b> atom type, the <b>first<b> parameter "+getNameParam(p1.name()));
						}
						if(p2 != TYPE_VARIABLE.INDIVIDUALID && p2 != TYPE_VARIABLE.IVARIABLE){
							err.addErrors("Incorrect parameter type used in <b>Data value property</b> atom type, the <b>second<b> parameter "+getNameParam(p2.name()));
						}
					}
					break;
				case SAME_DIFERENT:
					break;
				case BUILTIN:
					break;
				}			
		}
		return false;
	}

	private boolean isPredicatesWrong(Rule rule) {
		// The predicate [predicate] is undefined in the ontology
		boolean ret = false;
		for(Atom a : rule.getAtoms()){
			String l = a.getPredicateID();
			if( l.startsWith("'")){
				l = l.substring(1, l.length()-1);
			}

			boolean aux = l.toLowerCase().equals("differentfrom")
					|| l.toLowerCase().equals("sameas")
					|| ontologyManager.hasOWLClass(l)
					|| ontologyManager.hasOWLDatatype(l)
					|| ontologyManager.hasOWLDatatypePropertie(l)
					|| ontologyManager.hasOWLObjectPropertie(l)
					|| swrlManager.hasBuiltin(l);
			
			if (!aux){
				List<String> listIds = ontologyManager.getIDsForLabel(l, false);
				if (listIds.size()>0){
					aux = true;
					if (listIds.size()>1){
						String Ids = "";
						for (String Id : listIds)
							if (Ids.isEmpty())
								Ids = "<b>"+Id+"</b>";
							else
								Ids = Ids + ", <b>"+Id+"</b>";
						err.addErrors("The predicate label <b>"+l+"</b> represents more than one ID: "+Ids);
					}
				}
				
			}
			
			if(!aux){
				err.addErrors("The predicate <b>"+l+"</b> is undefined in the ontology");
				ret = true;
			}
		}
		return ret;
	}

	private boolean isNumParametersWrong(Rule rule) {
		// The [atype] atom must has [num] parameter(s) instead of [num] parameter(s)
		
		boolean ret = false;
		for(Atom a : rule.getAtoms()){
			int countVarEmpty = 0;
			for (Variable var : a.getVariables())
				if (var.getSimpleID().trim().equals(""))
					countVarEmpty++;
					
					
			if (a.getAtomType() != TYPE_ATOM.NULL)
				switch(a.getAtomType()){
				case CLASS:
				case DATARANGE:
					if((a.getCountVariables()-countVarEmpty) != 1){
						ret = ret || true;
						err.addErrors("The <b>"+a.getPredicateID()+"</b> atom must has 1 parameter instead of "+a.getCountVariables()+" parameter(s)");
					}
					break;
				case DATAVALUE_PROPERTY:
				case INDIVIDUAL_PROPERTY:
				case SAME_DIFERENT:
					if((a.getCountVariables()-countVarEmpty) != 2){
						ret = ret || true;
						err.addErrors("The <b>"+a.getPredicateID()+"</b> atom must has 2 parameters instead of "+a.getCountVariables()+" parameter(s)");
					}
					break;
				case BUILTIN:
					if((a.getCountVariables()-countVarEmpty) <= 1){
						ret = ret || true;
						err.addErrors("The <b>"+a.getPredicateID()+"</b> atom must has more than 1 parameters");
					}
					break;
				}
		}
		return ret;
	}

	private boolean isVariableInconsistent(Rule rule) {
		//The variable [var] occur in the consequent and does not occur in the antecedent of rule
		boolean ret = false;
		ArrayList<String> varant = new ArrayList<String>();
		ArrayList<String> varcon = new ArrayList<String>();
		for(Atom a : rule.getAntecedent()){
			for(Variable p : a.getVariables()){
				if( p.getTypeVariable() == TYPE_VARIABLE.DVARIABLE || p.getTypeVariable() == TYPE_VARIABLE.IVARIABLE ){
					if(!varant.contains(p.getFormatedID()))
						varant.add(p.getFormatedID());
				}
			}
		}
		for(Atom a : rule.getConsequent()){
			for(Variable p : a.getVariables()){
				if( p.getTypeVariable() == TYPE_VARIABLE.DVARIABLE || p.getTypeVariable() == TYPE_VARIABLE.IVARIABLE ){
					if(!varant.contains(p.getFormatedID()) && !varcon.contains(p.getFormatedID())){
						varcon.add(p.getFormatedID());
						err.addErrors("The variable <b>"+p.getFormatedID()+"</b> occur in the consequent and does not occur in the antecedent of rule");
						ret = ret || true;
					}
				}
			}
		}
		return ret;
	}

	private boolean isBuiltinInConsequent(Rule rule) {
		// Builtin is used in consequent
		for(Atom a : rule.getConsequent()){
			if (a.getAtomType() != TYPE_ATOM.NULL)
				if( a.getAtomType() == TYPE_ATOM.BUILTIN ){
					err.addWarning("Builtin is used in consequent");
					return true;
				}
		}
		return false;
	}

	private boolean isSameAntecedent(Rule rule) {
		// TODO Duplicated antecedent in the rule set
		return false;
	}

	private boolean isEmptySomePart(Rule rule) {
		boolean ret = false;
		//Empty antecedent - is treated as trivially true (satisfied by every interpretation)
		if(rule.getAntecedent() == null || rule.getAntecedent().size() == 0){
			err.addWarning("Empty antecedent - is treated as trivially true (satisfied by every interpretation)");
			ret = true;
		}

		//Empty consequent - is treated as trivially false (not satisfied by any interpretation)
		if(rule.getConsequent() == null || rule.getConsequent().size() == 0){
			err.addWarning("Empty consequent - is treated as trivially false (not satisfied by any interpretation)");
			ret = ret || true;
		}
		return ret;
	}

	private boolean isDuplicateRule(Rule rule) {

		// Duplicated rule
		MatrizAtomCharacteristic mpc = new MatrizAtomCharacteristic();
		mpc.addRule(rules);

		// Rule analised
		mpc.addRule(rule);

		if( mpc.getIdenticalRules(rule).size() > 0 ){
			err.addErrors("Duplicated rule");
			return true;
		}
		return false;
	}

	private boolean isDuplicateName(String name) {
		// Duplicated rule name
		for(Rule r : rules){
			if(r.getNameRule().equals(name)){
				err.addErrors("Duplicated rule name: <b>"+name+"</b>");
				return true;
			}
		}
		return false;
	}
}

