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
public class TextFilePageBuilder extends PageBuilder{

    File file;
    
    public TextFilePageBuilder(File f){
        file = f;
        title = f.getName();
    }
    @Override
    public String createContent() {
        StringBuilder builder = new StringBuilder();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            while(line != null){
                line = line.replaceAll(" ", "&nbsp;");
                builder.append(line).append("<br>");
                line = reader.readLine();
            }
            reader.close();
        } catch (FileNotFoundException ex) {ex.printStackTrace();}
          catch(IOException ex)            {ex.printStackTrace();}
          
        return builder.toString();
    }
    
}
