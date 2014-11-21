package br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor;

import java.util.ArrayList;

import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Rule;


@SuppressWarnings("serial")
public class RuleSetImpl extends ArrayList<Rule> implements RuleSet {

	
	private Long versionOntology;
	
	public RuleSetImpl(){
		versionOntology = new Long(-1);
		
	}

	@Override
	public int getCountAtoms() {
		int total = 0;
		for( Rule r : this){
			total += r.getNumAtoms();
		}
		return total;
	}
	@Override
	public int getCountAtomsAntecedent() {
		int sum = 0;
		for( Rule r : this){
			sum += r.getNumAntecedent();
		}
		return sum;
	}

	@Override
	public int getCountAtomsConsequent() {
		int sum = 0;
		for( Rule r : this){
			sum += r.getNumConsequent();
		}
		return sum;
	}
	
	@Override
	public Long getVersionOntology() {
		return versionOntology;
	}
	
	@Override
	public void setVersionOntology(Long version) {
		this.versionOntology = version;
		
	}

	
	public int getRule(String ruleName) {
		for( int i = 0; i <this.size(); i++){
			if (this.get(i).getNameRule().equals(ruleName)){
				return i;
			}
		}
		return -1;
	}
	
	@Override
	public int getIndexToInsertRule(String name){
		for (int i = 0; i < this.size(); i++)
			if (this.get(i).getNameRule().compareTo(name) >= 0)
				return i;
		
		return size();
	}

}
