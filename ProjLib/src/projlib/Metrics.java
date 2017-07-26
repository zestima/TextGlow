/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projlib;

/**
 *
 * @author zsdaking
 */
public class Metrics {

    private String name;
    private String gttr;
    private String adj;
    private String verb;
    private String voc;
    private String noun;
    private String adjratio;
    private String vocd;
    private String mtld;

    public String getMtld() {
        return mtld;
    }

    public void setMtld(String mtld) {
        this.mtld = mtld;
    }

    public Metrics(String name, String gttr, String adj, String verb, String voc,String noun,String adjratio,String vocd) {
        this.name = name;
        this.gttr = gttr;
        this.adj = adj;
        this.verb = verb;
        this.voc = voc;
        this.noun = noun;
        this.adjratio = adjratio;
    }

    public void setVocd(String vocd) {
        this.vocd = vocd;
    }

    public String getVocd() {
        return vocd;
    }

    public String getAdjratio() {
        return adjratio;
    }

    public void setAdjratio(String adjratio) {
        this.adjratio = adjratio;
    }

    public void setNoun(String noun) {
        this.noun = noun;
    }

    public String getNoun() {
        return noun;
    }

    public String getVoc() {
        return voc;
    }

    public void setVoc(String voc) {
        this.voc = voc;
    }

    public void setGttr(String gttr) {
        this.gttr = gttr;
    }

    public void setAdj(String adj) {
        this.adj = adj;
    }

    public String getGttr() {
        return gttr;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdj() {
        return adj;
    }

    public Metrics() {
    }

    public void setVerb(String verb) {
        this.verb = verb;
    }

    public String getVerb() {
        return verb;
    }

    public String getName() {
        return name;
    }
}
