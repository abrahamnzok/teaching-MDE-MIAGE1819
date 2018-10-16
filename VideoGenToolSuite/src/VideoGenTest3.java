import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.junit.Test;
import org.xtext.example.mydsl.videoGen.AlternativeVideoSeq;
import org.xtext.example.mydsl.videoGen.Image;
import org.xtext.example.mydsl.videoGen.MandatoryVideoSeq;
import org.xtext.example.mydsl.videoGen.Media;
import org.xtext.example.mydsl.videoGen.OptionalVideoSeq;
import org.xtext.example.mydsl.videoGen.VideoDescription;
import org.xtext.example.mydsl.videoGen.VideoGeneratorModel;
import org.xtext.example.mydsl.videoGen.VideoSeq;

public class VideoGenTest3 {

	@Test
	public void tp3() {
		VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI("example1.videogen"));
		assertNotNull(videoGen);		
		System.out.println(videoGen.getInformation().getAuthorName());		
		assertEquals(4,videoGen.getMedias().size());
		Random rand = new Random();
		EList<Media> medias = videoGen.getMedias();
		List<String> aSequence;
		List<List<String>> allSequences = new ArrayList<>();
		StringBuilder fileCSV = new StringBuilder();
		boolean isVariant ;
		String baseURI = "/Users/Abraham/Desktop/Pexels";
		List<String> allVideoSeq = new ArrayList<>();
		
		for(Media media: medias) {
			if(media instanceof Image) {
				
			} else if (media instanceof VideoSeq) {
				VideoSeq vseq = (VideoSeq) media;
				if (vseq instanceof MandatoryVideoSeq) {
					String videoId = ((MandatoryVideoSeq) vseq).getDescription().getVideoid();
					allVideoSeq.add(videoId);
				} else if (vseq instanceof OptionalVideoSeq) {
					int nombreAleatoire = rand.nextInt(2);
					if(nombreAleatoire == 1) {
						String videoId = ((OptionalVideoSeq) vseq).getDescription().getVideoid();
						allVideoSeq.add(videoId);
					}else {
						String videoId = ((OptionalVideoSeq) vseq).getDescription().getVideoid();
						allVideoSeq.add(videoId);
					}
				} else if (vseq instanceof AlternativeVideoSeq) {
					EList<VideoDescription> videodesc= ((AlternativeVideoSeq) vseq).getVideodescs();
					for(VideoDescription vid : videodesc) {
						allVideoSeq.add(vid.getVideoid());
					}
				}
			}
		}
				
		for(int i= 0; i < medias.size(); i++) {
			aSequence = new ArrayList<>();
			for(Media media: medias) {
				if(media instanceof Image) {
					
				} else if (media instanceof VideoSeq) {
					VideoSeq vseq = (VideoSeq) media;
					if (vseq instanceof MandatoryVideoSeq) {
						String videoId = ((MandatoryVideoSeq) vseq).getDescription().getVideoid();
						aSequence.add(videoId);
					} else if (vseq instanceof OptionalVideoSeq) {
						int nombreAleatoire = rand.nextInt(2);
						if(nombreAleatoire == 1) {
							String videoId = ((OptionalVideoSeq) vseq).getDescription().getVideoid();
							aSequence.add(videoId);
						}
					} else if (vseq instanceof AlternativeVideoSeq) {
						EList<VideoDescription> videodesc= ((AlternativeVideoSeq) vseq).getVideodescs();
						int nombreAleatoire = rand.nextInt(videodesc.size());
						aSequence.add(videodesc.get(nombreAleatoire).getVideoid());
					}
				}
			}
			allSequences.add(aSequence);
		}
		
		fileCSV.append("id").append("; ");
		for(int i = 0; i < allVideoSeq.size()-1; i++) {
			fileCSV.append(allVideoSeq.get(i)).append("; ");
		}
		fileCSV.append(allVideoSeq.get(allVideoSeq.size()-1)).append(";").append("\n");
		
		for(int i = 0; i < allSequences.size(); i++) {
			fileCSV.append(i).append("; ");
			for(String videoSeq : allVideoSeq) {
				isVariant = allSequences.get(i).contains(videoSeq);
				fileCSV.append(isVariant).append("; ");
			}
			fileCSV.append("\n");
		}
		
		System.out.println(fileCSV);
		System.out.println(allSequences);
	}

}
