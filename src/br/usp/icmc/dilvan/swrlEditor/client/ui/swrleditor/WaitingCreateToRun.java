package br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;

public abstract class WaitingCreateToRun implements Runnable {

	private Widget componentForTest;
	private int delayMillis;
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
				
				//System.out.println("Tentou........");
				
				if ((WaitingCreateToRun.this.componentForTest.getOffsetHeight() > 0) || (WaitingCreateToRun.this.componentForTest.getOffsetWidth() > 0)){

					//System.out.println("Feito..");
					
					WaitingCreateToRun.this.run();
					this.cancel();

					try {
						WaitingCreateToRun.this.finalize();
					} catch (Throwable e) {
						e.printStackTrace();
					}
					
				}else
					schedule(WaitingCreateToRun.this.delayMillis);
			}
		};
		timer.schedule(delayMillis);
	}
	
	public void stop(){
		if (timer != null)
			timer.cancel();
	}
	
}
