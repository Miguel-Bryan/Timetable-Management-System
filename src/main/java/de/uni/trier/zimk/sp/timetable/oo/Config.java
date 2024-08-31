/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uni.trier.zimk.sp.timetable.oo;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.thoughtworks.xstream.io.xml.DomDriver;
import de.uni.trier.zimk.sp.timetable.util.Timetable;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.swing.JFileChooser;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import static java.lang.System.out;


/**
 *
 * @author Landry Ngani
 */
public class Config {

    @XStreamOmitField
    private static Logger logger = Logger.getLogger(Config.class);
    public final static String ORG_CONFIG_FILE = "/de/uni/trier/zimk/sp/timetable/io/configuration.xml";
    private final static String WORKERS_CONFIG_FILE = "/de/uni/trier/zimk/sp/timetable/io/workers.xml";
    public final static String JSON_ORG_CONFIG_FILE = "/de/uni/trier/zimk/sp/timetable/io/configuration.json";
    private final static String JSON_WORKERS_CONFIG_FILE = "/de/uni/trier/zimk/sp/timetable/io/workers.json";
    
    
    
    
    public static void saveSampleConfig() {

        String[] DAY_NAMES = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        List<Workday> workdays = new ArrayList<Workday>();
        int weekLength = 5;
        for (int i = 0; i < weekLength; i++) {
            workdays.add(new Workday(i + 1, DAY_NAMES[i]));
        }

        String[] LOC_NAMES = {"SP 1", "SP 2", "SP 3"};
        List<Location> locations = new ArrayList<Location>();
        for (int i = 0; i < LOC_NAMES.length; i++) {
            locations.add(new Location(i + 1, LOC_NAMES[i]));
        }

        int[] CAPACITIES = {1, 2, 2, 2, 2};
        for (int i = 0; i < weekLength; i++) {
            for (int j = 0; j < locations.size(); j++) {

                int min = 8;
                int max = 18;
                int step = 2;

                for (int a = min; a < max; a = a + step) {
                    LocationShift shift = new LocationShift(workdays.get(i), locations.get(j), a, a + step, CAPACITIES[ (a - min) / step]);
                    locations.get(j).getShifts().add(shift);
                }
            }
        }
        
        String [] WORKERS_NAMES = {
            "Iris", "Christian H.", "Thorsten", "Jan", "Sabrina", "Anna", "Patrick",
            "Raphael", "Michael", "Katha", "Thomas", "Adrian", "Christian G.", "Joscha"
        };
        
        int[] WORKER_DEBITS = {
            7, 6, 3, 3, 6, 3, 9,
            3, 5, 4, 5, 4, 9, 8};

        OrganisationalConfiguration configuration = Config.loadOrganisationalConfiguration();
        List<Workday> workdaysX = configuration.getWorkdays();
        
        
        List<Worker> workers = new ArrayList<Worker>();
        for (int i = 0; i < WORKERS_NAMES.length; i++) {
            Worker worker = new Worker(WORKERS_NAMES[i], WORKER_DEBITS[i]);
            
            List<TimePeriod> preferences = new ArrayList<TimePeriod>();
            TimePeriod tp = new TimePeriod( workdays.get( 0 ), 8 , 18 );
            preferences.add(tp);
            worker.setPreferences( preferences );
            
            workers.add(worker);
        } 


        OrganisationalConfiguration organisationalConfiguration = new OrganisationalConfiguration(workdays, locations, workers);
        saveOrganisationalConfigurationWithJackson(organisationalConfiguration);
    }

