package br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.decisiontree;


import java.util.ArrayList;
import java.util.List;

public class NodeDecisionTreeRanking implements NodeDecisionTree {

    //private defaultNodeInRulesTreeLabel nodeLabel;

    private static final long serialVersionUID = 1L;

    private String value;
    private ATOM_TYPE type;
    private String toolTip;

    private NodeDecisionTree parentNode;

    private String nameRule = "";

    private List<String> rulesRelated;

    /* Children nodes */
    private final List<NodeDecisionTree> childNodes = new ArrayList<NodeDecisionTree>();


    public NodeDecisionTreeRanking() {
	super();
	rulesRelated = new ArrayList<String>();
	nameRule = "";

    }

    public NodeDecisionTreeRanking(NodeDecisionTree parentNode, String toolTip) {
	this();
	value = "";
	this.toolTip = toolTip;
	//this.nodeLabel = new defaultNodeInRulesTreeLabel(this);
	this.parentNode = parentNode;
    }

    public void addChildNodes(NodeDecisionTree node) {
	childNodes.add(node);
    }

    @Override
    public ATOM_TYPE getAtomType(){
	return type;
    }

    public NodeDecisionTree getChild(int index) {
	return childNodes.get(index);
    }

    @Override
    public List<NodeDecisionTree> getChildren() {
	return childNodes;
    }

    public int getNumberOfChildren() {
	return childNodes.size();
    }

    @Override
    public NodeDecisionTree getParentNode() {
	return parentNode;
    }

    @Override
    public String getRuleName() {
	return nameRule;
    }

    @Override
    public List<String> getRulesRelated() {
	return rulesRelated;
    }

    @Override
    public String getToolTip() {
	return toolTip;
    }

    @Override
    public String getValue(){
	return value;
    }

    public NodeDecisionTree removeChildNodes(int index) {
	return childNodes.remove(index);
    }

    public void setAtomType(ATOM_TYPE type){
	this.type = type;
    }

    public void setChildNodes(int index, NodeDecisionTree node) {
	childNodes.set(index, node);
    }

    public void setParentNode(NodeDecisionTree parentNode) {
	this.parentNode = parentNode;
    }

    public void setRuleName(String nameRule) {
	this.nameRule = nameRule;
    }

    public void setRulesRelated(List<String> rulesRelated) {
	this.rulesRelated = rulesRelated;
    }

    public void setToolTip(String toolTip){
	this.toolTip = toolTip;
    }

    public void setValue(String value){
	this.value = value;
	//nodeLabel.setText(value);
    }
}
