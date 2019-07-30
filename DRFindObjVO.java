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
import java.math.*;

public class DRFindObjVO<T>
{
	private T[] obj;
	private DRListTBL<T> xdrl;

	public void setObjArray(T[] o){
		this.obj = o;
	}
	public T[] getObjArray(){
		this.xdrl = null;
		return this.obj;
	}
	public DRList<T> getDRList(){
		this.xdrl = null;
		DRList<T> ndrl = new DRList<T>();
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
	//===============================================================================
	@SuppressWarnings("unchecked")
	public <Any> Any getMax(String fieldName) throws DRNoMatchException {
		
		DRFind<T> drf = new DRFind<T>();
		String ft = drf.getFieldType(fieldName,this.obj).toUpperCase();
		String[] xx = drf.getFieldString(fieldName,this.obj);
    		if (ft.equals("DOUBLE")||ft.equals("FLOAT")) {
			Double[] xxd = new Double[xx.length];
			double maxd = 0.0d;
			for (int i = 0; i < xx.length; i++) if (xx[i] != null) xxd[i] =Double.parseDouble(xx[i]);
			for (int i = 0; i < xx.length; i++) if (xxd[i] > maxd) maxd = xxd[i];
			if (ft.equals("FLOAT")) return (Any)(Float)(float)maxd;
        		return (Any)(Double)maxd;
    		}else if (ft.equals("LONG")||ft.equals("INT")||ft.equals("SHORT")||ft.equals("BYTE")) {
			Long[] xxl = new Long[xx.length];
			long maxl = 0;
			for (int i = 0; i < xx.length; i++) if (xx[i] != null) xxl[i] = Long.parseLong(xx[i]);
			for (int i = 0; i < xx.length; i++) if (xxl[i] > maxl) maxl = xxl[i];
			if (ft.equals("INT")) return (Any)(Integer)(int)maxl;
 			if (ft.equals("SHORT")) return (Any)(Short)(short)maxl;
			if (ft.equals("BYTE")) return (Any)(Byte)(byte)maxl;
          		return (Any)(Long)maxl;
		}else if (ft.equals("BIGDECIMAL")){
			BigDecimal[] xxb = new BigDecimal[xx.length];
			BigDecimal maxb = new BigDecimal("0.0");
			for (int i = 0; i < xx.length; i++) xxb[i] = new BigDecimal(xx[i]);
			for (int i = 0; i < xx.length; i++) if (xxb[i] != null && xxb[i].compareTo(maxb) > 0) maxb = xxb[i];
          		return (Any)(BigDecimal)maxb;
		}else if (ft.equals("BIGINTEGER")){
			BigInteger[] xxb = new BigInteger[xx.length];
			BigInteger maxb = new BigInteger("0");
			for (int i = 0; i < xx.length; i++) xxb[i] = new BigInteger(xx[i]);
			for (int i = 0; i < xx.length; i++) if (xxb[i] != null && xxb[i].compareTo(maxb) > 0) maxb = xxb[i];
          		return (Any)(BigInteger)maxb;
		}else if (ft.equals("STRING")) {
			String maxs = "";
			for (int i = 0; i < xx.length; i++) if (xx[i] != null && xx[i].compareTo(maxs) > 0) maxs = xx[i];
			return (Any)(String)maxs;
		}else{
	        	throw new DRNoMatchException("Invalid field type "+ft);
		}
	}
	//===============================================================================
	@SuppressWarnings("unchecked")
	public <Any> Any getMin(String fieldName) throws DRNoMatchException {
		
		DRFind<T> drf = new DRFind<T>();
		String ft = drf.getFieldType(fieldName,this.obj).toUpperCase();
		String[] xx = drf.getFieldString(fieldName,this.obj);
    		if (ft.equals("DOUBLE")||ft.equals("FLOAT")) {
			Double[] xxd = new Double[xx.length];
			for (int i = 0; i < xx.length; i++) xxd[i] = Double.parseDouble(xx[i]);
			double mind = xxd[0];
			for (int i = 0; i < xx.length; i++) if (xxd[i] != null && xxd[i] < mind) mind = xxd[i];
			if (ft.equals("FLOAT")) return (Any)(Float)(float)mind;
        		return (Any)(Double)mind;
    		}else if (ft.equals("LONG")||ft.equals("INT")||ft.equals("SHORT")||ft.equals("BYTE")) {
			Long[] xxl = new Long[xx.length];
			for (int i = 0; i < xx.length; i++) xxl[i] = Long.parseLong(xx[i]);
			long minl = xxl[0];
			for (int i = 0; i < xx.length; i++) if (xxl[i] != null && xxl[i] < minl) minl = xxl[i];
			if (ft.equals("INT")) return (Any)(Integer)(int)minl;
 			if (ft.equals("SHORT")) return (Any)(Short)(short)minl;
			if (ft.equals("BYTE")) return (Any)(Byte)(byte)minl;
          		return (Any)(Long)minl;
		}else if (ft.equals("BIGDECIMAL")){
			BigDecimal[] xxb = new BigDecimal[xx.length];
			BigDecimal minb = new BigDecimal(xx[0]);
			for (int i = 0; i < xx.length; i++) xxb[i] = new BigDecimal(xx[i]);
			for (int i = 0; i < xx.length; i++) if (xxb[i] != null && xxb[i].compareTo(minb) < 0) minb = xxb[i];
          		return (Any)(BigDecimal)minb;
		}else if (ft.equals("BIGINTEGER")){
			BigInteger[] xxb = new BigInteger[xx.length];
			BigInteger minb = new BigInteger(xx[0]);
			for (int i = 0; i < xx.length; i++) xxb[i] = new BigInteger(xx[i]);
			for (int i = 0; i < xx.length; i++) if (xxb[i] != null && xxb[i].compareTo(minb) < 0) minb = xxb[i];
          		return (Any)(BigInteger)minb;
		}else if (ft.equals("STRING")) {
			String mins = xx[0];
			for (int i = 0; i < xx.length; i++) if (xx[i] != null && xx[i].compareTo(mins) < 0) mins = xx[i];
			return (Any)(String)mins;
		}else{
	        	throw new DRNoMatchException("Invalid field type "+ft);
		}
	}	//===============================================================================
	@SuppressWarnings("unchecked")
	public <Any> Any[] getFieldValue(String fieldName) throws DRNoMatchException {
		DRFind<T> drf = new DRFind<T>();
		String ft = drf.getFieldType(fieldName,this.obj).toUpperCase();
		String[] xx = drf.getFieldString(fieldName,this.obj);
		//System.out.println("gfv - ft=["+ft+"] xx0="+xx[0]+" xxl="+xx.length);
    		if (ft.equals("DOUBLE")) {
			Double[] xxd = new Double[xx.length];
			for (int i = 0; i < xx.length; i++) xxd[i] = Double.parseDouble(xx[i]);
        		return (Any[])(Double[])xxd;
    		}else if (ft.equals("FLOAT")) {
			Float[] xxd = new Float[xx.length];
			for (int i = 0; i < xx.length; i++) xxd[i] = Float.parseFloat(xx[i]);
        		return (Any[])(Float[])xxd;
    		}else if (ft.equals("LONG")) {
			Long[] xxd = new Long[xx.length];
			for (int i = 0; i < xx.length; i++) xxd[i] = Long.parseLong(xx[i]);
        		return (Any[])(Long[])xxd;
    		}else if (ft.equals("INT")) {
			System.out.println("gfv - 1ft=["+ft+"] xx0="+xx[0]+" xxl="+xx.length);
			Integer[] xxd = new Integer[xx.length];
			for (int i = 0; i < xx.length; i++) xxd[i] = Integer.parseInt(xx[i]);
        		return (Any[])(Integer[])xxd;
    		}else if (ft.equals("BYTE")) {
			Byte[] xxd = new Byte[xx.length];
			for (int i = 0; i < xx.length; i++) xxd[i] = Byte.parseByte(xx[i]);
        		return (Any[])(Byte[])xxd;
    		}else if (ft.equals("SHORT")) {
			Short[] xxd = new Short[xx.length];
			for (int i = 0; i < xx.length; i++) xxd[i] = Short.parseShort(xx[i]);
        		return (Any[])(Short[])xxd;
    		}else if (ft.equals("CHAR")) {
			Character[] xxd = new Character[xx.length];
			for (int i = 0; i < xx.length; i++) xxd[i] = xx[i].charAt(0);
        		return (Any[])(Character[])xxd;
    		}else if (ft.equals("BIGDECIMAL")) {
			BigDecimal[] xxd = new BigDecimal[xx.length];
			for (int i = 0; i < xx.length; i++) xxd[i] = new BigDecimal(xx[i]);
        		return (Any[])(BigDecimal[])xxd;
    		}else if (ft.equals("BIGINTEGER")) {
			BigInteger[] xxd = new BigInteger[xx.length];
			for (int i = 0; i < xx.length; i++) xxd[i] = new BigInteger(xx[i]);
        		return (Any[])(BigInteger[])xxd;
    		}
		if (ft.equals("STRING")) {
			return (Any[])xx;
		}else{
	        	throw new DRNoMatchException("Invalid field type "+ft);
		}
	}
	//===============================================================================
	@SuppressWarnings("unchecked")
	public <Any> Any getAvg(String fieldName) throws DRNoMatchException {
		DRFind<T> drf = new DRFind<T>();
		String ft = drf.getFieldType(fieldName,this.obj).toUpperCase();
    		if (ft.equals("DOUBLE")) {
        		return (Any)((Double)(double)getDouble("avg", fieldName));
    		}else if (ft.equals("FLOAT")) {
        		return (Any)((Float)(float)getDouble("avg", fieldName));
    		}else if (ft.equals("BIGDECIMAL")) {
			BigDecimal xx = getBigDecimal("avg", fieldName);
        		return (Any)(BigDecimal)xx;
    		}else if (ft.equals("BIGINTEGER")) {
			BigInteger xx = getBigInteger("avg", fieldName);
        		return (Any)(BigInteger)xx;
    		}else if (ft.equals("LONG")){
        		return (Any)((Long)(long)getLong("avg", fieldName));
    		}else if (ft.equals("INT")){
        		return (Any)((Integer)(int)getLong("avg", fieldName));
		}else{
        		throw new DRNoMatchException("Invalid field type "+ft);
    		}
	}
	//===============================================================================
	@SuppressWarnings("unchecked")
	public <Any> Any getSum(String fieldName, String fieldType) throws DRNoMatchException {
		String ft = fieldType.toUpperCase();
    		if (ft.equals("DOUBLE")) {
        		return (Any)((Double)(double)getDouble("sum", fieldName));
    		}else if (ft.equals("FLOAT")) {
        		return (Any)((Float)(float)getDouble("sum", fieldName));
    		}else if (ft.equals("BIGDECIMAL")) {
			BigDecimal xx = getBigDecimal("sum", fieldName);
        		return (Any)(BigDecimal)xx;
    		}else if (ft.equals("BIGINTEGER")) {
			BigInteger xx = getBigInteger("sum", fieldName);
        		return (Any)(BigInteger)xx;
    		}else if (ft.equals("LONG")){
        		return (Any)((Long)(long)getLong("sum", fieldName));
    		}else if (ft.equals("INTEGER")){
        		return (Any)((Integer)(int)getLong("sum", fieldName));
		}else{
        		throw new DRNoMatchException("Invalid field type "+ft);
    		}
	}
	//===========================================================================
	public double getDouble(String operator, String fieldName) throws DRNoMatchException {
		DRFind<T> drf = new DRFind<T>();
		return drf.getDouble(operator, fieldName, this.obj);	//operator can be avg or sum
	}
	//===========================================================================
	public BigDecimal getBigDecimal(String operator, String fieldName) throws DRNoMatchException {
		DRFind<T> drf = new DRFind<T>();
		return drf.getBigDecimal(operator, fieldName, this.obj);	//operator can be avg or sum
	}
	//===========================================================================
	public BigInteger getBigInteger(String operator, String fieldName) throws DRNoMatchException {
		DRFind<T> drf = new DRFind<T>();
		return drf.getBigInteger(operator, fieldName, this.obj);	//operator can be avg or sum
	}
	//============================================================================
	public long getLong(String operator, String fieldName) throws DRNoMatchException {
		DRFind<T> drf = new DRFind<T>();
		return drf.getLong(operator, fieldName, this.obj);	//operator can be avg or sum
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
}
