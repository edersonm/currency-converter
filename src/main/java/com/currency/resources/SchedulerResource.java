package com.currency.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.currency.util.UpdaterUtil;

import io.dropwizard.hibernate.UnitOfWork;

@Path("/scheduler/")
@Produces(MediaType.APPLICATION_JSON)
public class SchedulerResource {
	/**
	 * Change the time, in minutes, on which the server is suppose to make request to the provider.
	 * 
	 * @param minutes minutes interval.
	 * @return OK if the procedure was executed correctly, ERROR otherwise.
	 */
	@GET
	@Path("/change_update_time")
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    public String changeUpdateTime(@QueryParam("intervalInMinutes") int minutes) {
        boolean result = UpdaterUtil.updateTimer(minutes);
		if(result)
		   return "OK";
		else
		   return "ERROR";
    }
	
	/**
	 * Change the maximum interval of cache look-up to current time - seconds.ii
	 * 
	 * @param seconds value in seconds.
	 * @return  OK if the procedure was executed correctly, ERROR otherwise.
	 */
	@GET
	@Path("/change_db_cache_time")
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    public String changeDbCacheTime(@QueryParam("intervalInSeconds") int seconds) {
        boolean result = UpdaterUtil.updateRequestTimer(seconds);
		if(result)
		   return "OK";
		else
		   return "ERROR";
    }
}
