/*
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License");  you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * http//www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 *
 * The Original Code is OBO CONVERTER TAB.
 *
 * OBO CONVERTER TAB was developed by Dilvan Moreira at the SMI-Stanford University
 * (http//www.smi.stanford.edu) at the Stanford University School of Medicine
 * with support from CAPES - http://www.capes.gov.br
 *
 * @author      Dilvan Moreira
 * @version     1.0
 * @since       1.0
 */

package edu.stanford.smi.protegex.oboconverter.tabwidget;

import java.awt.BorderLayout;
import java.io.File;
import java.util.Collection;
import java.util.Date;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.web.PopupFeatures;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import javafx.util.Callback;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import netscape.javascript.JSObject;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.Widget;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protege.model.Project;
import edu.stanford.smi.protege.resource.Icons;
import edu.stanford.smi.protege.ui.HeaderComponent;
import edu.stanford.smi.protege.util.ComponentFactory;
import edu.stanford.smi.protege.util.SelectionEvent;
import edu.stanford.smi.protege.util.SelectionListener;
import edu.stanford.smi.protege.util.WaitCursor;
import edu.stanford.smi.protege.widget.InstanceClsesPanel;
import edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.ui.properties.OWLPropertyHierarchiesPanel;
import edu.stanford.smi.protegex.owl.ui.widget.AbstractTabWidget;

class Browser {

	private static final Logger log = Logger.getLogger(Browser.class.getName() );
	
	public class JavaApp {

		public void openClsPropBrowsers() {
			//tab.subpane.setDividerLocation(WIDTH/2);
			tab.pane.setDividerLocation(0.25);
//			Platform.runLater(new Runnable() {
//				@Override
//				public void run() {
//					tab.subpane.setDividerLocation(0.5);
//				}
//			});
			SwingUtilities.invokeLater(new Runnable() {
			    public void run() {
			    	tab.subpane.setDividerLocation(0.5);
			    }
			  });
				
			log.info("JAVA APP1");
		}
		
		public void closeClsPropBrowsers() {
			tab.pane.setDividerLocation(1);
			log.info("JAVA APP2");
		}
	}
	
	final WebView browser = new WebView();

	//WebEngine webEngine;

	final WebEngine webEngine = browser.getEngine();

	OboConverterTab tab;
	
	public Browser(OboConverterTab tab1) {
		
		tab = tab1;
		
		//browser = ;

		//webEngine = browser.getEngine();
		//apply the styles
		//getStyleClass().add("browser");
		// load the web page
		webEngine.load("http://127.0.0.1:8080/SwrlGWT.html");//file:///Users/dilvan/Dropbox/workspace/testjavafxBrowser/index.html");
		//webEngine.load("file:///Users/dilvan/Dropbox/workspace/testjavafxBrowser/index.html");
		//webEngine.loadContent("index.html");
		//add the web view to the scene
		// process page loading
		webEngine.getLoadWorker().stateProperty().addListener(
				new ChangeListener<State>() {
					@Override
					public void changed(ObservableValue<? extends State> ov,
							State oldState, State newState) {
						//toolBar.getChildren().remove(showPrevDoc);
						if (newState == State.SUCCEEDED) {
							//if (firstRun) {
								//webEngine.load("http://127.0.0.1:8080/SwrlGWT.html"); // file);
							
							JSObject win1 =
									(JSObject) webEngine.executeScript("window");
							win1.setMember("app", new JavaApp());
							//if (needDocumentationButton) {
							//    toolBar.getChildren().add(showPrevDoc);
							//}
							
							webEngine.setOnAlert(new EventHandler<WebEvent<String>>() {
								@Override
								public void handle(WebEvent<String> arg) {
									System.out.println("Alert1: "+arg.getData());
								}});
						}
					}
				}
				);
		//getChildren().add(browser);
	}
}

/**
 *
 * @author Dilvan Moreira
 * @version 1.0
 */
@SuppressWarnings("serial")
class BrowserPanel extends JPanel {

