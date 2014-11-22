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

package br.usp.icmc.dilvan.swrlEditor.swrlEditorTab;

import java.awt.BorderLayout;
import java.io.File;
import java.util.Collection;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import netscape.javascript.JSObject;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protege.model.Project;
import edu.stanford.smi.protege.resource.Icons;
import edu.stanford.smi.protege.ui.HeaderComponent;
import edu.stanford.smi.protege.util.ComponentFactory;
import edu.stanford.smi.protege.util.SelectionEvent;
import edu.stanford.smi.protege.util.SelectionListener;
import edu.stanford.smi.protege.widget.InstanceClsesPanel;
import edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.ui.properties.OWLPropertyHierarchiesPanel;
import edu.stanford.smi.protegex.owl.ui.widget.AbstractTabWidget;

class Browser {

    public class JavaApp {

	public void closeClsPropBrowsers() {
	    tab.pane.setDividerLocation(1);
	    log.info("closeClsPropBrowsers");
	}

	public void openClsPropBrowsers() {
	    tab.pane.setDividerLocation(0.25);
	    SwingUtilities.invokeLater(new Runnable() {
		@Override
		public void run() {
		    tab.subpane.setDividerLocation(0.5);
		}
	    });
	    log.info("openClsPropBrowsers");
	}
    }

    private static final Logger log = Logger.getLogger(Browser.class.getName() );

    final WebView browser = new WebView();
    final WebEngine webEngine = browser.getEngine();
    SWRLEditorTab tab;

    public Browser(SWRLEditorTab tab1) {
	tab = tab1;

	// load the web page
	webEngine.load("http://127.0.0.1:"+ SWRLEditorTab.servletPort + "/SwrlGWT.html");

	// process page loading
	webEngine.getLoadWorker().stateProperty().addListener(
		new ChangeListener<State>() {
		    @Override
		    public void changed(ObservableValue<? extends State> ov,
			    State oldState, State newState) {
			if (newState == State.SUCCEEDED) {
			    JSObject win1 =
				    (JSObject) webEngine.executeScript("window");
			    win1.setMember("app", new JavaApp());
			    webEngine.setOnAlert(new EventHandler<WebEvent<String>>() {
				@Override
				public void handle(WebEvent<String> arg) {
				    System.out.println("Alert: "+arg.getData());
				}
			    });
			}
		    }
		});
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
	return new HeaderComponent("SWRL Editor", "For Project", label);
    }
}

@SuppressWarnings("serial")
public class SWRLEditorTab extends AbstractTabWidget {

    public static KnowledgeBase getKb() {
	if (kb == null)
	    throw new RuntimeException("Null Knowledge Base.");
	log.info("not null kb");
	return kb;
    }

    /**
     * Test if project is suitable (OWL project).
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static boolean isSuitable(Project project, Collection errors) {
	if (!(project.getKnowledgeBase() instanceof OWLModel)) {
	    errors.add(new String(
		    "This tab can only be used with OWL projects."));
	    return false;
	}
	return true;
    }

    private static final Logger log = Logger.getLogger(SWRLEditorTab.class.getName() );
    private static KnowledgeBase kb;
    JSplitPane pane;

    Browser browser1;
    JSplitPane subpane;

    volatile static int servletPort = -1;

    //volatile static boolean servletOn = false;

    private JComponent createClsesPanel() {
	InstanceClsesPanel clsPanel = new InstanceClsesPanel(getProject());
	clsPanel.addSelectionListener(new SelectionListener() {
	    @Override
	    public void selectionChanged(SelectionEvent event) {
		log.info(event.toString());
		Object obj= ((InstanceClsesPanel) event.getSource()).getSelection().iterator().next();
		if (obj instanceof OWLNamedClass)
		    fireResourceChosenEvent("Class", (OWLNamedClass) obj);
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
	return pane;
    }

    private JSplitPane createInstanceSplitter() {
	JSplitPane pane = createLeftRightSplitPane("SWRLEditorTab.left_right", 250);
	pane.setLeftComponent(createClsesPanel());
	pane.setRightComponent(createPropertiesPanel());
	return pane;
    }

    //class Listner implements
    private JComponent createPropertiesPanel() {
	OWLPropertyHierarchiesPanel propPanel = new OWLPropertyHierarchiesPanel(
		getOWLModel());
	propPanel.addSelectionListener(new SelectionListener() {
	    @Override
	    public void selectionChanged(SelectionEvent event) {
		log.info("l1= "+event.getSelectable());
		log.info("l2= "+((OWLPropertyHierarchiesPanel)event.getSelectable()).isValid());

		Object obj= ((OWLPropertyHierarchiesPanel) event.getSource()).getSelection().iterator().next();
		if (obj==null) return;
		if (obj instanceof OWLObjectProperty)
		    fireResourceChosenEvent("ObjectProperty", (OWLObjectProperty) obj);
		else if (obj instanceof OWLDatatypeProperty)
		    fireResourceChosenEvent("DatatypeProperty", (OWLDatatypeProperty) obj);
		else return;

		log.info(obj.toString());
	    }
	});
	return propPanel;
    }

    JComponent createSWRLPanel() {

	final JFXPanel fxPanel = new JFXPanel();

	Platform.runLater(new Runnable() {
	    @Override
	    public void run() {
		while (servletPort == -1);
		// Important! Once platform exits we cannot create
		// more javafx components
		Platform.setImplicitExit(false);

		browser1 = new Browser(SWRLEditorTab.this);
		Scene scene = new Scene(browser1.browser);//,750,500, Color.web("#666970"));
		fxPanel.setScene(scene);
	    }
	});
	return new BrowserPanel(getProject().getName(), fxPanel);
    }

    void fireResourceChosenEvent(final String type, final RDFResource rsr) {
	log.info(rsr.getPrefixedName());
	Platform.runLater(new Runnable() {
	    @Override
	    public void run() {
		browser1.webEngine.executeScript("window.resourceChosenEvent('"+ type + "', '" + rsr.getPrefixedName() +"');");
	    }
	});
    }

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

	if (servletPort == -1)
	    initServlet();
	setIcon(Icons.getViewClsIcon());// .getInstanceIcon());
	setLabel("SWRL Editor");
	add(createClsSplitter());
    }

    void initServlet() {

	new Thread(new Runnable() {
	    @Override
	    public void run() {
		Server server = new Server(0);

		WebAppContext webapp = new WebAppContext();
		webapp.setClassLoader(SWRLEditorTab.this.getClass().getClassLoader());
		webapp.setContextPath("/");
		String file = new File(".").getAbsolutePath();
		if (file.endsWith("SWRLVisualEditor/."))
		    file = file + "/war";
		else
		    file = file + "/plugins/br.usp.icmc.dilvan.swrlEditor/war";
		log.info("War folder: "+ file);
		webapp.setWar(file);
		server.setHandler(webapp);

		try {
		    server.start();
		    Thread.sleep(1000);
		    servletPort = server.getConnectors()[0].getLocalPort();
		    log.info("port= " + servletPort);
		    //servletOn = true;
		    server.join();
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	}).start();
    }
}

