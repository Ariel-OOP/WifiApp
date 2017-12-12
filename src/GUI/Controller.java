package GUI;

import components.*;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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
    public File selectedFile;
    public List<File> selectedFiles;
    List<WifiPointsTimePlace> processedFile;
    public HashRouters<String,WIFISample> routersOfAllFiles;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        selectedFile=null;
        selectedFolder=null;
        selectedFiles = new ArrayList<>();

    }

    public void folderChooser(){
        System.out.println("clicked addDir button");
        bottomLabel.setStyle("-fx-text-fill: black;");
        bottomLabel.setText("choose folder");
        DirectoryChooser dirChooser = new DirectoryChooser ();
        dirChooser.setInitialDirectory(new File("FileResources"));
        selectedFolder = dirChooser.showDialog(null);
        if (selectedFolder != null){
            System.out.println(selectedFolder.getAbsolutePath());
            bottomLabel.setStyle("-fx-text-fill: black;");
            bottomLabel.setText("choose folder: " +selectedFolder.getAbsolutePath());
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
        fileChooser.setInitialDirectory(new File("C:\\"));
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("CSV files","*.csv") );
        selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile==null){ //null
            System.out.println("didn't choose file");
            bottomLabel.setStyle("-fx-text-fill: red;");
            bottomLabel.setText("didn't choose file");
        }else if (selectedFile != null && selectedFile.getName().toLowerCase().endsWith(".csv") ){
            System.out.println(selectedFile.getAbsolutePath());
            bottomLabel.setStyle("-fx-text-fill: black;");
            bottomLabel.setText("choose file: " +selectedFile.getAbsolutePath());
            selectedFiles.add(selectedFile);

        }else if(!selectedFile.getName().toLowerCase().endsWith(".csv")) {
            selectedFile = null;
            System.out.println("incorrect file type");
            bottomLabel.setStyle("-fx-text-fill: red;");
            bottomLabel.setText("incorrect file type");
        }
    }

    //TODO fill in
    public void clearData(){
        selectedFile=null;
        selectedFolder=null;
        selectedFiles.clear();
        bottomLabel.setStyle("-fx-text-fill: black;");
        bottomLabel.setText("cleared data");
        System.out.println("cleared data");

    }

    public void save2CSV(){
        bottomLabel.setText("please wait...");

//        processedFile.clear(); //if clicked twice
        if(selectedFiles==null && selectedFolder==null){
            bottomLabel.setStyle("-fx-text-fill: red;");
            bottomLabel.setText("sorry cannot export without files");
            return;
        }

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
        for(File file : selectedFiles){
            System.out.println(file.getAbsolutePath());
        }


        OutputCSVWriter outputCSVWriter = new OutputCSVWriter(selectedFiles,"testOutputCSV.csv");

        processedFile =  outputCSVWriter.sortAndMergeFiles();
        outputCSVWriter.ExportToCSV(processedFile);

        routersOfAllFiles = outputCSVWriter.getAllRoutersOfTheFiles();

        bottomLabel.setStyle("-fx-text-fill: black;");
        bottomLabel.setText("successfully exported CSV file");
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
}

