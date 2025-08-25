package com.jp.fileorganizer;

import io.vertx.core.AbstractVerticle;

import java.nio.file.*;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class FolderWatcherVerticle extends AbstractVerticle {

    private final String folderPath;
    private final Map<String, Long> lastSeen = new HashMap<>();
    private final long cooldown = 500; // 500ms deduplication window
    private final Queue<Path> fileQueue = new ConcurrentLinkedQueue<>();
    FileOrganizer organizer;
    private final Set<Path> ignoredFolders;//= new HashSet<>();


//organizer.organizeFile(file.toFile());

    public FolderWatcherVerticle(String folderPath) {
        this.folderPath = folderPath;
        this.organizer = new FileOrganizer(folderPath);
        this.ignoredFolders = new HashSet<>();
        addIgnoredFolders(new String[]{});
    }


    @Override
    public void start() {
        // Thread to watch folder
        new Thread(() -> watchFolder(), "FolderWatcherThread").start();

        // Thread to process stable files
        new Thread(() -> processQueue(), "FileProcessorThread").start();
    }

    private void watchFolder() {
        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
            Path path = Paths.get(folderPath);
            path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY);

            System.out.println("Watching folder: " + folderPath);

            while (true) {
                WatchKey key = watchService.take(); // blocking
                for (WatchEvent<?> event : key.pollEvents()) {
                    Path filename = (Path) event.context();
                    String fnameStr = filename.toString().toLowerCase();

                    // Skip temp or incomplete files
                    if (fnameStr.endsWith(".tmp") || fnameStr.endsWith(".crdownload") || fnameStr.endsWith(".part")) {
                        continue;
                    }

                    Path fullPath = path.resolve(filename);

                    // Skip ignored folders
                    if (Files.isDirectory(fullPath) || ignoredFolders.contains(fullPath)) {
                        continue;
                    }

                    // Deduplicate multiple events
                    long now = System.currentTimeMillis();
                    if (lastSeen.containsKey(fnameStr) && now - lastSeen.get(fnameStr) < cooldown) {
                        continue;
                    }
                    lastSeen.put(fnameStr, now);

                    System.out.println("New file detected: " + fullPath);

                    // Add to queue for stability check
                    fileQueue.offer(fullPath);
                }
                key.reset();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processQueue() {
        while (true) {
            Path file = fileQueue.poll();
            if (file != null) {
                if (FileStabilityChecker.isFileStable(file, 5, 1000)) {
                    System.out.println("Processing stable file: " + file);
                    // Call your FileOrganizer logic here
                    organizer.organizeFile(file.toFile());
                } else {
                    // Not stable, put back to queue
                    fileQueue.offer(file);
                }
            }

            try {
                Thread.sleep(500); // avoid busy wait
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public void addIgnoredFolders(String[] folderstoIgnore) {

        ignoredFolders.add(Paths.get(folderPath, "Documents"));
        ignoredFolders.add(Paths.get(folderPath, "Sheets"));
        ignoredFolders.add(Paths.get(folderPath, "Images"));
        ignoredFolders.add(Paths.get(folderPath, "Videos"));
        ignoredFolders.add(Paths.get(folderPath, "Archives"));
        ignoredFolders.add(Paths.get(folderPath, "Code"));
        ignoredFolders.add(Paths.get(folderPath, "Others"));

    }

}
