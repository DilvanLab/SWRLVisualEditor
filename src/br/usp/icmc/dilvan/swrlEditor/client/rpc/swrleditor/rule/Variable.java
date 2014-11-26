package br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule;

import java.io.Serializable;
import java.util.List;

public interface Variable extends Serializable, Cloneable {
    public enum TYPE_VARIABLE {IVARIABLE, DVARIABLE, DATALITERAL, INDIVIDUALID, NULL};

    public Variable cloneOnlyID();
    @Override
    public boolean equals(Object variable);

    public String getComment();
    public String getFormatedID();

    public String getFormatedLabel();
    public String getSimpleID();

    public String getSimpleLabel();
    public TYPE_VARIABLE getTypeVariable();

    public void setComment(List<String> simpleComment);
    public void setSimpleID(String simpleID);

    public void setSimpleLabel(List<String> simpleLabel);

    public void setTypeVariable(TYPE_VARIABLE typeVariable);
}
