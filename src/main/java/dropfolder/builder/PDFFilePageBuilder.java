/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dropfolder.builder;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Laptop
 */
public class PDFFilePageBuilder extends PageBuilder{
    
    File file;
    
    public PDFFilePageBuilder(File f){
        file = f;
        raw = true;
        contentType = "application/pdf";
    }
    
    @Override
    public String createContent() {
        StringBuilder builder = new StringBuilder();
        List<byte[]> data = new ArrayList<byte[]>();
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
    
    public byte[] createData(){
        byte[] data = new byte[(int)file.length()];
        try {
            FileInputStream fis = new FileInputStream(file);
            fis.read(data);
            fis.close();
        } catch (IOException ex) { ex.printStackTrace();}
        return data;
    }
    
}

