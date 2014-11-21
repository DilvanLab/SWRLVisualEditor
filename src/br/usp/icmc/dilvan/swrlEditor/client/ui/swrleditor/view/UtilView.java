package br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.usp.icmc.dilvan.swrlEditor.client.resources.Resources;
import br.usp.icmc.dilvan.swrlEditor.client.resources.UtilResource;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Atom;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Variable;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.VisualizationView.TYPE_VIEW;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;


public class UtilView {

	public interface BuiltinMap {
		public String getValue(List<Variable> params, TYPE_VIEW typeView);
	}

	public static String getAtomHightlights(Atom atom, boolean useAnd, TYPE_VIEW typeView){
		//TODO transform the HTML in distinct components
		String params = "";
		for(int i = 0; i < atom.getCountVariables(); i++){
			Variable pr = atom.getVariables().get(i);
			if(!params.isEmpty())
				params += ", ";
			
			
			if (typeView == TYPE_VIEW.ID)
				params += "<span class=\""+UtilResource.getCssTypeVariableView(pr.getTypeVariable())+"\">"+pr.getFormatedID()+"</span>";
			else
				params += "<span class=\""+UtilResource.getCssTypeVariableView(pr.getTypeVariable())+"\">"+pr.getFormatedLabel()+"</span>";
		}
		String endAtom;
		if(useAnd)
			endAtom = " ^ </div>";
		else
			endAtom = "</div>";

		if (typeView == TYPE_VIEW.ID)
			return "<div class=\""+Resources.INSTANCE.swrleditor().swrlRule()+"\"><span class=\""+ UtilResource.getCssTypeAtom(atom.getAtomType()) +"\">"+ atom.getPredicateID() +"</span>("+params+")"+endAtom;
		else
			return "<div class=\""+Resources.INSTANCE.swrleditor().swrlRule()+"\"><span class=\""+ UtilResource.getCssTypeAtom(atom.getAtomType()) +"\">"+ atom.getPredicateLabel() +"</span>("+params+")"+endAtom;
	}

	public static String getAtomsHightlights(List<Atom> atoms, TYPE_VIEW typeView){
		String strAtom = "";
		for(Atom at : atoms){
			String params = "";
			for(int i = 0; i < at.getCountVariables(); i++){
				Variable pr = at.getVariables().get(i);
				if(!params.isEmpty())
					params += ", ";
				if (typeView == TYPE_VIEW.ID)
					params += "<span class=\""+UtilResource.getCssTypeVariableView(pr.getTypeVariable())+"\">"+pr.getFormatedID()+"</span>";
				else
					params += "<span class=\""+UtilResource.getCssTypeVariableView(pr.getTypeVariable())+"\">"+pr.getFormatedLabel()+"</span>";
			}
			if(!strAtom.isEmpty())
				strAtom += " ^ </div>";

			
			if (typeView == TYPE_VIEW.ID)
				strAtom += "<div class=\""+Resources.INSTANCE.swrleditor().swrlRule()+"\"><span class=\""+ UtilResource.getCssTypeAtom(at.getAtomType()) +"\">"+ at.getPredicateID() +"</span>("+params+")";
			else
				strAtom += "<div class=\""+Resources.INSTANCE.swrleditor().swrlRule()+"\"><span class=\""+ UtilResource.getCssTypeAtom(at.getAtomType()) +"\">"+ at.getPredicateLabel() +"</span>("+params+")";
		}
		strAtom += "</div>";
		return strAtom;
	}

	public static String formatFilter(List<String> lstAnd, List<String> lstOr, List<String> lstNot) {
		
		String filter = "";
		
		for (String s: lstAnd){
			if (filter.isEmpty()){
				filter =  formatStringFilter(s);
			}else{
				filter = filter + formatOperatorFilter(" AND ") + formatStringFilter(s);
			}
		}

		boolean first = true;
		for (String s: lstOr){
			if (filter.isEmpty()){
				filter =  " ("+ formatStringFilter(s);
				first = false;
			}else if (first){
				filter = filter + formatOperatorFilter(" AND ")+"("+ formatStringFilter(s);
				first = false;
			}else{
				filter = filter + formatOperatorFilter(" OR ") + formatStringFilter(s);
			}
		}
		if (!first)
			filter = filter + ") ";
		

		for (String s: lstNot){
			if (filter.isEmpty()){
				filter = filter + formatOperatorFilter(" NOT ")+"("+formatStringFilter(s)+")";
			}else{
				filter = filter + formatOperatorFilter(" AND NOT ")+"("+formatStringFilter(s)+")";
			}
		}
		return filter;
	}

