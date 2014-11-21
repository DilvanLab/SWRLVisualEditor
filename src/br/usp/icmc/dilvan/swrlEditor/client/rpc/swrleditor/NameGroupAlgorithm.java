package br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor;

import java.io.Serializable;

@SuppressWarnings("serial")
public class NameGroupAlgorithm implements Serializable{

	private String name;
	private boolean canSetNumberOfGroups;

	public NameGroupAlgorithm(){}
	
	public NameGroupAlgorithm(String name, boolean canSetNumberOfGroups) {
		super();
		this.name = name;
		this.canSetNumberOfGroups = canSetNumberOfGroups;
	}

	public String getName() {
		return name;
	}

	public boolean isCanSetNumberOfGroups() {
		return canSetNumberOfGroups;
	}
}
