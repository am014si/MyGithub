package com.mbbatch.parameter;

import java.util.Random;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersIncrementer;
 
public class DynamicJobParameters implements JobParametersIncrementer 
{  
    public JobParameters getNext(JobParameters parameters) 
    {      
               
    	if (parameters==null || parameters.isEmpty()) 
        {
            return new JobParametersBuilder().addLong("run.id", 1L).toJobParameters();
         }
             
        long id = parameters.getLong("run.id") + 1; 
    	parameters = new JobParametersBuilder().addLong("run.id", id).toJobParameters();
        return parameters;        
    }
}
