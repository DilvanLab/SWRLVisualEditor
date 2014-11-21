package br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.activity.visualization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.usp.icmc.dilvan.swrlEditor.client.resources.UtilResource;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Atom;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Rule;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Variable;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.UtilView.BuiltinMap;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.VisualizationView.TYPE_VIEW;


public class AutismModel {
	protected Rule rule;
	private TYPE_VIEW typeView;

	// List of errors
	ArrayList<String> errors = new ArrayList<String>();

	// The main atom
	ArrayList<Atom> mainAnt = new ArrayList<Atom>();
	ArrayList<Atom> mainCons = new ArrayList<Atom>();

	// List of every builtins
	ArrayList<Atom> builtins = new ArrayList<Atom>();

	// Atoms organized by the properties. The key is the variable property and the value is a array list of every atoms with that properties 
	HashMap<String, ArrayList<Atom>> properties1 = new HashMap<String, ArrayList<Atom>>();
	HashMap<String, ArrayList<Atom>> properties2 = new HashMap<String, ArrayList<Atom>>();

	public AutismModel(Rule r, TYPE_VIEW typeViewInOptions){
		this.rule = r;
		this.typeView = typeViewInOptions;
		// Selecting the main atom in antecedent
		getMainAtoms(mainAnt, r.getAntecedent());
		if(mainAnt.size() != 1){
			errors.add("Exist "+mainAnt.size()+" class atom type in antecedent");
		}

		// Selecting the main atom in consequent
		getMainAtoms(mainCons, r.getConsequent());
		if(mainCons.size() == 0){
			errors.add("Exist 0 class atom type in consequent");
		}

		// Selecting the properties and builtins
		getPropertysAndBuiltins();
	}

	private void getPropertysAndBuiltins() {
		for(Atom a : rule.getAtoms()){
			if( Atom.TYPE_ATOM.DATAVALUE_PROPERTY.equals(a.getAtomType()) || Atom.TYPE_ATOM.INDIVIDUAL_PROPERTY.equals(a.getAtomType()) ){

				String firstParam;
				if (typeView == TYPE_VIEW.ID)
					firstParam = a.getVariables().get(0).getFormatedID();
				else
					firstParam = a.getVariables().get(0).getFormatedLabel();

				if(!properties1.containsKey(firstParam))
					properties1.put(firstParam, new ArrayList<Atom>());
				properties1.get(firstParam).add(a);

				String lastParam;
				if (typeView == TYPE_VIEW.ID)
					lastParam = a.getVariables().get(1).getFormatedID();
				else
					lastParam = a.getVariables().get(1).getFormatedLabel();


				if(lastParam.startsWith("?")){
					if(!properties2.containsKey(lastParam))
						properties2.put(lastParam, new ArrayList<Atom>());
					properties2.get(lastParam).add(a);
				}
			} else if(Atom.TYPE_ATOM.BUILTIN.equals(a.getAtomType())){
				builtins.add(a);
			}
		}
	}

	private void getMainAtoms(List<Atom> main, List<Atom> alist) {
		for(Atom a : alist){
			if(Atom.TYPE_ATOM.CLASS.equals(a.getAtomType())){
				main.add(a);
			}
		}
	}

	public boolean isCorrect(){
		return errors.size() == 0;
	}

	public Atom getMainAnt() {
		if(mainAnt.size() > 0)
			return mainAnt.get(0);
		else
			return null;
	}

	public ArrayList<Atom> getMainCons() {
		ArrayList<Atom> ret = new ArrayList<Atom>();
		for(Atom a : mainCons){
			ret.add(a);
		}
		return ret;
	}

	public ArrayList<Atom> getFirstParam(String predicate){
		if(properties1.containsKey(predicate))
			return properties1.get(predicate);
		return new ArrayList<Atom>();
	}


	public ArrayList<Atom> getLastParam(String predicate){
		if(properties2.containsKey(predicate))
			return properties2.get(predicate);
		return new ArrayList<Atom>();
	}


