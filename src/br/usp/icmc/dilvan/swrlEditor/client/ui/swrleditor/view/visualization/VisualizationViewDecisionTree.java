package br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.visualization;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.usp.icmc.dilvan.swrlEditor.client.resources.Resources;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.decisiontree.NodeDecisionTree;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.decisiontree.NodeDecisionTree.ATOM_TYPE;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.WaitingCreateToRun;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.util.Options;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.util.UtilLoading;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.OptionsView;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.VisualizationView.Presenter;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.visualization.decisiontree.DefaultNodeInRulesTreeLabel;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.visualization.decisiontree.NodeLabelListener;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.visualization.decisiontree.NodeToolTip;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.visualization.decisiontree.NodeTreeInt;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.graphics.client.Color;
import com.google.gwt.widgetideas.graphics.client.GWTCanvas;

public class VisualizationViewDecisionTree extends Composite {

	private static VisualizationViewDecisionTreeUiBinder uiBinder = GWT
			.create(VisualizationViewDecisionTreeUiBinder.class);
	@UiField
	AbsolutePanel panelPathLabels;
	@UiField
	ListBox listAlgorithm;
	@UiField
	Button btnRefresh;
	@UiField
	AbsolutePanel panelPath;
	@UiField
	AbsolutePanel panelTree;
	@UiField
	ScrollPanel scroolPath;
	@UiField
	ScrollPanel scroolTree;
	@UiField
	VerticalPanel pnlBase;
	@UiField
	HorizontalPanel pnlOptions;

	final private PopupPanel rightClickMenuInsert = new PopupPanel(true);
	final private PopupPanel rightClickMenuEdit = new PopupPanel(true);

	private final int NUMBER_LEVELS_DISPLAYED = 3;
	private final int PANELTREE_HEIGHT_INITIAL = 30;
	private final int NODE_HEIGHT = 17;
	// private final int NODE_WIDTH = 500;
	private final int SPACE_BETWEEN_NODES = 100;

	private double widthChar = 0;

	private NodeDecisionTree tree;

	private AbsolutePanel panelTreeLabels;
	private GWTCanvas canvasTree;

	private Presenter presenter;

	private final String SUSPENSION_POINTS = "...";

	private boolean writePermission;

	private NodeToolTip toolTipNodes =  new NodeToolTip();

	private DefaultNodeInRulesTreeLabel rootLabel;

	private List<GraphAttributes> graphAttributes;
	private int graphAttributesNumber;


	interface VisualizationViewDecisionTreeUiBinder extends
	UiBinder<Widget, VisualizationViewDecisionTree> {
	}

	public VisualizationViewDecisionTree() {
		initWidget(uiBinder.createAndBindUi(this));

		graphAttributes = new ArrayList<GraphAttributes>();
		graphAttributes.add(new GraphAttributes(new Color(255, 0, 0), 4, 2));
		graphAttributes.add(new GraphAttributes(new Color(0, 255, 0), 8, 5));
		graphAttributes.add(new GraphAttributes(new Color(0, 0, 255), 12, 8));

		graphAttributes.add(new GraphAttributes(new Color(255, 255, 0), 16, 11));
		graphAttributes.add(new GraphAttributes(new Color(0, 255, 255), 20, 14));
	}

	public VisualizationViewDecisionTree(Presenter presenter) {
		this();

		this.presenter = presenter;

		panelPath.addStyleName(Resources.INSTANCE.swrleditor()
				.decisionTreeBackground());
		panelPathLabels.addStyleName(Resources.INSTANCE.swrleditor()
				.decisionTreeBackground());
		panelTree.addStyleName(Resources.INSTANCE.swrleditor()
				.decisionTreeBackground());
		scroolTree.addStyleName(Resources.INSTANCE.swrleditor()
				.decisionTreeBackground());

		canvasTree = new GWTCanvas(1000, 1000);
		canvasTree.setSize("100%", "100%");
		canvasTree.setLineWidth(1);
		canvasTree.setStrokeStyle(Color.BLACK);

		panelTreeLabels = new AbsolutePanel();
		panelTreeLabels.setSize("100px", "100%");

		panelTree.add(canvasTree, 0, 0);
		panelTree.add(panelTreeLabels, 0, 0);

	}

