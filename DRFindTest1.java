
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
		nameVO nvo1 = new nameVO();
		DRList<nameVO> dl4 = new DRList<nameVO>();
		nameVO[] nvoa = new nameVO[10101];
		String xstr = "";
		int j = 200000;
		int k = 0;
		int l = 0;
		boolean xflag = false;
		for (int i = 0; i < nvoa.length; i++){
			nvoa[i] = new nameVO();
			nvoa[i].firstName = "Dave"+Integer.toString(l);
			nvoa[i].surName = "Number"+Integer.toString(l);
			nvoa[i].houseNo = l;
			nvoa[i].postCode = "CR "+Integer.toString(k);
			nvoa[i].salary = 25000.00f + Float.parseFloat(Integer.toString(l));
			xstr =  j - l + "";
			nvoa[i].houseVal = new BigDecimal(xstr);
			xstr =  "01883" + Integer.toString(l);
			nvoa[i].telNo = new BigInteger(xstr);
			if (xflag == false) xflag = true; else xflag = false;
			nvoa[i].detached = xflag;
			dl4.DRadd(nvoa[i]);
			if (l % 100 == 0){
				i++;
				if (i > 10100) break;
				nvoa[i] = new nameVO();
				nvoa[i].firstName = nvoa[i - 1].firstName;
				nvoa[i].surName = nvoa[i - 1].surName;
				nvoa[i].houseNo = nvoa[i - 1].houseNo;
				nvoa[i].postCode = nvoa[i - 1].postCode;
				//nvoa[i].salary = nvoa[i - 1].salary;
				nvoa[i].houseVal = nvoa[i - 1].houseVal;
				nvoa[i].telNo = nvoa[i - 1].telNo;
				nvoa[i].detached = nvoa[i - 1].detached;
				//System.out.println("DLT - sal="+nvoa[i].salary+"/"+nvoa[i - 1].salary+" sn="+nvoa[i].surName+"/"+
				//nvoa[i - 1].surName+" hv="+nvoa[i].houseVal+"/"+nvoa[i - 1].houseVal+" pc="+nvoa[i].postCode+"/"+
				//nvoa[i - 1].postCode+" fn="+nvoa[i].firstName+"/"+nvoa[i - 1].firstName+" hn="+nvoa[i].houseNo+"/"+
				//nvoa[i - 1].houseNo);
				k++;
				dl4.DRadd(nvoa[i]);
			}
			l++;
			//System.out.println("DLT - nvo size="+dl4.DRsize()+" minsal="+nvo.salary+" minnam="+nvo.surName);
		}
		nvo = (nameVO)dl4.DRget(1);
		System.out.println("DLT - nvo size="+dl4.DRsize()+" minsal="+nvo.salary+" minnam="+nvo.surName);

		long sti = System.currentTimeMillis();
		try{
			Object[] obj = dl4.DRFind("surName","=","Number100").DRFindOr("surName","=","Number101").DRFindOr("surName","=","Number102")
				.getObjArray();
			nvo = (nameVO)obj[0];
			System.out.println("DLT - or3= found snam="+nvo.surName+" sal="+nvo.salary+" ol="+obj.length);

			obj = dl4.DRFind("surName","=","Number101").DRFindOr("surName","=","Number101").DRFindOr("surName","=","Number102")
				.getObjArray();
			nvo = (nameVO)obj[0];
			System.out.println("DLT - or3>2 found snam="+nvo.surName+" sal="+nvo.salary+" ol="+obj.length);

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

			obj = dl4.DRFind("houseVal",">","199000").DRFindAnd("houseVal","<","199900").DRFindAnd("houseVal asc","Like","00")
				.DRFindAnd("detached","=","false").getObjArray();
			for (int i = 0; i < obj.length; i++){
				nvo = (nameVO)obj[i];
				System.out.println("DLT - houseVal found snam="+nvo.surName+" hv="+nvo.houseVal+" sal="+nvo.salary+" "+nvo.detached);
			}

			int cnt = dl4.DRFind("houseVal",">","195000").DRFindAnd("houseVal","<","195100").DRFindAnd("detached","=","true")
				.getCount();
			System.out.println("DLT - true cnt="+cnt);

			float favg = dl4.DRFind("houseVal",">","195000").DRFindAnd("houseVal","<","195100").DRFindAnd("houseVal asc","Like","8")
				.getAvg("salary");
			int iavg = dl4.DRFind("houseVal",">","195000").DRFindAnd("houseVal","<","195100").DRFindAnd("houseVal asc","Like","8")
				.getAvg("houseNo");
			System.out.println("DLT - favg="+favg+" iavg="+iavg);

			long tot = dl4.DRFind("houseVal",">","195000").DRFindAnd("houseVal","<","195100").DRFindAnd("houseVal asc","Like","8")
				.getSum("salary","LONG");
			int toti = dl4.DRFind("houseVal",">","195000").DRFindAnd("houseVal","<","195100").DRFindAnd("houseVal asc","Like","8")
				.getSum("salary","INTEGER");
			float totf = dl4.DRFind("houseVal",">","195000").DRFindAnd("houseVal","<","195100").DRFindAnd("houseVal asc","Like","8")
				.getSum("salary","FLOAT");
			double totd = dl4.DRFind("houseVal",">","195000").DRFindAnd("houseVal","<","195100").DRFindAnd("houseVal asc","Like","8")
				.getSum("salary","DOUBLE");
			System.out.println("DLT - tot="+tot+" toti="+toti+" totf="+totf+" totd="+totd);

			cnt = dl4.DRFind("postCode","=","CR 90").getCount();
			float avg1 = dl4.DRFind("postCode","=","CR 90").getAvg("salary");
			double sum1 = dl4.DRFind("postCode","=","CR 90").getSum("salary","DOUBLE");
			System.out.println("DLT - avg="+avg1+" sum="+sum1+" cnt="+cnt);

			BigDecimal avg = dl4.DRFind("postCode","=","CR 90").getAvg("houseVal");
			tot = dl4.DRFind("postCode","=","CR 90").getSum("houseVal","LONG");
			System.out.println("DLT - avgbgdec avg="+avg+" sum="+tot);

			int cnt1 = dl4.DRFind("houseVal",">","199650").DRFindAnd("houseVal","<","199850").getCount();
			cnt = dl4.DRFind("houseVal",">","199650").DRFindAnd("houseVal","<","199850").distinct().getCount();
			System.out.println("DLT - disctcnt="+cnt+" cnt1="+cnt1);
			obj = dl4.DRFind("houseVal","=","199700").getObjArray();
			nvo = (nameVO)obj[0];
			if (obj.length > 1) {
				nvo1 = (nameVO)obj[1];
				System.out.println("DLT - finddup found snam="+nvo.surName+"/"+nvo1.surName+" ol="+obj.length);
				System.out.println("DLT - getfv found snam="+dl4.DRFind("houseVal","=","199700").getFieldValue("surName")[0]+"/"+
					dl4.DRFind("houseVal","=","199700").getFieldValue("surName")[1]+" ol="+obj.length);
			}
			BigDecimal maxb = dl4.DRFind("postCode","=","CR 90").getMax("houseVal");
			BigDecimal minb = dl4.DRFind("postCode","=","CR 90").getMin("houseVal");
			BigDecimal sumd = dl4.DRFind("postCode","=","CR 90").getSum("houseVal","bigdecimal");
			BigDecimal avgd = dl4.DRFind("postCode","=","CR 90").getAvg("houseVal");
			System.out.println("DLT - bigd minb="+minb+" maxb="+maxb+
				" maxs="+dl4.DRFind("postCode","=","CR 90").getMax("surName")+
				" mins="+dl4.DRFind("postCode","=","CR 90").getMin("surName")+" sumd="+sumd+" avgd="+avgd);

			BigInteger maxi = dl4.DRFind("postCode","=","CR 90").getMax("telNo");
			BigInteger mini = dl4.DRFind("postCode","=","CR 90").getMin("telNo");
			BigInteger sumi = dl4.DRFind("postCode","=","CR 90").getSum("telNo","biginteger");
			BigInteger avgi = dl4.DRFind("postCode","=","CR 90").getAvg("telNo");
			System.out.println("DLT - bigi mini="+mini+" maxi="+maxi+" sumi="+sumi+" avgi="+avgi);

			obj = dl4.DRFind("telNo","<","18839000").DRFindAnd("telNo asc","Like","00")
				.DRFindAnd("detached","=","false").getObjArray();
			System.out.println("DLT - bigi objlen="+obj.length);

		}catch (DRNoMatchException dnm){
			System.out.println("DLT - like99 err dn "+dnm.getMessage());
		}
		try{
			long tot = dl4.DRFind("postCode","=","CR 90").getLong("sum","postCode");
			System.out.println("DLT - glpostcode sum="+tot);

		}catch (DRNoMatchException dnm){
			System.out.println("DLT - getlpostcode err dn "+dnm.getMessage());
		}
		try{
			String str = dl4.DRFind("postCode","=","CR 90").getAvg("surName");
			System.out.println("DLT - avsurname avsurn="+str);

		}catch (DRNoMatchException dnm){
			System.out.println("DLT - avsurname err dn "+dnm.getMessage());
		}

		DRList<Integer> dl5 = new DRList<Integer>();
		for (int i = 0; i < 1001; i++) dl5.DRadd(Integer.valueOf(i));
		try{
			int min = dl5.DRFind("","Like","99").getMin("");
			int max = dl5.DRFind("","Like","99").getMax("");
			int avg = dl5.DRFind("","Like","99").getAvg("");
			BigDecimal sum = dl5.DRFind("","Like","99").getSum("","bigdecimal");
			System.out.println("DLT - dl5int99 minx="+min+" max="+max+" avg="+avg+" sum="+sum);

		}catch (DRNoMatchException dnm){
			System.out.println("DLT - intarray err dn "+dnm.getMessage());
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
		BigInteger telNo;
		boolean detached;
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






