package api.functionality;

public interface CsvFileHandler {

	public   String fullCsv(int compId, String compName, String country);
	
	public   String reducedCsv(int compId, String compName, String country);
}
