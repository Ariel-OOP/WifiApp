package components;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OutputCSVWriter {

	//Delimiter used in CSV file
	private static final String NEW_LINE_SEPARATOR = "\n";

	//CSV file header
	private static final Object [] FILE_HEADER = {"Time","ID","Lat","Lon","Alt","#WiFi networks","SSID1","MAC1","Frequncy1","Signal1",
			"SSID2","MAC2","Frequncy2","Signal2","SSID3","MAC3","Frequncy3","Signal3","SSID4","MAC4","Frequncy4","Signal4",
			"SSID5","MAC5","Frequncy5","Signal5","SSID6","MAC6","Frequncy6","Signal6","SSID7","MAC7","Frequncy7","Signal7",
			"SSID8","MAC8","Frequncy8","Signal8","SSID9","MAC9","Frequncy9","Signal9","SSID10","MAC10","Frequncy10","Signal10"};

	List<File> files;
	File dir;

	//String outputPath;

	HashRouters<String,WIFISample> allRoutersOfTheFiles;

	/**
	 * 
	 * @param files the file destination to output the file and name
	 */

	public OutputCSVWriter(List<File> files) {
		this.files = files;
		//this.outputPath = outputPath;


		allRoutersOfTheFiles = new HashRouters<>();

//		//Deletes file if it exists
//		File fileToDelete = new File(outputPath+".csv");
//		fileToDelete.delete();

	}
	public List<WifiPointsTimePlace> sortAndMergeFiles() {
		List<WifiPointsTimePlace> allSortedPoints = new ArrayList<>(); //from all the files together
		List<WifiPointsTimePlace> processedFile = new ArrayList<>();
		WigleFileReader wigleFileReader;

		for (File file : files) {

				wigleFileReader = new WigleFileReader(file.getPath());
				wigleFileReader.readCsvFile();

				allRoutersOfTheFiles.mergeToHash(wigleFileReader.getHashRouters());

				//allSortedPoints = wigleFileReader.getWigleList();
				processedFile.addAll(wigleFileReader.getWigleList());
		}
		return processedFile;
	}

	public static void ExportToCSV(List<WifiPointsTimePlace> fileAfterSortintAndMerging,String outputPath) {

		//Deletes file if it exists
		File fileToDelete = new File(outputPath+".csv");
		fileToDelete.delete();

		FileWriter fileWriter = null;

		CSVPrinter csvFilePrinter = null;

		//Create the CSVFormat object with "\n" as a record delimiter
		CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator(NEW_LINE_SEPARATOR);

		try {

			//initialize FileWriter object
			fileWriter = new FileWriter(outputPath);

			//initialize CSVPrinter object 
			csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);

			//Create CSV file header
			csvFilePrinter.printRecord(FILE_HEADER);

				for(WifiPointsTimePlace line : fileAfterSortintAndMerging)
					csvFilePrinter.printRecord(line.getWifiPoints());

			System.out.println("CSV file was created successfully !!!");

		} catch (Exception e) {
			System.out.println("Error in CsvFileWriter !!!");
			e.printStackTrace();
		} finally {
			try {
				fileWriter.flush();
				fileWriter.close();
				csvFilePrinter.close();
			} catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter/csvPrinter !!!");
				e.printStackTrace();
			}
		}
	}

	public HashRouters<String, WIFISample> getAllRoutersOfTheFiles() {
		return allRoutersOfTheFiles;
	}
}
