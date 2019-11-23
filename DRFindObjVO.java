package projects.DRList.Jar;

import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;
import java.sql.Timestamp;
import java.time.*;

import projects.DRList.Jar.DRArrayList;
import projects.DRList.Jar.DRListTBL;
import projects.DRList.Jar.DRNoMatchException;
import projects.DRList.Jar.DRListNoFind;

import java.lang.reflect.*;
import java.math.*;

public class DRFindObjVO<T> extends DRFind
{
	private T[] obj;
	private DRListTBL<T> xdrl;

	public void debug(String msg){
		System.out.println(msg);
	}
	public void setObjArray(T[] o){
		this.obj = o;
	}
	public T[] getObjArray(){
		this.xdrl = null;
		return this.obj;
	}
	public DRListNoFind<T> getDRList(){
		this.xdrl = null;
		DRListNoFind<T> ndrl = new DRListNoFind<T>();
		for (int i = 0; i < obj.length; i++) ndrl.DRadd(this.obj[i]);
		return ndrl;
	}
	public List<T> getArrayList(){
		this.xdrl = null;
		List<T> nlst = new ArrayList<T>();
		for (int i = 0; i < obj.length; i++) nlst.add(this.obj[i]);
		return nlst;
	}
	public int getCount(){
		return obj.length;
	}
	public DRFindObjVO<T> distinct() throws DRNoMatchException
	{
		DRFind<T> drf = new DRFind<T>();
		return drf.distinct(obj);
	}
	public DRFindObjVO<T> sortAsc(String fieldName) throws DRNoMatchException
	{
		DRFind<T> drf = new DRFind<T>();
		return drf.sortAsc(obj,fieldName);
	}
	public DRFindObjVO<T> sortDsc(String fieldName) throws DRNoMatchException
	{
		DRFind<T> drf = new DRFind<T>();
		return drf.sortDsc(obj,fieldName);
	}
	//===============================================================================
	@SuppressWarnings("unchecked")
	public <Any> Any getMax(String fieldName) throws DRNoMatchException {
		return invokeMaxMin(fieldName, "getMax");
	}
	//===============================================================================
	@SuppressWarnings("unchecked")
	public <Any> Any getMin(String fieldName) throws DRNoMatchException {
		return invokeMaxMin(fieldName, "getMin");
	}
	//===============================================================================
	@SuppressWarnings("unchecked")
	public <Any> Any invokeMaxMin(String fieldName, String mName) throws DRNoMatchException {
		DRFind<T> drf = new DRFind<T>();
		String ft = drf.getFieldType(fieldName,this.obj).toUpperCase();
		String[] xx = drf.getFieldString(fieldName,this.obj);
		try{
			getNumericVO gn = new getNumericVO();
			String methodName = mName+ft;
			Class[] cArg = new Class[1]; 
			cArg[0] = String[].class;
 			Method m = gn.getClass().getDeclaredMethod(methodName,cArg);
			Object[] param = {xx};
			return (Any)m.invoke(gn,param);

		} catch (IllegalAccessException|NullPointerException|NoSuchMethodException|InvocationTargetException yy){ 
			throw new DRNoMatchException("Error maxmin finding entered field type "+ft+" "+yy.toString());
		}
	}
	//===============================================================================
	@SuppressWarnings("unchecked")
	public <Any> Any[] getFieldValue(String fieldName) throws DRNoMatchException {
		DRFind<T> drf = new DRFind<T>();
		DRFindVO vo = new DRFindVO();
		fieldName = drf.checkForArray(fieldName);
		String ft = drf.getFieldType(fieldName,this.obj).toUpperCase();
		String[] xx = drf.getFieldString(fieldName,this.obj);
		try{
			getNumericVO gn = new getNumericVO();
			String methodName = "getField"+ft;
			Class[] cArg = new Class[1]; 
			cArg[0] = String[].class;
 			Method m = gn.getClass().getDeclaredMethod(methodName,cArg);
			Object[] param = {xx};
			return (Any[])m.invoke(gn,param);

		} catch (IllegalAccessException|NullPointerException|NoSuchMethodException|InvocationTargetException yy){ 
			throw new DRNoMatchException("Error getfield finding entered field type "+ft+" "+yy.toString());
		}
	}
	//===============================================================================
	@SuppressWarnings("unchecked")
	public <Any> Any getAvg(String fieldName) throws DRNoMatchException {
		DRFind<T> drf = new DRFind<T>();
		String fieldType = drf.getFieldType(fieldName,this.obj).toUpperCase();
		return invokeMethods(fieldName, fieldType, "avg", "get");
	}
	//===============================================================================
	@SuppressWarnings("unchecked")
	public <Any> Any getSum(String fieldName, String fieldType) throws DRNoMatchException {
		return invokeMethods(fieldName, fieldType, "sum", "get");
	}
	//===============================================================================
	@SuppressWarnings("unchecked")
	public <Any> Any invokeMethods(String fieldName, String fieldType, String op, String mName) throws DRNoMatchException {
		String ft = fieldType.toUpperCase();
		DRFind<T> drf = new DRFind<T>();
		String ft1 = drf.getFieldType(fieldName,this.obj).toUpperCase();
		if (ft1.equals("STRING")||ft1.equals("CHARACTER")||ft1.equals("BOOLEAN")) 
			throw new DRNoMatchException("Field type is not of type numeric "+fieldName);
		if (ft.equals("STRING")||ft.equals("CHARACTER")||ft.equals("BOOLEAN")) 
			throw new DRNoMatchException("Entered Field type is not of type numeric "+fieldName);
		String methodName = "";
		try{
			getNumericVO gn = new getNumericVO();
			methodName = mName+ft;
			Class[] cArg = new Class[2]; 
			cArg[0] = String.class;
			cArg[1] = String.class;
 			Method m = gn.getClass().getDeclaredMethod(methodName,cArg);
			Object[] param = {op,fieldName};
			return (Any)m.invoke(gn,param);

		} catch (IllegalAccessException|NullPointerException|NoSuchMethodException|InvocationTargetException xx){ 
			throw new DRNoMatchException("Error invmeth finding entered field type "+ft1+" mn="+methodName+
					" fn="+fieldName+" "+xx.toString()+" "+xx.getCause());
		}
	}
	public void setDRL(DRListTBL<T> xdrl){
		//System.out.println("sd - "+xdrl.size);
		this.xdrl = xdrl;
	}
	public DRFindObjVO<T> DRFindAnd(String fieldName, String operator, String value) 
		throws DRNoMatchException
	{
		DRFind<T> drf = new DRFind<T>();
		return drf.FindAnd(fieldName, operator, value, this.obj);
	}
	public DRFindObjVO<T> DRFindOr(String fieldName, String operator, String value) 
		throws DRNoMatchException
	{
		DRFind<T> drf = new DRFind<T>();
		//System.out.println("fo - "+this.drl.size+" ol="+this.obj.length);
		return drf.FindOr(fieldName, operator, value, this.xdrl, this.obj);
	}
	public DRFindObjVO<T> DRFindMinus(String fieldName, String operator, String value) 
		throws DRNoMatchException
	{
		DRFind<T> drf = new DRFind<T>();
		return drf.FindMinus(fieldName, operator, value, this.obj);
	}
	//============================================
	public class getNumericVO{

