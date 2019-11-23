
package projects.DRList.Jar;

import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;
import java.sql.Timestamp;
import java.time.*;
import java.math.*;
import java.lang.reflect.*;

import java.time.format.*;
import java.time.temporal.*;

import projects.DRList.Jar.DRList;
import projects.DRList.Jar.DRListNoFind;
import projects.DRList.Jar.DRFind;
import projects.DRList.Jar.DRNoMatchException;

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
		long oneday = 24 * 3600 * 1000;
		long today = System.currentTimeMillis() + oneday * 100;

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
			nvoa[i].purchaseDate = today - (i * oneday);
			nvoa[i].age[0] = (byte)(l % 100);
			nvoa[i].age[1] = (byte)((l % 100) + 1);
			nvoa[i].age[2] = (byte)((l % 100) + 2);
			nvoa[i].age[3] = (byte)((l % 100) + 3);
			nvoa[i].age[4] = (byte)((l % 100) + 4);
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
				nvoa[i].purchaseDate = nvoa[i - 1].purchaseDate;
				nvoa[i].age = nvoa[i - 1].age;
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
			Object[] obj = dl4.DRFind("surName","=","Number100").DRFindOr("surName","=","Number101").
				DRFindOr("surName","=","Number102").getObjArray();
			nvo = (nameVO)obj[0];
			System.out.println("DLT - or3= found snam="+nvo.surName+" sal="+nvo.salary+" ol="+obj.length);

			obj = dl4.DRFind("surName","=","Number101").DRFindOr("surName","=","Number101").DRFindOr("surName","=","Number102")
				.getObjArray();
			nvo = (nameVO)obj[0];
			System.out.println("DLT - or3>2 found snam="+nvo.surName+" sal="+nvo.salary+" ol="+obj.length);
			obj = dl4.DRFind("getFirstName","=","Dave101").DRFindOr("getFirstName","=","Dave101")
				.DRFindOr("getFirstName","=","Dave102").getObjArray();
			nvo = (nameVO)obj[0];
			System.out.println("DLT - fnc=2 found snam="+nvo.surName+" sal="+nvo.salary+" ol="+obj.length);

			DRListNoFind<nameVO> xdrl = dl4.DRFind("surName","=","Number100").DRFindOr("surName","=","Number102").getDRList();
			DRList<nameVO> ndrl =  dl4.convertToDRList(xdrl);
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
			obj = dl4.DRFind("getHouseNo",">","-99").DRFindAnd("getHouseNo","<","99").DRFindAnd("getHouseNo",">","9").getObjArray();
			nvo = (nameVO)obj[0];
			System.out.println("DLT - fnc= found snam="+nvo.surName+" sal="+nvo.salary+" ol="+obj.length);

			obj = dl4.DRFind("houseVal",">","199000").DRFindAnd("houseVal","<","199900").DRFindAnd("houseVal asc","Like","00")
				.DRFindAnd("detached","=","false").getObjArray();
			for (int i = 0; i < obj.length; i++){
				nvo = (nameVO)obj[i];
				System.out.println("DLT - houseVal found snam="+nvo.surName+" hv="+nvo.houseVal+" sal="+nvo.salary										+" "+nvo.detached);
			}

			int cnt = dl4.DRFind("houseVal",">","195000").DRFindAnd("houseVal","<","195100").DRFindAnd("detached","=","true")
				.getCount();
			System.out.println("DLT - true cnt="+cnt);

			float favg = dl4.DRFind("houseVal",">","195000").DRFindAnd("houseVal","<","195100").DRFindAnd("houseVal asc","Like","8")
				.getAvg("salary");
			System.out.println("DLT - favg="+favg);
			int iavg = dl4.DRFind("houseVal",">","195000").DRFindAnd("houseVal","<","195100").DRFindAnd("houseVal asc","Like","8")
				.getAvg("houseNo");
			BigDecimal bdavg = dl4.DRFind("postCode","=","CR 90").getAvg("houseVal");
			BigDecimal bdmin = dl4.DRFind("postCode","=","CR 90").getMin("houseVal");
			BigDecimal bdmax = dl4.DRFind("postCode","=","CR 90").getMax("houseVal");
			System.out.println("DLT - favg="+favg+" iavg="+iavg+" bdavg="+bdavg+" minbd="+bdmin+" maxbd="+bdmax);

			long tot = dl4.DRFind("houseVal",">","195000").DRFindAnd("houseVal","<","195100").DRFindAnd("houseVal asc","Like","8")
				.getSum("salary","LONG");
			int toti = dl4.DRFind("houseVal",">","195000").DRFindAnd("houseVal","<","195100").DRFindAnd("houseVal asc","Like","8")
				.getSum("salary","INTEGER");
			float totf = dl4.DRFind("houseVal",">","195000").DRFindAnd("houseVal","<","195100").DRFindAnd("houseVal asc","Like","8")
				.getSum("salary","FLOAT");
			double totd = dl4.DRFind("houseVal",">","195000").DRFindAnd("houseVal","<","195100").DRFindAnd("houseVal asc","Like","8")
				.getSum("salary","DOUBLE");
			System.out.println("DLT - tot="+tot+" toti="+toti+" totf="+totf+" totd="+totd);
			tot = dl4.DRFind("houseVal",">","195000").DRFindAnd("houseVal","<","195100").DRFindAnd("houseVal asc","Like","8")
				.getSum("getHouseNo","LONG");
			toti = dl4.DRFind("houseVal",">","195000").DRFindAnd("houseVal","<","195100").DRFindAnd("houseVal asc","Like","8")
				.getSum("getHouseNo","INTEGER");
			totf = dl4.DRFind("houseVal",">","195000").DRFindAnd("houseVal","<","195100").DRFindAnd("houseVal asc","Like","8")
				.getSum("getHouseNo","FLOAT");
			totd = dl4.DRFind("houseVal",">","195000").DRFindAnd("houseVal","<","195100").DRFindAnd("houseVal asc","Like","8")
				.getSum("getHouseNo","DOUBLE");
			System.out.println("DLT - fnc hn tot="+tot+" toti="+toti+" totf="+totf+" totd="+totd);

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
			maxi = dl4.DRFind("postCode","=","CR 90").getMax("getTelNo");
			mini = dl4.DRFind("postCode","=","CR 90").getMin("getTelNo");
			sumi = dl4.DRFind("postCode","=","CR 90").getSum("getTelNo","biginteger");
			avgi = dl4.DRFind("postCode","=","CR 90").getAvg("getTelNo");
			System.out.println("DLT - funcbigi mini="+mini+" maxi="+maxi+" sumi="+sumi+" avgi="+avgi);

			obj = dl4.DRFind("telNo","<","18839000").DRFindAnd("telNo asc","Like","00")
				.DRFindAnd("detached","=","false").getObjArray();
			System.out.println("DLT - bigi objlen="+obj.length);
			obj = dl4.DRFind("getTelNo","<","18839000").DRFindAnd("getTelNo asc","Like","00")
				.DRFindAnd("detached","=","false").getObjArray();
			System.out.println("DLT - bigifnc objlen="+obj.length);

			obj = dl4.DRFind("purchaseDate asc",">","<Date>:-10d").getObjArray();
			nvo = (nameVO)obj[0];
			DateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
			System.out.println("DLT - dated objlen="+obj.length+" lastd="+df.format(nvo.purchaseDate));

			obj = dl4.DRFind("purchaseDate asc",">","<Date>:-10w").getObjArray();
			nvo = (nameVO)obj[0];
			System.out.println("DLT - datew objlen="+obj.length+" lastd="+df.format(nvo.purchaseDate));

			obj = dl4.DRFind("purchaseDate asc",">","<Date>:-10h").getObjArray();
			nvo = (nameVO)obj[0];
			System.out.println("DLT - dateh objlen="+obj.length+" lastd="+df.format(nvo.purchaseDate));

			obj = dl4.DRFind("purchaseDate",">","<Date>:27-06-2019").DRFindAnd("purchaseDate","<","<Date>:05-07-2019")
				.sortDsc("purchaseDate").getObjArray();
			nvo = (nameVO)obj[0];
			System.out.println("DLT - dateh objlen="+obj.length+" sn="+nvo.surName+" lastd="+df.format(nvo.purchaseDate));
			obj = dl4.DRFind("purchaseDate",">","<Date>:27-06-2019").DRFindAnd("purchaseDate","<","<Date>:05-07-2019")
				.sortDsc("getHouseNo").getObjArray();
			nvo = (nameVO)obj[0];
			System.out.println("DLT - fncdateh objlen="+obj.length+" sn="+nvo.surName+" lastd="+df.format(nvo.purchaseDate));

			obj = dl4.DRFind("purchaseDate asc",">","<Date>:Mon-2").DRFindAnd("purchaseDate asc","<","<Date>:Mon2")
				.getObjArray();
			nvo = (nameVO)obj[0];
			System.out.println("DLT - dateh objlen="+obj.length+" lastd="+df.format(nvo.purchaseDate));
			obj = dl4.DRFind("purchaseDate asc",">","<Date>:Mon-2").DRFindAnd("purchaseDate asc","<","<Date>:Mon2")
				.sortDsc("getTelNo").getObjArray();
			nvo = (nameVO)obj[0];
			System.out.println("DLT - fncteldateh objlen="+obj.length+" lastd="+df.format(nvo.purchaseDate));

			obj = dl4.DRFind("age",">","19").DRFindAnd("age","<","22").getObjArray();
			nvo = (nameVO)obj[0];
			System.out.println("DLT - age> ol="+obj.length+" age="+nvo.age[0]+"!"+nvo.age[1]+"!"+nvo.age[2]+"!"+
						nvo.age[3]+"!"+nvo.age[4]+"!");
			obj = dl4.DRFind("age","=","19").getObjArray();
			nvo = (nameVO)obj[0];
			System.out.println("DLT - age=19 ol="+obj.length+" nam="+nvo.surName+
					" age="+nvo.age[0]+"!"+nvo.age[1]+"!"+nvo.age[2]+"!"+nvo.age[3]+"!"+nvo.age[4]+"!");
			obj = dl4.DRFind("age","=","19").DRFindAnd("age","max","").getObjArray();
			nvo = (nameVO)obj[0];
			System.out.println("DLT - age=max ol="+obj.length+" nam="+nvo.surName+
				" age="+nvo.age[0]+"!"+nvo.age[1]+"!"+nvo.age[2]+"!"+nvo.age[3]+"!"+nvo.age[4]+"!");
			obj = dl4.DRFind("age","=","19").DRFindAnd("age","min","").getObjArray();
			nvo = (nameVO)obj[0];
			System.out.println("DLT - age=min ol="+obj.length+" nam="+nvo.surName+
				" age="+nvo.age[0]+"!"+nvo.age[1]+"!"+nvo.age[2]+"!"+nvo.age[3]+"!"+nvo.age[4]+"!");
			obj = dl4.DRFind("purchaseDate",">","<Date>:-10h").sortDsc("age").getObjArray();
			nvo = (nameVO)obj[0];
			System.out.println("DLT - age=dsc ol="+obj.length+" nam="+nvo.surName+
				" age="+nvo.age[0]+"!"+nvo.age[1]+"!"+nvo.age[2]+"!"+nvo.age[3]+"!"+nvo.age[4]+"!");

			Byte[] iarray = (Byte[])dl4.DRFind("age",">","19").DRFindAnd("age","<","22").getFieldValue("age[2]");
			System.out.println("DLT - agefv - len="+iarray.length+" i0="+iarray[0]+" lst-"+iarray[iarray.length - 1]);

			Byte[] iarray1 = (Byte[])dl4.DRFind("age",">","19").DRFindAnd("age","<","22").getFieldValue("age");
			System.out.println("DLT - agefv1 - len="+iarray1.length+" i0="+iarray1[0]+" lst-"+iarray1[iarray.length - 1]);
		}catch (DRNoMatchException dnm){
			System.out.println("DLT - like99 err dn "+dnm.getMessage());
		}
		try{
			long tot = dl4.DRFind("postCode","=","CR 90").getSum("postCode","Long");
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
		try{
			String str = dl4.DRFind("getHouseNo","=","CR 90").getMax("surName");
			System.out.println("DLT - fnc= max="+str);

		}catch (DRNoMatchException dnm){
			System.out.println("DLT - fnc= err dn "+dnm.getMessage());
		}
		try{
			String str = dl4.DRFind("setHouseNo","=","90").getMax("surName");
			System.out.println("DLT - invfnc= max="+str);

		}catch (DRNoMatchException dnm){
			System.out.println("DLT - invfnc= err dn "+dnm.getMessage());
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
		String dateStr = "2019-02-30 00:00:00";
		Timestamp ts1 = Timestamp.valueOf("2019-12-31 00:00:00");
		long t1 = ts1.getTime(); 
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
		DRFind drf= new DRFind();

		Timestamp ts = Timestamp.valueOf("2019-08-26 00:00:00");			//mon
		long baseDate = ts.getTime();

		long todaydow = 7 - ((Math.abs(baseDate - today)/oneday)%7 + 1); 
		long cdt1 = 1473670749000l;
		Object tobj = new BigDecimal(10.0);
		String z = tobj.getClass().toString();
		z = "float";
		String[] z1 = z.split("\\.");
		System.out.println("TST - 2day="+today+" gt="+ts1.getTime()+" cdt1="+df.format(cdt1)+
				" tdow="+(z1[z1.length - 1]));
		System.out.println("DLT - ti="+(System.currentTimeMillis() - sti)+"ms");

	}
	//============================================
	public static class nameVO {

		private String firstName;
		private String surName;
		private int houseNo;
		private String postCode;
		private float salary;
		private BigDecimal houseVal;
		private BigInteger telNo;
		private boolean detached;
		private long purchaseDate;
		private byte[] age = new byte[5];

		public String getFirstName(){
			return firstName;
		}
		public int getHouseNo(){
			return houseNo;
		}
		public void setHouseNo(int x){
			houseNo = x;
		}
		public BigInteger getTelNo(){
			return telNo;
		}
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






