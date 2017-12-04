/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package unity.client;

import java.util.stream.Stream;

import org.gitlab4j.api.GitLabApiException;

import unity.api.DXException;
import unity.api.DXRepository;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public class ClientDXRepository extends ClientDXSessionObject implements DXRepository
{
    /**
     *
     */
    private final ClientDXProject project;

    /**
     * @param session
     * @param project
     */
    public ClientDXRepository(ClientDXSession session, ClientDXProject project)
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
    public ClientDXBranch getBranch(String branchName) throws DXException
    {
        try {
            return new ClientDXBranch(getSession(), this,
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
    public Stream<ClientDXBranch> getBranches() throws DXException
    {
        try {
            return api().getRepositoryApi().getBranches(project.getId()).stream()
                    .map(b -> new ClientDXBranch(getSession(), this, b));
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
    public ClientDXCommit getCommit(String commitId) throws DXException
    {
        try {
            return new ClientDXCommit(getSession(), this, api().getCommitsApi().getCommit(project.getId(), commitId));
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
    public ClientDXBranch getDefaultBranch() throws DXException
    {
        return getBranch(project.getDefaultBranchName());
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXRepository#getDefaultOntology()
     */
    @Override
    public ClientDXOntology getDefaultOntology() throws DXException
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
    public ClientDXOntology getOntology(String branchName) throws DXException
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
    public ClientDXProject getProject()
    {
        return project;
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXRepository#getTags()
     */
    @Override
    public Stream<ClientDXTag> getTags() throws DXException
    {
        try {
            return api().getRepositoryApi().getTags(project.getId()).stream()
                    .map(t -> new ClientDXTag(getSession(), this, t));
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
