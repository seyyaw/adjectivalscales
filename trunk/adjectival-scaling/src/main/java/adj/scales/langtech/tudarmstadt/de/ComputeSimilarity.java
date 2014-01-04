package adj.scales.langtech.tudarmstadt.de;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;

/**
 * Hello world!
 *
 */
public class ComputeSimilarity
{
    public static void main(String[] args)
        throws FileNotFoundException
    {
        FileReader reader = new FileReader(new File(args[0]));
        LineIterator lineIterator = IOUtils.lineIterator(reader);
        String adj1 = "FASTEST";
        String adj2 = "FAST";
        String vector1 = null, vector2 = null, vectorMiddle = "";
        while (lineIterator.hasNext()) {
            String line = lineIterator.nextLine();
            String[] words = line.split(" ");
            if (words[0].equals(adj1)) {
                vector1 = line;
            }
            if (words[0].equals(adj2)) {
                vector2 = line;
            }
            if (vector1 != null && vector2 != null) {
                break;
            }
        }
        List<Double> vector1List = new ArrayList<Double>();
        List<Double> vector2List = new ArrayList<Double>();
        List<Double> vectorsMiddle = new ArrayList<Double>();
        String[] vectors1 = vector1.split(" ");
        String[] vectors2 = vector2.split(" ");
        for (int i = 1; i < vectors1.length; i++) {
            vector1List.add(Double.parseDouble(vectors1[i]));
            vector2List.add(Double.parseDouble(vectors2[i]));
            vectorsMiddle.add((vector1List.get(i - 1) - vector2List.get(i - 1)) / 2.0
                    + vector2List.get(i - 1));
            vectorMiddle = vectorMiddle + vectorsMiddle.get(i - 1) + " ";
        }

        System.out.println(vector1);
        System.out.println(vector2);
        System.out.println(vectorMiddle);
        System.out.println(cosineSimilarity(vector1List, vector2List));
        System.out.println(cosineSimilarity(vector1List, vectorsMiddle));
        System.out.println(cosineSimilarity(vector2List, vectorsMiddle));

        // stores lower adjectives with the cosine similarity of
        Map<Double, String> lower = new TreeMap<Double, String>();

        // stores uper adjectives with the cosine similarity of
        Map<Double, String> upper = new TreeMap<Double, String>();

        reader = new FileReader(new File(args[0]));
        lineIterator = IOUtils.lineIterator(reader);
        lineIterator.nextLine();

        double max = 0.0;
        String word = "";

        while (lineIterator.hasNext()) {
            String line = lineIterator.nextLine();
            String[] words = line.split(" ");
            List<Double> vector = new ArrayList<Double>();
            for (int i = 1; i < words.length; i++) {
                vector.add(Double.parseDouble(words[i]));
            }
            Double middle = cosineSimilarity(vector1List, vector2List);
            Double similarity = cosineSimilarity(vectorsMiddle, vector);
            if (middle < similarity) {
                addToUpper(upper, similarity, words[0]);
            }
            else {
                addToLower(lower, similarity, words[0]);
            }

            Double average = cosineSimilarity(vectorsMiddle, vector);
            if (max < average && !(words[0].equals(adj1) || words[0].equals(adj2))) {
               max = average;
               word = words[0];
            }
        }
        System.out.println(word);
        System.out.println(max);
        System.out.println(lower);
        System.out.println(upper);
    }

    // http://bytes4u.blogspot.de/2013/03/cosine-similarity-implementation-in-java.html
    private static double cosineSimilarity(List<Double> vec1, List<Double> vec2)
    {
        double dp = dot_product(vec1, vec2);
        double magnitudeA = find_magnitude(vec1);
        double magnitudeB = find_magnitude(vec2);
        return (dp) / (magnitudeA * magnitudeB);
    }

    private static double find_magnitude(List<Double> vec)
    {
        double sum_mag = 0;
        for (double element : vec) {
            sum_mag = sum_mag + element * element;
        }
        return Math.sqrt(sum_mag);
    }

    private static double dot_product(List<Double> vec1, List<Double> vec2)
    {
        double sum = 0;
        for (int i = 0; i < vec1.size(); i++) {
            sum = sum + vec1.get(i) * vec2.get(i);
        }
        return sum;
    }

    private static void addToLower(Map<Double, String> aWords, Double aValue, String aWord)
    {
        List<Double> aLists = new ArrayList<Double>(aWords.keySet());
        if (aLists.size() < 2) {
            aWords.put(aValue, aWord);
        }
        else if (aLists.get(0) < aValue) {
            aWords.remove(aLists.get(0));
            aWords.put(aValue, aWord);
        }

    }

    private static void addToUpper(Map<Double, String> aWords, Double aValue, String aWord)
    {

        List<Double> aLists = new ArrayList<Double>(aWords.keySet());
        if (aLists.size() < 2) {
            aWords.put(aValue, aWord);
        }
        else if (aLists.get(aLists.size() - 1) > aValue) {
            aWords.remove(aLists.get(aLists.size() - 1));
            aWords.put(aValue, aWord);
        }

    }
}
