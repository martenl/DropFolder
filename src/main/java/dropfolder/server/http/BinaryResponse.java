/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dropfolder.server.http;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Laptop
 */
public class BinaryResponse {

    Socket client;   
    final int status;
    final String statusMsg;
    Map<String,String> headers;
    byte[] body;
    
    public BinaryResponse(Socket _client,int _status,String _statusMsg){
        client = _client;
        status = _status;
        statusMsg = _statusMsg;     
        headers = new HashMap<String,String>();
    }
    
    public void setBody(byte[] _body){
        body = _body;
    }
    
    public void setHeader(String name,String value){
        headers.put(name, value);
    }
     
    public void send(){
        StringBuilder builder = new StringBuilder();
        builder.append("HTTP/1.1 ").append(status).append(" ").append(statusMsg).append("\n");
        for(String name : headers.keySet()){
            builder.append(name).append(": ").append(headers.get(name)).append("\n");
        }
        builder.append("\n");
        try{
            client.getOutputStream().write(builder.toString().getBytes());
            client.getOutputStream().write(body);
        }catch(IOException ex){ ex.printStackTrace();}
        
    }
    
    
}
