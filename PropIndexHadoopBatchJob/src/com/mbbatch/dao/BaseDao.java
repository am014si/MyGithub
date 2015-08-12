package com.mbbatch.dao;

import java.io.Serializable;
import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
 * General DAO interface for all daos in da LWS.
 * 
 * @author Leo Kim (lkim@limewire.com)
 * @param <T>
 *          bean that is to be handled by the implementing dao
 */
public interface BaseDao<T extends Object> {
  /** Load a specific T given an id. */
  T findById(Serializable id);

  /** Loads entire list of T objects in the db. */
  List<T> findAll();

  List<T> findAllById(String idColumn, String[] idArray);
  
  List<T> findAllByQuery(final String whereClause, final Object... values);
  
  /** Loads list of T objects with limits. */
  List<T> findAllByQuery(int start, int maxResults, final String whereClause, final Object... values);
  
  T findByQuery(final String whereClause, final Object... values);

  /** Attach the given object to the current session using HibernateTemplate.update(). */
  @Transactional(readOnly = false, propagation = Propagation.SUPPORTS)
  void update(T t);

  @Transactional(readOnly = false, propagation = Propagation.SUPPORTS)
  void save(T t);
  
  @Transactional(readOnly = false, propagation = Propagation.SUPPORTS)
  Object saveNew(Object obj);
  
  @Transactional(readOnly = false, propagation = Propagation.SUPPORTS)
  void create(T t);
  
  @Transactional(readOnly = false, propagation = Propagation.SUPPORTS)
  void delete(T t);

  @Transactional(readOnly = false, propagation = Propagation.SUPPORTS)
  public void deleteAll(final List<T> list);
  
  long getCount();

  long findCountByQuery(final String whereClause, final Object... values);
  long sumByQuery(final String columnName, final String whereClause, final Object... values);
  long getCountFromStartId(long startId);

  List<T> findAllFromStartId(long startId, int maxResults);
  
  void bulkUpdate(final String setClause, final Object... values);
  void saveAll(final List<T> list) throws Exception;
 
}
