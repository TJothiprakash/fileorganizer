package com.jp.fileorganizer;

import java.nio.file.Files;
import java.nio.file.Path;

public class FileStabilityChecker {

    /**
     * Checks if a file size is stable across multiple intervals.
     * @param file Path of the file
     * @param attempts Number of consecutive size checks
     * @param intervalMillis Interval between checks in milliseconds
     * @return true if file is stable, false otherwise
     */
    public static boolean isFileStable(Path file, int attempts, long intervalMillis) {
        try {
            long previousSize = -1;
            int stableCount = 0;

            for (int i = 0; i < attempts; i++) {
                if (!Files.exists(file)) return false;

                long currentSize = Files.size(file);

                if (currentSize == previousSize) {
                    stableCount++;
                } else {
                    stableCount = 1;
                    previousSize = currentSize;
                }

                if (stableCount >= attempts) return true;

                Thread.sleep(intervalMillis);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}

