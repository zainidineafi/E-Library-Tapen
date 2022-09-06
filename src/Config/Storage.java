/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Config;

import java.io.File;
import java.nio.file.Files;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import Config.Helper;

/**
 *
 * @author Illuminate
 */
public class Storage {
    /*
    * String type = "image|video|text"
    */
    public File open(String type) {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        
        switch(type) {
            case "image":
                    fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
                    );
                break;
            case "video":
                    fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("Video Files", "*.mp4", "*.mkv")
                    );
                break;
            case "text":
                    fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("Text Files", "*.txt")
                    );
                break;
            default:
                break;
        }
        
        return fileChooser.showOpenDialog(stage);
    }
    
    public String upload(String path, String filename, String replaceName) {
        try {
            if(replaceName.equals("")) {
                replaceName = new Helper().hashToMD5("File uploaded at " + new Helper().getDate());
            }

            File oldFile = null;
            File newFile = null;
            String ext = "jpg";
            oldFile = new File(filename);
            newFile = new File(path + "/" + replaceName + "." + ext);
            Files.copy(oldFile.toPath(), newFile.toPath());
            
            return newFile.getPath().replace("\\", "\\\\");
        } catch(Exception e) {
            throw new Error("Terjadi kesalahan mengunggah file.");
        }
    }
    
    public boolean delete(String path) {
        File file = new File(path);
        
        if(file.exists() && !file.isDirectory()) {
            file.delete();
        }
        
        return true;
    }
}
