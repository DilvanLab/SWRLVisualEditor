package br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.activity.info;

import java.util.ArrayList;
import java.util.List;

import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.RuleSet;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Atom;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Rule;


public class RegisterInfo {

	public int maxAtoms = 0;
	public int minAtoms = 10000;
	public int sumVariables = 0;
	public int sumVariablesDistinct = 0;
	public int maxNumVariables = 0;
	public int maxNumVariablesDistinct = 0;
	public int minNumVariables = 10000;
	public int minNumVariablesDistinct = 10000;

	public int maxAtomAntecedent = 0;
	public int minAtomAntecedent = 10000;
	public int sumVariablesAntecedent = 0;
	public int maxNumVariablesAntecedent = 0;
	public int minNumVariablesAntecedent = 10000;
	public int maxNumVariablesDistinctAntecedent = 0;
	public int minNumVariablesDistinctAntecedent = 10000;
	public int sumVariablesDistinctAntecedent = 0;

	public int maxAtomConsequent = 0;
	public int minAtomConsequent = 10000;
	public int sumVariablesConsequent = 0;
	public int maxNumVariablesConsequent = 0;
	public int minNumVariablesConsequent = 10000;
	public int maxNumVariablesDistinctConsequent = 0;
	public int minNumVariablesDistinctConsequent = 10000;
	public int sumVariablesDistinctConsequent = 0;

	public int sumPredicateDistinct = 0;
	public int sumPredicateDistinctAntecedent = 0;
	public int sumPredicateDistinctConsequent = 0;
	
	
	private RuleSet rules;

	public RegisterInfo(RuleSet rules) {
		super();
		this.rules = rules;
		generatedDistinctInfo();
	}

	private void generatedDistinctInfo(){
		List<String> predicateDistinct = new ArrayList<String>();
		List<String> predicateDistinctAntecedent = new ArrayList<String>();
		List<String> predicateDistinctConsequent = new ArrayList<String>();

		for (Rule r : rules){

			//Gera numero maximo de atomos
			if( r.getNumAtoms() > maxAtoms )
				maxAtoms = r.getNumAtoms();

			//Gera numero minimo de atomos
			if( r.getNumAtoms() < minAtoms )
				minAtoms = r.getNumAtoms();

			// Soma do numero de variaveis
			sumVariables += r.getNumVariables();

			// Soma do numero de variaveis distintas
			sumVariablesDistinct += r.getNumVariablesDistinct();

			// Maximo de variaveis em uma regra
			if( r.getNumVariables() > maxNumVariables )
				maxNumVariables = r.getNumVariables();

			// Maximo de variaveis distintas em uma regra
			if( r.getNumVariablesDistinct() > maxNumVariablesDistinct )
				maxNumVariablesDistinct = r.getNumVariablesDistinct();

			// Minimo de variaveis em uma regra
			if( r.getNumVariables() < minNumVariables )
				minNumVariables = r.getNumVariables();

			// Minimo de variaveis distintas em uma regra
			if( r.getNumVariablesDistinct() < minNumVariablesDistinct )
				minNumVariablesDistinct = r.getNumVariablesDistinct();


			//Antecedente
			// Maximo de atomos no Antecedente
			if( r.getNumAntecedent() > maxAtomAntecedent )
				maxAtomAntecedent = r.getNumAntecedent();

			// Minimo de atomos no Antecedente
			if( r.getNumAntecedent() < minAtomAntecedent )
				minAtomAntecedent = r.getNumAntecedent();

			// Soma do numero de variaveis no Antecedente
			sumVariablesAntecedent += r.getNumVariablesAntecedent();

			// Maximo de variaveis no Antecedente
			if( r.getNumVariablesAntecedent() > maxNumVariablesAntecedent )
				maxNumVariablesAntecedent = r.getNumVariablesAntecedent();

			// Minimo de variaveis no Antecedente
			if( r.getNumVariablesAntecedent() < minNumVariablesAntecedent )
				minNumVariablesAntecedent = r.getNumVariablesAntecedent();

			// Maximo de variaveis distintas no Antecedente
			if( r.getNumVariablesDistinctAntecedent() > maxNumVariablesDistinctAntecedent )
				maxNumVariablesDistinctAntecedent = r.getNumVariablesDistinctAntecedent();

			// Minimo de variaveis distintas no Antecedente
			if( r.getNumVariablesDistinctAntecedent() < minNumVariablesDistinctAntecedent )
				minNumVariablesDistinctAntecedent = r.getNumVariablesDistinctAntecedent();

			// Soma das variaveis distintas no Antecedente
			sumVariablesDistinctAntecedent += r.getNumVariablesDistinctAntecedent();


			//Consequente
			// Maximo de atomos no Consequente
			if( r.getNumConsequent() > maxAtomConsequent )
				maxAtomConsequent = r.getNumConsequent();

			// Minimo de atomos no Consequente
			if( r.getNumConsequent() < minAtomConsequent )
				minAtomConsequent = r.getNumConsequent();

			// Soma do numero de variaveis no Consequente
			sumVariablesConsequent += r.getNumVariablesConsequent();

			// Maximo de variaveis no Consequente
			if( r.getNumVariablesConsequent() > maxNumVariablesConsequent )
				maxNumVariablesConsequent = r.getNumVariablesConsequent();

			// Maximo de variaveis no Consequente
			if( r.getNumVariablesConsequent() < minNumVariablesConsequent )
				minNumVariablesConsequent = r.getNumVariablesConsequent();

			// Maximo de variaveis distintas no Consequente
			if( r.getNumVariablesDistinctConsequent() > maxNumVariablesDistinctConsequent )
				maxNumVariablesDistinctConsequent = r.getNumVariablesDistinctConsequent();
			
			// Minimo de variaveis distintas no Consequente
			if( r.getNumVariablesDistinctConsequent() < minNumVariablesDistinctConsequent )
				minNumVariablesDistinctConsequent = r.getNumVariablesDistinctConsequent();
			
			// Soma das variaveis distintas no Consequente
			sumVariablesDistinctConsequent += r.getNumVariablesDistinctConsequent();

			for (Atom a : r.getAntecedent()){

				if(!predicateDistinct.contains(a.getPredicateID())){
					sumPredicateDistinct++;
					predicateDistinct.add(a.getPredicateID());
				}
				
				if(!predicateDistinctAntecedent.contains(a.getPredicateID())){
					sumPredicateDistinctAntecedent++;
					predicateDistinctAntecedent.add(a.getPredicateID());
				}
			}

			for (Atom a : r.getConsequent()){
				if(!predicateDistinct.contains(a.getPredicateID())){
					sumPredicateDistinct++;
					predicateDistinct.add(a.getPredicateID());
				}
				
				if(!predicateDistinctConsequent.contains(a.getPredicateID())){
					sumPredicateDistinctConsequent++;
					predicateDistinctConsequent.add(a.getPredicateID());
				}
			}
			
			
		}
	}
}
