package app.util.feature;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import app.util.feature.Feature;

import org.springframework.util.StreamUtils;

public class FeatureDocument implements Feature {
	private Integer id;
	private String contents;
	private String description;
	private String type;
	private String name;
	
    public FeatureDocument() {
		this.id=-1;
		this.name="";
		this.type="";
		this.contents="";
		this.description="";
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

	public void setId(Integer id) {
		this.id=id;
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public void setDescription(String description) {
		this.description=description;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public void setName(String name) {
		this.name=name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setType(String type) {
		this.type=type;
	}
	
	public String getType() {
		return this.type;
	}
	
	public void setContents(String contents) {
		this.contents=contents;
	}
	
	public String getContents() {
		return this.contents;
	}
	
	public String toString() {
		return "{\"id\":\""+id+"\",\"name\":\""+name+"\",\"type\":\""+type+"\",\"description\":\""+description+"\",\"contents\":\""+contents+"\"}";
	}
}

