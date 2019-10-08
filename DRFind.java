package projects.DRList.Jar;

import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;
import java.sql.Timestamp;
import java.time.*;

import projects.DRList.Jar.DRArrayList;
import projects.DRList.Jar.DRCode;
import projects.DRList.Jar.DRListTBL;
import projects.DRList.Jar.DRNoMatchException;
import projects.DRList.Jar.DRFindObjVO;
import projects.DRList.Jar.ProcessTypeVO;
import projects.DRList.Jar.DRFindVO;

import java.lang.reflect.*;
import java.math.*;

public class DRFind<T>
{
	long today = System.currentTimeMillis();
	long oneday = 24 * 3600 * 1000;
	long onehour = 3600 * 1000;
	long onemin = 60 * 1000;
	char[] di = {'s','m','h','d','w','M','y'};	//sec,min,hour,day,week,month,year
	long[] din = {1000, onemin, onehour, oneday, oneday * 7, oneday * 31, oneday * 365};
	String[] dow = {"Mon","Tue","Wed","Thu","Fri","Sat","Sun"};

	byte[] md = {31,28,31,30,31,30,31,31,30,31,30,31};
	byte[] lmd = {31,29,31,30,31,30,31,31,30,31,30,31};

	Timestamp ts = Timestamp.valueOf("2019-08-19 00:00:00");			//mon
	long baseDate = ts.getTime();

	long todaydow = ((Math.abs(baseDate - today)/oneday)%7); 

