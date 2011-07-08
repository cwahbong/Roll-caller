package ntucsie;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author cw.ahbong
 */
public class RollCallFileSet {
    private File _nameListFile;
    private File _pickedListFile;
    
    public RollCallFileSet(File nameList) {
        _nameListFile = nameList;
        _pickedListFile = new File(_nameListFile.getName()+".picked");
    }
    
    public RollCallFileSet(File nameListFile, File pickedListFile) {
        _nameListFile = nameListFile;
        _pickedListFile = pickedListFile;
    }
    
    public List<Boolean> createPickedList()
    throws IOException {
        int size = readNameList().size();
        List<Boolean> lb = new ArrayList<Boolean>(size);
        for(int i=0; i<size; ++i)
            lb.add(false);
        writePickedList(lb);
        return lb;
    }
    
    public List<String> readNameList()
    throws IOException {
        List<String> nameList = new ArrayList<String>();
        BufferedReader br = new BufferedReader(new InputStreamReader(
                        new FileInputStream(_nameListFile), "big5"));
        String line;
        while((line=br.readLine())!=null) {
            nameList.add(line);
        }
        br.close();
        return nameList;
    }
    
    public List<Boolean> readPickedList()
    throws IOException {
        List<Boolean> pickedStatusList = new ArrayList<Boolean>();
        BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(_pickedListFile), "big5"));
        String line;
        while((line=br.readLine())!=null) {
            switch(Integer.parseInt(line)) {
                case 0:
                    pickedStatusList.add(false);
                    break;
                case 1:
                    pickedStatusList.add(true);
                    break;
                default:
                    throw new IOException("FileCorrupted"); // FileCorruptException
            }
        }
        br.close();
        return pickedStatusList;
    }
    
    public void writePickedList(List<Boolean> pickedList)
    throws IOException{
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(_pickedListFile, false),"big5"));
        for(Boolean b: pickedList) {
            if(b) {
                pw.println("1");
            }
            else {
                pw.println("0");
            }
        }
        pw.close();
    }
    
}
