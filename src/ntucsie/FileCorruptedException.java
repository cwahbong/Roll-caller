package ntucsie;

import java.io.File;
import java.io.IOException;

public class FileCorruptedException extends IOException {
    public File _file;
    public FileCorruptedException() {
    }
}
