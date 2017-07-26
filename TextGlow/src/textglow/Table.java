/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package textglow;

/**
 *
 * @author zsdaking
 */
public class Table {
    private String name;
    public Table(String filename){
        name = filename;
    }
    public String getName(){
        return name;
    }
    public void setName(String filename) {
        name = filename;
    }
}
