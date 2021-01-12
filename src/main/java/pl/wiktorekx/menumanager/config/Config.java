package pl.wiktorekx.menumanager.config;

import com.google.common.io.ByteStreams;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;

public class Config extends YamlConfiguration {
    private File file;

    public Config(File file) {
        this.file = file;
    }

    public void loadConfig(){
        this.loadConfig(null);
    }

    public void loadConfig(InputStream resource){
        try {
            if(!file.exists()){
                file.createNewFile();
                if(resource != null){
                    OutputStream outputStream = new FileOutputStream(file);
                    ByteStreams.copy(resource, outputStream);
                    resource.close();
                    outputStream.close();
                }
            }
            load(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public File getFile() {
        return file;
    }
}
