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
   * @param {VideoSeq} vseq to process
   * @return {Boolean} true if vseq is an instance of {MandatoryVideoSeq}
   */
  public boolean isMandatory(VideoSeq vseq) {
    return vseq instanceof MandatoryVideoSeq;
  }

  /**
   *
   * @param {VideoSeq} vseq to process
   * @return {Boolean} true if vseq is an instance of {AlternativeVideoSeq}
   */
  public boolean isAlternative(VideoSeq vseq) {
    return vseq instanceof AlternativeVideoSeq;
  }

  /**
   *
   * @param {VideoSeq} vseq to process
   * @return {Boolean} true if vseq is an instance of {OptionalVideoSeq}
   */
  public boolean isOptional(VideoSeq vseq) {
    return vseq instanceof OptionalVideoSeq;
  }

  /**
   *
   * @param {Media} media to process
   * @return {Boolean} true if media is an not an instance of {Image}, false otherwise
   */
  public boolean isVideoSeq(Media media) {
    return media instanceof Image ? false : true;
  }

  /**
   *
   * @param {Media} media to process
   * @return {VideoSeq} a videoseq Object
   */
  public VideoSeq renderVseq(Media media) {
    return isVideoSeq(media) ? (VideoSeq) media : (VideoSeq) null;
  }


  /**
   *
   * @param {Integer} u
   * @return {Integer} the factorial of a number
   */
  public int factorialOf(int u) {
    return u == 1 || u == 0 ? 1 : u * factorialOf(u - 1);
  }

  /**
   *
   * @param {Integer} n collection size
   * @param {Integer} k number of elements selected
   * @return {Integer} result from the combination formula
   */
  public int nOverK(int n, int k) {
    return (int) factorialOf(n) / (factorialOf(k) * factorialOf(n - k));
  }


  /**
   *
   * @param {VideoSeq} vseq to process
   * @return {String} path to a {MandatoryVideoSeq}
   */
  public String getMandatoryLocation(VideoSeq vseq) {
    return ((MandatoryVideoSeq) vseq).getDescription().getLocation();
  }

  /**
   *
   * @param {VideoSeq} vseq to process
   * @return {String} id of a {MandatoryVideoSeq}
   */
  public String getMandatoryId(VideoSeq vseq) {
    return ((MandatoryVideoSeq) vseq).getDescription().getVideoid();
  }

  /**
   *
   * @param {VideoSeq} vseq to process
   * @return {String} id of a {OptionalVideoSeq}
   */
  public String getOptionalId(VideoSeq vseq) {
    return ((OptionalVideoSeq) vseq).getDescription().getVideoid();
  }

  /**
   *
   * @param {VideoSeq} vseq to process
   * @return {String} random id of a {OptionalVideoSeq}
   */
  public String getRandomOptionalId(VideoSeq vseq) {
    return new Random().nextBoolean() ? ((OptionalVideoSeq) vseq).getDescription().getVideoid()
        : "";
  }

  /**
   *
   * @param {VideoSeq} vseq to process
   * @param {Integer} index position of an element 
   * @return {String} id of a {AlternativeVideoSeq}
   */
  public String getAlternativeId(VideoSeq vseq, int index) {
    EList<VideoDescription> videodesc = ((AlternativeVideoSeq) vseq).getVideodescs();
    return videodesc.get(index).getVideoid();
  }

  /**
   *
   * @param {VideoSeq} vseq to process
   * @return {String} Random id of a {AlternativeVideoSeq}
   */
  public String getRandomAlternativeId(VideoSeq vseq) {
    EList<VideoDescription> videodesc = ((AlternativeVideoSeq) vseq).getVideodescs();
    int random = new Random().nextInt(videodesc.size());
    return videodesc.get(random).getVideoid();
  }

  /**
   *
   * @param {VideoSeq} vseq to process
   * @return {Integer} Size of an {AlternativeVideoSeq}
   */
  public int getAlternativeSize(VideoSeq vseq) {
    EList<VideoDescription> videodesc = ((AlternativeVideoSeq) vseq).getVideodescs();
    return videodesc.size();
  }

  /**
   *
   * @param {VideoSeq} vseq to process
   * @return {String} path to a random {OptionalVideoSeq}
   */
  public String getOptionalLocation(VideoSeq vseq) {
    return new Random().nextBoolean() ? ((OptionalVideoSeq) vseq).getDescription().getLocation()
        : "";
  }

  /**
   *
   * @param {VideoSeq} vseq to process
   * @return {String} path to an {OptionalVideoSeq}
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
   * @param {VideoSeq} vseq to process
   * @return {String} path to a random {AlternativeVideoSeq}
   */
  public String getRandomAlternativeLocation(VideoSeq vseq) {
    EList<VideoDescription> videodesc = ((AlternativeVideoSeq) vseq).getVideodescs();
    int random = new Random().nextInt(videodesc.size());
    return videodesc.get(random).getLocation();
  }

  /**
   *
   * @param {VideoSeq} vseq to process
   * @return {String} path to a {VideoSeq}
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
   * @param {EList} medias in a Videogen file
   * @return {Integer} number of {OptionalVideoSeq} in a file
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
   * @param {EList} medias in a Videogen file
   * @return {Integer} number of {AlternativeVideoSeq} in a file
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
   * @param {EList} medias in a Videogen file
   * @return {Integer} 1 if {MandatoryVideoSeq} is present, 0 otherwise
   */
  public int isMandatoryPresent(EList<Media> medias) {
    boolean isTypePresent = false;
    int collectionSize = medias.size();
    int u = 0;
    int o = 0;
    while (o < collectionSize && !isTypePresent) {
      VideoSeq vseq = this.renderVseq(medias.get(o));
      isTypePresent = this.isMandatory(vseq);
      isTypePresent = this.isOptional(vseq);
      u = isTypePresent ? 1 : 0;
      o += 1;
    }
    return u;
  }

  /**
   *
   * @param {EList} medias in a Videogen file
   * @return {Integer} 1 if {OptionalVideoSeq} is present, 0 otherwise
   */
  public int isOptionalPresent(EList<Media> medias) {
    boolean isTypePresent = false;
    int collectionSize = medias.size();
    int u = 0;
    int o = 0;
    while (o < collectionSize && !isTypePresent) {
        VideoSeq vseq = this.renderVseq(medias.get(o));
        isTypePresent = this.isOptional(vseq);
        isTypePresent = this.isOptional(vseq);
        u = isTypePresent ? 1 : 0;
        o += 1;
      }
    return u;
  }

  /**
   *
   * @param {EList} medias in a Videogen file
   * @return {Integer} 1 if {AlternativeVideoSeq} is present, 0 otherwise
   */
  public int isAlternativePresent(EList<Media> medias) {
    boolean isTypePresent = false;
    int collectionSize = medias.size();
    int u = 0;
    int o = 0;
    while (o < collectionSize && !isTypePresent) {
        VideoSeq vseq = this.renderVseq(medias.get(o));
        isTypePresent = this.isAlternative(vseq);
        isTypePresent = this.isAlternative(vseq);
        u = isTypePresent ? 1 : 0;
        o += 1;
      }
    return u;
  }


  /**
   * 
   * @param {EList} medias in a Videogen file
   * @return {String} the number of possible variants
   */
  public int nOfvariants(EList<Media> medias) {
    int nm = this.getAlternativeSize(medias) + this.getOptionalSize(medias) + 1;
    int kt = this.isAlternativePresent(medias) + this.isOptionalPresent(medias);
    return 1 + this.nOverK(nm, kt);
  }

  /**
   *
   * @param {VideoSeq} vseq to process
   * @return {String} identifier of a {VideoSeq}
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
   * @param {EList} medias in a Videogen file
   * @return {List} collection of all {Media} Ids
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
   * @param {String} basePath working directory
   * @param {List} variants is combination of different {Media} files
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
