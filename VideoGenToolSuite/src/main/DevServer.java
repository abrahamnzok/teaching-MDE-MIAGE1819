package main;

import static spark.Spark.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.eclipse.emf.common.util.EList;
import org.xtext.example.mydsl.videoGen.Media;

import com.google.gson.Gson;

import spark.Request;
import spark.Response;

public class DevServer {

  private VideoSeqUtils utils = new VideoSeqUtils();
  private EList<Media> medias = new VideoGenGenerator().getMedias("example1.videogen");
  private FfmpegEngine ffmpeg;
  private Analysis analysis = new Analysis();

  private String port = "8080";
  private String resDataType = "application/json";
  private String resImageType = "image/gif";
  private String resMediaType = "video/mkv";
  private String resPngType = "image/png";

  public Integer generate(Request req, Response res)
      throws IOException, InterruptedException {
    this.ffmpeg = new FfmpegEngine();
    boolean autogenerate = Boolean.parseBoolean(req.queryParams("autogen"));
    String userPlaylist = req.body();
    List<String> playlist =
        autogenerate ? this.ffmpeg.playListFiles(medias) : this.ffmpeg.playListFiles(userPlaylist);
    System.out.println(playlist);    
    String playlistFile = this.ffmpeg.createFfmpegPlaylist(playlist);
    this.ffmpeg.writeToFile(playlistFile);
    String filename = req.queryParams("filename");
    this.ffmpeg.generateVideo(filename);
    Path file = Paths.get(this.ffmpeg.getOutputLocation());
    byte[] video = Files.readAllBytes(file);
    res.status(200);
    HttpServletResponse resp = res.raw();
    resp.setContentType("video/mkv");
    resp.getOutputStream().write(video);
    resp.getOutputStream().close();
    return 200;
  }

  public Integer getGif(Request req, Response res) throws InterruptedException, IOException {
    res.type(this.resImageType);
    String gifName = req.queryParams("gifname");
    boolean generate = this.ffmpeg.generateGif(gifName);
    if (generate) {
      Path file = Paths.get(this.ffmpeg.getGifLocation());
      byte[] gif = Files.readAllBytes(file);
      HttpServletResponse resp = res.raw();
      resp.setContentType("image/gif");
      resp.getOutputStream().write(gif);
      resp.getOutputStream().close();
      return resp.getStatus();
    } else {
      res.status(500);
      return res.status();
    }
  }

  public String getVariantDuration(Request req, Response res) throws IOException, ParseException {
    res.type(this.resDataType);
    return this.analysis.getDuration();

  }

  public String getPossibleVariansAndSize(Request req, Response res) {
    res.type(this.resDataType);
    return this.analysis.getVariantsSize(medias, utils.getMediaIds(medias));
  }
}
