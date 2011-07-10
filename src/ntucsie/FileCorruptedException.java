package ntucsie;

import java.io.File;
import java.io.IOException;

/**
 * It will be thrown when it thinks the file is corrupted.
 * @author cw.ahbong
 */
public class FileCorruptedException extends IOException {
    private static final long serialVersionUID = 1L;
    private File _file;
    
    /** 
     * @param file      The corrupted file.
     */
    public FileCorruptedException(File file) {
        _file = file;
    }
    
    /**
     * Getter.
     * @return          The corrupted file.
     */
    public File getFile()
    {
        return _file;
    }
}
