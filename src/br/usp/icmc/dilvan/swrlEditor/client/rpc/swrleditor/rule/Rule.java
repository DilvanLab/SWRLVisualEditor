package br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule;

import java.io.Serializable;
import java.util.List;

public interface Rule extends Serializable{

    public void addAntecedent(Atom atom);
    public void addConsequent(Atom atom);

    public Rule cloneOnlyID();
    public boolean existsAtomAntecedent(Atom atom);

    public boolean existsAtomConsequent(Atom atom);
    public List<Atom> getAntecedent();

    public List<String> getAntecedentParaphrase();
    public Atom getAtomByValue(String value);

    public List<Atom> getAtoms();
    public List<Atom> getConsequent();

    public String getConsequentParaphrase();
    public String getFormatedRuleID();
    public String getFormatedRuleLabel();

    public String getNameRule();
    public int getNumAntecedent();
    // TODO Verificar nomes dos metodos
    public int getNumAtoms();

    public int getNumConsequent();
    // TODO Verificar nomes dos metodos
    public int getNumVariables();
    public int getNumVariablesAntecedent();

    public int getNumVariablesConsequent();
    // TODO Verificar nomes dos metodos
    public int getNumVariablesDistinct();

    public int getNumVariablesDistinctAntecedent();

    public int getNumVariablesDistinctConsequent();
    public String getParaphrase();
    public boolean isEnabled();

    public boolean removeAtom(Atom atom);
    public void setAntecedentParaphrase(List<String> antecedentParaphrase);

    public void setConsequentParaphrase(String consequentParaphrase);

    public void setEnabled(boolean enabled);

    public void setNameRule(String nameRule);

    // TODO retirar metodo de set
    public void setParaphrase(String paraphrase);
}
