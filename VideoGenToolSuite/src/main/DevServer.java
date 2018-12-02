package main;

import static spark.Spark.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
  private Analysis analysis;

  private String port = "8080";
  private String resDataType = "application/json";
  private String resImageType = "image/gif";
  private String resMediaType = "video/mkv";
  private String resPngType = "image/png";

  /**
   *
   * @param req
   * @param res
   * @return
   * @throws InterruptedException
   * @throws IOException
   */
  public String getMedias(Request req, Response res) throws InterruptedException, IOException {
    res.type(this.resDataType);
    this.ffmpeg = new FfmpegEngine();
    List<Map<String, String>> data = this.ffmpeg.mediaInformation(medias);
    return new Gson().toJson(data);
  }

  /**
   *
   * @param req
   * @param res
   * @return
   * @throws IOException
   * @throws InterruptedException
   */
  public String generate(Request req, Response res)
      throws IOException, InterruptedException {
    boolean autogenerate = Boolean.parseBoolean(req.queryParams("autogen"));
    String userPlaylist = req.body();
    this.ffmpeg = new FfmpegEngine();
    List<String> playlist =
        autogenerate ? this.ffmpeg.playListFiles(medias)
            : userPlaylist.isEmpty() ? this.ffmpeg.playListFiles(medias) : 
            	this.ffmpeg.playListFiles(userPlaylist);
    String playlistFile = this.ffmpeg.createFfmpegPlaylist(playlist);
    this.ffmpeg.writeToFile(playlistFile);
    String filename = req.queryParams("filename");
    this.ffmpeg.generateVideo(filename);
    res.status(200);
    res.type(resDataType);
    return this.ffmpeg.getOutputLocation();
  }


  /**
   *
   * @param req
   * @param res
   * @return
   * @throws InterruptedException
   * @throws IOException
   */
  public int getGif(Request req, Response res) throws InterruptedException, IOException {
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

  /**
   *
   * @param req
   * @param res
   * @return
   * @throws IOException
   * @throws ParseException
   */
  public String getVariantDuration(Request req, Response res) throws IOException, ParseException {
    this.analysis = new Analysis();
    res.type(this.resDataType);
    return new Gson().toJson(this.analysis.getDuration(this.ffmpeg.getOutputLocation()));

  }

  /**
   *
   * @param req
   * @param res
   * @return
 * @throws IOException 
   */
  public String getPossibleVariansAndSize(Request req, Response res) throws IOException {
    this.analysis = new Analysis();
    res.type(this.resDataType);
    return this.analysis.getVariantsSize(this.medias, this.utils.getMediaIds(medias));
  }
}
