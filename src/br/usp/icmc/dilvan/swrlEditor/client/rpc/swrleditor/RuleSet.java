package br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor;

import java.io.Serializable;
import java.util.List;

import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Rule;


public interface RuleSet extends Serializable, List<Rule> {
	public int getCountAtoms();
	public int getCountAtomsAntecedent();
	public int getCountAtomsConsequent();

	public int getRule(String ruleName);
	public int getIndexToInsertRule(String name);

	public Long getVersionOntology();
	public void setVersionOntology(Long version);
}
