
package projects.DRList.Jar;

public class DRNoMatchException extends Exception{

	String s1;

	public DRNoMatchException() {super();}

	public DRNoMatchException(String s2) {
		this.s1 = s2;
	}

	public String getMessage(){
		return s1;
	}
}
