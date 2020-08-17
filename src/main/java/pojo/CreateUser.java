package pojo;

public class CreateUser {

	private String name=null;
	private String job=null;
	
	public CreateUser(String name, String job) {
		this.name = name;
		this.job = job;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getName() {
		return name;
	}
	
	public String getJob() {
		return job;
	}
	
	
	
	
}