	private static String formatOperatorFilter(String s){
		return "<span class='"+Resources.INSTANCE.swrleditor().operatorFilter()+"'>"+s+"</span>";
	}

	
	private static String formatStringFilter(String s){
		return "<span class='"+Resources.INSTANCE.swrleditor().det()+"'>\""+s+"\"</span>";
	}


	// rulePart parameter is add with class in the atoms, because when the user clicked and click handler use this
	public static FlexTable getAtomsVisualizationPanel(List<Atom> listAtoms, ClickHandler handler,  String rulePart, TYPE_VIEW typeView) {
		FlexTable table = new FlexTable();

		int row = 0;
		List<Atom> used = new ArrayList<Atom>();
		for(Atom a:listAtoms){
			if(a.getAtomType() == Atom.TYPE_ATOM.CLASS){

				String paramName;
				if (a.getCountVariables() == 0)
					paramName = "";
				else if (typeView == TYPE_VIEW.ID)
					paramName = a.getVariables().get(0).getFormatedID();
				else
					paramName = a.getVariables().get(0).getFormatedLabel();

				used.add(a);

				FlowPanel linhas = new FlowPanel();
				for(int i = 0; i < listAtoms.size(); i++ ){
					Atom lastMain = listAtoms.get(i);
					if(used.contains(lastMain) || lastMain.equals(a)) 
						continue;
					if(lastMain.getAtomType() == Atom.TYPE_ATOM.CLASS || lastMain.getAtomType() == Atom.TYPE_ATOM.BUILTIN || lastMain.getAtomType() == Atom.TYPE_ATOM.SAME_DIFERENT || lastMain.getAtomType() == Atom.TYPE_ATOM.DATARANGE) 
						continue;
					if (typeView == TYPE_VIEW.ID){
						if(lastMain.getVariables().get(0).getFormatedID().equals(paramName)){
							used.add(lastMain);
							Variable param = lastMain.getVariables().get(1);
							String img = "";
							
							
							
							
							if(lastMain.getAtomType() == Atom.TYPE_ATOM.INDIVIDUAL_PROPERTY)
								img = "<img src=\""+Resources.INSTANCE.oWLObjectProperty().getURL()+"\" /> ";
							else
								img = "<img src=\""+Resources.INSTANCE.oWLDatatypeProperty().getURL()+"\" /> ";

							
							HTML obj = new HTML(img+lastMain.getPredicateID()+" "+getHTMLParameter(param, typeView));
							//HTML obj = new HTML(img+lastMain.getPredicateID()+" "+getHTMLParameter(param, typeView));
							obj.setTitle(lastMain.getAtomID());
							obj.addStyleName(UtilResource.getCssRulePart(rulePart));
							if(handler!=null){
								obj.addStyleName(Resources.INSTANCE.swrleditor().vtlink());
								obj.addClickHandler(handler);
							}
							linhas.add(obj); 
						}
					}else{
						if(lastMain.getVariables().get(0).getFormatedLabel().equals(paramName)){
							used.add(lastMain);
							Variable param = lastMain.getVariables().get(1);
							String img = "";
							if(lastMain.getAtomType() == Atom.TYPE_ATOM.INDIVIDUAL_PROPERTY)
								img = "<img src=\""+Resources.INSTANCE.oWLObjectProperty().getURL()+"\" /> ";
							else
								img = "<img src=\""+Resources.INSTANCE.oWLDatatypeProperty().getURL()+"\" /> ";

							HTML obj = new HTML(img+lastMain.getPredicateLabel()+" "+getHTMLParameter(param, typeView));
							obj.setTitle(lastMain.getAtomLabel());
							obj.addStyleName(UtilResource.getCssRulePart(rulePart));
							if(handler!=null){
								obj.addStyleName(Resources.INSTANCE.swrleditor().vtlink());
								obj.addClickHandler(handler);
							}
							linhas.add(obj); 
						}
					}
				}
				HTML obj;
				if (typeView == TYPE_VIEW.ID){
					obj = new HTML("<img src=\""+Resources.INSTANCE.class_().getURL()+"\" /><br /><b>"+paramName+"</b><br />"+a.getPredicateID());
					obj.setTitle(a.getAtomID());
				}else{
					obj = new HTML("<img src=\""+Resources.INSTANCE.class_().getURL()+"\" /><br /><b>"+paramName+"</b><br />"+a.getPredicateLabel());
					obj.setTitle(a.getAtomLabel());
				}

				obj.addStyleName(UtilResource.getCssRulePart(rulePart));
				if(handler!=null){
					obj.addStyleName(Resources.INSTANCE.swrleditor().vtlink());
					obj.addClickHandler(handler);
				}

				table.setWidget(row, 0, obj);
				table.getFlexCellFormatter().setStyleName(row, 0, Resources.INSTANCE.swrleditor().q_class());
				table.setWidget(row, 1, linhas);
				table.getFlexCellFormatter().setStyleName(row, 1, Resources.INSTANCE.swrleditor().q_subnivel());
				row++;
			} 
		}
		// Others Atoms without a class atom related
		for(Atom a:listAtoms){
			if(used.contains(a)) 
				continue;
			if(a.getAtomType() == Atom.TYPE_ATOM.BUILTIN){
				used.add(a);

				HTML obj = new HTML("<img src=\""+Resources.INSTANCE.builtin().getURL()+"\" /> "+getFormatedBuiltin(a, typeView));

				if (typeView == TYPE_VIEW.ID)
					obj.setTitle(a.getAtomID());
				else
					obj.setTitle(a.getAtomLabel());

				obj.addStyleName(UtilResource.getCssRulePart(rulePart));
				if(handler!=null){
					obj.addStyleName(Resources.INSTANCE.swrleditor().vtlink());
					obj.addClickHandler(handler);
				}

				table.setWidget(row, 0, obj);
				table.getFlexCellFormatter().setColSpan(row, 0, 2);
				table.getFlexCellFormatter().setStyleName(row, 0, Resources.INSTANCE.swrleditor().q_builtin());
				row++;
			} else if(a.getAtomType() == Atom.TYPE_ATOM.SAME_DIFERENT ){
				used.add(a);

				String label;
				if (typeView == TYPE_VIEW.ID)
					label = a.getPredicateID().equalsIgnoreCase("differentFrom") ? "different from" : "same as";
				else
					label = a.getPredicateLabel().equalsIgnoreCase("differentFrom") ? "different from" : "same as";

				
				
				HTML obj = new HTML("<img src=\""+Resources.INSTANCE.sameDifferent().getURL()+"\" /> "+
						getHTMLParameter(a.getVariables().get(0), typeView)+
						" <span class='"+UtilResource.getCssTypeAtom(a.getAtomType())+"'>"+label+"</span> "+
						getHTMLParameter(a.getVariables().get(1), typeView));

				if (typeView == TYPE_VIEW.ID)
					obj.setTitle(a.getAtomID());
				else
					obj.setTitle(a.getAtomLabel());


				obj.addStyleName(UtilResource.getCssRulePart(rulePart));
				if(handler!=null){
					obj.addStyleName(Resources.INSTANCE.swrleditor().vtlink());
					obj.addClickHandler(handler);
				}
				table.setWidget(row, 0, obj);
				table.getFlexCellFormatter().setColSpan(row, 0, 2);
				table.getFlexCellFormatter().setStyleName(row, 0, Resources.INSTANCE.swrleditor().q_builtin());
				row++;
			} else if ((a.getAtomType() == Atom.TYPE_ATOM.INDIVIDUAL_PROPERTY ) || (a.getAtomType() == Atom.TYPE_ATOM.DATAVALUE_PROPERTY)) {
				String img;
				if(a.getAtomType() == Atom.TYPE_ATOM.INDIVIDUAL_PROPERTY)
					img = "<img src=\""+Resources.INSTANCE.oWLObjectProperty().getURL()+"\" /> ";
				else
					img = "<img src=\""+Resources.INSTANCE.oWLDatatypeProperty().getURL()+"\" /> ";

				HTML obj;
				if (typeView == TYPE_VIEW.ID){ 
					obj = new HTML(img+getHTMLParameter(a.getVariables().get(0), typeView)+" "+a.getPredicateID()+" "+getHTMLParameter(a.getVariables().get(1), typeView));
					obj.setTitle(a.getAtomID());
				}else{
					obj = new HTML(img+getHTMLParameter(a.getVariables().get(0), typeView)+" "+a.getPredicateLabel()+" "+getHTMLParameter(a.getVariables().get(1), typeView));
					obj.setTitle(a.getAtomLabel());				
				}


				obj.addStyleName(UtilResource.getCssRulePart(rulePart));
				if(handler!=null){
					obj.addStyleName(Resources.INSTANCE.swrleditor().vtlink());
					obj.addClickHandler(handler);
				}
				table.setWidget(row, 0, obj);
				table.getFlexCellFormatter().setColSpan(row, 0, 2);
				table.getFlexCellFormatter().setStyleName(row, 0, Resources.INSTANCE.swrleditor().q_builtin());
				row++;
			} else {
				HTML obj;
				if (typeView == TYPE_VIEW.ID){ 
					obj = new HTML(a.getAtomID());
					obj.setTitle(a.getAtomID());
				}else{
					obj = new HTML(a.getAtomLabel());
					obj.setTitle(a.getAtomLabel());			
				}


				obj.addStyleName(UtilResource.getCssRulePart(rulePart));
				if(handler!=null){
					obj.addStyleName(Resources.INSTANCE.swrleditor().vtlink());
					obj.addClickHandler(handler);
				}
				table.setWidget(row, 0, obj);
				table.getFlexCellFormatter().setColSpan(row, 0, 2);
				table.getFlexCellFormatter().setStyleName(row, 0, Resources.INSTANCE.swrleditor().q_builtin());
				row++;
			}
		}

		return table;
	}

