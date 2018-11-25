package main;
import static spark.Spark.*;

import java.io.IOException;
import java.text.ParseException;

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
	private String responseDataType = "application/json";
	private String respImageType = "image/gif";
	private String respPngType = "image/png";
	
	public String getVariantDuration(Request req, Response res) throws IOException, ParseException {
		res.type(responseDataType);
		return this.analysis.getDuration();

	}
	public String getPossibleVariansAndSize(Request req, Response res) {
		res.type(responseDataType);
		return this.analysis.getVariantsSize(medias, utils.getMediaIds(medias));
	}
}
