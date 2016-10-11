package api.functionality;

import java.time.LocalDate;

public interface CsvFileHandler {

	public   String fullCsv(int compId, String compName, String country);
	
	public   String reducedCsv(int compId, String compName, String country);
	
	
	public   String reducedCsv(int compId, String compName, String country,LocalDate ld);

	String fullCsv(int compId, String compName, String country, LocalDate ld, String t1, String t2);
}
