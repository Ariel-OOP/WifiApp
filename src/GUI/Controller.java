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
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable{

    public Label bottomLabel;
    public Label dataSize;
    public Label numOfAP;
    public Label latLabel;
    public Label lonLabel;
    public Label altLabel;
    public TextField macInput;

    public File selectedFolder;
    public File selectedFile;

    public HashRouters<String,WIFISample> routersOfAllFiles;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        selectedFile=null;
        selectedFolder=null;

    }

    public void folderChooser(){
        System.out.println("clicked addDir button");
        bottomLabel.setText("choose folder");
        DirectoryChooser dirChooser = new DirectoryChooser ();
        dirChooser.setInitialDirectory(new File("FileResources"));
        selectedFolder = dirChooser.showDialog(null);
        if (selectedFolder != null){
            System.out.println(selectedFolder.getAbsolutePath());
            bottomLabel.setText("choose folder: " +selectedFolder.getAbsolutePath());
        }else{
            System.out.println("didn't choose folder");
            bottomLabel.setText("didn't choose folder");
        }
    }

    public void fileChooser(){
        System.out.println("clicked addCSV button");
        bottomLabel.setText("choose file");
        FileChooser fileChooser = new FileChooser ();
        fileChooser.setInitialDirectory(new File("C:\\"));
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("CSV files","*.csv") );
        selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile==null){ //null
            System.out.println("didn't choose file");
            bottomLabel.setText("didn't choose file");
        }else if (selectedFile != null && selectedFile.getName().toLowerCase().endsWith(".csv") ){
            System.out.println(selectedFile.getAbsolutePath());
            bottomLabel.setText("choose file: " +selectedFile.getAbsolutePath());
        }else if(!selectedFile.getName().toLowerCase().endsWith(".csv")) {
            selectedFile = null;
            System.out.println("incorrect file type");
            bottomLabel.setText("incorrect file type");
        }
    }

    //TODO fill in
    public void clearData(){
        selectedFile=null;
        selectedFolder=null;
        bottomLabel.setText("cleared data");
    }

    //TODO button for destination
    //TODO fill in-only csv
    public void save2CSV(){

        OutputCSVWriter outputCSVWriter = new OutputCSVWriter(selectedFolder.toString(),"testOutputCSV.csv");
        List<WifiPointsTimePlace> processedFile =  outputCSVWriter.sortAndMergeFiles();
        outputCSVWriter.ExportToCSV(processedFile);

        routersOfAllFiles = outputCSVWriter.getAllRoutersOfTheFiles();
    }

    public void submitMac(){
        if (selectedFile==null && selectedFolder==null){
            bottomLabel.setText("no files selected for algorithm");
            return;
        }
        String userMacInput = macInput.getText();

        WeightedArithmeticMean weightedArithmeticMean = new WeightedArithmeticMean(routersOfAllFiles);
        WIFIWeight ww =weightedArithmeticMean.getWAM(userMacInput);

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
            latLabel.setText(String.valueOf(ww.getWIFI_Lat()).substring(0,7));
            lonLabel.setText(String.valueOf(ww.getWIFI_Lon()).substring(0,7));
            altLabel.setText(String.valueOf(ww.getWIFI_Alt()).substring(0,7));
        }else{
            latLabel.setText(String.valueOf(0.0));
            lonLabel.setText(String.valueOf(0.0));
            altLabel.setText(String.valueOf(0.0));
        }

    }

}

