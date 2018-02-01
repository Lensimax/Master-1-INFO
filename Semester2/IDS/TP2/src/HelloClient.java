import java.rmi.*;
import java.rmi.registry.*;

public class HelloClient {
    public static void main(String [] args) {

        try {
            if (args.length < 1) {
                System.out.println("Usage: java HelloClient <rmiregistry host>");
                return;
            }

            String host = args[0];

            // Get remote object reference
            Registry registry = LocateRegistry.getRegistry(host);
            Hello h = (Hello) registry.lookup("HelloService");

            // Remote method invocation
            String res = h.sayHello();
            System.out.println(res);
            String test = "Jo";
            String name = h.sayName(test);
            System.out.println(name);

        } catch (Exception e)  {
            System.err.println("Error on client: " + e);
        }
    }
}