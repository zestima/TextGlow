/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projlib;

import hultig.sumo.OpenNLPKit;
import hultig.sumo.Sentence;
import hultig.sumo.Text;
import hultig.sumo.Word;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

/**
 *
 * @author zsdaking
 */
public class ProjLib {

    public ProjLib() {

    }

    public Text getTextFromFile(String path) throws IOException {
        Text oText = new Text();
        if (path.contains(".txt")) {
            oText.readFile(path).toLowerCase();
        } else if (path.contains(".pdf")) {
            try {
                PDDocument doc = PDDocument.load(new File(path));
                PDFTextStripper pdfStripper = new PDFTextStripper();
                oText.add(pdfStripper.getText(doc).toLowerCase());
                doc.close();
            } catch (Exception e) {

            }
        } else if (path.contains(".doc")) {
            if (path.contains(".docx")) {
                XWPFDocument docx = new XWPFDocument(new FileInputStream(path));
                XWPFWordExtractor we = new XWPFWordExtractor(docx);
                oText.add(we.getText().toLowerCase());
            } else {
                HWPFDocument doc = new HWPFDocument(new FileInputStream(path));
                WordExtractor we = new WordExtractor(doc);
                oText.add(we.getText().toLowerCase());
            }
        } else {
            return null;
        }
        return oText;
    }

    public void generalTTR(String text, Metrics Object) {
        String[] saWords = text.split(" "); // String Array
        HashMap<String, Integer> HMcounter = new HashMap<>();//Criar HashMap associando palavras a número de repetições
        int iVocCount = 0, iTotCount = 0; //Contadores Necessários
        for (int i = 0; i < saWords.length; i++) { //Percorrer todas as palavras
            if (HMcounter.containsKey(saWords[i])) { //Se estiverem no HashMap aumentar o seu número de repetições 1 vez
                HMcounter.put(saWords[i], HMcounter.get(saWords[i]) + 1);
            } else {//Se não estiverem, colocá-las lá
                HMcounter.put(saWords[i], 1);
                iVocCount++;//Número de palavras diferentes, ou seja do vocabulário
            }
            iTotCount++;//Número total de palavras
        }
        double fLexDiv = ((double) (iVocCount) / iTotCount);
        Object.setGttr(String.valueOf(fLexDiv));
        Object.setVoc(String.valueOf(iVocCount));
    }

    public void wordCaracterization(Text oT, OpenNLPKit model, Metrics Object) {
        int iAdjCounter = 0;
        int iVerbCounter = 0;
        int iNounCounter = 0;
        for (Sentence oSentence : oT) {
            oSentence = model.postag(oSentence);
            for (Word oWord : oSentence) {
                if (oWord.getTag().contains("JJ")) {
                    iAdjCounter++;
                } else if (oWord.getTag().contains("VB")) {
                    iVerbCounter++;
                } else if (oWord.getTag().contains("NN")) {
                    iNounCounter++;
                }
            }
        }
        Object.setAdj(String.valueOf(iAdjCounter));
        Object.setVerb(String.valueOf(iVerbCounter));
        Object.setNoun(String.valueOf(iNounCounter));
        Object.setAdjratio(String.valueOf(((double) (iAdjCounter) / (iNounCounter + iVerbCounter))));
    }

    public double ttrOfSomeWords(String text, int n) {
        String[] saWords = text.split(" "); // String Array
        Random r = new Random();
        List<String> words = new ArrayList<>();//Lista para as palavras
        for (int j = 0; j < 100; j++) { //Fazer 100 amostras
            for (int i = 0; i < n; i++) { //Fazer amostras de n palavras começando em 35
                //System.out.println(saWords[r.nextInt(saWords.length)]);
                words.add(saWords[r.nextInt(saWords.length)]);
            }
        }
        HashMap<String, Integer> HMcounter = new HashMap<>();//Criar HashMap associando palavras a número de repetições
        int iVocCount = 0, iTotCount = 0; //Contadores Necessários
        for (int i = 0; i < words.size(); i++) { //Percorrer todas as palavras
            if (HMcounter.containsKey(words.get(i))) { //Se estiverem no HashMap aumentar o seu número de repetições 1 vez
                HMcounter.put(words.get(i), HMcounter.get(words.get(i)) + 1);
            } else {//Se não estiverem, colocá-las lá
                HMcounter.put(words.get(i), 1);
                iVocCount++;//Número de palavras diferentes, ou seja do vocabulário
            }
            iTotCount++;//Número total de palavras
        }
        return ((double) (iVocCount) / iTotCount);
    }

    public void vocD(Text oText, Metrics Object) {
        double ttr = 0;
        for (int i = 35; i <= 50; i++) { //Correr [35,50] ou [35,50[
            ttr = ttr + ttrOfSomeWords(oText.toString(), i);
        }
        Object.setVocd(String.valueOf(ttr / 16));
    }

    public void mtLD(String text, Metrics Object) {
        int factors = 0;
        String[] saWords = text.split(" "); // String Array
        HashMap<String, Integer> HMcounter = new HashMap<>();//Criar HashMap associando palavras a número de repetições
        int iVocCount = 0, iTotCount = 0, iFullTotal = 0; //Contadores Necessários
        for (int i = 0; i < saWords.length; i++) { //Percorrer todas as palavras
            if (HMcounter.containsKey(saWords[i])) { //Se estiverem no HashMap aumentar o seu número de repetições 1 vez
                HMcounter.put(saWords[i], HMcounter.get(saWords[i]) + 1);
            } else {//Se não estiverem, colocá-las lá
                HMcounter.put(saWords[i], 1);
                iVocCount++;//Número de palavras diferentes, ou seja do vocabulário
            }
            iTotCount++;//Número total de palavras until reset
            iFullTotal++;//Número total de palavras
            double ttr = ((double) (iVocCount / iTotCount));
            if (ttr < 0.720) {
                factors = factors + 1;
                HMcounter.clear();
                iVocCount = 0;
                iTotCount = 0;
                ttr = 0;
            }

        }
        double mtld = (double) ((iFullTotal) / factors);
        Object.setMtld(String.valueOf(mtld));
    }

    public Metrics newMetrics() {
        return (new Metrics());
    }

    public static void main(String[] args) {
        //mtLD("of the people by the people for the people");
    }
}
