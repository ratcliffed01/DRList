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

import java.lang.reflect.*;

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
			if (drl.dl.obj instanceof Integer) vo.setFieldType("int");
			if (drl.dl.obj instanceof Float) vo.setFieldType("float");
			if (drl.dl.obj instanceof Byte) vo.setFieldType("byte");
			if (drl.dl.obj instanceof Double) vo.setFieldType("double");
			if (drl.dl.obj instanceof Long) vo.setFieldType("long");
			if (drl.dl.obj instanceof Short) vo.setFieldType("short");
			if (drl.dl.obj instanceof Character) vo.setFieldType("char");
			vo.setFieldName(fieldName);
		}else{
			String fieldType = f.getType().toString();
			if (fieldType.indexOf("String") > -1) fieldType = "String";
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
		}else{
			if (op.equals("Min") || op.equals("Max")){
				vo.setValue(value);					//this should be ""
			}else{
				boolean ret1 = false;
				try{
					if (vo.getFieldType().equals("float")) Float.parseFloat(value);
					if (vo.getFieldType().equals("double")) Double.parseDouble(value);
					if (vo.getFieldType().equals("int")) Integer.parseInt(value);
					if (vo.getFieldType().equals("byte")) Byte.parseByte(value);
					if (vo.getFieldType().equals("long")) Long.parseLong(value);
					if (vo.getFieldType().equals("short")) Short.parseShort(value);

					ret1 = true;
				}catch (NumberFormatException e){ret1 = false;}
				debug1("vf - start2 ret="+ret1+" v="+value);
				if (ret1) vo.setValue(value); 
				else throw new DRNoMatchException("Value parameter not suitable for field parameter");
			}
		}
		debug1("vf - end val="+vo.getValue()+" ft="+vo.getFieldType());
		return vo;
	}
	//================================================
	public T[] DRFind(String fieldName, String operator, String value, DRListTBL<T> drl) 
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
	public T[] DRFindAnd(String fieldName, String operator, String value, T[] obj) 
		throws DRNoMatchException
	{
		DRFind<T> drf = new DRFind<T>();
		DRCode<T> drc = new DRCode<T>();
		DRListTBL<T> ndrl = new DRListTBL<T>();
		DRFindVO vo = new DRFindVO();

		for (int i = 0; i < obj.length; i++)
			drc.DRadd(obj[i],ndrl);

		T[] obj1 = drf.DRFind(fieldName, operator, value, ndrl);

		return obj1;
	}
	//================================================
	public T[] DRFindOr(String fieldName, String operator, String value, DRListTBL<T> drl, T[] obj) 
		throws DRNoMatchException
	{
		DRFind<T> drf = new DRFind<T>();
		DRCode<T> drc = new DRCode<T>();

		if (drl.fdl.obj == null) throw new DRNoMatchException("ListTbl is null");

		T[] obj1 = drf.DRFind(fieldName, operator, value, drl);
		T[] obj2 = drf.combineObjs(obj,obj1);
		return obj2;
	}
	//================================================
	public T[] DRFindMinus(String fieldName, String operator, String value, T[] obj) 
		throws DRNoMatchException
	{
		DRFind<T> drf = new DRFind<T>();
		DRCode<T> drc = new DRCode<T>();
		DRListTBL<T> ndrl = new DRListTBL<T>();
		DRFindVO vo = new DRFindVO();

		for (int i = 0; i < obj.length; i++)
			drc.DRadd(obj[i],ndrl);

		T[] obj1 = drf.DRFind(fieldName, operator, value, ndrl);

		T[] obj2 = drf.minusObjs(obj,obj1);

		return obj2;
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

		for (int i = 0; i < drc.DRsize(drl); i++){
			vo.setObjValue(drl.dl.obj);
			if (greater < 2){							// <,=,>
				if (vo.compareVals() == greater) drc.DRadd(drl.dl.obj,dl1);
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
			drl.dl = drl.dl.next;
		}
		int size = drc.DRsize(dl1);
		debug1("f= - after comp siz="+size+" dl1o=");
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
			String o1String = (String)fvo.getObjValue();

			fvo.setObjValue(obj2);
			String o2String = (String)fvo.getObjValue();

			debug1("cobj = ft="+ft+" fn="+fn+" o1="+o1String+" o2="+o2String);

			int greater = 0;
			int x1 = o1String.compareTo(o2String);
			if (x1 == 0) greater = 0;			//V > O
			if (x1 > 0) greater = 1;
			if (x1 < 0) greater = -1;
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
	//=====================================================
	public class DRFindVO
	{
		private String fieldName;
		private String fieldType;

		private String vString;
		private String oString;

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
		public void setFieldType(String x){
			this.fieldType = x;
		}
		public String getFieldType(){
			return this.fieldType;
		}
		public void setValue(String x){
			//debug("sv - x="+x+" ft="+fieldType);
	
			vString = x;
			if (!fieldType.equals("String"))
				vString =  zeroes.substring(vString.length()) + vString;
		}
		public Object getValue(){
			return this.vString;
		}
		//===============================================================
		public void setObjValue(Object x) throws DRNoMatchException
		{
			//debug("sov start - fn="+" fl=");
			if (this.fieldName.length() == 0){
				if (fieldType.equals("String")) oString = (String)x;
				if (fieldType.equals("int")) oString = Integer.toString((Integer)x);
				if (fieldType.equals("long")) oString = Long.toString((Long)x);
				if (fieldType.equals("float")) oString = Float.toString((Float)x);
				if (fieldType.equals("byte")) oString = Byte.toString((Byte)x);
				if (fieldType.equals("double")) oString = Double.toString((Double)x);
				if (fieldType.equals("short")) oString = Short.toString((Short)x);
				if (fieldType.equals("char")) oString = Character.toString((Character)x);
			}else{
				try{
					//debug("sov - x="+x.toString()+" ft="+fieldType+" fn="+fieldName);
					Class c1 = x.getClass();
					Field f = c1.getDeclaredField(fieldName);
					f.setAccessible(true);
					if (fieldType.equals("String")) oString = (String)f.get(x);
					if (fieldType.equals("int")) oString = Integer.toString(Integer.valueOf(f.getInt(x)));
					if (fieldType.equals("long")) oString = Long.toString(Long.valueOf(f.getLong(x)));
					if (fieldType.equals("float")) oString = Float.toString(Float.valueOf(f.getFloat(x)));
					if (fieldType.equals("byte")) oString = Byte.toString(Byte.valueOf(f.getByte(x)));
					if (fieldType.equals("double")) oString = Double.toString(Double.valueOf(f.getDouble(x)));
					if (fieldType.equals("short")) oString = Short.toString(Short.valueOf(f.getShort(x)));
					if (fieldType.equals("char")) oString = Character.toString(Character.valueOf(f.getChar(x)));

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
			if (!fieldType.equals("String"))
				oString =  zeroes.substring(oString.length()) + oString;
			//debug("sov end - os="+oString);
		}
		//==================================================
		public Object getObjValue(){
			return oString;
		}
		//=======================================================
		public int compareVals(){
			int greater = 0;
			int x1 = vString.compareTo(oString);
			if (x1 == 0) greater = 0;			//V > O
			if (x1 > 0) greater = -1;
			if (x1 < 0) greater = 1;
			//debug1("cv - g="+greater+" ostr="+oString+" vstr="+vString);
			return greater;
		}
		//=======================================================
		public String removeLead0s(String x){
			int i = 0;
			for (i = 0; i < x.length(); i++)
				if (x.charAt(i) != '0') break;
			 
			return x.substring(i);
		}
		//=======================================================
		public int compareLike(){
			int greater = 0;
			if (vString.charAt(0) == '0' && !fieldType.equals("String")) vString = removeLead0s(vString);
			int x1 = oString.indexOf(vString);
			if (x1 > -1) greater = 1;			//V > O
			//debug1("cl - g="+greater+" ostr="+oString+" vstr="+vString);
			return greater;
		}

	}
}