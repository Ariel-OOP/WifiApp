package components;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class MainConsole {

    static List<WifiPointsTimePlace> processedFile = new ArrayList<>();
    static HashRouters<String,WIFISample> routersOfAllFiles = new HashRouters<>();

    public static void main(String[] args) {
        List<File> selectedFiles = new ArrayList<>();

//        String folderPath="FileResources";
        //copy path of files in the folder to the list
        String folderPath="E:\\OOP_GitHub\\Assignment OOP\\WifiApp\\FileResources";
        File Dir = new File(new File(folderPath).toString());
        for(File oneFile : Dir.listFiles()){
            selectedFiles.add(oneFile);
        }

        //selectedFiles.add(new File(folderPath));

        //Read the files and create combination CSV file
        OutputCSVWriter outputCSVWriter = new OutputCSVWriter(selectedFiles);

        //Add the new combination lines into the exist lines
        processedFile.addAll(outputCSVWriter.sortAndMergeFiles());
        routersOfAllFiles.mergeToHash(outputCSVWriter.getAllRoutersOfTheFiles());

        OutputCSVWriter.ExportToCSV(processedFile,"testOutputCSV.csv");

        //Algorithm 1
//        ArrayList<WIFIWeight> userInput = new ArrayList<WIFIWeight>();
//
//        WIFIWeight a = new WIFIWeight("3c:1e:04:03:7f:17",0,0,0,-30,0);
//        WIFIWeight b = new WIFIWeight("74:da:38:50:77:f2",0,0,0,-49,0);
//        WIFIWeight d = new WIFIWeight("c4:3d:c7:5a:79:1c",0,0,0,-90,0);
//
//        userInput.add(a);
//        userInput.add(b);
//        userInput.add(d);
//
//        List<WIFIWeight> kLineMostSimilar = Algorithm2.getKMostSimilar(processedFile, userInput, 3);
//
//        routersOfAllFiles.mergeToHash(outputCSVWriter.getAllRoutersOfTheFiles());
//
//        Set<String> keysAddHash = routersOfAllFiles.getRoutersHashTable().keySet();
//        List<WIFIWeight> MACs_after_algorith_1 = new ArrayList<>();
//
//        for(String key : keysAddHash )
//        {
//            WeightedArithmeticMean weightedArithmeticMean = new WeightedArithmeticMean(routersOfAllFiles);
//        MACs_after_algorith_1.add(weightedArithmeticMean.getWAMbyMac(key));
//        }

        //Algorithm 2


        ArrayList<WIFIWeight> listOfWIFIWeightsUsingAlgo2 = new ArrayList<>();//Hold locations of all lines of the combination without location CSV File

        //Read the combination-without-location-CSV-File and inserts all line to the ArrayList<ArrayList<WIFIWeight>>.
        //the innter ArrayList<WIFIWeight> hold one line of combination-without-location-CSV-File. and the external ArrayList hold all of lines.
        ArrayList<ArrayList<WIFIWeight>> listOfCombinationCsvLines = CSVReader.readCombinationCsvFile("E:\\OOP_GitHub\\Assignment OOP\\WifiApp\\noGPSFolder\\_comb_no_gps_ts2_.csv");
        for(ArrayList<WIFIWeight> line : listOfCombinationCsvLines) {
            //run algorithm 2 on each line, get the WIFIWeight of each line and insert to ArrayList.
            List<WIFIWeight> kLineMostSimilar = Algorithm2.getKMostSimilar(processedFile, line, 3);
            WeightedArithmeticMean weightedArithmeticMean = new WeightedArithmeticMean(routersOfAllFiles);
            WIFIWeight ww = weightedArithmeticMean.getWamByList(kLineMostSimilar);

            listOfWIFIWeightsUsingAlgo2.add(ww);
        }

        //Getting all lines of combination-without-location-CSV-File and insert the new locations and export to new file
        try {
            List<WifiPointsTimePlace> s =  CoboCSVReader.readCsvFile("E:\\OOP_GitHub\\Assignment OOP\\WifiApp\\noGPSFolder\\_comb_no_gps_ts2_.csv", routersOfAllFiles);
            OutputCSVWriter.changeLocationOfFile(listOfWIFIWeightsUsingAlgo2,s, "afterAlgo2.csv");
        }
        catch (IOException e)
        {

        }


        OutputCSVWriter outputCSVWriterBenMosheFiles = new OutputCSVWriter(selectedFiles);
        processedFile.addAll(outputCSVWriterBenMosheFiles.sortAndMergeFiles());


       // WeightedArithmeticMean weightedArithmeticMean = new WeightedArithmeticMean(routersOfAllFiles);
       // WIFIWeight ww =weightedArithmeticMean.getWAM("00:22:b0:75:7d:eb");

        Scanner stdin = new Scanner(System.in);
        char c = printMenu();
        int inputChoice = Integer.parseInt(c+"");
        Filter filter = new Filter(inputChoice);
        do{
            LineFilters.printInput(inputChoice);
        }while (!filter.setFilter(stdin.nextLine()));

        KmlExporter kmlExporter = new KmlExporter("testOutputCSV.csv","E:\\OOP_GitHub\\Assignment OOP\\WifiApp\\resources\\helloKML1.kml");
        if(kmlExporter.csvToKml(filter) )
            System.out.println("successful export");
        else
            System.out.println("failure to export");
    }

    private static char printMenu() {
        char c;
        Scanner stdin = new Scanner(System.in);
        System.out.println("=========================================\nThis is a filter of points.\n"
                + "* If you don't want any filter enter 0\n"
                + "* If you want to filter by coordinates enter 1\n"
                + "* If you want to filter by time enter 2\n"
                + "* If you want to filter by ID enter 3\n"
                + "===============================================\nEnter a number: ");

        do {
            c = stdin.nextLine().charAt(0);
            if (c>51 || c<48) System.out.println("Not valid choice, please choose again\n");
        }while(c>58 || c<48);
        return c;
    }
}
