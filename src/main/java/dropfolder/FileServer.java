/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dropfolder;

import dropfolder.server.http.BinaryResponse;
import dropfolder.server.http.Request;
import dropfolder.server.http.Response;
import dropfolder.builder.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.SocketTimeoutException;

import java.net.Socket;



/**
 *
 * @author Laptop
 */
public class FileServer {

    
    ServerSocket server;
    String directoryName;
    boolean running;
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        FileServer myFileServer = new FileServer();
        switch(args.length){
            case 0:
                System.out.println("usage: fileserver <port> <directory>");
                System.out.println("--port: on which port shall the fileserver listen");
                System.out.println("--directory (optional): which directory should be served");
                break;
            case 1:
                myFileServer.start(Integer.parseInt(args[0]),"webroot");
                break;
            default:
                myFileServer.start(Integer.parseInt(args[0]),args[1]);
        }
        
        
    }
    
    public void start(int port,String _directoryName){
        directoryName = _directoryName;
        APICall.setPathOffset(_directoryName);
        File directory = new File(directoryName);
        if(!directory.exists()){
          directory.mkdirs();
        }
        System.out.println("Starting...");
        try{
           server = new ServerSocket(port);
           server.setSoTimeout(2000);
           running = true;
           while(running){        
               try{
                    Socket sock = server.accept();
                    (new Thread(new Servlet(sock))).start();
               }catch(SocketTimeoutException ex){ ; }// Just a timeout}
           } 
        }catch(IOException ex){ ex.printStackTrace();}
        
        System.out.println("Stopping...");
    }
    
    
    class Servlet implements Runnable{
        
        Socket client;
        Request request;
        String response;
        
        Servlet(Socket sock){
            client = sock;
            try{
                BufferedReader reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                String line = reader.readLine();
                int length = line.length();
                if(line != null){
                  String[] parts = line.split(" ");
                  request = new Request(parts[0],parts[1].replaceAll("%20", " "));
                  line = reader.readLine();
                  while(!line.equals("")){
                      request.setHeader(line, line);
                      length += line.length();
                      line = reader.readLine();
                  }
                  System.out.println("length: "+length);
                }
            }catch(IOException ex){ ex.printStackTrace();}
        
        }

        @Override
        public void run() {            
            try{
                Response r;
                if(request == null) return;
                if(request.getResource().equals("/kill")){
                    running = false;
                    System.out.println("Killing server...");
                    r = Response.createEndPage();
                }
                else if(request.getResource().startsWith("/api")){
                    r = APICall.makeAPICall(request);
                } 
                else if(request.getResource().equals("/favicon.ico")){
                        BinaryResponse br = new BinaryResponse(client,200,"OK");
                        ImageFilePageBuilder pb = new ImageFilePageBuilder(getClass().getResourceAsStream("/images/favicon.ico"),"ico");
                        br.setHeader("Content-Type",pb.getContentType());
                        br.setBody(pb.createData());
                        br.send();
                        client.close();
                        return;
                } else if(request.getResource().startsWith("/images")){
                        ImageFilePageBuilder pb = null;
                        String resource= request.getResource().substring(request.getResource().lastIndexOf('/')+1);
                        if(resource.equals("folder.png")) pb = new ImageFilePageBuilder(getClass().getResourceAsStream("/images/folder.png"),"png");
                        else if(resource.equals("folder_new.png"))  pb = new ImageFilePageBuilder(getClass().getResourceAsStream("/images/folder_new.png"),"png");
                        else if(resource.equals("shutdown.png")) pb = new ImageFilePageBuilder(getClass().getResourceAsStream("/images/shutdown_box_red.png"),"png");
                        else if(resource.equals("parent.png"))pb = new ImageFilePageBuilder(getClass().getResourceAsStream("/images/folder_blue_parent.png"),"png");
                        else if(resource.equals("delete.png"))pb = new ImageFilePageBuilder(getClass().getResourceAsStream("/images/delete.png"),"png");
                        else if(resource.equals("txt.png"))pb = new ImageFilePageBuilder(getClass().getResourceAsStream("/images/text.png"),"png");
                        else if(resource.equals("image.png"))pb = new ImageFilePageBuilder(getClass().getResourceAsStream("/images/image.png"),"png");
                        if(pb != null){
                            BinaryResponse br = new BinaryResponse(client,200,"OK");
                            br.setHeader("Content-Type",pb.getContentType());
                            br.setBody(pb.createData());
                            br.send(); 
                            client.close();
                            return;
                        }else{
                            r = Response.create404();
                        }
                }else if(request.getResource().equals("/NeuerOrdner.jpg")){
                        BinaryResponse br = new BinaryResponse(client,200,"OK");
                        ImageFilePageBuilder pb = new ImageFilePageBuilder(new File("C:\\Users\\Laptop\\Desktop\\NeuerOrdner.jpg"),"jpg");
                        br.setHeader("Content-Type",pb.getContentType());
                        br.setBody(pb.createData());
                        br.send();
                        client.close();
                        return;
                }else{               
                    String[] path = request.getResource().split("/");
                    StringBuilder pathBuilder = new StringBuilder();
                    for(int i = 1;i<path.length;i++) pathBuilder.append(File.separator).append(path[i]);
                    String contentPath = directoryName+pathBuilder.toString();
                    File f = new File(contentPath);
                    if(contentPath.endsWith(".pdf")){
                        BinaryResponse br = new BinaryResponse(client,200,"OK");
                        PDFFilePageBuilder pb = new PDFFilePageBuilder(f);
                        br.setHeader("Content-Type",pb.getContentType());
                        br.setBody(pb.createData());
                        br.send();
                        client.close();
                        return;
                    }else if(contentPath.endsWith(".jpeg") || contentPath.endsWith(".jpg")){                   
                        BinaryResponse br = new BinaryResponse(client,200,"OK");
                        ImageFilePageBuilder pb = new ImageFilePageBuilder(f,"jpg");
                        br.setHeader("Content-Type",pb.getContentType());
                        br.setBody(pb.createData());
                        br.send();
                        client.close();
                        return;
                    }
                    PageBuilder pb = PageBuilder.create(f, pathBuilder.toString());
                    if(pb != null){
                        r = Response.create200();
                        r.setBody(pb.createPage());
                        r.setHeader("Content-Type",pb.getContentType());
                    }else{
                        r = Response.create404();
                }
                }
                // write response
                PrintWriter writer = new PrintWriter(client.getOutputStream());
                writer.print(r);
                writer.flush();
                client.close();
            }catch(IOException ex){ ex.printStackTrace();}
            
        }
    }
}
