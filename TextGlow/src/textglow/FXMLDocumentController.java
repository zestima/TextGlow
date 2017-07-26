/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
Ler os xmls, testar os blogs, mtld, 

software para calcular o ttr e adjratio nas frases.

 */
package textglow;

import hultig.io.HULTIGVars;
import hultig.sumo.OpenNLPKit;
import hultig.sumo.Text;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import projlib.Metrics;
import projlib.ProjLib;

/**
 *
 * @author zsdaking
 */
public class FXMLDocumentController implements Initializable {

    //Vars
    @FXML
    private TableView<Table> FilesTable;
    @FXML
    private TableColumn<Table, String> column;
    @FXML
    private TableView<Metrics> metrics;
    
    OpenNLPKit model = new OpenNLPKit("/home/zsdaking/Drive/Jose Santos/Codigo/opennlp-tools-1.5.0" + "/models/english/");
    ObservableList<Metrics> MetricsList;
    ObservableList<Table> FileList;
    ProjLib main = new ProjLib();
    Metrics Object = main.newMetrics();
    boolean compareFlag = false;
    
    @FXML
    private void ChooseFile(ActionEvent event) { //tem threads
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open File");
        chooser.setInitialDirectory(new java.io.File("/home/zsdaking/Drive/Jose Santos/Textos"));
        File f = chooser.showOpenDialog(null);
        if (f != null) {
            Table t = new Table(f.getPath());
            Platform.runLater(() -> {
                try {
                    Text oText = main.getTextFromFile(f.getPath());
                    if (oText == null) {
                        return;
                    }
                    FileList.add(t);
                    Task GTTRTask = new Task() {
                        @Override
                        protected java.lang.Object call() throws Exception {
                            return null;
                        }
                        
                        @Override
                        public void run() {
                            main.generalTTR(oText.toString(), Object);
                            System.out.println("Got GTTR");
                        }
                    };
                    Thread GTTRThread = new Thread(GTTRTask);
                    Task WordCTask = new Task() {
                        @Override
                        protected java.lang.Object call() throws Exception {
                            return null;
                        }
                        
                        @Override
                        public void run() {
                            main.wordCaracterization(oText, model, Object);
                            System.out.println("Got WordCarac");
                        }
                    };
                    Thread WordCThread = new Thread(WordCTask);
                    Task VOCD = new Task() {
                        @Override
                        protected java.lang.Object call() throws Exception {
                            return null;
                        }
                        
                        @Override
                        public void run() {
                            main.vocD(oText, Object);
                            System.out.println("Got VOCD");
                        }
                    };
                    Thread VOCDThread = new Thread(VOCD);
                    Task MTLD = new Task() {
                        @Override
                        protected java.lang.Object call() throws Exception {
                            return null;
                        }
                        
                        @Override
                        public void run() {
                            main.mtLD(oText.toString(), Object);
                            System.out.println("Got MTLD");
                        }
                    };
                    Thread MTLDThread = new Thread(MTLD);
                    WordCThread.start();
                    VOCDThread.start();
                    GTTRThread.start();
                    MTLDThread.start();
                    GTTRThread.join();
                    WordCThread.join();
                    VOCDThread.join();
                    MTLDThread.join();
                    Object.setName(f.getName());
                    boolean check = false;
                    for (int i = 0; i < MetricsList.size(); i++) {
                        if (MetricsList.get(i).getName().equals(Object.getName())) {
                            MetricsList.set(i, Object);
                            check = true;
                        }
                    }
                    if (check == false) {
                        MetricsList.add(Object);
                    }
                    //compareActive();
                    Object = new Metrics();
                    
                } catch (Exception ex) {
                    
                }
            });
        }
    }
    
