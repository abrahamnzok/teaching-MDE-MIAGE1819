package main;
import gen.VideoGenHelper;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.xtext.example.mydsl.videoGen.Media;

public class VideoGenGenerator {
	private String uri = "example1.videogen";
	public EList<Media> getMedias(String uri){
		if(uri.isEmpty()) {
			this.uri = uri;
		}
		return new VideoGenHelper().loadVideoGenerator(URI.createURI(this.uri)).getMedias();
	}
	public EList<Media> getMedias(){
		return new VideoGenHelper().loadVideoGenerator(URI.createURI(this.uri)).getMedias();
	}
}
