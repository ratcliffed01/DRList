
package projects.DRList.Jar;

import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;
import java.sql.Timestamp;
import java.time.*;

import projects.DRList.Jar.DRArrayList;
import projects.DRList.Jar.DRIndex;
import projects.DRList.Jar.DRBTree;
import projects.DRList.Jar.DRCode;
import projects.DRList.Jar.DRListTBL;
import projects.DRList.Jar.DRNoMatchException;

public class DRListTest
{

	//=================================================
	public static void main(String[] args) {

		// {colour,weight,price,ratiox10}
		knapVO[] vos1 = {
			new knapVO("green",8.0f,894.0f,0.0f),
			new knapVO("blue",6.0f,260.0f,0.0f),
			new knapVO("brown",4.0f,392.0f,0.0f),
			new knapVO("yellow",0.0f,281.0f,0.0f),
			new knapVO("grey",21.0f,27.0f,0.0f)
		};

		DRList<knapVO> dl1 = new DRList<knapVO>();
		dl1.DRadd(vos1[0]);
		dl1.DRadd(vos1[1]);
		dl1.DRadd(vos1[2]);
		dl1.DRadd(vos1[3]);
		dl1.DRadd(vos1[4]);

		knapVO vos2 = new knapVO();
		vos2 = (knapVO)dl1.DRget(3);
		String x1 = vos2.col;
		vos2 = (knapVO)dl1.DRnext();
		String x2 = vos2.col;
		System.out.println("DLT - DR size=" + dl1.DRsize()+ " col="+x1+" "+x2+" gkblue="+dl1.DRgetKey("blue"));

		knapVO[] vos3 = {new knapVO("black",9.0f,883.0f,0.0f)};
		if (dl1.DRinsert(vos3[0]))
			System.out.println("DLT - insert success DR size=" + dl1.DRsize());
		else
			System.out.println("DLT - insert fail");

		try{
			dl1.DRsortNoKeyAsc("col");
		}catch (DRNoMatchException nsf){
			System.out.println("DLT - nsf price1 "+nsf.getMessage());
		}
		for (int i=0; i < dl1.DRsize(); i++){
			vos2 = (knapVO)dl1.DRget(i);
			System.out.println("DLT - i="+i+" col="+vos2.col+" price="+vos2.price);
		}
		try{
			dl1.DRsortNoKeyAsc("price");
		}catch (DRNoMatchException nsf){
			System.out.println("DLT - nsf price1 "+nsf.getMessage());
		}
		for (int i=0; i < dl1.DRsize(); i++){
			vos2 = (knapVO)dl1.DRget(i);
			System.out.println("DLT - i="+i+" col="+vos2.col+" price="+vos2.price);
		}
		try{
			dl1.DRsortNoKeyAsc("price1");
		}catch (DRNoMatchException nsf){
			System.out.println("DLT - nsf price1 "+nsf.getMessage());
		}

		dl1.DRclear();

		//==================================================================
		long sti = System.currentTimeMillis();
		DRList<Integer> dl2 = new DRList<Integer>();
		for (int i = 0; i < 1451; i++){
			//System.out.println("DLT - kvo i="+i);
			dl2.DRaddkey(Integer.valueOf(i),Integer.toString(i));
		}
		x2 = Integer.toString((Integer)dl2.DRget(1450));
		if (dl2.hasNext()){
			x2 += " "+Integer.toString((Integer)dl2.DRnext());
		}else{
			x2 += " end of list";
		}	
		System.out.println("DLT - DR size=" + dl2.DRsize()+ " i="+x2);
		x2 = Integer.toString((Integer)dl2.DRgetKey("1400"));
		System.out.println("DLT - DR size=" + dl2.DRsize()+ " k="+x2+
			" getk110="+Integer.toString((Integer)dl2.DRgetKey("110")));

		DRArrayList<Integer>[] xx=dl2.toArray();
		String zz = Integer.toString((Integer)xx[1].obj)+"  xsk100="+xx[100].sortKey;
		System.out.println("DLT - xxsize="+xx.length+" xx1="+zz);

		dl2.DRclear();
		System.out.println("DLT - clear size = "+dl2.DRsize());

		dl2.toDRList(xx);
		System.out.println("DLT - todrl size = "+dl2.DRsize()+" dl341="+
			Integer.toString((Integer)dl2.DRget(1341))+
			" getlast="+Integer.toString((Integer)dl2.DRget(dl2.DRsize() - 1))+
			" getk110="+Integer.toString((Integer)dl2.DRgetKey("110")));

		dl2.DRsortAsc();
		System.out.println("DLT - asc sz="+dl2.DRsize()+" get1="+Integer.toString((Integer)dl2.DRget(1))+" getlast="+
					Integer.toString((Integer)dl2.DRget(dl2.DRsize() - 1)));

		System.out.println("DLT - dsc sz="+dl2.DRsize()+" get1="+Integer.toString((Integer)dl2.DRget(1))+" getlast="+
					Integer.toString((Integer)dl2.DRget(dl2.DRsize() - 1))+
			" getk110="+Integer.toString((Integer)dl2.DRgetKey("110")));

		dl2.DRsortDsc();
		System.out.println("DLT - dsc sz="+dl2.DRsize()+" get1="+Integer.toString((Integer)dl2.DRget(1))+" getlast="+
					Integer.toString((Integer)dl2.DRget(dl2.DRsize() - 1))+
			" getk110="+Integer.toString((Integer)dl2.DRgetKey("110")));

		if (dl2.DRget(99) != null){
			System.out.println("DLT - get99="+Integer.toString((Integer)dl2.DRget(99)));
			if (dl2.DRdelete()){ 
				System.out.println("DLT - element deleted");
			}else{
				System.out.println("DLT - element NOT deleted");
			}
		}else{
			System.out.println("DLT - element not found");
		}
		if (dl2.DRgetKey("1351") == null){
			System.out.println("DLT - new get99="+Integer.toString((Integer)dl2.DRget(99))+" siz="+dl2.DRsize()+
				" 1351 not found");
		}else{
			System.out.println("DLT - new get99="+Integer.toString((Integer)dl2.DRget(99))+" siz="+dl2.DRsize()+
					" gk="+Integer.toString((Integer)dl2.DRgetKey("1351")));
		}
		if (dl2.DRget(-1) != null){
			System.out.println("DLT - found get-1");
		}else{
			System.out.println("DLT - notfound get-1");
		}
		if (dl2.DRget(1500) != null){
			System.out.println("DLT - found get1500");
		}else{
			System.out.println("DLT - notfound get1500");
		}
		if (dl2.hasNext()) 
			System.out.println("DLT - new next="+Integer.toString((Integer)dl2.DRnext())+
				" ti="+(System.currentTimeMillis() - sti)+"ms");
/**/
		//==============================================================
		sti = System.currentTimeMillis();

		DRListTest dltest = new DRListTest();
		DRList<String> dl3 = new DRList<String>();

		System.out.println("DLT - 1st="+dl3.DRgetFirst()+" last="+dl3.DRgetLast()+
			" gk="+dl3.DRgetKey("110")+" g101="+dl3.DRget(101));

		dl3 = dltest.readFile("names.txt",dl3);
			
		String name = "";
		for (int i = 0; i < 1000; i++){			//load another 1000 names
			name = "DaveR Number"+i;
			dl3.DRaddkey(name,name);
		}

		System.out.println("DLT - siz="+dl3.DRsize()+" 1st="+dl3.DRgetFirst()+" last="+dl3.DRgetLast()+
				" nxt="+dl3.DRnext());
		System.out.println("DLT - ti="+(System.currentTimeMillis() - sti)+"ms");
		sti = System.currentTimeMillis();

		dl3.DRsortAsc();
		System.out.println("DLT - asc siz="+dl3.DRsize()+" 1st="+dl3.DRgetFirst()+" last="+dl3.DRgetLast()+
				" nxt="+dl3.DRnext());
		dl3.DRsortDsc();
		System.out.println("DLT - dsc siz="+dl3.DRsize()+" 1st="+dl3.DRgetFirst()+" last="+dl3.DRgetLast()+
				" nxt="+dl3.DRnext());

		System.out.println("DLT - b4 del get110="+dl3.DRget(110)+" deleted="+dl3.DRdelete());
		
		System.out.println("DLT - post del get110="+dl3.DRget(110));

		for (int i = 0; i < 110; i++){
			if (dl3.DRdelete()) x2 = dl3.DRnext();
		}
		System.out.println("DLT - del siz="+dl3.DRsize()+" 1st="+dl3.DRgetFirst()+" last="+dl3.DRgetLast()+
				" nxt="+dl3.DRnext());

		System.out.println("DLT - del 1st="+dl3.DRgetFirst()+" "+dl3.DRdelete());
		System.out.println("DLT - del last="+dl3.DRgetLast()+" "+dl3.DRdelete());
		System.out.println("DLT - del siz="+dl3.DRsize()+" 1st="+dl3.DRgetFirst()+" last="+dl3.DRgetLast()+
				" nxt="+dl3.DRnext());
		System.out.println("DLT - getkey daver190 - "+dl3.DRgetKey("DaveR Number190"));
		System.out.println("DLT - get2004 - "+dl3.DRget(2004));
		System.out.println("DLT - getkey daver194 - "+dl3.DRgetKey("DaveR Number194"));

		for (int l = 1001; l < 9000; l++){			//load another 1000 names
			name = "DaveR Number"+Integer.toString(Integer.valueOf(l));
			dl3.DRaddkey(name,name);
		}
		System.out.println("DLT - new siz="+dl3.DRsize()+" 1st="+dl3.DRgetFirst()+" last="+dl3.DRgetLast()+
				" getkey daver1010 - "+dl3.DRgetKey("DaveR Number1010")+
				" getkey daver1001 - "+dl3.DRgetKey("DaveR Number1001"));
		System.out.println("DLT - getkey daver3160 - "+dl3.DRgetKey("DaveR Number3160"));
		System.out.println("DLT - get4662 - "+dl3.DRget(4662)+" nxt="+dl3.DRnext());
		
		System.out.println("DLT - get4000 - "+dl3.DRget(4000)+" nxt="+dl3.DRnext());

		name = "DaveR Number1010";		//add duplicate
		dl3.DRaddkey(name+"dup1",name);
		name = "DaveR Number1010";		//add duplicate
		dl3.DRaddkey(name+"dup2",name);
		name = "DaveR Number1010";		//add duplicate
		dl3.DRaddkey(name+"dup3",name);
		name = "DaveR Number1010";		//add duplicate
		dl3.DRaddkey(name+"dup4",name);
		System.out.println("DLT - getkey daver1010 - "+dl3.DRgetKey("DaveR Number1010")+" dup "+dl3.hasDuplicates());

		if (dl3.hasDuplicates()){ 
			int[] ii = dl3.DRgetKeyDupI("DaveR Number1010");
			Object[] aa = dl3.DRgetKeyDup("DaveR Number1010");
			System.out.println("DLT - post getkd aal="+aa.length);
			for (int i = 0; i < aa.length; i++)
				System.out.println("DLT - dupnames = "+aa[i]+" ("+ii[i]+")");
		}
		System.out.println("DLT - 1st get10479 - "+dl3.DRget(10479)+" siz="+dl3.DRsize());
		dl3.DRset("DaveR Number1010dup3settest");
		System.out.println("DLT - 2nd get2487 - "+dl3.DRget(2487)+" del="+dl3.DRdelete());
		System.out.println("DLT - 3rd gk10477="+dl3.DRget(10477)+" dup="+dl3.hasDuplicates());
		if (dl3.DRdelete()){
			int[] ii1 = dl3.DRgetKeyDupI("DaveR Number1010");
			Object[] aa1 = dl3.DRgetKeyDup("DaveR Number1010");
			System.out.println("DLT - post dupdel aal="+aa1.length+" iil="+ii1.length+" siz="+dl3.DRsize());
			for (int i = 0; i < aa1.length; i++)
				System.out.println("DLT - dupnames = "+aa1[i]+" ("+ii1[i]+")");
		}
		if (dl3.reIndex()){
			System.out.println("DLT - reindex successful siz="+dl3.DRsize());
		}
		dl3.DRsortAsc();
		System.out.println("DLT - asc siz="+dl3.DRsize()+" 1st="+dl3.DRgetFirst()+" last="+dl3.DRgetLast()+
				" getkey daver1010 - "+dl3.DRgetKey("DaveR Number1010")+" get10010="+dl3.DRget(10010));

		System.out.println("DLT - ti="+(System.currentTimeMillis() - sti)+"ms");
	
		DRList<DRList<Character>> dl4 = new DRList<DRList<Character>>();
		dl4.DRadd(dltest.twoDDList("111111"));
		dl4.DRadd(dltest.twoDDList("1 13 1"));
		dl4.DRadd(dltest.twoDDList("11  11"));

		char[] xxc = new char[dl4.DRsize()];
		String str = "";
		for (int i = 0; i< dl4.DRsize(); i++){
			xxc = dltest.getChar(dl4.DRget(i));
			str = "";
			for (int j = 0; j < xxc.length; j++)
				str += String.valueOf(xxc[j]);
			System.out.println(str);
		}
		System.out.println("outout 1,3 - "+dltest.get2DArray(dl4,1,2)+dltest.get2DArray(dl4,1,3));

		System.out.println("DLT - asc siz="+dl3.DRsize()+" getkey daver1010 - "+dl3.DRgetKey("DaveR Number1010"));
		if (dl3.DRinsertKey("DaveR Number1010a","DaveR Number1010a"))
			System.out.println("DLT - insert 1010a successful siz="+dl3.DRsize()+" 1010a="+
				dl3.DRgetKey("DaveR Number1010a"));
		else
			System.out.println("DLT - insert 1010a fail");

		try{
			dl3.DRsortNoKeyDsc("col");
		}catch (DRNoMatchException nsf){
			System.out.println("DLT - nsf col "+nsf.getMessage());
		}
		System.out.println("DLT - sort obj 1st="+dl3.DRgetFirst()+" lst="+dl3.DRgetLast());

	}
	//======================================================
    	public char get2DArray(DRList<DRList<Character>> dl4, int i,int j){
		DRListTest dltest = new DRListTest();
		char oc = dltest.getOneChar(dl4.DRget(i),j);
		return oc;
	}
	//=======================================================
     	public char getOneChar(DRList<Character> dl5,int i){
		char oc = dl5.DRget(i);
		return oc;
	}
	//=======================================================
    	public char[] getChar(DRList<Character> dl5){
		char[] xxc = new char[dl5.DRsize()];
		for (int i = 0; i < dl5.DRsize(); i++)
			xxc[i] = dl5.DRget(i);
		return xxc;
	}
	//======================================================
    	public DRList<Character> twoDDList(String str){

		DRList<Character> c = new DRList<Character>();

		char x;
		for (int i = 0; i < str.length(); i++){
			x = str.charAt(i);
			c.DRadd(x);
		}
		return c;
    	}

