package br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.ontology.protege3;

import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Atom.TYPE_ATOM;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.OntologyView.Presenter;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.Node;
import com.gwtext.client.widgets.tree.TreeNode;
import com.gwtext.client.widgets.tree.event.TreeNodeListenerAdapter;

//import edu.stanford.bmir.protege.web.client.model.Project;
//import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
//import edu.stanford.bmir.protege.web.client.ui.ontology.properties.PropertiesTreePortlet;

public class PropertiesTreePortletSwrlEditor extends SimplePanel { //PropertiesTreePortlet {

	private TreeNodeListenerAdapter nodeListener;

	public PropertiesTreePortletSwrlEditor() {//Project project) {
		//super(project);
		//setTitle("");
		add(new Label("PropertiesTreePortletSwrlEditor"));
	}

	//	@Override
	//	protected void addToolbarButtons() { }

	//	@Override
	//	protected TreeNode createTreeNode(EntityData entityData) {
	//		TreeNode node = super.createTreeNode(entityData);
	//		node.addListener(nodeListener);
	//		return node;
	//	}

	//	@Override
	//	protected Tool[] getTools() {
	//		return new Tool[] {};
	//	}

	public void setPresenter(final Presenter presenter){
		nodeListener = new TreeNodeListenerAdapter() {
			@Override
			public void onClick(Node node, EventObject e) {
				int[] xy = e.getXY();

				if ( ((TreeNode)node).getIconCls().equals("protege-datatype-property-icon")) {
					presenter.setSelectedPredicate(TYPE_ATOM.DATAVALUE_PROPERTY, ((TreeNode)node).getText(), xy[0], xy[1]);
				} else {
					presenter.setSelectedPredicate(TYPE_ATOM.INDIVIDUAL_PROPERTY, ((TreeNode)node).getText(), xy[0], xy[1]);
				}
			}
		};
	}
}
