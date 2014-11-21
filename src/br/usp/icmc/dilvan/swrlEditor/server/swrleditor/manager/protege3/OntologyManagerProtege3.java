package br.usp.icmc.dilvan.swrlEditor.server.swrleditor.manager.protege3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import br.usp.icmc.dilvan.swrlEditor.server.swrleditor.manager.OntologyManager;
import br.usp.icmc.dilvan.swrlEditor.server.swrleditor.manager.SWRLManager;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.model.NamespaceUtil;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.model.RDFSDatatype;
import edu.stanford.smi.protegex.owl.model.impl.DefaultRDFSLiteral;
//import edu.stanford.bmir.protege.web.client.model.event.OntologyEvent;
//import edu.stanford.bmir.protege.web.server.Protege3ProjectManager;
//import edu.stanford.bmir.protege.web.server.ServerProject;

public class OntologyManagerProtege3 implements OntologyManager {

	private final JenaOWLModel owlModel;
	private final SWRLManager swrlManager;
	//private ServerProject<Project> serverProject;
	private final String projectName;

	//	public OntologyManagerProtege3(String projectName) {
	public OntologyManagerProtege3(KnowledgeBase kb, String projectName) {
		//serverProject = Protege3ProjectManager.getProjectManager().getServerProject(projectName, false);
		//owlModel = (JenaOWLModel) serverProject.getProject()
		//		.getKnowledgeBase();
		if (kb==null) throw new RuntimeException("Null Knowledge Base.");
		owlModel = (JenaOWLModel) kb;
		swrlManager = new SWRLManagerProtege3(this);
		this.projectName = projectName;

	}

	//	public List<OntologyEvent> getEvents(long fromVersion) {
	//		if (serverProject == null)
	//			throw new RuntimeException("Could not get ontology: " + projectName
	//					+ " from server.");
	//		return serverProject.isLoaded() ? serverProject.getEvents(fromVersion)
	//				: null;
	//	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getIDsForLabel(String label, boolean returnFirst) {

		List<String> result = new ArrayList<String>();

		for (RDFSClass cls : (Collection<RDFSClass>) owlModel.getRDFSClasses()) {
			for (Object lbl : (Collection<?>) cls.getLabels()) {
				if (lbl instanceof DefaultRDFSLiteral){
					if (((DefaultRDFSLiteral)lbl).getBrowserText().equals(label))
						if (!result.contains(cls.getBrowserText())) {
							result.add(cls.getBrowserText());
							if (returnFirst)
								return result;
						}
				}
			}
		}

		for (RDFProperty property : (Collection<RDFProperty>) owlModel
				.getRDFProperties()) {

			for (Object obj : (Collection<Object>) property.getLabels()) {
				String lbl = "";
				if (obj instanceof String) {
					lbl = (String) obj;
				} else if (obj instanceof DefaultRDFSLiteral) {
					DefaultRDFSLiteral rdfsLiteral = (DefaultRDFSLiteral) obj;
					lbl = rdfsLiteral.getBrowserText();
				}

				if (!lbl.isEmpty()) {
					if (lbl.equals(label))
						if (!result.contains(property.getBrowserText())) {
							result.add(property.getBrowserText());
							if (returnFirst)
								return result;
						}
				}
			}
		}

