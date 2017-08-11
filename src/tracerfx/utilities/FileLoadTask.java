/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tracerfx.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import tracerfx.control.FileContent.FileContent;
import tracerfx.utilities.Strings;

/**
 *
 * @author Dariusz Lelek
 */
public class FileLoadTask extends Thread{
    private final FileContent fileContent;
    private final int threadNumber;
    private boolean running = true;
    private static int EXCLUDED_ASCII[] = {0};

    public FileLoadTask(FileContent fileContent, int threadNumber) {
        this.fileContent = fileContent;
        this.threadNumber = threadNumber;
        this.running = true;
    }
    
    
    @Override
    public void run() {
        try {
            List<String> fileLines = getFileLines(fileContent);
            setFileLinesToContent(fileContent, fileLines);
        } catch (InterruptedException ex) {

            
        }
    }

    
    private static void setFileLinesToContent(FileContent fileContent, List<String> fileLines) {
        Platform.runLater(() -> {fileContent.processFileLoadEnd(fileLines);});
    }

    private static int getFileLineNumber(File file) {
        LineNumberReader lnr = null;
        try (FileReader fileReader = file != null ? new FileReader(file) : new FileReader(Strings.EMPTY.toString())) {
            lnr = new LineNumberReader(fileReader);
            lnr.skip(Long.MAX_VALUE);
        } catch (IOException ex) {
            // TODO logger
            ex.printStackTrace();
        }
        return lnr != null ? lnr.getLineNumber() : -1;
    }

    private static List<String> getFileLines(FileContent fileContent) throws InterruptedException {
        List<String> content = new ArrayList<>();
        long fileLinesNum = getFileLineNumber(fileContent.getFile());
        long updateEveryLine = fileLinesNum/100 > 0 ? fileLinesNum/20 : 1;
        final DoubleProperty progressProeperty = fileContent.getFileLoadingProgressProperty();
        long currentLine = 0;
        
        if (fileContent.getFile() != null && fileContent.getFile().exists()) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileContent.getFile()), "utf-8"))) {
                for (String line = null; (line = br.readLine()) != null;) {
                    if(++currentLine % updateEveryLine == 0){
                        updateProgressProperty(progressProeperty, (double)currentLine/fileLinesNum);
                    }
                    if (validLine(line)) {
                        content.add(line);
                    }
                }
            } catch (IOException ex) {
                // TODO
                ex.printStackTrace();
            }
        }
        return content;
    }
    
    private static void updateProgressProperty(final DoubleProperty progressProeperty, final double value) {
        Platform.runLater(() -> {progressProeperty.set(value);});
    }

    static boolean validLine(String line) {
        if (line.isEmpty()) {
            return false;
        }
        return !(line.length() == 1 && containsExcludedASCII(line));
    }

    static boolean containsExcludedASCII(String line) {
        for (int i = 0; i < EXCLUDED_ASCII.length; i++) {
            if ((int) line.charAt(0) == EXCLUDED_ASCII[i]) {
                return true;
            }
        }
        return false;
    }
}
