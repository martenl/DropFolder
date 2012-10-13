/*
 * @author Marten Losansky
 * @version 0.1
 */
package dropfolder.builder;

import java.io.File;
import java.io.PrintWriter;

/**
 *
 * @author Laptop
 */
public abstract class PageBuilder {
    
    protected String title;
    protected boolean raw = false;
    protected String contentType = "text/html; charset=UTF-8";;
    
    
    /*
     * 
     * @params 
     * 
     * @return
     */
    public static PageBuilder create(File f,String pathOffset){
        PageBuilder result = null;
        if(f.exists()){
          if(f.isDirectory()){
              result = new DirectoryPageBuilder(f,pathOffset);
          }else{
              String fileName = f.getName();
              String fileExtension = fileName.substring(fileName.lastIndexOf('.')+1);
              if(fileExtension.equals("txt")){
                  result = new TextFilePageBuilder(f);
              }else if(fileExtension.equals("html") || fileExtension.equals("htm")){
                  result = new HTMLFilePageBuilder(f);
              }else if(fileExtension.equals("pdf")){
                  result = new PDFFilePageBuilder(f);
              }else if(fileExtension.equals("jpg") || fileExtension.equals("jpeg")){
                  result = new ImageFilePageBuilder(f,fileExtension);
                  
              }
              
          }
        }
        
        return result;
    }
    
    
    /*
     * 
     * @return 
     */
    public String createPage(){
        StringBuilder builder = new StringBuilder(); 
        if(!raw) 
            builder.append("<html>\n<head>\n<title>")
                    .append(title)
                    .append("</title>\n")
                    .append("<script type=\"text/javascript\" src=\"http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js\"></script>\n")
                    .append("<script type=\"text/javascript\" >\n\n")
                    .append("function signal(id){\n")
                    .append("  alert(id);\n")
                    .append("}\n")
                    .append("function init(id){\n")
                    .append("  \n")
                    .append("}\n")
                    .append("function sendApiCall(id,type){\n")
                    .append("  var url = id;\n")
                    .append("  if(url ==\"\") url=\"/\";\n")
                    .append("  if(type == 1){\n")
                    .append("    url = \"/api/delete\"+url;\n")
                    .append("  } else if(type == 2){\n")
                    .append("    url = \"/api/rename\"+url;\n")
                    .append("    var newName = prompt(\"Bitte neuen Namen für die Datei / den Ordner angeben:\");")
                    .append("    if(newName == \"\" || newName == null ){")
                    .append("      alert(\"Name kann kein leerer String sein.\");")
                    .append("      return;} \n")
                    .append("    url = url+\"?newName=\"+newName; \n")
                    .append("  }else if(type == 3){\n")
                    .append("    url = \"/api/createDirectory\"+url;\n")
                    .append("    var newName = prompt(\"Bitte Namen für den neuen Ordner angeben:\");\n")
                    .append("    if(newName == \"\" || newName == null ){ alert(\"Name kann kein leerer String sein.\");return;} \n")
                    .append("    url = url+\"?newName=\"+newName;\n")
                    .append("  }\n")
                    .append("  $.get(url, function(data) {\n")
                    .append("    location.reload();\n")
                    .append("});\n")
                    .append("}\n")
                    .append("</script>\n")
                    .append("<style type=\"text/css\">\n")
                    .append("a { color:blue;text-decoration: none;font-family:'Arial';color:black;font-size:small}\n")
                    .append("a:hover { text-decoration: underline}\n")
                    .append(".file { height: 5%}\n") //
                    .append("hr { opacity : 0.2}")
                    .append(".fileFocus { background-color: #B0E0E6}\n")
                    .append("#fileList { position:absolute;width:50%;left:30%;top:20%;z-index:1} \n")
                    .append("#fileListHead { position:fixed;width:50%;left:30%;top:15%;height:5%;z-index:2;background-color:#00CCFF;} \n")
                    .append("#menuBackground { position:fixed;width:50%;left:30%;top:0%;height:15%;z-index:2; background-color:#FFF;} \n")
                    .append("#menu { position:absolute;width:20%;right:0%;bottom:0%; } \n")
                    .append(".link {position: relative; left:2%;bottom:20%;}\n")
                    .append(".delete {position: absolute;right:0%}\n")
                    .append(".rename {position: absolute;right:7.5%}\n")
                    .append(".createDirectory {position: absolute;right:0%}\n")
                    .append(".deleted( display: none;}\n")
                    .append("</style>\n")
                    .append("</head>\n")
                    .append("<body onload=\"init();\">\n");
        builder.append(createContent());
        if(!raw) 
            builder.append("\n</body>\n</html>\n");
        return builder.toString();
        
    }
    
    
    public abstract String createContent();
    
    /*
     * 
     * 
     * 
     * @return Returns the Content type of the Page
     */
    public String getContentType(){
        return contentType;
    }
}