	private static String getHTMLParameter(Variable param, TYPE_VIEW typeView){
		
		
		if (typeView == TYPE_VIEW.ID)
			return "<b class=\""+UtilResource.getCssTypeVariableView(param.getTypeVariable())+"\">"+param.getFormatedID()+"</b>";
		else 
			return "<b class=\""+UtilResource.getCssTypeVariableView(param.getTypeVariable())+"\">"+param.getFormatedLabel()+"</b>";
	}
	private static String getFormatedBuiltin(Atom a, TYPE_VIEW typeView){
		if(a.getAtomType() != Atom.TYPE_ATOM.BUILTIN) return "";
		HashMap<String, BuiltinMap> dict = getListBuiltins();


		String atom;
		if (typeView == TYPE_VIEW.ID)
			atom = a.getPredicateID();
		else
			atom = a.getPredicateLabel();



		if(dict.containsKey(atom.toLowerCase())){
			return dict.get(atom.toLowerCase()).getValue(a.getVariables(), typeView);
		} else {
			String ret = atom+"("+getHTMLParameter(a.getVariables().get(0), typeView);
			for(int i = 1; i < a.getCountVariables(); i++)
				ret += ", "+getHTMLParameter(a.getVariables().get(i), typeView);
			return ret+")";
		}

	}