	public void setWritePermission(boolean permission) {
		this.writePermission = permission;
	}

	public void setTree(NodeDecisionTree treeNode) {
		this.tree = treeNode;
		if (tree != null) {

			WaitingCreateToRun waiting = new WaitingCreateToRun(
					panelPathLabels, 10) {
				@Override
				public void run() {
					showDecisionTree();
				}
			};
			waiting.start();
		}
	}

	private void showDecisionTree() {
		drawTree(tree, 50, NUMBER_LEVELS_DISPLAYED);

		UtilLoading.hide();
	}

	private void drawTree(NodeDecisionTree node, int left, int viewLevel) {

		graphAttributesNumber = 0; 

		scroolPath.setWidth(Integer.toString(this.getOffsetWidth()) + "px");
		scroolTree.setWidth(Integer.toString(this.getOffsetWidth()) + "px");
		scroolTree
		.setHeight(Integer.toString(pnlBase.getOffsetHeight()
				- (scroolPath.getOffsetHeight() + pnlOptions
						.getOffsetHeight()))
						+ "px");

		scroolTree
		.setHeight(Integer.toString(pnlBase.getOffsetHeight()
				- (scroolPath.getOffsetHeight() + pnlOptions
						.getOffsetHeight()))
						+ "px");

		panelPathLabels.clear();
		canvasTree.clear();
		panelTreeLabels.clear();

		NodeTreeInt sheetNumber = new NodeTreeInt(0);

		int[] majorWidth = new int[NUMBER_LEVELS_DISPLAYED];
		for (int i = 0; i < NUMBER_LEVELS_DISPLAYED; i++)
			majorWidth[i] = 0;

		calculatesSizeNode(node, sheetNumber, viewLevel, majorWidth);

		int sizePath = drawPath(node) + 50;

		setWidthPath(sizePath);

		int sumWidth = 0;

		for (int i = 0; i < majorWidth.length; i++)
			sumWidth = (int) (sumWidth + (majorWidth[i] * widthChar) + SPACE_BETWEEN_NODES);

		sumWidth += 600;

		setSizePanelTree(sumWidth, NODE_HEIGHT * (sheetNumber.getValue() + 2)
				+ PANELTREE_HEIGHT_INITIAL);

		rootLabel = null;
		int scroolTop = drawNode(-1, -1, node, sheetNumber, left,
				PANELTREE_HEIGHT_INITIAL, viewLevel, majorWidth);

		scroolTree.setVerticalScrollPosition(scroolTop
				- (scroolTree.getOffsetHeight() / 2));
		scroolTree.setHorizontalScrollPosition(0);

	}

	private int calculatesSizeNode(NodeDecisionTree node,
			NodeTreeInt sheetNumber, int viewLevel, int[] majorWidth) {

		if (viewLevel == 0) {
			sheetNumber.setValue(1);
			return 1;
		}
		int sum = 0;

		for (int i = 0; i < node.getChildren().size(); i++) {
			NodeTreeInt newSheetNumber = sheetNumber
					.addChildNodes(new NodeTreeInt(0));

			sum = sum
					+ calculatesSizeNode(node.getChildren().get(i),
							newSheetNumber, viewLevel - 1, majorWidth);

			if (majorWidth[NUMBER_LEVELS_DISPLAYED - viewLevel] < node
					.getChildren().get(i).getValue().length())
				majorWidth[NUMBER_LEVELS_DISPLAYED - viewLevel] = node
				.getChildren().get(i).getValue().length();

		}
		if (sum == 0) {
			sheetNumber.setValue(1);
			return 1;
		}
		sheetNumber.setValue(sum);
		return sum;
	}

