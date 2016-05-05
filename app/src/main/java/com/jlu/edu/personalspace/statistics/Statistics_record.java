package com.jlu.edu.personalspace.statistics;

public class Statistics_record {
	private String usernumber;
	private String name;
	private String teacher;
	private String student;
	private int count;

	public String getUsernumber() {
		return usernumber;
	}

	public void setUsernumber(String usernumber) {
		this.usernumber = usernumber;
	}



	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTeacher() {
		return teacher;
	}

	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}

	public String getStudent() {
		return student;
	}

	public void setStudent(String student) {
		this.student = student;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "Statistics_record [username=" + usernumber + ", name=" + name
				+ ", teacher=" + teacher + ", student=" + student + ", count="
				+ count + "]";
	}

}
