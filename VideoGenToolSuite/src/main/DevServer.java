package main;
import static spark.Spark.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.xtext.example.mydsl.videoGen.Media;

import spark.Request;
import spark.Response;

public class DevServer {
	private VideoSeqUtils utils = new VideoSeqUtils();
	private EList<Media> medias = new VideoGenGenerator().getMedias("example1.videogen");
	private FfmpegEngine ffmpeg = new FfmpegEngine();
	private Analysis analysis = new Analysis();

	private String port = "8080";
	private String resDataType = "application/json";
	private String resImageType = "image/gif";
	private String resMediaType = "video/mkv";
	private String resPngType = "image/png";
	
	public String getAutoGenerate(Request req, Response res) throws FileNotFoundException, UnsupportedEncodingException {
		res.type(this.resDataType);
		List<String> playlist = this.ffmpeg.playListFiles(medias);
		String playlistFile = this.ffmpeg.createFfmpegPlaylist(playlist);
		this.ffmpeg.writeToFile(playlistFile);
		boolean doGenerate = Boolean.parseBoolean(req.queryParams("activate"));
		System.out.println(doGenerate);
		String filename = req.queryParams("filename");
		System.out.println(filename);
		this.ffmpeg.generateVideo(doGenerate, filename);
		return this.ffmpeg.getOutputLocation();
	}
	
	public String getVariantDuration(Request req, Response res) throws IOException, ParseException {
		res.type(this.resDataType);
		return this.analysis.getDuration();

	}
	public String getPossibleVariansAndSize(Request req, Response res) {
		res.type(this.resDataType);
		return this.analysis.getVariantsSize(medias, utils.getMediaIds(medias));
	}
}