    /**
     *
     */
    public static void saveSampleWorkers() {        
        String [] WORKERS_NAMES = {
            "Iris", "Christian H.", "Thorsten", "Jan", "Sabrina", "Anna", "Patrick",
            "Raphael", "Michael", "Katha", "Thomas", "Adrian", "Christian G.", "Joscha"
       };     
        int[] WORKER_DEBITS = {
            7, 6, 3, 3, 6, 3, 9,
            3, 5, 4, 5, 4, 9, 8};
        OrganisationalConfiguration configuration = Config.loadOrganisationalConfiguration();
        List<Workday> workdays = configuration.getWorkdays();
        
        List<Worker> workers = new ArrayList<Worker>();
        for (int i = 0; i < WORKERS_NAMES.length; i++) {
            Worker worker = new Worker(WORKERS_NAMES[i], WORKER_DEBITS[i]);
            
            List<TimePeriod> preferences = new ArrayList<TimePeriod>();
            TimePeriod tp = new TimePeriod( workdays.get( 0 ), 8 , 18 );
            preferences.add(tp);
            worker.setPreferences( preferences );
            
            workers.add(worker);     
        }  
        WorkerConfiguration workerConfiguration = new WorkerConfiguration(workdays, workers);
        saveWorkerConfigurationWithJackson(workerConfiguration);
    }

    /**
     *
     * @return
     *
    public static OrganisationalConfiguration loadOrganisationalConfiguration() {

        OrganisationalConfiguration configuration = null;
        ObjectMapper objectMapper = new ObjectMapper();
        
        //XStream xstream = new XStream(new StaxDriver());
        XStream xstream = new XStream(new DomDriver());

        xstream.alias("organisationalConfiguration", OrganisationalConfiguration.class);
        xstream.alias("workday", Workday.class);
        xstream.alias("worker", Worker.class);
        xstream.alias("color", WorkerColor.class);
        xstream.alias("location", Location.class);
        xstream.alias("locationShift", LocationShift.class);
        xstream.alias("timePeriod", TimePeriod.class);

        xstream.processAnnotations(OrganisationalConfiguration.class);
        xstream.processAnnotations(Workday.class);
        xstream.processAnnotations(Worker.class);
        xstream.processAnnotations(Location.class);
        xstream.processAnnotations(LocationShift.class);
        xstream.processAnnotations(TimePeriod.class);

        String userHome = System.getProperty("user.home");
        JFileChooser chooser = new JFileChooser(userHome);
        
        try {
            
            File xmlFile = new File(Timetable.class.getResource(ORG_CONFIG_FILE).getFile());
            InputStream in = new FileInputStream(xmlFile);

            configuration = (OrganisationalConfiguration) xstream.fromXML(in);

            logger.info("Configuration loaded with : " + configuration.getWorkdays().size() + " workdays / " + configuration.getLocations().size() + " locations.");  
            System.out.println("Configuration loaded with : " + configuration.getWorkdays().size() + " workdays / " + configuration.getLocations().size() + " locations.");  

        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(Timetable.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // Update the relations LocationShift<-->Worker
        updateShiftAndWorkerRelations(configuration);
        
        return configuration;
    }
    
    */
    
