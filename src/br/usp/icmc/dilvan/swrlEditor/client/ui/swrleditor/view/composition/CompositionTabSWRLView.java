package br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.composition;

import java.util.List;

import br.usp.icmc.dilvan.swrlEditor.client.resources.Resources;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Atom;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.AtomImpl;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Rule;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Atom.TYPE_ATOM;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.UtilView;
import br.usp.icmc.dilvan.swrlEditor.client.ui.swrleditor.view.VisualizationView.TYPE_VIEW;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.BlurEvent;

public class CompositionTabSWRLView extends Composite implements
		CompositionTabView {
	private Presenter presenter;
	private static final Binder binder = GWT.create(Binder.class);
	@UiField
	RichTextArea swrlAntecedent;
	@UiField
	RichTextArea swrlConsequent;

	// TODO retirar essa forma de formatar
	private final String STYLE_RULE = "font-family: courier new; font-size: 8pt; padding: 1px; font-weight: bold;";
	private final String STYLE_CLASS = "color: #FFA500;";
	private final String STYLE_OBJECTPROPERTY = "color: #1874CD;";
	private final String STYLE_DATATYPEPROPERTY = "color: #32CD32;";
	private final String STYLE_BUILTIN = "color: #CD3700;";
	private final String STYLE_VARIABLE = "font-weight: bold;";
	private final String STYLE_DATATYPE = "color: #7D26CD;";

	private Rule rule;
	private TYPE_VIEW typeView;

	private boolean swrlAntecedentFocused;
	private boolean swrlConsequentFocused;

	interface Binder extends UiBinder<Widget, CompositionTabSWRLView> {
	}

	public CompositionTabSWRLView() {
		initWidget(binder.createAndBindUi(this));
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void setRule(Rule r) {
		this.rule = r;
		loadAndShowRule();
	}

	@Override
	public void setNewRule(Rule r) {
		swrlAntecedent.setHTML("");
		swrlConsequent.setHTML("");
		this.rule = r;
	}
	
	@Override
	public void setTypeView(TYPE_VIEW typeView) {
		this.typeView = typeView;
	}

	private void loadAndShowRule() {
		if (rule != null) {
			if (swrlAntecedentFocused) {
				String html = getHTMLFormat(rule.getAntecedent());
				if (!html.trim().equals(swrlAntecedent.getHTML()))
					swrlAntecedent.setHTML(html);
			} else
				swrlAntecedent.setHTML(getHTMLFormat(rule.getAntecedent()));

			if (swrlConsequentFocused) {
				String html = getHTMLFormat(rule.getConsequent());
				if (!html.trim().equals(swrlConsequent.getHTML()))
					swrlConsequent.setHTML(html);
			} else
				swrlConsequent.setHTML(getHTMLFormat(rule.getConsequent()));
		}
	}

	private String getHTMLFormat(List<Atom> atoms) {
		String aux = UtilView
				.getAtomsHightlights(atoms, typeView)
				.replace(
						"class=\"" + Resources.INSTANCE.swrleditor().swrlRule()
								+ "\"", "style=\"" + STYLE_RULE + "\"")
				.replace(
						"class=\""
								+ Resources.INSTANCE.swrleditor().atom_CLASS()
								+ "\"", "style=\"" + STYLE_CLASS + "\"")
				.replace(
						"class=\""
								+ Resources.INSTANCE.swrleditor()
										.atom_INDIVIDUAL_PROPERTY() + "\"",
						"style=\"" + STYLE_OBJECTPROPERTY + "\"")
				.replace(
						"class=\""
								+ Resources.INSTANCE.swrleditor()
										.atom_DATAVALUE_PROPERTY() + "\"",
						"style=\"" + STYLE_DATATYPEPROPERTY + "\"")
				.replace(
						"class=\""
								+ Resources.INSTANCE.swrleditor()
										.atom_BUILTIN() + "\"",
						"style=\"" + STYLE_BUILTIN + "\"")
				.replace(
						"class=\""
								+ Resources.INSTANCE.swrleditor()
										.param_IVARIABLE() + "\"",
						"style=\"" + STYLE_VARIABLE + "\"")
				.replace(
						"class=\""
								+ Resources.INSTANCE.swrleditor()
										.param_DVARIABLE() + "\"",
						"style=\"" + STYLE_VARIABLE + "\"")
				.replace(
						"class=\""
								+ Resources.INSTANCE.swrleditor()
										.param_DATALITERAL() + "\"",
						"style=\"" + STYLE_DATATYPE + "\"")
				.replace(
						"class=\""
								+ Resources.INSTANCE.swrleditor()
										.param_INDIVIDUALID() + "\"",
						"style=\"" + STYLE_DATATYPE + "\"");

		return aux;
	}

	@UiHandler("swrlAntecedent")
	void onSwrlAntecedentFocus(FocusEvent event) {
		swrlAntecedentFocused = true;

	}

	@UiHandler("swrlConsequent")
	void onSwrlConsequentFocus(FocusEvent event) {
		swrlConsequentFocused = true;
	}

	@UiHandler("swrlAntecedent")
	void onSwrlAntecedentBlur(BlurEvent event) {
		String newRule = swrlAntecedent.getText() + " -> "
				+ swrlConsequent.getText();
		if (rule != null)
			if (!rule.toString().replace(" ", "")
					.equals(newRule.replace(" ", ""))) {
				presenter.setRuleString(newRule);
			}
		swrlAntecedentFocused = false;
	}

	@UiHandler("swrlConsequent")
	void onSwrlConsequentBlur(BlurEvent event) {
		String newRule = swrlAntecedent.getText() + " -> "
				+ swrlConsequent.getText();
		if (rule != null)
			if (!rule.toString().replace(" ", "")
					.equals(newRule.replace(" ", ""))) {
				presenter.setRuleString(newRule);
			}
		swrlConsequentFocused = false;
	}

	@Override
	public void addAtom(final Atom atom, final boolean isAntecedent) {

		Timer timer = new Timer() {
			@Override
			public void run() {

				if (!presenter.isBlockedAlterRule()) {

					if (isAntecedent)
						rule.addAntecedent(atom);
					else
						rule.addConsequent(atom);

					loadAndShowRule();
					presenter.getErrors();

				} else
					schedule(10);
			}
		};
		timer.schedule(10);
	}

	@Override
	public void addPredicate(final TYPE_ATOM type, final String predicate,
			final boolean isAntecedent) {
		Timer timer = new Timer() {
			@Override
			public void run() {

				if (!presenter.isBlockedAlterRule()) {

					Atom newAtom = new AtomImpl();
					newAtom.setAtomType(type);
					newAtom.setPredicateID(predicate);

					if (isAntecedent)
						rule.addAntecedent(newAtom);
					else
						rule.addConsequent(newAtom);

					loadAndShowRule();
					presenter.getErrors();

				} else
					schedule(10);
			}
		};
		timer.schedule(10);

	}

}
