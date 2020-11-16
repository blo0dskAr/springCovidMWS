package at.blo0dy.springCovidMWS.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class FileUtils {



  // Stuendliche GesundheitsMinisterium daten passen schwer nicht mit den Data.gv.at-daten ueberein.
  // Daher nur die taegliche übermittlung der Data.gv daten verwendet.
//  private static final String fileName = "data.zip";
//  private static final String fetchURL= "https://info.gesundheitsministerium.at/data/" + fileName;
//  private static final String fileDirectory = "H:/covidApp";

  private static final String fileName = "CovidFaelle_Timeline.csv";
  private static final String fetchURL= "https://covid19-dashboard.ages.at/data/" + fileName;
  private static final String fileDirectory = "H:/covidApp";

  private static final String dataDirectory = fileDirectory + "/data";
  private static final String filePath = fileDirectory + "/" + fileName;

  public static void checkAndCreateFolder() {
    // Check if Directory exists, and create otherwise
    log.debug("Check if FilePath " + "(" + fileDirectory + ") exists");
    Path path = Paths.get(fileDirectory);
    Path dataPath = Paths.get(dataDirectory);
    if (!Files.exists(dataPath) ) {
      log.debug("FilePath not found: --> Creating FilePath: " + path.toString());
      try {
        Files.createDirectory(dataPath);
        log.debug("FilePath successfully created");
      } catch (IOException e) {
        log.warn("IOException caught at FolderCreation:");
        log.error(e.getMessage());
      }
    }
  }

  public static String saveDataFile() {
    log.debug("Trying to Download file from: " + fetchURL );
    try {
      ReadableByteChannel readableByteChannel = Channels.newChannel(new URL(fetchURL).openStream());
      log.debug("File downloaded. Saving file...") ;
      FileOutputStream fileOutputStream = new FileOutputStream(filePath);
      fileOutputStream.getChannel()
              .transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
      fileOutputStream.close();
      readableByteChannel.close();
      log.debug("File successfully downloaded and saved at: " + filePath );
    } catch (MalformedURLException e) {
      log.warn("MalformedURLException caught:");
      log.error(e.getMessage());
    } catch (IOException e) {
      log.warn("IOException caught:");
      log.error(e.getMessage());
    }
    return filePath;
  }

  // Fuers unzippen. aktuelles file aber kein zip mehr.
//  public static void unzipFile() {
//    File destDir = new File(dataDirectory + "/");
//    log.debug("Unzipping file to Directory: " + destDir);
//    byte[] buffer = new byte[1024];
//    try {
//      ZipInputStream zis = new ZipInputStream(new FileInputStream(filePath));
//      ZipEntry zipEntry = zis.getNextEntry();
//      while (zipEntry != null) {
//        log.debug("Extracting File " + zipEntry + " " + dataDirectory);
//        File newFile = getSingleFileFromZip(destDir, zipEntry);
//        FileOutputStream fos = new FileOutputStream(newFile);
//        int len;
//        while ((len = zis.read(buffer)) > 0) {
//          fos.write(buffer, 0, len);
//        }
//        fos.close();
//        zipEntry = zis.getNextEntry();
//      }
//      zis.closeEntry();
//      zis.close();
//      log.debug("Zipfile sucessfully unpacked");
//    } catch (FileNotFoundException e) {
//      log.warn("FileNotFoundException caught:");
//      log.error(e.getMessage());
//    } catch (IOException e) {
//      log.warn("IOException caught:");
//      log.error(e.getMessage());
//    }
//
//  }
//
//  // Zip-Slip prevention
//  public static File getSingleFileFromZip(File destinationDir, ZipEntry zipEntry) throws IOException {
//    File destFile = new File(destinationDir, zipEntry.getName());
//
//    String destDirPath = destinationDir.getCanonicalPath();
//    String destFilePath = destFile.getCanonicalPath();
//
//    if (!destFilePath.startsWith(destDirPath + File.separator)) {
//      throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
//    }
//    return destFile;
//  }



}
