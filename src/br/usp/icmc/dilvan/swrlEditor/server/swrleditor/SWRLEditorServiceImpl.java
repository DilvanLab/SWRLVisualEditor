package br.usp.icmc.dilvan.swrlEditor.server.swrleditor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.Errors;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.NameGroupAlgorithm;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.RuleEvents;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.RuleSet;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.decisiontree.NodeDecisionTree;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Atom;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Atom.TYPE_ATOM;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.AtomImpl;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Rule;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.RuleImpl;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Variable;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.VariableImpl;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.SWRLService;
import br.usp.icmc.dilvan.swrlEditor.server.swrleditor.decisiontree.GenerateNodeRootDecisionTree;
import br.usp.icmc.dilvan.swrlEditor.server.swrleditor.groups.GroupAxiome;
import br.usp.icmc.dilvan.swrlEditor.server.swrleditor.groups.GroupRules;
import br.usp.icmc.dilvan.swrlEditor.server.swrleditor.manager.OntologyManager;
import br.usp.icmc.dilvan.swrlEditor.server.swrleditor.manager.SWRLManager;
import br.usp.icmc.dilvan.swrlEditor.server.swrleditor.manager.protege3.OntologyManagerProtege3;
import br.usp.icmc.dilvan.swrlEditor.server.swrleditor.manager.protege3.SWRLManagerProtege3;
import br.usp.icmc.dilvan.swrlEditor.server.swrleditor.suggestterms.SuggestTerms;
import br.usp.icmc.dilvan.swrlEditor.swrlEditorTab.SWRLEditorTab;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class SWRLEditorServiceImpl extends RemoteServiceServlet implements SWRLService {
    private final Map<String,OntologyManager> ontologyManager = new HashMap<String,OntologyManager>();
    private final Map<String,SWRLManager> swrlManager = new HashMap<String,SWRLManager>();

    @Override
    public boolean deleteRule(String projectName, String ruleName) {
	return getSWRLManager(projectName).deleteRule(ruleName);
    }

    private Atom getAtom(String projectName, String atom){
	Atom newAtom = new AtomImpl();

	if (atom.contains("(")){
	    newAtom.setPredicateID(atom.substring(0,atom.indexOf("(")).trim());

	    newAtom.setAtomType(setAtomType(projectName, newAtom.getPredicateID()));
	    if (newAtom.getAtomType() == TYPE_ATOM.NULL){
		List<String> listId = getOntologyManager(projectName).getIDsForLabel(newAtom.getPredicateID(), true);
		if (listId.size() > 0)
		    newAtom.setAtomType(setAtomType(projectName, listId.get(0)));
	    }

	    String aux;
	    if (atom.contains(")"))
		aux = atom.substring(atom.indexOf("(")+1,atom.indexOf(")"));
	    else
		aux = atom.substring(atom.indexOf("(")+1,atom.length());

	    if(aux.indexOf("'")>-1){
		StringBuilder newAux = new StringBuilder();
		boolean b = false;
		char []vetAux = aux.toCharArray();
		for(int i = 0; i < aux.length(); i++){
		    if( vetAux[i] == '\'' )
			b = !b;
		    if(vetAux[i] == ',' & b)
			newAux.append("[comma]");
		    else
			newAux.append(vetAux[i]);
		}
		aux = newAux.toString();
	    }

	    Variable newVar;

	    int count = 1;
	    for(String v: aux.split(",")){
		if( v.indexOf("[comma]")>-1)
		    v = v.replace("[comma]", ",");

		newVar = new VariableImpl();

		if(v.trim().startsWith("?"))
		    newVar.setSimpleID(v.trim().substring(1));
		else if(v.trim().startsWith("\"")){
		    if (v.trim().endsWith("\""))
			newVar.setSimpleID(v.trim().substring(1, v.trim().length()-1));
		    else
			newVar.setSimpleID(v.trim().substring(1, v.trim().length()));
		} else
		    newVar.setSimpleID(v.trim());

		newVar.setTypeVariable(VariableImpl.getTYPE_VARIABLE(newAtom.getAtomType(), newAtom.getPredicateID(), v, count++));

		newAtom.addVariable(newVar);
	    }
	}else{
	    newAtom.setPredicateID(atom);
	    newAtom.setAtomType(TYPE_ATOM.NULL);
	}
	return newAtom;
    }

    private List<Atom> getAtoms(String projectName, String atoms){
	List<Atom> result = new ArrayList<Atom>();

	String listAtoms[] = atoms.split("\\^");
	for(String atom :listAtoms)
	    result.add(getAtom(projectName, atom));
	return result;
    }

    @Override
    public NodeDecisionTree getDecisionTree(String projectName, String algorithmName) {
	for (GenerateNodeRootDecisionTree alg : loadDecisionTreeAlgorithms())
	    if (alg.getAlgorithmName().equals(algorithmName))
		try {
		    alg.setRuleSet(getRules(projectName));
		    alg.setSWRLFactory(((SWRLManagerProtege3)getSWRLManager(projectName)).getSWRLFactory());
		    alg.run();
		    return alg.getRootNode();
		} catch (Exception e) {
		    GWT.log("Error: "+e.getMessage());
		    break;
		}
	return null;
    }

    @Override
    public ArrayList<String> getDecisionTreeAlgorithmsList() {
	ArrayList<String> result = new ArrayList<String>();

	for (GenerateNodeRootDecisionTree alg : loadDecisionTreeAlgorithms())
	    result.add(alg.getAlgorithmName());
	return result;
    }

    @Override
    public Errors getErrorsList(String projectName, Rule rule, boolean isNew){
	return getSWRLManager(projectName).getErrorsList(rule, isNew);
    }

    @Override
    public ArrayList<NameGroupAlgorithm> getGroupAlgorithmsList() {
	ArrayList<NameGroupAlgorithm> ret = new ArrayList<NameGroupAlgorithm>();
	for (GroupRules alg : loadGroupAlgorithms())
	    ret.add(new NameGroupAlgorithm(alg.getAlgorithmName(), alg.canSetNumberOfGroups()));

	return ret;
    }

    @Override
    public ArrayList<ArrayList<String>> getGroups(String projectName,
	    String algorithmName, int numGroups) {
	ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();

	for (GroupRules alg : loadGroupAlgorithms())
	    if (alg.getAlgorithmName().equals(algorithmName)){
		try {
		    alg.setNumberOfGroups(numGroups);
		    alg.setRuleSet(getRules(projectName));
		    alg.setOWLModel(((OntologyManagerProtege3)getOntologyManager(projectName)).getOwlModel());
		    alg.run();
		    List<List<String>> original = alg.getGroups();

		    for (List<String> aux : original)
			result.add((ArrayList<String>) aux);

		} catch (Exception e) {
		    GWT.log("Error: "+e.getMessage());
		    break;
		}
		break;
	    }

	return result;
    }

    /**
     * Return the List of Entity presents in the ontology used in projectName
     * The Entity can be class, object and individual properties and builtins
     */
    @Override
    public ArrayList<String> getListBuiltins(String projectName) {
	SWRLManager swrlManager = getSWRLManager(projectName);
	return (ArrayList<String>) swrlManager.getBuiltins();
    }

    /**
     * Return the OntologyManager to manipulates the ontology
     */
    protected OntologyManager getOntologyManager(String projectName){
	if(!ontologyManager.containsKey(projectName))
	    ontologyManager.put(projectName, new OntologyManagerProtege3(
		    SWRLEditorTab.getKb(), projectName));
	return ontologyManager.get(projectName);
    }

    @Override
    public Rule getRule(String projectName, String ruleName) {
	for (Rule rule : getSWRLManager(projectName).getRules())
	    if (ruleName.equals(rule.getNameRule()))
		return rule;
	return null;
    }

    @Override
    public RuleEvents getRuleEvents(String projectName, Long fromVersion) {
	return getSWRLManager(projectName).getRuleEvents(fromVersion);
    }

    @Override
    public String getRuleName(String projectName, String antecedent, String consequent) {

	List<Atom> antecedents = getAtoms(projectName, antecedent);
	List<Atom> consequents = getAtoms(projectName, consequent);

	RuleSet rules = getRules(projectName);

	for (Rule r : rules){
	    if (r.getAntecedent().size() != antecedents.size() || r.getConsequent().size() != consequents.size())
		continue;
	    boolean findRule = true;
	    for (Atom a: antecedents)
		if (!r.existsAtomAntecedent(a)){
		    findRule = false;
		    break;
		}
	    if (findRule)
		for (Atom a: consequents)
		    if (!r.existsAtomConsequent(a)){
			findRule = false;
			break;
		    }
	    if (findRule)
		return r.getNameRule();
	}
	return "";
    }

    /**
     * Return all rules presents in projectName
     */
    @Override
    public RuleSet getRules(String projectName) {
	return getSWRLManager(projectName).getRules();
    }

    @Override
    public ArrayList<String> getSelfCompletion(String projectName, String text,
	    int maxTerms, TYPE_ATOM typeAtom) {
	List<String> result = new ArrayList<String>();

	if (typeAtom == TYPE_ATOM.BUILTIN)
	    return (ArrayList<String>) getSWRLManager(projectName).getBuiltins(text, maxTerms);
	else if (typeAtom == TYPE_ATOM.SAME_DIFERENT)
	    return (ArrayList<String>) getOntologyManager(projectName).getOWLSameAsDiferentFrom(text, maxTerms);
	else if (typeAtom == TYPE_ATOM.CLASS)
	    return (ArrayList<String>) getOntologyManager(projectName).getOWLClass(text, maxTerms);
	else if (typeAtom == TYPE_ATOM.DATARANGE)
	    return (ArrayList<String>) getOntologyManager(projectName).getOWLDatatype(text, maxTerms);
	else if (typeAtom == TYPE_ATOM.DATAVALUE_PROPERTY)
	    return (ArrayList<String>) getOntologyManager(projectName).getOWLDatatypePropertie(text, maxTerms);
	else if (typeAtom == TYPE_ATOM.INDIVIDUAL_PROPERTY)
	    return (ArrayList<String>) getOntologyManager(projectName).getOWLObjectPropertie(text, maxTerms);

	return (ArrayList<String>) result;
    }

    @Override
    public ArrayList<Rule> getSimilarRules(String projectName, Rule base, float distance, boolean isNew) {
	GroupAxiome gpr =  new GroupAxiome();
	gpr.setOWLModel(((OntologyManagerProtege3)getOntologyManager(projectName)).getOwlModel());
	gpr.setRuleSet(getRules(projectName));

	return (ArrayList<Rule>) gpr.getGroupOfThis(base);
    }

    @Override
    public ArrayList<String> getSimilarRulesAlgorithmsList() {
	ArrayList<String> ret = new ArrayList<String>();
	ret.add("Default");

	return ret;
    }

    @Override
    public Rule getStringToRule(String projectName, String rule) {
	String parts[] = rule.split("->");
	Rule newRule = new RuleImpl();

	if(parts.length>=1)
	    if(!parts[0].trim().isEmpty())
		for(Atom a : getAtoms(projectName, parts[0].trim()))
		    newRule.addAntecedent(a);

	if(parts.length==2)
	    if(!parts[1].trim().isEmpty())
		for(Atom a : getAtoms(projectName, parts[1].trim()))
		    newRule.addConsequent(a);
	return newRule;
    }

    @Override
    public ArrayList<Atom> getSuggestTerms(String projectName, Rule rule) {
	rule.setNameRule("suggestRule");
	SuggestTerms st = new SuggestTerms(getRules(projectName), rule);

	ArrayList<Atom> listAtom = new ArrayList<Atom>();
	for(String aux : st.getTermsCombination(true)){
	    Atom a = getAtom(projectName, aux);
	    listAtom.add(a);
	}
	return listAtom;
    }

    @Override
    public ArrayList<String> getSuggestTermsAlgorithmsList() {
	ArrayList<String> ret = new ArrayList<String>();
	ret.add("Default");

	return ret;
    }

    /**
     * Return the SWRLManager to manipulates rules in projectName
     */
    protected SWRLManager getSWRLManager(String projectName){
	if(!swrlManager.containsKey(projectName))
	    swrlManager.put(projectName, getOntologyManager(projectName).getSWRLManager());
	return swrlManager.get(projectName);
    }

    protected ServiceLoader<GenerateNodeRootDecisionTree> loadDecisionTreeAlgorithms(){
	return ServiceLoader.load(GenerateNodeRootDecisionTree.class);
    }

    protected ServiceLoader<GroupRules> loadGroupAlgorithms(){
	return ServiceLoader.load(GroupRules.class);
    }

    @Override
    public ArrayList<String> runRules(String projectName) {
	return (ArrayList<String>) getSWRLManager(projectName).runRules();
    }

    @Override
    public boolean saveEditRule(String projectName, String ruleName, String oldRuleName, Rule rule) {
	return getSWRLManager(projectName).updateRule(ruleName, oldRuleName, rule);
    }

    @Override
    public boolean saveNewRule(String projectName, String ruleName, Rule rule) {
	return getSWRLManager(projectName).insertRule(ruleName, rule);
    }

    private TYPE_ATOM setAtomType(String projectName, String predicate){
	if (getOntologyManager(projectName).hasOWLClass(predicate))
	    return TYPE_ATOM.CLASS;
	else if (getOntologyManager(projectName).hasOWLDatatypePropertie(predicate))
	    return TYPE_ATOM.DATAVALUE_PROPERTY;
	else if (getOntologyManager(projectName).hasOWLObjectPropertie(predicate))
	    return TYPE_ATOM.INDIVIDUAL_PROPERTY;
	else if (getSWRLManager(projectName).hasBuiltin(predicate))
	    return TYPE_ATOM.BUILTIN;
	else if (getOntologyManager(projectName).hasOWLDatatype(predicate))
	    return TYPE_ATOM.DATARANGE;
	else if( predicate.toLowerCase().equals("sameas") || predicate.toLowerCase().equals("differentfrom") )
	    return TYPE_ATOM.SAME_DIFERENT;
	else
	    return TYPE_ATOM.NULL;
    }
}
