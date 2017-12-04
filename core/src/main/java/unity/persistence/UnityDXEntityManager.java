/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package unity.persistence;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.metamodel.Metamodel;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public class UnityDXEntityManager implements AutoCloseable, EntityManager
{
    /**
     *
     */
    private final EntityManager em;

    /**
     * @param em
     */
    public UnityDXEntityManager(final EntityManager em)
    {
        this.em = em;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.persistence.EntityManager#clear()
     */
    @Override
    public void clear()
    {
        em.clear();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.AutoCloseable#close()
     */
    @Override
    public void close()
    {
        em.close();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.persistence.EntityManager#contains(java.lang.Object)
     */
    @Override
    public boolean contains(final Object entity)
    {
        return em.contains(entity);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.persistence.EntityManager#createEntityGraph(java.lang.Class)
     */
    @Override
    public <T> EntityGraph<T> createEntityGraph(final Class<T> rootType)
    {
        return em.createEntityGraph(rootType);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.persistence.EntityManager#createEntityGraph(java.lang.String)
     */
    @Override
    public EntityGraph<?> createEntityGraph(final String graphName)
    {
        return em.createEntityGraph(graphName);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.persistence.EntityManager#createNamedQuery(java.lang.String)
     */
    @Override
    public Query createNamedQuery(final String name)
    {
        return em.createNamedQuery(name);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.persistence.EntityManager#createNamedQuery(java.lang.String,
     * java.lang.Class)
     */
    @Override
    public <T> TypedQuery<T> createNamedQuery(final String name, final Class<T> resultClass)
    {
        return em.createNamedQuery(name, resultClass);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * javax.persistence.EntityManager#createNamedStoredProcedureQuery(java.lang.
     * String)
     */
    @Override
    public StoredProcedureQuery createNamedStoredProcedureQuery(final String name)
    {
        return em.createNamedStoredProcedureQuery(name);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.persistence.EntityManager#createNativeQuery(java.lang.String)
     */
    @Override
    public Query createNativeQuery(final String sqlString)
    {
        return em.createNativeQuery(sqlString);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.persistence.EntityManager#createNativeQuery(java.lang.String,
     * java.lang.Class)
     */
    @Override
    @SuppressWarnings("rawtypes")
    public Query createNativeQuery(final String sqlString, final Class resultClass)
    {
        return em.createNativeQuery(sqlString, resultClass);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.persistence.EntityManager#createNativeQuery(java.lang.String,
     * java.lang.String)
     */
    @Override
    public Query createNativeQuery(final String sqlString, final String resultSetMapping)
    {
        return em.createNativeQuery(sqlString, resultSetMapping);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.persistence.EntityManager#createQuery(javax.persistence.criteria.
     * CriteriaDelete)
     */
    @Override
    @SuppressWarnings("rawtypes")
    public Query createQuery(final CriteriaDelete deleteQuery)
    {
        return em.createQuery(deleteQuery);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.persistence.EntityManager#createQuery(javax.persistence.criteria.
     * CriteriaQuery)
     */
    @Override
    public <T> TypedQuery<T> createQuery(final CriteriaQuery<T> criteriaQuery)
    {
        return em.createQuery(criteriaQuery);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.persistence.EntityManager#createQuery(javax.persistence.criteria.
     * CriteriaUpdate)
     */
    @Override
    @SuppressWarnings("rawtypes")
    public Query createQuery(final CriteriaUpdate updateQuery)
    {
        return em.createQuery(updateQuery);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.persistence.EntityManager#createQuery(java.lang.String)
     */
    @Override
    public Query createQuery(final String qlString)
    {
        return em.createQuery(qlString);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.persistence.EntityManager#createQuery(java.lang.String,
     * java.lang.Class)
     */
    @Override
    public <T> TypedQuery<T> createQuery(final String qlString, final Class<T> resultClass)
    {
        return em.createQuery(qlString, resultClass);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * javax.persistence.EntityManager#createStoredProcedureQuery(java.lang.String)
     */
    @Override
    public StoredProcedureQuery createStoredProcedureQuery(final String procedureName)
    {
        return em.createStoredProcedureQuery(procedureName);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * javax.persistence.EntityManager#createStoredProcedureQuery(java.lang.String,
     * java.lang.Class[])
     */
    @Override
    @SuppressWarnings("rawtypes")
    public StoredProcedureQuery createStoredProcedureQuery(final String procedureName, final Class... resultClasses)
    {
        return em.createStoredProcedureQuery(procedureName, resultClasses);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * javax.persistence.EntityManager#createStoredProcedureQuery(java.lang.String,
     * java.lang.String[])
     */
    @Override
    public StoredProcedureQuery createStoredProcedureQuery(final String procedureName,
            final String... resultSetMappings)
    {
        return em.createStoredProcedureQuery(procedureName, resultSetMappings);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.persistence.EntityManager#detach(java.lang.Object)
     */
    @Override
    public void detach(final Object entity)
    {
        em.detach(entity);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.persistence.EntityManager#find(java.lang.Class, java.lang.Object)
     */
    @Override
    public <T> T find(final Class<T> entityClass, final Object primaryKey)
    {
        return em.find(entityClass, primaryKey);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.persistence.EntityManager#find(java.lang.Class, java.lang.Object,
     * javax.persistence.LockModeType)
     */
    @Override
    public <T> T find(final Class<T> entityClass, final Object primaryKey, final LockModeType lockMode)
    {
        return em.find(entityClass, primaryKey, lockMode);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.persistence.EntityManager#find(java.lang.Class, java.lang.Object,
     * javax.persistence.LockModeType, java.util.Map)
     */
    @Override
    public <T> T find(final Class<T> entityClass, final Object primaryKey, final LockModeType lockMode,
            final Map<String, Object> properties)
    {
        return em.find(entityClass, primaryKey, lockMode, properties);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.persistence.EntityManager#find(java.lang.Class, java.lang.Object,
     * java.util.Map)
     */
    @Override
    public <T> T find(final Class<T> entityClass, final Object primaryKey, final Map<String, Object> properties)
    {
        return em.find(entityClass, primaryKey, properties);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.persistence.EntityManager#flush()
     */
    @Override
    public void flush()
    {
        em.flush();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.persistence.EntityManager#getCriteriaBuilder()
     */
    @Override
    public CriteriaBuilder getCriteriaBuilder()
    {
        return em.getCriteriaBuilder();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.persistence.EntityManager#getDelegate()
     */
    @Override
    public Object getDelegate()
    {
        return em.getDelegate();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.persistence.EntityManager#getEntityGraph(java.lang.String)
     */
    @Override
    public EntityGraph<?> getEntityGraph(final String graphName)
    {
        return em.getEntityGraph(graphName);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.persistence.EntityManager#getEntityGraphs(java.lang.Class)
     */
    @Override
    public <T> List<EntityGraph<? super T>> getEntityGraphs(final Class<T> entityClass)
    {
        return em.getEntityGraphs(entityClass);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.persistence.EntityManager#getEntityManagerFactory()
     */
    @Override
    public EntityManagerFactory getEntityManagerFactory()
    {
        return em.getEntityManagerFactory();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.persistence.EntityManager#getFlushMode()
     */
    @Override
    public FlushModeType getFlushMode()
    {
        return em.getFlushMode();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.persistence.EntityManager#getLockMode(java.lang.Object)
     */
    @Override
    public LockModeType getLockMode(final Object entity)
    {
        return em.getLockMode(entity);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.persistence.EntityManager#getMetamodel()
     */
    @Override
    public Metamodel getMetamodel()
    {
        return em.getMetamodel();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.persistence.EntityManager#getProperties()
     */
    @Override
    public Map<String, Object> getProperties()
    {
        return em.getProperties();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.persistence.EntityManager#getReference(java.lang.Class,
     * java.lang.Object)
     */
    @Override
    public <T> T getReference(final Class<T> entityClass, final Object primaryKey)
    {
        return em.getReference(entityClass, primaryKey);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.persistence.EntityManager#getTransaction()
     */
    @Override
    public EntityTransaction getTransaction()
    {
        return em.getTransaction();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.persistence.EntityManager#isJoinedToTransaction()
     */
    @Override
    public boolean isJoinedToTransaction()
    {
        return em.isJoinedToTransaction();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.persistence.EntityManager#isOpen()
     */
    @Override
    public boolean isOpen()
    {
        return em.isOpen();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.persistence.EntityManager#joinTransaction()
     */
    @Override
    public void joinTransaction()
    {
        em.joinTransaction();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.persistence.EntityManager#lock(java.lang.Object,
     * javax.persistence.LockModeType)
     */
    @Override
    public void lock(final Object entity, final LockModeType lockMode)
    {
        em.lock(entity, lockMode);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.persistence.EntityManager#lock(java.lang.Object,
     * javax.persistence.LockModeType, java.util.Map)
     */
    @Override
    public void lock(final Object entity, final LockModeType lockMode, final Map<String, Object> properties)
    {
        em.lock(entity, lockMode, properties);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.persistence.EntityManager#merge(java.lang.Object)
     */
    @Override
    public <T> T merge(final T entity)
    {
        return em.merge(entity);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.persistence.EntityManager#persist(java.lang.Object)
     */
    @Override
    public void persist(final Object entity)
    {
        em.persist(entity);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.persistence.EntityManager#refresh(java.lang.Object)
     */
    @Override
    public void refresh(final Object entity)
    {
        em.refresh(entity);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.persistence.EntityManager#refresh(java.lang.Object,
     * javax.persistence.LockModeType)
     */
    @Override
    public void refresh(final Object entity, final LockModeType lockMode)
    {
        em.refresh(entity, lockMode);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.persistence.EntityManager#refresh(java.lang.Object,
     * javax.persistence.LockModeType, java.util.Map)
     */
    @Override
    public void refresh(final Object entity, final LockModeType lockMode, final Map<String, Object> properties)
    {
        em.refresh(entity, lockMode, properties);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.persistence.EntityManager#refresh(java.lang.Object, java.util.Map)
     */
    @Override
    public void refresh(final Object entity, final Map<String, Object> properties)
    {
        em.refresh(entity, properties);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.persistence.EntityManager#remove(java.lang.Object)
     */
    @Override
    public void remove(final Object entity)
    {
        em.remove(entity);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * javax.persistence.EntityManager#setFlushMode(javax.persistence.FlushModeType)
     */
    @Override
    public void setFlushMode(final FlushModeType flushMode)
    {
        em.setFlushMode(flushMode);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.persistence.EntityManager#setProperty(java.lang.String,
     * java.lang.Object)
     */
    @Override
    public void setProperty(final String propertyName, final Object value)
    {
        em.setProperty(propertyName, value);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.persistence.EntityManager#unwrap(java.lang.Class)
     */
    @Override
    public <T> T unwrap(final Class<T> cls)
    {
        return em.unwrap(cls);
    }
}
