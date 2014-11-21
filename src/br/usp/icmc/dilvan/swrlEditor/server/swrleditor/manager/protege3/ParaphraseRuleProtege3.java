package br.usp.icmc.dilvan.swrlEditor.server.swrleditor.manager.protege3;
import java.util.*;

import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.swrl.model.*;

public class ParaphraseRuleProtege3 {
	
	final String lineBreak = "\n";

	final String startTab = "\t";
	final String endTab = "";

	String sig = "";

	@SuppressWarnings("rawtypes")
	List cros;
	@SuppressWarnings("rawtypes")
	List oth;

	private static RDFProperty bph;
	
	public ParaphraseRuleProtege3(OWLModel owlModel) {
		super();

		bph = owlModel.getRDFProperty("http://swrl.stanford.edu/ontologies/3.3/swrla.owl#hasBuiltInPhrase");
	}
	
	private static String getFormated(SWRLFactory factory, String paraphrase, String beforeFormat, String afterFormat) {

		List<String> keyWords = new ArrayList<String>();
		keyWords.add("IF");
		keyWords.add("THEN");
		keyWords.add("AND");
		keyWords.add("SUCH THAT");
		keyWords.add("WHERE");
		keyWords.add("FOR EACH");
		keyWords.add("THERE IS");
		keyWords.add("HAS");
		keyWords.add("VALUE");
		keyWords.add("FOR");
		keyWords.add("IS THE SAME AS");
		keyWords.add("IS DIFFERENT FROM"); 
		keyWords.add("RNAGE");
		keyWords.add("IS AN ");
		keyWords.add("IS A ");
		keyWords.add(" A ");
		keyWords.add(" AN ");
		
		for (SWRLBuiltin builtin: factory.getBuiltins())
			if (builtin.getPropertyValue(bph) != null)
				keyWords.add((String) builtin.getPropertyValue(bph));
		
		String copy;
		for (String key :keyWords){
			copy = paraphrase;
			paraphrase = "";
			while (copy.indexOf(key) >= 0){
				int pos = copy.indexOf(key);
				int tam = key.length();
				
				paraphrase = paraphrase + copy.substring(0, pos)+beforeFormat+copy.substring(pos, pos+tam)+afterFormat;
				copy = copy.substring(pos+tam, copy.length());
				
			}
			paraphrase = paraphrase + copy;
		}
		
		return paraphrase;
	}
	

	public static String getFormatedViewParaphrase(SWRLFactory factory, String paraphrase) {
		return "<pre>"+getFormated(factory, paraphrase, "<b>", "</b>")+"</pre>";
	}

	public static List <String> getFormatedParaphraseAntecendent(SWRLFactory factory, String paraphrase) {
		
		//String BOLD = "bold;";

		
		
	//	String before = "<b>";//"<span style=\""+ BOLD +"\">";
		//String after = "</b>";//"</span>";
		
		List <String> antecedentParaphrase = new ArrayList<String>();
		
		String tempParaphrase = paraphrase;
		String tempAnte;
		
		tempParaphrase = tempParaphrase.substring(tempParaphrase.indexOf("IF")+2, tempParaphrase.indexOf("THEN")-1).trim();


		while(!tempParaphrase.equals("")){
			
			if (tempParaphrase.indexOf((char)10) > 0)
				tempAnte = tempParaphrase.substring(0, tempParaphrase.indexOf((char)10)).trim();
			else{
				tempAnte = tempParaphrase.trim();
			}
				
			if (tempAnte.trim().startsWith("AND"))
				tempAnte = tempAnte.substring(3).trim();
			
			if (tempAnte.trim().startsWith("IF"))
				tempAnte = tempAnte.substring(2).trim();
			
			if (!tempAnte.equals(""))
				antecedentParaphrase.add(tempAnte);
			
			tempParaphrase = tempParaphrase.trim();
			if (!tempParaphrase.equals(""))
				if (tempParaphrase.indexOf((char)10) > 0){
					tempParaphrase = tempParaphrase.substring(tempParaphrase.indexOf((char)10)).trim();
				}else
					tempParaphrase = "";
			
			tempParaphrase = tempParaphrase.trim();
		}
		
		return antecedentParaphrase;
	}

