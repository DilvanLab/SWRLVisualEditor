package br.usp.icmc.dilvan.swrlEditor.server.swrleditor.manager.protege3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFList;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLAtom;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLBuiltinAtom;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLClassAtom;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLDataRangeAtom;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLDatavaluedPropertyAtom;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLDifferentIndividualsAtom;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLImp;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLIndividualPropertyAtom;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLSameIndividualAtom;

public class CreateSignature {
	String fsig = "";
	String sig = "";

	public String createSignature(OWLModel owlModel, SWRLImp rule) throws OntologyLoadException {

		RDFProperty bph = owlModel.getRDFProperty("http://swrl.stanford.edu/ontologies/3.3/swrla.owl#hasBuiltInPhrase");

		List head, body;
		HashMap<String, List> map;
		HashMap<String, List> cmap = new HashMap<String, List>();

		String[] order;
		String root;
		List<String> p, p2;

		String signature;


		p = new ArrayList();
		p2 = null;

		head = rule.getHead().getValues();
		body = rule.getBody().getValues();
		List<Pair> aliases = new ArrayList<Pair>();
		List<Pair> aliases2 = new ArrayList<Pair>();
		List<String> sequence = new ArrayList<String>();

		ExtendedMap xmap = ScanRules(body, aliases, p,
				sequence); // Scan the rules & Generate the hashmap
		map = xmap.map;

		p = xmap.p;
		collapse(aliases, map);
		transAliases(aliases);
		cmap.putAll(map); // Generate a new copy of the rules

		order = reorderVar(map); // Sort the Hashmap
		reorderAtoms(cmap); // Sort the Atom list for each Hashmap element

		fsig = "";

		while (!cmap.isEmpty()) {

			root = ChooseRoot(cmap, order, sequence);
			sig = "";
			DFSJoin(root, cmap, body, false, false, bph,
					aliases);

			fsig += sig + "^";
		}
		fsig = fsig.substring(0, fsig.length() - 1);

		fsig += "-";

		xmap = ScanRules(head, aliases2, p2, sequence); 

		map = xmap.map;
		collapse(aliases, map);
		cmap.putAll(map); // Generate a new copy of the rules

		order = reorderVar(map); // Sort the Hashmap
		reorderAtoms(cmap); // Sort the Atom list for each Hashmap element

		while (!cmap.isEmpty()) {
			if (!p.isEmpty()) {
				root = p.get(0);

				p.remove(0);

				if (cmap.containsKey(root)) {
					sig = "";
					DFSJoin(root, cmap, head, true, false, bph,
							aliases);
				}

			} else {
				root = ChooseRoot(cmap, order, sequence);

				sig = "";
				DFSJoin(root, cmap, head, false, false, bph,
						aliases);
			}
			fsig += sig + "^";
		}
		fsig = fsig.substring(0, fsig.length() - 1);

		signature = fsig;

		return signature;
	}

	private void transAliases(List<Pair> aliases) {
		String m;
		Pair p = null;
		for (int i = 0; i < aliases.size(); i++) {
			m = aliases.get(i).b;
			for (int j = i + 1; j < aliases.size(); j++) {
				if (m.equals(aliases.get(j).a)) {
					p = new Pair(aliases.get(i).a, aliases.get(j).b);
					aliases.set(j, p);
				}
			}

		}
	}

	private void DFSJoin(String root, HashMap<String, List> map, List bList,
			boolean OWLTH, boolean WR, RDFProperty bph,
			List aliases) {
		Argument arg;
		SWRLAtom atom;
		int Rtype;
		String argName2;
		List list = map.remove(root);

		sig += "(";
		if (((Argument) list.get(0)).ruleType == 1 && OWLTH) {
			sig += "@";
		}

		for (Object aList : list) {
			arg = (Argument) aList;
			atom = (SWRLAtom) bList.get(arg.value);

			Rtype = arg.ruleType;
			sig += Rtype;

			if (Rtype == 2) {
				argName2 = stringPruning(((((SWRLIndividualPropertyAtom) atom)
						.getArgument2()).getBrowserText()));
				if (map.containsKey(argName2)) {
					sig += "#";
					DFSJoin(argName2, map, bList, false, true,
							bph, aliases);
				}
			} else if (Rtype == 4) {
				argName2 = stringPruning(((((SWRLDatavaluedPropertyAtom) atom)
						.getArgument2()).getBrowserText()));
				if (map.containsKey(argName2)) {
					sig += "#";

					DFSJoin(argName2, map, bList, false, true,
							bph, aliases);
				}
			}
		}
		sig += ")";
	}

