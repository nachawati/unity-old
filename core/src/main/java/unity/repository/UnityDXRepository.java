/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package unity.repository;

import java.util.stream.Stream;

import org.gitlab4j.api.GitLabApiException;

import unity.UnityDXSession;
import unity.UnityDXSessionObject;
import unity.api.DXException;
import unity.api.DXRepository;
import unity.ontology.UnityDXOntology;
import unity.system.UnityDXProject;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public class UnityDXRepository extends UnityDXSessionObject implements DXRepository
{
    /**
     *
     */
    private final UnityDXProject project;

    /**
     * @param session
     * @param project
     */
    public UnityDXRepository(UnityDXSession session, UnityDXProject project)
    {
        super(session);
        this.project = project;
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXRepository#getBranch(java.lang.String)
     */
    @Override
    public UnityDXBranch getBranch(String branchName) throws DXException
    {
        try {
            return new UnityDXBranch(getSession(), this,
                    api().getRepositoryApi().getBranch(project.getId(), branchName));
        } catch (final GitLabApiException e) {
            throw new DXException(e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXRepository#getBranches()
     */
    @Override
    public Stream<UnityDXBranch> getBranches() throws DXException
    {
        try {
            return api().getRepositoryApi().getBranches(project.getId()).stream()
                    .map(b -> new UnityDXBranch(getSession(), this, b));
        } catch (final GitLabApiException e) {
            throw new DXException(e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXRepository#getCommit(java.lang.String)
     */
    @Override
    public UnityDXCommit getCommit(String commitId) throws DXException
    {
        try {
            return new UnityDXCommit(getSession(), this, api().getCommitsApi().getCommit(project.getId(), commitId));
        } catch (final GitLabApiException e) {
            throw new DXException(e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXRepository#getDefaultBranch()
     */
    @Override
    public UnityDXBranch getDefaultBranch() throws DXException
    {
        return getBranch(project.getDefaultBranchName());
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXRepository#getDefaultOntology()
     */
    @Override
    public UnityDXOntology getDefaultOntology() throws DXException
    {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXRepository#getOntology(java.lang.String)
     */
    @Override
    public UnityDXOntology getOntology(String branchName) throws DXException
    {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXRepository#getProject()
     */
    @Override
    public UnityDXProject getProject()
    {
        return project;
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXRepository#getTags()
     */
    @Override
    public Stream<UnityDXTag> getTags() throws DXException
    {
        try {
            return api().getRepositoryApi().getTags(project.getId()).stream()
                    .map(t -> new UnityDXTag(getSession(), this, t));
        } catch (final GitLabApiException e) {
            throw new DXException(e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXRepository#unprotectBranch(java.lang.String)
     */
    @Override
    public void unprotectBranch(String branchName) throws DXException
    {
        try {
            api().getRepositoryApi().unprotectBranch(project.getId(), branchName);
        } catch (final GitLabApiException e) {
            throw new DXException(e);
        }
    }
}
