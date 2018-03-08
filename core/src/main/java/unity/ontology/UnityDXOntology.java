/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package unity.ontology;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.io.IOUtils;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.ontology.Ontology;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.tdb.TDBFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.TurtleDocumentFormat;
import org.semanticweb.owlapi.io.StreamDocumentSource;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import com.google.gson.JsonObject;

import unity.UnityDXSession;
import unity.UnityDXSessionObject;
import unity.api.DXBranch;
import unity.api.DXException;
import unity.api.DXFile;
import unity.api.DXOntology;
import unity.api.DXProject;
import unity.api.DXResource;
import unity.repository.UnityDXBranch;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public class UnityDXOntology extends UnityDXSessionObject implements DXOntology
{
    /**
     *
     */
    private final UnityDXBranch branch;
    private Dataset             dataset;
    private Model               model;
    private OntModel            ontModel;
    private Ontology            ontology;

    /**
     * @param session
     * @param branch
     */
    public UnityDXOntology(UnityDXSession session, UnityDXBranch branch)
    {
        super(session);
        this.branch = branch;
        try {
            dataset = openDataset();
            init();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close()
    {
        ontModel.close();
        model.close();
    }

    public List<Individual> getArtifacts()
    {
        try {
            dataset.begin(ReadWrite.READ);
            return ontModel.listIndividuals().toList();
        } finally {
            dataset.end();
        }
    }

    public List<OntClass> getArtifactTypes()
    {
        try {
            dataset.begin(ReadWrite.READ);
            final OntClass cls = ontModel.getOntClass("http://dgms.io/ontologies/example#Resource");
            return cls.listSubClasses(true).toList();
        } catch (final Exception e) {
            return Collections.emptyList();
        } finally {
            dataset.end();
        }
    }

    public List<OntClass> getArtifactTypes(final String cls)
    {
        try {
            dataset.begin(ReadWrite.READ);
            return ontModel.getOntClass(cls).listSubClasses(true).toList();
        } catch (final Exception e) {
            return Collections.emptyList();
        } finally {
            dataset.end();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXOntology#getBranch()
     */
    @Override
    public DXBranch getBranch()
    {
        return branch;
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXOntology#getFiles(java.lang.String)
     */
    @Override
    public Stream<? extends DXFile> getFiles(String type)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXOntology#getProject()
     */
    @Override
    public DXProject getProject()
    {
        return branch.getProject();
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXOntology#getResource(java.lang.String)
     */
    @Override
    public JsonObject getResource(String identifier)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXOntology#getResources(java.lang.String)
     */
    @Override
    public Stream<? extends DXResource> getResources(String type)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public String getSchema(final String type)
    {
        try {
            dataset.begin(ReadWrite.READ);
            // final URI ontologyUri =
            // getSession().getGitLabHost().resolve(getProject().getPathWithNamespace());
            final Individual individual = ontModel.getIndividual(type);
            return individual.getPropertyValue(ontModel.getProperty("http://dgms.io/ontologies/example#hasSchema"))
                    .asLiteral().getString();
        } catch (final Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            dataset.end();
        }
    }

    public String getType(String identifier)
    {

        try {
            dataset.begin(ReadWrite.READ);
            final URI ontologyUri = getSession().getGitLabHost().resolve(getProject().getPathWithNamespace());
            final Individual individual = ontModel.getIndividual(ontologyUri + "/" + identifier);
            System.out.println(ontologyUri + "/" + identifier);
            return individual.getOntClass().toString();
        } catch (final Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            dataset.end();
        }
    }

    private void init() throws DXException
    {
        try {
            dataset.begin(ReadWrite.WRITE);
            model = dataset.getNamedModel(branch.getName());
            ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, model);
            final String ontologyUri = getSession().getGitLabHost().resolve(branch.getProject().getPathWithNamespace())
                    .toString();
            ontology = ontModel.getOntology(ontologyUri);
            if (ontology == null)
                ontology = ontModel.createOntology(ontologyUri);
            if (!branch.getId().equals(ontology.getVersionInfo())) {
                ontModel.removeAll();
                sync(ontologyUri);
            }

            dataset.commit();
        } catch (final Exception e) {
            dataset.abort();
            throw new DXException(e);
        } finally {
            dataset.end();
        }
    }

    private Dataset openDataset() throws DXException
    {
        try {
            final Path tdb = UnityDXSession.getLocalSystemPath().resolve("tdb");
            final Path path = tdb.resolve(branch.getProject().getPathWithNamespace());
            Files.createDirectories(path);
            return TDBFactory.createDataset(path.toString());
        } catch (final IOException e) {
            throw new DXException(e);
        }
    }

    private void sync(String ontologyUri) throws DXException, IOException
    {
        try (InputStream input = branch.getFile("/ontology.owl").newInputStream()) {
            final OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
            final OWLOntology ontology = manager.loadOntologyFromOntologyDocument(new StreamDocumentSource(input));
            final ByteArrayOutputStream output = new ByteArrayOutputStream();
            ontology.saveOntology(new TurtleDocumentFormat(), output);
            ontModel.read(new ByteArrayInputStream(output.toByteArray()), ontologyUri, "TTL");
            branch.getMetaDataFiles(true).forEach(file -> {
                final String path = file.getPath();
                try (InputStream stream = file.newInputStream()) {
                    model.read(stream, ontologyUri + "/" + path.substring(0, path.length() - 16), "JSON-LD");
                } catch (final Exception e) {
                }
            });
        } catch (final Exception e) {
            throw new DXException(e);
        } finally {
            ontology = ontModel.createOntology(ontologyUri);
            ontology.setVersionInfo(branch.getId());
        }
    }
}
