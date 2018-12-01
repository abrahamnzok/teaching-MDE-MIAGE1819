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

  /**
   *
   * @param vseq
   * @return
   */
  public boolean isMandatory(VideoSeq vseq) {
    return vseq instanceof MandatoryVideoSeq;
  }

  /**
   *
   * @param vseq
   * @return
   */
  public boolean isAlternative(VideoSeq vseq) {
    return vseq instanceof AlternativeVideoSeq;
  }

  /**
   *
   * @param vseq
   * @return
   */
  public boolean isOptional(VideoSeq vseq) {
    return vseq instanceof OptionalVideoSeq;
  }

  /**
   *
   * @param media
   * @return
   */
  public boolean isVideoSeq(Media media) {
    return media instanceof Image ? false : true;
  }

  /**
   *
   * @param media
   * @return
   */
  public VideoSeq renderVseq(Media media) {
    return isVideoSeq(media) ? (VideoSeq) media : (VideoSeq) null;
  }


  /**
   *
   * @param u
   * @return
   */
  public int factorialOf(int u) {
    return u == 1 || u == 0 ? 1 : u * factorialOf(u - 1);
  }

  /**
   *
   * @param n
   * @param k
   * @return
   */
  public int nOverK(int n, int k) {
    return (int) factorialOf(n) / (factorialOf(k) * factorialOf(n - k));
  }


  /**
   *
   * @param vseq
   * @return
   */
  public String getMandatoryLocation(VideoSeq vseq) {
    return ((MandatoryVideoSeq) vseq).getDescription().getLocation();
  }

  /**
   *
   * @param vseq
   * @return
   */
  public String getMandatoryId(VideoSeq vseq) {
    return ((MandatoryVideoSeq) vseq).getDescription().getVideoid();
  }

  /**
   *
   * @param vseq
   * @return
   */
  public String getOptionalId(VideoSeq vseq) {
    return ((OptionalVideoSeq) vseq).getDescription().getVideoid();
  }

  /**
   *
   * @param vseq
   * @return
   */
  public String getRandomOptionalId(VideoSeq vseq) {
    return new Random().nextBoolean() ? ((OptionalVideoSeq) vseq).getDescription().getVideoid()
        : "";
  }

  /**
   *
   * @param vseq
   * @param index
   * @return
   */
  public String getAlternativeId(VideoSeq vseq, int index) {
    EList<VideoDescription> videodesc = ((AlternativeVideoSeq) vseq).getVideodescs();
    return videodesc.get(index).getVideoid();
  }

  /**
   *
   * @param vseq
   * @return
   */
  public String getRandomAlternativeId(VideoSeq vseq) {
    EList<VideoDescription> videodesc = ((AlternativeVideoSeq) vseq).getVideodescs();
    int random = new Random().nextInt(videodesc.size());
    return videodesc.get(random).getVideoid();
  }

  /**
   *
   * @param vseq
   * @return
   */
  public int getAlternativeSize(VideoSeq vseq) {
    EList<VideoDescription> videodesc = ((AlternativeVideoSeq) vseq).getVideodescs();
    return videodesc.size();
  }

  /**
   *
   * @param vseq
   * @return
   */
  public String getOptionalLocation(VideoSeq vseq) {
    return new Random().nextBoolean() ? ((OptionalVideoSeq) vseq).getDescription().getLocation()
        : "";
  }

  /**
   *
   * @param vseq
   * @return
   */
  public String getEachOptionalLocation(VideoSeq vseq) {
    return ((OptionalVideoSeq) vseq).getDescription().getLocation();
  }

  public String getAlternativeLocation(VideoSeq vseq, int index) {
    EList<VideoDescription> videodesc = ((AlternativeVideoSeq) vseq).getVideodescs();
    return videodesc.get(index).getLocation();
  }

  /**
   *
   * @param vseq
   * @return
   */
  public String getRandomAlternativeLocation(VideoSeq vseq) {
    EList<VideoDescription> videodesc = ((AlternativeVideoSeq) vseq).getVideodescs();
    int random = new Random().nextInt(videodesc.size());
    return videodesc.get(random).getLocation();
  }

  /**
   *
   * @param vseq
   * @return
   */
  public String getLocationOfVseq(VideoSeq vseq) {
    if (isAlternative(vseq)) {
      return this.getRandomAlternativeLocation(vseq);
    } else if (this.isOptional(vseq)) {
      return this.getOptionalLocation(vseq);
    } else {
      return this.getMandatoryLocation(vseq);
    }
  }


  /**
   *
   * @param medias
   * @return
   */
  public int getOptionalSize(EList<Media> medias) {
    int i = 0;
    for (Media media : medias) {
      if (this.isOptional(this.renderVseq(media))) {
        i += 1;
      }
    }
    return i;
  }

  /**
   *
   * @param medias
   * @return
   */
  public int getAlternativeSize(EList<Media> medias) {
    int i = 0;
    for (Media media : medias) {
      VideoSeq vseq = this.renderVseq(media);
      if (this.isAlternative(vseq)) {
        EList<VideoDescription> videodesc = ((AlternativeVideoSeq) vseq).getVideodescs();
        i += videodesc.size();
      }
    }
    return i > 0 ? i : 0;
  }

  /**
   *
   * @param medias
   * @return
   */
  public int isMandatoryPresent(EList<Media> medias) {
    boolean isTypePresent = false;
    int collectionSize = medias.size();
    int u = 0;
    for (int i = 0; i < collectionSize; i++) {
      VideoSeq vseq = this.renderVseq(medias.get(i));
      isTypePresent = this.isMandatory(vseq);
      u = isTypePresent ? 1 : 0;
    }
    return u;
  }

  /**
   *
   * @param medias
   * @return
   */
  public int isOptionalPresent(EList<Media> medias) {
    boolean isTypePresent = false;
    int collectionSize = medias.size();
    int u = 0;
    for (int i = 0; i < collectionSize; i++) {
      VideoSeq vseq = this.renderVseq(medias.get(i));
      isTypePresent = this.isOptional(vseq);
      u = isTypePresent ? 1 : 0;
    }
    return u;
  }

  /**
   *
   * @param medias
   * @return
   */
  public int isAlternativePresent(EList<Media> medias) {
    boolean isTypePresent = false;
    int collectionSize = medias.size();
    int u = 0;
    for (int i = 0; i < collectionSize; i++) {
      VideoSeq vseq = this.renderVseq(medias.get(i));
      isTypePresent = this.isAlternative(vseq);
      u = isTypePresent ? 1 : 0;
    }
    return u;
  }


  /**
   *
   * @param nbOfAlternatives
   * @param nbOfOptionals
   * @return
   */
  public int nOfvariants(EList<Media> medias) {
    int nm = this.getAlternativeSize(medias) + this.getOptionalSize(medias) + 1;
    int kt = this.isAlternativePresent(medias) + this.isOptionalPresent(medias) + this
        .isMandatoryPresent(medias);
    return 1 + this.nOverK(nm, kt);
  }

  /**
   *
   * @param vseq
   * @return
   */
  public String getIdOfVseq(VideoSeq vseq) {
    if (isAlternative(vseq)) {
      return this.getRandomAlternativeId(vseq);
    } else if (this.isOptional(vseq)) {
      return this.getRandomOptionalId(vseq);
    } else {
      return this.getMandatoryId(vseq);
    }
  }


  /**
   *
   * @param medias
   * @return
   */
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

  /**
   *
   * @param basePath
   * @param variants
   * @return
   */
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
