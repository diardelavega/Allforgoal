package basicStruct;

import java.util.List;

public class AsyncRequest {

	private String type;
	private List<Integer> list;
	private String atts;
	private int serialCode;

	public AsyncRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AsyncRequest(String type, List<Integer> list, String atts,
			int serialCode) {
		super();
		this.type = type;
		this.list = list;
		this.atts = atts;
		this.serialCode = serialCode;
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

}
