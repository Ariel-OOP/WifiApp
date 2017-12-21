package components;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Moshe
 */
public class Algorithm2 {

    //Constants of the calculate.
    final static double sig_diff = 0.4;
    final static double power = 2;
    final static double norm = 10000;
    final static int diff_no_sig = 100;

    /**
     * The function calculates the similarity between each line from the "FinalCSV" file and the information from the user "inputFromUser".
     * @param FinalCSV - List lines of a Final (combination) CSV file. Type of list is WifiPointsTimePlace.
     * @param inputFromUser - List of inputs from the user when each input contains MAC address and power. Type of list is WIFIWeight.
     * @param k - The number of rows that are most similar to the input, returned by the function within a list.
     * @return the k rows that most similar. Type of ArrayList<WIFIWeight>.
     */
    public static ArrayList<WIFIWeight> getKMostSimilar(List<WifiPointsTimePlace> FinalCSV,ArrayList<WIFIWeight> inputFromUser, int k)
    {
        WIFIWeight kSimilar[] = new WIFIWeight[k];
        WIFIWeight wifiWeightOfOneLine;
        ArrayList<WIFIWeight> kMostSimilar = new ArrayList<>();

        for(int i = 0; i < kSimilar.length; i++)//Reset of a list
            kSimilar[i] = new WIFIWeight("",0,0,0,0,0);

        //kSimilar = Arrays.stream(kSimilar).map((x) -> x = new WIFIWeight("",0,0,0,0,0));

        for (WifiPointsTimePlace line : FinalCSV)
        {
            wifiWeightOfOneLine = line.checkSimilarity(inputFromUser);//Calculate the similar for each line

            WIFIWeight cotterntWifiWight = wifiWeightOfOneLine;
            WIFIWeight temp;
            //Try to insert the new wifiWeightOfOneLine into the array of k most similar.
            for(int i = 0; i < kSimilar.length; i++)
            {
                if(kSimilar[i].getWIFI_Weight() < cotterntWifiWight.getWIFI_Weight())
                {
                    temp = kSimilar[i];
                    kSimilar[i] = cotterntWifiWight;
                    cotterntWifiWight = temp;
                }
            }
        }

        for (int i = 0; i < kSimilar.length; i++)
            kMostSimilar.add(kSimilar[i]);

        return kMostSimilar;
    }

//    private static void enterToArrayOfKSimilar(WIFIWeight[] kSimilar, WIFIWeight wifiWeightOfOneLine) {
//        WIFIWeight cotterntWifiWight = wifiWeightOfOneLine;
//        WIFIWeight temp;
//
//
//        for(int i = 0; i < kSimilar.length; i++)
//        {
//            if(kSimilar[i].getWIFI_Weight() < cotterntWifiWight.getWIFI_Weight())
//            {
//                temp = kSimilar[i];
//                kSimilar[i] = cotterntWifiWight;
//                cotterntWifiWight = temp;
//            }
//        }
//    }

    /**
     * The funcion gets two lists of signals and calculate the weight (pi, in a assignment) between the lists.
     * @param signalsLine - List of signals. Type of ArrayList is Integer.
     * @param signalsUser - List of signals. Type of ArrayList is Integer.
     * @return weight, Type of integer.
     */
    public static double calcWeight(ArrayList<Integer> signalsLine, ArrayList<Integer> signalsUser)
    {
        double weight = 1;
        int singleDiff = 0;

        ArrayList<Integer> diffs = new ArrayList<Integer>();
        ArrayList<Double> weights = new ArrayList<Double>();

        for(int i = 0; i < signalsUser.size(); i++) {

            singleDiff = (signalsLine.get(i) == -120)? diff_no_sig : Math.abs(Math.abs(signalsLine.get(i)) - Math.abs(signalsUser.get(i)));
            diffs.add(singleDiff);
        }

        for(int i = 0; i < diffs.size(); i++) {
            int difference = (diffs.get(i) > 3)? diffs.get(i) : 3;//Don't divide by zero.
            weights.add(norm/(Math.pow(difference,sig_diff)*Math.pow(signalsUser.get(i),power)));
        }

        for(int i = 0; i < weights.size(); i++) {
            weight *= weights.get(i);
        }

        return weight;
    }
}
