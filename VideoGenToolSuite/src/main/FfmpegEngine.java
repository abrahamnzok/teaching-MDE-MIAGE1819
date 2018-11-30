package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.xtext.example.mydsl.videoGen.AlternativeVideoSeq;
import org.xtext.example.mydsl.videoGen.Media;
import org.xtext.example.mydsl.videoGen.VideoDescription;
import org.xtext.example.mydsl.videoGen.VideoSeq;

import com.google.gson.Gson;

public class FfmpegEngine {

  private List<String> playlist = new ArrayList<>();
  private String ffmpegFile = "";
  private String playlistPath = "/Users/Abraham/Desktop/Pexels/playlist.txt";
  private String outputBasePath = "/Users/Abraham/Desktop/Pexels/";
  private String compilationLocation = this.outputBasePath;
  private String basePath = "/Users/Abraham/Desktop/Pexels/";
  private VideoSeqUtils utils = new VideoSeqUtils();
  private String gifLocation = "/Users/Abraham/Desktop/Pexels/";
  private String paletteLocation = "/Users/Abraham/Desktop/Pexels/";
  private String vignetteLocation = "/Users/Abraham/Desktop/Pexels/";

  /**
   * 
   * @param medias
   * @return
   * @throws IOException
   * @throws InterruptedException
   */
  public List<Map<String, String>> mediaInformation(EList<Media> medias)
      throws IOException, InterruptedException {
    List<Map<String, String>> mediaSetInformation = new ArrayList<>();
    Map<String, String> information;
    Path path;
    String imgBase64;
    String vignette;
    for (Media media : medias) {
      VideoSeq vseq = this.utils.renderVseq(media);
      if (this.utils.isMandatory(vseq)) {
        information = new HashMap<String, String>();
        vignette = this.generateVignette(this.utils.getMandatoryLocation(vseq),
            this.utils.getMandatoryId(vseq));
        path = Paths.get(vignette);
        imgBase64 = Base64.getEncoder().encodeToString(Files.readAllBytes(path));
        information.put("type", "mandatory");
        information.put("location", this.utils.getMandatoryLocation(vseq));
        information.put("image", imgBase64);
        mediaSetInformation.add(information);
      } else if (this.utils.isOptional(vseq)) {
        information = new HashMap<String, String>();
        vignette = this
            .generateVignette(this.utils.getEachOptionalLocation(vseq), this.utils.getOptionalId(vseq));
        path = Paths.get(vignette);
        imgBase64 = Base64.getEncoder().encodeToString(Files.readAllBytes(path));
        information.put("type", "optional");
        information.put("location", this.utils.getEachOptionalLocation(vseq));
        information.put("image", imgBase64);
        mediaSetInformation.add(information);
      } else {
    	  information = new HashMap<>();
          for (int index = 0; index < this.utils.getAlternativeSize(vseq); index++) {
        	  String alternativeId = this.utils.getAlternativeId(vseq, index);
        	  String alternativeLocation = this.utils.getAlternativeLocation(vseq, index);
              vignette = this.generateVignette(alternativeLocation, alternativeId);
              System.out.println(vignette);
              path = Paths.get(vignette);
              imgBase64 = Base64.getEncoder().encodeToString(Files.readAllBytes(path));
              information.put("type", "alternative");
              information.put("location", alternativeLocation);
              information.put("image", imgBase64);
              mediaSetInformation.add(information);
          }
        }
    }
    return mediaSetInformation;
  }

  /**
   *
   * @param medias
   * @return
   */
  public List<String> playListFiles(EList<Media> medias) {
    medias.forEach(media -> {
      this.playlist.add(this.utils.getLocationOfVseq(this.utils.renderVseq(media)));
      this.playlist.remove("");
    });
    return this.playlist;
  }

  /**
   *
   * @param userPlaylist
   * @return
   */
  public List<String> playListFiles(String userPlaylist) {
    return new Gson().fromJson(userPlaylist, List.class);
  }

  /**
   *
   * @param playlist
   * @return
   */
  public String createFfmpegPlaylist(List<String> playlist) {
    playlist.forEach(element -> this.ffmpegFile += "file '" + element + "' \n");
    return this.ffmpegFile;
  }

  /**
   *
   * @param playlist
   * @throws IOException
   */
  public void writeToFile(String playlist)
      throws IOException {
    Path path = Paths.get(this.playlistPath);
    Files.write(path, playlist.getBytes(),
        StandardOpenOption.CREATE,
        StandardOpenOption.TRUNCATE_EXISTING);
  }

  /**
   *
   * @param filename
   * @throws InterruptedException
   */
  public void generateVideo(String filename) throws InterruptedException {
    String location = this.basePath + filename + ".mkv";
    this.compilationLocation = location;
    this.execute(
        "/usr/local/bin/ffmpeg -y -f concat -safe 0 -i " + this.playlistPath + " -c copy "
            + this.compilationLocation);
  }

  /**
   *
   * @param name
   * @return
   * @throws InterruptedException
   */
  public boolean generateGif(String name) throws InterruptedException {
    String palette = this.paletteLocation + "palette.png";
    String gifName = name.isEmpty() ? "output.gif" : name;
    String gif = this.gifLocation + gifName + ".gif";
    this.gifLocation = gif;
    Path path = Paths.get(this.compilationLocation);
    if (Files.exists(path) && ! Files.isDirectory(path)) {
      String cmdPalette = "/usr/local/bin/ffmpeg -y -t 3 -ss 2.6 -i " + this.compilationLocation
          + " -vf fps=15,scale=400:-1:flags=lanczos,palettegen " + palette;
      String gifCmd = "/usr/local/bin/ffmpeg -y -i " + this.compilationLocation + " -i " + palette
          + " -filter_complex fps=15,scale=400:-1:flags=lanczos[x];[x][1:v]paletteuse " + gif;
      this.execute(cmdPalette);
      this.execute(gifCmd);
      this.gifLocation = this.basePath;
      return true;
    } else {
      return false;

    }
  }

  /**
   *
   * @param input
   * @param output
   * @return
   * @throws InterruptedException
   */
  public String generateVignette(String input, String output) throws InterruptedException {
    String command = "/usr/local/bin/ffmpeg -y -i " + input
        + " -vf scale=300x200 -r 1 -t 00:00:01 -ss 00:00:02  -f image2 "
        + this.vignetteLocation + output + ".jpg";
    this.execute(command);
    System.out.println(command);
    return this.vignetteLocation + output + ".jpg";
  }

  /**
   *
   * @return
   */
  public String getGifLocation() {
    return this.gifLocation;
  }

  /**
   *
   * @return
   */
  public String getOutputLocation() {
    return this.compilationLocation;
  }


  /**
   *
   * @param medias
   * @return
   */
  public List<String> generateVariant(EList<Media> medias) {
    List<String> variant = new ArrayList<>();
    medias.forEach(media -> {
      variant.add(this.utils.getIdOfVseq(this.utils.renderVseq(media)));
      variant.remove("");
    });

    return variant;
  }

  /**
   *
   * @return
   */
  public String getOutputPath() {
    return this.outputBasePath;
  }

  /**
   *
   * @param command
   * @throws InterruptedException
   */
  public void execute(String command) throws InterruptedException {
    //System.out.println(command);
    try {
      Process proc = Runtime.getRuntime().exec(command);
      int exitValue = proc.waitFor();
      System.out.println("exit value: " + exitValue);
    } catch (Throwable t) {
      t.printStackTrace();
    }
  }


}
