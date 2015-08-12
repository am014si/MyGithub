/*
 * AbstractDao.java
 *
 * Created on October 14, 2007, 12:48 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.mbbatch.dao;

import java.io.Serializable;
import java.util.List;


/* Data Access Object (DAO) interface.   This is an empty interface
 * used to tag our DAO classes.  Common methods for each interface
 * could be added here.
 *
 * @author Ram (TimesJobs.com)
 */
public interface SuperDao {


	/**
	 * Description : SaveNew used to insert new row to the Database.
	 * @param obj  is Pojo Object 
	 * @return Object
	 */
	public abstract Object saveNew(Object obj);

	public abstract void saveUpdate(Object obj);

	/**
	 * Description : SaveNew used to update the existing row to the Database.
	 * @param obj is Pojo Object 
	 */
	public abstract void update(Object obj);

	/**
	 * Description : To save list of opljo objects into database.
	 * @param list List of Pojo Objedcts.
	 */
	public abstract void saveList(List list);

	/**
	 * Description : Delets an existing row from database. identified by identifier of obj.
	 * @param obj is Pojo Object
	 */
	public abstract void delete(Object obj);

	/**
	 * Description : Delets the existing rows from database. 
	 * @param List of Pojo Objects.
	 */
	public abstract void deleteList(List list);

	public abstract Object fetchByPk(Serializable pk);


}

