package com.currency.resources;

import java.text.ParseException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import com.currency.core.CurrencyData;
import com.currency.core.CurrencyException;
import com.currency.service.CurrencyProcessorService;
import com.currency.util.DateUtil;

import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.errors.ErrorMessage;

@Path("/currency/")
@Produces(MediaType.APPLICATION_JSON)
public class CurrencyResource implements ExceptionMapper<CurrencyException> {

    private CurrencyProcessorService service;

    public CurrencyResource(CurrencyProcessorService service) {
        this.service = service;
    }

    /**
     * Get current currency from provider or from cache
     * 
     * @return CurrencyData or description of the failure.
     * @throws CurrencyException
     * @throws InterruptedException 
     */
    @GET
    @Path("/get_current")
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    public CurrencyData getCurrency() throws CurrencyException, InterruptedException {
        return service.getCurrentCurrency();

    }

    /**
     * Get currency currency history from database.
     * 
     * @param beginDate
     *            results >= dates in yyyy-MM-dd'T'HH:mm:ss.SSS format
     * @param endDate
     *            results <= dates in yyyy-MM-dd'T'HH:mm:ss.SSS format
     * @param queryByRequest
     *            look based on request date or currency date. @See
     *            service.getCurrency for more information
     * @return CurrencyData list or description of the failure.
     * @throws CurrencyException
     * @throws ParseException 
     */
    @GET
    @Path("/get_by_interval")
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    public List<CurrencyData> getCurrency(@QueryParam("beginDate") String beginDate,
            @QueryParam("endDate") String endDate, @QueryParam("queryByRequestDate") boolean queryByRequest)
                    throws CurrencyException, ParseException {

        return service.getCurrency(DateUtil.dateConverter(beginDate), DateUtil.dateConverter(endDate), queryByRequest);

    }

    @Override
    public Response toResponse(CurrencyException ex) {
        return Response.status(500).entity(new ErrorMessage(ex.getMessage())).type(MediaType.APPLICATION_JSON)
                .build();
    }
}
