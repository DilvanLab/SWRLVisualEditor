package br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.visualization;

import java.util.List;

import br.usp.icmc.dilvan.swrlEditor.client.resources.Resources;
import br.usp.icmc.dilvan.swrlEditor.client.resources.UtilResource;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Atom;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Rule;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Variable;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.activity.visualization.AutismModel;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.VisualizationView.Presenter;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.VisualizationView.TYPE_VIEW;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;


public class SimpleRuleViewAutism extends SimpleRuleView {


	public SimpleRuleViewAutism(Rule rule, Presenter presenter, TYPE_VIEW typeView) {
		super(rule, presenter, typeView);
	}
	
	@Override
	public void show() {
		contentRule.clear();
		contentRule.add(getSWRLAutismPanel(rule, clickHandlerAtom));
	}

	private VerticalPanel getSWRLAutismPanel(Rule rule, ClickHandler clickHandlerAtom){
		
		AutismModel au = presenter.getAutismModel(rule, typeView);
		
		String match = au.getMatch();

		VerticalPanel vp = new VerticalPanel();
		vp.setWidth("100%");

		// Linha 1 - class e match 
		HorizontalPanel title = new HorizontalPanel();	
		title.setSpacing(5);

		Atom antMain = au.getMainAnt();
		String antecedentMainText = "undefined";

		if(antMain != null){
			if (typeView == TYPE_VIEW.ID)
				antecedentMainText = antMain.getPredicateID();
			else
				antecedentMainText = antMain.getPredicateLabel();
		}
		
		HTML block = new HTML("<span class='"+ Resources.INSTANCE.swrleditor().det()+" "+Resources.INSTANCE.swrleditor().peq()+"'>class</span><br /><img src=\""+Resources.INSTANCE.class_().getURL()+"\" /> "+ antecedentMainText);
		block.setStyleName(Resources.INSTANCE.swrleditor().q_class());
		title.add(block);
		title.add(new HTML("<span class='"+ Resources.INSTANCE.swrleditor().det()+" "+Resources.INSTANCE.swrleditor().peq()+"'>match</span><br /><b>"+match+"</b>"));
		vp.add(title);

		// Linhas N - form to each class in consequent
		for(Atom atom : au.getMainCons()){
			vp.add(new HTML("<hr>"));


			if (typeView == TYPE_VIEW.ID){
				vp.add(new HTML("<img src=\""+Resources.INSTANCE.individuo().getURL()+"\"/> <b>"+atom.getPredicateID()+"</b>"));
				for(Atom a : au.getFirstParam(atom.getVariables().get(0).getFormatedID())){
					HorizontalPanel line = new HorizontalPanel();
					vp.add(line);

					line.setVerticalAlignment( HasVerticalAlignment.ALIGN_MIDDLE );
					line.setSpacing(5);

					String img;
					if(a.getAtomType() == Atom.TYPE_ATOM.INDIVIDUAL_PROPERTY)
						img = "<img src=\""+Resources.INSTANCE.oWLObjectProperty().getURL()+"\" /> ";
					else
						img = "<img src=\""+Resources.INSTANCE.oWLDatatypeProperty().getURL()+"\" /> ";
					String label = a.getPredicateID().substring(a.getPredicateID().indexOf(":")+1).replace("_", " ").replace("'","");

					line.add(new HTML("<div style='width: 300px; text-align: right;'>"+img+" "+label+"</div>"));
					line.getWidget(0).setStyleName(Resources.INSTANCE.swrleditor().q_subnivel());
					Variable secondParam = a.getVariables().get(1);

					if( !secondParam.getFormatedID().startsWith("?") ){
						if(Variable.TYPE_VARIABLE.INDIVIDUALID.equals(secondParam.getTypeVariable())){
							
							line.add(new HTML("<b class='"+Resources.INSTANCE.swrleditor().param_INDIVIDUALID()+"'><img src=\""+Resources.INSTANCE.class_().getURL()+"\" />"+secondParam.getFormatedID()+"</b>"));
						} else {
							line.add(new HTML("<b class='"+Resources.INSTANCE.swrleditor().param_DATALITERAL()+"'>"+secondParam.getFormatedID()+"</b>"));
						}
					} else {
						List<Atom> subAtoms = au.getLastParam(a.getVariables().get(1).getFormatedID());
						if(subAtoms.size() == 2){
							Atom oa;
							if(subAtoms.get(0).equals(a)){
								oa = subAtoms.get(1);
							} else {
								oa = subAtoms.get(0);
							}
							String imgDeriv = "";
							if(rule.getAntecedent().contains(oa)){
								imgDeriv = "<img src=\""+Resources.INSTANCE.classAntecedent().getURL()+"\" align='left'/>&nbsp; ";
							}
							
							line.add(new HTML(imgDeriv+"<b class='"+ UtilResource.getCssTypeAtom(oa.getAtomType()) +"'>"+oa.getPredicateID()+"</b>"));
						} else if (subAtoms.size() == 1){
							Atom aa = subAtoms.get(0); 
							String atomName = "";
							for(Atom sAtom : au.getMainCons()){
								if(atom.equals(sAtom)) continue;
								if( aa.getVariables().get(1).getFormatedID().equals(sAtom.getVariables().get(0).getFormatedID()) ){
									atomName = sAtom.getPredicateID();
									break;
								}
							}
							if(atomName.isEmpty()){
								au.setErrors("Imposible found subtree for the argument "+a.getVariables().get(1).getFormatedID());
							} else {
								line.add(new HTML("<img src=\""+Resources.INSTANCE.individuo().getURL()+"\"/><b>"+atomName+"</b>"));
							}
						} else {
							au.setErrors("Imposible mapping the argument "+a.getVariables().get(1).getFormatedID());
						}
					}
				}
			}else{
				vp.add(new HTML("<img src=\""+Resources.INSTANCE.individuo().getURL()+"\"/> <b>"+atom.getPredicateLabel()+"</b>"));
				for(Atom a : au.getFirstParam(atom.getVariables().get(0).getFormatedLabel())){
					HorizontalPanel line = new HorizontalPanel();
					vp.add(line);

					line.setVerticalAlignment( HasVerticalAlignment.ALIGN_MIDDLE );
					line.setSpacing(5);

					String img;
					if(a.getAtomType() == Atom.TYPE_ATOM.INDIVIDUAL_PROPERTY)
						img = "<img src=\""+Resources.INSTANCE.oWLObjectProperty().getURL()+"\" /> ";
					else
						img = "<img src=\""+Resources.INSTANCE.oWLDatatypeProperty().getURL()+"\" /> ";
					String label = a.getPredicateLabel().substring(a.getPredicateLabel().indexOf(":")+1).replace("_", " ").replace("'","");

					line.add(new HTML("<div style='width: 300px; text-align: right;'>"+img+" "+label+"</div>"));
					line.getWidget(0).setStyleName(Resources.INSTANCE.swrleditor().q_subnivel());
					Variable secondParam = a.getVariables().get(1);

					if( !secondParam.getFormatedLabel().startsWith("?") ){
						if(Variable.TYPE_VARIABLE.INDIVIDUALID.equals(secondParam.getTypeVariable())){
							line.add(new HTML("<b class='"+Resources.INSTANCE.swrleditor().param_INDIVIDUALID()+"'><img src=\""+Resources.INSTANCE.class_().getURL()+"\" />"+secondParam.getFormatedLabel()+"</b>"));
						} else {
							line.add(new HTML("<b class='"+Resources.INSTANCE.swrleditor().param_DATALITERAL()+"'>"+secondParam.getFormatedLabel()+"</b>"));
						}
					} else {
						List<Atom> subAtoms = au.getLastParam(a.getVariables().get(1).getFormatedLabel());
						if(subAtoms.size() == 2){
							Atom oa;
							if(subAtoms.get(0).equals(a)){
								oa = subAtoms.get(1);
							} else {
								oa = subAtoms.get(0);
							}
							String imgDeriv = "";
							if(rule.getAntecedent().contains(oa)){
								imgDeriv = "<img src=\""+Resources.INSTANCE.classAntecedent().getURL()+"\" align='left'/>&nbsp; ";
							}
							line.add(new HTML(imgDeriv+"<b class='"+ UtilResource.getCssTypeAtom(oa.getAtomType()) +"'>"+oa.getPredicateLabel()+"</b>"));
						} else if (subAtoms.size() == 1){
							Atom aa = subAtoms.get(0); 
							String atomName = "";
							for(Atom sAtom : au.getMainCons()){
								if(atom.equals(sAtom)) continue;
								if( aa.getVariables().get(1).getFormatedLabel().equals(sAtom.getVariables().get(0).getFormatedLabel()) ){
									atomName = sAtom.getPredicateLabel();
									break;
								}
							}
							if(atomName.isEmpty()){
								au.setErrors("Imposible found subtree for the argument "+a.getVariables().get(1).getFormatedLabel());
							} else {
								line.add(new HTML("<img src=\""+Resources.INSTANCE.individuo().getURL()+"\"/><b>"+atomName+"</b>"));
							}
						} else {
							au.setErrors("Imposible mapping the argument "+a.getVariables().get(1).getFormatedLabel());
						}
					}
				}
			}
		}

		//Some errors
		if(!au.isCorrect()){
			vp.add(new HTML("<hr>"));
			for(String s : au.getErrors()){
				HTML e = new HTML(s);
				e.addStyleName(Resources.INSTANCE.swrleditor().error());
				vp.add(e);
			}
		}

		return vp;
	}
	
	
}
