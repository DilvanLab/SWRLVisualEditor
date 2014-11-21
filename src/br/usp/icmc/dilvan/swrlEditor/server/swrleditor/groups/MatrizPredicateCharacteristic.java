package br.usp.icmc.dilvan.swrlEditor.server.swrleditor.groups;


import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.RuleSet;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Atom;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Rule;


public class MatrizPredicateCharacteristic {
	public enum DISTANCE_MODE {EUCLIDIAN, MANHATHAN};
	private ArrayList<String> characteristics;
	private ArrayList<Rule> rules;
	private ArrayList<Hashtable<String, Integer>> matriz;
	
	public MatrizPredicateCharacteristic(){
		characteristics = new ArrayList<String>();
		rules = new ArrayList<Rule>();
		matriz = new ArrayList<Hashtable<String, Integer>>();
	}
	
	public void addCharacteristic(String charac){
		if(!characteristics.contains(charac)){
			characteristics.add(charac);
			for(Hashtable<String, Integer> rule:matriz){
				rule.put(charac, 0);
			}
		}
	}
	
	public String[] getCharacteristic(){
		String[] ret = new String[characteristics.size()];
		int i = 0;
		for(String s:characteristics)
			ret[i++] = s;
		
		return ret;
	}
	
	public int getNumCharacteristic(){
		return characteristics.size();
	}
	
	public int[] getValues(Rule r){
		Hashtable<String, Integer> vector = matriz.get(rules.indexOf(r));
		int[] ret = new int[vector.size()];
		int i = 0;
		for(String s:characteristics)
			ret[i++] = vector.get(s);
		
		return ret;
	}
	
	public void showValues(Rule r){
		Hashtable<String, Integer> vector = matriz.get(rules.indexOf(r));

		for(String s:characteristics)
			System.out.print(vector.get(s));
		System.out.println(" = "+r.getNameRule());
	}
	
	public int getValue(Rule r, String charac){
		Hashtable<String, Integer> vector = matriz.get(rules.indexOf(r));
		return vector.get(charac);
	}
	
	public void addRule(Rule rule, String charac, int number){
		addCharacteristic(charac);
		Hashtable<String, Integer> aux;
		if( rules.contains(rule) ){
			aux = matriz.get(rules.indexOf(rule));
		} else {
			rules.add(rule);
			aux = new Hashtable<String, Integer>();
			matriz.add(aux);	
		}
		aux.put(charac, number);
	}
	
	public void addRule(Rule rule){
		addAtoms(rule, rule.getAtoms());
	}
	
	public void addAntecedent(Rule rule){
		addAtoms(rule, rule.getAntecedent());
	}
	
	public void addConsequent(Rule rule){
		addAtoms(rule, rule.getConsequent());
	}
	
	protected void addAtoms(Rule rule, List<Atom> atoms) {
		Hashtable<String, Integer> predicates = new Hashtable<String, Integer>();
		for(Atom atom : atoms){
			Integer value = predicates.get(getValueReference(atom));
			if( value == null )
				value = 0;
			predicates.put(getValueReference(atom),value+1);
			addCharacteristic(getValueReference(atom));
		}
		for(String charac:characteristics){
			Integer value = predicates.get(charac);
			if( value == null )
				value = 0;
			addRule(rule, charac, value);
		}
	}

	protected String getValueReference(Atom a){
		return a.getPredicateID();
	}
	
	public void addRule(RuleSet rls){
		for(Rule rule:rls){
			addRule(rule);
		}
	}
	
	public void fill(Rule r){
		Hashtable<String, Integer> vector = matriz.get(rules.indexOf(r));
		for(String charac:characteristics){
			Integer value = vector.get(charac);
			if( value == null ){
				vector.put(charac, 0);
			}
		}
	}
	
	public ArrayList<Rule> getIdenticalRules(Rule r){
		return getIdenticalRules(r, DISTANCE_MODE.MANHATHAN, 0);
	}
	
