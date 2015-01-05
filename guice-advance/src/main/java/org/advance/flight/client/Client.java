package org.advance.flight.client;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import org.advance.flight.engine.FlightEngine;
import org.advance.flight.supplier.*;
import org.advance.flight.utils.FlightUtils;

import static org.advance.flight.utils.FlightUtils.parseDate;

public class Client 
{
    public static void main( String[] args )
    {
    	/**
    	 * Guice Bootstrapping code
    	 * method local classes for demonstration
    	 * only.
    	 */
        class CSVSupplierModule extends AbstractModule{
    		@Override
    		public void configure() {
    			//bind(String.class).annotatedWith(Names.named("csvPath")).toInstance("./flightCSV/");
    			bind(String.class).toInstance("./guice-advance/src/main/resources/flightCSV");
    			Names.bindProperties(binder(), csvProperties());
    			try {
					bind(File.class).toConstructor(File.class.getConstructor(String.class));
				} catch (SecurityException | NoSuchMethodException e) {
					e.printStackTrace();
				}
            }
    		
    		public Properties csvProperties(){
    			Properties properties = new Properties();
    			FileReader fileReader;
				try {
					fileReader = new FileReader(new File(getClass().getResource("/application.properties").getPath()));
					try {
						properties.load(fileReader);
					}catch (IOException e) {
						e.printStackTrace();
					}finally{
						try {
							fileReader.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
    			return properties;
    		}
        }
        
        class FlightEngineModule extends AbstractModule{
        	@Override
    		public void configure() {
    			bind(FlightSupplier.class).annotatedWith(CSV.class).to(CSVSupplier.class);
    			bind(FlightSupplier.class).
    				annotatedWith(Names.named("xmlSupplier")).
    					toInstance(new XMLSupplier());
    			bindConstant().annotatedWith(Names.named("maxResults")).to(10);
    		}
        }
        
        class ClientModule extends AbstractModule{
    		@Override
    		protected void configure() {
    			bind(SearchRequest.class).toInstance(new SearchRequest());
    		}
        }
        
        class FlightUtilityModule extends AbstractModule{
        	@Override
        	protected void configure(){
        		bindConstant().annotatedWith(Names.named("dateFormat")).to("dd-MM-yy");
        		requestStaticInjection(FlightUtils.class);
        	}
        }
        
    	Injector injector = Guice.createInjector(new CSVSupplierModule(),
    												new FlightEngineModule(),
    												  new ClientModule(), 
    												    new FlightUtilityModule());
    	Client client = injector.getInstance(Client.class);
    	client.makeRequest();
    }
    
    public Client(){
    }
    
    @Inject
    private FlightEngine flightEngine;
    
    @Inject
    private SearchRequest searchRequest;
    
    public void makeRequest(){
		
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
