import java.io.*;
import java.util.*;


public class Driver {
    // Display data as bar charts and averages
    public static  void showHistogram(Map<String, Double> mapValue)
    {
        System.out.println("\tthe number of times with a given sentiment score");
        for (Map.Entry<String, Double> entry : mapValue.entrySet()) {
            System.out.printf("%-15s : ",entry.getKey());
            String str = "";
            for (int j = 0 ; j < entry.getValue()*20; j ++)
            {
                str =  str + "*";
            }
            System.out.printf("%-50s  %.2f\n",str,entry.getValue());
        }
    }
    public static Map<String,Integer> printTopWords(ArrayList<Sentence> sentences){
        Map <String,Integer> wordCounts = new HashMap<String,Integer>();
        for (Sentence sentence : sentences) {
            ArrayList<String> words = sentence.splitSentence();
            //System.out.println(Arrays.toString(words.toArray()));
            for (String word : words) {
                if (wordCounts.containsKey(word))
                    wordCounts.put(word, wordCounts.get(word) + 1);
                else
                    wordCounts.put(word, 1);
            }
        }

        return wordCounts;
    }
    public static void main(String[] args) {
        // create list of sentences from csv
        ArrayList<Sentence> sentences = new ArrayList<Sentence>();
        try{
            Scanner fr = new Scanner(new File("E:\\IDEA\\part3\\testdata.manual.2009.06.14.csv"));
            System.out.print("file content:");
            while(fr.hasNext()) {
                String line = fr.nextLine();
                String[] strings = line.split(",");
                sentences.add(new Sentence(strings[5].substring(1,strings[5].length() - 1), strings[2].substring(1,strings[2].length() - 1)));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // question 1
        // store all of the time
        String[] timeFilters = {"Mon Jun 08", "Mon May 11", "Mon May 18", "Mon May 25", "Sat Jun 13", "Sat May 16", "Sat May 23",
        "Sun Jun 07","Sun Jun 14", "Sun May 17","Sun May 24", "Sun May 31", "Thu Jun 04", "Thu May 14", "Tue Jun 02",
        "Tue Jun 09", "Tue May 19", "Wed Jun 10", "Wed May 20", "Wed May 27", "Wed May 20", "Wed Jun 10"};
        Map<String, Double> mapValue = new HashMap<>();
        Map<String, Integer> mapNum = new HashMap<>();

        for (String str : timeFilters)
        {
            mapValue.put(str, 0.0);
            mapNum.put(str, 0);
        }
        for (Sentence sentence : sentences) {

            for (String str : timeFilters)
            {
                String filter = str+ " 00:00:00 UTC 2009-" + str + " 23:59:59 UTC 2009";
                if (sentence.keep(filter)) {
                    mapValue.replace(str,mapValue.get(str) + sentence.getSentiment()) ;
                    mapNum.replace(str,mapNum.get(str) + 1) ;
                }
            }
        }
        for (Map.Entry<String, Double> entry : mapValue.entrySet()) {
            int num = mapNum.get(entry.getKey());
            entry.setValue(entry.getValue() / num);
        }
        showHistogram(mapValue);

        // question 2
        // get topic word of all sentence by ranking the frequence of all words
        Map<String,Integer> wordCounts = printTopWords(sentences);
        ArrayList <String> results = new ArrayList<String>();
        for (Map.Entry<String, Integer> set : wordCounts.entrySet()){
            String value = set.getValue().toString();
            results.add(value + " " + set.getKey());
        }
        Collections.sort(results);
        Collections.reverse(results);
        // the topic is results[0]
        double []values = {0,0,0,0,0,0};
        for (int i = 0 ; i < 5; i ++)
        {
            double value = 0;
            int num = 0;
            for (Sentence sentence : sentences) {

                for (String str : timeFilters)
                {
                    String filter = results.get(i).split(" ")[1];
                    if (sentence.keep(filter)) {
                        value += sentence.getSentiment();
                        num += 1;
                    }
                }
            }
            value /= num;
            values[i] = value;
        }
        for (int i = 0 ; i < 5; i ++)
        {
            System.out.printf("The topic is %s , and average score is %.2f\n",results.get(i).split(" ")[1], values[i]);
        }


    }
}
