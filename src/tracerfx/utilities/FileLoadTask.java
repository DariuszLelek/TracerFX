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
import tracerfx.component.FileContent;

/**
 *
 * @author Dariusz Lelek
 */
public class FileLoadTask extends Thread{
    private final FileContent fileContent;
    private boolean running;
    private final int EXCLUDED_ASCII[] = {0};

    public FileLoadTask(FileContent fileContent) {
        this.fileContent = fileContent;
        this.running = true;
    }
    
    public void stopTask(){
        running = false;
    }
    
    @Override
    public void run() {
        try {
            List<String> fileLines = getFileLines(fileContent);
            setFileLinesToContent(fileContent, fileLines);
        } catch (InterruptedException ex) {
            // TODO
            ex.printStackTrace();
        }
    }

    
    private void setFileLinesToContent(FileContent fileContent, List<String> fileLines) {
        if (running) {
            Platform.runLater(() -> {
                fileContent.processFileLoadEnd(fileLines);
            });
        }
    }

    private int getFileLineNumber(File file) {
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

    private List<String> getFileLines(FileContent fileContent) throws InterruptedException {
        List<String> content = new ArrayList<>();
        long fileLinesNum = getFileLineNumber(fileContent.getFile());
        long updateEveryLine = fileLinesNum/100 > 0 ? fileLinesNum/20 : 1;
        final DoubleProperty progressProeperty = fileContent.getFileLoadingProgressProperty();
        long currentLine = 0;
        
        if (fileContent.getFile() != null && fileContent.getFile().exists()) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileContent.getFile()), "utf-8"))) {
                for (String line = null; running && (line = br.readLine()) != null;) {
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
    
    private void updateProgressProperty(final DoubleProperty progressProeperty, final double value) {
        Platform.runLater(() -> {progressProeperty.set(value);});
    }

    private boolean validLine(String line) {
        return line.isEmpty() ? false : !(line.length() == 1 && containsExcludedASCII(line));
    }

    private boolean containsExcludedASCII(String line) {
        for (int i = 0; i < EXCLUDED_ASCII.length; i++) {
            if ((int) line.charAt(0) == EXCLUDED_ASCII[i]) {
                return true;
            }
        }
        return false;
    }
}