    /*
    public static OrganisationalConfiguration loadOrganisationalConfigurationWithJackson() {
        
        OrganisationalConfiguration configuration = null;
        System.out.println("Hi, I'm  before the try of loadOrganisationalConfigurationWithJackson !!\n");
        try{
             
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
            

            //writing to console, can write to any output stream such as file
            StringWriter configString = new StringWriter();
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(configString, configuration);
         
            File configJsonFile = new File(Timetable.class.getResource(JSON_ORG_CONFIG_FILE).getFile());
            System.out.println("\nAbsolutePath : "+configJsonFile.getAbsoluteFile());
            
            configuration = objectMapper.readValue(configJsonFile, OrganisationalConfiguration.class);
            System.out.println(configuration + "I'm configuration !!!!");
            System.out.println("\n\n-->\n" + configString + "\n<-- \n\n");
                        
            File jsonFile = new File(Config.class.getResource(JSON_ORG_CONFIG_FILE).getFile());
            //File xmlFile = new File(Config.class.getResource(WORKERS_CONFIG_FILE).getFile());
                        
            //File xmlFile = new File(Config.class.getResource(ORG_CONFIG_FILE).getFile());
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(jsonFile));

            System.out.println("\n\nGoing to be saved <WORKERS> in : " + jsonFile.getAbsolutePath() + ". \n\n");
            out.writeObject(configString.toString());
            out.close();
        }
         catch (Exception ex) {
             
             System.out.println("Hi, try hasn't worked,so I'm catching ! :  "+ ex.getLocalizedMessage());
             ex.printStackTrace();
            // java.util.logging.Logger.getLogger(Timetable.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Update the relations LocationShift<-->Worker
        updateShiftAndWorkerRelations(configuration);
        return configuration;
    }
     */
    
    
    
    
  /* *** This Method is used to convert the previous xml configuration file to the json configuration file(Which is used now) ****
    
    public static OrganisationalConfiguration loadOrganisationalConfigurationWithGSON(){
        OrganisationalConfiguration configuration = null;
        GsonBuilder gsonBuilder = new GsonBuilder();        
        Gson gson = gsonBuilder.create();
         XStream xstream = new XStream(new DomDriver());

        xstream.alias("organisationalConfiguration", OrganisationalConfiguration.class);
        xstream.alias("workday", Workday.class);
        xstream.alias("worker", Worker.class);
        xstream.alias("color", WorkerColor.class);
        xstream.alias("location", Location.class);
        xstream.alias("locationShift", LocationShift.class);
        xstream.alias("timePeriod", TimePeriod.class);

        xstream.processAnnotations(OrganisationalConfiguration.class);
        xstream.processAnnotations(Workday.class);
        xstream.processAnnotations(Worker.class);
        xstream.processAnnotations(Location.class);
        xstream.processAnnotations(LocationShift.class);
        xstream.processAnnotations(TimePeriod.class);

        try{
       
        //Load the xmlFile
        File xmlFile = new File(Config.class.getResource(ORG_CONFIG_FILE).getFile());
        System.out.println("\nPath to the xmlFile : " + xmlFile + "\n");
        InputStream in = new FileInputStream(xmlFile); 
        
        //Deserialize the xmlFile to java object
        configuration = (OrganisationalConfiguration) xstream.fromXML(in);
        System.out.println("\nXml to Java Object: " + configuration);
        
        // CONVERTING THE JAVA OBJECT TO JSON
        Gson gsonInstance = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
        String jsonString = gsonInstance.toJson(configuration);
        System.out.println("\nFILE CONVERTED TO JSON ------> : \n" + jsonString);
        
        //Storing the JSON data in the .json file
        Gson prettyGson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();       
        String prettyJson = prettyGson.toJson(configuration);
        //System.out.println("FILE CONVERTED TO JSON PRETTY ------> : \n" + prettyJson);
        
        
        FileWriter configJsonFile = new FileWriter(Config.class.getResource(JSON_ORG_CONFIG_FILE).getFile());
        System.out.println("Path of JSONconfiguration.json: " + configJsonFile);
        configJsonFile.write(prettyJson);
       System.out.println("Content of JSONconfiguration.json: " + prettyJson);
        
        }
        catch(Exception e){
            e.printStackTrace();
        }     
        if(configuration != null){           
        }else{
            System.out.println("configuration is null");
        }
        return configuration;
    }
    */
    
    
    
    /**
     * 
     * @return 
     */
    public static WorkerConfiguration loadWorkersX() {
        WorkerConfiguration configuration = null;

        //XStream xstream = new XStream(new StaxDriver());
        XStream xstream = new XStream(new DomDriver());
        //xstream.

        xstream.alias("workerConfiguration", WorkerConfiguration.class);
        xstream.alias("workday", Workday.class);
        xstream.alias("worker", Worker.class);
        xstream.alias("color", WorkerColor.class);
        xstream.alias("location", Location.class);
        xstream.alias("locationShift", LocationShift.class);
        xstream.alias("timePeriod", TimePeriod.class);

        xstream.processAnnotations(OrganisationalConfiguration.class);
        xstream.processAnnotations(Workday.class);
        xstream.processAnnotations(Worker.class);
        xstream.processAnnotations(Location.class);
        xstream.processAnnotations(LocationShift.class);
        xstream.processAnnotations(TimePeriod.class);
        
        String userHome = System.getProperty("user.home");
        JFileChooser chooser = new JFileChooser(userHome);

        try {
            File xmlFile = new File(Timetable.class.getResource(WORKERS_CONFIG_FILE).getFile());
            InputStream in = new FileInputStream(xmlFile);

            configuration = (WorkerConfiguration) xstream.fromXML(in);

            logger.info("Configuration loaded with : " + configuration.getWorkdays().size() + " workdays / " + configuration.getWorkers().size()+ " workers.");

        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
        }

        return configuration;
    }

