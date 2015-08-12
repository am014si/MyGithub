import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class ForumDB2ToMysql {

	private static Connection db2conn;
	private static Connection mysqlconn;
	private static List<ForumDataBean> forumDataBeanList = new ArrayList<ForumDataBean>();
	private static Map<String,String> forumCityMap = new HashMap<String,String>();
	public void createDBConnection(){
		try{
		
			Class.forName("com.ibm.db2.jcc.DB2Driver").newInstance();
	      //String url = "jdbc:db2://10.150.200.201:50000/MBPROD";
	      //db2conn = DriverManager.getConnection(url, "property", "property");
			String url = "jdbc:db2://192.168.206.242:50000/MBLIVEDB";
			db2conn = DriverManager.getConnection(url, "property", "tpmbd@t@");
			
			
	      Class.forName("com.mysql.jdbc.Driver").newInstance();
	      //url = "jdbc:mysql://localhost:3307/jforum";
	      //mysqlconn = DriverManager.getConnection(url, "root", "root");
	      //url = "jdbc:mysql://115.112.206.148:3306/jforum_new";
	      //mysqlconn = DriverManager.getConnection(url, "appuser", "staging@app");
	      
	      url = "jdbc:mysql://localhost:3306/jforum_new";
	      mysqlconn = DriverManager.getConnection(url, "root", "mbforumdb");
	      
	      
			//String url = "jdbc:mysql://dbserver.cs7hgtnps9sx.us-east-1.rds.amazonaws.com:3306/db_gohf";
	      //conn = DriverManager.getConnection(url, "gohf_mb", "8MmcDwPMJX3u5RTR");
	      //getForumData(db2conn);
	     // writeToMysql(mysqlconn);
		} catch (ClassNotFoundException ex) {
			System.err.println(ex.getMessage());
			//customLogFactory.error("Exception in creating connection with DB server ",ex);
		}
	    catch (IllegalAccessException ex) {
	    	System.err.println(ex.getMessage());
	    	//customLogFactory.error("Exception in creating connection with DB server ",ex);
	    }
	    catch (InstantiationException ex) {
	    	System.err.println(ex.getMessage());
	    	//customLogFactory.error("Exception in creating connection with DB server ",ex);
	    }
	    catch (SQLException ex) {
	    	System.err.println(ex.getMessage());
	    	//customLogFactory.error("Exception in creating connection with DB server ",ex);
	    }
	}
	
	public static void main(String[] args) {
		String forumUsrId = null;
		int topicId = 0; int postId = 0;
		ForumDB2ToMysql forumDB2ToMysql = new ForumDB2ToMysql();
		forumDB2ToMysql.createDBConnection();
		forumDB2ToMysql.createForumCityMap();
		
		forumDB2ToMysql.getQuestionsData();
		
		for(ForumDataBean bean: forumDataBeanList){
			
			//Get City and Locality
			forumDB2ToMysql.getCityLocalityData(bean);
			
			//Check user exist in forum
			forumUsrId = forumDB2ToMysql.checkUserExistInForum(bean.getFubiRfNum());
			if(forumUsrId == null || StringUtils.isEmpty(forumUsrId)){
				
				//Create user in forum if it doesn't exist
				forumUsrId = forumDB2ToMysql.createForumUser(bean.getFubiRfNum());
			}
			String forumId = forumCityMap.get(bean.getCity());
			if(forumId == null || forumId.isEmpty()){
				forumId = "1";
			}
			//Create Topic
			topicId = forumDB2ToMysql.createTopic(bean,forumUsrId);
			
			//Create Post Text
			postId = forumDB2ToMysql.createPostText(bean,"Q");
			
			//Create Post
			forumDB2ToMysql.createPost(forumUsrId,forumId,topicId,postId,bean,"Q");
			
			//Answers section
			List<ForumDataBean> forumAnswerDataBeanList = forumDB2ToMysql.getListOfAnswers(bean.getQueRfNum());
			
			//Update Topic replies in topics table
			forumDB2ToMysql.updateTopicReplies(forumAnswerDataBeanList.size(),topicId);
			
			for(ForumDataBean fbean: forumAnswerDataBeanList){
				
				//Check user exist in forum
				forumUsrId = forumDB2ToMysql.checkUserExistInForum(fbean.getAnsUbiRfNum());
				if(forumUsrId == null || StringUtils.isEmpty(forumUsrId)){
					
					//Create user in forum if it doesn't exist
					forumUsrId = forumDB2ToMysql.createForumUser(fbean.getAnsUbiRfNum());
				}
				
				//Update User karma by multiplying AnsLikecount * 5 and add to the existing karma value
				forumDB2ToMysql.updateUserKarma(fbean.getAnsLikeCount(),forumUsrId);
				
				forumId = forumCityMap.get(bean.getCity());
				if(forumId == null || forumId.isEmpty()){
					forumId = "1";
				}
				//Create Topic
				//topicId = createTopic(bean,forumUsrId);
				
				//Create Post Text
				postId = forumDB2ToMysql.createPostText(fbean,"A");
				
				//Create Post
				forumDB2ToMysql.createPost(forumUsrId,forumId,topicId,postId,fbean,"A");
				
			}
			
		}
		
		try{
	    	if(db2conn != null){
	    		db2conn.close();
	    	}
	    	if(mysqlconn != null){
	    		mysqlconn.close();
	    	}
    	}catch(SQLException sqex){
    		//customLogFactory.error("Exception in closing DB connection objects ",sqex);
    	}
	}
	
	public void getCityLocalityData(ForumDataBean bean){
		PreparedStatement smtLocality = null; ResultSet rsLocality = null;
		StringBuffer localitybuff = new StringBuffer();
		localitybuff.append("select lmt.LMTNAME from TPLMT lmt where lmt.LMTRFNUM = ? ");
		PreparedStatement smtCity = null; ResultSet rsCity = null;
		StringBuffer citybuff = new StringBuffer();
		citybuff.append("select cnd.CNDDESC from TPCND cnd where cnd.CNDRFNUM = ? ");
		PreparedStatement smtCityLocality = null; ResultSet rsCityLocality = null;
		StringBuffer localityCityBuff = new StringBuffer();
		localityCityBuff.append("select lmt.LMTCNDCITY from TPLMT lmt where lmt.LMTRFNUM = ? ");
		try{
			if(bean.getLmtRfNum() != null){
				String localityName = null;
				smtLocality = db2conn.prepareStatement(localitybuff.toString());
				smtLocality.setString(1,bean.getLmtRfNum());
				rsLocality = smtLocality.executeQuery();
				while(rsLocality.next()){
					localityName = rsLocality.getString("LMTNAME");
				}
				bean.setLocality(localityName);
			}
			if(bean.getCityRfNum() != null){
				String cityName = null;
				smtCity = db2conn.prepareStatement(citybuff.toString());
				smtCity.setString(1,bean.getCityRfNum());
				rsCity = smtCity.executeQuery();
				while(rsCity.next()){
					cityName = rsCity.getString("CNDDESC");
				}
				bean.setCity(cityName);
			}
			if(bean.getCityRfNum() == null && bean.getLmtRfNum() != null){
				String cityRfNum = null; String cityName = null;
				smtCityLocality = db2conn.prepareStatement(localityCityBuff.toString());
				smtCityLocality.setString(1, bean.getLmtRfNum());
				rsCityLocality = smtCityLocality.executeQuery();
				while(rsCityLocality.next()){
					cityRfNum = rsCityLocality.getString("LMTCNDCITY");
					bean.setCityRfNum(cityRfNum);
				}
				smtCity = db2conn.prepareStatement(citybuff.toString());
				smtCity.setString(1,cityRfNum);
				rsCity = smtCity.executeQuery();
				while(rsCity.next()){
					cityName = rsCity.getString("CNDDESC");
				}
				bean.setCity(cityName);
			}
			if(bean.getCityRfNum() != null && bean.getLmtRfNum() == null){
				bean.setLocality("");
			}
		}catch (SQLException ex) {
		      System.err.println(ex.getMessage());
		      //customLogFactory.error("Exception while update in DB",ex);
	    }finally{
	    	try{
		    	if(smtCity != null){
		    		smtCity.close();
		    	}
		    	if(smtCityLocality != null){
		    		smtCityLocality.close();
		    	}
		    	if(smtLocality != null){
		    		smtLocality.close();
		    	}
		    	}catch(SQLException sqe){
		    	}
		    }
	}
	
	public void updateTopicReplies(int replyCount, int topicId){
		PreparedStatement smt = null;
		StringBuffer buff = new StringBuffer();
		buff.append("Update jforum_topics j set j.topic_replies = ? where j.topic_id = ?");
		try{
			smt = mysqlconn.prepareStatement(buff.toString());
			smt.setInt(1,replyCount);
			smt.setInt(2,topicId);
			smt.executeUpdate();
		}catch (SQLException ex) {
		      System.err.println(ex.getMessage());
		      //customLogFactory.error("Exception while update in DB",ex);
	    }finally{
	    	try{
		    	if(smt != null){
		    		smt.close();
		    	}}catch(SQLException sqe){
		    	}
		    }
	}
	
	public void updateUserKarma(int likeCount,String forumUsrId){
		StringBuffer karmaBuff = new StringBuffer();
		StringBuffer usrBuff = new StringBuffer();
		PreparedStatement stmt = null; PreparedStatement ustmt = null;
		ResultSet rs = null; ResultSet urs = null; int userKarma = 0;
		usrBuff.append("select user_karma from jforum_users where user_id = ? ");
		karmaBuff.append("Update jforum_users j set j.user_karma = ? where j.user_id = ?");
		try
	    {
	    	ustmt = mysqlconn.prepareStatement(usrBuff.toString());
	    	ustmt.setInt(1,Integer.parseInt(forumUsrId));
	    	urs = ustmt.executeQuery();
	    	while(urs.next()){
	    		userKarma = urs.getInt("user_karma");
	    	}
	    }catch (SQLException ex) {
		      System.err.println(ex.getMessage());
		      //customLogFactory.error("Exception while update in DB",ex);
	    }finally{
	    	try{
	    		if(urs != null){
	    			urs.close();
		    	}
		    	if(ustmt != null){
		    		ustmt.close();
		    	}}catch(SQLException sqe){
		    	}
		    }
	    userKarma = userKarma + (5*likeCount);
	    try {
	    	stmt = mysqlconn.prepareStatement(karmaBuff.toString());
	    	stmt.setInt(1,userKarma);
	    	stmt.setInt(2,Integer.parseInt(forumUsrId));
	    	stmt.executeUpdate();
	    }catch (SQLException e) {
		      System.err.println(e.getMessage());
		      //customLogFactory.error("Exception while update in DB",ex);
	    }finally{
	    	try{
		    	if(stmt != null){
		    		stmt.close();
		    	}}catch(SQLException sqe){
		    	}
		    }
	}
	
	public List<ForumDataBean> getListOfAnswers(String queRfNum){
		PreparedStatement psmt = null;
		List<ForumDataBean> forumAnswerDataBeanList = new ArrayList<ForumDataBean>();
		StringBuffer sbf = new StringBuffer(); ResultSet rs = null; ForumDataBean forumBean = null;
		sbf.append("select ans.MBFANSUBIRFNUM , ans.MBFANSRFNUM, ans.MBFLIKECOUNT, ans.MBFANSDESC, ans.MBFANSISACTIVE, ans.MBFANSISFIRST, ");
		sbf.append("ans.CREATEDATE from MBFANS ans where ans.MBFANSQUERFNUM = ?");
		try{
		psmt = db2conn.prepareStatement(sbf.toString());
		psmt.setString(1,queRfNum);
		rs = psmt.executeQuery();
		while(rs.next()){
			forumBean = new ForumDataBean();
			forumBean.setAnsUbiRfNum(rs.getString("MBFANSUBIRFNUM"));
			forumBean.setAnsRfNum(rs.getString("MBFANSRFNUM"));
			forumBean.setAnsLikeCount(rs.getInt("MBFLIKECOUNT"));
			forumBean.setAnsDesc(rs.getString("MBFANSDESC"));
			forumBean.setAnsIsActive(rs.getString("MBFANSISACTIVE"));
			forumBean.setAnsIsFirst(rs.getString("MBFANSISFIRST"));
			forumBean.setAnsCreateDate(rs.getTimestamp("CREATEDATE"));
			forumAnswerDataBeanList.add(forumBean);
		}
		}catch (SQLException ex) {
		      System.err.println(ex.getMessage());
		      //customLogFactory.error("Exception while update in DB",ex);
	    }finally{
	    	try{
	    		if(rs != null){
	    			rs.close();
		    	}
		    	if(psmt != null){
		    		psmt.close();
		    	}}catch(SQLException sqe){
		    	}
		    }
		return forumAnswerDataBeanList;
	}
	
	public void createPost(String forumUsrId,String forumId,int topicId,int postId, ForumDataBean bean, String queOrAns){
		StringBuffer postBuffer = new StringBuffer();
		PreparedStatement stmt = null;
		postBuffer.append("INSERT INTO jforum_posts (post_id, topic_id, forum_id, user_id, post_time, ");
		postBuffer.append(" post_edit_time, is_answer) ");
		postBuffer.append("VALUES (?, ?, ?, ?, ?, ?, ?)");
		try
	    {
	    	stmt = mysqlconn.prepareStatement(postBuffer.toString());
	    	stmt.setInt(1,postId);
	    	stmt.setInt(2,topicId);
	    	stmt.setInt(3,Integer.parseInt(forumId));
	    	stmt.setInt(4,Integer.parseInt(forumUsrId));
	    	
	    	if(queOrAns != null && queOrAns.equalsIgnoreCase("Q")){
	    		stmt.setTimestamp(5,bean.getCreateDate());
		    	stmt.setTimestamp(6,bean.getCreateDate());
	    		stmt.setString(7,"N");
	    	} else {
	    		stmt.setTimestamp(5,bean.getAnsCreateDate());
		    	stmt.setTimestamp(6,bean.getAnsCreateDate());
	    		stmt.setString(7,"Y");
	    	}
	    	stmt.executeUpdate();
	    }catch (SQLException ex) {
		      System.err.println(ex.getMessage());
		      //customLogFactory.error("Exception while update in DB",ex);
	    }finally{
	    	try{
		    	if(stmt != null){
		    		stmt.close();
		    	}}catch(SQLException sqe){
		    	}
		    }
	    
	    
	    //Update Topics table with first Post Id in case of questions
	    if(queOrAns != null && queOrAns.equalsIgnoreCase("Q")){
		    PreparedStatement stmt1 = null; ResultSet rs1 = null;
		    StringBuffer buff1 = new StringBuffer();
		    buff1.append("Update jforum_topics j set j.topic_first_post_id = ?, j.topic_last_post_id = ? where j.topic_id = ?");
		    try{
		    	stmt1 = mysqlconn.prepareStatement(buff1.toString());
		    	stmt1.setInt(1, postId);
		    	stmt1.setInt(2, postId);
		    	stmt1.setInt(3, topicId);
		    	stmt1.executeUpdate();
		    }catch (SQLException ex1) {
			      System.err.println(ex1.getMessage());
			      //customLogFactory.error("Exception while update in DB",ex);
		    }finally{
		    	try{
			    	if(stmt1 != null){
			    		stmt1.close();
			    	}}catch(SQLException sqe){
			    	}
			    }
		    
		    //Increment forum_topics in forums table in case of Questions only
		    PreparedStatement stmt2 = null; ResultSet rs2 = null;
		    PreparedStatement fstmt = null; ResultSet frs = null;
		    StringBuffer buff2 = new StringBuffer(); StringBuffer fbuff = new StringBuffer();
		    fbuff.append("select j.forum_topics from jforum_forums j where j.forum_id = ?");
		    buff2.append("Update jforum_forums j set j.forum_topics = ?, j.forum_last_post_id = ? where j.forum_id = ?");
		    int forumTopics = 0;
		    try{
		    	fstmt = mysqlconn.prepareStatement(fbuff.toString());
		    	fstmt.setInt(1, Integer.parseInt(forumId));
		    	frs = fstmt.executeQuery();
		    	while(frs.next()){
		    		forumTopics = frs.getInt("forum_topics");
		    	}
		    }catch (SQLException ex1) {
			      System.err.println(ex1.getMessage());
			      //customLogFactory.error("Exception while update in DB",ex);
		    }finally{
		    	try{
			    	if(fstmt != null){
			    		fstmt.close();
			    	}}catch(SQLException sqe){
			    	}
			    }
		    forumTopics = forumTopics + 1;
		    try{
		    	stmt2 = mysqlconn.prepareStatement(buff2.toString());
		    	stmt2.setInt(1, forumTopics);
		    	stmt2.setInt(2, postId);
		    	stmt2.setInt(3, Integer.parseInt(forumId));
		    	stmt2.executeUpdate();
		    }catch (SQLException ex1) {
			      System.err.println(ex1.getMessage());
			      //customLogFactory.error("Exception while update in DB",ex);
		    }finally{
		    	try{
			    	if(stmt2 != null){
			    		stmt2.close();
			    	}}catch(SQLException sqe){
			    	}
			    }
	    }
	    
	    //Update forum table with last post id and Topics table with last post Id in case post is an answer.
	    if(queOrAns != null && queOrAns.equalsIgnoreCase("A")){
	    	PreparedStatement stmt1 = null; ResultSet rs1 = null;
		    StringBuffer buff1 = new StringBuffer();
		    buff1.append("Update jforum_topics j set j.topic_last_post_id = ? where j.topic_id = ?");
		    try{
		    	stmt1 = mysqlconn.prepareStatement(buff1.toString());
		    	stmt1.setInt(1, postId);
		    	stmt1.setInt(2, topicId);
		    	stmt1.executeUpdate();
		    }catch (SQLException ex1) {
			      System.err.println(ex1.getMessage());
			      //customLogFactory.error("Exception while update in DB",ex);
		    }finally{
		    	try{
			    	if(stmt1 != null){
			    		stmt1.close();
			    	}}catch(SQLException sqe){
			    	}
			    }
		    
		    PreparedStatement fstmt = null; ResultSet frs = null;
		    StringBuffer buff2 = new StringBuffer();
		    buff2.append("Update jforum_forums j set j.forum_last_post_id = ? where j.forum_id = ?");
		    try{
		    	fstmt = mysqlconn.prepareStatement(buff2.toString());
		    	fstmt.setInt(1, postId);
		    	fstmt.setInt(2, Integer.parseInt(forumId));
		    	fstmt.executeUpdate();
		    }catch (SQLException ex1) {
			      System.err.println(ex1.getMessage());
			      //customLogFactory.error("Exception while update in DB",ex);
		    }finally{
		    	try{
			    	if(fstmt != null){
			    		fstmt.close();
			    	}}catch(SQLException sqe){
			    	}
			    }
	    }
	   
	}
	
	public int createPostText(ForumDataBean bean, String qOrA){
		StringBuffer postBuffer = new StringBuffer();
		PreparedStatement stmt = null;String topicTitle = null;int postId = 0;
		PreparedStatement selectStmt = null; ResultSet selectrs = null;StringBuffer selectBuff = new StringBuffer();
		selectBuff.append("Select post_id from jforum_posts_text order by post_id desc");
		try{
			selectStmt = mysqlconn.prepareStatement(selectBuff.toString());
			selectrs = selectStmt.executeQuery();
			while(selectrs.next()){
				postId = selectrs.getInt("post_id");
				break;
			}
		}catch (SQLException ex1) {
		      System.err.println(ex1.getMessage());
		      //customLogFactory.error("Exception while update in DB",ex);
	    }finally{
	    	try{
	    		if(selectrs != null){
	    			selectrs.close();
		    	}
		    	if(selectStmt != null){
		    		selectStmt.close();
		    	}}catch(SQLException sqe){
		    	}
		    }
		postId = postId+1;int len =0;
		postBuffer.append("INSERT INTO jforum_posts_text (  post_text, post_subject, post_id ) VALUES (?, ?, ?)");
		try
	    {
	    	stmt = mysqlconn.prepareStatement(postBuffer.toString());
	    	if(qOrA != null && qOrA.equalsIgnoreCase("Q")){
		    	stmt.setString(1, bean.getQueDesc());
		    	//100 characters
		    	if(bean.getQueDesc() != null && !bean.getQueDesc().isEmpty()){
		    		if(bean.getQueDesc().length() >= 100){
		    			len = 99;
		    		} else {
		    			len = bean.getQueDesc().length();
		    		}
		    		topicTitle = bean.getQueDesc().substring(0, len);
		    	}
		    	stmt.setString(2,topicTitle);
		    	stmt.setInt(3,postId);
	    	} else if(qOrA != null && qOrA.equalsIgnoreCase("A")){
	    		stmt.setString(1, bean.getAnsDesc());
	    		//100 characters
	    		if(bean.getAnsDesc() != null && !bean.getAnsDesc().isEmpty()){
		    		if(bean.getAnsDesc().length() >= 100){
		    			len = 99;
		    		} else {
		    			len = bean.getAnsDesc().length();
		    		}
		    		topicTitle = bean.getAnsDesc().substring(0, len);
		    	}
		    	stmt.setString(2,topicTitle);
		    	stmt.setInt(3,postId);
	    	}
	    	stmt.executeUpdate();
	    	/*ResultSet rs = stmt.getGeneratedKeys();
	    	if(rs.next()){
	    		postId = rs.getInt(1);
	    	}*/
	    }catch (SQLException ex) {
		      System.err.println(ex.getMessage());
		      //customLogFactory.error("Exception while update in DB",ex);
	    }finally{
	    	try{
		    	if(stmt != null){
		    		stmt.close();
		    	}}catch(SQLException sqe){
		    	}
		    }
	    //Update Topic first post id and last post ID after a post is created
		return postId;
	}
	
	public void createForumCityMap(){
		PreparedStatement psmt = null;
		StringBuffer sbf = new StringBuffer(); ResultSet rs = null;
		sbf.append("select forum_id, forum_name from jforum_forums");
		try{
		psmt = mysqlconn.prepareStatement(sbf.toString());
		rs = psmt.executeQuery();
		while(rs.next()){
			forumCityMap.put(rs.getString("forum_name"),rs.getString("forum_id"));
		}
		}catch (SQLException ex) {
		      System.err.println(ex.getMessage());
		      //customLogFactory.error("Exception while update in DB",ex);
	    }finally{
	    	try{
	    		if(rs != null){
	    			rs.close();
		    	}
	    	if(psmt != null){
	    		psmt.close();
	    	}}catch(SQLException sqe){
	    	}
	    }
	}
	
	public int createTopic(ForumDataBean bean, String forumUsrId){
		StringBuffer topicBuffer = new StringBuffer();
		PreparedStatement stmt = null;String topicTitle = null;
		topicBuffer.append(" INSERT INTO jforum_topics (forum_id, topic_title, user_id, topic_time, ");
		topicBuffer.append("topic_type, moderated,topic_city,topic_locality,topic_from_mb,topic_views,topic_projects,tags,moderation_reason");
		topicBuffer.append(") VALUES (?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?) ");
		String forumId = forumCityMap.get(bean.getCity());
		if(forumId == null || forumId.isEmpty()){
			forumId = "1";
		}
		int topicId = 0;int len =0;
		try
	    {
	    	stmt = mysqlconn.prepareStatement(topicBuffer.toString());
	    	stmt.setInt(1, Integer.parseInt(forumId));
	    	
	    	//100 characters only
	    	if(bean.getQueDesc() != null && !bean.getQueDesc().isEmpty()){
	    		if(bean.getQueDesc().length() >= 100){
	    			len = 99;
	    		} else {
	    			len = bean.getQueDesc().length();
	    		}
	    		topicTitle = bean.getQueDesc().substring(0, len);
	    	}
	    	stmt.setString(2, topicTitle);
	    	stmt.setString(3,forumUsrId);
	    	stmt.setTimestamp(4, bean.getCreateDate());
	    	stmt.setInt(5, 0);
	    	stmt.setInt(6, 0);
	    	stmt.setString(7, bean.getCity());
	    	stmt.setString(8, bean.getLocality());
	    	stmt.setInt(9,1);
	    	stmt.setInt(10,Integer.parseInt(bean.getViewedCnt()));
	    	stmt.setString(11,"");
	    	stmt.setString(12,"");
	    	stmt.setString(13,"");
	    	stmt.executeUpdate();
	    	ResultSet rs = stmt.getGeneratedKeys();
	    	if(rs.next()){
	    		topicId = rs.getInt(1);
	    	}
	    }catch (SQLException ex) {
		      System.err.println(ex.getMessage());
		      //customLogFactory.error("Exception while update in DB",ex);
	    }finally{
	    	try{
		    	if(stmt != null){
		    		stmt.close();
		    	}}catch(SQLException sqe){
		    	}
		    }
	    //Update Topic first post id and last post ID after a post is created
		return topicId;
	}
	
	public String createForumUser(String fubiUsrId){
		PreparedStatement stmt = null;
	    StringBuffer queryBuffer = new StringBuffer();
	    ResultSet rs = null;
	    //String forumUsrId = null;
	    StringBuffer getMBfUserBuffer = new StringBuffer();
	    getMBfUserBuffer.append("select ubi.UBILOGIN, ubi.UBIPASS, ubi.UBIFNAME, ubi.UBIMNAME, ubi.UBILNAME, ubi.UBICNDCITY, ubi.UBICNDSTATE, ubi.UBICNDCOUNTRY, fubi.MBFPOSTCOUNT, fubi.MBFANSCOUNT, ");
		getMBfUserBuffer.append("fubi.CREATEDATE, fubi.ISEXPERT, ubi.UBIADDR1, ubi.UBIEMAIL, ubi.UBIRFNUM, ubi.UBIUSERTYPE, fubi.MBFISACTIVE ");
		getMBfUserBuffer.append(" from MBFUBI fubi, TPUBI ubi where fubi.MBFUBIRFNUM = ubi.UBIRFNUM and fubi.MBFRFNUM = ? ");
		String ubiLogin = null;String ubiPass = null;String ubiFName=null;String ubiMName=null;String ubiLName=null;String ubiCity=null;String ubiState=null;
		String ubiCountry=null;String ubiPostCnt=null;String ubiAnsCnt=null;String ubiregDate=null;String ubiIsExprt=null;String ubiAddr=null;String ubiEmail=null;
		String ubiRfNum=null;String ubiUsrType=null;String ubiUsrActive=null;
		
		try
	    {
	    	stmt = db2conn.prepareStatement(getMBfUserBuffer.toString());
	    	stmt.setInt(1, Integer.parseInt(fubiUsrId));
	    	rs = stmt.executeQuery();
		    while(rs.next()){
		    	ubiLogin = rs.getString("UBILOGIN");
		    	ubiPass = rs.getString("UBIPASS");
		    	ubiFName = rs.getString("UBIFNAME");
		    	ubiMName = rs.getString("UBIMNAME");
		    	ubiLName = rs.getString("UBILNAME");
		    	ubiCity = rs.getString("UBICNDCITY");
		    	ubiState = rs.getString("UBICNDSTATE");
		    	ubiCountry = rs.getString("UBICNDCOUNTRY");
		    	ubiPostCnt = rs.getString("MBFPOSTCOUNT");
		    	ubiAnsCnt = rs.getString("MBFANSCOUNT");
		    	ubiregDate = rs.getTimestamp("CREATEDATE").toString();
		    	
		    	ubiIsExprt = rs.getString("ISEXPERT");
		    	ubiAddr = rs.getString("UBIADDR1");
		    	ubiEmail = rs.getString("UBIEMAIL");
		    	ubiRfNum = rs.getString("UBIRFNUM");
		    	ubiUsrType = rs.getString("UBIUSERTYPE");
		    	ubiUsrActive = rs.getString("MBFISACTIVE");
		    }
	    }catch (SQLException ex) {
		      System.err.println(ex.getMessage());
		      //customLogFactory.error("Exception while update in DB",ex);
	    }finally{
	    	try{
	    		if(rs != null){
	    			rs.close();
		    	}
		    	if(stmt != null){
		    		stmt.close();
		    	}}catch(SQLException sqe){
		    	}
		    }
		
	  //If user is inactive means !='Y' then mark deleted as 1 in Mysql
	    //If user is expert means == 'Y' put rank as 3
		int rankId = 0;
		if(ubiIsExprt != null && ubiIsExprt.trim().equalsIgnoreCase("Y")){
			rankId = 3;
		}
		int deleted = 0;
		if(ubiUsrActive != null && !ubiUsrActive.trim().equalsIgnoreCase("Y")){
			deleted = 1;
		}
	    int userCreated = 0;
	    String createdUserId = null;
	    PreparedStatement pstmt = null;
		queryBuffer.append("INSERT INTO jforum_users (username,firstName,lastName,middleName,userType,address,mbStateCode,");
		queryBuffer.append("mbCityCode,mbCountryCode, user_password, user_email, user_regdate, user_posts, rank_id, deleted,mb_userRefNo) ");
		queryBuffer.append("VALUES (?, ?, ?, ?, ?,?, ?, ?, ?, ?,?, ?, ?, ?, ?,?) ");
	    try
	    {
	    	pstmt = mysqlconn.prepareStatement(queryBuffer.toString());
	    	if(ubiLogin == null || ubiLogin.isEmpty()){
	    		ubiLogin = "magicbricks123";
	    	}
	    	if(ubiPass == null || ubiPass.isEmpty()){
	    		ubiPass = ubiLogin;
	    	}
	    	if(ubiLName != null && !ubiLName.isEmpty()){
	    		if(ubiLName.length() >= 50){
	    			ubiLName = ubiLName.substring(0, 49);
	    		}
	    	}
	    	if(ubiEmail == null || ubiEmail.isEmpty()){
	    		ubiEmail = "";
	    	}
	    	pstmt.setString(1, ubiLogin);
	    	pstmt.setString(2, ubiFName);
	    	pstmt.setString(3, ubiLName);
	    	pstmt.setString(4, ubiMName);
	    	pstmt.setString(5, ubiUsrType);
	    	pstmt.setString(6, ubiAddr);pstmt.setString(7, ubiState);pstmt.setString(8, ubiCity);pstmt.setString(9, ubiCountry);pstmt.setString(10, ubiPass);
	    	pstmt.setString(11, ubiEmail);pstmt.setString(12, ubiregDate);pstmt.setInt(14, rankId);pstmt.setInt(15, deleted);
	    	int postCnt = 0;int ansCnt = 0;
	    	try{
		    	postCnt = Integer.parseInt(ubiPostCnt);
		    	ansCnt = Integer.parseInt(ubiAnsCnt);
	    	}catch(NumberFormatException excep){
	    	}
	    	pstmt.setInt(13, (postCnt+ansCnt));
	    	
	    	pstmt.setString(16, fubiUsrId);
	    	userCreated = pstmt.executeUpdate();
		    if(userCreated > 0){
		    	PreparedStatement ptstmt = null;
		    	ResultSet rs1 = null;
		    	ptstmt = mysqlconn.prepareStatement("Select user_id from jforum_users where mb_userRefNo = ?");
		    	ptstmt.setString(1,fubiUsrId);
		    	rs1 = ptstmt.executeQuery();
		    	while(rs1.next()){
		    		createdUserId = rs1.getString("user_id");
		    	}
		    	
		    }
	    }catch (SQLException ex) {
		      System.err.println(ex.getMessage());
		      //customLogFactory.error("Exception while update in DB",ex);
	    }finally{
	    	try{
		    	if(pstmt != null){
		    		pstmt.close();
		    	}}catch(SQLException sqe){
		    	}
		    }
	    //Insert into users_groups
	    StringBuffer ugBuffer = new StringBuffer();
	    PreparedStatement mt = null;
	    ugBuffer.append("INSERT INTO jforum_user_groups ( user_id, group_id ) VALUES ( ?, ? )");
	    try
	    {
	    	mt = mysqlconn.prepareStatement(ugBuffer.toString());
	    	mt.setInt(1, Integer.parseInt(createdUserId));
	    	mt.setInt(2, 1);
	    	mt.executeUpdate();
	    }catch (SQLException ex) {
		      System.err.println(ex.getMessage());
		      //customLogFactory.error("Exception while update in DB",ex);
	    }finally{
	    	try{
		    	if(mt != null){
		    		mt.close();
		    	}}catch(SQLException sqe){
		    	}
		    }
		return createdUserId;
	}
	
	public String checkUserExistInForum(String usrId){
		PreparedStatement stmt = null;
	    StringBuffer queryBuffer = new StringBuffer();
	    ResultSet rs = null;
	    String forumUsrId = null;
	    queryBuffer.append("select user_id from jforum_users users where users.mb_userRefNo = ? ");
	    try
	    {
	    	stmt = mysqlconn.prepareStatement(queryBuffer.toString());
	    	stmt.setString(1, usrId);
	    	rs = stmt.executeQuery();
		    while(rs.next()){
		    	forumUsrId = rs.getString("user_id");
		    }
	    }catch (SQLException ex) {
		      System.err.println(ex.getMessage());
		      //customLogFactory.error("Exception while update in DB",ex);
	    }finally{
	    	try{
	    		if(rs != null){
	    			rs.close();
		    	}
		    	if(stmt != null){
		    		stmt.close();
		    	}}catch(SQLException sqe){
		    	}
		    }
		return forumUsrId;    
	}
	
	public void getQuestionsData(){
		PreparedStatement stmt = null;
		ForumDataBean forumDataBean = null;
	    StringBuffer queryBuffer = new StringBuffer();
	    ResultSet rs = null;
	    //queryBuffer.append("select que.MBFQUERFNUM, que.MBFQUEVIEWED, que.MBFQUEDESC, cnd.CNDDESC, que.MBFQUEUBIRFNUM, que.CREATEDATE, que.MBFQUEVIEWED, ");
		//queryBuffer.append("lmt.LMTNAME from MBFQUE que, TPCND cnd, TPLMT lmt  where que.MBFQUEISACTIVE = 'Y' and ");
		//queryBuffer.append(" que.MBFQUECNDCITY = cnd.CNDRFNUM and que.MBFQUELMTRFNUM = lmt.LMTRFNUM ");
	    
	    queryBuffer.append("select que.MBFQUERFNUM, que.MBFQUEVIEWED, que.MBFQUEDESC, que.MBFQUEUBIRFNUM, que.CREATEDATE, que.MBFQUEVIEWED, que.MBFQUELMTRFNUM, que.MBFQUECNDCITY ");
		queryBuffer.append("from MBFQUE que where que.MBFQUEISACTIVE = 'Y' ");
	    try
	    {
	    	stmt = db2conn.prepareStatement(queryBuffer.toString());
	    	rs = stmt.executeQuery();
		      while (rs.next())
		      {
		    	  forumDataBean = new ForumDataBean();
		    	  //forumDataBean.setLikeCnt(rs.getString("MBFLIKECOUNT"));
		    	  forumDataBean.setCityRfNum(rs.getString("MBFQUECNDCITY"));
		    	  forumDataBean.setLmtRfNum(rs.getString("MBFQUELMTRFNUM"));
		    	  forumDataBean.setQueRfNum(rs.getString("MBFQUERFNUM"));
		    	  //forumDataBean.setAnsQueRfNum(rs.getString("MBFANSQUERFNUM"));
		    	  forumDataBean.setViewedCnt(rs.getString("MBFQUEVIEWED"));
		    	  forumDataBean.setQueDesc(rs.getString("MBFQUEDESC"));
		    	  forumDataBean.setViewedCnt(rs.getString("MBFQUEVIEWED"));
		    	  //forumDataBean.setCity(rs.getString("CNDDESC"));
		    	  //forumDataBean.setLocality(rs.getString("LMTNAME"));
		    	  forumDataBean.setFubiRfNum(rs.getString("MBFQUEUBIRFNUM"));
		    	  forumDataBean.setCreateDate(rs.getTimestamp("CREATEDATE"));
		    	  forumDataBeanList.add(forumDataBean);
	    	  }
		    
	    }
	    catch (SQLException ex) {
	      System.err.println(ex.getMessage());
	      //customLogFactory.error("Exception while update in DB",ex);
	    }finally{
	    	try{
	    		if(rs != null){
	    			rs.close();
		    	}
		    	if(stmt != null){
		    		stmt.close();
		    	}}catch(SQLException sqe){
		    	}
		    }
	    //return forumDataBeanList;
	}
	
	
	
	
	 public void getForumUsersData(Connection conn){
		PreparedStatement stmt = null;
		ForumDataBean forumDataBean = null;
	    StringBuffer queryBuffer = new StringBuffer();
	    ResultSet rs = null;
		queryBuffer.append("select ubi.UBILOGIN, ubi.UBIPASS, ubi.UBIFNAME, ubi.UBIMNAME, ubi.UBILNAME, ubi.UBICNDCITY, ubi.UBICNDSTATE, ubi.UBICNDCOUNTRY, fubi.MBFPOSTCOUNT, fubi.MBFANSCOUNT, ");
		queryBuffer.append("fubi.CREATEDATE, fubi.ISEXPERT, ubi.UBIADDR1, ubi.UBIEMAIL, ubi.UBIRFNUM, ubi.UBIUSERTYPE, fubi.MBFISACTIVE ");
		queryBuffer.append(" from MBFUBI fubi, TPUBI ubi where fubi.MBFUBIRFNUM = ubi.UBIRFNUM");
	    try
	    {
	    	stmt = conn.prepareStatement(queryBuffer.toString());
	    	rs = stmt.executeQuery();
		      while (rs.next())
		      {
		    	  forumDataBean = new ForumDataBean();
		    	  forumDataBean.setLikeCnt(rs.getString("MBFLIKECOUNT"));
		    	  forumDataBean.setQueRfNum(rs.getString("MBFQUERFNUM"));
		    	  forumDataBean.setAnsQueRfNum(rs.getString("MBFANSQUERFNUM"));
		    	  forumDataBean.setViewedCnt(rs.getString("MBFQUEVIEWED"));
		    	  forumDataBean.setQueDesc(rs.getString("MBFQUEDESC"));
		    	  forumDataBean.setAnsDesc(rs.getString("MBFANSDESC"));
		    	  forumDataBean.setCity(rs.getString("CNDDESC"));
		    	  forumDataBean.setLocality(rs.getString("LMTNAME"));
		    	  forumDataBeanList.add(forumDataBean);
	    	  }
		    
	    }
	    catch (SQLException ex) {
	      System.err.println(ex.getMessage());
	      //customLogFactory.error("Exception while update in DB",ex);
	    }finally{
	    	try{
		    	if(stmt != null){
		    		stmt.close();
		    	}
		    	/*if(conn != null){
		    		conn.close();
		    	}*/
	    	}catch(SQLException sqex){
	    		//customLogFactory.error("Exception in closing DB connection objects ",sqex);
	    	}
	    }
	}
	 
	 
	public void getForumTopicsData(Connection conn){
		PreparedStatement stmt = null;
		ForumDataBean forumDataBean = null;
	    StringBuffer queryBuffer = new StringBuffer();
	    ResultSet rs = null;
		queryBuffer.append("select que.MBFQUERFNUM, que.MBFQUEVIEWED, que.MBFQUEDESC, cnd.CNDDESC,  ");
		queryBuffer.append("lmt.LMTNAME from MBFQUE que, TPCND cnd, TPLMT lmt  where que.MBFQUEISACTIVE = 'Y' and ");
		queryBuffer.append(" que.MBFQUECNDCITY = cnd.CNDRFNUM and que.MBFQUELMTRFNUM = lmt.LMTRFNUM ");
	    try
	    {
	    	stmt = conn.prepareStatement(queryBuffer.toString());
	    	rs = stmt.executeQuery();
		      while (rs.next())
		      {
		    	  forumDataBean = new ForumDataBean();
		    	  forumDataBean.setLikeCnt(rs.getString("MBFLIKECOUNT"));
		    	  forumDataBean.setQueRfNum(rs.getString("MBFQUERFNUM"));
		    	  forumDataBean.setAnsQueRfNum(rs.getString("MBFANSQUERFNUM"));
		    	  forumDataBean.setViewedCnt(rs.getString("MBFQUEVIEWED"));
		    	  forumDataBean.setQueDesc(rs.getString("MBFQUEDESC"));
		    	  forumDataBean.setAnsDesc(rs.getString("MBFANSDESC"));
		    	  forumDataBean.setCity(rs.getString("CNDDESC"));
		    	  forumDataBean.setLocality(rs.getString("LMTNAME"));
		    	  forumDataBeanList.add(forumDataBean);
	    	  }
		    
	    }
	    catch (SQLException ex) {
	      System.err.println(ex.getMessage());
	      //customLogFactory.error("Exception while update in DB",ex);
	    }finally{
	    	try{
		    	if(stmt != null){
		    		stmt.close();
		    	}
		    	/*if(conn != null){
		    		conn.close();
		    	}*/
	    	}catch(SQLException sqex){
	    		//customLogFactory.error("Exception in closing DB connection objects ",sqex);
	    	}
	    }
	}
	
	public void getForumData(Connection conn){
		PreparedStatement stmt = null;
		ForumDataBean forumDataBean = null;
	    StringBuffer queryBuffer = new StringBuffer();
	    ResultSet rs = null;
		queryBuffer.append("select ans.MBFLIKECOUNT, que.MBFQUERFNUM, ans.MBFANSQUERFNUM, que.MBFQUEVIEWED, que.MBFQUEDESC, ans.MBFANSDESC, cnd.CNDDESC,  ");
		queryBuffer.append("lmt.LMTNAME from MBFANS ans, MBFQUE que, TPCND cnd, TPLMT lmt  where ans.MBFANSISACTIVE = 'Y' and que.MBFQUEISACTIVE = 'Y' and ");
		queryBuffer.append(" ans.MBFANSQUERFNUM = que.MBFQUERFNUM and que.MBFQUECNDCITY = cnd.CNDRFNUM and que.MBFQUELMTRFNUM = lmt.LMTRFNUM ");
	    try
	    {
	    	stmt = conn.prepareStatement(queryBuffer.toString());
	    	rs = stmt.executeQuery();
		      while (rs.next())
		      {
		    	  forumDataBean = new ForumDataBean();
		    	  forumDataBean.setLikeCnt(rs.getString("MBFLIKECOUNT"));
		    	  forumDataBean.setQueRfNum(rs.getString("MBFQUERFNUM"));
		    	  forumDataBean.setAnsQueRfNum(rs.getString("MBFANSQUERFNUM"));
		    	  forumDataBean.setViewedCnt(rs.getString("MBFQUEVIEWED"));
		    	  forumDataBean.setQueDesc(rs.getString("MBFQUEDESC"));
		    	  forumDataBean.setAnsDesc(rs.getString("MBFANSDESC"));
		    	  forumDataBean.setCity(rs.getString("CNDDESC"));
		    	  forumDataBean.setLocality(rs.getString("LMTNAME"));
		    	  forumDataBeanList.add(forumDataBean);
	    	  }
		    
	    }
	    catch (SQLException ex) {
	      System.err.println(ex.getMessage());
	      //customLogFactory.error("Exception while update in DB",ex);
	    }finally{
	    	/*try{
		    	if(stmt != null){
		    		stmt.close();
		    	}
		    	if(conn != null){
		    		conn.close();
		    	}
	    	}catch(SQLException sqex){
	    		//customLogFactory.error("Exception in closing DB connection objects ",sqex);
	    	}*/
	    }
	}
	
	public void writeToMysql(Connection conn){
		PreparedStatement stmt = null;
		ForumDataBean forumDataBean = null;
	    StringBuffer queryBuffer = new StringBuffer();
	    ResultSet rs = null;
		queryBuffer.append("select ans.MBFLIKECOUNT, que.MBFQUERFNUM, ans.MBFANSQUERFNUM, que.MBFQUEVIEWED, que.MBFQUEDESC, ans.MBFANSDESC, cnd.CNDDESC,  ");
		queryBuffer.append("lmt.LMTNAME from MBFANS ans, MBFQUE que, TPCND cnd, TPLMT lmt  where ans.MBFANSISACTIVE = 'Y' and que.MBFQUEISACTIVE = 'Y' and ");
		queryBuffer.append(" ans.MBFANSQUERFNUM = que.MBFQUERFNUM and que.MBFQUECNDCITY = cnd.CNDRFNUM and que.MBFQUELMTRFNUM = lmt.LMTRFNUM ");
	    try
	    {
	    	stmt = conn.prepareStatement(queryBuffer.toString());
	    	rs = stmt.executeQuery();
		      while (rs.next())
		      {
		    	  forumDataBean = new ForumDataBean();
		    	  forumDataBean.setLikeCnt(rs.getString("MBFLIKECOUNT"));
		    	  forumDataBean.setQueRfNum(rs.getString("MBFQUERFNUM"));
		    	  forumDataBean.setAnsQueRfNum(rs.getString("MBFANSQUERFNUM"));
		    	  forumDataBean.setViewedCnt(rs.getString("MBFQUEVIEWED"));
		    	  forumDataBean.setQueDesc(rs.getString("MBFQUEDESC"));
		    	  forumDataBean.setAnsDesc(rs.getString("MBFANSDESC"));
		    	  forumDataBean.setCity(rs.getString("CNDDESC"));
		    	  forumDataBean.setLocality(rs.getString("LMTNAME"));
		    	  forumDataBeanList.add(forumDataBean);
	    	  }
		    
	    }
	    catch (SQLException ex) {
	      System.err.println(ex.getMessage());
	      //customLogFactory.error("Exception while update in DB",ex);
	    }finally{
	    	try{
		    	if(stmt != null){
		    		stmt.close();
		    	}
		    	if(conn != null){
		    		conn.close();
		    	}
	    	}catch(SQLException sqex){
	    		//customLogFactory.error("Exception in closing DB connection objects ",sqex);
	    	}
	    }
	}
	
}
