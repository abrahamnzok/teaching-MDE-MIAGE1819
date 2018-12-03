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
  private String outputBasePath = System.getProperty("user.dir") + "/Pexels/";
  private String compilationLocation = this.outputBasePath;
  private String playlistPath = this.outputBasePath + "playlist.txt";
  private String basePath = this.outputBasePath;
  private VideoSeqUtils utils = new VideoSeqUtils();
  private String gifLocation = this.outputBasePath;
  private String paletteLocation = this.outputBasePath;
  private String vignetteLocation = this.outputBasePath;

  /**
   * 
   * @param medias in a Videogen file
   * @return {List} of map containing information about each media
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
        String medianame = this.utils.getMandatoryId(vseq);
        String location = this.utils.getMandatoryLocation(vseq);
        vignette = this.generateVignette(location,
            medianame);
        path = Paths.get(vignette);
        imgBase64 = Base64.getEncoder().encodeToString(Files.readAllBytes(path));
        information.put("medianame", medianame);
        information.put("mediatype", "mandatory");
        information.put("mediasrc", location);
        information.put("src", "data:image/jpeg;base64," + imgBase64);
        mediaSetInformation.add(information);
      } else if (this.utils.isOptional(vseq)) {
        information = new HashMap<String, String>();
        String medianame = this.utils.getOptionalId(vseq);
        String location = this.utils.getEachOptionalLocation(vseq);
        vignette = this.generateVignette(location,
            medianame);
        vignette = this
            .generateVignette(location, medianame);
        path = Paths.get(vignette);
        imgBase64 = Base64.getEncoder().encodeToString(Files.readAllBytes(path));
        information.put("medianame", medianame);
        information.put("mediatype", "optional");
        information.put("mediasrc", location);
        information.put("src", "data:image/jpeg;base64, " + imgBase64);
        mediaSetInformation.add(information);
      } else if (this.utils.isAlternative(vseq)) {
          for (int index = 0; index < this.utils.getAlternativeSize(vseq); index++) {
        	  information = new HashMap<>();
        	  String medianame = this.utils.getAlternativeId(vseq, index);
        	  String alternativeLocation = this.utils.getAlternativeLocation(vseq, index);
              vignette = this.generateVignette(alternativeLocation, medianame);
              path = Paths.get(vignette);
              imgBase64 = Base64.getEncoder().encodeToString(Files.readAllBytes(path));
              information.put("medianame", medianame);
              information.put("mediatype", "alternative");
              information.put("mediasrc", alternativeLocation);
              information.put("src", "data:image/jpeg;base64, " + imgBase64);
              mediaSetInformation.add(information);
          }
        }
    }
    return mediaSetInformation;
  }
  

  /**
   *
   * @param medias in a Videogen file
   * @return {List} with the files which will be compiled by ffmpeg
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
   * @param {String} userPlaylist 
   * @return {List} with the files chosen by a user
   */
  public List<String> playListFiles(String userPlaylist) {
    return new Gson().fromJson(userPlaylist, List.class);
  }

  /**
   *
   * @param {List} playlist containing different video sequence path
   * @return {String} a single unit which store information for ffmpeg
   */
  public String createFfmpegPlaylist(List<String> playlist) {
    playlist.forEach(element -> this.ffmpegFile += "file '" + element + "' \n");
    return this.ffmpegFile;
  }

  /**
   *
   * @param {String} playlist to write to a file
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
   * @param {String} filename given to the compiled video
   * This methods generates a video sequence from the given ffmpeg playlist
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
   * @param {String} name given to the generated gif
   * @return {Boolean} true if there is a compiled video, false otherwise
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
   * @param {String} input location of a media
   * @param {String} output location of the vignette
   * @return
   * @throws InterruptedException
   */
  public String generateVignette(String input, String output) throws InterruptedException {
    String command = "/usr/local/bin/ffmpeg -y -i " + input
        + " -vf scale=300x200 -r 1 -t 00:00:01 -ss 00:00:02  -f image2 "
        + this.vignetteLocation + output + ".jpg";
    this.execute(command);
    return this.vignetteLocation + output + ".jpg";
  }

  /**
   *
   * @return {String} location of the gif
   */
  public String getGifLocation() {
    return this.gifLocation;
  }

  /**
   *
   * @return {String} location of the compiled video
   */
  public String getOutputLocation() {
    return this.compilationLocation;
  }


  /**
   *
   * @param medias in a Videogen file
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
   * @return {String} the output location of all the project files
   */
  public String getOutputPath() {
    return this.outputBasePath;
  }

  /**
   *
   * @param {String} command to be executed
   * @throws InterruptedException
   */
  public void execute(String command) throws InterruptedException {
    try {
      Process proc = Runtime.getRuntime().exec(command);
      int exitValue = proc.waitFor();
    } catch (Throwable t) {
      t.printStackTrace();
    }
  }


}
