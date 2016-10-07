package basicStruct;

import java.time.LocalDate;
import java.util.List;

public class AsyncRequest {

	private String type;
	private List<Integer> list;
	private String atts;
	private int serialCode;
	private LocalDate ld;

	public AsyncRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AsyncRequest(String type, List<Integer> list, String atts,
			int serialCode, LocalDate ld) {
		super();
		this.type = type;
		this.list = list;
		this.atts = atts;
		this.serialCode = serialCode;
		this.ld = ld;
	}

	public String getType() {
		return type;
	}

	public List<Integer> getList() {
		return list;
	}

	public String getAtts() {
		return atts;
	}

	public int getSerialCode() {
		return serialCode;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setList(List<Integer> list) {
		this.list = list;
	}

	public void setAtts(String atts) {
		this.atts = atts;
	}

	public void setSerialCode(int serialCode) {
		this.serialCode = serialCode;
	}

	public LocalDate getLd() {
		return ld;
	}

	public void setLd(LocalDate ld) {
		this.ld = ld;
	}

	public void show() {
		System.out.print("dat: " + ld + " || type: " + type + " || atts: " + atts
				+ " || seri: " + serialCode + ",  list:[ ");
		for (int i : list) {
			System.out.print(i + ",");
		}
		System.out.println("]");
	}
}
