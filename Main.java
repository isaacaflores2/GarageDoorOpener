package bb_garagedooropener;
import java.io.FileReader;
import java.io.File;
import java.util.Scanner;


/**
 *
 * @author Isaac Flores June 2018
 * - Main class initiates the two servers (HTTPS and MQTT Client) that 
 * make up our HTTPS-MQTT Bridge. 
 * - The main class creates an instace of the config class with loads a config
 * file. The default file is titled "garage_config.txt" Use this file to 
 * format your own custom config file. 
 * - To do: 1) Remove remaining string literals
 * 2) Add descriptive comments for each class and methods
 * 3) Remove string literal for config file path 
 *      -provide option to pass file path as command line argument 
 *      -provide option to look for file for config file in current directory
 * 4) Add option to pass state variables of mqtt client and https server to handler class 
 *      - the handlerclass will have function to display status of mqtt client, https server
 *      - provide state of other services (garage door mqtt client, mosquitto broker)
 */
public class Main 
{
    
    //Debug flag to allow debug and error messages to print to stdout
    public static boolean debugFlag = false;
    
   
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        
        //Create instance of configuration class
        Config config = new Config(); 

        if(args.length == 0 )
        {
            //Read config from default directory (default directory is the current working directory)
            config.loadConfig("/home/debian/GarageDoor/build/bb_garagedooropener/garage_config.txt");       
            
        }        
        else 
        {   
            for(int i=0; i < args.length; i++)
            {
                if(args[i].contentEquals(Config.debugArg) )
                    debugFlag = true;

                if(args[i].contentEquals(Config.configFilePathArg))
                {
                    //Read config file
                    config.loadConfig(args[i+1]);
                    
                    //progress counter past file path to next arguments
                    i++;
                }
                
                if(args[i].contentEquals(config.helpArg))
                {
                    config.printConfigHelp();
                }
            }            
            
            //If config file path command line argument was not set. Load default config file
            config.loadConfig("/home/debian/GarageDoor/build/bb_garagedooropener/garage_config.txt");          
        }
        
        /*
        String currentDirectory;
        currentDirectory = System.getProperty("user.dir");
        System.out.println("Current working directory : "+currentDirectory);
        */
        
        //Start MqttClient
        GarageMqttServer garageMqttServer = new GarageMqttServer(config); 
        garageMqttServer.start();
  
        //Start HTTPS Server
        HTTPSServer httpsServer = new HTTPSServer(config, garageMqttServer);
        httpsServer.start();     
        
        System.out.println("MqttClient and HTTPS Server have started!");               
    }
    
}
