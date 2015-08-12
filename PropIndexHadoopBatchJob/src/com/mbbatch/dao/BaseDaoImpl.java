package com.mbbatch.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.persistence.Entity;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.LockAcquisitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;



/**
 * Base DAO for the entity beans. Most DAOs will probably extend this class
 * since it provides all the basic CRUD operations. You can even possible roll
 * your own DAO on the fly with this base implementation.
 * 
 * @param <T>
 *            entity bean to be handled by the DAO.
 */
@Transactional
@SuppressWarnings(value = "unchecked")
@Repository
public abstract class BaseDaoImpl<T extends Object> implements BaseDao<T> {

	private final Class<T> daoClass;
	
	private static final Logger logger = Logger.getLogger(BaseDaoImpl.class);


	@Autowired
	private SessionFactory sessionFactory;

	public BaseDaoImpl() {
		this.daoClass = (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
		if (null == this.daoClass) {
			throw new IllegalStateException(
					"Can't instantiate DaoImpl with a null class object.");
		}
		// make sure it's properly annotated as an Entity
		if (!this.daoClass.isAnnotationPresent(Entity.class)) {
			throw new IllegalStateException(
					this.daoClass
							+ " is not an Entity. Make sure you've "
							+ "properly annotated it with the javax.persistence.Entity annotation.");
		}
	}

	protected final Class<T> getDaoClass() {
		return daoClass;
	}
	

	
	/**
	 * Helper method that takes a where clause to load a single instance of T
	 * based on the query. Example usage:
	 * 
	 * <pre>
	 * public WIPStatus getWIPStatus(final String url) {
	 * 	return loadByQuery(&quot;WHERE url=?&quot;, url);
	 * }
	 * </pre>
	 */
	public final T findByQuery(final String whereClause,
			final Object... values) {
		final List<T> list = findAllByQuery(0, 1, whereClause, values);
		return 0 == list.size() ? null : list.get(0);
	}

	/**
	 * Helper method that takes a where clause to load all instances of T based
	 * on the query. Example usage:
	 * 
	 * <pre>
	 * public WIPStatus getWIPStatus(final String url) {
	 * 	return loadByQuery(&quot;WHERE url=?&quot;, url);
	 * }
	 * </pre>
	 * 
	 * or:
	 * 
	 * <pre>
	 * public WIPStatus getWIPStatus(final String url) {
	 * 	return loadByQuery(&quot;order by title&quot;);
	 * }
	 * </pre>
	 */
	public List<T> findAllByQuery(final String whereClause,
			final Object... values) {
		return findAllByQuery(-1, -1, whereClause, values);
	}

	/**
	 * Loads an instance of T based on the given primary key.
	 */
	public final T findById(final Serializable id) {
		return (T) sessionFactory.getCurrentSession().get(getDaoClass(), id);
	}
	
	public final List<T> findAllById(String idColumn, String[] idArray){
	    String whereClause = " where "+ inClause(idColumn, idArray);
	    return findAllByQuery(whereClause);
	}

	public Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	/**
	 * Returns a <code>List</code> of all <code>T</code> objects.
	 * 
	 * @return Non-null <code>List</code> of <code>T</code> objects
	 */
	public final List<T> findAll() {
		return findAllByQuery(-1, -1, "");
	}

	/**
	 * Returns a <code>List</code> of all <code>T</code> objects.
	 * 
	 * @param maxResults
	 *            if you want unlimited results, set to -1
	 * @return Non-null <code>List</code> of <code>T</code> objects
	 */
	protected List<T> findAllByQuery(final int maxResults) {
		return findAllByQuery(-1, maxResults, "");
	}

	/**
	 * Return a <code>List</code> of all <code>T</code> objects.
	 * 
	 * @param start
	 *            first result is 0
	 * @param maxResults
	 *            if you want unlimited results, set to 0
	 * @return Non-null <code>List</code> of <code>T</code> objects
	 */
	public List<T> findAllByQuery(final int start, final int maxResults) {
		return findAllByQuery(start, maxResults, "");
	}

	
	
	/**
	 * Return a <code>List</code> of all <code>T</code> objects.
	 * 
	 * @param start
	 *            first result is 0
	 * @param maxResults
	 *            if you want unlimited results, set to 0
	 * @param whereClause
	 *            a SQL where clause associated with the passed-in values and
	 *            types
	 * @param values
	 *            object array of values
	 * @return Non-null <code>List</code> of <code>T</code> objects
	 */
	public List<T> findAllByQuery(final int start, final int maxResults,
			final String whereClause, final Object... values) {
		return doGenericListQuery(start, maxResults, "FROM "
				+ getDaoClass().getName() + " " + whereClause, values);
	}
	
		/**
	 * This is a generic single-object querying method. If more than one result
	 * is returned from the query, then a <code>NonUniqueResultException</code>
	 * is thrown.
	 */
	protected final Object doGenericSingleObjectQuery(final String sql,
			final Object... values) {
		final Query query = sessionFactory.getCurrentSession().createQuery(sql);
		setQueryParameters(query, -1, 0, values);
		// this throws the NonUniqueResultException
		return query.uniqueResult();

	}
	
	protected final List<T> doGenericListQuery(final String hql, final Object... values){
		return doGenericListQuery(-1, 0, hql, values);
	}

	public final long findCountByQuery(final String whereClause, final Object... values){
		final Query query = sessionFactory.getCurrentSession().createQuery(
				"select count(*) from " + getDaoClass().getName() + " "
						+ whereClause);
        setQueryParameters(query, -1, 0, values);
        // this throws the NonUniqueResultException
        Long count = (Long) query.uniqueResult();
        return (count == null) ? 0 : count.longValue();
	}
	
	public final long sumByQuery(final String columnName, final String whereClause, final Object... values){
		final Query query = sessionFactory.getCurrentSession().createQuery(
				"select sum(" + columnName + ") from "
						+ getDaoClass().getName() + " " + whereClause);
        setQueryParameters(query, -1, 0, values);
        // this throws the NonUniqueResultException
        Long sum = (Long) query.uniqueResult();
        return (sum == null) ? 0 : sum.longValue();
	}
	
	/** This is the core querying method. */
	protected final List<T> doGenericListQuery(final int start,
			final int maxResults, final String hql, final Object... values) {
		return (List<T>) doQuery(start, maxResults, hql, values);
	}

	protected final List<? extends Object> doQuery(final int start,
			final int maxResults, final String hql, final Object... values) {
		
		final Query query = sessionFactory.getCurrentSession().createQuery(hql);
		setQueryParameters(query, start, maxResults, values);
		final List<? extends Object> t = query.list();
		return t != null ? t : Collections.EMPTY_LIST;
	}

	protected void setQueryParameters(final Query query, final int start,
			final int maxResults, final Object... values) {
		
		if (maxResults > 0) {
			query.setMaxResults(maxResults);
		}
		SessionFactoryUtils.applyTransactionTimeout(query, getSessionFactory());
		// here's the parameter that HibernateTemplate doesn't check for in
		// find().
		if (start >= 0) {
			query.setFirstResult(start);
		}
		for (int i = 0; i < values.length; i++) {
			if(!(values[i] instanceof Collection)) {
				query.setParameter(i, values[i]);
			} else {
				query.setParameterList("list", (Collection<?>) values[i]);
			}
		}
		
	}

	private SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	/**
	 * Override this method if you wish to enable/disable the query cache for
	 * your dao's queries.
	 * 
	 * @return true by default.
	 */
	protected boolean isQueryCacheEnabled() {
		return true;
	}

	/* This methods always creates new object never updates them
	 * 
	 */
	public void create(final T t){
		getCurrentSession().save(t);
	}
	
	/**
	 * Inserts or updates a <code>T</code>.
	 */
	public void save(final T t) {
		flush();
		clear();
		sessionFactory.getCurrentSession().saveOrUpdate(t);
		flush();
		clear();
	}

	public void update(final T t) {
		sessionFactory.getCurrentSession().update(t);
	}

	public void delete(final T t) {
		sessionFactory.getCurrentSession().delete(t);
	}

	public void deleteAll(final List<T> list){
		if(list != null) {
			for(T t: list) {
				delete(t);
			}
		}
	}
	
	public long getCount() {
		final String sql = "select count(*) from " + getDaoClass().getName();
		return ((Long) doGenericSingleObjectQuery(sql)).longValue();
	}

	public long getCountFromStartId(final long startId) {
		final String sql = "select count(*) from " + getDaoClass().getName()
				+ " where id >= ?";
		final Long count = (Long) doGenericSingleObjectQuery(sql, startId);
		return null == count ? 0L : count.longValue();
	}

	public List<T> findAllFromStartId(final long startId, final int maxResults) {
		return findAllByQuery(0, maxResults, "where id >= ?", startId);
	}

	public void bulkUpdate(final String setClause, final Object... values){
		Query query = sessionFactory.getCurrentSession().createQuery(
				"UPDATE " + getDaoClass().getName() + setClause);
        setQueryParameters(query, -1, 0, values);
        query.executeUpdate();
	}
	
	/**
	 * Deletes the hell out of all records of passed-in table. This should
	 * probably only ever be used to help load fake data.
	 * 
	 * @param table
	 *            Table containing records to delete the hell out of.
	 */
	protected void deleteTheHellOutOfThisTable(final String table) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				"delete from" + table);
		query.executeUpdate();
	}

	public void flush() {
		sessionFactory.getCurrentSession().flush();
	}

	public void clear() {
		sessionFactory.getCurrentSession().clear();
	}

	protected final StringBuilder makeInClause(final int numElements) {
		final StringBuilder sb = new StringBuilder();
		sb.append("in (");
		for (int i = 0; i < numElements;) {
			sb.append('?');
			if (++i < numElements) {
				sb.append(",");
			}
		}
		sb.append(")");
		return sb;
	}

	protected int getCountValue(final Long count) {
		if (null == count) {
			return 0;
		}

		return count.intValue();
	}

	protected long getCountValueLong(final Long count) {
		if (null == count) {
			return 0L;
		}

		return count.longValue();
	}
	
	protected static String inClause(String colName, Object[] values){
		return  " " + colName + " in ('" + StringUtils.join(values, "','") + "') ";
	}
	public Object saveNew(Object obj) {
		Object pk = null;
		try {
			pk =  getCurrentSession().save(obj);
			getCurrentSession().flush();
			getCurrentSession().clear();
		} catch (LockAcquisitionException e) {
			//customLogFactory.error("LockAcquisitionException in saveNew of superDao "+obj.getClass()+" ",e);
			SQLException sqlEx = e.getSQLException();
			//customLogFactory.error("Exception in saveNew "+obj.getClass()+" SQLException of superDao "+sqlEx.getNextException().getMessage());
			sqlEx.getNextException().printStackTrace();
		}catch (Exception e) {
			//customLogFactory.error("Exception in saveNew "+obj.getClass()+"of superDao ",e);
			e.printStackTrace();
			throw new DataIntegrityViolationException("SuperDAO saveNew");
		}
		return pk;
	}
	
	public void saveAll(final List<T> list){
		try{
			Session s = getCurrentSession();
			for(Object obj : list)
				s.save(obj);
			s.flush();
			s.clear();
		}catch (LockAcquisitionException e) {
			SQLException sqlEx = e.getSQLException();
			sqlEx.getNextException().printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
			throw new DataIntegrityViolationException("SuperDAO saveAll");
		}
	}
}
