package br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor;

import java.io.Serializable;

import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Rule;


@SuppressWarnings("serial")
public class RuleEvent implements Serializable {

	public enum TYPE_EVENT {INSERT, EDIT, DELETE};
	
	private String oldRuleName;
	private Rule rule;
	private TYPE_EVENT typeEvent;
	private Long versionOntology;
	
	public RuleEvent() {
		super();
	}

	public RuleEvent(String oldRuleName, Rule rule, TYPE_EVENT typeEvent, Long versionOntology) {
		super();
		this.oldRuleName = oldRuleName;
		this.rule = rule;
		this.typeEvent = typeEvent;
		this.versionOntology = versionOntology;
	}

	public String getOldRuleName() {
		return oldRuleName;
	}

	public void setOldRuleName(String oldRuleName) {
		this.oldRuleName = oldRuleName;
	}

	public Rule getRule() {
		return rule;
	}

	public void setRule(Rule rule) {
		this.rule = rule;
	}

	public TYPE_EVENT getTypeEvent() {
		return typeEvent;
	}

	public void setTypeEvent(TYPE_EVENT typeEvent) {
		this.typeEvent = typeEvent;
	}

	public Long getVersionOntology() {
		return versionOntology;
	}

	public void setVersionOntology(Long version) {
		this.versionOntology = version;
	}

	
}
