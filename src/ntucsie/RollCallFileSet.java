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
import java.util.Map;
import java.util.TreeMap;

/**
 * A file set needed by a roll call, including a file recording the name list,
 * and a file recording the picked status.
 * @author cw.ahbong
 */
public class RollCallFileSet {
    private File _nameListFile;
    private File _pickedStatusFile;
    
    public RollCallFileSet(File nameListFile) {
        this(nameListFile, new File(nameListFile.getName()+".picked"));
    }
    
    public RollCallFileSet(File nameListFile, File pickedListFile) {
        _nameListFile = nameListFile;
        _pickedStatusFile = pickedListFile;
    }
    
    public Map<String, Boolean> createPickedStatus()
    throws IOException {
        Map<String, Boolean> ps = new TreeMap<String, Boolean>();
        writePickedStatus(ps);
        return ps;
    }
    
    public List<String> readNameList()
    throws IOException {
        List<String> nameList = new ArrayList<String>();
        BufferedReader br = new BufferedReader(new InputStreamReader(
                        new FileInputStream(_nameListFile), "big5"));
        String line;
        while((line=br.readLine())!=null) {
            line = line.trim();
            if(!line.equals("")) {
                nameList.add(line);
            }
        }
        br.close();
        return nameList;
    }
    
    public Map<String, Boolean> readPickedList()
    throws IOException {
        Map<String, Boolean> pickedStatusMap = new TreeMap<String, Boolean>();
        BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(_pickedStatusFile), "big5"));
        String line;
        while((line=br.readLine())!=null) {
            String data[] = line.split("\t");
            if(data.length!=2)
            {
                throw new FileCorruptedException(_pickedStatusFile);
            }
            switch(Integer.parseInt(data[1])) {
                case 0:
                    pickedStatusMap.put(data[0], false);
                    break;
                case 1:
                    pickedStatusMap.put(data[0], true);
                    break;
                default:
                    throw new FileCorruptedException(_pickedStatusFile);
            }
        }
        br.close();
        return pickedStatusMap;
    }
    
    public void writePickedStatus(Map<String, Boolean> pickedStatus)
    throws IOException{
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(_pickedStatusFile, false),"big5"));
        for(String name: pickedStatus.keySet()) {
            if(pickedStatus.get(name)) {
                pw.println(name + "\t1");
            }
            else {
                pw.println(name + "\t0");
            }
        }
        pw.close();
    }
    
}
