package com.mbbatch.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;

import com.mbbatch.domain.Tpcnd;
import com.mbbatch.domain.Tplmt;
import com.mbbatch.domain.Tplrt;
import com.mbbatch.domain.Tppmt;
import com.mbbatch.domain.Tpsrec;

public class PropIndexDaoHibernate extends SuperDaoHibernate implements PropIndexDao{

	private static final Logger _logger =  Logger.getLogger(PropIndexDaoHibernate.class);
	
	protected Object getPojoObject() {
		return Tplrt.class;
	}
	
	public List<Tplrt> generateLocalityReportData(int year, int month){
		try{
		StringBuffer queryBuffer = new StringBuffer();
		queryBuffer.append("select lrt FROM Tplrt lrt WHERE year(lrt.modidate) = :year and month(lrt.modidate) = :month");
		String[] names = new String[] { "year","month"};
		Object[] params = new Object[] { Integer.valueOf(year), Integer.valueOf(month)};
		return getHibernateTemplate().findByNamedParam(queryBuffer.toString(), names, params);
		}catch(Exception ex){
			_logger.error(ex.getMessage());
			return null;
		}
	}
	
	public List<String> getRFCodesLookupData(){
		 List<String> rfNumCodesList = new ArrayList<String>();
		
		//GetRF NumCodes from TPLMT
		List<Tplmt> temp = getRFCodesDataFromTplmt();
		for(Tplmt tplmt: temp){
			rfNumCodesList.add(String.valueOf(tplmt.getLmtrfnum()) +","+ String.valueOf(tplmt.getLmtname())+","+ String.valueOf(tplmt.getLmtcndcity()));
		}
		System.out.println("RFCodes Data from tplmt "+temp.size());
		//GetRF Numcodes from TPCND
		List<Tpcnd> tempCndList = getRFCodesDataFromTpcnd();
		for(Tpcnd tpcnd: tempCndList){
			rfNumCodesList.add(String.valueOf(tpcnd.getCndrfnum()) +","+ String.valueOf(tpcnd.getCnddesc())+","+null);
		}
		System.out.println("RFCodes Data from tpcnd "+tempCndList.size());
		return rfNumCodesList;
	}

	public List<Tpcnd> getRFCodesDataFromTpcnd(){
	try{
		StringBuffer queryBuffer = new StringBuffer();
		queryBuffer.append("select cnd FROM Tpcnd cnd");
		return getHibernateTemplate().find(queryBuffer.toString());
		}catch(Exception ex){
			_logger.error(ex.getMessage());
			return null;
		}
	}
	
	public List<Tplmt> getRFCodesDataFromTplmt(){
	try{
		StringBuffer queryBuffer = new StringBuffer();
		queryBuffer.append("select lmt FROM Tplmt lmt");
		return getHibernateTemplate().find(queryBuffer.toString());
		}catch(Exception ex){
			//_logger.error(ex.getMessage());
			System.out.println(ex);
			ex.printStackTrace();
			return null;
		}
	}
	
	public List<Tppmt> getLocalityWiseListingData(int year, int month){
	try{
		StringBuffer queryBuffer = new StringBuffer();
		queryBuffer.append("select pmt FROM Tppmt pmt WHERE year(pmt.createdate) = :year and month(pmt.createdate) = :month and pmt.pmtisactive='Y' and pmt.pmtmoderresult != 20202");
		String[] names = new String[] { "year","month"};
		Object[] params = new Object[] { Integer.valueOf(year), Integer.valueOf(month)};
		return getHibernateTemplate().findByNamedParam(queryBuffer.toString(), names, params);
		}catch(Exception ex){
			_logger.error(ex.getMessage());
			return null;
		}
	}
	
	public List<Tpsrec> getLocalityWiseSearchesData(int year, int month){
		try{
			StringBuffer queryBuffer = new StringBuffer();
			queryBuffer.append("select srec FROM Tpsrec srec WHERE year(srec.createdate) = :year and month(srec.createdate) = :month");
			String[] names = new String[] { "year","month"};
			Object[] params = new Object[] { Integer.valueOf(year), Integer.valueOf(month)};
			return getHibernateTemplate().findByNamedParam(queryBuffer.toString(), names, params);
			}catch(Exception ex){
				//_logger.error(ex.getMessage());
				System.out.println(ex.getMessage());
				ex.printStackTrace();
				return null;
			}
		}
	@Override
	protected Criteria createCriteria(Object obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Object getPojoObj() {
		// TODO Auto-generated method stub
		return null;
	}
}
