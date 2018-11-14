package main;
import java.util.Random;

import org.eclipse.emf.common.util.EList;
import org.xtext.example.mydsl.videoGen.AlternativeVideoSeq;
import org.xtext.example.mydsl.videoGen.Image;
import org.xtext.example.mydsl.videoGen.MandatoryVideoSeq;
import org.xtext.example.mydsl.videoGen.Media;
import org.xtext.example.mydsl.videoGen.OptionalVideoSeq;
import org.xtext.example.mydsl.videoGen.VideoDescription;
import org.xtext.example.mydsl.videoGen.VideoSeq;

public class VideoSeqUtils {

  public boolean isMandatory(VideoSeq vseq) {
    return vseq instanceof MandatoryVideoSeq;
  }

  public boolean isAlternative(VideoSeq vseq) {
    return vseq instanceof AlternativeVideoSeq;
  }

  public boolean isOptional(VideoSeq vseq) {
    return vseq instanceof OptionalVideoSeq;
  }
  
  public boolean isVideoSeq(Media media) {
	  return media instanceof Image ? false : true;
  }
  
  public VideoSeq renderVseq(Media media) {
	  return isVideoSeq(media) ? (VideoSeq) media : (VideoSeq) null;
  }
  
  public String getLocationOfVseq(VideoSeq vseq) {
    if (isAlternative(vseq)) {
      EList<VideoDescription> videodesc = ((AlternativeVideoSeq) vseq).getVideodescs();
      int random = new Random().nextInt(videodesc.size());
      return videodesc.get(random).getLocation();
    } else {
      return isMandatory(vseq) ? ((MandatoryVideoSeq) vseq).getDescription().getLocation()
          : ((OptionalVideoSeq) vseq).getDescription().getLocation();
    }
  }
  
}
