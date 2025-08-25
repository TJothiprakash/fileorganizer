
import com.jp.fileorganizer.FileOrganizer;
import com.jp.fileorganizer.FolderWatcherVerticle;
import io.vertx.core.Vertx;
public class MainVertxApp {
    public static void main(String[] args) {
        String folderPath = "D:/comet/downloads";

//        java -jar file-organizer.jar        # normal event-driven mode
//java -jar file-organizer.jar --scan # scan & organize existing files, then start watcher

        boolean doScan = args.length > 0 && args[0].equalsIgnoreCase("--scan");

        FileOrganizer organizer = new FileOrganizer(folderPath);

        // On-demand scan
        if (doScan) {
            System.out.println("Running on-demand scan...");
            organizer.organizeAllFiles(); // A method that scans and organizes all existing files
        }

        // Start folder watcher as usual
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new FolderWatcherVerticle(folderPath));
    }
}
