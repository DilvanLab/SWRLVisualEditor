package br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;

public abstract class WaitingCreateToRun implements Runnable {

    private final Widget componentForTest;
    private final int delayMillis;
    private Timer timer;

    public WaitingCreateToRun(Widget componentForTest, int delayMillis) {
	super();
	this.componentForTest = componentForTest;
	this.delayMillis = delayMillis;
    }

    public void start(){
	timer = new Timer() {
	    @Override
	    public void run() {

		if (componentForTest.getOffsetHeight() > 0 || componentForTest.getOffsetWidth() > 0){

		    //System.out.println("Done..");
		    WaitingCreateToRun.this.run();
		    cancel();

		    try {
			WaitingCreateToRun.this.finalize();
		    } catch (Throwable e) {
			e.printStackTrace();
		    }

		}else
		    schedule(delayMillis);
	    }
	};
	timer.schedule(delayMillis);
    }

    public void stop(){
	if (timer != null)
	    timer.cancel();
    }
}
