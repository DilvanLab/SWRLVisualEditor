package br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class RuleImpl implements Rule {

    private String nameRule;
    private final  List<Atom> antecedent;
    private final  List<Atom> consequent;

    private String paraphrase;
    private List<String> antecedentParaphrase;
    private String consequentParaphrase;

    private boolean enabled;

    public RuleImpl(){
	nameRule = "";
	antecedent = new ArrayList<Atom>();
	consequent = new ArrayList<Atom>();
	antecedentParaphrase = new ArrayList<String>();
    }

    @Override
    public void addAntecedent(Atom atom) {
	antecedent.add(atom);
    }

    @Override
    public void addConsequent(Atom atom) {
	consequent.add(atom);
    }

    @Override
    public Rule cloneOnlyID(){
	Rule r = new RuleImpl();

	r.setNameRule(nameRule);
	r.setEnabled(enabled);

	for (Atom a: antecedent)
	    r.addAntecedent(a.cloneOnlyID());
	for (Atom a: consequent)
	    r.addConsequent(a.cloneOnlyID());
	return r;
    }

    @Override
    public boolean existsAtomAntecedent(Atom atom) {
	boolean result = false;
	for (Atom a: getAntecedent())
	    if (a.equals(atom))
		result = true;
	return result;
    }

    @Override
    public boolean existsAtomConsequent(Atom atom) {
	boolean result = false;
	for (Atom a: getConsequent())
	    if (a.equals(atom))
		result = true;
	return result;
    }

    @Override
    public List<Atom> getAntecedent() {
	return antecedent;
    }

    @Override
    public List<String> getAntecedentParaphrase() {
	return antecedentParaphrase;
    }

    @Override
    public Atom getAtomByValue(String value) {
	for(Atom a : getAtoms())
	    if(a.getPredicateID().equals(value) || a.getPredicateLabel().equals(value))
		return a;
	return null;
    }

    @Override
    public List<Atom> getAtoms() {
	List<Atom> atoms = new ArrayList<Atom>();
	if (getAntecedent() != null)
	    atoms.addAll(getAntecedent());
	if (getConsequent() != null)
	    atoms.addAll(getConsequent());
	return atoms;
    }

    @Override
    public List<Atom> getConsequent() {
	return consequent;
    }

    @Override
    public String getConsequentParaphrase() {
	return consequentParaphrase;
    }

    private String getFormatedAtomsID(List<Atom> atoms) {
	String result = "";
	for(Atom v:atoms)
	    result += v.getAtomID()+" ^ ";
	return result.length() > 0 ? result.substring(0, result.length()-3) : "";
    }

    private String getFormatedAtomsLabel(List<Atom> atoms) {
	String result = "";
	for(Atom v:atoms)
	    result += v.getAtomLabel()+" ^ ";
	return result.length() > 0 ? result.substring(0, result.length()-3) : "";
    }

    @Override
    public String getFormatedRuleID() {
	return getFormatedAtomsID(getAntecedent())+" -> "+getFormatedAtomsID(getConsequent());
    }

    @Override
    public String getFormatedRuleLabel() {
	return getFormatedAtomsLabel(getAntecedent())+" -> "+getFormatedAtomsLabel(getConsequent());
    }

    @Override
    public String getNameRule() {
	return nameRule;
    }

    @Override
    public int getNumAntecedent() {
	return antecedent.size();
    }

    @Override
    public int getNumAtoms() {
	return getNumAntecedent()+getNumConsequent();
    }

    @Override
    public int getNumConsequent() {
	return consequent.size();
    }

    private int getNumParamsDistinct(List<Atom> atoms) {
	int sum = 0;
	List<String> list = new ArrayList<String>();
	for(Atom a : atoms)
	    for(Variable v : a.getVariables())
		if(!list.contains(v.getFormatedID())){
		    sum++;
		    list.add(v.getFormatedID());
		}
	return sum;
    }

    @Override
    public int getNumVariables() {
	return getNumVariablesAntecedent() + getNumVariablesConsequent();
    }

    @Override
    public int getNumVariablesAntecedent() {
	int sum = 0;
	for(Atom a : getAntecedent())
	    sum += a.getCountVariables();
	return sum;
    }

    @Override
    public int getNumVariablesConsequent() {
	int sum = 0;
	for(Atom a : getConsequent())
	    sum += a.getCountVariables();
	return sum;
    }

    @Override
    public int getNumVariablesDistinct() {
	return getNumParamsDistinct(getAtoms());
    }

    @Override
    public int getNumVariablesDistinctAntecedent() {
	return getNumParamsDistinct(getAntecedent());
    }

    @Override
    public int getNumVariablesDistinctConsequent() {
	return getNumParamsDistinct(getConsequent());
    }

    @Override
    public String getParaphrase() {
	return paraphrase;
    }

    @Override
    public boolean isEnabled() {
	return enabled;
    }

    @Override
    public boolean removeAtom(Atom atom){
	if(antecedent.contains(atom))
	    return antecedent.remove(atom);
	else if(consequent.contains(atom))
	    return consequent.remove(atom);
	return false;
    }

    @Override
    public void setAntecedentParaphrase(List<String> antecedentParaphrase) {
	this.antecedentParaphrase = antecedentParaphrase;
    }

    @Override
    public void setConsequentParaphrase(String consequentParaphrase) {
	this.consequentParaphrase =  consequentParaphrase;
    }

    @Override
    public void setEnabled(boolean enabled) {
	this.enabled = enabled;
    }

    @Override
    public void setNameRule(String nameRule) {
	this.nameRule = nameRule;
    }

    @Override
    public void setParaphrase(String paraphrase) {
	this.paraphrase = paraphrase;
    }
}
