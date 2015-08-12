package com.mbbatch.itemreader;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

public class CommentsMailerReader implements ItemReader{

	private static String value= "success";
	@Override
	public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		System.out.println("@@@@@@@@@@@@@@22");
		return value;				
	}	
}
