package br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.activity.info;

import java.util.ArrayList;
import java.util.List;

import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.RuleSet;

public class GeneratedRulesInfo {
    private final RuleSet rules;
    private final RegisterInfo ri;

    public GeneratedRulesInfo(RuleSet rules){
	this.rules = rules;
	ri =  new RegisterInfo(rules);
    }

    private float getAvgAtoms(){
	return (float)getNumAtoms() / (float)getNumRules();
    }

    private float getAvgAtomsBody(){
	return (float)getNumAtomsBody() / (float)getNumRules();
    }

    private float getAvgAtomsHead(){
	return (float)getNumAtomsHead() / (float)getNumRules();
    }

    private float getAvgParams(){
	return (float)getNumParams() / (float)getNumRules();
    }

    private float getAvgParamsBody(){
	return (float)getNumParamsBody() / (float)getNumRules();
    }

    private float getAvgParamsDistinct(){
	return (float)getNumParamsDistinct() / (float)getNumRules();
    }

    private float getAvgParamsDistinctBody() {
	return (float)getNumParamsDistinctBody() / (float)getNumRules();
    }

    private float getAvgParamsDistinctHead() {
	return (float)getNumParamsDistinctHead() / (float)getNumRules();
    }

    private float getAvgParamsHead(){
	return (float)getNumParamsHead() / (float)getNumRules();
    }

    private int getMaxAtoms(){
	return ri.maxAtoms;
    }

    private int getMaxAtomsBody(){
	return ri.maxAtomAntecedent;
    }

    private int getMaxAtomsHead(){
	return ri.maxAtomConsequent;
    }

    private int getMinAtoms(){
	return ri.minAtoms;
    }

    private int getMinAtomsBody(){
	return ri.minAtomAntecedent;
    }

    private int getMinAtomsHead(){
	return ri.minAtomConsequent;
    }

    private int getNumAtoms(){
	return rules.getCountAtoms();
    }

    //Body Overview
    private int getNumAtomsBody(){
	return rules.getCountAtomsAntecedent();
    }

    //Head overview
    private int getNumAtomsHead(){
	return rules.getCountAtomsConsequent();
    }

    private int getNumDistinctAtoms(){
	return ri.sumPredicateDistinct;
    }

    private int getNumDistinctAtomsBody(){
	return ri.sumPredicateDistinctAntecedent;
    }

    private int getNumDistinctAtomsHead(){
	return ri.sumPredicateDistinctConsequent;
    }

    private int getNumMaxParams(){
	return ri.maxNumVariables;
    }

    private int getNumMaxParamsBody(){
	return ri.maxNumVariablesAntecedent;
    }

    private int getNumMaxParamsDistinct(){
	return ri.maxNumVariablesDistinct;
    }

    private int getNumMaxParamsDistinctBody() {
	return ri.maxNumVariablesDistinctAntecedent;
    }

    private int getNumMaxParamsDistinctHead() {
	return ri.maxNumVariablesDistinctConsequent;
    }

    private int getNumMaxParamsHead(){
	return ri.maxNumVariablesConsequent;
    }

    private int getNumMinParams(){
	return ri.minNumVariables;
    }

    private int getNumMinParamsBody(){
	return ri.minNumVariablesAntecedent;
    }

    private int getNumMinParamsDistinct(){
	return ri.minNumVariablesDistinct;
    }

    private int getNumMinParamsDistinctBody() {
	return ri.minNumVariablesDistinctAntecedent;
    }

    private int getNumMinParamsDistinctHead() {
	return ri.minNumVariablesDistinctConsequent;
    }

    private int getNumMinParamsHead(){
	return ri.minNumVariablesConsequent;
    }

    private int getNumParams(){
	return ri.sumVariables;
    }

    private int getNumParamsBody(){
	return ri.sumVariablesAntecedent;
    }

    private int getNumParamsDistinct(){
	return ri.sumVariablesDistinct;
    }

    private int getNumParamsDistinctBody() {
	return ri.sumVariablesDistinctAntecedent;
    }

    private int getNumParamsDistinctHead() {
	return ri.sumVariablesDistinctConsequent;
    }

    private int getNumParamsHead(){
	return ri.sumVariablesConsequent;
    }

    // Rules Overview
    private int getNumRules(){
	return rules.size();
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
	infos.add(new RuleInfo("Number of distinct arguments",getNumParamsDistinct(),getNumParamsDistinctBody(), getNumParamsDistinctHead()));
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
