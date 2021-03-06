package GUI;

import components.*;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import javax.annotation.processing.FilerException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

public class Controller implements Initializable{

    public Label bottomLabel;
    public Label dataSize;
    public Label numOfAP;
    public Label latLabel1;
    public Label lonLabel1;
    public Label altLabel1;
    public Label latLabel2;
    public Label lonLabel2;
    public Label altLabel2;
    public TextField macInput;
    public TextField macAlgo2input1;
    public TextField macAlgo2input2;
    public TextField macAlgo2input3;
    public TextField sig1input;
    public TextField sig2input;
    public TextField sig3input;

    public File selectedFolder;
    public File comboFile;
    public File selectedFile;
    public List<File> selectedFiles;
    public List<File> selectedComboFles;
    List<WifiPointsTimePlace> processedFile;
    public HashRouters<String,WIFISample> routersOfAllFiles;

    private boolean addFileOrFolder = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        selectedFile=null;
        selectedFolder=null;
        selectedFiles = new ArrayList<>();
        selectedComboFles = new ArrayList<>();

        routersOfAllFiles = new HashRouters<>();
        processedFile = new ArrayList<>();
    }

    public void folderChooser(){
        System.out.println("clicked addDir button");
        bottomLabel.setStyle("-fx-text-fill: black;");
        bottomLabel.setText("choose folder");
        DirectoryChooser dirChooser = new DirectoryChooser ();
        dirChooser.setInitialDirectory(new File("FileResources"));
        selectedFolder = dirChooser.showDialog(null);
        if (selectedFolder != null){
            addFileOrFolder = true;
            System.out.println(selectedFolder.getAbsolutePath());
            bottomLabel.setStyle("-fx-text-fill: black;");
            bottomLabel.setText("choose folder: " +selectedFolder.getAbsolutePath());

            File dir = new File(selectedFolder.toString());
            for (File file : dir.listFiles()) {
                //Incorrect file type-reject
                if (!(file.getName().toLowerCase().endsWith(".csv"))) {
                    System.out.println(file.getName() + " is an incorrect file type in the folder");
                    System.out.println("the file was not added to the csv file error 404");
                    continue;
                }else{ //add absolute path
                    selectedFiles.add(file.getAbsoluteFile());
                }
            }

            // Print the names of files
            for(File file : selectedFiles){
                System.out.println(file.getAbsolutePath());
            }

        }else{
            System.out.println("didn't choose folder");
            bottomLabel.setStyle("-fx-text-fill: red;");
            bottomLabel.setText("didn't choose folder");
        }
    }

    public void fileChooser(){
        System.out.println("clicked addCSV button");
        bottomLabel.setStyle("-fx-text-fill: black;");
        bottomLabel.setText("choose file");
        FileChooser fileChooser = new FileChooser ();
        fileChooser.setInitialDirectory(new File("noGPSFolder"));
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("CSV files","*.csv") );
        selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile==null){ //null
            System.out.println("didn't choose file");
            bottomLabel.setStyle("-fx-text-fill: red;");
            bottomLabel.setText("didn't choose file");
        }else if (selectedFile != null && selectedFile.getName().toLowerCase().endsWith(".csv") ){
            addFileOrFolder = true;
            System.out.println(selectedFile.getAbsolutePath());
            bottomLabel.setStyle("-fx-text-fill: black;");
            bottomLabel.setText("choose file: " +selectedFile.getAbsolutePath());
            selectedComboFles.add(selectedFile.getAbsoluteFile());
        }else if(!selectedFile.getName().toLowerCase().endsWith(".csv")) {
            selectedFile = null;
            System.out.println("incorrect file type");
            bottomLabel.setStyle("-fx-text-fill: red;");
            bottomLabel.setText("incorrect file type");
        }
    }

    //TODO fill in
    public void clearData(){
        addFileOrFolder = false;
        selectedFile=null;
        selectedFolder=null;
        selectedFiles.clear();
        bottomLabel.setStyle("-fx-text-fill: black;");
        bottomLabel.setText("cleared data");
        System.out.println("cleared data");

        routersOfAllFiles = new HashRouters<>();
        processedFile = new ArrayList<>();

        dataSize.setText(processedFile.size() + "");
    }

    public void save2CSV(){
        bottomLabel.setText("please wait...");

        OutputCSVWriter outputCSVWriter = null;

        if(selectedComboFles==null && selectedFolder==null ){
            bottomLabel.setStyle("-fx-text-fill: red;");
            bottomLabel.setText("sorry cannot export without files");
            return;
        }

        if(!addFileOrFolder)
        {
            bottomLabel.setStyle("-fx-text-fill: red;");
            bottomLabel.setText("sorry cannot export new file without new files or clear");
            return;
        }

        if(selectedFolder != null)
        {
            outputCSVWriter = new OutputCSVWriter(selectedFiles);
            processedFile.addAll(outputCSVWriter.sortAndMergeFiles());
            routersOfAllFiles.mergeToHash(outputCSVWriter.getAllRoutersOfTheFiles());
            selectedFiles.clear();
        }

//        OutputCSVWriter outputCSVWriter = new OutputCSVWriter(selectedFiles);
//
//        processedFile =  outputCSVWriter.sortAndMergeFiles();
//        processedFile.addAll(outputCSVWriter.sortAndMergeFiles());

        if(selectedComboFles != null)
        {
            for(File file : selectedComboFles)
            {
                try {
                    processedFile.addAll(CoboCSVReader.readCsvFile(file.getAbsolutePath(),routersOfAllFiles));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            selectedComboFles.clear();
        }


        dataSize.setText(processedFile.size() + "");

        outputCSVWriter.ExportToCSV(processedFile,"testOutputCSV.csv");

//        routersOfAllFiles = outputCSVWriter.getAllRoutersOfTheFiles();

//        routersOfAllFiles.mergeToHash(outputCSVWriter.getAllRoutersOfTheFiles());

        bottomLabel.setStyle("-fx-text-fill: black;");
        bottomLabel.setText("successfully exported CSV file");

        addFileOrFolder = false;
    }

    public void submitMac(){
        if(processedFile==null){
            bottomLabel.setStyle("-fx-text-fill: red;");
            bottomLabel.setText("no files selected for algorithm 1");
            return;
        }
        if (selectedFile==null && selectedFolder==null){
            bottomLabel.setStyle("-fx-text-fill: red;");
            bottomLabel.setText("no files selected for algorithm");
            return;
        }
        String userMacInput = macInput.getText();

        WeightedArithmeticMean weightedArithmeticMean = new WeightedArithmeticMean(routersOfAllFiles);
        WIFIWeight ww =weightedArithmeticMean.getWAMbyMac(userMacInput);

        if(ww==null){
            System.out.println("incorrect input");
            bottomLabel.setText("incorrect input");
        }else{
            System.out.println("calculated coordinates for given mac are:");
            bottomLabel.setText("calculated coordinates for given mac");
        }

        if(ww!=null) {
            System.out.println(ww.getWIFI_Lat());
            System.out.println(ww.getWIFI_Lon());
            System.out.println(ww.getWIFI_Alt());
            latLabel1.setText(String.valueOf(ww.getWIFI_Lat()).substring(0,7));
            lonLabel1.setText(String.valueOf(ww.getWIFI_Lon()).substring(0,7));
            altLabel1.setText(String.valueOf(ww.getWIFI_Alt()).substring(0,7));
        }else{
            latLabel1.setText(String.valueOf(0.0));
            lonLabel1.setText(String.valueOf(0.0));
            altLabel1.setText(String.valueOf(0.0));
        }

    }

    public void submitFilter(){

//        Filter... filter
//        for (Filter fil : filter){
//            if (!fil.checkLineByFilter(lineInformation)) {
//                isAllConditionsTrue=false;
//            }
//        }
    }
    public void submitAlgo2(){
        if(processedFile==null){
            bottomLabel.setStyle("-fx-text-fill: red;");
            bottomLabel.setText("cannot filter no file");
            return;
        }
        ArrayList<WIFIWeight> userInput = new ArrayList<>();
        userInput.add(new WIFIWeight(macAlgo2input1.getText(),0,0,0,Integer.parseInt(sig1input.getText()),0));
        userInput.add(new WIFIWeight(macAlgo2input2.getText(),0,0,0,Integer.parseInt(sig2input.getText()),0));
        userInput.add(new WIFIWeight(macAlgo2input3.getText(),0,0,0,Integer.parseInt(sig3input.getText()),0));
//          WIFIWeight a = new WIFIWeight("3c:1e:04:03:7f:17",0,0,0,-30,0);
//          WIFIWeight b = new WIFIWeight("74:da:38:50:77:f2",0,0,0,-49,0);
//          WIFIWeight d = new WIFIWeight("c4:3d:c7:5a:79:1c",0,0,0,-90,0);
//
//          userInput.add(a);
//          userInput.add(b);
//          userInput.add(d);

        List<WIFIWeight> kLineMostSimilar = Algorithm2.getKMostSimilar(processedFile, userInput, 3);

        WeightedArithmeticMean weightedArithmeticMean = new WeightedArithmeticMean(routersOfAllFiles);

        WIFIWeight ww = weightedArithmeticMean.getWamByList(kLineMostSimilar);

        if (ww!=null){
            System.out.println(ww.getWIFI_Lat());
            System.out.println(ww.getWIFI_Lon());
            System.out.println(ww.getWIFI_Alt());
            latLabel2.setText(String.valueOf(ww.getWIFI_Lat()).substring(0,7));
            lonLabel2.setText(String.valueOf(ww.getWIFI_Lon()).substring(0,7));
            altLabel2.setText(String.valueOf(ww.getWIFI_Alt()).substring(0,7));

        }
    }

    public void exportMACAlgo2CSV(){

        Set<String> keysAddHash = routersOfAllFiles.getRoutersHashTable().keySet();
        List<WIFIWeight> MACs_after_algorith_1 = new ArrayList<>();

        for(String key : keysAddHash )
        {
            WeightedArithmeticMean weightedArithmeticMean = new WeightedArithmeticMean(routersOfAllFiles);
            MACs_after_algorith_1.add(weightedArithmeticMean.getWAMbyMac(key));
        }
        OutputCSVWriter.ExportToCSV(MACs_after_algorith_1,"macOutputCSV.csv");
        System.out.println("successful mac export");
        bottomLabel.setStyle("-fx-text-fill: #131dff;");
        bottomLabel.setText("successful mac export");
    }

    public void addComboCSV(){
        System.out.println("clicked combo button");
        bottomLabel.setStyle("-fx-text-fill: black;");
        bottomLabel.setText("choose file");
        FileChooser fileChooser = new FileChooser ();
        fileChooser.setInitialDirectory(new File("noGPSFolder"));
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("CSV files","*.csv") );
        comboFile = fileChooser.showOpenDialog(null);

        if (comboFile==null){ //null
            System.out.println("didn't choose file");
            bottomLabel.setStyle("-fx-text-fill: red;");
            bottomLabel.setText("didn't choose file");
        }else if (comboFile != null && comboFile.getName().toLowerCase().endsWith(".csv") ){
            System.out.println(comboFile.getAbsolutePath());
            bottomLabel.setStyle("-fx-text-fill: black;");
            bottomLabel.setText("choose file: " +comboFile.getAbsolutePath());

        }else if(!comboFile.getName().toLowerCase().endsWith(".csv")) {
            comboFile = null;
            System.out.println("incorrect file type");
            bottomLabel.setStyle("-fx-text-fill: red;");
            bottomLabel.setText("incorrect file type");
        }

    }

    public void comboFiller() throws IOException {
        //Algorithm 2
        if (comboFile==null)
            throw new IOException();

        ArrayList<WIFIWeight> listOfWIFIWeightsUsingAlgo2 = new ArrayList<>();//Hold locations of all lines of the combination without location CSV File

        //Read the combination-without-location-CSV-File and inserts all line to the ArrayList<ArrayList<WIFIWeight>>.
        //the innter ArrayList<WIFIWeight> hold one line of combination-without-location-CSV-File. and the external ArrayList hold all of lines.
        ArrayList<ArrayList<WIFIWeight>> listOfCombinationCsvLines = CSVReader.readCombinationCsvFile(comboFile.getAbsolutePath());
        for(ArrayList<WIFIWeight> line : listOfCombinationCsvLines) {
            //run algorithm 2 on each line, get the WIFIWeight of each line and insert to ArrayList.
            List<WIFIWeight> kLineMostSimilar = Algorithm2.getKMostSimilar(processedFile, line, 3);
            WeightedArithmeticMean weightedArithmeticMean = new WeightedArithmeticMean(routersOfAllFiles);
            WIFIWeight ww = weightedArithmeticMean.getWamByList(kLineMostSimilar);

            listOfWIFIWeightsUsingAlgo2.add(ww);
        }

        //Getting all lines of combination-without-location-CSV-File and insert the new locations and export to new file
        try {
            List<WifiPointsTimePlace> s =  CoboCSVReader.readCsvFile(comboFile.getAbsolutePath(), routersOfAllFiles);
            OutputCSVWriter.changeLocationOfFile(listOfWIFIWeightsUsingAlgo2,s, "afterAlgo2.csv");
        }
        catch (IOException e)
        {

        }

        bottomLabel.setText("combo filler");
        System.out.println("combo filler successful");
    }
}

