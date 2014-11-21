package br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.activity.info;

import java.util.ArrayList;
import java.util.List;

import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.RuleSet;



public class GeneratedRulesInfo {
	private RuleSet rules;
	private RegisterInfo ri;

	public GeneratedRulesInfo(RuleSet rules){
		this.rules = rules;
		ri =  new RegisterInfo(rules);
	}

	// Rules Overview
	private int getNumRules(){
		return rules.size();
	}

	private int getNumAtoms(){
		return rules.getCountAtoms();
	}

	private int getNumDistinctAtoms(){
		return ri.sumPredicateDistinct;
	}

	private float getAvgAtoms(){
		return (float)getNumAtoms() / (float)getNumRules();
	}

	private int getMaxAtoms(){
		return ri.maxAtoms;
	}

	private int getMinAtoms(){
		return ri.minAtoms;
	}

	private int getNumParams(){
		return ri.sumVariables;
	}

	private int getNumParamsDistinct(){
		return ri.sumVariablesDistinct;
	}

	private float getAvgParams(){
		return (float)getNumParams() / (float)getNumRules();
	}

	private float getAvgParamsDistinct(){
		return (float)getNumParamsDistinct() / (float)getNumRules();
	}

	private int getNumMaxParams(){
		return ri.maxNumVariables;
	}

	private int getNumMaxParamsDistinct(){
		return ri.maxNumVariablesDistinct;
	}

	private int getNumMinParams(){
		return ri.minNumVariables;
	}

	private int getNumMinParamsDistinct(){
		return ri.minNumVariablesDistinct;
	}

	//Body Overview
	private int getNumAtomsBody(){
		return rules.getCountAtomsAntecedent();
	}

	private int getNumDistinctAtomsBody(){
		return ri.sumPredicateDistinctAntecedent;
	}

	private float getAvgAtomsBody(){
		return (float)getNumAtomsBody() / (float)getNumRules();
	}

	private int getMaxAtomsBody(){
		return ri.maxAtomAntecedent;
	}

	private int getMinAtomsBody(){
		return ri.minAtomAntecedent;
	}

	private int getNumParamsBody(){
		return ri.sumVariablesAntecedent;
	}

	private float getAvgParamsBody(){
		return (float)getNumParamsBody() / (float)getNumRules();
	}

	private int getNumMaxParamsBody(){
		return ri.maxNumVariablesAntecedent;
	}

	private int getNumMinParamsBody(){
		return ri.minNumVariablesAntecedent;
	}

	private int getNumMaxParamsDistinctBody() {
		return ri.maxNumVariablesDistinctAntecedent;
	}
	
	private int getNumMinParamsDistinctBody() {
		return ri.minNumVariablesDistinctAntecedent;
	}
	
	private float getAvgParamsDistinctBody() {
		return (float)getNumParamsDistinctBody() / (float)getNumRules();
	}

	private int getNumParamsDistinctBody() {
		return ri.sumVariablesDistinctAntecedent;
	}

	//Head overview
	private int getNumAtomsHead(){
		return rules.getCountAtomsConsequent();
	}

	private int getNumDistinctAtomsHead(){
		return ri.sumPredicateDistinctConsequent;
	}

	private float getAvgAtomsHead(){
		return (float)getNumAtomsHead() / (float)getNumRules();
	}

	private int getMaxAtomsHead(){
		return ri.maxAtomConsequent;
	}

	private int getMinAtomsHead(){
		return ri.minAtomConsequent;
	}

	private int getNumParamsHead(){
		return ri.sumVariablesConsequent;
	}

	private float getAvgParamsHead(){
		return (float)getNumParamsHead() / (float)getNumRules();
	}

	private int getNumMaxParamsHead(){
		return ri.maxNumVariablesConsequent;
	}

	private int getNumMinParamsHead(){
		return ri.minNumVariablesConsequent;
	}

	private int getNumMaxParamsDistinctHead() {
		return ri.maxNumVariablesDistinctConsequent;
	}
	
	private int getNumMinParamsDistinctHead() {
		return ri.minNumVariablesDistinctConsequent;
	}

	private float getAvgParamsDistinctHead() {
		return (float)getNumParamsDistinctHead() / (float)getNumRules();
	}

	private int getNumParamsDistinctHead() {
		return ri.sumVariablesDistinctConsequent;
	}
	
	public List<RuleInfo> getRulesInfo() {
		List<RuleInfo> infos = new ArrayList<RuleInfo>();

		infos.add(new RuleInfo("Number of rules",getNumRules(),-1,-1));
		infos.add(new RuleInfo("Number of atoms",getNumAtoms(),getNumAtomsBody(), getNumAtomsHead()));
		infos.add(new RuleInfo("Number of distinct predicate atoms", getNumDistinctAtoms(),getNumDistinctAtomsBody(), getNumDistinctAtomsHead()));
		infos.add(new RuleInfo("Average of atoms by number of rules", (int)getAvgAtoms(), (int)getAvgAtomsBody(), (int)getAvgAtomsHead()));
		infos.add(new RuleInfo("Max number of atoms",getMaxAtoms(),getMaxAtomsBody(), getMaxAtomsHead()));
		if(getNumRules() == 0)
			infos.add(new RuleInfo("Min number of atoms",0,0,0));
		else
			infos.add(new RuleInfo("Min number of atoms",getMinAtoms(),getMinAtomsBody(), getMinAtomsHead()));
		infos.add(new RuleInfo("Number of arguments",getNumParams(),getNumParamsBody(), getNumParamsHead()));
		infos.add(new RuleInfo("Number of distinct arguments",getNumParamsDistinct(),(int)getNumParamsDistinctBody(), (int)getNumParamsDistinctHead()));
		infos.add(new RuleInfo("Average of arguments by number of rules", (int)getAvgParams(), (int)getAvgParamsBody(), (int)getAvgParamsHead()));
		infos.add(new RuleInfo("Average of distinct arguments by number of rules", (int)getAvgParamsDistinct(), (int)getAvgParamsDistinctBody(), (int)getAvgParamsDistinctHead()));
		infos.add(new RuleInfo("Max number of arguments",getNumMaxParams(),getNumMaxParamsBody(), getNumMaxParamsHead()));
		infos.add(new RuleInfo("Max number of distinct arguments",getNumMaxParamsDistinct(),getNumMaxParamsDistinctBody(), getNumMaxParamsDistinctHead()));
		if(getNumRules() == 0) {
			infos.add(new RuleInfo("Min number of arguments",0, 0, 0));
			infos.add(new RuleInfo("Min number of distinct arguments",0, 0, 0));
		} else {
			infos.add(new RuleInfo("Min number of arguments",getNumMinParams(),getNumMinParamsBody(), getNumMinParamsHead()));
			infos.add(new RuleInfo("Min number of distinct arguments",getNumMinParamsDistinct(),getNumMinParamsDistinctBody(), getNumMinParamsDistinctHead()));
		}
		return infos;
	}
	
}
