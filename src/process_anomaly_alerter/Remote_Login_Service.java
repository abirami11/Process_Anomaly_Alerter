/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package process_anomaly_alerter;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author naval_
 */
public class Remote_Login_Service {

    Remote_Login_Service() {
        int counter=0;
        Data_Store DS = new Data_Store();
        String[][] AllCC = DS.Fetch_Client_Credentials();
        while(AllCC[counter][0] != null){
            Thread thread = new Thread(new DoLogin_FetchData(AllCC[counter][0],AllCC[counter][1],AllCC[counter++][2]));
            thread.start();
        }
    }
}


class DoLogin_FetchData implements Runnable{
    String user = "";
    String password = "";
    String host = "";
    String User_List_File="UserList.csv";
    String cmd="ps -eo comm= | sed 's|.*/||'";
    int port=22;
    
    DoLogin_FetchData(String user,String password, String host){
        this.user=user;
        this.password=password;
        this.host=host;
    }

    @Override
    public void run() {
        Date date = new Date();
        String line = "";
        String result = "";
        SimpleDateFormat ft =  new SimpleDateFormat ("hh:mm:ss");

        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(user, host, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            System.out.println("Establishing Connection for host: "+host);

            // Avoid asking for key confirmation
            Properties prop = new Properties();
            prop.put("StrictHostKeyChecking", "no");
            session.setConfig(prop);
            session.connect();

            Channel channel = session.openChannel("shell");
            channel.connect();

            DataInputStream dataIn = new DataInputStream(channel.getInputStream());  
            DataOutputStream dataOut = new DataOutputStream(channel.getOutputStream());  

            // send ls command to the server  
            dataOut.writeBytes(cmd+"\r\n exit\r\n");  
            dataOut.flush();  

            // and print the response   
            System.out.println(dataIn.readLine());
            while(!dataIn.readLine().contains(cmd));
 //           System.out.println(dataIn.readLine());
 //           System.out.println(dataIn.readLine());
        
            while ((line = dataIn.readLine()) != null) 
                if(!line.contains(user))
                    result += line + "\n";
// add time                    result += ft.format(date) +","+line + "\n";
            
        
            dataIn.close();  
            dataOut.close();  
            channel.disconnect();  
            session.disconnect();
            Log_Collector LC = new Log_Collector();
            LC.Collect_Log(user,host, result);
            
            System.out.println("Logs uploaded at "+ft.format(date)+"...Connection terminated successfully!!");
        } catch(Exception e){System.err.print(e);
        }    
    }    
}