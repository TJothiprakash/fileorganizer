📂 File Organizer (Vert.x + Java)

A simple File Organizer built in Java + Vert.x that automatically organizes files in a given folder by their type/extension.
It can run in two modes:

Normal mode → watches the folder in real-time and organizes new files.

Scan mode (--scan) → first scans and organizes all existing files, then starts real-time watching.

🚀 Features

✅ Automatically organizes files into subfolders (Images/, Documents/, Videos/, etc.)

✅ Supports on-demand full scan of existing files (--scan).

✅ Runs in event-driven mode with Vert.x for efficient folder watching.

✅ Works cross-platform (Windows, macOS, Linux).

🛠️ Setup in IntelliJ IDEA

Clone the repo

git clone https://github.com/tjothiprakash/file-organizer.git
cd file-organizer


Open in IntelliJ

Open IntelliJ IDEA → Open Project → select the cloned folder.

IntelliJ will detect it as a Maven project automatically.

Import dependencies

IntelliJ may ask: "Maven projects need to be imported" → click Import.

Run inside IntelliJ

Locate MainVertxApp.java.

Right-click → Run 'MainVertxApp'.

(Optional) Add --scan in Run Configurations → Program Arguments.

⚡ Running from Terminal
1. Build JAR with Maven
   mvn clean package


This generates a JAR inside:

target/file-organizer-1.0-SNAPSHOT.jar

2. Run normally (watch only)
   java -jar target/file-organizer-1.0-SNAPSHOT.jar

3. Run with scan (organize existing files first)
   java -jar target/file-organizer-1.0-SNAPSHOT.jar --scan

📂 Example

Suppose your Downloads folder has:

report.pdf
movie.mp4
photo.jpg


After running the organizer, the folder becomes:

Documents/report.pdf
Videos/movie.mp4
Images/photo.jpg


And any new file added later is automatically organized.

✅ Future Improvements (Optional Ideas)

Configurable folder mappings (e.g., .pdf → Docs/).

Logging + notification when files are moved.

Multi-folder support.