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

	public class ProcessTypeVO {

	    	//===================================================================================
    		public static void debug2(String msg){
			System.out.println(msg);
    		}

		public Boolean isValidLong(String value){
			boolean ret = false;	
			try{
				Long.parseLong(value);
				ret = true;
			}catch (NumberFormatException e){
				ret = false;
			}
			return ret;
		}
		public Boolean isValidDouble(String value){
			boolean ret = false;	
			try{
				Double.parseDouble(value);
				ret = true;
			}catch (NumberFormatException e){
				ret = false;
			}
			return ret;
		}
		public Boolean isValidBigDecimal(String value){
			boolean ret = false;	
			try{
				new BigDecimal(value);
				ret = true;
			}catch (NumberFormatException e){
				ret = false;
			}
			return ret;
		}
		public Boolean isValidBigInteger(String value){
			boolean ret = false;	
			try{
				new BigInteger(value);
				ret = true;
			}catch (NumberFormatException e){
				ret = false;
			}
			return ret;
		}
		public Boolean isValidBoolean(String value){
			String valBool = value.toUpperCase();
			return (valBool.equals("TRUE")||valBool.equals("FALSE"));
		}
		public Boolean isValidByte(String value){
			boolean ret = false;	
			try{
				Byte.parseByte(value);
				ret = true;
			}catch (NumberFormatException e){
				ret = false;
			}
			return ret;
		}
		public Boolean isValidShort(String value){
			boolean ret = false;	
			try{
				Short.parseShort(value);
				ret = true;
			}catch (NumberFormatException e){
				ret = false;
			}
			return ret;
		}
		public Boolean isValidFloat(String value){
			boolean ret = false;	
			try{
				Float.parseFloat(value);
				ret = true;
			}catch (NumberFormatException e){
				ret = false;
			}
			return ret;
		}
		public Boolean isValidInteger(String value){
			boolean ret = false;	
			try{
				Integer.parseInt(value);
				ret = true;
			}catch (NumberFormatException e){
				ret = false;
			}
			return ret;
		}
		public Boolean isValidString(String value){
			return true;
		}
		public Boolean isValidCharacter(String value){
			return true;
		}
		//============================================================

	}
