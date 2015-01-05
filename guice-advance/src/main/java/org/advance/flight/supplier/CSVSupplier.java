package org.advance.flight.supplier;


import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.Set;
import java.util.TreeSet;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import au.com.bytecode.opencsv.CSVReader;

import static org.advance.flight.utils.FlightUtils.parseDate;

public class CSVSupplier implements FlightSupplier{
	Set<SearchResponse> searchResponses = new TreeSet<>();

	private File csvFolder;
	
	public File getCsvFolder() {
		return csvFolder;
	}

	@Inject
	public void setCsvFolder(File csvFolder) {
		this.csvFolder = csvFolder;
	}

	@Inject
    @Named("csv.folder")
	private String csvPath;
	
	public String getCsvPath() {
		return csvPath;
	}

	public void setCsvPath(String csvPath) {
		this.csvPath = csvPath;
	}

	public CSVSupplier() {
	
	}

	public Set<SearchResponse> getResults() {
		if(searchResponses.isEmpty()){
			loadCSVFiles();
		}
		return searchResponses;
	}

	private void loadCSVFiles() {
		// Directory path here
		
		String fileName;

		File folder = getCsvFolder();
		
		File[] listOfFiles = folder.listFiles();
		
		for (int i = 0; i < listOfFiles.length; i++) {

			if (listOfFiles[i].isFile()) {
				File file = listOfFiles[i];
				if (file.getName().endsWith(".csv") || file.getName().endsWith(".CSV")) {
					CSVReader reader;

					try {
						reader = new CSVReader(new FileReader(file));
						String[] nextLine;
						int counter = 0;
						
						while ((nextLine = reader.readNext()) != null) {

							SearchResponse flightSearchRS = new SearchResponse();

							flightSearchRS.setFlightNumber(nextLine[0]);
							flightSearchRS.setDepartureLocation(nextLine[1]);
							flightSearchRS.setArrivalLocation(nextLine[2]);
							flightSearchRS.setValidDate(parseDate(nextLine[3]));
							flightSearchRS.setDepartTime(nextLine[4]);
							flightSearchRS.setFlightDuration(Double
									.parseDouble(nextLine[5]));
							flightSearchRS.setFare(Float
									.parseFloat(nextLine[6]));

							searchResponses.add(flightSearchRS);

							counter++;
						}
					} catch (IOException | ParseException e) {
						e.printStackTrace();
					}

                }
			}
		}
	}

}
