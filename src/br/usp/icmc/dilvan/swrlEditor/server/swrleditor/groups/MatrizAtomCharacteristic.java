package br.usp.icmc.dilvan.swrlEditor.server.swrleditor.groups;

import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Atom;

public class MatrizAtomCharacteristic extends MatrizPredicateCharacteristic {
	
	@Override
	public String getValueReference(Atom a){
		return a.getAtomID();
	}
	
}
