package org.flight.client;

import static org.flight.utils.FlightUtils.parseDate;

import java.text.ParseException;
import java.util.Date;
import java.util.List;


import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import org.flight.engine.FlightEngine;
import org.flight.supplier.CSVSupplier;
import org.flight.supplier.FlightSupplier;
import org.flight.supplier.SearchResponse;

public class Client 
{
    public static void main( String[] args )
    {
    	/**
    	 * Guice Bootstrapping code
    	 */
        class CSVSupplierModule extends AbstractModule{
    		@Override
    		public void configure() {
    			bind(String.class).annotatedWith(Names.named("csvPath")).toInstance("./guice-framework/src/main/resources/flightCSV");
    			bind(FlightSupplier.class).to(CSVSupplier.class);
    		}
        }
        
    	Injector injector = Guice.createInjector(new CSVSupplierModule());
    	Client client = injector.getInstance(Client.class);
    	
    	client.makeRequest();
    }

    //Constructor injection is a way to combine instantiation of objects with dependency injection.
    @Inject
    public Client(FlightEngine flightEngine){
        this.flightEngine = flightEngine;
    }

    private FlightEngine flightEngine;
    
    public void makeRequest(){
    	
    	SearchRequest searchRequest = new SearchRequest();
		
		searchRequest.setArrival_location("LHR");
		searchRequest.setDeparture_location("FRA");
		
		Date flightDate = null;
		
		try {
			flightDate = parseDate("20-11-2010");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		searchRequest.setFlightDate(flightDate);		
		
		List<SearchResponse> responseList = flightEngine.processRequest(searchRequest);
		
		for(SearchResponse flightSearchRS : responseList){
			System.out.println(flightSearchRS.getArrivalLocation() + " - "+flightSearchRS.getDepartureLocation());
		}
    	
    }
    

}
