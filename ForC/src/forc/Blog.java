package forc;

import hultig.io.FileIN;
import hultig.sumo.Text;
import hultig.util.Toolkit;

/**
 * <p>
 * The Blog class ...
 * </p>
 *
 * <p>
 * <b>University of Beira Interior</b> (UBI)<br />
 * Centre For Human Language Technology and Bioinformatics (HULTIG)
 * </p>
 *
 * @date 0:37:35 24/fev/2015
 * @author J. P. Cordeiro
 */
public class Blog 
{
    private final String cod;
    private final char   sex;
    private final int    age;
    private final String subject;
    private final String sign;
    private       Text   text;

    public Blog(String cod, char sex, int age, String subject, String sign) {
        this.cod = cod;
        this.sex = sex;
        this.age = age;
        this.sign = sign;
        this.subject = subject;
        this.text= null;
    }
    
    public static Blog getInstance(FileIN f) {
        if ( f == null || !f.exists() || !f.isFile() ) return null;
        Blog b= getInstance(f.getName());
        if ( b == null ) return null;
        
        b.loadTextFromFile(f);
        return b;
    }
            
    public static Blog getInstance(String filename) {
        String[] vs= filename.split("[.]");
        if ( vs.length < 5 ) return null;
        try {
            Integer age = Integer.parseInt(vs[2]);
            return new Blog(vs[0], vs[1].charAt(0), age, vs[3], vs[4]);
        } catch (NumberFormatException numberFormatException) {
            return null;
        }
    }
    
    private void loadTextFromFile(FileIN f) {
        f.open();
        text= new Text();
        boolean insidePostText= false;
        for (;;) {
            String line= f.read();
            if ( line == null ) break;
            line= line.trim();
            if ( line.length() < 1 )  continue;
            if ( line.startsWith("<date>") || line.contains("Blog>") )  continue;
            if ( line.equals("<post>") ) {
                insidePostText= true;
                continue;
            }
            if ( line.equals("</post>") ) {
                insidePostText= false;
                continue;
            }
            if ( insidePostText ) {
                Text t= new Text(textFilter(line));
                text.add(t);
            }
        }
    }
    
    private static String textFilter(String s) {
        s= s.replaceAll("urlLink", "");
        s= s.replaceAll("[.]+", ". ");
        s= s.replaceAll("[,]+", ", ");
        s= s.replaceAll("[!]+", "! ");
        s= s.replaceAll("[?]+", "? ");
        return s;
    }
    
    public void setText(String s) {
        text= new Text(s);
    }
    
    public Text getText() {
        return text;
    }

    public String getCod() {
        return cod;
    }

    public char getSex() {
        return sex;
    }

    public int getAge() {
        return age;
    }

    public String getSubject() {
        return subject;
    }

    public String getSign() {
        return sign;
    }
    
    public void addText(String s) {
        if ( text == null )  {
            setText(s);
            return;
        }
        text.add(new Text(s));
    }
    
    public void printHeader(int size) {
        String sexs= sex=='m'?"Male":"Female";
        System.out.println(Toolkit.sline('-', size));        
        System.out.printf( " ID:%9s        %7s   %3d     %10s     %s\n", cod, sexs, age, subject, sign);
        System.out.println(Toolkit.sline('-', size));
    }
    
    public void print(int size) {
        printHeader(size);
        if ( text == null ) return;
        text.printJustified(size);
        System.out.println(Toolkit.sline('-', size));
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        String path= "/Volumes/MacSSX/blogs/";
        Blog b= getInstance(new FileIN(path+"800873.male.27.Education.Aquarius.xml"));
        if ( b == null ) return;
        b.print(70);
    }
}
