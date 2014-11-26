package br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule;

import java.io.Serializable;
import java.util.List;

public interface Atom extends Serializable, Cloneable {
    public enum TYPE_ATOM {CLASS, INDIVIDUAL_PROPERTY, SAME_DIFERENT, DATAVALUE_PROPERTY, BUILTIN, DATARANGE, NULL};

    public void addVariable(Variable v);
    public Atom cloneOnlyID();

    @Override
    public boolean equals(Object atom);
    public String getAtomID();

    public String getAtomLabel();
    public TYPE_ATOM getAtomType();

    public int getCountVariables();
    public String getPredicateComment();

    public String getPredicateID();
    public String getPredicateLabel();

    public List<Variable> getVariables();
    public void setAtomType(TYPE_ATOM atomType);
    public void setPredicateComment(List<String> predicateComment);

    public void setPredicateID(String predicateID);

    public void setPredicateLabel(List<String> predicateLabel);
}