    	//===================================================================================
	// read txt files from local folder and load into 1darray, 1st line is number of names
    	public DRList<String> readFile(String path, DRList<String> dl)
    	{

		int i = 0;
		int j = 0;

		try
		{
			RandomAccessFile cp = new RandomAccessFile(path, "r");

			String allLines = "";
			String xx = "";

			while ((xx=cp.readLine())!=null){
				dl.DRaddkey(xx,xx);
			}
			cp.close();

			//System.out.println("rf - siz="+linex.length+" j="+j);

			return dl;
		}
		catch (IOException ioe)
		{
        	    	System.out.println("reading file IOException - "+ioe.getMessage());
	    		return null;
		}
		catch (Exception e)
		{
            		System.out.println("reading files Exception - i="+i+" j="+j+" "+e.getMessage());
	    		return null;
		}
    	}

	public static class knapVO {

		private String col;
		private float wght;
		private float price;
		private float ratio;

		public knapVO(){
		}

		public knapVO(String c, float w, float p, float r){
			this.col = c;
			this.wght = w;
			this.price = p;
			this.ratio = r;
		}

		public String getCol(){
			return this.col;
		}
		public void setCol(String x){
			this.col = x;
		}

		public float getWght(){
			return this.wght;
		}
		public void setWght(float x){
			this.wght = x;
		}

		public float getPrice(){
			return this.price;
		}
		public void setPrice(float x){
			this.price = x;
		}
		
		public float getRatio(){
			return this.ratio;
		}
		public void setRatio(float x){
			this.ratio = x;
		}
	}

}






