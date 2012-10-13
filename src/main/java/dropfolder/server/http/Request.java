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
public class Request {
    private final String method;
    private final String resource;
    private Map<String,String> headers;
    private Map<String,String> queryValues;
    
    public Request(String _method,String _resource){
        method = _method;
        if(_resource.indexOf("?") != -1){
            resource = _resource.substring(0,_resource.indexOf("?"));
            parseQuery(_resource.substring(_resource.indexOf("?")+1));
        }else{
            resource = _resource;
        }
        headers = new HashMap<String,String>();
    }
    
    public String getResource(){
        return resource;
    }
    
    public String getMethod(){
        return method;
    }
    
    public void setHeader(String name,String value){
        headers.put(name,value);
    }
    
    public String getHeader(String name){
        return headers.get(name);
    }
    
    public String getQueryValue(String name){
        return queryValues.get(name);
    }
    
    private void parseQuery(String query){
        queryValues = new HashMap<String,String>();
        StringBuilder name = new StringBuilder();
        StringBuilder value = new StringBuilder();
        int pos = 0;
        while(pos < query.length()){
            while(query.charAt(pos) != '='){
                name.append(query.charAt(pos));
                pos++;
            }
            pos++;
            while(pos < query.length() && query.charAt(pos) != '&'){
                value.append(query.charAt(pos));
                pos++;
            }
            queryValues.put(name.toString(), value.toString());
            name.delete(0, name.length());
            value.delete(0, value.length());
            pos++;
        }
    }
    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append("Method: ").append(method).append("\n");
        builder.append("Resource: ").append(resource).append("\n");
        for(String name : headers.keySet()){
            builder.append(name).append(": ").append(headers.get(name)).append("\n");
        }
        return builder.toString();
        
    }
}
