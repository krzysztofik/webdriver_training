package org.flight.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.flight.client.SearchRequest;
import org.flight.exceptions.NoCriteriaMatchException;
import org.flight.exceptions.NoFlightAvailableException;
import org.flight.supplier.FlightSupplier;
import org.flight.supplier.SearchResponse;
import org.flight.utils.OutputPreference;

import com.google.inject.Inject;

public class FlightEngine {

	private FlightSupplier flightSupplier;
	
	public FlightSupplier getFlightSupplier() {
		return flightSupplier;
	}

    //In case we need to have our fields properly testable, we usually declare them as private, along with getter and setter APIs.
    //We could inject the dependency by applying the @Inject annotation over the setter API
    @Inject
	public void setFlightSupplier(FlightSupplier flightSupplier) {
		this.flightSupplier = flightSupplier;
	}

	public List<SearchResponse> processRequest(SearchRequest flightSearchRQ) {
		List<SearchResponse> responseList = new ArrayList<>();

		boolean criteriaMatch = false;

		for(SearchResponse flightSearchRS : flightSupplier.getResults()){
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
                // TODO Auto-generated method stub
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
