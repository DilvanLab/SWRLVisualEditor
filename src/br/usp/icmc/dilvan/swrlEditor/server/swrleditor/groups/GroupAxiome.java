package br.usp.icmc.dilvan.swrlEditor.server.swrleditor.groups;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.RuleSet;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Rule;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLFactory;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLImp;

public class GroupAxiome implements GroupRules {

	private OWLModel owlModel;
	private RuleSet ruleSet;
	private Map<String, List<String>> rules;

	@Override
	public void run() {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		List<String> list = null;

		RDFProperty hrg = owlModel
				.getRDFProperty("http://swrl.stanford.edu/ontologies/3.3/swrla.owl#hasRuleCategory");
		SWRLFactory factory = new SWRLFactory(owlModel);

		for (SWRLImp rule : factory.getImps()) {

			list = map.get(rule.getPropertyValue(hrg));
			if (list != null) {
				list.add(rule.getLocalName());
			} else {
				list = new ArrayList<String>();
				list.add(rule.getLocalName());
				map.put(rule.getPropertyValue(hrg) + "", list);
			}
		}
		rules = map;
	}

	@Override
	public String getAlgorithmName() {
		return "Axiome Groups";
	}

	@Override
	public boolean canSetNumberOfGroups() {
		return false;
	}

	@Override
	public void setNumberOfGroups(int numGroups) {
	}

	@Override
	public void setOWLModel(OWLModel model) {
		this.owlModel = model;
	}

	@Override
	public void setRuleSet(RuleSet rules) {
		this.ruleSet = rules;
	}

	@Override
	public int getNumberOfGroups() {
		return rules.size();
	}

	@Override
	public List<List<String>> getGroups() {
		List<List<String>> result = new ArrayList<List<String>>();
		for (List<String> l : rules.values())
			result.add(l);

				return result;
	}

	public List<Rule> getGroupOfThis(Rule rule) {

		List<Rule> result = new ArrayList<Rule>();

		RDFProperty hrg = owlModel
				.getRDFProperty("http://swrl.stanford.edu/ontologies/3.3/swrla.owl#hasRuleCategory");
		SWRLFactory factory = new SWRLFactory(owlModel);

		SWRLImp ruleSel = null;
		for (SWRLImp r : factory.getImps()) {
			if (rule.getNameRule().equals(r.getLocalName())) {
				ruleSel = r;
				break;
			}
		}
		if (ruleSel != null) {
			String groupThisRule = (String) ruleSel.getPropertyValue(hrg);
			if (groupThisRule == null)
				groupThisRule = "";
			for (SWRLImp r : factory.getImps()) {
				String group = (String) r.getPropertyValue(hrg);
				if (group == null)
					group = "";

				if (groupThisRule.equals(group)) {
					if (!ruleSel.getLocalName().equals(r.getLocalName())) {
						int rulePos = ruleSet.getRule(r.getLocalName());
						if (rulePos > -1)
							result.add(ruleSet.get(rulePos));

					}
				}
			}
		}

		return result;
	}

}