	public String getMatch() {
		String aux = "";
		ArrayList<Atom> matches = new ArrayList<Atom>();

		for(String p : properties2.keySet()){
			if(!properties1.containsKey(p) && properties2.get(p).size() == 1){
				matches.add(properties2.get(p).get(0));
			}
		}
		if(matches.size() == 1 && !inBuiltin(matches.get(0).getVariables().get(1)) ){
			if (typeView == TYPE_VIEW.ID)
				aux = matches.get(0).getPredicateID();
			else
				aux = matches.get(0).getPredicateLabel();

		} else {
			HashMap<String, BuiltinMap> blt = getListBuiltins();
			HashMap<String, String> builtinexpression = new HashMap<String, String>();
			for(Atom a : builtins){

				if (typeView == TYPE_VIEW.ID){

					if(blt.containsKey(a.getPredicateID().toLowerCase())){
						if(a.getPredicateID().equalsIgnoreCase("swrlb:add") || a.getPredicateID().equalsIgnoreCase("swrlb:divide")){
							String newAux = "("+blt.get(a.getPredicateID().toLowerCase()).getValue(a.getVariables(), typeView)+")";
							for(String b : builtinexpression.keySet()){
								newAux = newAux.replace(b, builtinexpression.get(b));
							}
							builtinexpression.put(a.getVariables().get(0).getFormatedID(), newAux);
						} else {
							String newAux = blt.get(a.getPredicateID().toLowerCase()).getValue(a.getVariables(), typeView);
							for(String b : builtinexpression.keySet()){
								newAux = newAux.replace(b, builtinexpression.get(b));
							}

							if(aux.isEmpty())
								aux = newAux;
							else 
								aux += "<br /> AND <br />"+newAux;
						}
					} else {
						// makeOWLThing or createOWLThing or other builtins
					}

				}else{
					if(blt.containsKey(a.getPredicateLabel().toLowerCase())){
						if(a.getPredicateLabel().equalsIgnoreCase("swrlb:add") || a.getPredicateLabel().equalsIgnoreCase("swrlb:divide")){
							String newAux = "("+blt.get(a.getPredicateLabel().toLowerCase()).getValue(a.getVariables(), typeView)+")";
							for(String b : builtinexpression.keySet()){
								newAux = newAux.replace(b, builtinexpression.get(b));
							}
							builtinexpression.put(a.getVariables().get(0).getFormatedLabel(), newAux);
						} else {
							String newAux = blt.get(a.getPredicateLabel().toLowerCase()).getValue(a.getVariables(), typeView);
							for(String b : builtinexpression.keySet()){
								newAux = newAux.replace(b, builtinexpression.get(b));
							}

							if(aux.isEmpty())
								aux = newAux;
							else 
								aux += "<br /> AND <br />"+newAux;
						}
					} else {
						// makeOWLThing or createOWLThing or other builtins
					}
				}
			}
			if (typeView == TYPE_VIEW.ID){
				for(Atom a : matches){
					aux = aux.replace(a.getVariables().get(1).getFormatedID(), a.getPredicateID());
				}
			}else{
				for(Atom a : matches){
					aux = aux.replace(a.getVariables().get(1).getFormatedLabel(), a.getPredicateLabel());
				}
			}

		}

		return aux;
	}


	/**
	 * Return true if the param is presenting in some builtin, otherwise false
	 */
	private boolean inBuiltin(Variable param) {
		for(Atom a : builtins){
			for(Variable p : a.getVariables()){
				if(p.getFormatedID().equals(param.getFormatedID()))
					return true;
			}
		}
		return false;
	}

