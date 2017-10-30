/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package io.dgms.unity.repository;

import java.util.stream.Stream;

import org.gitlab4j.api.GitLabApiException;

import io.dgms.unity.UnityDGSession;
import io.dgms.unity.UnityDGSessionObject;
import io.dgms.unity.api.DGException;
import io.dgms.unity.api.DGRepository;
import io.dgms.unity.ontology.UnityDGOntology;
import io.dgms.unity.system.UnityDGProject;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public class UnityDGRepository extends UnityDGSessionObject implements DGRepository
{
    /**
     *
     */
    private final UnityDGProject project;

    /**
     * @param session
     * @param project
     */
    public UnityDGRepository(UnityDGSession session, UnityDGProject project)
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
    public UnityDGBranch getBranch(String branchName) throws DGException
    {
        try {
            return new UnityDGBranch(getSession(), this,
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
    public Stream<UnityDGBranch> getBranches() throws DGException
    {
        try {
            return api().getRepositoryApi().getBranches(project.getId()).stream()
                    .map(b -> new UnityDGBranch(getSession(), this, b));
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
    public UnityDGCommit getCommit(String commitId) throws DGException
    {
        try {
            return new UnityDGCommit(getSession(), this, api().getCommitsApi().getCommit(project.getId(), commitId));
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
    public UnityDGBranch getDefaultBranch() throws DGException
    {
        return getBranch(project.getDefaultBranchName());
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGRepository#getDefaultOntology()
     */
    @Override
    public UnityDGOntology getDefaultOntology() throws DGException
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
    public UnityDGOntology getOntology(String branchName) throws DGException
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
    public UnityDGProject getProject()
    {
        return project;
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGRepository#getTags()
     */
    @Override
    public Stream<UnityDGTag> getTags() throws DGException
    {
        try {
            return api().getRepositoryApi().getTags(project.getId()).stream()
                    .map(t -> new UnityDGTag(getSession(), this, t));
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
