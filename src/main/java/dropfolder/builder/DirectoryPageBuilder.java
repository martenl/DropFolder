/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dropfolder.builder;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Laptop
 */
public class DirectoryPageBuilder extends PageBuilder {
    
    List<String> directories;
    List<String> files;
    String pathOffset;
    public DirectoryPageBuilder(List<String> infiles){
        files = infiles;
    }
    
    public DirectoryPageBuilder(File directory,String _pathOffset){
        title = directory.getName();
        pathOffset =  _pathOffset.replace("\\", "/").replace(" ","%20");
        directories = new ArrayList<String>();
        files = new ArrayList<String>();
        for(File f : directory.listFiles()){
            if(f.isDirectory()){
                directories.add(f.getName());
            }else{
                files.add(f.getName());
            }
        }
        Collections.sort(directories);
        Collections.sort(files);
    }
    
    
    @Override
    public String createContent(){       
        String parentDirectory ="/";
        if(pathOffset.indexOf("/")!=-1 && pathOffset.indexOf("/")!=pathOffset.lastIndexOf("/")){
            parentDirectory = pathOffset.substring(0, pathOffset.lastIndexOf("/"));
        }
        StringBuilder builder = new StringBuilder();
        builder.append("<div id=\"fileListHead\"><b>").append(title).append("</b></div>\n");
        builder.append("<div id=\"fileList\">\n");
        for(String directory : directories){
            
            String id = pathOffset+"/"+directory;
            builder.append("<div class=\"file\" ><a href=\"")
                    .append(pathOffset)
                    .append("/")
                    .append(directory)
                    .append("\">")
                    .append("<img src=\"/images/folder.png\" />\n")
                    .append("</a>")
                    .append("<a href=\"")
                    .append(pathOffset)
                    .append("/")
                    .append(directory)
                    .append("\" class =\"link\">")
                    .append(directory)
                    .append("</a>\n")
                    .append("</a><a href=\"\" class = \"rename\" id=\"")
                    .append(pathOffset)
                    .append("/")
                    .append(directory)
                    .append("\" onclick=\"sendApiCall(this.id,2);return false;\"")
                    .append(">")
                    .append("Umbenennen")
                    .append("</a>\n")
                    .append("</a><a href=\"\" class = \"delete\" id=\"")
                    .append(id)
                    .append("\" onclick=\"sendApiCall(this.id,1);return false;\"")
                    .append(">")
                    .append("<img src=\"/images/delete.png\"  title=\"").append(directory).append(" löschen\"/>")
                    .append("</a></div>\n")
                    .append("<hr>\n");
        }
        for(String file : files){
            
            String id = pathOffset+"/"+file;
            builder.append("<div class=\"file\" >");
            if(file.endsWith("pdf")){
                builder.append("<img src=\"/images/pdf.png\" />");
            }else if(file.endsWith("jpg") || file.endsWith("jpeg")){
                builder.append("<img src=\"/images/image.png\" />");
            }else{
                builder.append("<img src=\"/images/txt.png\" />");
            }
            builder.append("<a href=\"")
                    .append(pathOffset)
                    .append("/")
                    .append(file)
                    .append("\"  class =\"link\">")
                    .append(file)
                    .append("</a>\n<a href=\"\" class = \"rename\" id=\"")
                    .append(pathOffset)
                    .append("/")
                    .append(file)
                    .append("\" onclick=\"sendApiCall(this.id,2);return false;\"")
                    .append(">")
                    .append("Umbenennen")
                    .append("</a>\n")
                    .append("</a><a href=\"\" class = \"delete\" id=\"")
                    .append(id)
                    .append("\" onclick=\"sendApiCall(this.id,1);return false;\"")
                    .append(">")
                    .append("<img src=\"/images/delete.png\"  title=\"").append(file).append(" löschen\"/>")
                    .append("</a></div>\n")
                    .append("<hr>\n");
        }
        builder.append("</div>\n");
        
        builder.append("<div id=\"menuBackground\">")
               .append("<div id=\"menu\">")
               .append("<a href=\"").append(parentDirectory).append("\"><img src=\"/images/parent.png\"  title=\"Zum Elternordner\"/></a>")
               .append("<a href=\"\" id=\"")
               .append(pathOffset)
               .append("\" onclick=\"sendApiCall(this.id,3);return false;\">")
               .append("<img src=\"/images/folder_new.png\"  title=\"Neuen Ordner anlegen\"/>")
               .append("</a>")
               .append(" <a href=\"/kill\"><img src=\"/images/shutdown.png\"  title=\"Server stoppen\"/></a></div></div>\n");
        
        return builder.toString();
    }
    
    
}
