package br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.visualization.decisiontree;

import br.usp.icmc.dilvan.swrlEditor.client.resources.Resources;
import br.usp.icmc.dilvan.swrlEditor.client.resources.UtilResource;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.decisiontree.NodeDecisionTree;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.decisiontree.NodeDecisionTree.ATOM_TYPE;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

public class DefaultNodeInRulesTreeLabel extends HTML implements NodeLabelNotifier{
		private NodeDecisionTree node;
		
		private NodeLabelListener listener = null;
		
		private TimerToolTip waitToolTip = null;

		public DefaultNodeInRulesTreeLabel(String label, NodeDecisionTree node) {
			super(label);
			this.node = node;

			this.addStyleName(Resources.INSTANCE.swrleditor().swrlRule());
			if ((this.node.getAtomType() != ATOM_TYPE.ROOT)
					&& (this.node.getAtomType() != ATOM_TYPE.CONSEQUENT))
				this.addStyleName(UtilResource.getCssTypeNodeAtom(this.node
						.getAtomType()));

			this.addStyleName(Resources.INSTANCE.swrleditor().whiteBackground());
			
			sinkEvents(Event.ONMOUSEUP | Event.MOUSEEVENTS | Event.ONCONTEXTMENU); 
		}

		public DefaultNodeInRulesTreeLabel(NodeDecisionTree node) {
			this(node.getValue(), node);
		}

		public NodeDecisionTree getNode() {
			return node;
		}

		@SuppressWarnings("deprecation")
		@Override
		public void onBrowserEvent(Event event) {
			  event.cancelBubble(true);//This will stop the event from being propagated
			  event.preventDefault();
			
			switch (DOM.eventGetType(event)) {
		        case Event.ONMOUSEUP:
		        	if (DOM.eventGetButton(event) == Event.BUTTON_LEFT) {
		        		if (listener != null)
		        			listener.onClick(this, event);
		        	}
		        	if (DOM.eventGetButton(event) == Event.BUTTON_RIGHT) {
		        		if (listener != null)
		        			listener.onRightClick(this, event);
		        	}
		        	break;
		        case Event.ONMOUSEMOVE:
		          	if (listener != null){
		          		listener.onMouseMove(this, event);
		          		if (waitToolTip == null)
		          			waitToolTip = new TimerToolTip(this, event);
		          		
		          	}
		        	break;
		        case Event.ONMOUSEOUT:
		          	if (listener != null){
		          		listener.onMouseMove(this, event);
		          		if (waitToolTip != null){
		          			waitToolTip.cancel();
		          			waitToolTip = null;
		          		}
		          	}
		        	break;
		        	
		        case Event.ONCONTEXTMENU:
		            GWT.log("Event.ONCONTEXTMENU", null);

		            break;
		            
		        default: 
	        }
		}
		
		@Override
		public void addMouseListener(NodeLabelListener listener) {
			this.listener = listener;
		}

		@Override
		public void removeMouseListener(NodeLabelListener listener) {
			this.listener = null;
		}
		
		private class TimerToolTip extends Timer{
			
			private Widget sender;
			private Event event;

			private TimerToolTip(Widget sender, Event event) {
				super();
				this.sender = sender;
				this.event = event;
				
				this.schedule(600);
				
				
			}

			@Override
			public void run() {
				if (listener != null)
					listener.onShowToolTip(sender, event);
			}
			
		}
		
	}