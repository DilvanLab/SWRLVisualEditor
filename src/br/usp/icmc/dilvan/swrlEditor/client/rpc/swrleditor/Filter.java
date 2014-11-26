package br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Atom;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Atom.TYPE_ATOM;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Rule;

@SuppressWarnings("serial")
public class Filter implements Serializable {

    private List<String> lstAnd;
    private List<String> lstOr;
    private List<String> lstNot;

    private boolean queryRuleName;
    private boolean queryClasses;
    private boolean queryDatatypeProperties;
    private boolean queryObjectProperties;
    private boolean queryBuiltin;
    private boolean queryComments;
    private boolean querySameDiferent;
    private boolean queryDataRange;

    private boolean queryAntecedent;
    private boolean queryConsequent;

    private boolean queryStartAtoms;
    private boolean queryMiddleAtoms;
    private boolean queryEndAtoms;

    public Filter(){
	lstAnd = new ArrayList<String>();
	lstOr = new ArrayList<String>();
	lstNot = new ArrayList<String>();

	queryRuleName = false;
	queryClasses = true;
	queryDatatypeProperties = true;
	queryObjectProperties = true;
	queryBuiltin = true;
	queryComments = false;
	querySameDiferent = true;
	queryDataRange = true;

	queryAntecedent = true;
	queryConsequent = true;
    }

    public boolean contains(Rule rule) {
	//.out.println("Nome regra:"+rule.getNameRule()+" --> Regra: "+rule.getFormatedRuleID());
	for (String s : lstAnd){
	    //System.out.println("testou AND: "+s);
	    if (queryRuleName)
		if (rule.getNameRule().toLowerCase().contains(s.toLowerCase()))
		    //System.out.println("Achou no nome da regra");
		    continue;
	    if (queryAntecedent && queryInListAtoms(rule.getAntecedent(), s))
		//System.out.println("Achou no antecedent");
		continue;
	    if (queryConsequent && queryInListAtoms(rule.getConsequent(), s))
		//System.out.println("Achou no consequent");
		continue;
	    return false;
	}

	boolean or = !(lstOr.size()>0);
	for (String s : lstOr){
	    //System.out.println("testou OR: "+s);
	    if (queryRuleName)
		if (rule.getNameRule().toLowerCase().contains(s.toLowerCase())){
		    //System.out.println("Achou no nome");
		    or = true;
		    break;
		}
	    if (queryAntecedent && queryInListAtoms(rule.getAntecedent(), s)){
		//System.out.println("Achou no antecedent");
		or = true;
		break;
	    }
	    if (queryConsequent && queryInListAtoms(rule.getConsequent(), s)){
		//System.out.println("Achou no consequent");
		or = true;
		break;
	    }
	}
	if (!or)
	    return false;

	for (String s : lstNot){
	    //System.out.println("testou NOT: "+s);
	    if (queryRuleName)
		if (rule.getNameRule().toLowerCase().contains(s.toLowerCase()))
		    //System.out.println("Achou no nome da regra");
		    return false;
	    if (queryAntecedent && queryInListAtoms(rule.getAntecedent(), s))
		//System.out.println("Achou no antecedent");
		return false;
	    if (queryConsequent && queryInListAtoms(rule.getConsequent(), s))
		//System.out.println("Achou no consequent");
		return false;
	}
	//System.out.println("Chegou...");
	return true;
    }

    public List<String> getLstAnd() {
	return lstAnd;
    }

    public List<String> getLstNot() {
	return lstNot;
    }

    public List<String> getLstOr() {
	return lstOr;
    }

    public boolean isEmpty(){
	return lstAnd.isEmpty() && lstOr.isEmpty() && lstNot.isEmpty();
    }

    public boolean isQueryAntecedent() {
	return queryAntecedent;
    }

    public boolean isQueryBuiltin() {
	return queryBuiltin;
    }

    public boolean isQueryClasses() {
	return queryClasses;
    }

    public boolean isQueryComments() {
	return queryComments;
    }

    public boolean isQueryConsequent() {
	return queryConsequent;
    }

    public boolean isQueryDataRange() {
	return queryDataRange;
    }

    public boolean isQueryDatatypeProperties() {
	return queryDatatypeProperties;
    }

    public boolean isQueryEndAtoms() {
	return queryEndAtoms;
    }

    public boolean isQueryMiddleAtoms() {
	return queryMiddleAtoms;
    }

    public boolean isQueryObjectProperties() {
	return queryObjectProperties;
    }

    public boolean isQueryRuleName() {
	return queryRuleName;
    }

    public boolean isQuerySameDiferent() {
	return querySameDiferent;
    }

    public boolean isQueryStartAtoms() {
	return queryStartAtoms;
    }

