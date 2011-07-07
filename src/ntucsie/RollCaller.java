package ntucsie;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;

/**
 * Perform a roll call.
 * It will need a file set to read the name list, and to record the picked list.
 * 
 * @author cw.ahbong
 *
 */
public class RollCaller {
	private RollCallFileSet _fileset;
	private List<Boolean> _pickedStatusList;
	private List<Integer> _unpickNumberList;
	private List<String> _nameList;
	private Random _random;
	
	/**
	 * 
	 * @param fileset
	 */
	public RollCaller(RollCallFileSet fileset) {
		_fileset = fileset;
		_random = new Random();
		try {
			_nameList = fileset.readNameList();
			_pickedStatusList = new ArrayList<Boolean>();
			for(int i=0; i<_nameList.size(); ++i) {
				_pickedStatusList.add(false);
			}
			// read picked status from file
			try {
				_pickedStatusList = fileset.readPickedList();
			}
			catch(FileNotFoundException e) {
				_pickedStatusList = fileset.createPickedList();
			}
			
			_unpickNumberList = new ArrayList<Integer>();
			for(int i=0; i<_pickedStatusList.size(); ++i) {
				if(_pickedStatusList.get(i)==false) {
					_unpickNumberList.add(i);
				}
			}
			for(String str: _nameList)
				System.err.println(str);
		}
		catch (FileNotFoundException e){
			JOptionPane.showMessageDialog(null,
					"Cannot find the name list file.",
					"Error",
					JOptionPane.ERROR_MESSAGE);
		}
		catch (IOException e) {
			JOptionPane.showMessageDialog(null,
					"IO error occured.",
					"Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * @return	the number of students.
	 */
	public int getTotalStudentNum() {
		return _nameList.size();
	}
	
	/**
	 * @return	the name list of students.
	 */
	public List<String> getNameList() {
		return new ArrayList<String>(_nameList);
	}
	
	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public String rollCall()
	throws IOException {
		if(_unpickNumberList.size()==0) {
			for(int i=0; i<_nameList.size(); ++i) {
				_unpickNumberList.add(i);
				_pickedStatusList.set(i, false);
			}
		}
		int index = _unpickNumberList.get(_random.nextInt(_unpickNumberList.size()));
		_pickedStatusList.set(index, true);
		_unpickNumberList.remove(new Integer(index));
		_fileset.writePickedList(_pickedStatusList);
		return _nameList.get(index);
	}
}
