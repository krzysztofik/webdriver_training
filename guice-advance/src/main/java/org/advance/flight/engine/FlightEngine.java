package org.advance.flight.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.advance.flight.client.SearchRequest;
import org.advance.flight.exceptions.NoCriteriaMatchException;
import org.advance.flight.exceptions.NoFlightAvailableException;
import org.advance.flight.supplier.CSV;
import org.advance.flight.supplier.FlightSupplier;
import org.advance.flight.supplier.SearchResponse;
import org.advance.flight.utils.OutputPreference;

public class FlightEngine {
	private int maxResults;
	
	private FlightSupplier csvFlightSupplier;
	
	private FlightSupplier xmlFlightSupplier;
	
	public int getMaxResults() {
		return maxResults;
	}

	@Inject
	public void setMaxResults(@Named("maxResults")int maxResults) {
		this.maxResults = maxResults;
	}

	public FlightSupplier getXmlFlightSupplier() {
		return xmlFlightSupplier;
	}

	@Inject
	public void setXmlFlightSupplier(@Named("xmlSupplier")FlightSupplier xmlFlightSupplier) {
		this.xmlFlightSupplier = xmlFlightSupplier;
	}

	@Inject
	public FlightEngine(@CSV FlightSupplier flightSupplier) {
		this.csvFlightSupplier = flightSupplier;
	}
	
	public FlightSupplier getFlightSupplier() {
		return csvFlightSupplier;
	}

	public void setFlightSupplier(FlightSupplier flightSupplier) {
		this.csvFlightSupplier = flightSupplier;
	}

	public List<SearchResponse> processRequest(SearchRequest flightSearchRQ) {
		List<SearchResponse> responseList = new ArrayList<>();

		boolean criteriaMatch = false;

		for(SearchResponse flightSearchRS : csvFlightSupplier.getResults()){
			if(flightSearchRS.getArrivalLocation().equals(
					flightSearchRQ.getArrival_location())
					||
				flightSearchRS.getDepartureLocation().equals(flightSearchRQ.getDeparture_location()))
				criteriaMatch = true;
			
			if (flightSearchRS.getArrivalLocation().equals(
					flightSearchRQ.getArrival_location())
					&&
				flightSearchRS.getDepartureLocation().equals(flightSearchRQ.getDeparture_location())
					&&
				(flightSearchRS.getValidDate().compareTo(flightSearchRQ.getFlightDate()) == 0)
			) {
				responseList.add(flightSearchRS);
			}
		}
		
		if(!criteriaMatch)
			throw new NoCriteriaMatchException("Depart/Arrival Codes don't match our records");
		if(responseList.size() == 0)
			throw new NoFlightAvailableException("No flights found for given specified date");
		
		if(flightSearchRQ.getPreferences().contains(OutputPreference.DURATION)){
			Collections.sort(responseList, (o1, o2) -> {
                int result = 0;

                if(o1.getFlightDuration() > o2.getFlightDuration())
                    result = 1;
                else if(o1.getFlightDuration() < o2.getFlightDuration())
                    result = -1;

                return result;
            });
		}
		
		return responseList;
	}	
}
