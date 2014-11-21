package br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.visualization.decisiontree;

import java.util.ArrayList;
import java.util.List;

public class NodeTreeInt {

		int value;

		private List<NodeTreeInt> childNodes = new ArrayList<NodeTreeInt>();

		public NodeTreeInt(int value) {
			super();
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}

		public NodeTreeInt addChildNodes(NodeTreeInt node) {
			if (this.childNodes.add(node))
				return this.childNodes.get(this.childNodes.size() - 1);
			else
				return null;
		}

		public NodeTreeInt getChildNodes(int index) {
			return this.childNodes.get(index);
		}
	}