package br.usp.icmc.dilvan.swrlEditor.client.gwtWindow;

public class Window {

    public static void alert(String msg) {
	AlertDialog dialog = new AlertDialog();
	dialog.setText("Alert!");
	dialog.setMsg(msg);
	dialog.show();
    }

    public static void confirm(String msg, Runnable cbk) {
	ConfirmDialog dialog = new ConfirmDialog();
	dialog.setText("Confirm:");
	dialog.setMsg(msg);
	dialog.setCallback(cbk);
	dialog.show();
    }

    static final int posX = 50;

    static final int posY = 50;
}


