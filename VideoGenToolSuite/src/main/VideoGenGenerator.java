package main;
import gen.VideoGenHelper;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.xtext.example.mydsl.videoGen.Media;

public class VideoGenGenerator {
	
	public EList<Media> getMedias(String uri){
		return new VideoGenHelper().loadVideoGenerator(URI.createURI(uri)).getMedias();
	}
}
