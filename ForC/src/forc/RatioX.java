package forc;

import hultig.sumo.OpenNLPKit;
import hultig.sumo.Sentence;
import hultig.sumo.Text;
import hultig.sumo.Word;
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
public class RatioX {
    static Random ran = new Random();

    static public double vocADJRsome(String text, int n, OpenNLPKit model) {
        String[] saWords = text.split(" "); // String Array
        
        List<String> words = new ArrayList<>();//Lista para as palavras
        for (int j = 0; j < 100; j++) { //Fazer 100 amostras
            for (int i = 0; i < n; i++) { //Fazer amostras de n palavras começando em 35
                //System.out.println(saWords[ran.nextInt(saWords.length)]);
                words.add(saWords[ran.nextInt(saWords.length)]);
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
    
    
    
    
    public static double testADJR(List<Word> list) {
        double ttr= 0.0;
        for (int n = 35; n <= 50; n++) {
            int iAdjCounter  = 0;
            int iVerbCounter = 0;
            int iNounCounter = 0;
            for (int j = 0; j < 100; j++) { //Fazer 100 amostras
                for (int i = 0; i < n; i++) {
                    Word wi=    list.get(ran.nextInt(list.size()));
                    String pos= wi.getTag();
                    if (pos.contains("JJ")) {
                        iAdjCounter++;
                    } else if (pos.contains("VB")) {
                        iVerbCounter++;
                    } else if (pos.contains("NN")) {
                        iNounCounter++;
                    }
                }
            }
            double soma=  iAdjCounter + iNounCounter + iVerbCounter;
            double ratio= (iAdjCounter) / soma;
            System.out.printf(
                    "ADJ: %4d     NN: %4d     VB: %4d    SUM: %4.0f    Ratio: %.5f\n", 
                    iAdjCounter, iNounCounter, iVerbCounter, soma, ratio
            );
            ttr+= ratio;
        }
        return ttr/16.0;
    }
    
    
    public static double[] testADJR02(List<Word> list) {
        double[] v= {0.0, 0.0};
        for (int n = 35; n <= 50; n++) {
            double sumRatio= 0.0;
            for (int j = 0; j < 100; j++) { //Fazer 100 amostras
                int aj= 0;
                int nm= 0;
                int vb= 0;
                int rb= 0;
                for (int i = 0; i < n; i++) {
                    Word wi=    list.get(ran.nextInt(list.size()));
                    String pos= wi.getTag();
                    if (pos.startsWith("JJ")) {
                        aj++;
                    } else if (pos.startsWith("VB")) {
                        nm++;
                    } else if (pos.startsWith("NN")) {
                        vb++;
                    } else if (pos.startsWith("RB")) {
                        rb++;
                    }
                }
                double ratio= 1.0*(aj) / (nm + vb + rb);
                sumRatio+= ratio;
            }
            double r100= sumRatio/100.0;
            if ( r100 > v[1] ) v[1]= r100;
            v[0]+= r100;
        }
        v[0]= v[0]/16;
        return v;
    }
    

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        String path="";
        OpenNLPKit model = new OpenNLPKit("/Users/jpc/GDrive/Jose Santos/Codigo/opennlp-tools-1.5.0" + "/models/english/");
        //System.out.println("Introduz diretoria do ficheiro");
        File dir = //new File(new Scanner(System.in).nextLine());
                 new File("/Users/jpc/Desktop/X");
        //Scanner read = new Scanner(System.in);
        File[] ListOfFiles = dir.listFiles();

        System.out.println("\n");
        for (int i = 0; i < ListOfFiles.length; i++) {
            if ( !ListOfFiles[i].getPath().endsWith("txt") ) continue;
            Text texto = new Text();
            texto.readFile(ListOfFiles[i].toString());
            List<Word> lista= new ArrayList<Word>();
            int MAX= 0;
            for (Sentence s : texto) {
                Sentence sPOS= model.postag(s);
                //if ( ran.nextInt(100) == 7 && ++MAX < 7 )
                //    System.out.println("   "+sPOS);
                for (Word w : sPOS)  lista.add(w);
            }
               
            double[] v= testADJR02(lista);
            System.out.printf("%-30s", ListOfFiles[i].getName());
            for (int j = 0; j < v.length; j++) 
                System.out.printf("   %.7f", v[j]);
            System.out.println();
        }

    }

}
