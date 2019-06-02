
package DRList;

import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;
import java.sql.Timestamp;
import java.time.*;

import DRList.DRArrayList;
import DRList.DRIndex;
import DRList.DRBTree;
import DRList.DRCode;
import DRList.DRListTBL;
import DRList.DRNoMatchException;

public class DRFindTest
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
		try{
			Object[] obj = dl1.DRFind("price","=","260.0");
			vos2 = (knapVO)obj[0];
			System.out.println("DLT - eq found "+vos2.col+" "+vos2.price);

			obj = dl1.DRFind("price",">","260.0");
			vos2 = (knapVO)obj[0];
			System.out.println("DLT - gt found "+vos2.col+" "+vos2.price);

			obj = dl1.DRFind("price","<","260.0");
			vos2 = (knapVO)obj[0];
			System.out.println("DLT - lt found "+vos2.col+" "+vos2.price);

			obj = dl1.DRFind("col","Like","yell");
			vos2 = (knapVO)obj[0];
			System.out.println("DLT - like found "+vos2.col+" "+vos2.price);

			obj = dl1.DRFind("price","Min","");
			vos2 = (knapVO)obj[0];
			System.out.println("DLT - min found "+vos2.col+" "+vos2.price);

			obj = dl1.DRFind("price","Max","");
			vos2 = (knapVO)obj[0];
			System.out.println("DLT - max found "+vos2.col+" "+vos2.price);

		}catch (DRNoMatchException nsm){
			System.out.println("DLT - dnm price "+nsm.getMessage());
		}

		dl1.DRclear();

		//==================================================================
		DRList<Integer> dl2 = new DRList<Integer>();

		DRListTest dltest = new DRListTest();
		DRList<String> dl3 = new DRList<String>();
			
		String name = "";

		System.out.println("DLT - getkey daver190 - "+dl3.DRgetKey("DaveR Number190"));
		System.out.println("DLT - get2004 - "+dl3.DRget(2004));
		System.out.println("DLT - getkey daver194 - "+dl3.DRgetKey("DaveR Number194"));

		for (int l = 0; l < 10000; l++){			//load another 1000 names
			name = "DaveR Number"+Integer.toString(Integer.valueOf(l));
			dl3.DRaddkey(name,name);
		}
		System.out.println("DLT - new siz="+dl3.DRsize()+" 1st="+dl3.DRgetFirst()+" last="+dl3.DRgetLast()+
				" getkey daver1010 - "+dl3.DRgetKey("DaveR Number1010")+
				" getkey daver1001 - "+dl3.DRgetKey("DaveR Number1001"));
		if (dl3.reIndex()){
			System.out.println("DLT - reindex successful siz="+dl3.DRsize());
		}

		//=============================================================
		long sti = System.currentTimeMillis();
		try{
			sti = System.currentTimeMillis();
			Object[] obj = dl3.DRFind("","=","DaveR Number1011");
			System.out.println("DLT - eq found "+(String)obj[0]+" ol="+obj.length);

			obj = dl3.DRFind("dsc",">","DaveR Number9990");
			System.out.println("DLT - gt9011 found "+(String)obj[0]+" ol="+obj.length);

			obj = dl3.DRFind("","<","DaveR Number1011");
			System.out.println("DLT - lt found "+(String)obj[0]+" ol="+obj.length);

			obj = dl3.DRFind("","Like","99");
			System.out.println("DLT - like found 1st="+(String)obj[0]+" 2nd="+(String)obj[1]+" ol="+obj.length);

			obj = dl3.DRFind("","Min","");
			System.out.println("DLT - min found 1st="+(String)obj[0]+" ol="+obj.length);

			obj = dl3.DRFind("","Max","");
			System.out.println("DLT - max found 1st="+(String)obj[0]+" ol="+obj.length);

			obj = dl3.DRFindAnd("","<","DaveR Number1111",dl3.DRFind("",">","DaveR Number1011"));
			System.out.println("DLT - ltand found "+(String)obj[0]+" ol="+obj.length);

			System.out.println("DLT - ti="+(System.currentTimeMillis() - sti)+"ms");

		}catch (DRNoMatchException nsm){
			System.out.println("DLT - dnm dl3 "+nsm.getMessage());
		}
		dl3.DRclear();

		//========================================
		nameVO nvo = new nameVO();
		DRList<nameVO> dl4 = new DRList<nameVO>();
		nameVO[] nvoa = new nameVO[10001];
		for (int i = 0; i < 10001; i++){
			nvoa[i] = new nameVO();
			nvoa[i].firstName = "Dave"+Integer.toString(i);
			nvoa[i].surName = "Number"+Integer.toString(i);
			nvoa[i].houseNo = i;
			nvoa[i].postCode = "CR "+Integer.toString(i);
			nvoa[i].salary = 25000.00f + Float.parseFloat(Integer.toString(i)); 
			//System.out.println("DLT - nvo size="+dl4.DRsize()+" minsal="+nvo.salary+" minnam="+nvo.surName);
		}
		int pow = 2;
		int j = 0;
		int k = 0;
		int line = 1;
		while (pow < nvoa.length/2){
			j = nvoa.length/pow;
			k = j;
			while (k < nvoa.length){
				if (nvoa[k] != null){
					//debug("lbt - sk="+xx[k].sortKey);
					dl4.DRadd(nvoa[k]);
					nvoa[k] = null;
				}
				line++;
				k += j;
			}
			pow = pow * 2;
		}
		for (int i = 0; i < nvoa.length; i++){
			if (nvoa[i] != null) dl4.DRadd(nvoa[i]);
		}
		nvo = (nameVO)dl4.DRget(1);
		System.out.println("DLT - nvo size="+dl4.DRsize()+" minsal="+nvo.salary+" minnam="+nvo.surName);

		sti = System.currentTimeMillis();
		try{
			Object[] obj = dl4.DRFind("salary","=","30000.0");
			nvo = (nameVO)obj[0];
			System.out.println("DLT - eq found snam="+nvo.surName+" sal="+nvo.salary+" ol="+obj.length);

			obj = dl4.DRFind("salary",">","34000.0");
			nvo = (nameVO)obj[0];
			System.out.println("DLT - gt found snam="+nvo.surName+" sal="+nvo.salary+" ol="+obj.length);

			obj = dl4.DRFind("salary","<","25500.0");
			nvo = (nameVO)obj[0];
			System.out.println("DLT - lt found snam="+nvo.surName+" sal="+nvo.salary+" ol="+obj.length);

			obj = dl4.DRFind("surName","Like","999");
			nvo = (nameVO)obj[0];
			System.out.println("DLT - like found snam="+nvo.surName+" sal="+nvo.salary+" ol="+obj.length);

			obj = dl4.DRFind("salary","Min","");
			nvo = (nameVO)obj[0];
			System.out.println("DLT - min found snam="+nvo.surName+" sal="+nvo.salary+" ol="+obj.length);

			obj = dl4.DRFind("salary","Max","");
			nvo = (nameVO)obj[0];
			System.out.println("DLT - max found snam="+nvo.surName+" sal="+nvo.salary+" ol="+obj.length);

		}catch (DRNoMatchException nsm){
			System.out.println("DLT - dnm nvo "+nsm.getMessage());
		}
		System.out.println("DLT - ti="+(System.currentTimeMillis() - sti)+"ms");

		//error section
		try{
			Object[] obj = dl4.DRFind("fName","=","30000.0");
		}catch (DRNoMatchException nsm){
			System.out.println("DLT - nsm "+nsm.getMessage());
		}
		try{
			Object[] obj = dl4.DRFind("firstName","Like","David");
		}catch (DRNoMatchException nsm){
			System.out.println("DLT - nsm nolike "+nsm.getMessage());
		}
		try{
			Object[] obj = dl4.DRFind("salary",">","David");
			nvo = (nameVO)obj[0];
			System.out.println("DLT - sal > David ol="+obj.length+" o0="+nvo.salary);
		}catch (DRNoMatchException nsm){
			System.out.println("DLT - nsm invsalval "+nsm.getMessage());
		}

		sti = System.currentTimeMillis();
		try{
			Object[] obj = dl4.DRFindAnd("salary","<","31000.0",dl4.DRFind("salary",">","30000.0"));
			nvo = (nameVO)obj[obj.length - 1];
			System.out.println("DLT - and<> found snam="+nvo.surName+" sal="+nvo.salary+" ol="+obj.length);

			obj = dl4.DRFindAnd("surName dsc","Like","999",dl4.DRFind("salary",">","32000.0"));
			for (int i = 0; i < obj.length; i++){
				nvo = (nameVO)obj[i];
				System.out.println("DLT - >Likedsc found snam="+nvo.surName+" sal="+nvo.salary+" ol="+obj.length);
			}

			obj = dl4.DRFindAnd("firstName","Min","",
				dl4.DRFindAnd("surName","Like","999",dl4.DRFind("salary",">","32000.0")));
			nvo = (nameVO)obj[0];
			System.out.println("DLT - Min>Like found snam="+nvo.surName+" sal="+nvo.salary+" ol="+obj.length);

			obj = dl4.DRFindOr("surName","=","Number102",
				dl4.DRFindOr("surName","=","Number101",dl4.DRFind("surName","=","Number100")));
			nvo = (nameVO)obj[0];
			System.out.println("DLT - or3= found snam="+nvo.surName+" sal="+nvo.salary+" ol="+obj.length);

			obj = dl4.DRFindOr("salary",">","34800.0",dl4.DRFind("surName",">","Number9900"));
			nvo = (nameVO)obj[0];
			System.out.println("DLT - or2> found snam="+nvo.surName+" sal="+nvo.salary+" ol="+obj.length);

			obj = dl4.DRFindAnd("salary","Like","99",dl4.DRFind("salary",">","34800.0"));
			nvo = (nameVO)obj[0];
			System.out.println("DLT - sallike99 ol="+obj.length+" o0="+nvo.salary);

			obj = dl4.DRFindMinus("salary","Like","99",dl4.DRFind("salary dsc",">","34800.0"));
			nvo = (nameVO)obj[0];
			System.out.println("DLT - minussallike99 ol="+obj.length+" o0="+nvo.salary);

		}catch (DRNoMatchException nsm){
			System.out.println("DLT - dnm salary "+nsm.getMessage());
		}
		System.out.println("DLT - ti="+(System.currentTimeMillis() - sti)+"ms");

		try{
			Object[] obj = dl4.DRFindAnd("firstName","=","David",dl4.DRFind("salary",">","34990.0"));
			nvo = (nameVO)obj[0];
			System.out.println("DLT - sal > &fn=David ol="+obj.length+" o0="+nvo.salary);
		}catch (DRNoMatchException nsm){
			System.out.println("DLT - nsm not= "+nsm.getMessage());
		}
		try{
			Object[] obj = dl4.DRFindAnd("fn","=","David",dl4.DRFind("sal",">","34990.0"));
			nvo = (nameVO)obj[0];
			System.out.println("DLT - sal>&invfn ol="+obj.length+" o0="+nvo.salary);
		}catch (DRNoMatchException nsm){
			System.out.println("DLT - dnm invfield "+nsm.getMessage());
		}
		try{
			Object[] obj = dl4.DRFindAnd("salary","<","34980.0",dl4.DRFind("salary",">","34990.0"));
			nvo = (nameVO)obj[0];
			System.out.println("DLT - sal<> ol="+obj.length+" o0="+nvo.salary);
		}catch (DRNoMatchException nsm){
			System.out.println("DLT - dnm <>sal "+nsm.getMessage());
		}
		try{
			Object[] obj = dl4.DRFindAnd("","","",dl4.DRFind("salary",">","34990.0"));
			nvo = (nameVO)obj[0];
			System.out.println("DLT - nulland ol="+obj.length+" o0="+nvo.salary);
		}catch (DRNoMatchException nsm){
			System.out.println("DLT - nulland "+nsm.getMessage());
		}

		dl4.DRclear();
		try{
			Object[] obj = dl4.DRFindAnd("salary","<","34980.0",dl4.DRFind("salary",">","34990.0"));
			nvo = (nameVO)obj[0];
			System.out.println("DLT - dl4clear ol="+obj.length+" o0="+nvo.salary);
		}catch (DRNoMatchException nsm){
			System.out.println("DLT - dnm <>sal "+nsm.getMessage());
		}
	}
	//============================================
	public static class nameVO {

		String firstName;
		String surName;
		int houseNo;
		String postCode;
		float salary;

	}
	//============================================
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