	protected HashMap<String, BuiltinMap> getListBuiltins(){
		HashMap<String, BuiltinMap> dict = new HashMap<String, BuiltinMap>();
		// add
		dict.put("swrlb:add", new BuiltinMap() {
			@Override
			public String getValue(List<Variable> params, TYPE_VIEW typeView) {
				Variable p1 = params.get(1);

				
				
				
				String html;
				if (typeView == TYPE_VIEW.ID){
					html = "<span class='"+UtilResource.getCssTypeVariableView(p1.getTypeVariable())+"'>"+p1.getFormatedID()+"</span>";
					for(int i = 2; i < params.size(); i++){
						Variable p2 = params.get(i);
						html += " + <span class='"+UtilResource.getCssTypeVariableView(p2.getTypeVariable())+"'>"+p2.getFormatedID()+"</span>";
					}
				}else{
					html = "<span class='"+UtilResource.getCssTypeVariableView(p1.getTypeVariable())+"'>"+p1.getFormatedLabel()+"</span>";
					for(int i = 2; i < params.size(); i++){
						Variable p2 = params.get(i);
						html += " + <span class='"+UtilResource.getCssTypeVariableView(p2.getTypeVariable())+"'>"+p2.getFormatedLabel()+"</span>";
					}
				}


				return html;
			}
		});

		// divide
		dict.put("swrlb:divide", new BuiltinMap() {
			@Override
			public String getValue(List<Variable> params, TYPE_VIEW typeView) {
				Variable p1 = params.get(1);
				Variable p2 = params.get(2);
				if (typeView == TYPE_VIEW.ID)
					return "<span class='"+UtilResource.getCssTypeVariableView(p1.getTypeVariable())+"'>"+p1.getFormatedID()+"</span> / <span class='"+UtilResource.getCssTypeVariableView(p2.getTypeVariable())+"'>"+p2.getFormatedID()+"</span>";
				else
					return "<span class='"+UtilResource.getCssTypeVariableView(p1.getTypeVariable())+"'>"+p1.getFormatedLabel()+"</span> / <span class='param_"+UtilResource.getCssTypeVariableView(p2.getTypeVariable())+"'>"+p2.getFormatedLabel()+"</span>";
			}
		});

		// equal
		dict.put("swrlb:equal", new BuiltinMap() {
			@Override
			public String getValue(List<Variable> params, TYPE_VIEW typeView) {
				Variable p0 = params.get(0);
				Variable p1 = params.get(1);
				if (typeView == TYPE_VIEW.ID)
					return  "<span class='"+UtilResource.getCssTypeVariableView(p0.getTypeVariable())+"'>"+p0.getFormatedID()+"</span> = <span class='"+UtilResource.getCssTypeVariableView(p1.getTypeVariable())+"'>"+p1.getFormatedID()+"</span>";
				else
					return  "<span class='"+UtilResource.getCssTypeVariableView(p0.getTypeVariable())+"'>"+p0.getFormatedLabel()+"</span> = <span class='"+UtilResource.getCssTypeVariableView(p1.getTypeVariable())+"'>"+p1.getFormatedLabel()+"</span>";
			}
		});

		// greaterThan
		dict.put("swrlb:greaterthan", new BuiltinMap() {
			@Override
			public String getValue(List<Variable> params, TYPE_VIEW typeView) {
				Variable p0 = params.get(0);
				Variable p1 = params.get(1);
				if (typeView == TYPE_VIEW.ID)

					return  "<span class='"+UtilResource.getCssTypeVariableView(p0.getTypeVariable())+"'>"+p0.getFormatedID()+"</span> > <span class='"+UtilResource.getCssTypeVariableView(p1.getTypeVariable())+"'>"+p1.getFormatedID()+"</span>";
				else
					return  "<span class='"+UtilResource.getCssTypeVariableView(p0.getTypeVariable())+"'>"+p0.getFormatedLabel()+"</span> > <span class='"+UtilResource.getCssTypeVariableView(p1.getTypeVariable())+"'>"+p1.getFormatedLabel()+"</span>";
			}
		});

		// greaterThanOrEqual
		dict.put("swrlb:greaterthanorequal", new BuiltinMap() {
			@Override
			public String getValue(List<Variable> params, TYPE_VIEW typeView) {
				Variable p0 = params.get(0);
				Variable p1 = params.get(1);
				if (typeView == TYPE_VIEW.ID)

					return  "<span class='"+UtilResource.getCssTypeVariableView(p0.getTypeVariable())+"'>"+p0.getFormatedID()+"</span> >= <span class='"+UtilResource.getCssTypeVariableView(p1.getTypeVariable())+"'>"+p1.getFormatedID()+"</span>";
				else
					return  "<span class='"+UtilResource.getCssTypeVariableView(p0.getTypeVariable())+"'>"+p0.getFormatedLabel()+"</span> >= <span class='"+UtilResource.getCssTypeVariableView(p1.getTypeVariable())+"'>"+p1.getFormatedLabel()+"</span>";
			}
		});

		// lessThan
		dict.put("swrlb:lessthan", new BuiltinMap() {
			@Override
			public String getValue(List<Variable> params, TYPE_VIEW typeView) {
				Variable p0 = params.get(0);
				Variable p1 = params.get(1);
				if (typeView == TYPE_VIEW.ID)

					return  "<span class='"+UtilResource.getCssTypeVariableView(p0.getTypeVariable())+"'>"+p0.getFormatedID()+"</span> < <span class='"+UtilResource.getCssTypeVariableView(p1.getTypeVariable())+"'>"+p1.getFormatedID()+"</span>";
				else
					return  "<span class='"+UtilResource.getCssTypeVariableView(p0.getTypeVariable())+"'>"+p0.getFormatedLabel()+"</span> < <span class='"+UtilResource.getCssTypeVariableView(p1.getTypeVariable())+"'>"+p1.getFormatedLabel()+"</span>";
			}
		});

		// lessThanOrEqual
		dict.put("swrlb:lessthanorequal", new BuiltinMap() {
			@Override
			public String getValue(List<Variable> params, TYPE_VIEW typeView) {
				Variable p0 = params.get(0);
				Variable p1 = params.get(1);
				if (typeView == TYPE_VIEW.ID)
					return  "<span class='"+UtilResource.getCssTypeVariableView(p0.getTypeVariable())+"'>"+p0.getFormatedID()+"</span> <= <span class='"+UtilResource.getCssTypeVariableView(p1.getTypeVariable())+"'>"+p1.getFormatedID()+"</span>";
				else
					return  "<span class='"+UtilResource.getCssTypeVariableView(p0.getTypeVariable())+"'>"+p0.getFormatedLabel()+"</span> <= <span class='"+UtilResource.getCssTypeVariableView(p1.getTypeVariable())+"'>"+p1.getFormatedLabel()+"</span>";
			}
		});

		return dict;
	}

	public void setErrors(String string) {
		errors.add(string);
	}

	public ArrayList<String> getErrors() {
		return errors;
	}
}