	private static HashMap<String, BuiltinMap> getListBuiltins(){
		HashMap<String, BuiltinMap> dict = new HashMap<String, BuiltinMap>();
		// add
		dict.put("swrlb:add", new BuiltinMap() {
			@Override
			public String getValue(List<Variable> params, TYPE_VIEW typeView) {
				String html = getHTMLParameter(params.get(0), typeView)+" = "+getHTMLParameter(params.get(1), typeView);
				for(int i = 2; i < params.size(); i++)
					html += " + "+getHTMLParameter(params.get(i), typeView);
				return html;
			}
		});

		// divide
		dict.put("swrlb:divide", new BuiltinMap() {
			@Override
			public String getValue(List<Variable> params, TYPE_VIEW typeView) {
				return getHTMLParameter(params.get(0), typeView)+" = "+getHTMLParameter(params.get(1), typeView)+" / "+getHTMLParameter(params.get(2), typeView);
			}
		});

		// equal
		dict.put("swrlb:equal", new BuiltinMap() {
			@Override
			public String getValue(List<Variable> params, TYPE_VIEW typeView) {
				return getHTMLParameter(params.get(0), typeView)+" = "+getHTMLParameter(params.get(1), typeView);
			}
		});

		// greaterThan
		dict.put("swrlb:greaterthan", new BuiltinMap() {
			@Override
			public String getValue(List<Variable> params, TYPE_VIEW typeView) {
				return getHTMLParameter(params.get(0), typeView)+" > "+getHTMLParameter(params.get(1), typeView);
			}
		});

		// greaterThanOrEqual
		dict.put("swrlb:greaterthanorequal", new BuiltinMap() {
			@Override
			public String getValue(List<Variable> params, TYPE_VIEW typeView) {
				return getHTMLParameter(params.get(0), typeView)+" >= "+getHTMLParameter(params.get(1), typeView);
			}
		});

		// lessThan
		dict.put("swrlb:lessthan", new BuiltinMap() {
			@Override
			public String getValue(List<Variable> params, TYPE_VIEW typeView) {
				return getHTMLParameter(params.get(0), typeView)+" < "+getHTMLParameter(params.get(1), typeView);
			}
		});

		// lessThanOrEqual
		dict.put("swrlb:lessthanorequal", new BuiltinMap() {
			@Override
			public String getValue(List<Variable> params, TYPE_VIEW typeView) {
				return getHTMLParameter(params.get(0), typeView)+" <= "+getHTMLParameter(params.get(1), typeView);
			}
		});

		return dict;
	}

}