	private int drawPath(NodeDecisionTree node) {

		if (node.getParentNode() == null) {
			DefaultNodeInRulesTreeLabel label = new DefaultNodeInRulesTreeLabel(
					node);
			addEventsLabel(label);
			panelPathLabels.add(label, 10, 0);

			widthChar = (label.getOffsetWidth() * 1.0)
					/ (node.getValue().length() * 1.0);

			return label.getAbsoluteLeft() + label.getOffsetWidth();
		}

		int pos = drawPath(node.getParentNode());

		Label lblAnd = new Label("^");

		panelPathLabels.add(lblAnd, pos - 10, 0);

		DefaultNodeInRulesTreeLabel label = new DefaultNodeInRulesTreeLabel(
				node);
		addEventsLabel(label);
		panelPathLabels.add(label,
				lblAnd.getAbsoluteLeft() + lblAnd.getOffsetWidth() - 10, 0);
		return label.getAbsoluteLeft() + label.getOffsetWidth();
	}

	private int drawNode(int leftParent, int topParent, NodeDecisionTree node,
			NodeTreeInt sheetNumber, int left, int beginTop, int viewLevel,
			int[] majorWidthLevel) {
		if (node.getChildren().size() == 0)
			return 0;

		if (viewLevel == 0) {

			if ((node.getChildren().size() == 1) && node.getChildren().get(0).getAtomType() == ATOM_TYPE.CONSEQUENT) {

				DefaultNodeInRulesTreeLabel label = new DefaultNodeInRulesTreeLabel(
						node.getChildren().get(0));
				addEventsLabel(label);
				panelTreeLabels.add(label, left, topParent);
				canvasTree.beginPath();
				canvasTree.moveTo(
						leftParent
						+ (widthChar
								* node.getChildren().get(0).getValue()
								.length() + 5), topParent + 8);
				canvasTree.lineTo(left - 5, topParent + 8);
				canvasTree.closePath();
				canvasTree.stroke();

				if (rootLabel== null)
					rootLabel = label;

			} else {

				DefaultNodeInRulesTreeLabel labelAux = new DefaultNodeInRulesTreeLabel(
						SUSPENSION_POINTS, node);
				labelAux.addStyleName(Resources.INSTANCE.swrleditor()
						.whiteBackground());
				labelAux.addStyleName(Resources.INSTANCE.swrleditor()
						.swrlRule());

				addEventsLabel(labelAux);

				panelTreeLabels.add(labelAux, left, topParent);

				canvasTree.beginPath();
				canvasTree.moveTo(leftParent
						+ (widthChar * labelAux.getText().length() + 5),
						topParent + 8);
				canvasTree.lineTo(left - 5, topParent + 8);
				canvasTree.closePath();
				canvasTree.stroke();

				if (rootLabel== null)
					rootLabel = labelAux;

			}
			return 0;
		}

		int top = 0, sumChilds = 0, topForChild = 0;

		for (int i = 0; i < node.getChildren().size(); i++) {

			top = beginTop
					+ (NODE_HEIGHT * (sumChilds))
					+ (NODE_HEIGHT * (sheetNumber.getChildNodes(i).getValue()) / 2);
			topForChild = beginTop + (NODE_HEIGHT * (sumChilds));

			sumChilds = sumChilds + sheetNumber.getChildNodes(i).getValue();

			if (leftParent > -1) {
				canvasTree.beginPath();
				canvasTree.moveTo(leftParent
						+ (widthChar * node.getValue().length() + 5),
						topParent + 8);
				canvasTree.lineTo(left - 5, top + 8);
				canvasTree.closePath();
				canvasTree.stroke();
			}

			DefaultNodeInRulesTreeLabel label = new DefaultNodeInRulesTreeLabel(
					node.getChildren().get(i));
			addEventsLabel(label);
			panelTreeLabels.add(label, left, top);

			drawNode(left, top, node.getChildren().get(i),
					sheetNumber.getChildNodes(i),
					(int) (left + (majorWidthLevel[NUMBER_LEVELS_DISPLAYED
					                               - viewLevel] * widthChar))
					                               + SPACE_BETWEEN_NODES, topForChild, viewLevel - 1,
					                               majorWidthLevel);

		}
		return beginTop + (NODE_HEIGHT * sumChilds / 2);
	}