    private boolean queryInAtom(Atom a, String s){
	if (isQueryStartAtoms()){
	    if (a.getAtomID().toLowerCase().startsWith(s.toLowerCase()) || a.getAtomLabel().toLowerCase().startsWith(s.toLowerCase()))
		//System.out.println("Achou em ID Label");
		return true;
	    if (a.getAtomID().toLowerCase().startsWith(s.toLowerCase().replaceAll(" ", "")) || a.getAtomLabel().toLowerCase().startsWith(s.toLowerCase().replaceAll(" ", "")))
		//System.out.println("Achou em ID Label");
		return true;
	    if (queryComments)
		if (a.getPredicateComment().toLowerCase().startsWith(s.toLowerCase()))
		    //System.out.println("Achou em coment");
		    return true;
	}
	if (isQueryMiddleAtoms()){
	    if (a.getAtomID().toLowerCase().contains(s.toLowerCase()) || a.getAtomLabel().toLowerCase().contains(s.toLowerCase()))
		//System.out.println("Achou em ID Label");
		return true;
	    if (a.getAtomID().toLowerCase().contains(s.toLowerCase().replaceAll(" ", "")) || a.getAtomLabel().toLowerCase().contains(s.toLowerCase().replaceAll(" ", "")))
		//System.out.println("Achou em ID Label");
		return true;
	    if (queryComments)
		if (a.getPredicateComment().toLowerCase().contains(s.toLowerCase()))
		    //System.out.println("Achou em coment");
		    return true;
	}
	if (isQueryEndAtoms()){
	    if (a.getAtomID().toLowerCase().endsWith(s.toLowerCase()) || a.getAtomLabel().toLowerCase().endsWith(s.toLowerCase()))
		//System.out.println("Achou em ID Label");
		return true;

	    if (a.getAtomID().toLowerCase().endsWith(s.toLowerCase().replaceAll(" ", "")) || a.getAtomLabel().toLowerCase().endsWith(s.toLowerCase().replaceAll(" ", "")))
		//System.out.println("Achou em ID Label");
		return true;
	    if (queryComments)
		if (a.getPredicateComment().toLowerCase().endsWith(s.toLowerCase()))
		    //System.out.println("Achou em coment");
		    return true;
	}
	return false;
    }

    private boolean queryInListAtoms(List<Atom> atoms, String s){
	boolean findCond = false;

	for (Atom a: atoms)
	    if (a.getAtomType() == TYPE_ATOM.CLASS && queryClasses && queryInAtom(a, s)){
		//System.out.println("Achou em classes");
		findCond = true;
		break;
	    }
	    else if (a.getAtomType() == TYPE_ATOM.BUILTIN && queryBuiltin && queryInAtom(a, s)){
		//System.out.println("Achou em builtins");
		findCond = true;
		break;
	    }
	    else if (a.getAtomType() == TYPE_ATOM.DATARANGE && queryDataRange && queryInAtom(a, s)){
		//System.out.println("Achou em DataRange");
		findCond = true;
		break;
	    }
	    else if (a.getAtomType() == TYPE_ATOM.DATAVALUE_PROPERTY && queryDatatypeProperties && queryInAtom(a, s)){
		//System.out.println("Achou em DataValue");
		findCond = true;
		break;
	    }
	    else if (a.getAtomType() == TYPE_ATOM.INDIVIDUAL_PROPERTY && queryObjectProperties && queryInAtom(a, s)){
		//System.out.println("Achou em individual");
		findCond = true;
		break;
	    }
	    else if (a.getAtomType() == TYPE_ATOM.SAME_DIFERENT && querySameDiferent && queryInAtom(a, s)){
		//System.out.println("Achou em same");
		findCond = true;
		break;
	    }
	return findCond;
    }

    public void setLstAnd(List<String> lstAnd) {
	this.lstAnd = lstAnd;
    }

    public void setLstNot(List<String> lstNot) {
	this.lstNot = lstNot;
    }

    public void setLstOr(List<String> lstOr) {
	this.lstOr = lstOr;
    }

    public void setQueryAntecedent(boolean queryAntecedent) {
	this.queryAntecedent = queryAntecedent;
    }

    public void setQueryBuiltin(boolean queryBuiltin) {
	this.queryBuiltin = queryBuiltin;
    }

    public void setQueryClasses(boolean queryClasses) {
	this.queryClasses = queryClasses;
    }

    public void setQueryComments(boolean queryComments) {
	this.queryComments = queryComments;
    }

    public void setQueryConsequent(boolean queryConsequent) {
	this.queryConsequent = queryConsequent;
    }

    public void setQueryDataRange(boolean queryDataRange) {
	this.queryDataRange = queryDataRange;
    }

    public void setQueryDatatypeProperties(boolean queryDatatypeProperties) {
	this.queryDatatypeProperties = queryDatatypeProperties;
    }

    public void setQueryEndAtoms(boolean queryEndAtoms) {
	this.queryEndAtoms = queryEndAtoms;
    }

    public void setQueryMiddleAtoms(boolean queryMiddleAtoms) {
	this.queryMiddleAtoms = queryMiddleAtoms;
    }

    public void setQueryObjectProperties(boolean queryObjectProperties) {
	this.queryObjectProperties = queryObjectProperties;
    }

    public void setQueryRuleName(boolean queryRuleName) {
	this.queryRuleName = queryRuleName;
    }

    public void setQuerySameDiferent(boolean querySameDiferent) {
	this.querySameDiferent = querySameDiferent;
    }

    public void setQueryStartAtoms(boolean queryStartAtoms) {
	this.queryStartAtoms = queryStartAtoms;
    }
}
