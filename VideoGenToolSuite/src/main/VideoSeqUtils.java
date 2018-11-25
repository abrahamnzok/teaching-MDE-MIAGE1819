package main;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.FileUtils;
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

  public String getMandatoryLocation(VideoSeq vseq) {
    return ((MandatoryVideoSeq) vseq).getDescription().getLocation();
  }

  public String getMandatoryId(VideoSeq vseq) {
    return ((MandatoryVideoSeq) vseq).getDescription().getVideoid();
  }

  public String getOptionalId(VideoSeq vseq) {
    return ((OptionalVideoSeq) vseq).getDescription().getVideoid();
  }

  public String getRandomOptionalId(VideoSeq vseq) {
    return new Random().nextInt(2) == 1 ? ((OptionalVideoSeq) vseq).getDescription().getVideoid()
        : "";
  }

  public String getAlternativeId(VideoSeq vseq, int index) {
    EList<VideoDescription> videodesc = ((AlternativeVideoSeq) vseq).getVideodescs();
    return videodesc.get(index).getVideoid();
  }

  public String getRandomAlternativeId(VideoSeq vseq) {
    EList<VideoDescription> videodesc = ((AlternativeVideoSeq) vseq).getVideodescs();
    int random = new Random().nextInt(videodesc.size());
    return videodesc.get(random).getVideoid();
  }

  public int getAlternativeSize(VideoSeq vseq) {
    EList<VideoDescription> videodesc = ((AlternativeVideoSeq) vseq).getVideodescs();
    return videodesc.size();
  }

  public String getOptionalLocation(VideoSeq vseq) {
    return new Random().nextInt(2) == 1 ? ((OptionalVideoSeq) vseq).getDescription().getLocation()
        : "";
  }

  public String getAlternativeLocation(VideoSeq vseq) {
    EList<VideoDescription> videodesc = ((AlternativeVideoSeq) vseq).getVideodescs();
    int random = new Random().nextInt(videodesc.size());
    return videodesc.get(random).getLocation();
  }

  public String getLocationOfVseq(VideoSeq vseq) {
    if (isAlternative(vseq)) {
      return this.getAlternativeLocation(vseq);
    } else if (this.isOptional(vseq)) {
      return this.getOptionalLocation(vseq);
    } else {
      return this.getMandatoryLocation(vseq);
    }
  }

  public String getIdOfVseq(VideoSeq vseq) {
    if (isAlternative(vseq)) {
      return this.getRandomAlternativeId(vseq);
    } else if (this.isOptional(vseq)) {
      return this.getRandomOptionalId(vseq);
    } else {
      return this.getMandatoryId(vseq);
    }
  }


  public List<String> getMediaIds(EList<Media> medias) {
    List<String> mediaIds = new ArrayList<>();
    for (Media media : medias) {
      VideoSeq vseq = this.renderVseq(media);
      if (isAlternative(vseq)) {
        int alternativeSize = this.getAlternativeSize(vseq);
        int index = 0;
        while (index < alternativeSize) {
          mediaIds.add(this.getAlternativeId(vseq, index));
          index += 1;
        }
      } else if (this.isOptional(vseq)) {
        mediaIds.add(this.getOptionalId(vseq));
      } else {
        mediaIds.add(this.getMandatoryId(vseq));
      }
    }
    return mediaIds;
  }

  public String getVariantSize(String basePath, List<String> variants) {
    long fileSize = 0;
    for (String varianteId : variants) {
      String location = basePath + varianteId + ".mkv";
      Path filePath = Paths.get(location);
      if (Files.exists(filePath) && ! Files.isDirectory(filePath)) {
        fileSize += new File(location).length();
      }
    }
    return FileUtils.byteCountToDisplaySize(fileSize);
  }

}
