package components;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {

    final static int theColumnOfNumWIFI = 5;
    final static int NumberOfGlobalPropertiesColumn = 6;
    final static int NumberOfPointPropertiesColumn = 4;

    public static void main(String[] args) {

//        ArrayList<WIFIWeight> listOfCombinationCsvLines = CSVReader.readCombinationCsvFile("E:\\OOP_GitHub\\Assignment OOP\\WifiApp\\noGPSFolder\\_comb_no_gps_ts2_.csv");
//
//        List<WIFIWeight> kLineMostSimilar = Algorithm2.getKMostSimilar(processedFile, listOfCombinationCsvLines, listOfCombinationCsvLines.size());
//
//        WeightedArithmeticMean weightedArithmeticMean = new WeightedArithmeticMean(routersOfAllFiles);
//
//        WIFIWeight ww = weightedArithmeticMean.getWamByList(kLineMostSimilar);
    }

    /**
     * The function gets combination CSV file and read all line.
     * @return  ArrayList type of WifiPointsTimePlace for all lines of the file.
     */
    public static ArrayList<ArrayList<WIFIWeight>> readCombinationCsvFile(String filePath) {
        BufferedReader br = null;
        String line; //line of the file
        ArrayList<WIFIWeight> userInput_SingleLine = null;//ArrayList with all lines.
        ArrayList<ArrayList<WIFIWeight>> allLinesOfTheCombinationCsvFile = new ArrayList<>(); // every inner ArrayList holds the line in WIFIWeight format and every MAC is converted to WIFIWeight, and the Outer Arraylist holds every line

        try {

            br = new BufferedReader(new FileReader(filePath));

            while ((line = br.readLine()) != null) {
                // use comma as separator
                String[] arrayLine = line.split(",");//Split each line to parts of properties
                userInput_SingleLine = new ArrayList<>();

                int numberOfMACsInLine = Integer.valueOf(arrayLine[theColumnOfNumWIFI]);

                for(int i = 0; i < numberOfMACsInLine; i++)
                {
                    String mac = arrayLine[NumberOfGlobalPropertiesColumn + 1 + i * NumberOfPointPropertiesColumn];//add 1 for get the mac that located after SSID
                    double lat = 0;
                    double lon = 0;
                    double alt = 0;
                    int rssi = Integer.valueOf(arrayLine[NumberOfGlobalPropertiesColumn + 3 + i * NumberOfPointPropertiesColumn]);//add 3 for get the mac that located after RSSI
                    double weight = 0;

                    WIFIWeight ww = new WIFIWeight(mac,lat,lon,alt,rssi,weight);
                    userInput_SingleLine.add(ww);
                }
                allLinesOfTheCombinationCsvFile.add(userInput_SingleLine);

            }
        } catch (FileNotFoundException e) {
            System.out.println("This file is not exist.");
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
          catch (IOException e) {
            e.printStackTrace();
        }

        return allLinesOfTheCombinationCsvFile;

    }
}
