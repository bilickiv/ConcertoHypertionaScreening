package hu.aensys.concerto.hypertonia;

import java.util.List;
import java.util.Vector;

public class UserModel {

	private List<IllnessModel> illnesses = new Vector<IllnessModel>();
	public String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void addIllness(IllnessModel im)
	{
		illnesses.add(im);
	}
	public List<IllnessModel> getIllnesses()
    {
		return illnesses;
    }	
}
