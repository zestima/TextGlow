/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
///home/zsdaking/Drive/Jose Santos/Textos/Speeches
//adicionar 100 blocos
package forc;

import hultig.io.FileIN;
import hultig.io.FileOUT;
import hultig.sumo.OpenNLPKit;
import hultig.sumo.Sentence;
import hultig.sumo.Text;
import hultig.sumo.Word;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author zsdaking
 */
public class ForC {

    static Metrics main = new Metrics();

    public static void vocRelatedStuff(String text, String output) throws IOException {
        String[] saWords = text.split(" "); // String Array
        HashMap<String, Integer> HMcounter = new HashMap<>();//Criar HashMap associando palavras a número de repetições
        int iVocCount = 0, iNOWCounter = 0; //Contadores Necessários
        for (int i = 0; i < saWords.length; i++) { //Percorrer todas as palavras
            if (HMcounter.containsKey(saWords[i])) { //Se estiverem no HashMap aumentar o seu número de repetições 1 vez
                HMcounter.put(saWords[i], HMcounter.get(saWords[i]) + 1);
            } else {//Se não estiverem, colocá-las lá
                HMcounter.put(saWords[i], 1);
                iVocCount++;//Número de palavras diferentes, ou seja do vocabulário
            }
            iNOWCounter++;//Número total de palavras
        }
        BufferedWriter ttr = new BufferedWriter(new FileWriter(output + "/ATTR.dat", true));
        BufferedWriter avoc = new BufferedWriter(new FileWriter(output + "/AVOC.dat", true));
        BufferedWriter aws = new BufferedWriter(new FileWriter(output + "/AWS.dat", true));
        //System.out.println((double) (iVocCount) / iTotCount);
        main.setAvoc(iVocCount);
        main.setAws(iNOWCounter);
        main.setTtr((int) Math.round(((double) (iVocCount) / iNOWCounter) * 1000));
        ttr.write(main.getTtr() + " ");
        ttr.flush();
        avoc.write(main.getAvoc() + " ");
        avoc.flush();
        aws.write(main.getAws() + " ");
        aws.flush();
        ttr.close();
        avoc.close();
        aws.close();
        main = new Metrics();
    }

    public static void wordRelatedStuff(Sentence[] oSentence, OpenNLPKit model, String output) throws IOException {
        int iAdjCounter = 0;
        int iVerbCounter = 0;
        int iNounCounter = 0;
        int iNonWordCounter = 0;
        int iSmallWord = 0;
        int iMidWord = 0;
        int iLongWord = 0;
        for (int k = 0; k < oSentence.length; k++) {
            Sentence s = model.postag(oSentence[k]);
            for (Word oWord : s) {
                if (!oWord.isWord()) {
                    iNonWordCounter++;
                }
                if (oWord.length() < 3) {
                    iSmallWord++;
                }
                if (oWord.length() > 6) {
                    iLongWord++;
                }
                if (oWord.length() >= 3 && oWord.length() <= 6) {
                    iMidWord++;
                }
                if (oWord.getTag().contains("JJ")) {
                    iAdjCounter++;
                } else if (oWord.getTag().contains("VB")) {
                    iVerbCounter++;
                } else if (oWord.getTag().contains("NN")) {
                    iNounCounter++;
                }
            }
            BufferedWriter adj = new BufferedWriter(new FileWriter(output + "/AADJ.dat", true));
            BufferedWriter anw = new BufferedWriter(new FileWriter(output + "/ANW.dat", true));
            BufferedWriter asw = new BufferedWriter(new FileWriter(output + "/ASW.dat", true));
            BufferedWriter amw = new BufferedWriter(new FileWriter(output + "/AMW.dat", true));
            BufferedWriter alw = new BufferedWriter(new FileWriter(output + "/ALW.dat", true));
            main.setAsw(iSmallWord);
            main.setAnw(iNonWordCounter);
            main.setAmw(iMidWord);
            main.setAlw(iLongWord);
            main.setAdj((int) Math.round(((double) (iAdjCounter) / (iNounCounter + iVerbCounter + 1)) * 1000));
            adj.write(main.getAdj() + " ");
            adj.flush();
            anw.write(main.getAnw() + " ");
            anw.flush();
            asw.write(main.getAsw() + " ");
            asw.flush();
            amw.write(main.getAmw() + " ");
            amw.flush();
            alw.write(main.getAlw() + " ");
            alw.flush();
            adj.close();
            anw.close();
            asw.close();
            amw.close();
            alw.close();
            main = new Metrics();

        }
//System.out.println((double) (iAdjCounter) / (iNounCounter + iVerbCounter));
    }

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        OpenNLPKit model = new OpenNLPKit("/home/zsdaking/Drive/Jose Santos/Codigo/opennlp-tools-1.5.0" + "/models/english/");
        System.out.println("Introduz diretoria do ficheiro");
        File dir = new File(new Scanner(System.in).nextLine());
        Scanner read = new Scanner(System.in);
        System.out.println("Introduz diretoria de output");
        String output = read.nextLine();
        File[] ListOfFiles = dir.listFiles();

        for (int i = 0; i < ListOfFiles.length; i++) {
            Text oText = new Text();
            if (ListOfFiles[i].toString().contains(".txt")) {
                oText.readFile(ListOfFiles[i].toString());
                Sentence s = new Sentence();
                int j = 0;
                for (int k = 0, numTokens = 0; i < oText.size(); i++) {
                    Sentence sentenceI = oText.get(i);
                    for (Word w : sentenceI) {
                        s.add(w);
                        numTokens++;
                        if (numTokens % 100 == 0) {
                            Text t = new Text(s.toString());
                            vocRelatedStuff(t.toString(), output);
                            wordRelatedStuff(t.getSentences(), model, output);
                            s = new Sentence();
                        }
                    }
                }
            } else {
                Blog b = Blog.getInstance(new FileIN(ListOfFiles[i].toString()));
                oText = b.getText();
                Sentence s = new Sentence();
                int j = 0;
                for (int k = 0, numTokens = 0; i < oText.size(); i++) {
                    Sentence sentenceI = oText.get(i);
                    for (Word w : sentenceI) {
                        s.add(w);
                        numTokens++;
                        if (numTokens % 100 == 0) {
                            Text t = new Text(s.toString());
                            vocRelatedStuff(t.toString(), output);
                            wordRelatedStuff(t.getSentences(), model, output);
                            s = new Sentence();
                        }
                    }
                }

            }
        }

    }

}
