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
