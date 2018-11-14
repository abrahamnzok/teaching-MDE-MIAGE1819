import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import org.xtext.example.mydsl.videoGen.Media;

public class FfmpegEngine {

  private String playlist = "";

  public String createPlaylist(VideoGenGenerator generator, String uri) {
    VideoSeqUtils utils = new VideoSeqUtils();
    for (Media media : generator.getMedias(uri)) {
      this.playlist += "file '" + utils.getLocationOfVseq(utils.renderVseq(media)) + "'" + "\n";
    }
    return this.playlist;
  }

  public void writeToFile(String playlist) throws FileNotFoundException, UnsupportedEncodingException {
    PrintWriter writer = new PrintWriter("/Users/Abraham/Desktop/Pexels/playlist.txt", "UTF-8");
    writer.println(playlist);
    writer.close();
  }

  public void generateVideo(boolean doGenerate) {
    if (doGenerate) {

    }
  }
  
  public void autoGenerate (boolean doGenerate) {
	  if(doGenerate) {
		  
	  }
  }

}
