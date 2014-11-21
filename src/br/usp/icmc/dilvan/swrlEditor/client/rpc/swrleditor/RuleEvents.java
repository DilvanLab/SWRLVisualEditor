package br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class RuleEvents extends ArrayList<RuleEvent> implements Serializable  {
	
	private Long versionOntology;
	
	public RuleEvents() {
		super();
	}
	
	public Long getVersionOntology() {
		return versionOntology;
	}

	public void setVersionOntology(Long version) {
		this.versionOntology = version;
		
	}

}
