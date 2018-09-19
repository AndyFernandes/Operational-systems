/**
 * The RMI Client
 */

import java.rmi.*;

public class RMIClient 
{  
   public static void main(String args[]) { 
    try {
        /**
         * We only need this for loading remote classes
         * System.setSecurityManager(new RMISecurityManager());
         */
      String host = "rmi://127.0.0.1/DateServer";
      RemoteDate dateServer = (RemoteDate)Naming.lookup(host);
      System.out.println(dateServer.getDate());
    }
    catch (Exception e) {
        System.err.println(e);
    }
   }
}