    	//===================================================================================
    	public static void debug1(String msg){
		//System.out.println(msg);
    	}
    	//===================================================================================
    	public static void debug2(String msg){
		System.out.println(msg);
    	}
	//====================================================
	public int getOpCnt(String operator) throws DRNoMatchException {

		int opCnt = 99;
		String[] opArray = {"<","=",">","LIKE","MIN","MAX"};
		String op = operator.toUpperCase();

		for (int i = 0; i < opArray.length; i++)
			if (op.equals(opArray[i])) opCnt = i;

		if (opCnt == 99) throw new DRNoMatchException("Invalid operator sign");

		return opCnt;
		
	}
	//================================================
	public DRFindVO setFieldType(String fieldName, DRFindVO vo, DRListTBL<T> drl) 
	{
		String validTypes = "String|Integer|Short|Byte|Long|Boolean|BigDecimal|BigInteger|Character";
		String[] x = fieldName.split(" ");			//if asc/dsc at end of fieldname the sorted to true
		fieldName = x[0];
		if (x.length == 2){
			if (x[1].equals("asc")) vo.setSortAsc(true); 
			if (x[1].equals("dsc")) vo.setSortDsc(true);
		}
		debug1("vf - start fn="+fieldName+" siz="+drl.size);
		Class c1 = drl.dl.obj.getClass();
		Field f = null;
		try{
			f = c1.getDeclaredField(fieldName);
			vo.setFieldF1(f);
			f.setAccessible(true);

		}catch (NoSuchFieldException nsf){
			vo.setFieldType("");
			if (fieldName.equals("asc")) vo.setSortAsc(true);  
			if (fieldName.equals("dsc")) vo.setSortDsc(true);
			fieldName = "";
		}
		String fieldType = "";
		if (fieldName.length() == 0){
			String[] z = c1.toString().split("\\.");
			fieldType = z[z.length - 1];
			//debug2("st - ft=["+fieldType+"] c1="+c1.toString());
			if (validTypes.indexOf(fieldType) == -1) fieldType = "";
		}else{
			fieldType = f.getType().toString();
			String[] z = fieldType.split("\\.");
			fieldType = z[z.length - 1];
			fieldType = Character.toUpperCase(fieldType.charAt(0)) + fieldType.substring(1);  //set 1st char touppercase
			if (fieldType.equals("Int")) fieldType = "Integer";
		}
		vo.setFieldType(fieldType);
		vo.setFieldName(fieldName);
		return vo;
	}
	//=======================================================
	public boolean validDayMonth(String dateStr)
	{
		try
		{
			String[] x = dateStr.split(" ");
			String[] y = x[0].split("-");
			byte dd = Byte.parseByte(y[0]);
			byte mm = Byte.parseByte(y[1]);
			int yy = Integer.parseInt(y[2]);

			//2004 was leap year use that as the base, year 2000 was not a leap year
			byte[] dom = (Math.abs(2004 - yy) % 4 == 0 && yy%100 != 0) ? lmd : md;			//leapyear
			return ((mm > 0 || mm < 13) || (dd > 0 || dd <= dom[mm - 1]));

		}catch (Exception e){
			//System.out.println("vdate excep - ["+dateStr+"]");
			return false;
		}
	}
	//=======================================================
	public boolean validTime(String dateStr)
	{
		try
		{
			String[] x = dateStr.split(" ");
			String[] y = x[0].split("-");
			byte hh = 0;
			byte mi = 0;
			byte ss1 = 0;

			if (x.length > 1){
				y = x[1].split(":");
				hh = Byte.parseByte(y[0]);
				mi = Byte.parseByte(y[1]);
				int pos = (y[2].indexOf(".") == -1) ? y[2].length() : y[2].indexOf(".");
				ss1 = Byte.parseByte(y[2].substring(0,pos));
			}

			return (hh >= 0 || hh < 24 || mi >= 0 || mi < 60 || ss1 >= 0 || ss1 < 60);

		}catch (Exception e){
			//System.out.println("vdate excep - ["+dateStr+"]");
			return false;
		}
	}
	//=======================================================
	public boolean isValidDate(String dateStr)
	{
		return (validDayMonth(dateStr) && validTime(dateStr));
	}
	//================================================
	public DRFindVO validateDate(String fieldName, String value, DRFindVO vo, DRListTBL<T> drl, String op) 
		throws DRNoMatchException
	{
		DRFind<T> drf = new DRFind<T>();
		vo = drf.setFieldType(fieldName, vo, drl);

		if (!vo.getFieldType().equals("Long")) throw new DRNoMatchException("Valid date fieldtype not found");

		//the field type is long now we need to check that the format is correct date format or an integer
		vo.setDateInterval(' ');
		for (char x:di) if (x == value.charAt(value.length() -  1)) vo.setDateInterval(x);
		byte one = 1;								// format is dd-mm-yyyy
		if (vo.getDateInterval() == ' ') one = 0;				//format is 10d
		String dateStr = value.substring(value.indexOf("<Date>:")+7, value.length() - one);
		if (vo.getDateInterval() == ' '){
			boolean dowFound = false;
			int dowi = 0;
			for (int i = 0; i < dow.length; i++) {
				if (dateStr.indexOf(dow[i]) > -1){ 
					dowFound = true;
					dowi = i;			//0=mon...6=sun
					break;
				}
			} 
			if (dowFound){
				int weekno = 0;
				try{
					if (dateStr.length() == 3) weekno = 0;
					else weekno = Integer.parseInt(dateStr.substring(3,dateStr.length()));
				}catch (Exception e){throw new DRNoMatchException(e.getMessage()+" Invalid number with day of week - ["+dateStr+"]");}
				long dowdate = (today + (dowi - todaydow)*oneday) + weekno * oneday * 7;	//dow next week week
				DateFormat df = new SimpleDateFormat("dd-MM-yyyy 00:00:00");
				dateStr = df.format(dowdate);
				vo.setDateStr(dateStr);
			}else{
				vo.setDateIntervalNum(0);
				if (dateStr.indexOf(":") == -1) dateStr = dateStr + " 00:00:00";
				if (isValidDate(dateStr)) vo.setDateStr(dateStr); else throw new DRNoMatchException("Date is not valid");
			}
		}else{ 
			vo.setDateIntervalNum(Integer.parseInt(dateStr));
		}
		vo = drf.setStartDate(vo);

		drf.createMethods(vo);
		setValue(vo.getStartDate() + "",vo); 
		vo.setVOrigString(vo.getStartDate() + "");

		//DateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
		//debug2("vald - ds="+dateStr+" eov="+value.charAt(value.length() - 1)+" di="+vo.getDateInterval()+" val="+value+
		//	" sd="+df.format(vo.getStartDate()));
		return vo;
	}
	//================================================
	public DRFindVO setStartDate(DRFindVO vo) 
	{
		if (vo.getDateIntervalNum() == 0){
			String[] x = vo.getDateStr().split(" ");
			String[] y = x[0].split("-");
			//debug2("sd - ds="+vo.getDateStr());
			Timestamp ts1 = Timestamp.valueOf(y[2]+"-"+y[1]+"-"+y[0]+" "+"00:00:00");	//format "2019-12-31 00:00:00"
			vo.setStartDate(ts1.getTime());
		}else{
			for (int i = 0; i < di.length; i++) 
				if (vo.getDateInterval() == di[i]) vo.setStartDate(today + (vo.getDateIntervalNum() * din[i]));
		}
		return vo;
	}
	//================================================
	public DRFindVO validateField(String fieldName, String value, DRFindVO vo, DRListTBL<T> drl, String op) 
		throws DRNoMatchException
	{
		DRFind<T> drf = new DRFind<T>();
		vo = drf.setFieldType(fieldName, vo, drl);
		if (vo.getFieldType().equals("")) throw new DRNoMatchException("Valid fieldtype not found for validateField");

		//check all types of fields using invoke which returns a boolean
		op = op.toUpperCase();
		if (!op.equals("MIN") && !op.equals("MAX")){
			try{
				String methodName = "isValid"+vo.getFieldType();
				Class[] cArg = new Class[1]; 
				cArg[0] = String.class;
				ProcessTypeVO pt = new ProcessTypeVO();
  				Method m = pt.getClass().getDeclaredMethod(methodName,cArg);
				Object[] param = {value};
				Boolean ret = (Boolean) m.invoke(pt,param);
				if (!ret) throw new DRNoMatchException("Value parameter not suitable for field parameter "+vo.getFieldType());

			} catch (InvocationTargetException|IllegalAccessException|NullPointerException|NoSuchMethodException xx){ 
				throw new DRNoMatchException("Value parameter not suitable for field parameter "+xx.toString());
			}
		}
		vo = createMethods(vo);

		setValue(value,vo);						
		vo.setVOrigString(value);						
		debug1("vf - end val="+getValue(vo)+" ft="+vo.getFieldType());
		return vo;
	}
	//================================================
	public DRFindVO createMethods(DRFindVO vo) throws DRNoMatchException
	{
		//define all methods used for later use as once we have field type we can define the methods 
		//once which are invoked later
		try{
			String methodName = "setO"+vo.getFieldType();
			Class[] cArg = new Class[1]; 
			cArg[0] = String.class;
 			Method m = vo.getClass().getDeclaredMethod(methodName,cArg);
			vo.setmSetO(m);

			methodName = "setV"+vo.getFieldType();
 			m = vo.getClass().getDeclaredMethod(methodName,cArg);
			vo.setmSetV(m);

			methodName = "getV"+vo.getFieldType();
 			m = vo.getClass().getDeclaredMethod(methodName);
			vo.setmGetV(m);

			methodName = "getO"+vo.getFieldType();
 			m = vo.getClass().getDeclaredMethod(methodName);
			vo.setmGetO(m);

			methodName = "compare"+vo.getFieldType();
 			m = vo.getClass().getDeclaredMethod(methodName);
			vo.setmCompare(m);

		} catch (NullPointerException|NoSuchMethodException xx){ 
			throw new DRNoMatchException("Error setting vo methods r "+xx.toString());
		}
		return vo;
	}
	//================================================
	public T[] DRFindObj(String fieldName, String operator, String value, DRListTBL<T> drl) 
		throws DRNoMatchException
	{
		DRFind<T> drf = new DRFind<T>();
		DRFindVO vo = new DRFindVO();

		if (drl.fdl.obj == null) throw new DRNoMatchException("ListTbl is null");

		vo = drf.validateField(fieldName, value, vo, drl, operator);
		T[] obj = drf.DRFindInvoke(drf.getOpCnt(operator), vo, drl);
		return obj;
	}
	//================================================
	public DRFindObjVO<T> DRFind(String fieldName, String operator, String value, DRListTBL<T> drl) 
		throws DRNoMatchException
	{
		DRFind<T> drf = new DRFind<T>();
		DRFindVO vo = new DRFindVO();

		if (drl.fdl.obj == null) throw new DRNoMatchException("ListTbl is null");

		//check if date is being validated, this will have <Date>: in the value field
		if (value.indexOf("<Date>:") > -1)
			vo = drf.validateDate(fieldName, value, vo, drl, operator);
		else
			vo = drf.validateField(fieldName, value, vo, drl, operator);
		T[] obj = drf.DRFindInvoke(drf.getOpCnt(operator), vo, drl);

		DRFindObjVO<T> avo = new DRFindObjVO<T>();
		avo.setObjArray(obj);
		avo.setDRL(drl);
		return avo;
	}
	//================================================
	public DRFindObjVO<T> FindAnd(String fieldName, String operator, String value, T[] obj) 
		throws DRNoMatchException
	{
		DRFind<T> drf = new DRFind<T>();
		DRCode<T> drc = new DRCode<T>();
		DRListTBL<T> ndrl = new DRListTBL<T>();
		DRFindVO vo = new DRFindVO();

		for (int i = 0; i < obj.length; i++)
			drc.DRadd(obj[i],ndrl);

		DRFindObjVO<T> avo = drf.DRFind(fieldName, operator, value, ndrl);
		return avo;
	}
	//================================================
	public DRFindObjVO<T> FindOr(String fieldName, String operator, String value, DRListTBL<T> drl, T[] obj) 
		throws DRNoMatchException
	{
		DRFind<T> drf = new DRFind<T>();
		DRCode<T> drc = new DRCode<T>();
		DRFindObjVO<T> avo = new DRFindObjVO<T>();

		if (drl.fdl.obj == null) throw new DRNoMatchException("ListTbl is null");

		T[] obj1 = drf.DRFindObj(fieldName, operator, value, drl);
		T[] obj2 = drf.combineObjs(obj,obj1);
		avo.setObjArray(obj2);
		avo.setDRL(drl);
		return avo;
	}
	//================================================
	public DRFindObjVO<T> FindMinus(String fieldName, String operator, String value, T[] obj) 
		throws DRNoMatchException
	{
		DRFind<T> drf = new DRFind<T>();
		DRCode<T> drc = new DRCode<T>();
		DRListTBL<T> ndrl = new DRListTBL<T>();
		DRFindVO vo = new DRFindVO();

		for (int i = 0; i < obj.length; i++)
			drc.DRadd(obj[i],ndrl);

		T[] obj1 = drf.DRFindObj(fieldName, operator, value, ndrl);

		T[] obj2 = drf.minusObjs(obj,obj1);
		DRFindObjVO<T> avo = new DRFindObjVO<T>();

		avo.setObjArray(obj2);
		return avo;
	}
	//================================================
	public T[] DRFindObject(String fieldName, String operator, String value, T[] obj) 
		throws DRNoMatchException
	{
		DRFind<T> drf = new DRFind<T>();
		DRCode<T> drc = new DRCode<T>();
		DRListTBL<T> ndrl = new DRListTBL<T>();
		DRFindVO vo = new DRFindVO();

		for (int i = 0; i < obj.length; i++)
			drc.DRadd(obj[i],ndrl);

		T[] obj1 = drf.DRFind(fieldName, operator, value, ndrl).getObjArray();

		return obj1;
	}
	//==============================================================
	public T[] minusObjs(T[] obj1, T[] obj2) throws DRNoMatchException
	{
	
		DRListTBL<T> dl1 = new DRListTBL<T>();
		DRCode<T> drc = new DRCode<T>();

		boolean matched = false;
		for (int i = 0; i < obj1.length; i++){
			matched = false;
			for (int j = 0; j < obj2.length; j++){
				if (obj1[i] == obj2[j]){
					matched = true;
					break;
				} 
			}
			if (!matched) drc.DRadd(obj1[i],dl1);
		}

		int size = drc.DRsize(dl1);
		debug1("f= - after comp siz="+size+" dl1o=");
		if (size == 0) throw new DRNoMatchException("DRFindMinus returns no objects");

		@SuppressWarnings("unchecked")
		T[] o = (T[]) java.lang.reflect.Array.newInstance(dl1.dl.obj.getClass(), size);

		o[0] = drc.DRgetFirst(dl1).dl.obj;
		for (int i = 1; i < size; i++)
			o[i] = drc.DRnext(dl1).dl.obj;

		debug1("f= - end ol="+o.length);
		return o;
	}

