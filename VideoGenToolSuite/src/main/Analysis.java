package main;

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
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.xtext.example.mydsl.videoGen.Media;

import com.google.gson.Gson;



public class Analysis {

  private String basePath = "/Users/Abraham/Desktop/Pexels/";
  private String filePath = "/Users/Abraham/Desktop/Pexels/variante.mkv";
  private String command = "/usr/local/bin/ffprobe -v quiet -of csv=p=0 -show_entries format=duration ";
  private String duration = "";
  private VideoGenGenerator generator = new VideoGenGenerator();
  private VideoSeqUtils utils = new VideoSeqUtils();
  private FfmpegEngine engine = new FfmpegEngine();
  private List<String> mediaIds = new ArrayList<> ();

  public String getDuration() throws IOException, ParseException {
    Path path = Paths.get(filePath);
    boolean fileExists = false;
    if (Files.exists(path) && ! Files.isDirectory(path)) {
      // file exist
      fileExists = true;
      this.command += this.filePath;
      Process p = Runtime.getRuntime().exec(this.command);
      try (Scanner sc = new Scanner(p.getInputStream())) {
        while (sc.hasNext()) {
          this.duration = sc.nextLine();
        }
      }
      long durationLong = NumberFormat.getInstance().parse(this.duration).longValue();
      this.duration = LocalTime.MIN.plusSeconds(durationLong).toString();
    }
    return fileExists ? this.duration : "0h:00min:00sec";
  }
  
  public String getVariantsSize(EList<Media> medias, List<String> allMedias) {
	  List<Map> metadata = new ArrayList<>();
	  int u = 0;
	  for(int i = 0; i <= medias.size(); i++) {
		  List<String> variant = this.engine.generateVariant(medias);
		  Map<String, String> data =  new HashMap<String, String>();
		  data.put("id", String.valueOf(i));
		  allMedias.forEach(element -> 
			  data.put(element, String.valueOf(variant.contains(element)))
		  );
		  data.put("size", this.utils.getVariantSize(this.basePath,variant));
		  metadata.add(data);
	  }
	  return new Gson().toJson(metadata);
  }
}
