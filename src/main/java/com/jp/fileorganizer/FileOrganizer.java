package com.jp.fileorganizer;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class FileOrganizer {

    private final String baseDir;

    public FileOrganizer(String baseDir) {
        this.baseDir = baseDir;
    }

    public void organizeAllFiles() {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Path.of(baseDir))) {
            for (Path path : stream) {
                if (!Files.isDirectory(path)) {
                    organizeFile(path.toFile());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  void organizeFile(File file) {
        String fileName = file.getName().toLowerCase();
        Path targetDir;

        if (fileName.endsWith(".pdf") || fileName.endsWith(".docx") || fileName.endsWith(".txt")) {
            targetDir = Path.of(baseDir, "Documents");
        } else if (fileName.endsWith(".xls") || fileName.endsWith(".xlsx") || fileName.endsWith(".csv")) {
            targetDir = Path.of(baseDir, "Sheets");
        } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") ||
                fileName.endsWith(".png") || fileName.endsWith(".gif")) {
            targetDir = Path.of(baseDir, "Images");
        } else if (fileName.endsWith(".mp4") || fileName.endsWith(".mkv") || fileName.endsWith(".avi")) {
            targetDir = Path.of(baseDir, "Videos");
        } else if (fileName.endsWith(".zip") || fileName.endsWith(".rar") || fileName.endsWith(".tar.gz") || fileName.endsWith(".7z") || fileName.contains(".z0")) {
            targetDir = Path.of(baseDir, "Archives");
        } else if (fileName.endsWith(".java") || fileName.endsWith(".py") ||
                fileName.endsWith(".js") || fileName.endsWith(".cpp")) {
            targetDir = Path.of(baseDir, "Code");
        } else {
            targetDir = Path.of(baseDir, "Others");
        }

        try {
            Files.createDirectories(targetDir); // make sure folder exists
            Path targetPath = targetDir.resolve(file.getName());
            Files.move(file.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Moved " + file.getName() + " -> " + targetDir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
