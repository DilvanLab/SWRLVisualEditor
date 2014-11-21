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
//import edu.stanford.bmir.protege.web.client.ui.ontology.classes.ClassTreePortlet;
//import edu.stanford.bmir.protege.web.client.ui.util.UIUtil;

public class ClassTreePortletSwrlEditor extends SimplePanel { //ClassTreePortlet {

	private TreeNodeListenerAdapter nodeListener;

	public ClassTreePortletSwrlEditor() { //Project project) {
		//super(project, false, false, false, false, null);
		add(new Label("ClassTreePortletSwrlEditor"));
	}

	public void setPresenter(final Presenter presenter){
		nodeListener = new TreeNodeListenerAdapter() {
			@Override
			public void onClick(Node node, EventObject e) {
				int[] xy = e.getXY();
				presenter.setSelectedPredicate(TYPE_ATOM.CLASS, ((TreeNode)node).getText(), xy[0], xy[1]);
			}
		};
	}

	//	@Override
	//	protected TreeNode createTreeNode(final EntityData entityData) {
	//		final TreeNode node = new TreeNode(UIUtil.getDisplayText(entityData));
	//		node.setHref(null);
	//		node.setUserObject(entityData);
	//		node.setAllowDrag(false);
	//		node.setAllowDrop(false);
	//		setTreeNodeIcon(node, entityData);
	//
	//		node.setText(computeText(node));
	//
	//		node.addListener(nodeListener);
	//
	//		return node;
	//	}
}
