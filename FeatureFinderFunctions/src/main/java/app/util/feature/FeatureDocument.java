package app.util.feature;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import app.util.feature.Feature;

import org.springframework.util.StreamUtils;

public class FeatureDocument extends Document  {
	
    public FeatureDocument() {
		super();
	}

	public FeatureDocument(Integer id, String name, String type, String contents, String description) {
		this.id=id;
		this.name=name;
		this.type=type;
		this.contents=contents;
		this.description=description;
	}
	
	public FeatureDocument(Integer id, String name, String type, InputStream contentsStream, String description) {
		this.id=id;
		this.name=name;
		this.type=type;
		this.description=description;
		ByteArrayOutputStream arrayOutputStream;
		arrayOutputStream = new ByteArrayOutputStream();
		try {
		      StreamUtils.copy(contentsStream, arrayOutputStream);
		      this.contents = arrayOutputStream.toString(StandardCharsets.UTF_8.toString());
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}
	
	public String toString() {
		return "{\"id\":\""+id+"\",\"name\":\""+name+"\",\"type\":\""+type+"\",\"description\":\""+description+"\",\"contents\":\""+contents+"\"}";
	}
}

