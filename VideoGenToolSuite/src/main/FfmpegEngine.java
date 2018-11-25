package main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.xtext.example.mydsl.videoGen.Media;

public class FfmpegEngine {

  private List<String> playlist = new ArrayList<>();
  private String ffmpegFile = "";
  private String playlistPath = "/Users/Abraham/Desktop/Pexels/playlist.txt";
  private String outputBasePath = "/Users/Abraham/Desktop/Pexels/";
  private VideoSeqUtils utils = new VideoSeqUtils();


  public List<String> playListFiles(EList<Media> medias) {
    medias.forEach(media -> {
      this.playlist.add(this.utils.getLocationOfVseq(this.utils.renderVseq(media)));
      this.playlist.remove("");
    });
    return this.playlist;
  }

  public List<String> generateVariant(EList<Media> medias) {
    List<String> variant = new ArrayList<>();
    medias.forEach(media -> {
      variant.add(this.utils.getIdOfVseq(this.utils.renderVseq(media)));
      variant.remove("");
    });

    return variant;
  }

  public String createFfmpegPlaylist(List<String> playlist) {
    playlist.forEach(element -> this.ffmpegFile += "file '" + element + "' \n");

    return this.ffmpegFile;
  }

  public void writeToFile(String playlist)
      throws FileNotFoundException, UnsupportedEncodingException {
    PrintWriter writer = new PrintWriter(this.playlistPath, "UTF-8");
    writer.println(playlist);
    writer.close();
  }

  public void generateVideo(boolean doGenerate, String filename) {
    if (doGenerate) {
      this.outputBasePath += filename;
      this.execute(
          "/usr/local/bin/ffmpeg -y -f concat -safe 0 -i " + this.playlistPath + " -c copy "
              + this.outputBasePath + ".mkv");
    }
  }

  public String getOutputPath() {
    return this.outputBasePath;
  }

  public void execute(String command) {
    try {
      Process p = Runtime.getRuntime().exec(command);

    } catch (IOException e) {
      e.printStackTrace();

    }
  }

}
