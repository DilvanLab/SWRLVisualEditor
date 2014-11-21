package br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.activity.info;

public class RuleInfo {
	
	public String info;
	public int total;
	public int antecedent;
	public int consequent;

	public RuleInfo(){
	}
	
	public RuleInfo(String info) {
		this(info, -1, -1, -1);
	}

	public RuleInfo(String info, int rule, int antecend, int consequent) {
		this.info = info;
		this.total = rule;
		this.antecedent = antecend;
		this.consequent = consequent;
	}
	
	public String toString(){
		return info+", "+total+", "+antecedent+", "+consequent;
	}
}
