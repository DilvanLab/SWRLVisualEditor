package br.usp.icmc.dilvan.swrlEditor.server.swrleditor.suggestterms;

import java.util.Hashtable;
import java.util.Iterator;

public class AtomItem {
	private AtomList list;
	private String name;
    private Hashtable<AtomItem, Integer> conection;
    
    public AtomItem(AtomList list, String name){
    	conection = new Hashtable<AtomItem, Integer>();
    	this.list = list;
    	this.setName(name);
    }

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void addConection(AtomItem item) {
		AtomItem first, last;
		int position = name.compareTo(item.getName());
		if(position == 0){
			return ;
		}
		if(position < 0){
			first = this;
			last = item;
		} else {
			first = item;
			last = this;
		}
		
		first.getConections().put(last, first.getNumConection(last) + 1);
	}
	
	public int getNumConection(){
		int num = 0;
		for(int i = 0; i < list.getList().size(); i++){
			AtomItem item = list.getAtomItem(i);
			if(item.equals(this)){
				Iterator<Integer> iterator = conection.values().iterator();
				while(iterator.hasNext()){
					num += iterator.next();
				}
				break;
			}else {
				num += item.getNumConection(this);
			}
		}
		return num;
	}
	
	public int getNumDistinctConection() {
		int num = 0;
		
		for(int i = 0; i < list.getList().size(); i++){
			AtomItem item = list.getAtomItem(i);
			
			if(item.equals(this)){
				num += getConections().size();
				break;
			}else {
				if( item.hasValue(this) )
					num++;
			}
		}
		return num;
	}
	
	public boolean hasValue(AtomItem item) {
		return getNumConection(item) > 0;
	}

	public int getNumConection(AtomItem item){
		Integer i = conection.get(item);
		if(i==null)
			i = 0;
		return i;
	}

	public Hashtable<AtomItem, Integer> getConections() {
		return conection;
	}
	
	public AtomList getList(){
		return list;
	}

	public void removeConection(AtomItem subitem) {
		if( conection.containsKey(subitem) )
			conection.remove(subitem);
	}

	
}
