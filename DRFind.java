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
import DRList.DRFindObjVO;

import java.lang.reflect.*;
import java.math.*;

public class DRFind<T>
{
    	//===================================================================================
    	public static void debug1(String msg){
		//System.out.println(msg);
    	}
	//====================================================
	public int getOpCnt(String operator) throws DRNoMatchException {

		int opCnt = 99;
		String[] opArray = {"<","=",">","Like","Min","Max"};

		for (int i = 0; i < opArray.length; i++)
			if (operator.equals(opArray[i])) opCnt = i - 1;

		if (opCnt == 99) throw new DRNoMatchException("Invalid operator sign");

		return opCnt;
		
	}
	//================================================
	public DRFindVO setFieldType(String fieldName, DRFindVO vo, DRListTBL<T> drl) 
	{
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
		}catch (NoSuchFieldException nsf){
			vo.setFieldType("");
			if (fieldName.equals("asc")) vo.setSortAsc(true);  
			if (fieldName.equals("dsc")) vo.setSortDsc(true);
			fieldName = "";
		}
		debug1("vf - start1 fn="+fieldName);
		if (fieldName.length() == 0){
			if (drl.dl.obj instanceof String) vo.setFieldType("String");
			if (drl.dl.obj instanceof Boolean) vo.setFieldType("boolean");
			if (drl.dl.obj instanceof Integer) vo.setFieldType("int");
			if (drl.dl.obj instanceof Float) vo.setFieldType("float");
			if (drl.dl.obj instanceof Byte) vo.setFieldType("byte");
			if (drl.dl.obj instanceof Double) vo.setFieldType("double");
			if (drl.dl.obj instanceof Long) vo.setFieldType("long");
			if (drl.dl.obj instanceof Short) vo.setFieldType("short");
			if (drl.dl.obj instanceof Character) vo.setFieldType("char");
			if (drl.dl.obj instanceof BigDecimal) vo.setFieldType("BigDecimal");
			if (drl.dl.obj instanceof BigInteger) vo.setFieldType("BigInteger");
			vo.setFieldName(fieldName);
		}else{
			String fieldType = f.getType().toString();
			if (fieldType.indexOf("String") > -1) fieldType = "String";
			if (fieldType.indexOf("BigDecimal") > -1) fieldType = "BigDecimal";
			if (fieldType.indexOf("BigInteger") > -1) fieldType = "BigInteger";
			vo.setFieldType(fieldType);
			vo.setFieldName(fieldName);
		}
		return vo;
	}
	//================================================
	public DRFindVO validateField(String fieldName, String value, DRFindVO vo, DRListTBL<T> drl, String op) 
		throws DRNoMatchException
	{
		DRFind<T> drf = new DRFind<T>();
		vo = drf.setFieldType(fieldName, vo, drl);

		if (vo.getFieldType().equals("")) throw new DRNoMatchException("Valid fieldtype not found");
		if (vo.getFieldType().equals("String")){
			vo.setValue(value);						
			vo.setVOrigString(value);						
		}else if (vo.getFieldType().equals("boolean")){
			String valBool = value.toUpperCase();
			if (!valBool.equals("TRUE")&&!valBool.equals("FALSE")) throw new DRNoMatchException("Invalid value for Boolean "+value);
			vo.setValue(value);						
			vo.setVOrigString(value);						
		}else{
			if (op.equals("Min") || op.equals("Max")){
				vo.setValue(value);					//this should be ""
				vo.setVOrigString(value);						
			}else{
				boolean ret1 = false;
				try{
					if (vo.getFieldType().equals("float")) Float.parseFloat(value);
					if (vo.getFieldType().equals("double")) Double.parseDouble(value);
					if (vo.getFieldType().equals("int")) Integer.parseInt(value);
					if (vo.getFieldType().equals("byte")) Byte.parseByte(value);
					if (vo.getFieldType().equals("long")) Long.parseLong(value);
					if (vo.getFieldType().equals("short")) Short.parseShort(value);
					if (vo.getFieldType().equals("BigDecimal")) new BigDecimal(value);
					if (vo.getFieldType().equals("BigInteger")) new BigInteger(value);

					ret1 = true;
				}catch (NumberFormatException e){ret1 = false;}
				debug1("vf - start2 ret="+ret1+" v="+value);
				if (ret1){ 
					vo.setValue(value); 
					vo.setVOrigString(value);						
				}
				else throw new DRNoMatchException("Value parameter not suitable for field parameter");
			}
		}
		debug1("vf - end val="+vo.getValue()+" ft="+vo.getFieldType());
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

		if (vo.getFieldType().equals("String") || vo.getFieldType().equals("char"))
			throw new DRNoMatchException("Field type is not of type numeric");

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

		if (!vo.getFieldType().equals("BigDecimal"))
			throw new DRNoMatchException("Field type is not of type BigDecimal");

		BigDecimal bigval = new BigDecimal("0.0");
		BigDecimal bigtot = new BigDecimal("0.0");
		for (int i = 0; i < obj.length; i++){
			T x = obj[i];
			if (vo.getFieldName().length() == 0){
				bigval = (BigDecimal)x;
				bigtot = bigtot.add(bigval);
			}else{
				try{
					Class c1 = x.getClass();
					Field f = c1.getDeclaredField(vo.getFieldName());
					f.setAccessible(true);
					bigval = (BigDecimal) f.get(x);
					if (bigval==null) bigval = BigDecimal.valueOf(0); 
					bigtot = bigtot.add(bigval);
				}catch (NoSuchFieldException nsm){
					debug1("sov - nsm "+vo.getFieldName());
					throw new DRNoMatchException("NoSuchFieldExcep - bigdec "+vo.getFieldName());
				} catch (IllegalArgumentException iae) {
					debug1("sov - iae - "+vo.getFieldName());
					throw new DRNoMatchException("IllegalArgumentExcep - bigdec "+vo.getFieldName());
				}catch (IllegalAccessException acce) {
					debug1("sov - acce - "+vo.getFieldName());
					throw new DRNoMatchException("IllegalAccessExcep - bigdec "+vo.getFieldName());
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

		if (!vo.getFieldType().equals("BigInteger"))
			throw new DRNoMatchException("Field type is not of type BigInteger");

		BigInteger bigval = new BigInteger("0");
		BigInteger bigtot = new BigInteger("0");
		for (int i = 0; i < obj.length; i++){
			T x = obj[i];
			if (vo.getFieldName().length() == 0){
				bigval = (BigInteger)x;
				bigtot = bigtot.add(bigval);
			}else{
				try{
					Class c1 = x.getClass();
					Field f = c1.getDeclaredField(vo.getFieldName());
					f.setAccessible(true);
					bigval = (BigInteger) f.get(x);
					if (bigval==null) bigval = BigInteger.valueOf(0); 
					bigtot = bigtot.add(bigval);
				}catch (NoSuchFieldException nsm){
					debug1("sov - nsm "+vo.getFieldName());
					throw new DRNoMatchException("NoSuchFieldExcep - bigint "+vo.getFieldName());
				} catch (IllegalArgumentException iae) {
					debug1("sov - iae - "+vo.getFieldName());
					throw new DRNoMatchException("IllegalArgumentExcep - bigint "+vo.getFieldName());
				}catch (IllegalAccessException acce) {
					debug1("sov - acce - "+vo.getFieldName());
					throw new DRNoMatchException("IllegalAccessExcep - bigint "+vo.getFieldName());
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

		if (vo.getFieldType().equals("String") || vo.getFieldType().equals("char"))
			throw new DRNoMatchException("Field type is not of type numeric");

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
		if (vo.getFieldName().length() == 0){
			if (vo.getFieldType().equals("int")) dval = Double.parseDouble(Integer.toString((Integer)x));
			if (vo.getFieldType().equals("long")) dval = Double.parseDouble(Long.toString((Long)x));
			if (vo.getFieldType().equals("float")) dval = Double.parseDouble(Float.toString((Float)x));
			if (vo.getFieldType().equals("byte")) dval = Double.parseDouble(Byte.toString((Byte)x));
			if (vo.getFieldType().equals("double")) dval = Double.valueOf((Double)x);
			if (vo.getFieldType().equals("short")) dval = Double.parseDouble(Short.toString((Short)x));
			if (vo.getFieldType().equals("BigDecimal")) dval = Double.parseDouble(((BigDecimal)x).toString());
			if (vo.getFieldType().equals("BigInteger")) dval = Double.parseDouble(((BigInteger)x).toString());
		}else{
			try{
				//debug("sov - x="+x.toString()+" ft="+fieldType+" fn="+fieldName);
				Class c1 = x.getClass();
				Field f = c1.getDeclaredField(vo.getFieldName());
				f.setAccessible(true);
				if (vo.getFieldType().equals("int")) dval = Double.parseDouble(Integer.toString(f.getInt(x)));
				if (vo.getFieldType().equals("long")) dval = Double.parseDouble(Long.toString(f.getLong(x)));
				if (vo.getFieldType().equals("float")) dval = Double.parseDouble(Float.toString(f.getFloat(x)));
				if (vo.getFieldType().equals("byte")) dval = Double.parseDouble(Byte.toString(f.getByte(x)));
				if (vo.getFieldType().equals("double")) dval = Double.valueOf(f.getDouble(x));
				if (vo.getFieldType().equals("short")) dval = Double.parseDouble(Short.toString(f.getShort(x)));
				if (vo.getFieldType().equals("BigDecimal")) dval = Double.parseDouble(((BigDecimal) f.get(x)).toString());  //new BigDecimal((String)f.get(x));
				if (vo.getFieldType().equals("BigInteger")) dval = Double.parseDouble(((BigInteger) f.get(x)).toString());  //new BigInteger((String)f.get(x));

			}catch (NullPointerException npe){
				dval = 0.0;
			}catch (NoSuchFieldException nsm){
				debug1("sov - nsm "+vo.getFieldName());
				throw new DRNoMatchException("NoSuchFieldExcep - "+vo.getFieldName());
			} catch (IllegalArgumentException iae) {
				debug1("sov - iae - "+vo.getFieldName());
				throw new DRNoMatchException("IllegalArgumentExcep - "+vo.getFieldName());
			}catch (IllegalAccessException acce) {
				debug1("sov - acce - "+vo.getFieldName());
				throw new DRNoMatchException("IllegalAccessExcep - "+vo.getFieldName());
			}
		}
		//debug("sov end - os=");
		return dval;
	}
	//================================================
	public T[] DRFindInvoke(int opCnt, DRFindVO vo, DRListTBL<T> drl) throws DRNoMatchException
	{
		debug1("finv - start "+opCnt);

		DRFind<T> drf = new DRFind<T>();

		T[] obj = drf.DRFindEqGtLt(vo,drl,opCnt);
		return obj;
	}

	//==============================================================
	public T[] DRFindEqGtLt(DRFindVO vo, DRListTBL<T> drl, int greater) throws DRNoMatchException
	{
	
		DRListTBL<T> dl1 = new DRListTBL<T>();
		DRArrayList<T> dl2 = new DRArrayList<T>();
		DRCode<T> drc = new DRCode<T>();

		drl.dl = drl.fdl;

		vo.setObjValue(drl.dl.obj);

		if (greater > 2){ 
			vo.setValue((String)vo.getObjValue());
			dl2.obj = drl.dl.obj;
		}
		debug1("fegl - start val="+vo.getValue()+" gre="+greater);

		int j = 0;
		for (int i = 0; i < drc.DRsize(drl); i++){
			vo.setObjValue(drl.dl.obj);
			if (greater < 2){							// <,=,>
				if (vo.compareVals() == greater){
					j++;
 					drc.DRadd(drl.dl.obj,dl1);
				}
			}else if (greater == 2){						//like
				if (vo.compareLike() == 1) drc.DRadd(drl.dl.obj,dl1);
			}else if (greater == 3){						//Min
				if (vo.compareVals() == -1){ 					
					vo.setValue((String)vo.getObjValue());
					dl2.obj = drl.dl.obj;
				}
			}else if (greater == 4){						//Max
				if (vo.compareVals() == 1){ 
					vo.setValue((String)vo.getObjValue());
					dl2.obj = drl.dl.obj;
				}
			}
			drl = drc.DRnext(drl);
			if (drl.success == -1) break;
		}
		int size = drc.DRsize(dl1);
		debug1("f= - after comp siz="+size+" j="+j+" oval="+(String)vo.getObjValue());
		if (greater > 2){
			size = 1;
			drc.DRadd(dl2.obj,dl1);
			debug1("f= - min/max dl1s="+drc.DRsize(dl1));
		}
		if (size == 0) throw new DRNoMatchException("No matches found for selection criteria");

		@SuppressWarnings("unchecked")
		T[] o = (T[]) java.lang.reflect.Array.newInstance(dl1.dl.obj.getClass(), size);

		o[0] = drc.DRgetFirst(dl1).dl.obj;
		for (int i = 1; i < size; i++)
			o[i] = drc.DRnext(dl1).dl.obj;

		if (vo.getSortDsc() || vo.getSortAsc()) o = sortObjs(o,vo);
		debug1("f= - end ol="+o.length);
		return o;
	}
	//=========================================================
	public DRListTBL<T> DRsortNoKey(DRListTBL<T> drl,int asc,String sortName) throws DRNoMatchException{

		DRCode<T> drcode = new DRCode<T>();
		DRFind<T> drf = new DRFind<T>();
		DRFindVO vo = new DRFindVO();

		DRArrayList<T>[] xx = drcode.toArraySub(drl.dl,drl.fdl,drl.size);
		debug1("sank - xxl="+xx.length+" xsk="+xx[xx.length - 1].sortKey);

		vo = drf.setFieldType(sortName, vo, drl);
		if (vo.getFieldType().equals("")) throw new DRNoMatchException("Valid fieldtype not found");

		final String ft = vo.getFieldType();
		final String fn = vo.getFieldName();

		Arrays.sort(xx, new Comparator<DRArrayList<T>>() {
			@Override
			public int compare(DRArrayList<T> dl1, DRArrayList<T> dl2) {
				if (compareObjs(dl1.obj, dl2.obj, ft, fn)==1){		// is dl1 > dl2 then true
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

		int ascv = 0;
		if (vo.getSortAsc()) ascv = 1;
		if (vo.getSortDsc()) ascv = -1;
		final int asc = ascv;

		debug1("sob = asc="+vo.sortAsc+" dsc="+vo.sortDsc);

		Arrays.sort(objs, new Comparator<T>() {
			@Override
			public int compare(T o1, T o2)
			{
				if (compareObjs(o1, o2, ft, fn)==1){	// is o1 > o2 then true
					return asc;			// if asc = 1 then asc if -1 desc
				}else{
					return asc*-1;
				}
			}
		});

		return objs;
	}
	//==============================================================
	public int compareObjs(T obj1, T obj2, String ft, String fn)
	{
		try
		{
			DRFindVO fvo = new DRFindVO();
			fvo.setFieldType(ft);
			fvo.setFieldName(fn);

			fvo.setObjValue(obj1);
			fvo.setValue((String)fvo.getObjValue());
			fvo.setObjValue(obj2);

			debug1("cobj = ft="+ft+" fn="+fn);

			return fvo.compareVals()*-1;

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

		String[] xx = new String[obj.length];
		for (int i = 0; i < obj.length; i++){ 
			vo.setObjValue(obj[i]);
			xx[i] = (String)vo.getObjValue();;
		}

		return xx;
	}
	//=====================================================
	public class DRFindVO
	{
		private String fieldName;
		private String fieldType;

		private String vOrigString;
		private String vString;
		private int vInt;
		private byte vByte;
		private short vShort;
		private long vLong;
		private float vFloat;
		private double vDouble;
		private char vChar;
		private boolean vBool;
		private BigDecimal vbgd;
		private BigInteger vbgi;

		private String oString;
		private int oInt;
		private byte oByte;
		private short oShort;
		private long oLong;
		private float oFloat;
		private double oDouble;
		private char oChar;
		private boolean oBool;
		private BigDecimal obgd;
		private BigInteger obgi;

		String zeroes = "000000000000000000";

		private boolean sortAsc;
		private boolean sortDsc;

    		//===================================================================================
    		public void debug(String msg){
			//System.out.println(msg);
 		}

		public void setSortDsc(boolean x){
			this.sortDsc = x;
		}
		public boolean getSortDsc(){
			return this.sortDsc;
		}
		public void setSortAsc(boolean x){
			this.sortAsc = x;
		}
		public boolean getSortAsc(){
			return this.sortAsc;
		}
		public void setFieldName(String x){
			this.fieldName = x;
		}
		public String getFieldName(){
			return this.fieldName;
		}
		public String getVOrigString(){
			return this.vOrigString;
		}
		public void setVOrigString(String x){
			this.vOrigString = x;
		}
		public void setFieldType(String x){
			this.fieldType = x;
		}
		public String getFieldType(){
			return this.fieldType;
		}
		//============================================
		public void setValue(String x){
			//debug("sv - x="+x+" ft="+fieldType);

			if (x.length() == 0 && !fieldType.equals("String")) x = "0";
	
			if (fieldType.equals("String")) vString = x;
			if (fieldType.equals("int")) vInt = Integer.parseInt(x);
			if (fieldType.equals("boolean")) vBool = Boolean.parseBoolean(x);
			if (fieldType.equals("long")) vLong = Long.parseLong(x);
			if (fieldType.equals("float")) vFloat = Float.parseFloat(x);
			if (fieldType.equals("byte")) vByte = Byte.parseByte(x);
			if (fieldType.equals("double")) vDouble = Double.parseDouble(x);
			if (fieldType.equals("short")) vShort = Short.parseShort(x);
			if (fieldType.equals("char")) vChar = x.charAt(0);
			if (fieldType.equals("BigDecimal")) vbgd = new BigDecimal(x);
			if (fieldType.equals("BigInteger")) vbgi = new BigInteger(x);

		}
		//================================================	
		public Object getValue(){
			if (fieldType.equals("int")) vString = Integer.toString(oInt);
			if (fieldType.equals("long")) vString = Long.toString(oLong);
			if (fieldType.equals("float")) vString = Float.toString(oFloat);
			if (fieldType.equals("byte")) vString = Byte.toString(oByte);
			if (fieldType.equals("double")) vString = Double.toString(oDouble);
			if (fieldType.equals("short")) vString = Short.toString(oShort);
			if (fieldType.equals("char")) vString = Character.toString(oChar);
			if (fieldType.equals("BigDecimal")) vString = vbgd.toString();
			if (fieldType.equals("BigInteger")) vString = vbgi.toString();
			return vString;
		}
		//===============================================================
		public void setObjValue(Object x) throws DRNoMatchException
		{
			//debug("sov start - fn="+" fl=");
			if (this.fieldName.length() == 0){
				if (fieldType.equals("String")) oString = (String)x;
				if (fieldType.equals("int")) oInt = Integer.valueOf((Integer)x);
				if (fieldType.equals("boolean")) oBool = (Boolean)x;
				if (fieldType.equals("long")) oLong = Long.valueOf((Long)x);
				if (fieldType.equals("float")) oFloat = Float.valueOf((Float)x);
				if (fieldType.equals("byte")) oByte = Byte.valueOf((Byte)x);
				if (fieldType.equals("double")) oDouble = Double.valueOf((Double)x);
				if (fieldType.equals("short")) oShort = Short.valueOf((Short)x);
				if (fieldType.equals("char")) oChar = Character.valueOf((Character)x);
				if (fieldType.equals("BigDecimal")) obgd = (BigDecimal) x;
				if (fieldType.equals("BigInteger")) obgi = (BigInteger) x;
			}else{
				try{
					//debug("sov - x="+x.toString()+" ft="+fieldType+" fn="+fieldName);
					Class c1 = x.getClass();
					Field f = c1.getDeclaredField(fieldName);
					f.setAccessible(true);
					if (fieldType.equals("boolean")) oBool = Boolean.valueOf(f.getBoolean(x));
					if (fieldType.equals("String")) oString = (String)f.get(x);
					if (fieldType.equals("int")) oInt = Integer.valueOf(f.getInt(x));
					if (fieldType.equals("long")) oLong = Long.valueOf(f.getLong(x));
					if (fieldType.equals("float")) oFloat = Float.valueOf(f.getFloat(x));
					if (fieldType.equals("byte")) oByte = Byte.valueOf(f.getByte(x));
					if (fieldType.equals("double")) oDouble = Double.valueOf(f.getDouble(x));
					if (fieldType.equals("short")) oShort = Short.valueOf(f.getShort(x));
					if (fieldType.equals("char")) oChar = Character.valueOf(f.getChar(x));
					if (fieldType.equals("BigDecimal")) obgd = (BigDecimal) f.get(x);  //new BigDecimal((String)f.get(x));
					if (fieldType.equals("BigInteger")) obgi = (BigInteger) f.get(x);  //new BigInteger((String)f.get(x));

				}catch (NoSuchFieldException nsm){
					debug("sov - nsm "+fieldName+" "+oString);
					throw new DRNoMatchException("NoSuchFieldExcep - "+fieldName+" "+oString);
				} catch (IllegalArgumentException iae) {
					debug("sov - iae - "+fieldName+" "+oString);
					throw new DRNoMatchException("IllegalArgumentExcep - "+fieldName+" "+oString);
				}catch (IllegalAccessException acce) {
					debug("sov - acce - "+fieldName+" "+oString);
					throw new DRNoMatchException("IllegalAccessExcep - "+fieldName+" "+oString);
				}
			}
			//debug("sov end - os="+oString);
		}
		//==================================================
		public Object getObjValue(){

			try
			{
				if (fieldType.equals("int")) oString = Integer.toString(oInt);
				if (fieldType.equals("boolean")) oString = Boolean.toString(oBool);
				if (fieldType.equals("long")) oString = Long.toString(oLong);
				if (fieldType.equals("float")) oString = Float.toString(oFloat);
				if (fieldType.equals("byte")) oString = Byte.toString(oByte);
				if (fieldType.equals("double")) oString = Double.toString(oDouble);
				if (fieldType.equals("short")) oString = Short.toString(oShort);
				if (fieldType.equals("char")) oString = Character.toString(oChar);
				if (fieldType.equals("BigDecimal")) oString = obgd.toString();
				if (fieldType.equals("BigInteger")) oString = obgi.toString();
			} catch (NullPointerException npe){
				oString = "0";
			}
			return oString;
		}
		//=======================================================
		public int compareVals(){

			int greater = 0;
			int x1 = 99;
			if (fieldType.equals("String")){
				if (oString == null) oString = " "; 
				x1 = vString.compareTo(oString);
			}
			try
			{
				if (fieldType.equals("BigDecimal")) x1 = vbgd.compareTo(obgd);
				if (fieldType.equals("BigInteger")) x1 = vbgi.compareTo(obgi);
				if (fieldType.equals("byte")){	vLong = (long)vByte;	oLong = (long)oByte;}
				if (fieldType.equals("short")){	vLong = (long)vShort;	oLong = (long)oShort;}
				if (fieldType.equals("int")){	vLong = (long)vInt;	oLong = (long)oInt;}
				if (fieldType.equals("byte") || fieldType.equals("short") || fieldType.equals("int") || fieldType.equals("long")) {
					if (vLong == oLong) x1 = 0;
					if (vLong > oLong) x1 = 1;
					if (vLong < oLong) x1 = -1;
				}
				if (fieldType.equals("float")){
					if (vFloat == oFloat) x1 = 0;
					if (vFloat > oFloat) x1 = 1;
					if (vFloat < oFloat) x1 = -1;
				}
				if (fieldType.equals("double")){
					if (vDouble == oDouble) x1 = 0;
					if (vDouble > oDouble) x1 = 1;
					if (vDouble < oDouble) x1 = -1;
				}
				if (fieldType.equals("char")){
					if (vChar == oChar) x1 = 0;
					if (vChar > oChar) x1 = 1;
					if (vChar < oChar) x1 = -1;
				}
				if (fieldType.equals("boolean")){
					if (vBool == oBool) x1 = 0; else x1 = -1;
				}
				if (x1 == 0) greater = 0;			//V > O
				if (x1 > 0) greater = -1;
				if (x1 < 0) greater = 1;
			}catch (NullPointerException npe){
				greater = 99;				//this null rec will be ignored
			}
			debug("cv - g="+greater+" ostr="+oString+" vstr="+vString);

			return greater;
		}
		//=======================================================
		public int compareLike(){
			int greater = 0;

			String vstr = getVOrigString();
			String ostr = (String)getObjValue();

			int x1 = ostr.indexOf(vstr);
			if (x1 > -1) greater = 1;			//V > O
			//debug1("cl - g="+greater+" ostr="+oString+" vstr="+vString);
			return greater;
		}

	}
}