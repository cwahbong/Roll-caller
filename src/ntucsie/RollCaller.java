package ntucsie;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.JOptionPane;

/**
 * Perform a roll call.
 * It will need a file set to read the name list, and to record the picked list.
 * 
 * @author  cw.ahbong
 * @see     RollCallFileSet
 */
public class RollCaller {
    private RollCallFileSet _fileset;
    private Map<String, Boolean> _pickedStatus;
    private List<Integer> _unpickNumberList;
    private List<String> _nameList;
    private Random _random;
    
    /**
     * Construct a roll caller by a file set.
     * @param fileset       The file set.
     */
    public RollCaller(RollCallFileSet fileset) {
        _fileset = fileset;
        _random = new Random();
        try {
            _nameList = fileset.readNameList();
            // read picked status from file
            try {
                _pickedStatus = fileset.readPickedList();
            }
            catch(FileNotFoundException e) {
                System.err.println("Create new file");
                _pickedStatus = fileset.createPickedStatus();
            }
            catch(FileCorruptedException e) {
                System.err.println("Re-create new file");
                _pickedStatus = fileset.createPickedStatus();
            }
            
            _unpickNumberList = new ArrayList<Integer>();
            for(int i=0; i<_pickedStatus.size(); ++i) {
                if(_pickedStatus.get(_nameList.get(i))==false) {
                    _unpickNumberList.add(i);
                }
            }
        }
        catch (FileNotFoundException e){
            JOptionPane.showMessageDialog(null,
                    "Cannot find the name list file \"student.txt\".",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "IO error occured.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
    
    /**
     * Getter.
     * @return    The number of students.
     */
    public int getTotalStudentNum() {
        return _nameList.size();
    }
    
    /**
     * Getter.
     * @return    The name list of students. Each element in this list is the student's name.
     */
    public List<String> getNameList() {
        return new ArrayList<String>(_nameList);
    }
    
    /**
     * Perform a roll call. It will not call the same student until all of the student is called.
     * @return      The lucky student's name.
     * @throws      IOException
     */
    public String rollCall()
    throws IOException {
        if(_unpickNumberList.size()==0) {
            for(int i=0; i<_nameList.size(); ++i) {
                _unpickNumberList.add(i);
                _pickedStatus.put(_nameList.get(i), false);
            }
        }
        int index = _unpickNumberList.get(_random.nextInt(_unpickNumberList.size()));
        _pickedStatus.put(_nameList.get(index), true);
        _unpickNumberList.remove(new Integer(index));
        _fileset.writePickedStatus(_pickedStatus);
        return _nameList.get(index);
    }
}
