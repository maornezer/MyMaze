package Server;

import java.io.*;
import java.util.Properties;

public class Configurations {

    private static Configurations instanceSingleton = null;
    Properties prop;

    private Configurations() {
        prop = new Properties();
        loadConfig();
    }

    private void loadConfig() {
        File configFile = new File("resources/config.properties");
        if (configFile.length() == 0) {
            try (OutputStream output = new FileOutputStream(configFile)) {
                prop.setProperty("threadPoolSize", "10");
                //prop.setProperty("mazeGeneratingAlgorithm", "MyMazeGenerator");
                prop.setProperty("mazeSearchingAlgorithm", "BestFirstSearch");
                //prop.setProperty("CompressorType", "MyCompressorOutputStream");
                prop.store(output, null);
            } catch (IOException io) {
                io.printStackTrace();
            }
        } else {
            try (InputStream input = new FileInputStream(configFile)) {
                prop.load(input);
            } catch (IOException io) {
                io.printStackTrace();
            }
        }

    }
    /*
        The public function that check if there is an instance of this class return it, else creat new one and then return it
     */
    public static Configurations getInstance() {
        if(instanceSingleton ==null)
            instanceSingleton =new Configurations();
        return instanceSingleton;
    }

    //get the requested property
    public String getProp(String key) {
        return prop.getProperty(key);
    }

    //set new value of properties to the file
    public void setProp(String key, String value) {
        try{
            //C:\Users\USER\Desktop\maor maze\Maze\resources
            OutputStream out = new FileOutputStream("C:\\Users\\User\\Desktop\\Maze_Project\\MAZE\\resources\\config.properties");
            //set the new property
            prop.setProperty(key,value);
            //save it to the file
            prop.store(out,"");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}