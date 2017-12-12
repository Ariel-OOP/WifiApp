package components;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Nissan on 11/21/2017.
 */
public class MainConsole {


    public static void main(String[] args) {
        List<File> selectedFiles = new ArrayList<>();

        String folderPath="E:\\OOP_GitHub\\Assignment OOP\\WifiApp\\FileResources";
        File Dir = new File(new File(folderPath).toString());
        for(File oneFile : Dir.listFiles()){
            selectedFiles.add(oneFile);
        }

        //selectedFiles.add(new File(folderPath));

        OutputCSVWriter outputCSVWriter = new OutputCSVWriter(selectedFiles,"testOutputCSV.csv");
        List<WifiPointsTimePlace> processedFile = outputCSVWriter.sortAndMergeFiles();
        outputCSVWriter.ExportToCSV(processedFile);

        ArrayList<WIFIWeight> userInput = new ArrayList<WIFIWeight>();

        WIFIWeight a = new WIFIWeight("3c:1e:04:03:7f:17",0,0,0,-30,0);
        WIFIWeight b = new WIFIWeight("74:da:38:50:77:f2",0,0,0,-49,0);
        WIFIWeight d = new WIFIWeight("c4:3d:c7:5a:79:1c",0,0,0,-90,0);

        userInput.add(a);
        userInput.add(b);
        userInput.add(d);

        List<WIFIWeight> kLineMostSimilar = Algorithm2.getKMostSimilar(processedFile, userInput, 3);

        HashRouters<String,WIFISample> routersOfAllFiles = outputCSVWriter.getAllRoutersOfTheFiles();


       // WeightedArithmeticMean weightedArithmeticMean = new WeightedArithmeticMean(routersOfAllFiles);
       // WIFIWeight ww =weightedArithmeticMean.getWAM("00:22:b0:75:7d:eb");


        Scanner stdin = new Scanner(System.in);
        char c = printMenu();
        int inputChoice = Integer.parseInt(c+"");
        Filter filter = new Filter(inputChoice);
        do{
            LineFilters.printInput(inputChoice);
        }while (!filter.setFilter(stdin.nextLine()));

        KmlExporter kmlExporter = new KmlExporter("testOutputCSV.csv","resources\\helloKML1.kml");
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
