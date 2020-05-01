
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
import projects.DRList.Jar.DRListTBL;
import projects.DRList.Jar.DRFind;
import projects.DRList.Jar.DRNoMatchException;

public class DRFindTest2
{

	//=================================================
	public static void main(String[] args) {


		//========================================
		nameVO nvo = new nameVO();
		//nameVO nvo1 = new nameVO();
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
			nvoa[i].purchaseLDT = LocalDateTime.ofInstant(Instant.ofEpochMilli(today - (i * oneday)), ZoneId.systemDefault());
			nvoa[i].purchaseLD = nvoa[i].purchaseLDT.toLocalDate();
			nvoa[i].purchaseDDate = Date.from(nvoa[i].purchaseLDT.atZone(ZoneId.systemDefault()).toInstant());
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
				nvoa[i].purchaseLDT = nvoa[i - 1].purchaseLDT;
				nvoa[i].purchaseLD = nvoa[i - 1].purchaseLD;
				nvoa[i].purchaseDDate = nvoa[i - 1].purchaseDDate;
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

			Object[] obj = dl4.DRFind("purchaseDate asc",">","<Date>:-10d").getObjArray();
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
			nameVO nvol = (nameVO)obj[obj.length - 1];
			System.out.println("DLT - fncteldateh objlen="+obj.length+" 1std="+df.format(nvo.purchaseDate)+
				" lastd="+df.format(nvol.purchaseDate));

			obj = dl4.DRFind("purchaseDate",">","<Date>:-10h").sortDsc("age").getObjArray();
			nvo = (nameVO)obj[0];
			System.out.println("DLT - age=dsc ol="+obj.length+" nam="+nvo.surName+
				" age="+nvo.age[0]+"!"+nvo.age[1]+"!"+nvo.age[2]+"!"+nvo.age[3]+"!"+nvo.age[4]+"!");

			//===============================================================================================
			System.out.println("***  LocalDateTime - using LDT");

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
			obj = dl4.DRFind("purchaseLDT asc",">","<Date>:-10d").getObjArray();
			nvo = (nameVO)obj[0];
			System.out.println("DLT - ldtd objlen="+obj.length+" lastd="+nvo.purchaseLDT.format(formatter));

			obj = dl4.DRFind("purchaseLDT asc",">","<Date>:-10w").getObjArray();
			nvo = (nameVO)obj[0];
			System.out.println("DLT - ldtw objlen="+obj.length+" lastd="+nvo.purchaseLDT.format(formatter));

			obj = dl4.DRFind("purchaseLDT asc",">","<Date>:-10h").getObjArray();
			nvo = (nameVO)obj[0];
			System.out.println("DLT - ldth objlen="+obj.length+" lastd="+nvo.purchaseLDT.format(formatter));

			obj = dl4.DRFind("purchaseLDT",">","<Date>:27-06-2019").DRFindAnd("purchaseLDT","<","<Date>:05-07-2019")
				.sortDsc("purchaseLDT").getObjArray();
			nvo = (nameVO)obj[0];
			System.out.println("DLT - idtstrd objlen="+obj.length+" sn="+nvo.surName+" lastd="+nvo.purchaseLDT.format(formatter));

			obj = dl4.DRFind("purchaseLDT",">","<Date>:27-06-2019").DRFindAnd("purchaseLDT","<","<Date>:05-07-2019")
				.sortDsc("getHouseNo").getObjArray();
			nvo = (nameVO)obj[0];
			System.out.println("DLT - fncdateh objlen="+obj.length+" sn="+nvo.surName+" lastd="+nvo.purchaseLDT.format(formatter));

			obj = dl4.DRFind("purchaseLDT asc",">","<Date>:Mon-2").DRFindAnd("purchaseLDT asc","<","<Date>:Mon2")
				.getObjArray();
			nvo = (nameVO)obj[0];
			System.out.println("DLT - ldtmon objlen="+obj.length+" lastd="+nvo.purchaseLDT.format(formatter));

			obj = dl4.DRFind("purchaseLDT asc",">","<Date>:Mon-2").DRFindAnd("purchaseLDT asc","<","<Date>:Mon2")
				.sortDsc("getTelNo").getObjArray();
			nvo = (nameVO)obj[0];
			nameVO nvo2 = (nameVO)obj[obj.length - 1];
			System.out.println("DLT - ldtmon objlen="+obj.length+" 1std="+nvo.purchaseLDT.format(formatter)+
				" lastd="+nvo2.purchaseLDT.format(formatter));

			obj = dl4.DRFind("purchaseLDT",">","<Date>:-10h").sortDsc("age").getObjArray();
			nvo = (nameVO)obj[0];
			System.out.println("DLT - age=dsc ol="+obj.length+" nam="+nvo.surName+
				" age="+nvo.age[0]+"!"+nvo.age[1]+"!"+nvo.age[2]+"!"+nvo.age[3]+"!"+nvo.age[4]+"!");

			//===================================================================================================
			System.out.println("***  LocalDate - using LD");

			DateTimeFormatter formatld = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			obj = dl4.DRFind("purchaseLD asc",">","<Date>:-10d").getObjArray();
			nvo = (nameVO)obj[0];
			System.out.println("DLT - ldtd objlen="+obj.length+" lastd="+nvo.purchaseLD.format(formatld));

			obj = dl4.DRFind("purchaseLD asc",">","<Date>:-10w").getObjArray();
			nvo = (nameVO)obj[0];
			System.out.println("DLT - ldtw objlen="+obj.length+" lastd="+nvo.purchaseLD.format(formatld));

			obj = dl4.DRFind("purchaseLD asc",">","<Date>:-10h").getObjArray();
			nvo = (nameVO)obj[0];
			System.out.println("DLT - ldth objlen="+obj.length+" lastd="+nvo.purchaseLD.format(formatld));

			obj = dl4.DRFind("purchaseLD",">","<Date>:27-06-2019").DRFindAnd("purchaseLD","<","<Date>:05-07-2019")
				.sortDsc("purchaseLD").getObjArray();
			nvo = (nameVO)obj[0];
			System.out.println("DLT - idtstrd objlen="+obj.length+" sn="+nvo.surName+" lastd="+nvo.purchaseLD.format(formatld));

			obj = dl4.DRFind("purchaseLD",">","<Date>:27-06-2019").DRFindAnd("purchaseLD","<","<Date>:05-07-2019")
				.sortDsc("getHouseNo").getObjArray();
			nvo = (nameVO)obj[0];
			System.out.println("DLT - fncdateh objlen="+obj.length+" sn="+nvo.surName+" lastd="+nvo.purchaseLD.format(formatld));

			obj = dl4.DRFind("purchaseLD asc",">","<Date>:Mon-2").DRFindAnd("purchaseLD asc","<","<Date>:Mon2")
				.getObjArray();
			nvo = (nameVO)obj[0];
			System.out.println("DLT - ldtmon objlen="+obj.length+" lastd="+nvo.purchaseLD.format(formatld));

			obj = dl4.DRFind("purchaseLD asc",">","<Date>:Mon-2").DRFindAnd("purchaseLD asc","<","<Date>:Mon2")
				.sortDsc("getTelNo").getObjArray();
			nvo = (nameVO)obj[0];
			nameVO nvo3 = (nameVO)obj[obj.length - 1];
			System.out.println("DLT - ldtmon objlen="+obj.length+" 1std="+nvo.purchaseLD.format(formatld)+
				" lastd="+nvo3.purchaseLD.format(formatld));

			obj = dl4.DRFind("purchaseLD",">","<Date>:-10h").sortDsc("age").getObjArray();
			nvo = (nameVO)obj[0];
			System.out.println("DLT - age=dsc ol="+obj.length+" nam="+nvo.surName+
				" age="+nvo.age[0]+"!"+nvo.age[1]+"!"+nvo.age[2]+"!"+nvo.age[3]+"!"+nvo.age[4]+"!");

			//===============================================================================================
			System.out.println("***  Date - using Date");

			DateTimeFormatter formatd = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss");
			obj = dl4.DRFind("purchaseDDate asc",">","<Date>:-10d").getObjArray();
			nvo = (nameVO)obj[0];
			System.out.println("DLT - ldtd objlen="+obj.length+" lastd="+df.format(nvo.purchaseDDate));

			obj = dl4.DRFind("purchaseDDate asc",">","<Date>:-10w").getObjArray();
			nvo = (nameVO)obj[0];
			System.out.println("DLT - ldtw objlen="+obj.length+" lastd="+df.format(nvo.purchaseDDate));

			obj = dl4.DRFind("purchaseDDate asc",">","<Date>:-10h").getObjArray();
			nvo = (nameVO)obj[0];
			System.out.println("DLT - ldth objlen="+obj.length+" lastd="+df.format(nvo.purchaseDDate));

			obj = dl4.DRFind("purchaseDDate",">","<Date>:27-06-2019").DRFindAnd("purchaseDDate","<","<Date>:05-07-2019")
				.sortDsc("purchaseDDate").getObjArray();
			nvo = (nameVO)obj[0];
			System.out.println("DLT - idtstrd objlen="+obj.length+" sn="+nvo.surName+" lastd="+df.format(nvo.purchaseDDate));

			obj = dl4.DRFind("purchaseDDate",">","<Date>:27-06-2019").DRFindAnd("purchaseDDate","<","<Date>:05-07-2019")
				.sortDsc("getHouseNo").getObjArray();
			nvo = (nameVO)obj[0];
			System.out.println("DLT - fncdateh objlen="+obj.length+" sn="+nvo.surName+" lastd="+df.format(nvo.purchaseDDate));

			obj = dl4.DRFind("purchaseDDate asc",">","<Date>:Mon-2").DRFindAnd("purchaseDDate asc","<","<Date>:Mon2")
				.getObjArray();
			nvo = (nameVO)obj[0];
			System.out.println("DLT - ldtmon objlen="+obj.length+" lastd="+df.format(nvo.purchaseDDate));

			obj = dl4.DRFind("purchaseDDate asc",">","<Date>:Mon-2").DRFindAnd("purchaseDDate asc","<","<Date>:Mon2")
				.sortDsc("getTelNo").getObjArray();
			nvo = (nameVO)obj[0];
			nameVO nvo4 = (nameVO)obj[obj.length - 1];
			System.out.println("DLT - ldtmon objlen="+obj.length+" 1std="+df.format(nvo.purchaseDDate)+
				" lastd="+df.format(nvo4.purchaseDDate));

			obj = dl4.DRFind("purchaseDDate",">","<Date>:-10h").sortDsc("age").getObjArray();
			nvo = (nameVO)obj[0];
			System.out.println("DLT - age=dsc ol="+obj.length+" nam="+nvo.surName+
				" age="+nvo.age[0]+"!"+nvo.age[1]+"!"+nvo.age[2]+"!"+nvo.age[3]+"!"+nvo.age[4]+"!");

			//=================================================================
			System.out.println("***** cross date testing");
			obj = dl4.DRFind("purchaseDate",">","<Date>:-10w").DRFindAnd("purchaseLDT","<","<Date>:10d").
				DRFindAnd("purchaseLD asc",">","<Date>:Mon-1").getObjArray();
			nvo = (nameVO)obj[0];
			System.out.println("DLT - crossdate1 objlen="+obj.length+" lastd="+df.format(nvo.purchaseDate));

		}catch (DRNoMatchException dnm){
			System.out.println("DLT - like99 err dn "+dnm.getMessage());
		}

