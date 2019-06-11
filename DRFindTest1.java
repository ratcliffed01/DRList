
package DRList;

import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;
import java.sql.Timestamp;
import java.time.*;
import java.math.*;
import java.lang.reflect.*;

import DRList.DRArrayList;
import DRList.DRIndex;
import DRList.DRBTree;
import DRList.DRCode;
import DRList.DRListTBL;
import DRList.DRNoMatchException;

public class DRFindTest1
{

	//=================================================
	public static void main(String[] args) {


		//========================================
		nameVO nvo = new nameVO();
		DRList<nameVO> dl4 = new DRList<nameVO>();
		nameVO[] nvoa = new nameVO[10001];
		String xstr = "";
		int j = 200000;
		for (int i = 0; i < 10001; i++){
			nvoa[i] = new nameVO();
			nvoa[i].firstName = "Dave"+Integer.toString(i);
			nvoa[i].surName = "Number"+Integer.toString(i);
			nvoa[i].houseNo = i;
			nvoa[i].postCode = "CR "+Integer.toString(i);
			nvoa[i].salary = 25000.00f + Float.parseFloat(Integer.toString(i));
			xstr =  j - i + "";
			nvoa[i].houseVal = new BigDecimal(xstr);
			//System.out.println("DLT - nvo size="+dl4.DRsize()+" minsal="+nvo.salary+" minnam="+nvo.surName);

			dl4.DRadd(nvoa[i]);
		}
		nvo = (nameVO)dl4.DRget(1);
		System.out.println("DLT - nvo size="+dl4.DRsize()+" minsal="+nvo.salary+" minnam="+nvo.surName);

		long sti = System.currentTimeMillis();
		try{
			Object[] obj = dl4.DRFind("surName","=","Number100").DRFindOr("surName","=","Number101").DRFindOr("surName","=","Number102")
				.getObjArray();
			nvo = (nameVO)obj[0];
			System.out.println("DLT - or3= found snam="+nvo.surName+" sal="+nvo.salary+" ol="+obj.length);

			DRList<nameVO> ndrl =  dl4.DRFind("surName","=","Number100").DRFindOr("surName","=","Number102").getDRList();
			nvo = (nameVO)ndrl.DRget(0);
			System.out.println("DLT - ndrl found snam="+nvo.surName+" sal="+nvo.salary+" siz="+ndrl.DRsize());

			List<nameVO> nlst =  dl4.DRFind("surName","=","Number100").DRFindOr("surName","=","Number102").getArrayList();
			nvo = (nameVO)nlst.get(0);
			System.out.println("DLT - nlst found snam="+nvo.surName+" sal="+nvo.salary+" siz="+nlst.size());

			nameVO[] obj1 = dl4.DRFind("salary","<","25100").getObjArray();
			nvo = (nameVO)obj1[obj1.length - 1];
			System.out.println("DLT - fo= found snam="+nvo.surName+" sal="+nvo.salary+" ol="+obj1.length);
			obj1 = dl4.DRFindObject("salary",">","-25010.00",dl4.DRFindObject("salary","<","25090.00",obj1));
			nvo = (nameVO)obj1[0];
			System.out.println("DLT - fo= found snam="+nvo.surName+" sal="+nvo.salary+" ol="+obj1.length);

			obj = dl4.DRFind("houseNo",">","-99").DRFindAnd("houseNo","<","99").DRFindAnd("houseNo",">","9").getObjArray();
			nvo = (nameVO)obj[0];
			System.out.println("DLT - or3= found snam="+nvo.surName+" sal="+nvo.salary+" ol="+obj.length);

			obj = dl4.DRFind("houseVal",">","195000").DRFindAnd("houseVal","<","195100").DRFindAnd("houseVal asc","Like","8").getObjArray();
			for (int i = 0; i < obj.length; i++){
				nvo = (nameVO)obj[i];
				System.out.println("DLT - houseVal found snam="+nvo.surName+" hv="+nvo.houseVal+" sal="+nvo.salary);
			}
		}catch (DRNoMatchException dnm){
			System.out.println("DLT - like99 err dn "+dnm.getMessage());
		}

		System.out.println("DLT - ti="+(System.currentTimeMillis() - sti)+"ms");

	}
	//============================================
	public static class nameVO {

		String firstName;
		String surName;
		int houseNo;
		String postCode;
		float salary;
		BigDecimal houseVal;
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






