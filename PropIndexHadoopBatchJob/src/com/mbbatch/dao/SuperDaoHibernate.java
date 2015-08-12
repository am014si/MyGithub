package com.mbbatch.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;


public abstract class SuperDaoHibernate extends HibernateDaoSupport  {

	
	
	/*
	 * SaveNew used to insert new row to the Database
	 * Object obj is Pojo Object 
	 */
	
    public Object findByPk(Serializable pk)
    {
		Session hibSession = null;
		Object retval = null;
		try {
		    hibSession = getSession();
		    retval = hibSession.get(getPojoObj().getClass(), pk);
		} catch (HibernateException e) {
		    e.printStackTrace();
		} finally {
		}
		return retval;
	}
    
	public Object saveNew(Object obj) {
		Object pk = null;
		try {
		//	customLogFactory.info("  Saving ...................... ");
			pk =  getHibernateTemplate().save(obj);
		//	customLogFactory.info("  Saved ...................... ");
			getHibernateTemplate().flush();
		//	customLogFactory.info("  Saved ........2.............. ");
			getHibernateTemplate().clear();
		//	customLogFactory.info("  Saved ........3............. ");
		} catch (Exception e) {			
			e.printStackTrace();
			throw new DataIntegrityViolationException("SuperDAO saveNew");
		}

		return pk;
	}

	public void saveUpdate(Object obj) {
		try {
			getHibernateTemplate().saveOrUpdate(obj);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new DataIntegrityViolationException("SuperDAO saveUpdate");
		}

	}

	/*
	 * New method by raj just for update if not found then throw exception
	 * 
	 */
	
	public void update(Object obj) {
		try {
			getHibernateTemplate().update(obj);
		} catch (Exception e) {
			
			e.printStackTrace();
			throw new DataIntegrityViolationException("SuperDAO update");
		}

	}
	

	/*
	 * To write Save/Update multiple rows (an array of Objects)
	 * 
	 */
	public void saveList(List list) {
		try {
			getHibernateTemplate().saveOrUpdateAll(list);
			getHibernateTemplate().flush();
			getHibernateTemplate().clear();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new DataIntegrityViolationException("SuperDAO saveList");
		}
	}

	public void delete(Object obj) {
		try {
			getHibernateTemplate().delete(obj);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new DataIntegrityViolationException("SuperDAO delete");
		}

	}

	public void deleteList(List list) {
		try {
			getHibernateTemplate().deleteAll(list);			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new DataIntegrityViolationException("SuperDAO deleteList");
		}
		
	}

	public Object fetchByPk(Serializable pk) {
		Object retVal = null;
		try {
					
			retVal = getHibernateTemplate().load(getPojoObj().getClass(), pk);
			
			} 
		catch (Exception e) 
		{			
			// TODO: handle exception
			e.printStackTrace();
		}

		return retVal;
	}

	/*
	 * Used to get type of Object.
	 * This will be Overridden in each of the Class which will return the type of Pojo
	 * 
	 */
	protected abstract Object getPojoObj();
	 	    protected abstract Criteria createCriteria(Object obj);

}