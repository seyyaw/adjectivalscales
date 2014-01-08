package adj.scales.langtech.tudarmstadt.de;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;

/**
 * Hello world!
 *
 */
public class ComputeSimilarity
{
    static String adj1 = "FURIOUS";
    static String adj2 = "CALM";
    static String halfAdj = "";
    static String firstHalfAdj = "";
    static String secondHalfAdj = "";

    public static void main(String[] args)
        throws FileNotFoundException
    {
        FileReader reader = new FileReader(new File(args[0]));
        LineIterator lineIterator = IOUtils.lineIterator(reader);

        List<Double> vector1List = new ArrayList<Double>();
        List<Double> vector2List = new ArrayList<Double>();
        List<Double> vectorsMiddle = new ArrayList<Double>();

        List<Double> vectorFirstHalf = new ArrayList<Double>();
        List<Double> vectorsSecondHalf = new ArrayList<Double>();

        List<String> vectors = getVectors(lineIterator, adj1, adj2);

        String[] vectors1 = vectors.get(0).split(" ");
        String[] vectors2 = vectors.get(1).split(" ");

        for (int i = 1; i < vectors1.length; i++) {
            vector1List.add(Double.parseDouble(vectors1[i]));
            vector2List.add(Double.parseDouble(vectors2[i]));
        }

        setAverageVector(vector1List, vector2List, vectorsMiddle);

        System.out.println("SIM : " + cosineSimilarity(vector1List, vector2List));

        reader = new FileReader(new File(args[0]));
        lineIterator = IOUtils.lineIterator(reader);
        lineIterator.nextLine();

        Map<String, List<Double>> middleVector = getMiddleVector(lineIterator, adj1, adj2,
                vectorsMiddle);
        // =============================HALF============================//
        for (String middleWord : middleVector.keySet()) {
            System.out.println("MIDDLE : " + middleWord);
            halfAdj = middleWord;
        }

        setAverageVector(vector1List, middleVector.get(halfAdj), vectorFirstHalf);
        setAverageVector(middleVector.get(halfAdj), vector2List, vectorsSecondHalf);

        reader = new FileReader(new File(args[0]));
        lineIterator = IOUtils.lineIterator(reader);
        lineIterator.nextLine();

        Map<String, List<Double>> firstHalfMiddleVector = getMiddleVector(lineIterator, adj1,
                halfAdj, vectorFirstHalf);

        for (String middleWord : firstHalfMiddleVector.keySet()) {
            System.out.println("1st Half : " + middleWord);
            firstHalfAdj = middleWord;
        }

        reader = new FileReader(new File(args[0]));
        lineIterator = IOUtils.lineIterator(reader);
        lineIterator.nextLine();

        Map<String, List<Double>> secondHalfMiddleVector = getMiddleVector(lineIterator, halfAdj,
                adj2, vectorsSecondHalf);
        for (String middleWord : secondHalfMiddleVector.keySet()) {
            System.out.println("2nd Half : " + middleWord);
            secondHalfAdj = middleWord;
        }

    }

    private static void setAverageVector(List<Double> vector1List, List<Double> vector2List,
            List<Double> vectorsMiddle)
    {
        for (int i = 1; i < vector1List.size(); i++) {

            vectorsMiddle.add((vector1List.get(i - 1) - vector2List.get(i - 1)) / 2.0
                    + vector2List.get(i - 1));
        }

    }

    private static List<String> getVectors(LineIterator lineIterator, String word1, String word2)
    {
        List<String> vectors = new ArrayList<String>();
        while (lineIterator.hasNext()) {
            String line = lineIterator.nextLine();
            String[] words = line.split(" ");
            if (words[0].equals(word1)) {
                vectors.add(line);
            }
            if (words[0].equals(word2)) {
                vectors.add(line);
            }
            if (vectors.size() == 2) {
                break;
            }
        }
        return vectors;
    }

    private static Map<String, List<Double>> getMiddleVector(LineIterator lineIterator,
            String word1, String word2, List<Double> vectorsMiddle)
    {
        Map<String, List<Double>> middleVector = new HashMap<String, List<Double>>();
        double max = 0.0;
        while (lineIterator.hasNext()) {
            String line = lineIterator.nextLine();
            String[] words = line.split(" ");
            List<Double> vector = new ArrayList<Double>();
            for (int i = 1; i < words.length; i++) {
                vector.add(Double.parseDouble(words[i]));
            }
            Double average = cosineSimilarity(vectorsMiddle, vector);
            if (max < average
                    && !(words[0].equals(adj1)
                            || words[0].equals(adj2) || words[0].equals(firstHalfAdj) || words[0]
                                .equals(halfAdj))) {
                max = average;
                middleVector.clear();
                middleVector.put(words[0], vector);
            }
        }
        return middleVector;
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

}
