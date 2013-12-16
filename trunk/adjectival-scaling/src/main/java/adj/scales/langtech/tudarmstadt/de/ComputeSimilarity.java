package adj.scales.langtech.tudarmstadt.de;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

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
        String adj1 = "worst";
        String adj2 = "best";
        String vector1 = null, vector2 = null, vectorMiddle = "";
        while (lineIterator.hasNext()) {
            String line = lineIterator.nextLine();
            String[] words = line.split(" ");
            if (words[0].toLowerCase().equals(adj1)) {
                vector1 = line;
            }
            if (words[0].toLowerCase().equals(adj2)) {
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
        for(int i = 1; i<vectors1.length;i++){
            vector1List.add(Double.parseDouble(vectors1[i]));
            vector2List.add(Double.parseDouble(vectors2[i]));
            vectorsMiddle.add((vector1List.get(i-1) - vector2List.get(i-1))/2.0 + vector2List.get(i-1));
            vectorMiddle = vectorMiddle +  vectorsMiddle.get(i-1) + " ";
        }
        System.out.println(vector1);
        System.out.println(vector2);
        System.out.println(vectorMiddle);
    }
}