	BrowserPanel(String projectName, JFXPanel fxPanel) {
		setLayout(new BorderLayout());
		add(fxPanel, BorderLayout.CENTER);
		add(createClsBrowserHeader(projectName), BorderLayout.NORTH);
	}

	HeaderComponent createClsBrowserHeader(String projectName) {
		JLabel label = ComponentFactory.createLabel(projectName,
				Icons.getProjectIcon(), SwingConstants.LEFT);
		// String forProject =
		// LocalizedText.getText(ResourceKey.CLASS_BROWSER_FOR_PROJECT_LABEL);
		// String classBrowser =
		// LocalizedText.getText(ResourceKey.CLASS_BROWSER_TITLE);
		return new HeaderComponent("SWRL Editor", "For Project", label);
	}
}

public class OboConverterTab extends AbstractTabWidget {
	
	private static final Logger log = Logger.getLogger(OboConverterTab.class.getName() );
	
	public static KnowledgeBase getKb() {
		if (kb == null)
			throw new RuntimeException("Null Knowledge Base.");
		log.info("not null kb");
		return kb;
	}

	/**
	 * Test if project is suitable (OWL project).
	 */
	public static boolean isSuitable(Project project, Collection errors) {
		if (!(project.getKnowledgeBase() instanceof OWLModel)) {
			errors.add(new String(
					"This tab can only be used with OWL projects."));
			return false;
		}
		return true;
	}

	/**
	 *
	 */
	private static final long serialVersionUID = -2972887629157008554L;

	//private InstanceClsesPanel _clsesPanel;

	//private boolean firstRun = true;

	// protected JComponent createDirectInstancesList() {
	// _directInstancesList = new DirectInstancesList(getProject());
	// _directInstancesList.addSelectionListener(new SelectionListener() {
	// public void selectionChanged(SelectionEvent event) {
	// Collection selection = _directInstancesList.getSelection();
	// Instance selectedInstance;
	// if (selection.size() == 1) {
	// selectedInstance = (Instance)
	// CollectionUtilities.getFirstItem(selection);
	// } else {
	// selectedInstance = null;
	// }
	// _instanceDisplay.setInstance(selectedInstance);
	// _directTypesList.setInstance(selectedInstance);
	// }
	// });
	// setInstanceSelectable((Selectable)
	// _directInstancesList.getDragComponent());
	// return _directInstancesList;
	// }

	// protected JComponent createDirectTypesList() {
	// _directTypesList = new DirectTypesList(getProject());
	// return _directTypesList;
	// }

	// protected JComponent createInstanceDisplay() {
	// return new InstanceDisplay(getProject());
	// }

	// private JComponent createInstancesPanel() {
	// JSplitPane panel = ComponentFactory.createTopBottomSplitPane();
	// panel.setTopComponent(createDirectInstancesList());
	// panel.setBottomComponent(createDirectTypesList());
	// return panel;
	// }

	private static KnowledgeBase kb;

	// public LabeledComponent getLabeledComponent() {
	// return _clsesPanel.getLabeledComponent();
	// }

	//WebView browser;

	//WebEngine engine;

	JSplitPane pane;
	
    Browser browser1;

    JSplitPane subpane;

	// public void setSelectedInstance(Instance instance) {
	// _clsesPanel.setSelectedCls(instance.getDirectType());
	// _directInstancesList.setSelectedInstance(instance);
	// _directTypesList.setInstance(instance);
	// }

	// private void setupDragAndDrop() {
	// DragSource.getDefaultDragSource().createDefaultDragGestureRecognizer(_directInstancesList.getDragComponent(),
	// DnDConstants.ACTION_COPY_OR_MOVE, new
	// InstancesTabDirectInstancesListDragSourceListener());
	// new DropTarget(_clsesPanel.getDropComponent(),
	// DnDConstants.ACTION_COPY_OR_MOVE, new InstanceClsesTreeTarget());
	// }
	
	