		for (RDFSDatatype dataType : owlModel.getRDFSDatatypes()) {
			for (Object lbl : (Collection<Object>) dataType.getLabels()) {
				if (lbl instanceof DefaultRDFSLiteral){
					if (((DefaultRDFSLiteral)lbl).getBrowserText().equals(label))
						if (!result.contains(dataType.getBrowserText())) {
							result.add(dataType.getBrowserText());
							if (returnFirst)
								return result;
						}
				}
			}

		}

		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getOWLClass(String selfCompletion, int maxTerms) {
		List<String> result = new ArrayList<String>();
		int count = 0;
		for (RDFSClass cls : (Collection<RDFSClass>) owlModel.getRDFSClasses()) {
			// System.out.println("Local name: "+cls.getLocalName());
			String name = getUriToName(cls.getURI(), owlModel);
			if (name.startsWith(selfCompletion)) {
				result.add(name);
				count++;
				if (count == maxTerms) {
					break;
				} else {
					continue;
				}
			}
			for (Object lbl : (Collection<?>) cls.getLabels()) {
				if (lbl instanceof DefaultRDFSLiteral){
					if (((DefaultRDFSLiteral) lbl).getBrowserText().startsWith(selfCompletion)) {
						result.add(((DefaultRDFSLiteral) lbl).getBrowserText());
						count++;
						break;
					}
				}
			}
			if (count == maxTerms) {
				break;
			}
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getOWLDatatype(String selfCompletion, int maxTerms) {

		List<String> result = new ArrayList<String>();
		int count = 0;

		for (RDFSDatatype dataType : owlModel.getRDFSDatatypes()) {

			String name = getUriToName(dataType.getURI(), owlModel);
			if (name.startsWith(selfCompletion)) {
				result.add(name);
				count++;
				if (count == maxTerms) {
					break;
				} else {
					continue;
				}
			}
			for (Object lbl : (Collection<?>) dataType.getLabels()) {
				if (lbl instanceof DefaultRDFSLiteral){
					if (((DefaultRDFSLiteral) lbl).getBrowserText().startsWith(selfCompletion)) {
						result.add(((DefaultRDFSLiteral) lbl).getBrowserText());
						count++;
						break;
					}
				}
			}
			if (count == maxTerms) {
				break;
			}

		}

		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getOWLDatatypePropertie(String selfCompletion,
			int maxTerms) {

		List<String> result = new ArrayList<String>();
		int count = 0;
		for (RDFProperty property : (Collection<RDFProperty>) owlModel.getRDFProperties()) {

			if( property.getClass().getSimpleName().indexOf("DatatypeProperty")>0){
				String name = getUriToName(property.getURI(), owlModel);
				if (name.startsWith(selfCompletion)) {
					result.add(name);
					count++;
					if (count == maxTerms) {
						break;
					} else {
						continue;
					}


				}
				for (Object lbl : (Collection<?>) property.getLabels()) {
					if (lbl instanceof DefaultRDFSLiteral){
						if (((DefaultRDFSLiteral) lbl).getBrowserText().startsWith(selfCompletion)) {
							result.add(((DefaultRDFSLiteral) lbl).getBrowserText());
							count++;
							break;
						}
					}
				}
				if (count == maxTerms) {
					break;
				}
			}

		}

		return result;
	}

	public JenaOWLModel getOwlModel() {
		return owlModel;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getOWLObjectPropertie(String selfCompletion,
			int maxTerms) {

		List<String> result = new ArrayList<String>();
		int count = 0;
		for (RDFProperty property : (Collection<RDFProperty>) owlModel.getRDFProperties()) {

			if( property.getClass().getSimpleName().indexOf("ObjectProperty")>0){
				String name = getUriToName(property.getURI(), owlModel);
				if (name.startsWith(selfCompletion)) {
					result.add(name);
					count++;
					if (count == maxTerms) {
						break;
					} else {
						continue;
					}


				}
				for (Object lbl : (Collection<?>) property.getLabels()) {
					if (lbl instanceof DefaultRDFSLiteral){
						if (((DefaultRDFSLiteral) lbl).getBrowserText().startsWith(selfCompletion)) {
							result.add(((DefaultRDFSLiteral) lbl).getBrowserText());
							count++;
							break;
						}
					}
				}
				if (count == maxTerms) {
					break;
				}
			}

		}

		return result;
	}

	@Override
	public List<String> getOWLSameAsDiferentFrom(String selfCompletion,
			int maxTerms) {
		List<String> result = new ArrayList<String>();

		if ("sameas".contains(selfCompletion)) {
			result.add("sameas");
		}
		if ("differentfrom".contains(selfCompletion)) {
			result.add("differentfrom");
		}

		return result;
	}

	@Override
	public SWRLManager getSWRLManager() {
		return swrlManager;
	}

	public String getUriToName(String uri, JenaOWLModel owlModel) {
		if (uri.contains("#")) {
			if (owlModel.getPrefixForResourceName(uri).isEmpty())
				return NamespaceUtil.getLocalName(uri);
			else
				return owlModel.getPrefixForResourceName(uri) + ":"
				+ NamespaceUtil.getLocalName(uri);
		} else
			return uri;
	}

	// TODO rever esses Exception
	@Override
	public boolean hasOWLClass(String resource) {
		try {
			return (owlModel.getOWLNamedClass(resource) != null);
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean hasOWLDatatype(String resource) {
		try {
			return (owlModel.getOWLIndividual(resource) != null);
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean hasOWLDatatypePropertie(String resource) {
		try {
			return (owlModel.getOWLDatatypeProperty(resource) != null);
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean hasOWLObjectPropertie(String resource) {
		try {
			return (owlModel.getOWLObjectProperty(resource) != null);
		} catch (Exception e) {
			return false;
		}
	}
}
