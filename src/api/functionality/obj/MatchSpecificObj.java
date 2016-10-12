package api.functionality.obj;

public class MatchSpecificObj {
	private String csvTxt;
	private MatchSpecificDescriptionData msl;
	private long compId;
	private int linesRead;

	public String getCsvTxt() {
		return csvTxt;
	}

	public void setCsvTxt(String csvTxt) {
		this.csvTxt = csvTxt;
	}

	public MatchSpecificDescriptionData getMsl() {
		return msl;
	}

	public void setMsl(MatchSpecificDescriptionData msl) {
		this.msl = msl;
	}

	public long getCompId() {
		return compId;
	}

	public void setCompId(long compId) {
		this.compId = compId;
	}

	public int getLinesRead() {
		return linesRead;
	}

	public void setLinesRead(int linesRead) {
		this.linesRead = linesRead;
	}

}
