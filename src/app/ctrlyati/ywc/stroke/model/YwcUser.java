package app.ctrlyati.ywc.stroke.model;

public class YwcUser {

	private String mId;
	private String mName;
	private String mSurname;
	private GroupColor mColor;
	private double mScore;
	
	public enum GroupColor {
		RED, BLUE, GREEN, ORANGE
	}

	public String getName() {
		return mName;
	}

	public void setName(String mName) {
		this.mName = mName;
	}

	public String getSurname() {
		return mSurname;
	}

	public void setSurname(String mSurname) {
		this.mSurname = mSurname;
	}

	public GroupColor getColor() {
		return mColor;
	}

	public void setColor(GroupColor mColor) {
		this.mColor = mColor;
	}

	public double getScore() {
		return mScore;
	}

	public void setScore(double mScore) {
		this.mScore = mScore;
	}
	
	public String getId() {
		return mId;
	}
	
	public void setId(String mId) {
		this.mId = mId;
	}
	
	
}