	private void setWidthPath(int width) {
		if (width < scroolPath.getOffsetWidth()) {
			panelPath.setWidth("100%");
			panelPathLabels.setWidth("100%");
		} else {
			panelPath.setWidth(Integer.toString(width) + "px");
			panelPathLabels.setWidth(Integer.toString(width) + "px");
		}

	}

	public void setSizePanelTree(int widthPanelTree, int heightPanelTree) {
		canvasTree.resize(widthPanelTree, heightPanelTree);
		panelTree.setSize(Integer.toString(widthPanelTree) + "px",
				Integer.toString(heightPanelTree) + "px");
		panelTreeLabels.setSize(Integer.toString(widthPanelTree) + "px",
				Integer.toString(heightPanelTree) + "px");
	}

	private void addEventsLabel(DefaultNodeInRulesTreeLabel label) {
		if (label == null)
			return;

		if (label.getText().equals(NodeDecisionTree.ROOT_VALUE)) {
			label.addMouseListener(new NodeLabelListener() {

				@Override
				public void onClick(Widget sender, Event event) {
					clickNodeTree((DefaultNodeInRulesTreeLabel) sender);
				}

				@Override
				public void onRightClick(Widget sender, Event event) {
				}

				@Override
				public void onMouseMove(Widget sender, Event event) {
					moveMouseNodeTree((DefaultNodeInRulesTreeLabel) sender);
				}

				@Override
				public void onMouseOut(Widget sender, Event event) {
					exitMouseNodeTree((DefaultNodeInRulesTreeLabel) sender);
				}

				@Override
				public void onShowToolTip(Widget sender, Event event){}


			});

		} else if (label.getText().equals(SUSPENSION_POINTS)) {
			label.addMouseListener(new NodeLabelListener() {

				@Override
				public void onClick(Widget sender, Event event) {
					clickNodeTree((DefaultNodeInRulesTreeLabel) sender);
				}

				@Override
				public void onRightClick(Widget sender, Event event) {
					rigthClickNodeTree((DefaultNodeInRulesTreeLabel) sender, event);
				}

				@Override
				public void onMouseMove(Widget sender, Event event) {
					moveMouseNodeTree((DefaultNodeInRulesTreeLabel) sender);
				}

				@Override
				public void onMouseOut(Widget sender, Event event) {
					exitMouseNodeTree((DefaultNodeInRulesTreeLabel) sender);
				}

				@Override
				public void onShowToolTip(Widget sender, Event event){}

			});
		} else if (label.getText().equals(NodeDecisionTree.CONSEQUENT_VALUE)) {
			label.addMouseListener(new NodeLabelListener() {

				@Override
				public void onClick(Widget sender, Event event) {
				}

				@Override
				public void onRightClick(Widget sender, Event event) {
					rigthClickNodeTree((DefaultNodeInRulesTreeLabel) sender, event);
				}

				@Override
				public void onMouseMove(Widget sender, Event event) {
					moveMouseNodeTree((DefaultNodeInRulesTreeLabel) sender);
				}

				@Override
				public void onMouseOut(Widget sender, Event event) {
					toolTipNodes.hide();
					exitMouseNodeTree((DefaultNodeInRulesTreeLabel) sender);
				}

				@Override
				public void onShowToolTip(Widget sender, Event event){
					String textToolTip = ((DefaultNodeInRulesTreeLabel) sender).getNode().getToolTip();
					String css = Resources.INSTANCE.swrleditor().hint();
					int x = event.getClientX();
					int y = event.getClientY();
					toolTipNodes.show(textToolTip, 5000, css, x, y);
				}

			});
		} else {
			label.addMouseListener(new NodeLabelListener() {

				@Override
				public void onClick(Widget sender, Event event) {
					clickNodeTree((DefaultNodeInRulesTreeLabel) sender);
				}

				@Override
				public void onRightClick(Widget sender, Event event) {
					rigthClickNodeTree((DefaultNodeInRulesTreeLabel) sender, event);
				}

				@Override
				public void onMouseMove(Widget sender, Event event) {
					moveMouseNodeTree((DefaultNodeInRulesTreeLabel) sender);
				}

				@Override
				public void onMouseOut(Widget sender, Event event) {
					exitMouseNodeTree((DefaultNodeInRulesTreeLabel) sender);
				}

				@Override
				public void onShowToolTip(Widget sender, Event event){}

			});
		}

	}

