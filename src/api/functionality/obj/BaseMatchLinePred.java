package api.functionality.obj;

public class BaseMatchLinePred  {
	protected String t1;
	protected String t2;

	private String _1;
	private String _x;
	private String _2;
	private String _o;
	private String _u;

	private String p1y;
	private String p1n;

	private String p2y;
	private String p2n;

	private String ht;
	private String ft;

	public BaseMatchLinePred() {
		super();
	}

	public BaseMatchLinePred(String t1, String t2, String _1, String _x, String _2,
			String _o, String _u, String p1y, String p1n, String p2y,
			String p2n, String ht, String ft) {
		super();
		this.t1 = t1;
		this.t2 = t2;
		this._1 = _1;
		this._x = _x;
		this._2 = _2;
		this._o = _o;
		this._u = _u;
		this.p1y = p1y;
		this.p1n = p1n;
		this.p2y = p2y;
		this.p2n = p2n;
		this.ht = ht;
		this.ft = ft;
	}

	public String getT1() {
		return t1;
	}

	public String getT2() {
		return t2;
	}

	public String get_1() {
		return _1;
	}

	public String get_x() {
		return _x;
	}

	public String get_2() {
		return _2;
	}

	public String get_o() {
		return _o;
	}

	public String get_u() {
		return _u;
	}

	public String getP1y() {
		return p1y;
	}

	public String getP1n() {
		return p1n;
	}

	public String getP2y() {
		return p2y;
	}

	public String getP2n() {
		return p2n;
	}

	public String getHt() {
		return ht;
	}

	public String getFt() {
		return ft;
	}

	public void setT1(String t1) {
		this.t1 = t1;
	}

	public void setT2(String t2) {
		this.t2 = t2;
	}

	public void set_1(String _1) {
		this._1 = _1;
	}

	public void set_x(String _x) {
		this._x = _x;
	}

	public void set_2(String _2) {
		this._2 = _2;
	}

	public void set_o(String _o) {
		this._o = _o;
	}

	public void set_u(String _u) {
		this._u = _u;
	}

	public void setP1y(String p1y) {
		this.p1y = p1y;
	}

	public void setP1n(String p1n) {
		this.p1n = p1n;
	}

	public void setP2y(String p2y) {
		this.p2y = p2y;
	}

	public void setP2n(String p2n) {
		this.p2n = p2n;
	}

	public void setHt(String ht) {
		this.ht = ht;
	}

	public void setFt(String ft) {
		this.ft = ft;
	}

	public void matchPredLineShow(){
		StringBuilder sb = new StringBuilder();
		sb.append(t1);
		sb.append(" ");
		sb.append(t2);
		sb.append(" ");
		sb.append(_1);
		sb.append(" ");
		sb.append(_x);
		sb.append(" ");
		sb.append(_2);
		sb.append(" ");
		
		sb.append(_o);
		sb.append(" ");
		sb.append(_u);
		sb.append(" ");
		
		
		sb.append(p1y);
		sb.append(" ");
		sb.append(p1n);
		sb.append(" ");
		sb.append(p2y);
		sb.append(" ");
		sb.append(p2n);
		sb.append(" ");
		
		sb.append(ht);
		sb.append(" ");
		sb.append(ft);
		sb.append(" ");
	}
}
