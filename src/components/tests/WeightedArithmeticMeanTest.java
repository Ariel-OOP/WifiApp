package components.tests;

import components.*;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * Created by Nissan on 12/4/2017.
 */
public class WeightedArithmeticMeanTest {
    private static final double DELTA = 1e-8;

    @Test
    public void getWAMTest() throws Exception {
        String folderPath="FileResources";

        OutputCSVWriter outputCSVWriter = new OutputCSVWriter(folderPath,"testOutputCSV.csv");
        List<WifiPointsTimePlace> processedFile =  outputCSVWriter.sortAndMergeFiles();
        outputCSVWriter.ExportToCSV(processedFile);

        HashRouters<String, WIFISample> routersOfAllFiles = outputCSVWriter.getAllRoutersOfTheFiles();


        WeightedArithmeticMean weightedArithmeticMean = new WeightedArithmeticMean(routersOfAllFiles);
        WIFIWeight ww =weightedArithmeticMean.getWAM("00:22:b0:75:7d:eb");
        assertEquals(ww.getWIFI_Lat(), 32.16800543,DELTA);
        assertEquals(ww.getWIFI_Lon(), 34.80451717,DELTA);
        assertEquals(ww.getWIFI_Alt(),  33.01544196,DELTA);

        WIFIWeight ww2 =weightedArithmeticMean.getWAM("00:00:00:00:00:00");
        assertEquals(ww2,null);

    }
}