	private void clickNodeTree(DefaultNodeInRulesTreeLabel label) {
		drawTree(label.getNode(), 50, NUMBER_LEVELS_DISPLAYED);
	}

	private void rigthClickNodeTree(final DefaultNodeInRulesTreeLabel labelClick, Event event) {
		int x = DOM.eventGetClientX(event);
		int y = DOM.eventGetClientY(event);

		if (!labelClick.getText().equals(SUSPENSION_POINTS))
			if (labelClick.getNode().getRuleName()!= null && !labelClick.getNode().getRuleName().equals("")) {
				Command editRule = new Command() {
					@Override
					public void execute() {
						rightClickMenuEdit.hide();
						presenter.goToEditRule(labelClick.getNode().getRuleName());
					}
				};

				MenuBar popupMenuBarEdit = new MenuBar(true);
				MenuItem EditItem = new MenuItem("Edit Rule", true, editRule);
				EditItem.setEnabled(writePermission);
				if (writePermission)
					EditItem.setStyleName(Resources.INSTANCE.swrleditor().menuItemDecisionTree());
				else
					EditItem.setStyleName(Resources.INSTANCE.swrleditor().menuItemDecisionTreeDisable());

				popupMenuBarEdit.addItem(EditItem);


				if (labelClick.getNode().getRulesRelated()!=null && labelClick.getNode().getRulesRelated().size() > 0){
					Command viewRulesGraph = new Command() {
						@Override
						public void execute() {
							rightClickMenuEdit.hide();
							drawGraph();
						}

						// metodo responsavel por desenhar as linhas do grafo 
						// este metodo eh chamado pelo item do menu
						private void drawGraph(){
							if (graphAttributesNumber < graphAttributes.size()){

								DefaultNodeInRulesTreeLabel labelConnect;
								String labelText;
								int x1=0;
								int y1=0;
								int x2=0;
								int y2=0;
								boolean draw = false;

								//percorre todos os componentes do panel que contem a arvore
								for (int i = 0; i < panelTreeLabels.getWidgetCount(); i++){
									
									//teste para identificar so os nodos 
									if (panelTreeLabels.getWidget(i) instanceof DefaultNodeInRulesTreeLabel){
										labelConnect = (DefaultNodeInRulesTreeLabel) panelTreeLabels.getWidget(i);

										labelText = removesBrackets(labelConnect.getText());

										// Percorre todas as regras relaciondas com o no que recebeu o comando 
										for(String s: labelClick.getNode().getRulesRelated()){
											s = removesBrackets(s);

											//Testa se o no esta na lista dos nos do grafo
											// alem disso testa se o no ja esta ligado (Pai ou Filho)
											if (labelText.equals(s) && 
													testParents(s, labelClick.getNode().getParentNode(), labelClick.getNode().getChildren())) {


												//Calcula a origem (X1, Y1) da linha do grafo
												x1 = (labelClick.getAbsoluteLeft()+labelClick.getOffsetWidth())-canvasTree.getAbsoluteLeft()+3;
												y1 = labelClick.getAbsoluteTop()-canvasTree.getAbsoluteTop();

												//Calcula o destino (X2, Y2) da linha do grafo
												x2 = (labelConnect.getAbsoluteLeft()+labelConnect.getOffsetWidth())-canvasTree.getAbsoluteLeft()+3;
												y2 = labelConnect.getAbsoluteTop()-canvasTree.getAbsoluteTop();

												
												//Desenha um quadrado ao redor do no da origem
												drawQuad(graphAttributes.get(graphAttributesNumber).getColor(), 
														labelClick.getAbsoluteLeft()-canvasTree.getAbsoluteLeft(),
														labelClick.getAbsoluteTop()-canvasTree.getAbsoluteTop(),
														labelClick.getOffsetWidth(), labelClick.getOffsetHeight());

												//Desenha linha ligando dois nos
												drawCurvaQuad(graphAttributes.get(graphAttributesNumber).getColor(),
														graphAttributes.get(graphAttributesNumber).getxOffset(),
														graphAttributes.get(graphAttributesNumber).getyOffset(),
														x1, y1, x1, 10, x2, 10, x2, y2);

												
												
												draw = true;

												break;
											}
										}
									}
								}

								//Caso tenha desenhado a linha, incrementa graphAttributesNumber que identifica qual dos graphAttributes � usado
								if (draw)
									graphAttributesNumber++;
							}
						}
						
						//Metodo que remove colchetes do inicio de uma String
						// necessario pois os nodos possuem o numero de atomos em comum
						private String removesBrackets(String text){
							if (text.startsWith("["))
								return text.substring(text.indexOf("]")+1).trim();

							return text;
						}
						
						//Metodo que testa se o no ja nao eh Pai ou Filho, ou seja, nao precisa ligacao no grafo  
						private boolean testParents(String ruleName, NodeDecisionTree parent, List<NodeDecisionTree> children){

							if (ruleName.equals(removesBrackets(parent.getValue())))
								return false;

							for (NodeDecisionTree node : children)
								if (ruleName.equals(removesBrackets(node.getValue())))
									return false;

									return true;
						}

						//Desenha a linha do grafo
						private void drawCurvaQuad(Color color, int deslocx, int deslocy, int x1, int y1, int devx1, int devy1, int devx2, int devy2, int x2, int y2){

							y1 += deslocy; 
							devy1 += deslocy; 
							devy2 += deslocy; 
							y2 += deslocy; 

							canvasTree.beginPath();

							canvasTree.setStrokeStyle(color);

							canvasTree.moveTo(x1, y1);
							canvasTree.lineTo(x1+deslocx, y1);

							canvasTree.moveTo(x1+deslocx, y1);
							canvasTree.lineTo(devx1+deslocx, devy1);

							canvasTree.moveTo(devx1+deslocx, devy1);
							canvasTree.lineTo(devx2+deslocx, devy2);

							canvasTree.moveTo(devx2+deslocx, devy2);
							canvasTree.lineTo(x2+deslocx, y2);

							canvasTree.moveTo(x2+deslocx, y2);
							canvasTree.lineTo(x2, y2);

							canvasTree.closePath();
							canvasTree.stroke();
						}

						//Desenha o quadrado ao redor do n�
						private void drawQuad(Color color, int x, int y, int w, int h){

							canvasTree.beginPath();

							canvasTree.setStrokeStyle(color);

							canvasTree.moveTo(x, y);
							canvasTree.lineTo(x, y+h);

							canvasTree.moveTo(x, y+h);
							canvasTree.lineTo(x+w, y+h);

							canvasTree.moveTo(x+w, y+h);
							canvasTree.lineTo(x+w, y);

							canvasTree.moveTo(x+w, y);
							canvasTree.lineTo(x, y);

							canvasTree.closePath();
							canvasTree.stroke();
						}
					};


					MenuItem ViewGraphItem = new MenuItem("View Graph", true, viewRulesGraph);
					ViewGraphItem.setEnabled(true);
					ViewGraphItem.setStyleName(Resources.INSTANCE.swrleditor().menuItemDecisionTree());

					popupMenuBarEdit.addItem(ViewGraphItem);
				}

				popupMenuBarEdit.setVisible(true);

				rightClickMenuEdit.clear();
				rightClickMenuEdit.add(popupMenuBarEdit);

				rightClickMenuEdit.setPopupPosition(x, y);
				rightClickMenuEdit.show();
			} else {

				Command insertNewRule = new Command() {
					@Override
					public void execute() {
						rightClickMenuInsert.hide();

						String antecedent = "";
						NodeDecisionTree node = labelClick.getNode();
						while (node.getAtomType() !=  ATOM_TYPE.ROOT){

							if (antecedent.isEmpty())
								antecedent = node.getValue();
							else
								antecedent = antecedent + " ^ " + node.getValue();

							node = node.getParentNode();
						}

						presenter.goToNewRule(antecedent);
					}
				};

				MenuBar popupMenuBarInsert = new MenuBar(true);
				MenuItem insertItem = new MenuItem("Insert in New Rule", true, insertNewRule);

				insertItem.setEnabled(writePermission);
				if (writePermission)
					insertItem.setStyleName(Resources.INSTANCE.swrleditor().menuItemDecisionTree());
				else
					insertItem.setStyleName(Resources.INSTANCE.swrleditor().menuItemDecisionTreeDisable());

				popupMenuBarInsert.addItem(insertItem);
				popupMenuBarInsert.setVisible(true);
				rightClickMenuInsert.clear();
				rightClickMenuInsert.add(popupMenuBarInsert);


				rightClickMenuInsert.setPopupPosition(x, y);
				rightClickMenuInsert.show();
			}

	}