	private String ChooseRoot(HashMap<String, List> cmap,	String[] order, List sequence) {
		List list;
		Argument arg;

		if (cmap.size() == order.length) {
			for (int i = 0; i < order.length; i++) {
				list = cmap.get(order[i]);
				arg = (Argument) list.get(0);

				if (arg.ruleType == 1)
					return order[i];
			}

			return (String) sequence.get(0);
		} else {
			String key;
			Iterator it = cmap.keySet().iterator();
			while (it.hasNext()) {
				key = (String) it.next();
				list = cmap.get(key);

				if (((Argument) list.get(0)).ruleType == 1) {
					return key;

				}
			}

			it = cmap.keySet().iterator();
			return (String) it.next();
		}
	}

	private void collapse(List aliases, HashMap<String, List> map) {
		Pair p;
		List<Argument> l1, l2;
		for (Iterator ait = aliases.iterator(); ait.hasNext();) {
			p = (Pair) ait.next();
			if (map.containsKey(p.a) && map.containsKey(p.b)) {
				l1 = map.get(p.a);
				l2 = map.get(p.b);

				for (int i = 0; i < l2.size(); i++) {
					l1.add(l2.get(i));
				}

				map.put(p.a, l1);
				map.remove(p.b);
			}
		}
	}

	private void reorderAtoms(HashMap<String, List> cmap) {
		List list;
		String key;
		ArgComparator comparator = new ArgComparator();
		Iterator it = cmap.keySet().iterator();
		while (it.hasNext()) {
			key = (String) it.next();
			list = cmap.get(key);
			Collections.sort(list, comparator); // Sort the atom list
		}

	}

	// Prune the variable/predicate name for parsing
	private String stringPruning(String S) {
		if (S.startsWith("?"))
			S = S.substring(1);

		else if (S.startsWith("swrlb:"))
			S = S.substring(6);

		else if (S.startsWith("swrlx:"))
			S = S.substring(6);

		else if (S.startsWith("adi-2003:"))
			S = S.substring(9);

		else if (S.startsWith("temporal:"))
			S = S.substring(9);

		S = S.replace('_', ' ');

		return S;

	}

	// Insert an entry in the hashmap
	private HashMap insertMap(HashMap<String, List> map, String key, int value,
			int ruleType) {
		Argument newArg = new Argument(value, ruleType);

		if (map.containsKey(key)) {
			map.get(key).add(newArg);

		} else {
			List list = new ArrayList();
			list.add(newArg);
			map.put(key, list);
		}

		return map;
	}

	// Sort Variable in the Hashmap based on the number of repetition
	private String[] reorderVar(HashMap<String, List> map) {
		int n = map.size();
		int counter = 0;
		String[] result = new String[n];

		String key;
		List list;
		String maxKey = null;
		int max;

		Iterator it;

		while (!map.isEmpty()) // if Map is not empty
		{
			max = 0;
			it = map.keySet().iterator();
			while (it.hasNext()) {
				key = (String) it.next();
				list = map.get(key);

				if (list.size() > max) {
					max = list.size();
					maxKey = key;
				}
			}
			map.remove(maxKey);
			result[counter] = maxKey;
			counter++;

		}
		return result;
	}