		try
		{
			DateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
			Object[] obj = dl4.DRFind("purchaseDate",">","<Date>:-10x").getObjArray();
			nvo = (nameVO)obj[0];
			System.out.println("DLT - invdate1 objlen="+obj.length+" lastd="+df.format(nvo.purchaseDate));
		}catch (DRNoMatchException dnm){
			System.out.println("DLT - invalid date -10x "+dnm.getMessage());
		}
		try
		{
			DateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
			Object[] obj = dl4.DRFind("purchaseLDT",">","<Date>:30-02-2020").getObjArray();
			nvo = (nameVO)obj[0];
			System.out.println("DLT - invdate2 objlen="+obj.length+" lastd="+df.format(nvo.purchaseDate));
		}catch (DRNoMatchException dnm){
			System.out.println("DLT - invalid ldt 30feb "+dnm.getMessage());
		}
		try
		{
			DateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
			Object[] obj = dl4.DRFind("purchaseLDT",">","<Date>:29-02-2020 24:10:20").getObjArray();
			nvo = (nameVO)obj[0];
			System.out.println("DLT - invdate3 objlen="+obj.length+" lastd="+df.format(nvo.purchaseDate));
		}catch (DRNoMatchException dnm){
			System.out.println("DLT - invalid ldt TIME "+dnm.getMessage());
		}
		try
		{
			DateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
			Object[] obj = dl4.DRFind("purchaseLD",">","<Date>:29-02-2020 14:10:20").getObjArray();
			nvo = (nameVO)obj[0];
			System.out.println("DLT - invdate4 objlen="+obj.length+" lastd="+df.format(nvo.purchaseDate));
		}catch (DRNoMatchException dnm){
			System.out.println("DLT - invalid ld TIME "+dnm.getMessage());
		}

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
		private LocalDateTime purchaseLDT;
		private LocalDate purchaseLD;
		private Date purchaseDDate;
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






