/*
 * Copyright 2017 Dariusz Lelek.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tracerfx.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Dariusz Lelek
 */
public class FileUtility {

    protected final static int[] excludedASCII = {0};

    public static int getFileLineNumber(File file) {
        LineNumberReader lnr = null;
        try (FileReader fileReader = file != null ? new FileReader(file) : new FileReader("")) {
            lnr = new LineNumberReader(fileReader);
            lnr.skip(Long.MAX_VALUE);
        } catch (IOException ex) {
            // TODO logger
            ex.printStackTrace();
        }
        return lnr != null ? lnr.getLineNumber() : -1;
    }

    public static List<String> getFileLines(File file) {
        List<String> content = new ArrayList<>();
        if (file != null && file.exists()) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"))) {
                for (String line = null; (line = br.readLine()) != null;) {
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

    static boolean validLine(String line){      
        if(line.isEmpty()){
            return false;
        }
        return !(line.length() == 1 && containsExcludedASCII(line));
    }
    
    static boolean containsExcludedASCII(String line){
        for(int i = 0; i < excludedASCII.length; i++){
            if((int) line.charAt(0) == excludedASCII[i]){
                return true;
            }
        }
        return false;
    }

}
