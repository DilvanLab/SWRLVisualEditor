package br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.util;

public class UtilLoading {

    public static void hide(){
	//UIUtil.hideLoadProgessBar();
    }

    public static void show(String message, String title){
	//UIUtil.showLoadProgessBar(message, title);
    }

    public static void showLoadDecisionTree(){
	show("Loading Decision Tree", "Loading");
    }

    public static void showLoadGroups(){
	show("Loading Groups", "Loading");
    }

    public static void showLoadRules(){
	show("Loading Rules", "Loading");
    }

    public static void showLoadSWRLEditor(){
	show("Loading SWRLEditor", "Loading");
    }
}
