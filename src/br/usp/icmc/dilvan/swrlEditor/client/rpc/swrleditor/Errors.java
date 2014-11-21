package br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class Errors implements Serializable {
	
	private ArrayList<String> errors;
	private ArrayList<String> warnings;
	
	public Errors(){
		errors = new ArrayList<String>();
		warnings = new ArrayList<String>();
	}

	public void setErrors(ArrayList<String> erros) {
		this.errors = erros;
	}

	public void addErrors(String error){	
		errors.add(error);
	}
	
	public ArrayList<String> getErrors() {
		return errors;
	}

	public void setWarnings(ArrayList<String> warnings) {
		this.warnings = warnings;
	}
	
	public void addWarning(String warning){
		warnings.add(warning);
	}
	
	public ArrayList<String> getWarnings() {
		return warnings;
	}
	
	public ArrayList<String> getAll(){
		ArrayList<String> aux = new ArrayList<String>();
		for(String a:getErrors())
			aux.add(a);
		for(String a: getWarnings())
			aux.add(a);
		return aux;
	}
	
	
}