	//================================================
	public DRFindObjVO<T> distinct(T[] obj1) 
		throws DRNoMatchException
	{
		DRFind<T> drf = new DRFind<T>();
		DRCode<T> drc = new DRCode<T>();
		DRListTBL<T> ndrl = new DRListTBL<T>();
		DRFindVO vo = new DRFindVO();

		for (int i = 0; i < obj1.length - 1; i++){
			for (int j = i + 1; j < obj1.length; j++){
				if (checkFields(obj1[i],obj1[j])){
					obj1[i] = null;
					break;
				}
			}
			if (obj1[i] != null) drc.DRadd(obj1[i],ndrl);
		}
		drc.DRadd(obj1[obj1.length - 1],ndrl);

		@SuppressWarnings("unchecked")
		T[] nobj = (T[]) java.lang.reflect.Array.newInstance(ndrl.dl.obj.getClass(), drc.DRsize(ndrl));
		ndrl = drc.DRgetFirst(ndrl);
		for (int i = 0; i < nobj.length; i++){
			nobj[i] = ndrl.dl.obj;
			ndrl = drc.DRnext(ndrl);
		}

		DRFindObjVO<T> avo = new DRFindObjVO<T>();
		avo.setObjArray(nobj);

		return avo;
	}
	//================================================
	public boolean checkFields(T obj1, T obj2){

		boolean ret = false;
		Class c1 = obj1.getClass();
		Class c2 = obj2.getClass();
		if (c1 != c2) return false;
		String str1 = "";
		String str2 = "";
		int brcnt = 0;
		int i = 0;
		try{
			Field[] f1 = c1.getDeclaredFields();
			Field[] f2 = c2.getDeclaredFields();

			for (i = 0; i < f1.length; i++){
				try {
					f1[i].setAccessible(true);
					str1 = (String)f1[i].get(obj1).toString();
				} catch (NullPointerException npe) {
					str1 = "null";
				}
				try {
					f2[i].setAccessible(true);
					str2 = (String)f2[i].get(obj2).toString();
				} catch (NullPointerException npe) {
					str2 = "null";
				}
				if (str1.equals(str2)){
					//System.out.println("cf - i="+i+" s1="+str1+" s2="+str2+" f1l="+f1.length+" nam="+f1[i].getName());
					brcnt++;
				}
			}
			if (brcnt == i) ret = true;
			return ret;
		}catch (IllegalAccessException nsf){
			str1 = (String)obj1;
			str2 = (String)obj2;
			if (str1.equals(str2)) ret = true;
			return ret;
		}
	}
	//================================================
	public double getDouble(String op, String fieldName, T[] obj)
		throws DRNoMatchException
	{
		DRFind<T> drf = new DRFind<T>();
		DRListTBL<T> ndrl = new DRListTBL<T>();
		DRCode<T> drc = new DRCode<T>();
		DRFindVO vo = new DRFindVO();

		if (obj.length == 0) throw new DRNoMatchException("Object is null for getting average");

		for (int i = 0; i < obj.length; i++) drc.DRadd(obj[i],ndrl);

		vo = drf.setFieldType(fieldName, vo, ndrl);
		vo = createMethods(vo);

		double dval = 0.0d;
		for (int i = 0; i < obj.length; i++)
			dval += drf.getDoubleValue(obj[i],vo);

		if (op.equals("avg")) dval = dval / obj.length;

		return dval;		
	}
	//================================================
	public BigDecimal getBigDecimal(String op, String fieldName, T[] obj)
		throws DRNoMatchException
	{
		DRFind<T> drf = new DRFind<T>();
		DRListTBL<T> ndrl = new DRListTBL<T>();
		DRCode<T> drc = new DRCode<T>();
		DRFindVO vo = new DRFindVO();

		if (obj.length == 0) throw new DRNoMatchException("Object is null for getting BigDecimal average");

		for (int i = 0; i < obj.length; i++) drc.DRadd(obj[i],ndrl);

		vo = drf.setFieldType(fieldName, vo, ndrl);

		if (!vo.getFieldType().equals("BigDecimal") && !op.equals("sum"))
			throw new DRNoMatchException("Field type is not of type BigDecimal - "+vo.getFieldType());

		BigDecimal bigval = new BigDecimal("0.0");
		BigDecimal bigtot = new BigDecimal("0.0");
		for (int i = 0; i < obj.length; i++){
			T x = obj[i];
			if (vo.getFieldName().length() == 0){
				bigval = new BigDecimal(x.toString());
				bigtot = bigtot.add(bigval);
			}else{
				try{
					bigval = (BigDecimal) vo.getFieldF1().get(x);
					if (bigval==null) bigval = BigDecimal.valueOf(0); 
					bigtot = bigtot.add(bigval);
				} catch (IllegalArgumentException|IllegalAccessException iae) {
					debug1("sov - iae - "+vo.getFieldName());
					throw new DRNoMatchException("bigdec error for "+vo.getFieldName()+" "+iae.toString());
				}
			}
		}
		if (op.equals("avg")) bigtot = bigtot.divide(new BigDecimal(obj.length),5);

		return bigtot;
	}
	//================================================
	public BigInteger getBigInteger(String op, String fieldName, T[] obj)
		throws DRNoMatchException
	{
		DRFind<T> drf = new DRFind<T>();
		DRListTBL<T> ndrl = new DRListTBL<T>();
		DRCode<T> drc = new DRCode<T>();
		DRFindVO vo = new DRFindVO();

		if (obj.length == 0) throw new DRNoMatchException("Object is null for getting BigInteger average");

		for (int i = 0; i < obj.length; i++) drc.DRadd(obj[i],ndrl);

		vo = drf.setFieldType(fieldName, vo, ndrl);

		if (!vo.getFieldType().equals("BigInteger") && !op.equals("sum"))
			throw new DRNoMatchException("Field type is not of type BigInteger");

		BigInteger bigval = new BigInteger("0");
		BigInteger bigtot = new BigInteger("0");
		for (int i = 0; i < obj.length; i++){
			T x = obj[i];
			if (vo.getFieldName().length() == 0){
				bigval = new BigInteger(x.toString());
				bigtot = bigtot.add(bigval);
			}else{
				try{
					bigval = (BigInteger) vo.getFieldF1().get(x);
					if (bigval==null) bigval = BigInteger.valueOf(0); 
					bigtot = bigtot.add(bigval);
				} catch (IllegalArgumentException|IllegalAccessException iae) {
					debug1("sov - iae - "+vo.getFieldName());
					throw new DRNoMatchException("bigint exception for "+vo.getFieldName()+" "+iae.toString());
				}
			}
		}
		if (op.equals("avg")) bigtot = bigtot.divide(new BigInteger(Integer.toString(obj.length)));

		return bigtot;
	}
	//================================================
	public long getLong(String op, String fieldName, T[] obj)
		throws DRNoMatchException
	{
		DRFind<T> drf = new DRFind<T>();
		DRListTBL<T> ndrl = new DRListTBL<T>();
		DRCode<T> drc = new DRCode<T>();
		DRFindVO vo = new DRFindVO();

		if (obj.length == 0) throw new DRNoMatchException("Object is null for getting average");

		for (int i = 0; i < obj.length; i++) drc.DRadd(obj[i],ndrl);

		vo = drf.setFieldType(fieldName, vo, ndrl);
		if (vo.getFieldType().equals("String") || vo.getFieldType().equals("Character")|| vo.getFieldType().equals("Boolean"))
			throw new DRNoMatchException("Field type is not of type numeric");
		vo = createMethods(vo);

		double dval = 0.0d;
		for (int i = 0; i < obj.length; i++)
			dval += drf.getDoubleValue(obj[i],vo);

		if (op.equals("avg")) dval = dval / obj.length;

		String dStr = Double.toString(dval);
		long lval = 0;
		if (vo.getFieldType().equals("BigDecimal")) lval  = BigDecimal.valueOf(dval).longValue();
		else lval = Long.parseLong(dStr.substring(0,dStr.indexOf(".")));

		return lval;
	}
	//===============================================================
	public double getDoubleValue(Object x, DRFindVO vo) throws DRNoMatchException
	{
		//debug("sov start - fn="+" fl=");
		double dval = 0.0d;
		String xstr = "";
		if (vo.getFieldName().length() == 0) xstr = x.toString(); 
		else{ 
			try{
				xstr = vo.getFieldF1().get(x).toString();
			} catch (IllegalArgumentException|IllegalAccessException|NullPointerException iae) {
				debug2("gdv - xs="+xstr+" "+iae.toString());
				throw new DRNoMatchException("getDoubleF"+vo.getFieldType()+" - "+vo.getFieldName()+" "+iae.toString());
			}
		}
		ProcessTypeVO pt = new ProcessTypeVO();
		dval = Double.parseDouble(xstr);
		return dval;
	}
	//================================================
	public T[] DRFindInvoke(int opCnt, DRFindVO vo, DRListTBL<T> drl) throws DRNoMatchException
	{
		String[] opTxt = {"Less","Equals","Greater","Like","Min","Max"};
		debug1("finv - start "+opCnt+" optxt="+opTxt[opCnt]);

		DRFind<T> drf = new DRFind<T>();
		T[] obj;
		if (opTxt[opCnt].equals("Like")){
			obj = drf.DRFindLike(vo,drl);
		}else if (opTxt[opCnt].equals("Min")){
			try{
				String methodName = "compareValsLess";
				Class[] cArg = new Class[1]; 
				cArg[0] = Integer.class;
 				Method m = vo.getClass().getDeclaredMethod(methodName,cArg);
				vo.setmCompareVals(m);
			} catch (NullPointerException|NoSuchMethodException xx){ 
				throw new DRNoMatchException("Error declaring compareMin method "+xx.toString());
			}
			obj = drf.DRFindMinMax(vo,drl);
		}else if (opTxt[opCnt].equals("Max")){
			try{
				String methodName = "compareValsGreater";
				Class[] cArg = new Class[1]; 
				cArg[0] = Integer.class;
 				Method m = vo.getClass().getDeclaredMethod(methodName,cArg);
				vo.setmCompareVals(m);
			} catch (NullPointerException|NoSuchMethodException xx){ 
				throw new DRNoMatchException("Error declaring compareMax method "+xx.toString());
			}
			obj = drf.DRFindMinMax(vo,drl);
		}else{
			try{
				String methodName = "compareVals"+opTxt[opCnt];
				Class[] cArg = new Class[1]; 
				cArg[0] = Integer.class;
 				Method m = vo.getClass().getDeclaredMethod(methodName,cArg);
				vo.setmCompareVals(m);
			} catch (NullPointerException|NoSuchMethodException xx){ 
				throw new DRNoMatchException("Error declaring compareVals method optcnt="+opCnt+" "+xx.toString());
			}
			obj = drf.DRFindEqGtLt(vo,drl);
		}

		if (vo.getSortDsc() || vo.getSortAsc()) obj = sortObjs(obj,vo);
		debug1("f= - end ol="+obj.length);

		return obj;
	}