	public static String getFormatedParaphraseConsequent(SWRLFactory factory, String paraphrase) {
		return "<pre>"+getFormated(factory, paraphrase.substring(paraphrase.indexOf("THEN")+4).trim(), "<b>", "</b>")+"</pre>";
		
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String createParaphrase(SWRLImp element) {
		
		String Result = "";

		List head, body;
		HashMap<String, List> map;
		HashMap<String, List> cmap = new HashMap<String, List>();
		HashMap<String, List> ccmap = new HashMap<String, List>();

		String[] order;
		String root;
		List<String> CREATEOWLTHING, p, CREATEOWLTHING2, p2;

		cros = new ArrayList();
		oth = new ArrayList();

		CREATEOWLTHING = new ArrayList();
		p = new ArrayList();
		CREATEOWLTHING2 = null;
		p2 = null;


		head = element.getHead().getValues();
		body = element.getBody().getValues();
		List<Pair> aliases = new ArrayList<Pair>();
		List<Pair> aliases2 = new ArrayList<Pair>();
		List<String> sequence = new ArrayList<String>();

		ExtendedMap xmap = ScanRules(body, aliases, CREATEOWLTHING, p,
				sequence); // Scan the rules & Generate the hashmap
		map = xmap.map;

		CREATEOWLTHING = xmap.CREATEOWLTHING;
		p = xmap.p;
		collapse(aliases, map);
		transAliases(aliases);
		cmap.putAll(map); // Generate a new copy of the rules
		ccmap.putAll(map);

		order = reorderVar(map); // Sort the Hashmap
		reorderAtoms(cmap); // Sort the Atom list for each Hashmap element
		reorderAtoms(ccmap);
		boolean first = true;

		while (!cmap.isEmpty()) {
			if (first)
				first = false;
			else {
				Result += "AND ";
			}


			Result += "IF"+lineBreak;
			root = ChooseRoot(cmap, body, order, sequence);
			sig = "";
			cros.clear();
			
			Result += DFSJoin(root, cmap, body, startTab, false, false, aliases);

			Result += endTab;


			Result += lineBreak;

		}

		xmap = ScanRules(head, aliases2, CREATEOWLTHING2, p2, sequence); // Scan
		// the
		// rules
		// &
		// Generate
		// the
		// hashmap
		map = xmap.map;
		collapse(aliases, map);
		cmap.putAll(map); // Generate a new copy of the rules


		order = reorderVar(map); // Sort the Hashmap
		reorderAtoms(cmap); // Sort the Atom list for each Hashmap element


		Result += "THEN"+lineBreak;


		while (!cmap.isEmpty()) {
			if (!p.isEmpty()) {
				root = p.get(0);

				Result += startTab + CREATEOWLTHING.get(0);
				p.remove(0);
				CREATEOWLTHING.remove(0);

				if (cmap.containsKey(root)) {
					sig = "";
					cros.clear();
					Result += DFSJoin(root, cmap, head, startTab, true, false, aliases);
					
					Result += endTab;
				}
				
				Result += endTab;

			} else {
				root = ChooseRoot(cmap, head, order, sequence);

				sig = "";
				cros.clear();
				Result += DFSJoin(root, cmap, head, startTab, false, false, aliases);
				Result += endTab;
			}


			Result += lineBreak;

		}

		return Result;

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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private String DFSJoin(String root, HashMap<String, List> map, List bList,
			String prefix, boolean OWLTH, boolean WR, List aliases) {
		Argument arg;
		SWRLAtom atom;
		int Rtype;
		String argName2, prefix2;
		List list = map.remove(root);
		boolean first = true;
		String Result2 = new String();

		sig += "(";
		// Special case for the first rule
		if (((Argument) list.get(0)).ruleType == 1 && OWLTH) {
			if (startsWithVowel(root)) {

				Result2 += " AN \"" + root + "\" SUCH THAT"+lineBreak;
			} else {

				Result2 += " A \"" + root + "\" SUCH THAT"+lineBreak;
			}

			prefix += "\t"; // The second line starts after a tab
			sig += "@";

		}

		// Start checking Atoms
		for (Iterator it = list.iterator(); it.hasNext();) {
			arg = (Argument) it.next();
			atom = (SWRLAtom) bList.get(arg.value);

			if (first) {
				if (!WR) {

					Result2 += prefix;
				}

				first = false;

			} else {

				Result2 += prefix + "AND ";
			}

			Result2 += parseAnAtom(atom, aliases);

			Rtype = arg.ruleType;
			sig += Rtype;
			cros.add(atom);
			prefix2 = prefix;

			if (Rtype == 2) {
				argName2 = stringPruning(((((SWRLIndividualPropertyAtom) atom)
						.getArgument2()).getBrowserText()));
				if (map.containsKey(argName2)) {
					if (((Argument) (map.get(argName2).get(0))).ruleType != 5) {

						Result2 += lineBreak;
						prefix2 += "\t";

						Result2 += prefix2;
					}

					Result2 += " WHERE ";
					sig += "#";

					Result2 += DFSJoin(argName2, map, bList, prefix2, false, true, aliases);
				} else {
					Result2 += lineBreak;
				}

			} else if (Rtype == 4) {
				argName2 = stringPruning(((((SWRLDatavaluedPropertyAtom) atom)
						.getArgument2()).getBrowserText()));
				if (map.containsKey(argName2)) {
					if (((Argument) (map.get(argName2).get(0))).ruleType != 5) {
						Result2 += lineBreak;
						prefix2 += "\t";

						Result2 += prefix2;
					}

					Result2 += " WHERE ";
					sig += "#";
					Result2 += DFSJoin(argName2, map, bList, prefix2, false, true, aliases);
				} else {
					Result2 += lineBreak;
				}
			} else {
				Result2 += lineBreak;
			}

		}
		sig += ")";



		return Result2;
	}

	@SuppressWarnings("rawtypes")
	private String ChooseRoot(HashMap<String, List> cmap, List atomList,
			String[] order, List sequence) {
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
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
	public static String stringPruningStatic(String S) {
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

	// Prune the variable/predicate name for parsing
	public String stringPruning(String S) {
		S =  stringPruningStatic(S);
		
		return S;
	}

	// Insert an entry in the hashmap
	@SuppressWarnings({ "rawtypes", "unchecked" })
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

	private boolean startsWithVowel(String S) {
		S = S.toLowerCase();
		if (S.startsWith("a")
				|| S.startsWith("e")
				|| S.startsWith("i")
				|| S.startsWith("o")
				|| (S.startsWith("u") && !(S.startsWith("univers") || S
						.startsWith("unit"))) || S.equals("f") || S.equals("h")
						|| S.equals("l") || S.equals("m") || S.equals("n")
						|| S.equals("r") || S.equals("s") || S.equals("x")
						|| S.startsWith("hour") || S.startsWith("honor"))
			return true;
		else
			return false;
	}

	// Sort Variable in the Hashmap based on the number of repetition
	@SuppressWarnings("rawtypes")
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
			// System.out.println(maxKey);

		}
		return result;
	}

	// Scan the rules and generate the HashMap
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ExtendedMap ScanRules(List list, List aliases, List CREATEOWLTHING,
			List p, List<String> sequence) {
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

				if ((((SWRLBuiltinAtom) atom).getBuiltin()) != null){
					S = stringPruning((((SWRLBuiltinAtom) atom).getBuiltin())
							.getBrowserText());
				}else
					S = "UnknownBuiltin";

				if (S.equals("createOWLThing")) {
					oth.add(atom);
					p.add(argName);
					argus = argus.getRest();
					argName = stringPruning(((RDFResource) argus.getFirst()).getBrowserText());
					
					CREATEOWLTHING.add("FOR EACH \"" + argName + "\" THERE IS");
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
				// System.out.print(argName);
				// varMap = insertMap(varMap, argName, atomCounter, 3);

			} else if (ruleTypeName.equals("swrl:DifferentIndividualsAtom")) {
				argName = stringPruning(((((SWRLDifferentIndividualsAtom) atom)
						.getArgument1()).getBrowserText()));
				// System.out.print(argName);
				// varMap = insertMap(varMap, argName, atomCounter, 3);

			} else if (ruleTypeName.equals("swrl:DataRangeAtom")) {
				argName = stringPruning(((((SWRLDataRangeAtom) atom)
						.getArgument1()).getBrowserText()));
				// System.out.print(argName);
				sequence.add(argName);
				varMap = insertMap(varMap, argName, atomCounter, 6);

			} else {
				System.out.print("Error in atom type");
			}
			atomCounter++;
		}
		return (new ExtendedMap(varMap, CREATEOWLTHING, p));
	}

	@SuppressWarnings("rawtypes")
	private String uniqueArg(String arg2, List aliases) {
		Pair t;
		for (Iterator It = aliases.iterator(); It.hasNext();) {
			t = (Pair) It.next();
			if (arg2.equals(t.b))
				return t.a;
		}
		return arg2;
	}

	// Parse an Atom
	@SuppressWarnings("rawtypes")
	private String parseAnAtom(SWRLAtom atom, List aliases) {

		RDFList argus;
		RDFSClass ruleType;
		String S, ruleTypeName, argName, predicate;
		String Result1 = new String();

		S = atom.getBrowserText();
		ruleType = atom.getRDFType();
		ruleTypeName = ruleType.getBrowserText();
		// System.out.println("Name = " + S + "Type = " + ruleTypeName);
		boolean second = true;

		if (ruleTypeName.equals("swrl:ClassAtom")) {
			argName = stringPruning(((((SWRLClassAtom) atom).getArgument1())
					.getBrowserText()));
			argName = uniqueArg(argName, aliases);
			// System.out.print("\"" + argName + "\" ");
			Result1 += "\"" + argName + "\" ";
			// printMap(varMap);
			S = stringPruning((((SWRLClassAtom) atom).getClassPredicate())
					.getBrowserText());

			if (startsWithVowel(S)) {
				// System.out.print("IS AN " + S);
				Result1 += "IS AN " + S;
			} else {
				// System.out.print("IS A " + S);
				Result1 += "IS A " + S;
			}

		} else if (ruleTypeName.equals("swrl:IndividualPropertyAtom")) {
			argName = stringPruning((((SWRLIndividualPropertyAtom) atom)
					.getArgument1()).getBrowserText());
			argName = uniqueArg(argName, aliases);
			// System.out.print("\"" + argName + "\" ");
			Result1 += "\"" + argName + "\" ";

			// predicate = (((SWRLIndividualPropertyAtom)
			// atom).getPropertyPredicate()).getBrowserText();
			predicate = stringPruning((((SWRLIndividualPropertyAtom) atom)
					.getPropertyPredicate()).getBrowserText());
			// predicate = S;

			if (predicate.toLowerCase().startsWith("has ")) {
				// System.out.print("HAS " + predicate.substring(4) + " ");
				Result1 += "HAS " + predicate.substring(4) + " ";

			} else if (predicate.toLowerCase().startsWith("has")) {
				// System.out.print("HAS " + predicate.substring(3) + " ");
				Result1 += "HAS " + predicate.substring(3) + " ";
			} else {
				// System.out.print(predicate + " ");
				Result1 += predicate + " ";
			}

			argName = stringPruning(((((SWRLIndividualPropertyAtom) atom)
					.getArgument2()).getBrowserText()));
			argName = uniqueArg(argName, aliases);
			// System.out.print("\"" + argName + "\"");
			Result1 += "\"" + argName + "\"";

		} else if (ruleTypeName.equals("swrl:DatavaluedPropertyAtom")) {
			argName = stringPruning(((((SWRLDatavaluedPropertyAtom) atom)
					.getArgument1()).getBrowserText()));
			argName = uniqueArg(argName, aliases);
			// System.out.print("\"" + argName + "\" ");
			Result1 += "\"" + argName + "\" ";

			predicate = stringPruning((((SWRLDatavaluedPropertyAtom) atom)
					.getPropertyPredicate()).getBrowserText());

			argName = stringPruning((((SWRLDatavaluedPropertyAtom) atom)
					.getArgument2()).getBrowserText());
			argName = uniqueArg(argName, aliases);

			if (predicate.toLowerCase().startsWith("has_")) {
				// System.out.print("HAS ");
				// System.out.print(predicate.substring(4));
				// System.out.print(" \""+ argName +"\"");

				Result1 += "HAS " + predicate.substring(4) + " \"" + argName
				+ "\"";

			} else if (predicate.toLowerCase().startsWith("has")) {
				// System.out.print("HAS ");
				// System.out.print(predicate.substring(3));
				// System.out.print(" \""+ argName +"\"");
				Result1 += "HAS " + predicate.substring(3) + " \"" + argName
				+ "\"";

			} else {
				// System.out.print("HAS VALUE ");
				// System.out.print("\""+ argName +"\" FOR ");
				// System.out.print(predicate);
				Result1 += "HAS VALUE " + "\"" + argName + "\" FOR "
				+ predicate;

			}

		} else if (ruleTypeName.equals("swrl:BuiltinAtom")) {
			argus = ((SWRLBuiltinAtom) atom).getArguments();

			Object o = argus.getFirst();
			if (o instanceof RDFResource)
				argName = stringPruning(((RDFResource) argus.getFirst())
						.getBrowserText());
			else 
				argName = o + "";



			argName = uniqueArg(argName, aliases);
			// System.out.print("\"" + argName + "\" ");
			Result1 += "\"" + argName + "\" ";
			String mytmp = null;
			argus = argus.getRest();

			if ((((SWRLBuiltinAtom) atom).getBuiltin()) != null) {
				S = stringPruning((((SWRLBuiltinAtom) atom).getBuiltin())
						.getBrowserText());
				mytmp = (String) (((SWRLBuiltinAtom) atom).getBuiltin()).getPropertyValue(bph);
			} else
				S = "UnknownBuiltin";

			if (mytmp != null) {
				// System.out.print(mytmp + " ");
				Result1 += mytmp + " ";
			} else {
				// System.out.print(S + " ");
				Result1 += S + " ";
			}

			while (argus.isClosed()) {
				Object j = argus.getFirst();

				if (second)
					second = false;
				else {
					if (argus.size() > 1) {
						// System.out.print(", ");
						Result1 += ", ";
					} else {
						// System.out.print(" AND ");
						Result1 += " AND ";
					}
				}

				if (j instanceof RDFResource) {
					argName = stringPruning(((RDFResource) argus.getFirst())
							.getBrowserText());
					argName = uniqueArg(argName, aliases);
					// System.out.print("1 \"" + argName + "\"");
					Result1 += "\"" + argName + "\"";

				} else if (j instanceof String) {
					// System.out.print("2 \"" + j + "\"");
					Result1 += "\"" + j + "\"";

				} else {
					// System.out.print("3 "+j);
					Result1 += j;
				}

				argus = argus.getRest();
			}

		} else if (ruleTypeName.equals("swrl:SameIndividualAtom")) {
			argName = stringPruning(((((SWRLSameIndividualAtom) atom)
					.getArgument1()).getBrowserText()));
			// System.out.print("\"" + argName + "\" ");
			Result1 += "\"" + argName + "\" ";

			// System.out.print("IS THE SAME AS ");
			Result1 += "IS THE SAME AS ";

			argName = stringPruning(((((SWRLSameIndividualAtom) atom)
					.getArgument2()).getBrowserText()));
			// System.out.print("\""+ argName + "\"");
			Result1 += "\"" + argName + "\"";

		} else if (ruleTypeName.equals("swrl:DifferentIndividualsAtom")) {
			argName = stringPruning(((((SWRLDifferentIndividualsAtom) atom)
					.getArgument1()).getBrowserText()));
			// System.out.print("\"" + argName + "\" ");
			Result1 += "\"" + argName + "\" ";

			// System.out.print("IS DIFFERENT FROM ");
			Result1 += "IS DIFFERENT FROM ";

			argName = stringPruning(((((SWRLDifferentIndividualsAtom) atom)
					.getArgument2()).getBrowserText()));
			// System.out.print("\"" + argName + "\"");
			Result1 += "\"" + argName + "\"";

		} else if (ruleTypeName.equals("swrl:DataRangeAtom")) {
			argName = stringPruning(((((SWRLDataRangeAtom) atom).getArgument1())
					.getBrowserText()));
			// System.out.print("\"" + argName + "\" ");
			Result1 += "\"" + argName + "\" ";

			// System.out.print("HAS RNAGE ");
			Result1 += "HAS RNAGE ";

			// System.out.print("\"" + stringPruning(((((SWRLDataRangeAtom)
			// atom).getDataRange()).getBrowserText())) + "\"");
			Result1 += "\""
				+ stringPruning(((((SWRLDataRangeAtom) atom).getDataRange())
						.getBrowserText())) + "\"";

		} else {
			// System.out.print(S);
			Result1 += S;
			// System.out.print("["+ruleTypeName+"]");
			Result1 += "[" + ruleTypeName + "]";
		}

		return Result1;

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

	private class Pair {
		public String a;
		public String b;

		public Pair(String a, String b)
		{
			this.a = a;
			this.b = b;
		}

	}

	@SuppressWarnings("rawtypes")
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

	@SuppressWarnings("rawtypes")
	private class ExtendedMap {
		public List <String> CREATEOWLTHING;
		public List <String> p;
		public HashMap<String, List> map;

		@SuppressWarnings("unchecked")
		public ExtendedMap(HashMap<String, List> map, List CREATEOWLTHING, List p) 
		{
			this.CREATEOWLTHING = CREATEOWLTHING;
			this.p = p;
			this.map = map;
		}


	}

}