    @FXML
    private void ChooseFolder(ActionEvent event) {//Tem threads
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Choose Folder");
        chooser.setInitialDirectory(new java.io.File("/home/zsdaking/Drive/Jose Santos/Textos"));
        File dir = chooser.showDialog(null);
        if (dir != null) {
            File[] ListOfFiles = dir.listFiles();
            for (int i = 0; i < ListOfFiles.length; i++) {
                Table t = new Table(ListOfFiles[i].getPath());
                File f = ListOfFiles[i];
                Task task = new Task() {
                    @Override
                    protected java.lang.Object call() throws Exception {
                        Platform.runLater(() -> {
                            try {
                                Text oText = main.getTextFromFile(f.getPath());
                                if (oText == null) {
                                    return;
                                }
                                FileList.add(t);
                                Task GTTRTask = new Task() {
                                    @Override
                                    protected java.lang.Object call() throws Exception {
                                        return null;
                                    }
                                    
                                    @Override
                                    public void run() {
                                        main.generalTTR(oText.toString(), Object);
                                        System.out.println("Got GTTR");
                                    }
                                };
                                Thread GTTRThread = new Thread(GTTRTask);
                                Task WordCTask = new Task() {
                                    @Override
                                    protected java.lang.Object call() throws Exception {
                                        return null;
                                    }
                                    
                                    @Override
                                    public void run() {
                                        main.wordCaracterization(oText, model, Object);
                                        System.out.println("Got WordCarac");
                                    }
                                };
                                Thread WordCThread = new Thread(WordCTask);
                                Task VOCD = new Task() {
                                    @Override
                                    protected java.lang.Object call() throws Exception {
                                        return null;
                                    }
                                    
                                    @Override
                                    public void run() {
                                        main.vocD(oText, Object);
                                        System.out.println("Got VOCD");
                                    }
                                };
                                Thread VOCDThread = new Thread(VOCD);
                                Task MTLD = new Task() {
                                    @Override
                                    protected java.lang.Object call() throws Exception {
                                        return null;
                                    }
                                    
                                    @Override
                                    public void run() {
                                        main.mtLD(oText.toString(), Object);
                                        System.out.println("Got MTLD");
                                    }
                                };
                                Thread MTLDThread = new Thread(MTLD);
                                WordCThread.start();
                                VOCDThread.start();
                                GTTRThread.start();
                                MTLDThread.start();
                                GTTRThread.join();
                                WordCThread.join();
                                VOCDThread.join();
                                MTLDThread.join();
                                Object.setName(f.getName());
                                boolean check = false;
                                for (int j = 0; j < MetricsList.size(); j++) {
                                    if (MetricsList.get(j).getName().equals(Object.getName())) {
                                        MetricsList.set(j, Object);
                                        check = true;
                                    }
                                }
                                if (check == false) {
                                    MetricsList.add(Object);
                                }
                                Object = new Metrics();
                                
                            } catch (Exception ex) {
                                
                            }
                        });
                        return null;
                    }
                    
                };
                Thread thread = new Thread(task);
                thread.start();
            }
        }
        
    }
    
    @FXML
    private void ClearFiles(ActionEvent event) {
        FileList.clear();
    }
    
    @FXML
    private void ClearMetrics(ActionEvent event) {
        MetricsList.clear();
    }
    
    @FXML
    private void ExportCSV(ActionEvent event) throws FileNotFoundException, IOException {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Export to XLS file");
        dialog.setHeaderText("You are about to save all the data on the table!");
        dialog.setContentText("Please enter your file name:");
        Optional<String> result = dialog.showAndWait();
        FileOutputStream fileOut;
        if (!result.isPresent()) {
            return;
        }
        if (result.get().contains(".xls")) {
            fileOut = new FileOutputStream(result.get());
        } else {
            fileOut = new FileOutputStream(result.get() + ".xls");
        }
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet worksheet = workbook.createSheet("TextGlow Sheet");
        
        HSSFRow row = worksheet.createRow((short) 0);
        HSSFCell cell1 = row.createCell((short) 0);
        cell1.setCellValue("NAME");
        HSSFCell cell2 = row.createCell((short) 1);
        cell2.setCellValue("GTTR");
        HSSFCell cell3 = row.createCell((short) 2);
        cell3.setCellValue("VOCD");
        HSSFCell cell9 = row.createCell((short) 3);
        cell9.setCellValue("MTLD");
        HSSFCell cell4 = row.createCell((short) 4);
        cell4.setCellValue("ADJR");
        HSSFCell cell5 = row.createCell((short) 5);
        cell5.setCellValue("VOC");
        HSSFCell cell6 = row.createCell((short) 6);
        cell6.setCellValue("NOUN");
        HSSFCell cell7 = row.createCell((short) 7);
        cell7.setCellValue("ADJ");
        HSSFCell cell8 = row.createCell((short) 8);
        cell8.setCellValue("VERB");
        
        for (int i = 0; i < MetricsList.size(); i++) {
            row = worksheet.createRow((short) (i + 1));
            cell1 = row.createCell((short) 0);
            cell1.setCellValue(MetricsList.get(i).getName());
            cell2 = row.createCell((short) 1);
            cell2.setCellValue(Double.parseDouble(MetricsList.get(i).getGttr()));
            cell3 = row.createCell((short) 2);
            cell3.setCellValue(Double.parseDouble(MetricsList.get(i).getVocd()));
            cell9 = row.createCell((short) 3);
            cell9.setCellValue(Double.parseDouble(MetricsList.get(i).getMtld()));
            cell4 = row.createCell((short) 4);
            cell4.setCellValue(Double.parseDouble(MetricsList.get(i).getAdjratio()));
            cell5 = row.createCell((short) 5);
            cell5.setCellValue(Double.parseDouble(MetricsList.get(i).getVoc()));
            cell6 = row.createCell((short) 6);
            cell6.setCellValue(Double.parseDouble(MetricsList.get(i).getNoun()));
            cell7 = row.createCell((short) 7);
            cell7.setCellValue(Double.parseDouble(MetricsList.get(i).getAdj()));
            cell8 = row.createCell((short) 8);
            cell8.setCellValue(Double.parseDouble(MetricsList.get(i).getVerb()));
            
        }
        for (int i = 0; i < 8; i++) {
            worksheet.autoSizeColumn(i);
        }
        workbook.write(fileOut);
        fileOut.flush();
        fileOut.close();
        System.out.println("Table Exported to xls");
    }
    