	// Scan the rules and generate the HashMap
	private ExtendedMap ScanRules(List list, List aliases, List p, List<String> sequence) {
		SWRLAtom atom;
		RDFList argus;
		RDFSClass ruleType;
		String S, ruleTypeName;
		sequence.clear();

		HashMap<String, List> varMap = new HashMap<String, List>();
		int atomCounter = 0;
		String argName, argName2;

		for (Iterator It = list.iterator(); It.hasNext();) {
			atom = (SWRLAtom) It.next();
			S = atom.getBrowserText();
			ruleType = atom.getRDFType();
			ruleTypeName = ruleType.getBrowserText();

			if (ruleTypeName.equals("swrl:ClassAtom")) {
				argName = stringPruning(((((SWRLClassAtom) atom).getArgument1())
						.getBrowserText()));
				// System.out.print(argName);
				sequence.add(argName);
				varMap = insertMap(varMap, argName, atomCounter, 1);

			} else if (ruleTypeName.equals("swrl:IndividualPropertyAtom")) {
				argName = stringPruning((((SWRLIndividualPropertyAtom) atom)
						.getArgument1()).getBrowserText());
				// System.out.println(" insert " + argName);
				sequence.add(argName);
				varMap = insertMap(varMap, argName, atomCounter, 2);

			} else if (ruleTypeName.equals("swrl:DatavaluedPropertyAtom")) {
				argName = stringPruning(((((SWRLDatavaluedPropertyAtom) atom)
						.getArgument1()).getBrowserText()));
				// System.out.print(argName);
				sequence.add(argName);
				varMap = insertMap(varMap, argName, atomCounter, 4);

			} else if (ruleTypeName.equals("swrl:BuiltinAtom")) {
				argus = ((SWRLBuiltinAtom) atom).getArguments();

				Object o = argus.getFirst();
				if (o instanceof RDFResource)
					argName = stringPruning(((RDFResource) argus.getFirst())
							.getBrowserText());
				else
					argName = o + "";

				if ((((SWRLBuiltinAtom) atom).getBuiltin()) != null)
					S = stringPruning((((SWRLBuiltinAtom) atom).getBuiltin())
							.getBrowserText());
				else
					S = "UnknownBuiltin";

				if (S.equals("createOWLThing")) {
					p.add(argName);
					argus = argus.getRest();
					argName = stringPruning(((RDFResource) argus.getFirst())
							.getBrowserText());
					;
				} else {
					sequence.add(argName);
					varMap = insertMap(varMap, argName, atomCounter, 5);

				}

			} else if (ruleTypeName.equals("swrl:SameIndividualAtom")) {
				argName = stringPruning(((((SWRLSameIndividualAtom) atom)
						.getArgument1()).getBrowserText()));
				argName2 = stringPruning(((((SWRLSameIndividualAtom) atom)
						.getArgument2()).getBrowserText()));
				aliases.add(new Pair(argName, argName2));


			} else if (ruleTypeName.equals("swrl:DifferentIndividualsAtom")) {
				argName = stringPruning(((((SWRLDifferentIndividualsAtom) atom)
						.getArgument1()).getBrowserText()));


			} else if (ruleTypeName.equals("swrl:DataRangeAtom")) {
				argName = stringPruning(((((SWRLDataRangeAtom) atom)
						.getArgument1()).getBrowserText()));

				sequence.add(argName);
				varMap = insertMap(varMap, argName, atomCounter, 6);

			} else {
				System.out.print("Error in atom type");
			}
			atomCounter++;
		}
		return (new ExtendedMap(varMap, p));
	}

	private class Argument {

		public int ruleType;
		public int value;

		public Argument(int value, int ruleType)
		{
			this.value = value;
			this.ruleType = ruleType;
		}

	}

	private class ExtendedMap {
		public List <String> p;
		public HashMap<String, List> map;

		public ExtendedMap(HashMap<String, List> map, List p) 
		{
			this.p = p;
			this.map = map;
		}


	}

	private class Pair {
		public String a;
		public String b;

		public Pair(String a, String b)
		{
			this.a = a;
			this.b = b;
		}

	}

	private class ArgComparator implements Comparator{ //ArgComparator
		public int compare(Object o1, Object o2){

			int r1 = ( (Argument) o1).ruleType;
			int r2 = ( (Argument) o2).ruleType;

			if( r1 > r2 )
				return 1;
			else if( r1 < r2 )
				return -1;
			else
				return 0;
		}
	}
}
