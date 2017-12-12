package components.tests;

import components.*;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Nissan on 12/4/2017.
 */
public class WeightedArithmeticMeanTest {
    private static final double DELTA = 1e-8;

    @Test
    public void getWAMTest() throws Exception {
        List<File> folderPath= new ArrayList<>();
        folderPath.add(new File("C:\\Users\\nissa\\Desktop\\Year 2\\OOP\\Programs\\NewWifiApp\\FileResources\\WigleWifi_20171027164529.csv"));
        folderPath.add(new File("C:\\Users\\nissa\\Desktop\\Year 2\\OOP\\Programs\\NewWifiApp\\FileResources\\WigleWifi_20171027164517.csv"));
        folderPath.add(new File("C:\\Users\\nissa\\Desktop\\Year 2\\OOP\\Programs\\NewWifiApp\\FileResources\\WigleWifi_20171027164529.csv"));


        OutputCSVWriter outputCSVWriter = new OutputCSVWriter(folderPath,"testOutputCSV.csv");
        List<WifiPointsTimePlace> processedFile =  outputCSVWriter.sortAndMergeFiles();
        outputCSVWriter.ExportToCSV(processedFile);

        HashRouters<String, WIFISample> routersOfAllFiles = outputCSVWriter.getAllRoutersOfTheFiles();


        WeightedArithmeticMean weightedArithmeticMean = new WeightedArithmeticMean(routersOfAllFiles);
        WIFIWeight ww =weightedArithmeticMean.getWAMbyMac("00:22:b0:75:7d:eb");
        assertEquals(ww.getWIFI_Lat(), 32.16800543,DELTA);
        assertEquals(ww.getWIFI_Lon(), 34.80451717,DELTA);
        assertEquals(ww.getWIFI_Alt(),  33.01544196,DELTA);

        WIFIWeight ww2 =weightedArithmeticMean.getWAMbyMac("00:00:00:00:00:00");
        assertEquals(ww2,null);

    }
}