	private void moveMouseNodeTree(DefaultNodeInRulesTreeLabel label) {
		label.addStyleName(Resources.INSTANCE.swrleditor().pointerCursor());
	}

	private void exitMouseNodeTree(DefaultNodeInRulesTreeLabel label) {
		label.removeStyleName(Resources.INSTANCE.swrleditor().pointerCursor());
	}

	public String getAlgorithmName() {
		String result = "Default";
		if (listAlgorithm.getItemCount() != 0)
			result = listAlgorithm
			.getItemText(listAlgorithm.getSelectedIndex());

		return result;
	}

	@UiHandler("btnRefresh")
	void onBtnRefreshClick(ClickEvent event) {
		loadDecisionTree();
	}

	public void loadDecisionTree() {
		UtilLoading.showLoadDecisionTree();
		presenter.getDecisionTree(getAlgorithmName());
	}

	public void setListAlgorithm(List<String> list, Map<String, Object> config) {
		listAlgorithm.clear();
		String defaultAlg = Options.getStringOption(config,
				OptionsView.DefaultAlgorithmDecisionTreeStr, "");

		if (defaultAlg == null)
			defaultAlg = "";

		int count = 0;

		for (String algorithm : list) {
			if (Options.getBooleanOption(config, Options
					.removeCharInvalidForNameOptions(
							OptionsView.AlgorithmDecisionTreeStr_, algorithm),
							true)) {
				listAlgorithm.addItem(algorithm);

				if (defaultAlg.equals(algorithm))
					listAlgorithm.setSelectedIndex(count);

				count++;
			}
		}

		if (defaultAlg.equals(""))
			listAlgorithm.setSelectedIndex(0);
	}

	private class GraphAttributes{
		private Color color;
		private int xOffset;
		private int yOffset;


		public GraphAttributes(Color color, int xOffset, int yOffset) {
			super();
			this.color = color;
			this.xOffset = xOffset;
			this.yOffset = yOffset;
		}

		public Color getColor() {
			return color;
		}
		public int getxOffset() {
			return xOffset;
		}
		public int getyOffset() {
			return yOffset;
		}
	}

}
