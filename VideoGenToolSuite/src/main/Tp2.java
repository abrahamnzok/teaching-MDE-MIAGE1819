package main;
import static org.junit.Assert.*;
import static spark.Spark.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.junit.Test;
import gen.VideoGenHelper;
import org.xtext.example.mydsl.videoGen.AlternativeVideoSeq;
import org.xtext.example.mydsl.videoGen.Image;
import org.xtext.example.mydsl.videoGen.MandatoryVideoSeq;
import org.xtext.example.mydsl.videoGen.Media;
import org.xtext.example.mydsl.videoGen.OptionalVideoSeq;
import org.xtext.example.mydsl.videoGen.VideoDescription;
import org.xtext.example.mydsl.videoGen.VideoGeneratorModel;
import org.xtext.example.mydsl.videoGen.VideoSeq;

public class Tp2 {

	@Test
	public void VideGenTestffmpeg() throws IOException, ParseException {
		VideoSeqUtils utils = new VideoSeqUtils();
		EList<Media> medias = new VideoGenGenerator().getMedias("example1.videogen");
		FfmpegEngine ffmpeg = new FfmpegEngine();
		Analysis analysis = new Analysis();
		//System.out.println(analysis.getDuration());
		//System.out.println(ffmpeg.playListFiles(medias));
		//System.out.println(utils.getMediaIds(medias));
		List<String> variantes = ffmpeg.generateVariant(medias);
		//System.out.println(variantes);
		//System.out.println(utils.getVariantSize("/Users/Abraham/Desktop/Pexels/", variantes));
		System.out.println(analysis.getVariantsSize(medias, utils.getMediaIds(medias)));
		//System.out.println(analysis.getVariantsSize(medias, utils.getMediaIds(medias)));
		//analysis.getVariantSize();
		//analysis.getDuration();
	}
	
}