    @FXML
    private void tableClick(MouseEvent event) throws IOException { //tem threads
        try {
            Path p = Paths.get(FilesTable.getSelectionModel().getSelectedItem().getName());
            Text oText = main.getTextFromFile(p.toString());
            Task GTTRTask = new Task() {
                @Override
                protected java.lang.Object call() throws Exception {
                    return null;
                }
                
                @Override
                public void run() {
                    main.generalTTR(oText.toString(), Object);
                    System.out.println("Got GTTR");
                }
            };
            Thread GTTRThread = new Thread(GTTRTask);
            Task WordCTask = new Task() {
                @Override
                protected java.lang.Object call() throws Exception {
                    return null;
                }
                
                @Override
                public void run() {
                    main.wordCaracterization(oText, model, Object);
                    System.out.println("Got WordCarac");
                }
            };
            Thread WordCThread = new Thread(WordCTask);
            Task VOCD = new Task() {
                @Override
                protected java.lang.Object call() throws Exception {
                    return null;
                }
                
                @Override
                public void run() {
                    main.vocD(oText, Object);
                    System.out.println("Got VOCD");
                }
            };
            Thread VOCDThread = new Thread(VOCD);
            Task MTLD = new Task() {
                @Override
                protected java.lang.Object call() throws Exception {
                    return null;
                }
                
                @Override
                public void run() {
                    main.mtLD(oText.toString(), Object);
                    System.out.println("Got MTLD");
                }
            };
            Thread MTLDThread = new Thread(MTLD);
            WordCThread.start();
            VOCDThread.start();
            GTTRThread.start();
            MTLDThread.start();
            GTTRThread.join();
            WordCThread.join();
            VOCDThread.join();
            MTLDThread.join();
            Object.setName(p.getFileName().toString());
            boolean check = false;
            for (int i = 0; i < MetricsList.size(); i++) {
                if (MetricsList.get(i).getName().equals(Object.getName())) {
                    MetricsList.set(i, Object);
                    check = true;
                }
            }
            if (check == false) {
                MetricsList.add(Object);
            }
            Object = new Metrics();
        } catch (Exception e) {
            
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        FilesTable.setEditable(true);
        FilesTable.getSelectionModel().setCellSelectionEnabled(true);
        column.setCellValueFactory(new PropertyValueFactory<>("name"));
        FileList = FXCollections.observableArrayList();
        FilesTable.setItems(FileList);
        
        metrics.setEditable(true);
        TableColumn<Metrics, String> NAME = new TableColumn("NAME");
        TableColumn<Metrics, String> GTTR = new TableColumn("GTTR");
        TableColumn<Metrics, String> VOCD = new TableColumn("VOCD");
        TableColumn<Metrics, String> MTLD = new TableColumn("MTLD");
        TableColumn<Metrics, String> ADJR = new TableColumn("ADJR");
        TableColumn<Metrics, String> VOC = new TableColumn("VOC");
        TableColumn<Metrics, String> NOUN = new TableColumn("NOUN");
        TableColumn<Metrics, String> ADJ = new TableColumn("ADJ");
        TableColumn<Metrics, String> VERB = new TableColumn("VERB");
        
        GTTR.setCellValueFactory(new PropertyValueFactory<>("gttr"));
        ADJ.setCellValueFactory(new PropertyValueFactory<>("adj"));
        NAME.setCellValueFactory(new PropertyValueFactory<>("name"));
        VERB.setCellValueFactory(new PropertyValueFactory<>("verb"));
        VOC.setCellValueFactory(new PropertyValueFactory<>("voc"));
        NOUN.setCellValueFactory(new PropertyValueFactory<>("noun"));
        ADJR.setCellValueFactory(new PropertyValueFactory<>("adjratio"));
        VOCD.setCellValueFactory(new PropertyValueFactory<>("vocd"));
        MTLD.setCellValueFactory(new PropertyValueFactory<>("mtld"));
        
        NAME.setPrefWidth(150);
        GTTR.setPrefWidth(200);
        ADJ.setPrefWidth(50);
        VERB.setPrefWidth(50);
        VOC.setPrefWidth(50);
        NOUN.setPrefWidth(50);
        ADJR.setPrefWidth(50);
        VOCD.setPrefWidth(50);
        MTLD.setPrefWidth(50);
        
        MetricsList = FXCollections.observableArrayList();
        metrics.setItems(MetricsList);
        metrics.getColumns().addAll(NAME, GTTR, VOCD, MTLD, ADJR, VOC, NOUN, ADJ, VERB);
    }
    
}
