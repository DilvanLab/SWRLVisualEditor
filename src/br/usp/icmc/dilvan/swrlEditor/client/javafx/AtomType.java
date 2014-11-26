package br.usp.icmc.dilvan.swrlEditor.client.javafx;

import java.io.Serializable;

import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Atom;

@SuppressWarnings("serial")
public class AtomType implements Serializable {
    Atom.TYPE_ATOM atom;

    public AtomType(Atom.TYPE_ATOM atom) {this.atom = atom;}

    public Atom.TYPE_ATOM get() {return atom;}
}
