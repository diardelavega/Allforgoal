package basicStruct;

import java.util.ArrayList;
import java.util.List;

import api.functionality.obj.BaseMatchLinePred;

public class FullMatchPredLineToSubStructs {

	public MatchObj fullLineToMatchObj(FullMatchLine f) {
		MatchObj m = new MatchObj();
		m.setmId(f.getmId());
		m.setComId(f.getComId());
		m.setT1(f.getT1());
		m.setT2(f.getT2());
		m.setMatchTime(f.getMatchTime());
		m.setDat(f.getDat());
		m.set_1(f.get_1());
		m.set_x(f.get_x());
		m.set_2(f.get_2());

		m.set_u(f.get_o());
		m.set_u(f.get_u());
		m.setHt1(f.getHt1());
		m.setHt2(f.getHt2());
		m.setFt1(f.getFt1());
		m.setFt2(f.getFt2());
		return m;
	}

	public BaseMatchLinePred fullLineToBase(FullMatchLine f) {
		BaseMatchLinePred b = new BaseMatchLinePred();
		b.setT1(f.getT1());
		b.setT2(f.getT2());
		b.setHt1(f.getHt1());
		b.setHt2(f.getHt2());
		b.setFt1(f.getFt1());
		b.setFt2(f.getFt2());
		b.setH1(f.getH1());
		b.setHx(f.getHx());
		b.setH2(f.getH2());
		b.setSo(f.getSo());
		b.setSu(f.getSu());
		b.setP1n(f.getP1n());
		b.setP1y(f.getP1y());
		b.setP2n(f.getP2n());
		b.setP2y(f.getP2y());
		b.setHt(f.getHt());
		b.setFt(f.getFt());
		return b;
	}

	public FullMatchLine fullLineToBase(MatchObj m, BaseMatchLinePred b) {
		FullMatchLine f = new FullMatchLine();
		f.setmId(m.getmId());
		f.setComId(m.getComId());
		f.setT1(m.getT1());
		f.setT2(m.getT2());
		f.setMatchTime(m.getMatchTime());
		f.setDat(m.getDat());
		f.set_1(m.get_1());
		f.set_x(m.get_x());
		f.set_2(m.get_2());

		f.set_u(m.get_o());
		f.set_u(m.get_u());
		f.setHt1(m.getHt1());
		f.setHt2(m.getHt2());
		f.setFt1(m.getFt1());
		f.setFt2(m.getFt2());

		f.setH1(b.getH1());
		f.setHx(b.getHx());
		f.setH2(b.getH2());
		f.setSo(b.getSo());
		f.setSu(b.getSu());
		f.setP1n(b.getP1n());
		f.setP1y(b.getP1y());
		f.setP2n(b.getP2n());
		f.setP2y(b.getP2y());
		f.setHt(b.getHt());
		f.setFt(b.getFt());

		return f;
	}

	
	public FullMatchLine baseMatchLinePredEnhanceFullLine(FullMatchLine f, BaseMatchLinePred b) {
		f.setH1(b.getH1());
		f.setHx(b.getHx());
		f.setH2(b.getH2());
		f.setSo(b.getSo());
		f.setSu(b.getSu());
		f.setP1n(b.getP1n());
		f.setP1y(b.getP1y());
		f.setP2n(b.getP2n());
		f.setP2y(b.getP2y());
		f.setHt(b.getHt());
		f.setFt(b.getFt());
		return f;
	}
	
	public List<FullMatchLine> mobjToFullMatchLine(List<MatchObj> mobj) {
		List<FullMatchLine> fml = new ArrayList<FullMatchLine>();
		for (MatchObj m : mobj) {
			FullMatchLine f = new FullMatchLine(m);
			fml.add(f);

		}
		return fml;
	}

	public List<MatchObj> fullMatchPredLineToMatchObj(List<FullMatchLine> fml) {
		List<MatchObj> mol = new ArrayList<MatchObj>();
		for (FullMatchLine f : fml) {
			MatchObj m = fullLineToMatchObj(f);
			mol.add(m);
		}
		return mol;
	}

}