	private JComponent createClsesPanel() {
		InstanceClsesPanel clsPanel = new InstanceClsesPanel(getProject());
		clsPanel.addSelectionListener(new SelectionListener() {
			@Override
			public void selectionChanged(SelectionEvent event) {
				log.info(event.toString());
				Object obj= ((InstanceClsesPanel) event.getSource()).getSelection().iterator().next();
				if (obj instanceof OWLNamedClass) {
					fireResourceChosenEvent("Class", (OWLNamedClass) obj);
				}

				//transmitSelection();
			}
		});
		return clsPanel;
	}

	private JComponent createClsSplitter() {
		pane = createLeftRightSplitPane(
				"SWRLEditorTab.right.left_right", 250);
		subpane = createInstanceSplitter();
		pane.setLeftComponent(subpane);
		pane.setRightComponent(createSWRLPanel());
		//pane.setDividerLocation(10);
		return pane;
	}

	// public DirectInstancesList getDirectInstancesList() {
	// return _directInstancesList;
	// }
	// }

	private JSplitPane createInstanceSplitter() {
		JSplitPane pane = createLeftRightSplitPane("SWRLEditorTab.left_right", 250);
		pane.setLeftComponent(createClsesPanel());
		pane.setRightComponent(createPropertiesPanel());
		//pane.setDividerLocation(Browser.WIDTH/2);
		return pane;
	}
	
	//class LIstner implements
	private JComponent createPropertiesPanel() {
		OWLPropertyHierarchiesPanel propPanel = new OWLPropertyHierarchiesPanel(
				getOWLModel());
		propPanel.addSelectionListener(new SelectionListener() {
			@Override
			public void selectionChanged(SelectionEvent event) {
				log.info("lll= "+((OWLPropertyHierarchiesPanel)event.getSelectable()));
				log.info("l2= "+((OWLPropertyHierarchiesPanel)event.getSelectable()).isValid());
				//((OWLPropertyHierarchiesPanel)event.getSelectable()).clearSelection();
							
				//if (!((OWLPropertyHierarchiesPanel)event.getSelectable()).isValid()) {
				//	return;
				//}
				
				Object obj= ((OWLPropertyHierarchiesPanel) event.getSource()).getSelection().iterator().next();
				if (obj==null) return;
				if (obj instanceof OWLObjectProperty) {
					fireResourceChosenEvent("ObjectProperty", (OWLObjectProperty) obj);
				} else if (obj instanceof OWLDatatypeProperty) {
					fireResourceChosenEvent("DatatypeProperty", (OWLDatatypeProperty) obj);
				} else return;
				  
				log.info(obj.toString());

				//transmitSelection();
			}
		});
		return propPanel;
	}

	JComponent createSWRLPanel() {

		final JFXPanel fxPanel = new JFXPanel();

		Platform.runLater(new Runnable() {
				
			@Override
			public void run() {
				
				while (!servletOn);
				// Important! Once platform exits we cannot create
				// more javafx components
				Platform.setImplicitExit(false);
				
				// System.out.println(new Date());
				
				// This method is invoked on JavaFX thread
				//browser = new WebView();
				//engine = browser.getEngine();

				// browser.getEngine().load("file:///Users/dilvan/IdeaProjects/swrlEditor/Test.html");
				//String file = "file://" + new File(".").getAbsolutePath()
				//		+ "/war/test.html";
				//System.out.println(file);
				// browser.getEngine().load("http://uol.com.br");
				//engine.load(file); // "http://127.0.0.1:8080/SwrlGWT.html");
				//engine.load("http://127.0.0.1:8080/SwrlGWT.html");
				// //file);

				// final JSObject win = (JSObject)
				// engine.executeScript("window");
				// win.setMember("app", new JavaApp(engine));
				
				browser1 = new Browser(OboConverterTab.this);

				Scene scene = new Scene(browser1.browser);//,750,500, Color.web("#666970"));
				
				//Scene scene = new Scene(browser); // ,750,500,
				// Color.web("#666970"));
				fxPanel.setScene(scene);

				/*
				// process page loading
				engine.getLoadWorker().stateProperty()
				   .addListener(new ChangeListener<State>() {
					@Override
					public void changed(ObservableValue<? extends State> ov,
							State oldState, State newState) {
						if (newState == State.SUCCEEDED) {
							
							JSObject win = (JSObject) engine.executeScript("window");
					        win.setMember("app", new JavaApp());
					
							if (true) {//firstRun) {
								//engine.load("http://127.0.0.1:8080/SwrlGWT.html"); // file);
								
								
								engine.setOnAlert(new EventHandler<WebEvent<String>>() {
									@Override
									public void handle(WebEvent<String> arg) {
										System.out.println("Alert1: "+arg.getData());
										pane.setDividerLocation(10);
										
									}});
//								
//								engine.setCreatePopupHandler(new Callback<PopupFeatures, WebEngine>(){
//									@Override
//									public WebEngine call(PopupFeatures arg0) {
//										arg0.
//										return null;
//									}});
								
								//JSObject win = (JSObject) engine.executeScript("window");
								//win.setMember("treesCtrl", new TreesCtrl(OboConverterTab.this));
								
								//JSObject win = (JSObject) engine.executeScript("window");
			                    //win.setMember("app", new JavaApp());
								
								firstRun = false;
							}
					}
				}});*/
			}
		});
		return new BrowserPanel(getProject().getName(), fxPanel);
	}