		public Double getDOUBLE(String operator, String fieldName) throws DRNoMatchException {
			DRFind<T> drf = new DRFind<T>();
			return (Double)(double)drf.getDouble(operator, fieldName, obj);	//operator can be avg or sum
		}
		public Float getFLOAT(String operator, String fieldName) throws DRNoMatchException {
			DRFind<T> drf = new DRFind<T>();
			return (Float)(float)drf.getDouble(operator, fieldName, obj);	//operator can be avg or sum
		}
		public BigDecimal getBIGDECIMAL(String operator, String fieldName) throws DRNoMatchException {
			DRFind<T> drf = new DRFind<T>();
			return (BigDecimal)drf.getBigDecimal(operator, fieldName, obj);	//operator can be avg or sum
		}
		public BigInteger getBIGINTEGER(String operator, String fieldName) throws DRNoMatchException {
			DRFind<T> drf = new DRFind<T>();
			return drf.getBigInteger(operator, fieldName, obj);	//operator can be avg or sum
		}
		public Long getLONG(String operator, String fieldName) throws DRNoMatchException {
			DRFind<T> drf = new DRFind<T>();
			return (Long)(long)drf.getLong(operator, fieldName, obj);	//operator can be avg or sum
		}
		public Integer getINTEGER(String operator, String fieldName) throws DRNoMatchException {
			DRFind<T> drf = new DRFind<T>();
			return (Integer)(int)drf.getLong(operator, fieldName, obj);	//operator can be avg or sum
		}
		public Short getSHORT(String operator, String fieldName) throws DRNoMatchException {
			DRFind<T> drf = new DRFind<T>();
			return (Short)(short)drf.getLong(operator, fieldName, obj);	//operator can be avg or sum
		}
		public Byte getBYTE(String operator, String fieldName) throws DRNoMatchException {
			DRFind<T> drf = new DRFind<T>();
			return (Byte)(byte)drf.getLong(operator, fieldName, obj);	//operator can be avg or sum
		}
		public Double getMinDOUBLE(String[] xx){
			double mind = Double.parseDouble(xx[0]);
			for (int i = 0; i < xx.length; i++) if (xx[i] != null && Double.parseDouble(xx[i]) < mind) 
									mind = Double.parseDouble(xx[i]);
        		return (Double)mind;
		}
		public Float getMinFLOAT(String[] xx) {
			float mind = Float.parseFloat(xx[0]);
			for (int i = 0; i < xx.length; i++) if (xx[i] != null && Float.parseFloat(xx[i]) < mind) mind = Float.parseFloat(xx[i]);
        		return (Float)mind;
		}
		public Long getMinLONG(String[] xx) {
			long mind = Long.parseLong(xx[0]);
			for (int i = 0; i < xx.length; i++) if (xx[i] != null && Long.parseLong(xx[i]) < mind) mind = Long.parseLong(xx[i]);
        		return (Long)mind;
		}
		public Short getMinSHORT(String[] xx) {
			short mind = Short.parseShort(xx[0]);
			for (int i = 0; i < xx.length; i++) if (xx[i] != null && Short.parseShort(xx[i]) < mind) mind = Short.parseShort(xx[i]);
        		return (Short)mind;
		}
		public Byte getMinBYTE(String[] xx) {
			byte mind = Byte.parseByte(xx[0]);
			for (int i = 0; i < xx.length; i++) if (xx[i] != null && Byte.parseByte(xx[i]) < mind) mind = Byte.parseByte(xx[i]);
        		return (Byte)mind;
		}
		public Integer getMinINTEGER(String[] xx) {
			int mind = Integer.parseInt(xx[0]);
			for (int i = 0; i < xx.length; i++) if (xx[i] != null && Integer.parseInt(xx[i]) < mind) mind = Integer.parseInt(xx[i]);
        		return (Integer)mind;
		}
		public BigDecimal getMinBIGDECIMAL(String[] xx) {
			BigDecimal[] xxb = new BigDecimal[xx.length];
			BigDecimal minb = new BigDecimal(xx[0]);
			for (int i = 0; i < xx.length; i++){ 
				xxb[i] = new BigDecimal(xx[i]);
				if (xxb[i] != null && xxb[i].compareTo(minb) < 0) minb = xxb[i];
			}
          		return (BigDecimal)minb;
		}
		public BigInteger getMinBIGINTEGER(String[] xx) {
			BigInteger[] xxb = new BigInteger[xx.length];
			BigInteger minb = new BigInteger(xx[0]);
			for (int i = 0; i < xx.length; i++){
				xxb[i] = new BigInteger(xx[i]);
				if (xxb[i] != null && xxb[i].compareTo(minb) < 0) minb = xxb[i];
			}
          		return (BigInteger)minb;
		}
		public String getMinSTRING(String[] xx) {
			String mins = xx[0];
			for (int i = 0; i < xx.length; i++) if (xx[i] != null && xx[i].compareTo(mins) < 0) mins = xx[i];
			return (String)mins;
		}
		//============================================================================
		public Double getMaxDOUBLE(String[] xx){
			double mind = Double.parseDouble(xx[0]);
			for (int i = 0; i < xx.length; i++) if (xx[i] != null && Double.parseDouble(xx[i]) > mind) 
									mind = Double.parseDouble(xx[i]);
        		return (Double)mind;
		}
		public Float getMaxFLOAT(String[] xx) {
			float mind = Float.parseFloat(xx[0]);
			for (int i = 0; i < xx.length; i++) if (xx[i] != null && Float.parseFloat(xx[i]) > mind) mind = Float.parseFloat(xx[i]);
        		return (Float)mind;
		}
		public Long getMaxLONG(String[] xx) {
			long mind = Long.parseLong(xx[0]);
			for (int i = 0; i < xx.length; i++) if (xx[i] != null && Long.parseLong(xx[i]) > mind) mind = Long.parseLong(xx[i]);
        		return (Long)mind;
		}
		public Short getMaxSHORT(String[] xx) {
			short mind = Short.parseShort(xx[0]);
			for (int i = 0; i < xx.length; i++) if (xx[i] != null && Short.parseShort(xx[i]) > mind) mind = Short.parseShort(xx[i]);
        		return (Short)mind;
		}
		public Byte getMaxBYTE(String[] xx) {
			byte mind = Byte.parseByte(xx[0]);
			for (int i = 0; i < xx.length; i++) if (xx[i] != null && Byte.parseByte(xx[i]) > mind) mind = Byte.parseByte(xx[i]);
        		return (Byte)mind;
		}
		public Integer getMaxINTEGER(String[] xx) {
			int mind = Integer.parseInt(xx[0]);
			for (int i = 0; i < xx.length; i++) if (xx[i] != null && Integer.parseInt(xx[i]) > mind) mind = Integer.parseInt(xx[i]);
        		return (Integer)mind;
		}
		public BigDecimal getMaxBIGDECIMAL(String[] xx) {
			BigDecimal[] xxb = new BigDecimal[xx.length];
			BigDecimal minb = new BigDecimal(xx[0]);
			for (int i = 0; i < xx.length; i++){ 
				xxb[i] = new BigDecimal(xx[i]);
				if (xxb[i] != null && xxb[i].compareTo(minb) > 0) minb = xxb[i];
			}
          		return (BigDecimal)minb;
		}
		public BigInteger getMaxBIGINTEGER(String[] xx) {
			BigInteger[] xxb = new BigInteger[xx.length];
			BigInteger minb = new BigInteger(xx[0]);
			for (int i = 0; i < xx.length; i++){
				xxb[i] = new BigInteger(xx[i]);
				if (xxb[i] != null && xxb[i].compareTo(minb) > 0) minb = xxb[i];
			}
          		return (BigInteger)minb;
		}
		public String getMaxSTRING(String[] xx) {
			String mins = xx[0];
			for (int i = 0; i < xx.length; i++) if (xx[i] != null && xx[i].compareTo(mins) > 0) mins = xx[i];
			return (String)mins;
		}
		//============================================================================
		public Double[] getFieldDOUBLE(String[] xx) {
			Double[] xxd = new Double[xx.length];
			for (int i = 0; i < xx.length; i++) xxd[i] = Double.parseDouble(xx[i]);
        		return (Double[])xxd;
		}
		public Float[] getFieldFLOAT(String[] xx) {
			Float[] xxd = new Float[xx.length];
			for (int i = 0; i < xx.length; i++) xxd[i] = Float.parseFloat(xx[i]);
        		return (Float[])xxd;
		}
		public Long[] getFieldLONG(String[] xx) {
			Long[] xxd = new Long[xx.length];
			for (int i = 0; i < xx.length; i++) xxd[i] = Long.parseLong(xx[i]);
        		return (Long[])xxd;
		}
		public Integer[] getFieldINTEGER(String[] xx) throws DRNoMatchException {
			Integer[] xxd = new Integer[xx.length];
			for (int i = 0; i < xx.length; i++) xxd[i] = Integer.parseInt(xx[i]);
        		return (Integer[])xxd;
		}
		public Byte[] getFieldBYTE(String[] xx) {
			Byte[] xxd = new Byte[xx.length];
			for (int i = 0; i < xx.length; i++) xxd[i] = Byte.parseByte(xx[i]);
        		return (Byte[])xxd;
		}
		public Short[] getFieldSHORT(String[] xx) {
			Short[] xxd = new Short[xx.length];
			for (int i = 0; i < xx.length; i++) xxd[i] = Short.parseShort(xx[i]);
        		return (Short[])xxd;
		}
		public Character[] getFieldCHARACTER(String[] xx) {
			Character[] xxd = new Character[xx.length];
			for (int i = 0; i < xx.length; i++) xxd[i] = xx[i].charAt(0);
        		return (Character[])xxd;
		}
		public BigDecimal[] getFieldBIGDECIMAL(String[] xx) {
			BigDecimal[] xxd = new BigDecimal[xx.length];
			for (int i = 0; i < xx.length; i++) xxd[i] = new BigDecimal(xx[i]);
        		return (BigDecimal[])xxd;
		}
		public BigInteger[] getFieldBIGINTEGER(String[] xx) {
			BigInteger[] xxd = new BigInteger[xx.length];
			for (int i = 0; i < xx.length; i++) xxd[i] = new BigInteger(xx[i]);
        		return (BigInteger[])xxd;
    		}
		public String[] getFieldSTRING(String[] xx) throws DRNoMatchException {
			return (String[])xx;
		}
	}
}
