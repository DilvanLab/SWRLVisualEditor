package br.usp.icmc.dilvan.swrlEditor.server.swrleditor.suggestterms;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.RuleSet;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Atom;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Rule;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Variable;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Variable.TYPE_VARIABLE;
import br.usp.icmc.dilvan.swrlEditor.server.swrleditor.suggestterms.AtomList.TYPE;



public class SuggestTerms {
	
	private RuleSet rules;
	private Rule rule;
	private AtomList atomList;
	Hashtable<String, Integer> suggestTerms;

	public SuggestTerms(RuleSet rules, Rule rule){
		this.rules = rules;
		this.rule = rule;
	}
	
	// Get the list of terms that combining, the limiar is the size min of connections among two terms
	public ArrayList<String> getTermsCombination(boolean useID){
		
		int limiar = 0;
		if(rule.getNumAtoms() == 1)
			limiar = 2;
		
		this.atomList = new AtomList(rules, TYPE.ATOM_FULL, useID);
		suggestTerms = new Hashtable<String, Integer>();
		 
		boolean isFirst = true;
		for(Atom a : rule.getAtoms()){
			// Excluding atoms with data literals parameter
			boolean cont = true;
			for(Variable p : a.getVariables()){ 
				if(p.getTypeVariable() == TYPE_VARIABLE.DATALITERAL){
					cont = false;
				}
			}
			if(!cont) continue;
			
			// Create a list of removed Terms and put in every suggest terms, when the atom contains the suggest terms, it is removed from this list
			ArrayList<String> removedTerms = new ArrayList<String>();
			for(String aux : suggestTerms.keySet())
				removedTerms.add(aux);
			
			// Get the atom in the graph of atoms
			AtomItem ai = null;
			if (useID)
				ai = atomList.getAtomItem(a.getAtomID());
			else
				ai = atomList.getAtomItem(a.getAtomLabel());
			
			if(ai == null) continue;
			
			// Iterating the connections to put in suggest list and remove it from removedTerms 
			Enumeration<AtomItem> e = ai.getConections().keys();
			while(e.hasMoreElements()){
				AtomItem subItem = e.nextElement();
				if(ai.getNumConection(subItem)>limiar){
					removedTerms.remove(subItem.getName());
					if(suggestTerms.contains(subItem.getName()))
						suggestTerms.put(subItem.getName(), suggestTerms.get(subItem.getName())+ai.getNumConection(subItem));
					else if(isFirst)
						suggestTerms.put(subItem.getName(), ai.getNumConection(subItem));
						
				}
			}
			
			// Removing the terms that unrelated with other terms
			for(String aux : removedTerms){
				suggestTerms.remove(aux);
			}
			isFirst = false;
		}
		
		// Sort the terms to be send to client
		ArrayList<String> terms = new ArrayList<String>();
		int i = 1;
		while(suggestTerms.size() > terms.size()){
			if(suggestTerms.containsValue(i)){
				for(String aux : suggestTerms.keySet()){
					if(suggestTerms.get(aux).equals(i)){
						terms.add(aux);
					}
				}
			}
			i++;
		}
		
		return terms;
	}
	
	// Getting the number of occurrence of the term in suggest 
	public int getNumOccurrences(String term){
		if(suggestTerms == null || !suggestTerms.containsKey(term))
			return 0;
		return suggestTerms.get(term);
	}
}