	volatile int servletPort = -1;
	/**
	 * Startup code.
	 */
	@Override
	public void initialize() {

		log.info("Init: Begins.");
		kb = getKnowledgeBase();
		if (kb == null) {
			log.severe("Init: Null Knowledge Base.");
			throw new RuntimeException("Init SWRL Editor: Null Knowledge Base.");
		}
		log.info("Init: Kb ok. " + kb.toString());
		
		if (!servletOn)
			initServlet();
		setIcon(Icons.getViewClsIcon());// .getInstanceIcon());
		// setLabel(LocalizedText.getText(ResourceKey.INSTANCES_VIEW_TITLE));
		setLabel("SWRL Editor");
		add(createClsSplitter());
		//transmitSelection();
		// setupDragAndDrop();
		//setClsTree(_clsesPanel.getDropComponent());
	}

	void fireResourceChosenEvent(final String type, final RDFResource rsr) {	
		log.info(rsr.getPrefixedName());
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				browser1.webEngine.executeScript("window.resourceChosenEvent('"+ type + "', '" + rsr.getPrefixedName() +"');");
				//"alert(\"" + cls.getURI() + "\");");
			}
		});
	}
	
	volatile static boolean servletOn = false;
	
	void initServlet() {
		

		new Thread(new Runnable() {
			@Override
			public void run() {
				Server server = new Server(8080);
				servletPort = server.getConnectors()[0].getLocalPort();
				
				WebAppContext webapp = new WebAppContext();
				webapp.setClassLoader(OboConverterTab.this.getClass()
						.getClassLoader());
				webapp.setContextPath("/");
				String file = new File(".").getAbsolutePath() + "/war";
				log.info(file);
				webapp.setWar(file);
				// "../../tests/test-webapps/test-jetty-webapp/target/test-jetty-webapp-9.0.0-SNAPSHOT.war");
				server.setHandler(webapp);
				// HashLoginService loginService = new HashLoginService();
				// loginService.setName("Test Realm");
				// loginService.setConfig("src/test/resources/realm.properties");
				// server.addBean(OboConverterTab.this);

				try {
					server.start();
					Thread.sleep(1000);
					servletOn = true;
					server.join();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

//	public void setSelectedCls(Cls cls) {
//		_clsesPanel.setSelectedCls(cls);
//	}

//	protected void transmitSelection() {
//		WaitCursor cursor = new WaitCursor(this);
//		try {
//			Collection selection = _clsesPanel.getSelection();
//			transmitSelection(selection);
//		} finally {
//			cursor.hide();
//		}
//	}
//
//	protected void transmitSelection(Collection selection) {
//		//getRootPane().
//		//JOptionPane.showMessageDialog(frame, "Eggs are not supposed to be green.");
//		// _directInstancesList.setClses(selection);
//	}
}

