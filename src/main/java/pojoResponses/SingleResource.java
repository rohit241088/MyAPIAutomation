package pojoResponses;

public class SingleResource {
private String data=null;
private String ad=null;
public String getData() {
	return data;
}
public void setData(String data) {
	this.data = data;
}
public String getAd() {
	return ad;
}
public void setAd(String ad) {
	this.ad = ad;
}
class data{
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getPantone_value() {
		return pantone_value;
	}
	public void setPantone_value(String pantone_value) {
		this.pantone_value = pantone_value;
	}
	private String id=null;
	private String name=null;
	private String year=null;
	private String color=null;
	private String pantone_value=null;
	
}
class ad{
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	private String company=null;
	private String url=null;
	private String text=null;
	
}
}
