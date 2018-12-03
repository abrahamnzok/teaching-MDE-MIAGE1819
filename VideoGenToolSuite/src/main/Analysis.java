package main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
import org.eclipse.emf.common.util.EList;
import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xtext.example.mydsl.videoGen.Media;

import com.google.gson.Gson;


public class Analysis {

  private String basePath = System.getProperty("user.dir") + "/Pexels/";
  private String command = "/usr/local/bin/ffprobe -v quiet -of csv=p=0 -show_entries format=duration ";
  private String duration = "";
  private VideoSeqUtils utils = new VideoSeqUtils();
  private FfmpegEngine engine = new FfmpegEngine();

  /**
   *
   * @param uri Path to compilation 
   * @return {String} the duration of the compiled variant
   * @throws IOException
   * @throws ParseException
   */
  public String getDuration(String uri) throws IOException, ParseException {
    Path path = Paths.get(uri);
    boolean fileExists = false;
    long durationLong = 0;
    if (Files.exists(path) && ! Files.isDirectory(path)) {
      // file exist
      fileExists = true;
      this.command += uri;
      Process p = Runtime.getRuntime().exec(this.command);
      try (Scanner sc = new Scanner(p.getInputStream())) {
        while (sc.hasNext()) {
          this.duration = sc.nextLine();
        }
      }
      durationLong = NumberFormat.getInstance().parse(this.duration).longValue();
    }
    return fileExists ? LocalTime.MIN.plusSeconds(durationLong).toString() : "0h:00min:00sec";
  }

  /**
   *
   * @param medias in a Videogen file
   * @param allMedias {List} containing all the medias Ids
   * @return {String} data about each variants combination and their sizes
 * @throws IOException 
   */
  public String getVariantsSize(EList<Media> medias, List<String> allMedias) throws IOException {
    List<Map> metadata = new ArrayList<>();
    List<String> variant = new ArrayList<>();
    List<List<String>> collector = new ArrayList<>();
    int nbOfVariants = this.utils.nOfvariants(medias);
    for (int i = 0; i < nbOfVariants; i++) {
      collector.add(variant);
      variant = this.engine.generateVariant(medias);
      boolean collectorSaysTrue = collector.contains(variant);
      if (collectorSaysTrue) {
        variant = this.engine.generateVariant(medias);
        collectorSaysTrue = collector.contains(variant);
      }
      Map<String, String> data = new HashMap<String, String>();
      data.put("id", String.valueOf(i));
      for (String element : allMedias) {
        data.put(element, String.valueOf(variant.contains(element)));
      }
      data.put("size", this.utils.getVariantSize(this.basePath, variant));
      metadata.add(data);
    }
    String data = new Gson().toJson(metadata);
    JSONArray docs = new JSONArray(data);
    File file=new File(this.basePath + "variants.csv");
    String csv = CDL.toString(docs);
    FileUtils.writeStringToFile(file, csv);
    return new Gson().toJson(metadata);
  }

}