    /**
     * 
     * @param organisationalConfiguration 
     */
    public static void saveOrganisationalConfiguration(OrganisationalConfiguration organisationalConfiguration) {
        ObjectOutputStream out = null;

        try {

            //XStream xstream = new XStream(new StaxDriver());
            XStream xstream = new XStream(new DomDriver());

            xstream.alias("organisationalConfiguration", OrganisationalConfiguration.class);
            xstream.alias("workday", Workday.class);
            xstream.alias("worker", Worker.class);
            xstream.alias("color", WorkerColor.class);
            xstream.alias("location", Location.class);
            xstream.alias("locationShift", LocationShift.class);
            xstream.alias("timePeriod", TimePeriod.class);

           
            xstream.processAnnotations(Workday.class);
            xstream.processAnnotations(Worker.class);
            xstream.processAnnotations(Location.class);
            xstream.processAnnotations(LocationShift.class);
            xstream.processAnnotations(TimePeriod.class);

            String xml = xstream.toXML(organisationalConfiguration);

            System.out.println("\n\n-->\n" + xml + "\n<-- \n\n");

            File xmlFile = new File(Config.class.getResource(ORG_CONFIG_FILE).getFile());
            out = new ObjectOutputStream(new FileOutputStream(xmlFile));

            //out.writeUTF(xml);        
            out.writeObject(xml);

            out.close();


        } catch (FileNotFoundException ex) {
            java.util.logging.Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * 
     * @param configuration 
     
     */

    /*
    public static void saveWorkerConfiguration(WorkerConfiguration configuration) {
        ObjectOutputStream out = null;

        try {

            //XStream xstream = new XStream(new StaxDriver());
            XStream xstream = new XStream(new DomDriver());

            xstream.alias("workerConfiguration", WorkerConfiguration.class);
            xstream.alias("workday", Workday.class);
            xstream.alias("worker", Worker.class);
            xstream.alias("color", WorkerColor.class);
            xstream.alias("location", Location.class);
            xstream.alias("locationShift", LocationShift.class);
            xstream.alias("timePeriod", TimePeriod.class);

            xstream.processAnnotations(OrganisationalConfiguration.class);
            xstream.processAnnotations(Workday.class);
            xstream.processAnnotations(Worker.class);
            xstream.processAnnotations(Location.class);
            xstream.processAnnotations(LocationShift.class);
            xstream.processAnnotations(TimePeriod.class);

            String xml = xstream.toXML(configuration);

            System.out.println("\n\n-->\n" + xml + "\n<-- \n\n");

            File xmlFile = new File(Config.class.getResource(WORKERS_CONFIG_FILE).getFile());
            out = new ObjectOutputStream(new FileOutputStream(xmlFile));

            //out.writeUTF(xml);        
            out.writeObject(xml);

            out.close();


        } catch (FileNotFoundException ex) {
            java.util.logging.Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
*/
    
    /**
     * 
     * @param configuration 
     */
    private static void updateShiftAndWorkerRelations(OrganisationalConfiguration configuration) {
        if(configuration == null){
            System.out.println("configuration is null because the try didn't work. \n");
        }
        
        List<Worker> workers = configuration.getWorkers();
        
        if(workers == null){
            System.out.println("Workers list is null");
        }
        for(Worker worker : workers){
            if( ! worker.getShifts().isEmpty()){
                
                logger.info("Updating worker "+ worker.getName() + " with "+ worker.getShifts().size() + " LocationShifts.");
                List<LocationShift> shifts = worker.getShifts();
                for(LocationShift shift: shifts){
                    shift.getFixedWorkers().add(worker);
                }
                
            }
        }
    }
    

    /**
     * 
     * @param configuration 
     */
    public static void saveWorkerConfigurationWithJackson(WorkerConfiguration configuration) {
        ObjectOutputStream out = null;

        try {

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		
            //writing to console, can write to any output stream such as file
            StringWriter configString = new StringWriter();
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(configString, configuration);
            
            System.out.println("\n\n-->\n" + configString + "\n<-- \n\n");
                        
            File jsonFile = new File(Config.class.getResource(JSON_WORKERS_CONFIG_FILE).getFile());
            //File xmlFile = new File(Config.class.getResource(WORKERS_CONFIG_FILE).getFile());
                        
            File xmlFile = new File(Config.class.getResource(WORKERS_CONFIG_FILE).getFile());
            out = new ObjectOutputStream(new FileOutputStream(xmlFile));

            System.out.println("\n\nGoing to be saved <WORKERS> in : " + xmlFile.getAbsolutePath() + ". \n\n");
            
            //out.writeUTF(xml);        
            out.writeObject(configString.toString());

            out.close();


        } catch (FileNotFoundException ex) {
            java.util.logging.Logger.getLogger(Config.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Config.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
    
        /**
     * 
     * @param organisationalConfiguration 
     */
    public static void saveOrganisationalConfigurationWithJackson(OrganisationalConfiguration organisationalConfiguration) {
        ObjectOutputStream out = null;
        try {
            
            ObjectMapper objectMapper = new ObjectMapper();            
            objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
	
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            
            //writing to console, can write to any output stream such as file
            StringWriter configString = new StringWriter();
            objectMapper.writeValue(configString, organisationalConfiguration);
            
            System.out.println("\n\n-MIGUELITO ->\n" + configString + "\n<-- \n\n");            
            File jsonFile = new File(Config.class.getResource(JSON_ORG_CONFIG_FILE).getFile());
            objectMapper.writeValue(jsonFile,organisationalConfiguration);
            System.out.println("jsonFile saved in : " + jsonFile + "\n");
            if(jsonFile.length() == 0){
               System.out.println("File Exists but is empty !!!!");
            } else {
               System.out.println("File Exists and has a content !");
            }
            /*
            System.out.println("\n STEP ORG 01\n");
            File xmlFile = new File(Config.class.getResource(JSON_ORG_CONFIG_FILE).getFile());
            System.out.println("\n STEP ORG 02\n");
            out = new ObjectOutputStream(new FileOutputStream(xmlFile));
            */

            //System.out.println("\n\nGooing to be saved <CONFIG> in : " + xmlFile.getAbsolutePath() + ". \n\n");
            
            //out.writeUTF(xml);        
            //out.writeObject(configString.toString());
            //out.close();

        } catch (FileNotFoundException ex) {
            java.util.logging.Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
        } 
        /*
        finally {
            try {
                out.close();
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        */
    }

    
   public static OrganisationalConfiguration loadOrganisationalConfiguration(){ 
       OrganisationalConfiguration config = null;    
       ObjectMapper objectMapper = new ObjectMapper();
       
       try{
           File configFile = new File(Config.class.getResource(JSON_ORG_CONFIG_FILE).getFile());
           System.out.println("\nPath to the Configuration File : " + configFile + "\n");     
      
           objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
           config = objectMapper.readValue(configFile,OrganisationalConfiguration.class);           
           //System.out.println("\nConfig file found at: " + objectMapper);
           System.out.println("\n Success and valStart : " + config.getValidityStart());
           System.out.println("\n Workers : " + config.getWorkers().size());
           
       }catch(IOException e){
           //java.util.logging.Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, e);
           e.printStackTrace();
       }
       return config;
   }
}
