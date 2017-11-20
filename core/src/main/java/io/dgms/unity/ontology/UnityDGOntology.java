/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package io.dgms.unity.ontology;

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

import io.dgms.unity.UnityDGSession;
import io.dgms.unity.UnityDGSessionObject;
import io.dgms.unity.api.DGBranch;
import io.dgms.unity.api.DGException;
import io.dgms.unity.api.DGFile;
import io.dgms.unity.api.DGOntology;
import io.dgms.unity.api.DGProject;
import io.dgms.unity.api.DGResource;
import io.dgms.unity.repository.UnityDGBranch;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public class UnityDGOntology extends UnityDGSessionObject implements DGOntology
{
    /**
     *
     */
    private final UnityDGBranch branch;
    private Dataset             dataset;
    private Model               model;
    private OntModel            ontModel;
    private Ontology            ontology;

    /**
     * @param session
     * @param branch
     */
    public UnityDGOntology(UnityDGSession session, UnityDGBranch branch)
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
     * @see io.dgms.unity.api.DGOntology#getBranch()
     */
    @Override
    public DGBranch getBranch()
    {
        return branch;
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGOntology#getFiles(java.lang.String)
     */
    @Override
    public Stream<? extends DGFile> getFiles(String type)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGOntology#getProject()
     */
    @Override
    public DGProject getProject()
    {
        return branch.getProject();
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGOntology#getResource(java.lang.String)
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
     * @see io.dgms.unity.api.DGOntology#getResources(java.lang.String)
     */
    @Override
    public Stream<? extends DGResource> getResources(String type)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public String getSchema(final String type)
    {
        try {
            dataset.begin(ReadWrite.READ);
            final URI ontologyUri = getSession().getGitLabHost().resolve(getProject().getPathWithNamespace());
            final Individual individual = ontModel.getIndividual(type);
            System.out.println(individual + "individual");
            return individual.getPropertyValue(ontModel.getProperty("http://dgms.io/ontologies/example#hasSchema"))
                    .asLiteral().getString();
        } catch (final Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            dataset.end();
        }
    }

    private void init() throws DGException
    {
        try {
            dataset.begin(ReadWrite.WRITE);
            model = dataset.getNamedModel(branch.getName());
            ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, model);
            final String ontologyUri = branch.getProject().getPathWithNamespace();
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
            throw new DGException(e);
        } finally {
            dataset.end();
        }
    }

    private Dataset openDataset() throws DGException
    {
        try {
            final Path tdb = UnityDGSession.getLocalSystemPath().resolve("tdb");
            final Path path = tdb.resolve(branch.getProject().getPathWithNamespace());
            Files.createDirectories(path);
            return TDBFactory.createDataset(path.toString());
        } catch (final IOException e) {
            throw new DGException(e);
        }
    }

    private void sync(String ontologyUri) throws DGException, IOException
    {
        try (InputStream input = branch.getFile("/ontology.owl").newInputStream()) {
            final OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
            final OWLOntology ontology = manager.loadOntologyFromOntologyDocument(new StreamDocumentSource(input));
            final ByteArrayOutputStream output = new ByteArrayOutputStream();
            ontology.saveOntology(new TurtleDocumentFormat(), output);
            ontModel.read(new ByteArrayInputStream(output.toByteArray()), ontologyUri, "TTL");
            branch.getFiles(true).forEach(file -> {
                final String path = file.getPath();
                if (!file.isDirectory() && path.endsWith(".metadata.json"))
                    try (InputStream stream = file.newInputStream()) {
                        model.read(stream, ontologyUri + "/" + path.substring(0, path.length() - 14), "JSON-LD");
                    } catch (final Exception e) {
                    }
            });
        } catch (final Exception e) {
            throw new DGException(e);
        } finally {
            ontology = ontModel.createOntology(ontologyUri);
            ontology.setVersionInfo(branch.getId());
        }
    }
}
