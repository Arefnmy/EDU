package shared.util;

import shared.model.media.Media;
import shared.model.media.MediaType;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUploader {
    private static String path = "src/main/resources/shared/files";

    public static void setPath(Config config){
        path = config.getProperty("download-path");
    }

    public static Media uploadFile() {
        String name = JOptionPane.showInputDialog("Enter name for media :");
        if (name == null)
            return null;

        MediaType type = (MediaType) JOptionPane.showInputDialog(null, "Select media type :",
                "Media type", JOptionPane.INFORMATION_MESSAGE, null, MediaType.values(), MediaType.PDF);
        if (type == null)
            return null;

        JFileChooser fileChooser = new JFileChooser(path);
        int x = fileChooser.showOpenDialog(null);
        if (x == JFileChooser.FILES_ONLY) {
            File file = fileChooser.getSelectedFile();
            try {
                return new Media(name, Files.readAllBytes(Path.of(file.getPath())), type);
            } catch (IOException ignored) {
            }
        }
        return null;
    }

    public static String downloadFile(Media media){
        String path = FileUploader.path + "/" + media.getName() + media.getMediaType().getName();
        try {
            FileOutputStream fos = new FileOutputStream(path);
            fos.write(media.getBytes());
            return "Downloaded : " + path;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return "Error!";
    }
}
