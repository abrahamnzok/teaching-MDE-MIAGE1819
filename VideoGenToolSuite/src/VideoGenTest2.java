import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
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

public class VideoGenTest2 {

	@Test
	public void VideGenTestffmpeg() throws FileNotFoundException, UnsupportedEncodingException {
		VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI("example1.videogen"));
		assertNotNull(videoGen);		
		System.out.println(videoGen.getInformation().getAuthorName());		
		assertEquals(4,videoGen.getMedias().size());	
		Random rand = new Random();
		String playlist = "";
		EList<Media> medias = videoGen.getMedias();
		List<String> mediaMKV = new ArrayList<>();
		for(Media media: medias) {
			if(media instanceof Image) {
				
			} else if (media instanceof VideoSeq) {
				VideoSeq vseq = (VideoSeq) media;
				if (vseq instanceof MandatoryVideoSeq) {
					String location = ((MandatoryVideoSeq) vseq).getDescription().getLocation();
					mediaMKV.add(location);
					//playlist += "file '" + location + "'" + "\n"; 
				} else if (vseq instanceof OptionalVideoSeq) {
					int nombreAleatoire = rand.nextInt(2);
					if(nombreAleatoire == 1) {
						String location = ((OptionalVideoSeq) vseq).getDescription().getLocation();
						mediaMKV.add(location);
						//playlist += "file '" + location + "'"+ "\n"; 
					}
				} else if (vseq instanceof AlternativeVideoSeq) {
					EList<VideoDescription> videodesc= ((AlternativeVideoSeq) vseq).getVideodescs();
					int nombreAleatoire = rand.nextInt(videodesc.size());
					//playlist += "file '" + videodesc.get(nombreAleatoire).getLocation() + "'" + "\n";
					mediaMKV.add(videodesc.get(nombreAleatoire).getLocation());
				}
			}
		}
		
		for(String input: mediaMKV) {
			String output = input.replaceAll(".mp4", ".mkv");
			playlist += "file '" + output + "'" + "\n";
			String cmd = "/usr/local/bin/ffmpeg -i " + input + " -c:a copy -c:v copy " + output;
			try {
				Process p = Runtime.getRuntime().exec(cmd);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		PrintWriter writer = new PrintWriter("/Users/Abraham/Desktop/Pexels/playlist.txt", "UTF-8");
		writer.println(playlist);
		writer.close();
		String cmd = "/usr/local/bin/ffmpeg -f concat -safe 0 -i /Users/Abraham/Desktop/Pexels/playlist.txt -c copy /Users/Abraham/Desktop/Pexels/fmmpegOutput.mkv";
		try {
			Process p = Runtime.getRuntime().exec(cmd);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
