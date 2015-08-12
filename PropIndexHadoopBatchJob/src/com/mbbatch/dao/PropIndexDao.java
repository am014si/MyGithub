package com.mbbatch.dao;

import java.util.List;
import com.mbbatch.domain.Tplrt;

public interface PropIndexDao {
	
	List<Tplrt> generateLocalityReportData(int _year, int _month);

}
