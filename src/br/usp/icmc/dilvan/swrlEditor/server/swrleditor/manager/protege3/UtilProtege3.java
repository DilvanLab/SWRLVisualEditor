package br.usp.icmc.dilvan.swrlEditor.server.swrleditor.manager.protege3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.gwt.core.shared.GWT;

import edu.stanford.smi.protegex.owl.model.impl.DefaultRDFSLiteral;

public class UtilProtege3 {

    public static List<String> parseCollectionArrayList(Collection<?> collection){

	List<String> result = new ArrayList<>();
	if (collection != null)
	    for (Object o :collection)
		if (o instanceof DefaultRDFSLiteral)
		    result.add(((DefaultRDFSLiteral)o).getBrowserText());
		else
		    GWT.log(o.getClass().toString());
	return result;
	//return (collection != null) ? new ArrayList<String>(collection) : ;
    }
}
