/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dropfolder.server.http;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Laptop
 */
public class Response {
    
    final int status;
    final String statusMsg;
    Map<String,String> headers;
    String body;
    
    public static Response create404(){
        Response response = new Response(404,"Not Found");
        response.setHeader("Date", "Mon, 23 May 2005 22:38:34 GMT");
        response.setHeader("Content-Type" ,"text/html; charset=UTF-8");
        response.setHeader("Server", "FileServer 0.1");
        response.setBody("<html><head><title>Not Found</title></head><body><h1>File could not be found</h1></body></html>");
        return response;
    }
    
    public static Response create200(){
        Response response = new Response(200,"OK");
        response.setHeader("Date", "Mon, 23 May 2005 22:38:34 GMT");
        response.setHeader("Content-Type" ,"text/html; charset=UTF-8");
        response.setHeader("Server", "FileServer 0.1");
        return response;
    }
    
    public static Response createEndPage(){
        Response response = new Response(200,"OK");
        response.setHeader("Date", "Mon, 23 May 2005 22:38:34 GMT");
        response.setHeader("Content-Type" ,"text/html; charset=UTF-8");
        response.setHeader("Server", "FileServer 0.1");
        response.setBody("<html><head><title>Server shut down</title></head><body><h1>The server has been shutdown</h1></body></html>");
        return response;
    }
    
    public Response(int _status,String _statusMsg){
        status = _status;
        statusMsg = _statusMsg;
        headers = new HashMap<String,String>();
        
    }
    
    public void setHeader(String name,String value){
        headers.put(name, value);
    }
    
    public void setBody(Object data){
        if( data instanceof String){
            body = (String) data;
        }else{
            body = data.toString();
        }
        headers.put("Content-Length", String.valueOf(body.length()));
    }
    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append("HTTP/1.1 ").append(status).append(" ").append(statusMsg).append("\n");
        for(String name : headers.keySet()){
            builder.append(name).append(": ").append(headers.get(name)).append("\n");
        }
        builder.append("\n");
        builder.append(body);
        return builder.toString();
    }
}
