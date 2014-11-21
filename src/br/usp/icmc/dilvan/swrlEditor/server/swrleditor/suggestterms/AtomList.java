package br.usp.icmc.dilvan.swrlEditor.server.swrleditor.suggestterms;

import java.util.ArrayList;
import java.util.TreeSet;

import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.RuleSet;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Atom;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Rule;


public class AtomList {
	public enum TYPE {PREDICATE_FULL, PREDICATE_ANTECEDENT, PREDICATE_CONSEQUENT, ATOM_FULL, ATOM_ANTECEDENT, ATOM_CONSEQUENT, };
    private ArrayList<AtomItem> itens;
    
    public AtomList(RuleSet rules, boolean useID){
    	this(rules, TYPE.PREDICATE_FULL, useID);
    }
    
    public AtomList(RuleSet rules, TYPE mode, boolean useID){
    	itens = new ArrayList<AtomItem>();
    	DataAtomsItem creator = getCreator(mode, useID);
    	
    	for(Rule r : rules){
    		ArrayList<String> str = creator.get(r);

    		for(int i=0; i < str.size(); i++){
    			AtomItem main = getOrAddAtomItem(str.get(i));
    			for(int j=i+1; j < str.size(); j++){
    				main.addConection(getOrAddAtomItem(str.get(j)));
    			}
    		}
    	}
    }
    
    public int pruning(){
    	int limiar = 0;
    	
    	// Limiar
    	TreeSet<Integer> list = new TreeSet<Integer>();
    	for(AtomItem item: itens){
    		list.add(item.getNumDistinctConection());
    	}
    	
    	int ant = list.pollFirst();
    	int dist = 0;
    	while(!list.isEmpty()){
    		int atu = list.pollFirst();
    		if((atu - ant) > dist){
    			dist = atu-ant;
    			limiar = ant;
    		}
    		ant = atu;
    	}
    	
    	// Select the items that connection is more than limiar
    	ArrayList<AtomItem> removed = new ArrayList<AtomItem>();
    	for(AtomItem item:itens){
    		if( item.getNumDistinctConection() > limiar ){
    			removed.add(item);
    		}
    	}
    	
    	// Pruning item and sub-items
    	for(int i=itens.size()-1; i>=0; i--){
    		AtomItem item = itens.get(i);
    		if(removed.contains(item))
    			itens.remove(i);
			for(AtomItem subitem:removed){
				item.removeConection(subitem);
			}
    	}
    	
    	return limiar;
    }
    
    public ArrayList<AtomItem> getList(){
    	return itens;
    }

	private AtomItem getOrAddAtomItem(String nameAtom) {
		AtomItem ret = null;
		for(int i = 0; i < itens.size(); i++){
			AtomItem item = itens.get(i);
			int compareString = item.getName().compareTo(nameAtom);
			if(compareString==0){
				// Get the item
				ret = item;
				break;
			} else if(compareString > 0){
				// Add in the position i
				ret = new AtomItem(this, nameAtom);
				itens.add(i, ret);
				break;
			}
		}
		if(ret == null){
			// Add in the last position
			ret = new AtomItem(this, nameAtom);
			itens.add(ret);
		}
		return ret;
	}
	
	public AtomItem getAtomItem(String nameAtom){
		AtomItem ret = null;
		for(int i = 0; i < itens.size(); i++){
			if( itens.get(i).getName().equalsIgnoreCase(nameAtom) ){
				ret = itens.get(i);
				break;
			}
		}
		return ret;
	}

	public AtomItem getAtomItem(int i) {
		return itens.get(i);
	}
	
	public void showDetails(){
		System.out.println("List of predicates and conection numbers ("+itens.size()+")\n");
		for(AtomItem item: itens){
			System.out.println(item.getName()+";"+item.getNumDistinctConection()+";"+item.getNumConection());
			/*			
			Enumeration<AtomItem> e = item.getConections().keys();
			while(e.hasMoreElements()){
				AtomItem subitem = e.nextElement();
				System.out.println("     "+subitem.getName()+" "+item.getNumConection(subitem));
			}
			*/
		}
	}
	
	
	
	interface DataAtomsItem{
		public ArrayList<String> get(Rule r);
	}
	
	private DataAtomsItem getCreator(final AtomList.TYPE mode, final boolean useID){
		DataAtomsItem creator = null;

		switch(mode){
		case PREDICATE_FULL:
			creator = new DataAtomsItem(){
				public ArrayList<String> get(Rule r){
					ArrayList<String> str = new ArrayList<String>();
					for(Atom a : r.getAtoms()){
						if (useID){
							if(!str.contains(a.getPredicateID()))
								str.add(a.getPredicateID());
						}else{
							if(!str.contains(a.getPredicateLabel()))
								str.add(a.getPredicateLabel());
						}
						
					}
					return str;
				}
			};
			break;
		case PREDICATE_ANTECEDENT:
			creator = new DataAtomsItem(){
				public ArrayList<String> get(Rule r){
					ArrayList<String> str = new ArrayList<String>();
					for(Atom a : r.getAntecedent()){
						if (useID){
							if(!str.contains(a.getPredicateID()))
								str.add(a.getPredicateID());
						}else{
							if(!str.contains(a.getPredicateLabel()))
								str.add(a.getPredicateLabel());
						}
						
					}
					return str;
				}
			};
			break;
		case PREDICATE_CONSEQUENT:
			creator = new DataAtomsItem(){
				public ArrayList<String> get(Rule r){
					ArrayList<String> str = new ArrayList<String>();
					for(Atom a : r.getConsequent()){
						if (useID){
							if(!str.contains(a.getPredicateID()))
								str.add(a.getPredicateID());
						}else{
							if(!str.contains(a.getPredicateLabel()))
								str.add(a.getPredicateLabel());
						}
					}
					return str;
				}
			};
			break;
			case ATOM_FULL:
				creator = new DataAtomsItem(){
					public ArrayList<String> get(Rule r){
						ArrayList<String> str = new ArrayList<String>();
						for(Atom a : r.getAtoms()){
							if (useID){
								if(!str.contains(a.getAtomID()))
									str.add(a.getAtomID());
							}else{
								if(!str.contains(a.getAtomLabel()))
									str.add(a.getAtomLabel());
							}
						}
						return str;
					}
				};
				break;
			case ATOM_ANTECEDENT:
				creator = new DataAtomsItem(){
					public ArrayList<String> get(Rule r){
						ArrayList<String> str = new ArrayList<String>();
						for(Atom a : r.getAntecedent()){
							if (useID){
								if(!str.contains(a.getAtomID()))
									str.add(a.getAtomID());
							}else{
								if(!str.contains(a.getAtomLabel()))
									str.add(a.getAtomLabel());
							}
						}
						return str;
					}
				};
				break;
			case ATOM_CONSEQUENT:
				creator = new DataAtomsItem(){
					public ArrayList<String> get(Rule r){
						ArrayList<String> str = new ArrayList<String>();
						for(Atom a : r.getConsequent()){
							if (useID){
								if(!str.contains(a.getAtomID()))
									str.add(a.getAtomID());
							}else{
								if(!str.contains(a.getAtomLabel()))
									str.add(a.getAtomLabel());
							}
							
						}
						return str;
					}
				};
				break;
		}
		return creator;
	}
}
