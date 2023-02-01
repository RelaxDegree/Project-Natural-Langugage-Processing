import java.sql.Timestamp;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

import org.ejml.simple.SimpleMatrix;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations.SentimentAnnotatedTree;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import java.io.BufferedReader;//this has been used to extract  the csv file
import java.io.File; //this has been used to extract the csv file
import java.io.FileReader; //this has been used to extract  the csv file
import java.io.IOException; //this has been used to rextract the csv file
import java.util.ArrayList;//this has been used to use the arra list


class Sentence {
    public String text;
    public String date;
    public String location;
    public String chapter;
    public  int sentiment;
    private Timestamp timestamp;
    public Sentence(String text) {
        this.text = text;
    }
    @Override
    public String toString(){
        return this.text;
    }
    public void addToEnd(String endText){
        this.text += endText;
    }

    private double sentimentScore;

    public Sentence(String text, String date) {
        this.text = text;
        this.date = date;
    }
    public Sentence(String text, String date, String chapter) {
        this.text = text;
        this.date = date;
        this.chapter = chapter;
    }
    public String getText() {
        return text;
    }

    public Timestamp getTime() {
        return timestamp;
    }

    public double getSentimentScore() {
        return sentimentScore;
    }
    // when filter contains '-', it is time filter ; when filter contains ' ', it is chapter filter ;
    // when filter doesn't contain '-' or ' ', it is keyword filter ;
    public boolean keep(String temporalRange) {
        try {
            // Check if the input temporal range is a date range or a chapter range
            if (temporalRange.contains("-")) {
                // Use SimpleDateFormat to parse the input temporal range into start and end Timestamp objects
                SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd hh:mm:ss 'UTC' yyyy", Locale.ENGLISH);
                String[] dates = temporalRange.split("-");
                Timestamp start = new Timestamp(format.parse(dates[0]).getTime());
                Timestamp end = new Timestamp(format.parse(dates[1]).getTime());
                // Convert the Sentence object's date into a Timestamp object
                Timestamp sentenceTimestamp = new Timestamp(format.parse(this.date).getTime());

                // Check if the Sentence object's date falls within the specified range and return the result
                return !sentenceTimestamp.before(start) && !sentenceTimestamp.after(end);
            } else if (temporalRange.contains(" "))
            {
                // Check if the Sentence object's chapter matches the specified chapter and return the result
                return this.chapter.equals(temporalRange);
            }
            else
            {
                return text.contains(temporalRange);
            }
        } catch (ParseException e) {
            // Handle any exceptions and return false
            return false;
        }
    }

    public ArrayList<String> splitSentence() {
        // split the sentence into words
        String[] words = this.text.split("\\s+");

        // create an ArrayList to store the words
            ArrayList<String> wordList = new ArrayList<String>();

        // add each word to the ArrayList
        for (String word : words) {
            int flag = 0;

            String[] stopwords = {"a", "about", "above", "after", "again", "against", "all", "am", "an", "and", "any", "are", "aren't", "as", "at", "be", "because", "been", "before", "being", "below", "between", "both", "but", "by", "can't", "cannot", "could", "couldn't", "did", "didn't", "do", "does", "doesn't", "doing", "don't", "down", "during", "each", "few", "for", "from", "further", "had", "hadn't", "has", "hasn't", "have", "haven't", "having", "he", "he'd", "he'll", "he's", "her", "here", "here's", "hers", "herself", "him", "himself", "his", "how", "how's", "i", "i'd", "i'll", "i'm", "i've", "if", "in", "into", "is", "isn't", "it", "it's", "its", "itself", "let's", "me", "more", "most", "mustn't", "my", "myself", "no", "nor", "not", "of", "off", "on", "once", "only", "or", "other", "ought", "our", "ours ourselves", "out", "over", "own", "same", "shan't", "she", "she'd", "she'll", "she's", "should", "shouldn't", "so", "some", "such", "than", "that", "that's", "the", "their", "theirs", "them", "themselves", "then", "there", "there's", "these", "they", "they'd", "they'll", "they're", "they've", "this", "those", "through", "to", "too", "under", "until", "up", "very", "was", "wasn't", "we", "we'd", "we'll", "we're", "we've", "were", "weren't", "what", "what's", "when", "when's", "where", "where's", "which", "while", "who", "who's", "whom", "why", "why's", "with", "won't", "would", "wouldn't", "you", "you'd", "you'll", "you're", "you've", "your", "yours", "yourself", "yourselves"};
            for (String stop : stopwords) {
                if (stop.equals(word.toLowerCase())) {
                    flag = 1;
                    break;
                }
            }
            if (flag != 1)
                wordList.add(word.toLowerCase());
        }

        return wordList;
    }
    public int getSentiment() {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, parse, sentiment");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        Annotation annotation = pipeline.process(text);
        CoreMap sentence = annotation.get(CoreAnnotations.SentencesAnnotation.class).get(0);
        Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
        return RNNCoreAnnotations.getPredictedClass(tree);
    }
}
