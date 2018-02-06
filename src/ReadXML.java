import java.io.File;
import java.util.ArrayList;

public class ReadXML {
    File folder = new File("{FilePath}");
    File[] listOfFiles = folder.listFiles();
    int numberOfFiles = listOfFiles.length;

    ArrayList XMLfiles = new ArrayList<String>();

    private void FindFiles(){
        for(int i = 0; i < numberOfFiles; i++){
            String filename = listOfFiles[i].getName();
            if (filename.endsWith(".xml") || filename.endsWith(".XML")){
                System.out.println(filename);
                XMLfiles.add(filename);
            }
        }

    }
}
