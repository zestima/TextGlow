/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
///home/zsdaking/Drive/Jose Santos/Textos/Speeches
//adicionar 100 blocos
package forc;

import hultig.sumo.OpenNLPKit;
import hultig.sumo.Text;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author zsdaking
 */
public class Ratio {

    static public double vocADJRsome(String text, int n, OpenNLPKit model) {
        String[] saWords = text.split(" "); // String Array
        Random r = new Random();
        List<String> words = new ArrayList<>();//Lista para as palavras
        for (int j = 0; j < 100; j++) { //Fazer 100 amostras
            for (int i = 0; i < n; i++) { //Fazer amostras de n palavras começando em 35
                //System.out.println(saWords[r.nextInt(saWords.length)]);
                words.add(saWords[r.nextInt(saWords.length)]);
            }
        }
        int iAdjCounter = 0;
        int iVerbCounter = 0;
        int iNounCounter = 0;
        for (int i = 0; i < words.size(); i++) { //Percorrer todas as palavras
            String oWord = model.postag(words.get(i));
            if (oWord.contains("JJ")) {
                iAdjCounter++;
            } else if (oWord.contains("VB")) {
                iVerbCounter++;
            } else if (oWord.contains("NN")) {
                iNounCounter++;
            }
        }
        double soma=  iAdjCounter + iNounCounter + iVerbCounter;
        double ratio= (iAdjCounter) / soma;
        System.out.printf("ADJ: %4d     NN: %4d     VB: %4d    SUM: %4.0f    Ratio: %.5f\n", 
                iAdjCounter, iNounCounter, iVerbCounter, soma, ratio);     
        //System.out.println((iAdjCounter) / (iNounCounter + iVerbCounter));
        return ratio;
    }

    public static String vocADJR(Text oText, OpenNLPKit model) {
        double ttr = 0;
        for (int i = 35; i <= 50; i++) { //Correr [35,50] ou [35,50[
            ttr = ttr + vocADJRsome(oText.toString(), i, model);
        }
        return (String.valueOf(ttr / 16));
    }

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        String path="";
        OpenNLPKit model = new OpenNLPKit("/Users/jpc/GDrive/Jose Santos/Codigo/opennlp-tools-1.5.0" + "/models/english/");
        //System.out.println("Introduz diretoria do ficheiro");
        File dir = //new File(new Scanner(System.in).nextLine());
                 new File("/Users/jpc/GDrive/Jose Santos/Textos/Test 2");
        //Scanner read = new Scanner(System.in);
        File[] ListOfFiles = dir.listFiles();

        for (int i = 0; i < ListOfFiles.length; i++) {
            if ( !ListOfFiles[i].getPath().endsWith("txt") ) continue;
            Text oText = new Text();
            oText.readFile(ListOfFiles[i].toString());
            String s = vocADJR(oText, model);
            System.out.println(ListOfFiles[i] + "-" + s);       
            System.out.println("\n");
        }

    }

}