	//==============================================================
	public T[] DRFindEqGtLt(DRFindVO vo, DRListTBL<T> drl) throws DRNoMatchException
	{
	
		DRListTBL<T> dl1 = new DRListTBL<T>();
		DRCode<T> drc = new DRCode<T>();

		drl.dl = drl.fdl;

		setObjValue(drl.dl.obj,vo);

		debug1("fegl - start val="+getValue(vo));

		int j = 0;
		for (int i = 0; i < drc.DRsize(drl); i++){
			setObjValue(drl.dl.obj,vo);
			if (compareVals(vo)){
				drc.DRadd(drl.dl.obj,dl1);
			}
			drl = drc.DRnext(drl);
			if (drl.success == -1) break;
		}
		int size = drc.DRsize(dl1);
		debug1("f= - after comp siz="+size+" j="+j+" oval="+(String)getObjValue(vo));
		if (size == 0) throw new DRNoMatchException("No matches found for selection criteria oval="+(String)getObjValue(vo));

		@SuppressWarnings("unchecked")
		T[] o = (T[]) java.lang.reflect.Array.newInstance(dl1.dl.obj.getClass(), size);

		o[0] = drc.DRgetFirst(dl1).dl.obj;
		for (int i = 1; i < size; i++)
			o[i] = drc.DRnext(dl1).dl.obj;

		return o;
	}
	//==============================================================
	public T[] DRFindMinMax(DRFindVO vo, DRListTBL<T> drl) throws DRNoMatchException
	{
	
		DRListTBL<T> dl1 = new DRListTBL<T>();
		DRCode<T> drc = new DRCode<T>();
		DRArrayList<T> dl2 = new DRArrayList<T>();

		drl.dl = drl.fdl;

		dl2.obj = drl.dl.obj;
		setObjValue(drl.dl.obj,vo);
		setValue((String)getObjValue(vo),vo);
		debug1("fmin - start val="+getValue(vo));
		int j = 0;
		for (int i = 0; i < drc.DRsize(drl); i++){
			setObjValue(drl.dl.obj,vo);
			if (compareVals(vo)){
				setValue((String)getObjValue(vo),vo);
				dl2.obj = drl.dl.obj;
			}
			drl = drc.DRnext(drl);
			if (drl.success == -1) break;
		}
		if (dl2.obj == null) throw new DRNoMatchException("No min selection criteria oval="+(String)getObjValue(vo));

		@SuppressWarnings("unchecked")
		T[] o = (T[]) java.lang.reflect.Array.newInstance(drl.dl.obj.getClass(), 1);
		o[0] = dl2.obj;
		return o;
	}
	//==============================================================
	public T[] DRFindLike(DRFindVO vo, DRListTBL<T> drl) throws DRNoMatchException
	{
	
		DRListTBL<T> dl1 = new DRListTBL<T>();
		DRCode<T> drc = new DRCode<T>();

		drl.dl = drl.fdl;

		setObjValue(drl.dl.obj,vo);

		debug1("fegl - start val="+getValue(vo));

		int j = 0;
		for (int i = 0; i < drc.DRsize(drl); i++){
			setObjValue(drl.dl.obj,vo);
			if (compareLike(vo)){
				drc.DRadd(drl.dl.obj,dl1);
			}
			drl = drc.DRnext(drl);
			if (drl.success == -1) break;
		}
		int size = drc.DRsize(dl1);
		debug1("f= - after comp siz="+size+" j="+j+" oval="+(String)getObjValue(vo));
		if (size == 0) throw new DRNoMatchException("No matches found for selection criteria oval="+(String)getObjValue(vo));

		@SuppressWarnings("unchecked")
		T[] o = (T[]) java.lang.reflect.Array.newInstance(dl1.dl.obj.getClass(), size);

		o[0] = drc.DRgetFirst(dl1).dl.obj;
		for (int i = 1; i < size; i++)
			o[i] = drc.DRnext(dl1).dl.obj;

		return o;
	}
	//=========================================================
	public DRListTBL<T> DRsortNoKey(DRListTBL<T> drl,int asc,String sortName) throws DRNoMatchException{

		DRCode<T> drcode = new DRCode<T>();
		DRFind<T> drf = new DRFind<T>();
		DRFindVO vo = new DRFindVO();
		final DRFindVO fvo = vo;

		DRArrayList<T>[] xx = drcode.toArraySub(drl.dl,drl.fdl,drl.size);
		debug1("sank - xxl="+xx.length+" xsk="+xx[xx.length - 1].sortKey);

		vo = drf.setFieldType(sortName, vo, drl);
		if (vo.getFieldType().equals("")) throw new DRNoMatchException("Valid fieldtype not found for sortnokey");
		vo = createMethods(vo);

		final String ft = vo.getFieldType();
		final String fn = vo.getFieldName();

		Arrays.sort(xx, new Comparator<DRArrayList<T>>() {
			@Override
			public int compare(DRArrayList<T> dl1, DRArrayList<T> dl2) {
				if (compareObjs(dl1.obj, dl2.obj, fvo)==1){		// is dl1 > dl2 then true
					return asc;					// if asc = 1 then asc if -1 desc
				}else{
					return asc*-1;
				}
			}
		});

		drl = drcode.clear(drl);
		drl = drcode.toDRListsub(xx,drl);

		Arrays.fill(xx, null );				//clear down the array
		debug1("sank - size="+drl.size);

		return drl;
	}
	//==============================================================
	public T[] sortObjs(T[] objs, DRFindVO vo) throws DRNoMatchException
	{

		final String ft = vo.getFieldType();
		final String fn = vo.getFieldName();
		final DRFindVO fvo = vo;

		int ascv = 0;
		if (vo.getSortAsc()) ascv = 1;
		if (vo.getSortDsc()) ascv = -1;
		final int asc = ascv;

		debug1("sob = asc="+vo.getSortAsc()+" dsc="+vo.getSortDsc());

		Arrays.sort(objs, new Comparator<T>() {
			@Override
			public int compare(T o1, T o2)
			{
				if (compareObjs(o1, o2, fvo)==1){	// is o1 > o2 then true
					return asc;			// if asc = 1 then asc if -1 desc
				}else{
					return asc*-1;
				}
			}
		});

		return objs;
	}
	//==============================================================
	public int compareObjs(T obj1, T obj2, DRFindVO fvo)
	{
		int greater = 0;
		try
		{

			setObjValue(obj1,fvo);
			setValue((String)getObjValue(fvo),fvo);
			setObjValue(obj2,fvo);

			try{
				greater = (int)fvo.getmCompare().invoke(fvo);

			} catch (IllegalAccessException|InvocationTargetException|NullPointerException xx){ 
				System.out.println("compareObjs - "+xx.toString());
				return 0;
			}
			return greater;

		}catch (DRNoMatchException dnm){
			System.out.println("compareObjs - "+dnm.getMessage());
			return 0;
		}
	}
	//==============================================================
	public T[] combineObjs(T[] obj1, T[] obj2) throws DRNoMatchException
	{
	
		DRListTBL<T> dl1 = new DRListTBL<T>();
		DRCode<T> drc = new DRCode<T>();

		for (int i = 0; i < obj1.length; i++){
			for (int j = 0; j < obj2.length; j++){
				if (obj1[i] == obj2[j]) obj2[j] = null;
			}
			drc.DRadd(obj1[i],dl1);
		}
		for (int i = 0; i < obj2.length; i++)
			if (obj2[i] != null) drc.DRadd(obj2[i],dl1);

		int size = drc.DRsize(dl1);
		debug1("f= - after comp siz="+size+" dl1o=");
		if (size == 0) throw new DRNoMatchException("No objects to combine for FindOR");

		@SuppressWarnings("unchecked")
		T[] o = (T[]) java.lang.reflect.Array.newInstance(dl1.dl.obj.getClass(), size);

		o[0] = drc.DRgetFirst(dl1).dl.obj;
		for (int i = 1; i < size; i++)
			o[i] = drc.DRnext(dl1).dl.obj;

		debug1("f= - end ol="+o.length);
		return o;
	}
	//==============================================================
	public String getFieldType(String fieldName, T[] obj) throws DRNoMatchException
	{
		DRFind<T> drf = new DRFind<T>();
		DRListTBL<T> ndrl = new DRListTBL<T>();
		DRCode<T> drc = new DRCode<T>();
		DRFindVO vo = new DRFindVO();

		if (obj.length == 0) throw new DRNoMatchException("Object is null for getting field type");

		for (int i = 0; i < obj.length; i++) drc.DRadd(obj[i],ndrl);

		vo = drf.setFieldType(fieldName, vo, ndrl);

		return vo.getFieldType();
	}
	//==============================================================
	public String[] getFieldString(String fieldName, T[] obj) throws DRNoMatchException
	{
		DRFind<T> drf = new DRFind<T>();
		DRListTBL<T> ndrl = new DRListTBL<T>();
		DRCode<T> drc = new DRCode<T>();
		DRFindVO vo = new DRFindVO();

		if (obj.length == 0) throw new DRNoMatchException("Object is null for getting field type");

		for (int i = 0; i < obj.length; i++) drc.DRadd(obj[i],ndrl);
		vo = drf.setFieldType(fieldName, vo, ndrl);
		vo = drf.createMethods(vo);
			
		String[] xx = new String[obj.length];
		for (int i = 0; i < obj.length; i++){ 
			drf.setObjValue(obj[i],vo);
			xx[i] = (String)drf.getObjValue(vo);
		}

		return xx;
	}
	//===============================================================
	public void setObjValue(Object x, DRFindVO vo) throws DRNoMatchException
	{
		String str = "";
		if (vo.getFieldName().length() == 0){
			str = x.toString();
		}else{
			try{
				str = vo.getFieldF1().get(x).toString();
			}catch (IllegalArgumentException|IllegalAccessException|NullPointerException nsm){
				debug2("sov - nsm "+vo.getFieldName()+" "+vo.getFieldType());
				throw new DRNoMatchException("Error finding field - "+vo.getFieldName()+" "+nsm.toString());
			}
		}
		try{
			Object[] param = {str};
			vo.getmSetO().invoke(vo,param);

		} catch (IllegalAccessException|NullPointerException|InvocationTargetException xx){ 
			debug2("sov end - ft="+vo.getFieldType()+" str="+str+" fn="+vo.getFieldName());
			throw new DRNoMatchException("Error setObjValue variables "+xx.toString());
		}

	}
	//==================================================
	public Object getObjValue(DRFindVO vo)  throws DRNoMatchException {

		String retStr = "";
		try{
			retStr = (String)vo.getmGetO().invoke(vo);
			//debug2("sov end - ft="+vo.getFieldType()+" os="+vo.oString);

		} catch (IllegalAccessException|InvocationTargetException xx){ 
			throw new DRNoMatchException("Error getObjValue object variables "+xx.toString());
		} catch (NullPointerException npe){
			retStr = "0";
		}
		return retStr;
	}
	//============================================
	public void setValue(String x, DRFindVO vo) throws DRNoMatchException {
		//debug("sv - x="+x+" ft="+fieldType);

		if (x.length() == 0 && !vo.getFieldType().equals("String")) x = "0";
	
		try{
			Object[] param = {x};
			vo.getmSetV().invoke(vo,param);
			//debug2("sov end - ft="+vo.getFieldType());

		} catch (IllegalAccessException|NullPointerException|InvocationTargetException xx){ 
			throw new DRNoMatchException("Error setValue object variables "+xx.toString());
		}
	}
	//================================================	
	public Object getValue(DRFindVO vo)  throws DRNoMatchException {

		String retStr = "";
		try{
			retStr = (String)vo.getmGetV().invoke(vo);
			//debug2("sov end - ft="+vo.getFieldType());

		} catch (IllegalAccessException|InvocationTargetException xx){ 
			throw new DRNoMatchException("Error getting object variables "+xx.toString());
		} catch (NullPointerException npe){
			retStr = "0";
		}
		return retStr;
	}
	//=======================================================
	public Boolean compareVals(DRFindVO vo) throws DRNoMatchException {

		Boolean foundVal;
		try{
			int greater = (int)vo.getmCompare().invoke(vo);
			Object[] param = {greater};
			foundVal = (Boolean)vo.getmCompareVals().invoke(vo,param);

		} catch (IllegalAccessException|InvocationTargetException|NullPointerException xx){ 
			throw new DRNoMatchException("Error comparing object variables "+xx.toString());
		}

		return foundVal;
	}
	//=======================================================
	public boolean compareLike(DRFindVO vo) throws DRNoMatchException {

		String vstr = vo.getVOrigString();
		String ostr = (String)getObjValue(vo);

		int x1 = ostr.indexOf(vstr);
		return (x1 > -1);
	}
}