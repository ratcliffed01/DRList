package projects.DRList.Jar;

import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;
import java.sql.Timestamp;
import java.time.*;

import java.lang.reflect.*;
import java.math.*;

	public class DRFindVO
	{
		private String fieldName;
		private String fieldType;

		private Field f1;
		private Method mSetO;
		private Method mSetV;
		private Method mGetO;
		private Method mGetV;
		private Method mCompare;
		private Method mCompareVals;

		private String vOrigString;
		private String vString;
		private int vInteger;
		private byte vByte;
		private short vShort;
		private long vLong;
		private float vFloat;
		private double vDouble;
		private char vChar;
		private boolean vBoolean;
		private BigDecimal vBigDecimal;
		private BigInteger vBigInteger;

		private String oString;
		private int oInteger;
		private byte oByte;
		private short oShort;
		private long oLong;
		private float oFloat;
		private double oDouble;
		private char oChar;
		private boolean oBoolean;
		private BigDecimal oBigDecimal;
		private BigInteger oBigInteger;

		String zeroes = "000000000000000000";

		private boolean sortAsc;
		private boolean sortDsc;

		private char dateInterval;
		private int dateIntervalNum;
		private String dateStr;
		long startDate;

    		//===================================================================================
    		public void debug(String msg){
			//System.out.println(msg);
 		}
    		public void debug1(String msg){
			//System.out.println(msg);
 		}

		public Method getmCompareVals(){
			return mCompareVals;
		}
		public void setmCompareVals(Method m){
			mCompareVals = m;
		}
		public void setmSetO(Method m){
			mSetO =m;
		}
		public void setmGetO(Method m){
			mGetO =m;
		}
		public void setmSetV(Method m){
			mSetV =m;
		}
		public void setmGetV(Method m){
			mGetV =m;
		}
		public void setmCompare(Method m){
			mCompare =m;
		}
		public Method getmSetO(){
			return mSetO;
		}
		public Method getmGetO(){
			return mGetO;
		}
		public Method getmSetV(){
			return mSetV;
		}
		public Method getmGetV(){
			return mGetV;
		}

		public Method getmCompare(){
			return this.mCompare;
		}
		public void setFieldF1(Field f){
			this.f1 = f;
		}
		public Field getFieldF1(){
			return this.f1;
		}
		public void setStartDate(long x){
			this.startDate = x;
		}
		public long getStartDate(){
			return this.startDate;
		}
		public void setDateStr(String x){
			this.dateStr = x;
		}
		public String getDateStr(){
			return this.dateStr;
		}
		public void setDateIntervalNum(int x){
			this.dateIntervalNum = x;
		}
		public int getDateIntervalNum(){
			return this.dateIntervalNum;
		}
		public void setDateInterval(char x){
			this.dateInterval = x;
		}
		public char getDateInterval(){
			return this.dateInterval;
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


		//======================================================
		public void setOLong(String x)
		{
			oLong = Long.parseLong(x);
		}
		public void setOInteger(String x){
			oInteger = Integer.parseInt(x);
		}
		public void setOByte(String x){
			oByte = Byte.parseByte(x);
		}
		public void setOShort(String x){
			oShort = Short.parseShort(x);
		}
		public void setODouble(String x){
			oDouble = Double.parseDouble(x);
		}
		public void setOFloat(String x){
			oFloat = Float.parseFloat(x);
		}
		public void setOCharacter(String x){
			oChar = x.charAt(0);
		}
		public void setOString(String x){
			oString = x;
			//debug2("sos - x="+x+" os="+oString);
		}
		public void setOBoolean(String x){
			x = x.toUpperCase();
			if (x.equals("TRUE")) oBoolean = true; else oBoolean = false;
		}
		public void setOBigDecimal(String x){
			oBigDecimal = new BigDecimal(x);
		}
		public void setOBigInteger(String x){
			oBigInteger = new BigInteger(x);
		}
		//===========================================================
		public String getOLong()
		{
			return Long.toString(oLong);
		}
		public String getOInteger(){
			return Integer.toString(oInteger);
		}
		public String getOByte(){
			 return Byte.toString(oByte);
		}
		public String getOShort(){
			 return Short.toString(oShort);
		}
		public String getODouble(){
			 return Double.toString(oDouble);
		}
		public String getOFloat(){
			 return Float.toString(oFloat);
		}
		public String getOCharacter(){
			 return String.valueOf(oChar);
		}
		public String getOString(){
			return oString;
		}
		public String getOBoolean(){
			return Boolean.toString(oBoolean);
		}
		public String getOBigDecimal(){
			 return oBigDecimal.toPlainString();
		}
		public String getOBigInteger(){
			 return oBigInteger.toString();
		}
		//======================================================
		public void setVLong(String x)
		{
			vLong = Long.parseLong(x);
		}
		public void setVInteger(String x){
			vInteger = Integer.parseInt(x);
		}
		public void setVByte(String x){
			vByte = Byte.parseByte(x);
		}
		public void setVShort(String x){
			vShort = Short.parseShort(x);
		}
		public void setVDouble(String x){
			vDouble = Double.parseDouble(x);
		}
		public void setVFloat(String x){
			vFloat = Float.parseFloat(x);
		}
		public void setVCharacter(String x){
			vChar = x.charAt(0);
		}
		public void setVString(String x){
			vString = x;
		}
		public void setVBoolean(String x){
			x = x.toUpperCase();
			if (x.equals("TRUE")) vBoolean = true; else vBoolean = false;
		}
		public void setVBigDecimal(String x){
			vBigDecimal = new BigDecimal(x);
		}
		public void setVBigInteger(String x){
			vBigInteger = new BigInteger(x);
		}
		//===========================================================
		public String getVLong()
		{
			return Long.toString(vLong);
		}
		public String getVInteger(){
			return Integer.toString(vInteger);
		}
		public String getVByte(){
			 return Byte.toString(vByte);
		}
		public String getVShort(){
			 return Short.toString(vShort);
		}
		public String getVDouble(){
			 return Double.toString(vDouble);
		}
		public String getVFloat(){
			 return Float.toString(vFloat);
		}
		public String getVCharacter(){
			 return String.valueOf(vChar);
		}
		public String getVString(){
			return vString;
		}
		public String getVBoolean(){
			return Boolean.toString(vBoolean);
		}
		public String getVBigDecimal(){
			 return vBigDecimal.toPlainString();
		}
		public String getVBigInteger(){
			 return vBigInteger.toString();
		}
		//======================================================
		public int compareLong()
		{
			int greater = 0;
			try{
				greater = 0;
				if (vLong > oLong) greater = -1;
				if (vLong < oLong) greater = 1;
			}catch (NullPointerException npe){
				greater = 99;				//this null rec will be ignored
			}
			return greater;
		}
		public int compareInteger(){
			int greater = 0;
			try{
				greater = 0;
				if (vInteger > oInteger) greater = -1;
				if (vInteger < oInteger) greater = 1;
			}catch (NullPointerException npe){
				greater = 99;				//this null rec will be ignored
			}
			return greater;
		}
		public int compareByte(){
			int greater = 0;
			try
			{
				greater = 0;				//v = o
				if (vByte > oByte) greater = -1;
				if (vByte < oByte) greater = 1;
			}catch (NullPointerException npe){
				greater = 99;				//this null rec will be ignored
			}
			return greater;
		}
		public int compareShort(){
			int greater = 0;
			try
			{
				greater = 0;				//v = o
				if (vShort > oShort) greater = -1;
				if (vShort < oShort) greater = 1;
			}catch (NullPointerException npe){
				greater = 99;				//this null rec will be ignored
			}
			return greater;
		}
		public int compareDouble(){
			int greater = 0;
			try
			{
				greater = 0;				//v = o
				if (vDouble > oDouble) greater = -1;
				if (vDouble < oDouble) greater = 1;
			}catch (NullPointerException npe){
				greater = 99;				//this null rec will be ignored
			}
			return greater;
		}
		public int compareFloat(){
			int greater = 0;
			try
			{
				greater = 0;				//v = o
				if (vFloat > oFloat) greater = -1;
				if (vFloat < oFloat) greater = 1;
			}catch (NullPointerException npe){
				greater = 99;				//this null rec will be ignored
			}
			return greater;
		}
		public int compareCharacter(){
			int greater = 0;
			try{
				greater = 0;				//v = o
				if (vChar > oChar) greater = -1;
				if (vChar < oChar) greater = 1;
			}catch (NullPointerException npe){
				greater = 99;				//this null rec will be ignored
			}
			return greater;
		}
		public int compareString(){
			int greater = 0;
			int x1 = 99;
			if (oString == null) oString = " "; 
			x1 = vString.compareTo(oString);
			if (x1 == 0) greater = 0;			//V > O
			if (x1 > 0) greater = -1;
			if (x1 < 0) greater = 1;
			return greater;
		}
		public int compareBoolean(){
			int greater = 0;
			try
			{
				if (vBoolean == oBoolean) greater = 0; else greater = 1;
			}catch (NullPointerException npe){
				greater = 99;				//this null rec will be ignored
			}
			return greater;
		}
		public int compareBigDecimal(){
			int greater = 0;
			int x1 = 99;
			try
			{
				x1 = vBigDecimal.compareTo(oBigDecimal);
			}catch (NullPointerException npe){
				greater = 99;				//this null rec will be ignored
			}
			if (x1 == 0) greater = 0;			//V > O
			if (x1 > 0) greater = -1;
			if (x1 < 0) greater = 1;
			return greater;
		}
		public int compareBigInteger(){
			int greater = 0;
			int x1 = 99;
			try
			{
				x1 = vBigInteger.compareTo(oBigInteger);
			}catch (NullPointerException npe){
				greater = 99;				//this null rec will be ignored
			}
			if (x1 == 0) greater = 0;			//V > O
			if (x1 > 0) greater = -1;
			if (x1 < 0) greater = 1;
			return greater;
		}
		//====================================================================
		public Boolean compareValsLess(Integer greater){
			return (greater == -1);
		}
		//====================================================================
		public Boolean compareValsEquals(Integer greater){
			return (greater == 0);
		}
		//====================================================================
		public Boolean compareValsGreater(Integer greater){
			return (greater == 1);
		}

	}