	public ArrayList<Rule> getIdenticalRules(Rule r, DISTANCE_MODE mode, double limit){
		ArrayList<Rule> rulesReturn = new ArrayList<Rule>();
		Hashtable<Rule, Double> matDiff = getMatrizDifference(r, mode);
		Enumeration<Rule> rls = matDiff.keys();
		while(rls.hasMoreElements()){
			Rule rule = rls.nextElement();
			if(matDiff.get(rule) <= limit)
				rulesReturn.add(rule);
		}
		return rulesReturn;
	}
	
	public Hashtable<Rule, Double> getMatrizDifference(Rule r){
		return getMatrizDifference(r, DISTANCE_MODE.MANHATHAN);
	}
	
	public Hashtable<Rule, Double> getMatrizDifference(Rule r, DISTANCE_MODE mode){
		Hashtable<Rule, Double> matReturn = new Hashtable<Rule, Double>();
		switch( mode ){
		case EUCLIDIAN:
			for(Rule rule:rules){
				if(r.equals(rule)) continue;
				matReturn.put(rule, getEuclidianDistance(r, rule));
			}
			break;
		case MANHATHAN:
			for(Rule rule:rules){
				if(r.equals(rule)) continue;
				matReturn.put(rule, getManhathanDistance(r, rule));
			}
			break;
		}
		return matReturn;
	}
	
	public double getEuclidianDistance(Rule rule1, Rule rule2){
		return getEuclidianDistance(rules.indexOf(rule1),rules.indexOf(rule2));
	}

	public double getEuclidianDistance(int rule1, int rule2) {
		Hashtable<String, Integer> r1 = matriz.get(rule1);
		Hashtable<String, Integer> r2 = matriz.get(rule2);
		double sum = 0;
		for(String charactName: characteristics){
			sum += Math.pow(r1.get(charactName) - r2.get(charactName), 2);
		}
		return Math.sqrt(sum);
	}
	
	public double getManhathanDistance(Rule rule1, Rule rule2){
		return getManhathanDistance(rules.indexOf(rule1),rules.indexOf(rule2));
	}

	public double getManhathanDistance(int rule1, int rule2) {
		Hashtable<String, Integer> r1 = matriz.get(rule1);
		Hashtable<String, Integer> r2 = matriz.get(rule2);
		double sum = 0;
		for(String charactName: characteristics){
			sum += Math.abs(r1.get(charactName) - r2.get(charactName));
		}
		return sum;
	}
	
	public ArrayList<ArrayList<Rule>> getGroupRules(){
		return getGroupRules(DISTANCE_MODE.MANHATHAN, 1);
	}
	
	public ArrayList<ArrayList<Rule>> getGroupRules(DISTANCE_MODE mode, double limit){
		ArrayList<ArrayList<Rule>> groups = new ArrayList<ArrayList<Rule>>();
		// First group has all rules that are alone
		groups.add(new ArrayList<Rule>());
		ArrayList<Rule> used = new ArrayList<Rule>();
		for(int i = 0; i < rules.size(); i++){
			Rule rule = rules.get(i);
			if( used.contains(rule)) continue;
			
			ArrayList<Rule> aux = new ArrayList<Rule>();
			
			Hashtable<Rule, Double> matdiff = getMatrizDifference(rule,mode);
			for(int j= i+1; j<rules.size(); j++){
				Rule r = rules.get(j);
				Double val = matdiff.get(r);
				if(val==null)
					continue;
				
				if(val <= limit){
					used.add(r);
					aux.add(r);
				}
			}
			
			used.add(rule);
			if( aux.isEmpty()){
				groups.get(0).add(rule);
			} else {
				aux.add(0,rule);
				groups.add(aux);
			}
		}
		
		return groups;
	}

	public void removeRule(Rule rule) {
		int pos = rules.indexOf(rule);
		rules.remove(rule);
		matriz.remove(pos);
	}
	
}
