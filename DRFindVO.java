package projects.DRList.Jar;

import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;
import java.sql.Timestamp;
import java.time.*;

import java.lang.reflect.*;
import java.math.*;
import java.time.format.DateTimeFormatter;

import projects.DRList.Jar.DRLocalDateTime;

	public class DRFindVO
	{
		private String fieldName;
		private String fieldType;
		private String origFieldType;

		private boolean isArray;
		private boolean isMethod;
		private int opCnt;
		private String opTxt;

		private Field f1;
		private Method mSetO;
		private Method mSetV;
		private Method mGetO;
		private Method mGetV;
		private Method mCompare;
		private Method mCompareVals;
		private Method mCompareArray;
		private Method mGetter;

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
		private LocalDateTime vLDT;
		private LocalDate vLD;
		private Date vDate;

		private String oString;
		private String oArrayString;
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
		private LocalDateTime oLDT;
		private LocalDate oLD;
		private Date oDate;

		private long oMinLong	= 0;
		private long oMaxLong	= 0;
		private String oMinString = " ";
		private String oMaxString = " ";
		private double oMinDouble = 0.0d;
		private double oMaxDouble = 0.0d;
		private BigDecimal oMinBigD = BigDecimal.valueOf(0l);
		private BigDecimal oMaxBigD = BigDecimal.valueOf(0l);
		private BigInteger oMinBigI = BigInteger.valueOf(0l);
		private BigInteger oMaxBigI = BigInteger.valueOf(0l);
		private LocalDateTime oMinLDT = oLDT.now();
		private LocalDateTime oMaxLDT = oLDT.now();
		private LocalDate oMinLD = oLD.now();
		private LocalDate oMaxLD = oLD.now();
		private Date oMinDate = new Date();
		private Date oMaxDate = new Date();

		String zeroes = "000000000000000000";

		private boolean sortAsc;
		private boolean sortDsc;

		private char dateInterval;
		private int dateIntervalNum;
		private String dateStr;
		private long startDate;
		private LocalDateTime startLDT;

    		//===================================================================================
    		public void debug(String msg){
			//System.out.println(msg);
 		}
    		public void debug1(String msg){
			//System.out.println(msg);
 		}
    		public void debug2(String msg){
			System.out.println(msg);
 		}

		public void setMGetter(Method x){
			this.mGetter = x;
		}
		public Method getMGetter(){
			return this.mGetter;
		}
		public void setOpTxt(String x){
			this.opTxt = x;
		}
		public String getOpTxt(){
			return this.opTxt;
		}
		public void setOpCnt(int x){
			this.opCnt = x;
		}
		public int getOpCnt(){
			return this.opCnt;
		}
		public void setIsArray(boolean x){
			this.isArray = x;
		}
		public boolean getIsArray(){
			return this.isArray;
		}
		public void setIsMethod(boolean x){
			this.isMethod = x;
		}
		public boolean getIsMethod(){
			return this.isMethod;
		}
		public Method getmCompareArray(){
			return mCompareArray;
		}
		public void setmCompareArray(Method m){
			mCompareArray = m;
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
		public void setStartLDT(LocalDateTime x){
			this.startLDT = x;
		}
		public LocalDateTime getStartLDT(){
			return this.startLDT;
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
		public void setOrigFieldType(String x){
			this.origFieldType = x;
		}
		public String getOrigFieldType(){
			return this.origFieldType;
		}
		//======================================================
		public void setOArrayStr(String x)
		{
			oArrayString = x;
		}
		public String getOArrayStr()
		{
			return oArrayString;
		}
		//===================================================================================
		public void setOLong(String x)
		{
			if (isArray){
				String[] xx = x.split("!");
				oLong = Long.parseLong(xx[0]);
				oMinLong = oLong;
				oMaxLong = oLong;
				for (int i = 0; i < xx.length; i++){
					oLong = Long.parseLong(xx[i]); 
					if (oMinLong > oLong) oMinLong = oLong;
					if (oMaxLong < oLong) oMaxLong = oLong;
				}
			}else{
				oLong = Long.parseLong(x);
			}
		}
		public void setOInteger(String x){
			if (isArray){
				String[] xx = x.split("!");
				oLong = Long.parseLong(xx[0]);
				oMinLong = oLong;
				oMaxLong = oLong;
				for (int i = 0; i < xx.length; i++){
					oLong = Long.parseLong(xx[i]); 
					if (oMinLong > oLong) oMinLong = oLong;
					if (oMaxLong < oLong) oMaxLong = oLong;
				}
			}else{
				oInteger = Integer.parseInt(x);
			}
		}
		public void setOByte(String x){
			if (isArray){
				String[] xx = x.split("!");
				oLong = Long.parseLong(xx[0]);
				oMinLong = oLong;
				oMaxLong = oLong;
				for (int i = 0; i < xx.length; i++){
					oLong = Long.parseLong(xx[i]); 
					if (oMinLong > oLong) oMinLong = oLong;
					if (oMaxLong < oLong) oMaxLong = oLong;
				}
			}else{
				oByte = Byte.parseByte(x);
			}
		}
		public void setOShort(String x){
			if (isArray){
				String[] xx = x.split("!");
				oLong = Long.parseLong(xx[0]);
				oMinLong = oLong;
				oMaxLong = oLong;
				for (int i = 0; i < xx.length; i++){
					oLong = Long.parseLong(xx[i]); 
					if (oMinLong > oLong) oMinLong = oLong;
					if (oMaxLong < oLong) oMaxLong = oLong;
				}
			}else{
				oShort = Short.parseShort(x);
			}
		}
		public void setODouble(String x){
			if (isArray){
				String[] xx = x.split("!");
				oDouble = Double.parseDouble(xx[0]);
				oMinDouble = oDouble;
				oMaxDouble = oDouble;
				for (int i = 0; i < xx.length; i++){ 
					oDouble = Double.parseDouble(xx[i]);
					if (oMinDouble > oDouble) oMinDouble = oDouble;
				 	if (oMaxDouble < oDouble) oMaxDouble = oDouble;
				}
			}else{
				oDouble = Double.parseDouble(x);
			}
		}
		public void setOFloat(String x){
			if (isArray){
				String[] xx = x.split("!");
				oDouble = Double.parseDouble(xx[0]);
				oMinDouble = oDouble;
				oMaxDouble = oDouble;
				for (int i = 0; i < xx.length; i++){ 
					oDouble = Double.parseDouble(xx[i]);
					if (oMinDouble > oDouble) oMinDouble = oDouble;
				 	if (oMaxDouble < oDouble) oMaxDouble = oDouble;
				}
			}else{
				oFloat = Float.parseFloat(x);
			}
		}
		public void setOCharacter(String x){
			if (isArray){
				String[] xx = x.split("!");
				oMinString = xx[0];
				oMaxString = oMinString;
				for (int i = 0; i < xx.length; i++){ 
					if (oMinString.compareTo(xx[i]) < 0) oMinString = xx[i];
				 	if (oMaxString.compareTo(xx[i]) > 0) oMaxString = xx[i];
				}
			}else{
				oChar = x.charAt(0);
			}
		}
		public void setOString(String x){
			if (isArray){
				String[] xx = x.split("!");
				oMinString = xx[0];
				oMaxString = oMinString;
				for (int i = 0; i < xx.length; i++){ 
					if (oMinString.compareTo(xx[i]) < 0) oMinString = xx[i];
				 	if (oMaxString.compareTo(xx[i]) > 0) oMaxString = xx[i];
				}
			}else{
				oString = x;
			}
		}
		public void setOBoolean(String x){
			x = x.toUpperCase();
			if (x.equals("TRUE")) oBoolean = true; else oBoolean = false;
		}
		public void setOBigDecimal(String x){
			if (isArray){
				String[] xx = x.split("!");
				oBigDecimal = new BigDecimal(xx[0]);
				oMinBigD = oBigDecimal;
				oMaxBigD = oBigDecimal;
				for (int i = 0; i < xx.length; i++){ 
					oBigDecimal = new BigDecimal(xx[i]);
					if (oMinBigD.compareTo(oBigDecimal) < 0) oMinBigD = oBigDecimal;
				 	if (oMaxBigD.compareTo(oBigDecimal) > 0) oMaxBigD = oBigDecimal;
				}
			}else{
				oBigDecimal = new BigDecimal(x);
			}
		}
		public void setOBigInteger(String x){
			if (isArray){
				String[] xx = x.split("!");
				oBigInteger = new BigInteger(xx[0]);
				oMinBigI = oBigInteger;
				oMaxBigI = oBigInteger;
				for (int i = 0; i < xx.length; i++){ 
					oBigInteger = new BigInteger(xx[i]);
					if (oMinBigI.compareTo(oBigInteger) < 0) oMinBigI = oBigInteger;
				 	if (oMaxBigI.compareTo(oBigInteger) > 0) oMaxBigI = oBigInteger;
				}
			}else{
				oBigInteger = new BigInteger(x);
			}
		}
		public void setOLocalDateTime(String x){
			DRLocalDateTime dldt = new DRLocalDateTime();
			if (isArray){
				String[] xx = x.split("!");
				oLDT = dldt.setDateStr(xx[0]).getDateLDT();
				oMinLDT = oLDT;
				oMaxLDT = oLDT;
				for (int i = 0; i < xx.length; i++){ 
					oLDT = dldt.setDateStr(xx[i]).getDateLDT();
					if (oMinLDT.compareTo(oLDT) < 0) oMinLDT = oLDT;
				 	if (oMaxLDT.compareTo(oLDT) > 0) oMaxLDT = oLDT;
				}
			}else{
				//debug2("soldt - x="+x);
				oLDT = dldt.setDateStr(x).getDateLDT();
			}
		}
		public void setOLocalDate(String x){
			DRLocalDateTime dldt = new DRLocalDateTime();
			if (isArray){
				String[] xx = x.split("!");
				oLD = dldt.setLDString(xx[0]).getDateLocalDate();
				oMinLD = oLD;
				oMaxLD = oLD;
				for (int i = 0; i < xx.length; i++){ 
					oLD = dldt.setLDString(xx[i]).getDateLocalDate();
					if (oMinLD.compareTo(oLD) < 0) oMinLD = oLD;
				 	if (oMaxLD.compareTo(oLD) > 0) oMaxLD = oLD;
				}
			}else{
				//debug2("soldt - x="+x);
				oLD = dldt.setLDString(x).getDateLocalDate();
			}
		}
		public void setODate(String x){
			DRLocalDateTime dldt = new DRLocalDateTime();
			if (isArray){
				String[] xx = x.split("!");
				oDate = dldt.setDateStr(xx[0]).toDateDate();
				oMinDate = oDate;
				oMaxDate = oDate;
				for (int i = 0; i < xx.length; i++){ 
					oDate = dldt.setDateStr(xx[i]).toDateDate();
					if (oMinDate.compareTo(oDate) < 0) oMinDate = oDate;
				 	if (oMaxDate.compareTo(oDate) > 0) oMaxDate = oDate;
				}
			}else{
				//debug2("soldt - x="+x);
				oDate = dldt.setDateStr(x).toDateDate();
			}
		}
		//=====================================================================================
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
		public String getOLocalDateTime(){
			DRLocalDateTime ldt = new DRLocalDateTime();
			return ldt.setDateLDT(this.oLDT).toStringDate();
		}
		public String getOLocalDate(){
			DRLocalDateTime ldt = new DRLocalDateTime();
			return ldt.setDateLocalDate(this.oLD).toStringLocalDate();
		}
		public String getODate(){
			DRLocalDateTime ldt = new DRLocalDateTime();
			return ldt.setDateDate(this.oDate).toStringDate();
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
		public void setVLocalDateTime(String x){
			DRLocalDateTime dlt = new DRLocalDateTime();
			vLDT = dlt.setDateStr(x).getDateLDT();
			//debug2("setvldt vldt = "+vLDT+" x="+x);
		}
		public void setVLocalDate(String x){
			DRLocalDateTime dlt = new DRLocalDateTime();
			vLD = dlt.setLDString(x).getDateLocalDate();
			//debug2("setvld vld = "+vLD+" x="+x);
		}
		public void setVDate(String x){
			DRLocalDateTime dlt = new DRLocalDateTime();
			vDate = dlt.setDateStr(x).toDateDate();
			//debug2("setvldt vldt = "+vLDT+" x="+x);
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
		public String getVLocalDateTime(){
			DRLocalDateTime dlt = new DRLocalDateTime();
			return dlt.setDateLDT(vLDT).toStringDate();
		}
		public String getVLocalDate(){
			DRLocalDateTime dlt = new DRLocalDateTime();
			return dlt.setDateLocalDate(vLD).toStringLocalDate();
		}
		public String getVDate(){
			DRLocalDateTime dlt = new DRLocalDateTime();
			return dlt.setDateDate(vDate).toStringDate();
		}
		//======================================================	

		public int compareArrayLess()
		{
			oLong =  oMinLong;
			oInteger =  (int)oMinLong;
			oShort =  (short)oMinLong;
			oByte =  (byte)oMinLong;
			oString =  oMinString;
			oChar =  oMinString.charAt(0);
			oDouble = oMinDouble;
			oFloat =  (float)oMinDouble;
			oBigDecimal =  oMinBigD;
			oBigInteger =  oMinBigI;
			return 2;
		}
		public int compareArrayGreater()
		{
			//debug2("cag - start oml="+oMaxLong);
			try{
				oLong =  oMaxLong;
				oInteger =  (int)oMaxLong;
				oShort =  (short)oMaxLong;
				oByte =  (byte)oMaxLong;
				oString =  oMaxString;
				oChar =  oMaxString.charAt(0);
				oDouble = oMaxDouble;
				oFloat =  (float)oMaxDouble;
				oBigDecimal =  oMaxBigD;
				oBigInteger =  oMaxBigI;
			}catch (Exception xx){ debug2("excep - "+xx.toString());return 99; }
			return 2;
		}
		public int compareArrayMin()
		{
			return compareArrayLess();
		}
		public int compareArrayMax()
		{
			return compareArrayGreater();
		}
		public int compareArrayEquals()
		{
			int greater = 0;
			String ostr = "!" + oArrayString;
			String vstr = "!" + vOrigString + "!";
			if (ostr.indexOf(vstr) > -1) greater = 0; else greater = -1;
			return greater;
		}
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
		public int compareLocalDateTime(){
			int greater = 0;
			int x1 = 99;
			try
			{
				x1 = vLDT.compareTo(oLDT);
			}catch (NullPointerException npe){
				greater = 99;				//this null rec will be ignored
			}
			if (x1 == 0) greater = 0;			//V > O
			if (x1 > 0) greater = -1;
			if (x1 < 0) greater = 1;
			return greater;
		}
		public int compareLocalDate(){
			int greater = 0;
			int x1 = 99;
			try
			{
				x1 = vLD.compareTo(oLD);
			}catch (NullPointerException npe){
				greater = 99;				//this null rec will be ignored
			}
			if (x1 == 0) greater = 0;			//V > O
			if (x1 > 0) greater = -1;
			if (x1 < 0) greater = 1;
			return greater;
		}
		public int compareDate(){
			int greater = 0;
			int x1 = 99;
			try
			{
				x1 = vDate.compareTo(oDate);
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
                                                                                                            