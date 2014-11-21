package edu.stanford.smi.protegex.oboconverter.tabwidget;

import javafx.application.Platform;
import javafx.scene.web.WebEngine;

// JavaScript interface object
public class JavaApp {

	final WebEngine engine;
	private final OboConverterTab tab;

	public JavaApp(OboConverterTab tab, WebEngine engine) {
		this.engine = engine;
		this.tab = tab;
	}

	public String exit(int y) {
		System.out.println("Apertou "+y);
		engine.executeScript("myFunction()");
		//tab.outputArea.setText("Converting to OBO format ... (it may take several minutes)\n");


		new Thread(
				new Runnable () {
					@Override
					public void run() {
						try {
							Thread.sleep(10000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						Platform.runLater(new Runnable () {
							@Override
							public void run() {
								engine.executeScript("alert('From Java!')");
							}});
					}}
				).start();
		return "klklklkl";
	}
}
