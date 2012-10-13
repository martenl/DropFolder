/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dropfolder.builder;

import java.io.*;

/**
 *
 * @author Laptop
 */
public class HTMLFilePageBuilder extends PageBuilder{
    
    File file;
    
    public HTMLFilePageBuilder(File f){
        file = f;
        raw = true;
    }
    @Override
    public String createContent() {
        StringBuilder builder = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            while(line != null){
                builder.append(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (FileNotFoundException ex) {ex.printStackTrace();}
          catch(IOException ex)            {ex.printStackTrace();}
        return builder.toString();
    }
    
}
