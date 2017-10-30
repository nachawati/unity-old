/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package io.dgms.unity.client;

import java.util.stream.Stream;

import org.gitlab4j.api.GitLabApiException;

import io.dgms.unity.api.DGException;
import io.dgms.unity.api.DGRepository;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public class ClientDGRepository extends ClientDGSessionObject implements DGRepository
{
    /**
     *
     */
    private final ClientDGProject project;

    /**
     * @param session
     * @param project
     */
    public ClientDGRepository(ClientDGSession session, ClientDGProject project)
    {
        super(session);
        this.project = project;
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGRepository#getBranch(java.lang.String)
     */
    @Override
    public ClientDGBranch getBranch(String branchName) throws DGException
    {
        try {
            return new ClientDGBranch(getSession(), this,
                    api().getRepositoryApi().getBranch(project.getId(), branchName));
        } catch (final GitLabApiException e) {
            throw new DGException(e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGRepository#getBranches()
     */
    @Override
    public Stream<ClientDGBranch> getBranches() throws DGException
    {
        try {
            return api().getRepositoryApi().getBranches(project.getId()).stream()
                    .map(b -> new ClientDGBranch(getSession(), this, b));
        } catch (final GitLabApiException e) {
            throw new DGException(e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGRepository#getCommit(java.lang.String)
     */
    @Override
    public ClientDGCommit getCommit(String commitId) throws DGException
    {
        try {
            return new ClientDGCommit(getSession(), this, api().getCommitsApi().getCommit(project.getId(), commitId));
        } catch (final GitLabApiException e) {
            throw new DGException(e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGRepository#getDefaultBranch()
     */
    @Override
    public ClientDGBranch getDefaultBranch() throws DGException
    {
        return getBranch(project.getDefaultBranchName());
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGRepository#getDefaultOntology()
     */
    @Override
    public ClientDGOntology getDefaultOntology() throws DGException
    {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGRepository#getOntology(java.lang.String)
     */
    @Override
    public ClientDGOntology getOntology(String branchName) throws DGException
    {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGRepository#getProject()
     */
    @Override
    public ClientDGProject getProject()
    {
        return project;
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGRepository#getTags()
     */
    @Override
    public Stream<ClientDGTag> getTags() throws DGException
    {
        try {
            return api().getRepositoryApi().getTags(project.getId()).stream()
                    .map(t -> new ClientDGTag(getSession(), this, t));
        } catch (final GitLabApiException e) {
            throw new DGException(e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGRepository#unprotectBranch(java.lang.String)
     */
    @Override
    public void unprotectBranch(String branchName) throws DGException
    {
        try {
            api().getRepositoryApi().unprotectBranch(project.getId(), branchName);
        } catch (final GitLabApiException e) {
            throw new DGException(e);
        }
    }
}
