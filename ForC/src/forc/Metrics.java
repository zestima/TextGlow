/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forc;

/**
 *
 * @author zsdaking
 */
public class Metrics {

    private int ttr, adj, anw, asw, amw, alw, avoc, aws;

    public Metrics() {
        ttr = 0;
        adj = 0;
        anw = 0;
        asw = 0;
        amw = 0;
        alw = 0;
        avoc = 0;
        aws = 0;
    }

    public int getTtr() {
        return ttr;
    }

    public void setTtr(int ttr) {
        this.ttr = ttr;
    }

    public int getAdj() {
        return adj;
    }

    public void setAdj(int adj) {
        this.adj = adj;
    }

    public int getAnw() {
        return anw;
    }

    public void setAnw(int anw) {
        this.anw = anw;
    }

    public int getAsw() {
        return asw;
    }

    public void setAsw(int asw) {
        this.asw = asw;
    }

    public int getAmw() {
        return amw;
    }

    public void setAmw(int amw) {
        this.amw = amw;
    }

    public int getAlw() {
        return alw;
    }

    public void setAlw(int alw) {
        this.alw = alw;
    }

    public int getAvoc() {
        return avoc;
    }

    public void setAvoc(int avoc) {
        this.avoc = avoc;
    }

    public int getAws() {
        return aws;
    }

    public void setAws(int aws) {
        this.aws = aws;
    }
}
