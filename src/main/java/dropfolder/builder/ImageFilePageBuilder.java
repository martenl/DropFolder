/*
 * 
 * @author Marten Losansky
 */
package dropfolder.builder;

import java.io.*;

public class ImageFilePageBuilder extends PageBuilder{
    
    InputStream fis;
    
    public ImageFilePageBuilder(File f,String type){
       try {
          fis = new FileInputStream(f);
        } catch (FileNotFoundException ex) {
               ex.printStackTrace();
        }
        raw = true;
        contentType = "image/" + type;
        
    }
    
    public ImageFilePageBuilder(InputStream i,String type){
        fis = i;
        raw = true;
        contentType = "image/" + type;
        
    }
    
    
    @Override
    public String createContent() {
        return new String(createData());
    }
    
    public byte[] createData(){
        byte[] data = null;
        try {
            data = new byte[fis.available()];
            InputStreamReader reader = new InputStreamReader(fis);
            fis.read(data);
            fis.close();
        } catch (IOException ex) { ex.printStackTrace();}
        return data;
    }
    
}
