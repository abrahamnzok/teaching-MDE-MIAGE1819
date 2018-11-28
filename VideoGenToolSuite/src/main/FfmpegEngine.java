package main;

import java.awt.Image;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.NumberFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.xtext.example.mydsl.videoGen.Media;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class FfmpegEngine {

  private List<String> playlist = new ArrayList<>();
  private String ffmpegFile = "";
  private String playlistPath = "/Users/Abraham/Desktop/Pexels/playlist.txt";
  private String outputBasePath = "/Users/Abraham/Desktop/Pexels/";
  private String compilationLocation = this.outputBasePath;
  private VideoSeqUtils utils = new VideoSeqUtils();
  private String gifLocation = this.outputBasePath;
  private String paletteLocation = this.outputBasePath;


  public List<String> playListFiles(EList<Media> medias) {
    medias.forEach(media -> {
      this.playlist.add(this.utils.getLocationOfVseq(this.utils.renderVseq(media)));
      this.playlist.remove("");
    });
    return this.playlist;
  }

  public List<String> playListFiles(String userPlaylist) {
    return new Gson().fromJson(userPlaylist, List.class);

  }

  public String createFfmpegPlaylist(List<String> playlist) {
    playlist.forEach(element -> this.ffmpegFile += "file '" + element + "' \n");
    return this.ffmpegFile;
  }

  public void writeToFile(String playlist)
      throws IOException {
    Path path = Paths.get(this.playlistPath);
    Files.write(path, playlist.getBytes(),
        StandardOpenOption.CREATE,
        StandardOpenOption.TRUNCATE_EXISTING);
  }

  public void generateVideo(String filename) throws InterruptedException {
    this.compilationLocation += filename + ".mkv";
    this.execute(
        "/usr/local/bin/ffmpeg -y -f concat -safe 0 -i " + this.playlistPath + " -c copy "
            + this.compilationLocation);
  }

  public boolean generateGif(String name) throws InterruptedException {
    String palette = this.paletteLocation + "palette.png";
    String gifName = name.isEmpty() ? "output.gif" : name;
    String gif = this.gifLocation + gifName + ".gif";
    this.gifLocation = gif;
    Path path = Paths.get(this.compilationLocation);
    if (Files.exists(path) && ! Files.isDirectory(path)) {
      String cmdPalette = "/usr/local/bin/ffmpeg -t 3 -ss 2.6 -y -i " + this.compilationLocation
          + " -vf fps=15,scale=480:-1:flags=lanczos,palettegen " + palette;
      String gifCmd = "/usr/local/bin/ffmpeg -y -i " + this.compilationLocation + "\" -i \"" + palette
          + "\" -filter_complex \"fps=15,scale=480:-1:flags=lanczos[x];[x][1:v]paletteuse\" \""
          + gif;
      this.execute(cmdPalette);
      this.execute(gifCmd);
      return true;
    } else {
      return false;

    }
  }

  public String getGifLocation() {
    return this.gifLocation;
  }

  public String getOutputLocation() {
    return this.compilationLocation;
  }


  public List<String> generateVariant(EList<Media> medias) {
    List<String> variant = new ArrayList<>();
    medias.forEach(media -> {
      variant.add(this.utils.getIdOfVseq(this.utils.renderVseq(media)));
      variant.remove("");
    });

    return variant;
  }

  public String getOutputPath() {
    return this.outputBasePath;
  }

  public void execute(String command) throws InterruptedException {
    try {
      Process p = Runtime.getRuntime().exec(command);
      int exitcode = p.waitFor();

    } catch (IOException e) {
      e.printStackTrace();

    }
  }

}
