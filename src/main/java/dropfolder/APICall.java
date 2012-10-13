/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dropfolder;

import dropfolder.server.http.Request;
import dropfolder.server.http.Response;
import java.io.File;

/**
 *
 * @author Laptop
 */
public class APICall {
    
    static private String pathOffset;
    
    public static void setPathOffset(String _pathOffset){
        pathOffset = _pathOffset;
    }
    
    public static Response makeAPICall(Request request){
        Response r = null;
        int endIndex = request.getResource().indexOf("/",5);
        String path = request.getResource().substring(endIndex);
        String action = request.getResource().substring(5, endIndex);
        if(action.equals("delete")) {
                System.out.println("______"+path);
                if(delete(path)){
                    r = Response.create200();                  
                }
        } else if(action.equals("createDirectory")) {
                if(createDirectory(path,request.getQueryValue("newName"))){
                    r = Response.create200();                  
                }
        } else if(action.equals("rename")){
            if(rename(path,request.getQueryValue("newName"))){
                 r = Response.create200();
            }else{
                    r = Response.create404();
                }
        }else{
            r = Response.create404();
        }
        return r;
    }
    private static boolean delete(String path){
        File file = new File(pathOffset+path);
        boolean deleted = false;
        if(file.exists()){
             deleted = file.delete();
        }
        return deleted;
    }
    
    private static boolean createDirectory(String path,String name){
        File directory = new File(pathOffset+path+File.separator+name);
        boolean created = false;
        if(!directory.exists()){
             created = directory.mkdir();
        }
        return created;
    }
    
    private static boolean rename(String path,String newName){
        File file = new File(pathOffset+path);
        System.out.println("path: "+path.substring(0, path.lastIndexOf("/")));
        System.out.println("newNameParam: "+newName);
        newName = pathOffset+path.substring(0, path.lastIndexOf("/"))+File.separator+newName;
        System.out.println("newName: "+newName);
        System.out.println("oldName: "+path);
        boolean result = false;
        if(file.exists()){
            result = file.renameTo(new File(newName));
            System.out.println("XXX "+ result);
        }
        return result;
    }
    
}
