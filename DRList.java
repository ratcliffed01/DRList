// to compile do from folder above C:\projects\DRList>javac -cp ../ DRList.java
//========================================================================
//	Author - David Ratcliffe	Version - 1.0	Date - 21/03/2019
//
//	programs - DRList.java, DRArrayList.java, DRIndex.java, DRBTree.java, DRCode.java, DRListTBL
//
//	This is a variable collection list which uses index to get element number or key to get element from
//	entered key.
//
//	User Interface functions :-
//
//	void DRadd(T obj1)		- adds object (Integer, String or VO) without a key to the end of the list
//	boolean DRdelete()		- delete element from list once currency has been set by DRget. Initially
//					  flagged as deleted once 100 deleted removed and reindexed etc, returns boolean
//	void DRaddkey(T obj1, String sk)- adds object with a key in String format, to the end of the list
//	boolean hasNext()		- checks if there are more to the list from set currency, returns true or false
//	T DRgetKey(String key)		- gets element based on entered key, sets currency in the list and returns object
//	T DRget(int i)			- gets element using element number, sets currency and returns object
//	T DRnext()			- gets next element and returns object
//	T DRprev()			- gets previous element from currency and returns object
//	T DRgetFirst()			- gets first element in list and sets currency, returns object
//	T DRgetLast()			- gets last element in the list and sets currency, returns object
//	void DRclear()			- clears list, index, BTree and size variable
//	int DRsize()			- returns number of elements in list
//	DRArrayList<T>[] toArray()	- converts list to an array which will also contain sortkey
//	void DRsortAsc()		- if sortkey added will sort in key order, ascending
//	void DRsortDsc()		- if sortkey added will sort in key order, descending
//	void toDRList(DRArrayList<T>[] xx)- converts an array to DRList

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

public class DRList<T>
{

	DRListTBL<T> drl = new DRListTBL<T>();

    	//===================================================================================
    	public static void debug(String msg){
		System.out.println(msg);
    	}
	//================================================
	public boolean DRdelete(){

		DRCode<T> drcode = new DRCode<T>();
		drl = drcode.DRdelete(drl);
		
		boolean deleted = true;
		if (drl.success == -1) deleted = false;
		return deleted;
	}


	//================================================
	public void DRadd(T obj1){

		DRCode<T> drcode = new DRCode<T>();
		drl = drcode.DRadd(obj1,drl);

		return;
	}

	//================================================
	public void DRaddkey(T obj1, String sk){

		DRCode<T> drcode = new DRCode<T>();
		drl = drcode.DRaddkey(obj1,sk,drl);
		return;
	}

	//==================================================
	public boolean hasNext(){
		DRCode<T> drcode = new DRCode<T>();
		return drcode.hasNext(drl);
	}

	//==================================================
	public T DRgetKey(String key){

		DRCode<T> drcode = new DRCode<T>();

		drl.success = 0;
		drl = drcode.DRgetKey(key,drl);
		if (drl.success == -1) return null;

		return drl.dl.obj;

	}
	//==================================================
	public T DRget(int i){

		DRCode<T> drcode = new DRCode<T>();
		drl.success = 0;
		drl = drcode.DRgetEle(i,drl);
		if (drl.success == -1) return null;

		return drl.dl.obj;

	}

	//==================================================
	public T DRnext(){
		DRCode<T> drcode = new DRCode<T>();
		drl = drcode.DRnext(drl);
		if (drl.success == -1) return null;
		//System.out.println("drn - dlcnt="+drl.dl.count+" "+drl.dl.sortKey);
		return drl.dl.obj;
	}
	//==================================================
	public T DRprev(){
		DRCode<T> drcode = new DRCode<T>();
		drl = drcode.DRprev(drl);
		if (drl.success == -1) return null;
		return drl.dl.obj;
	}
	//==================================================
	public T DRgetFirst(){
		DRCode<T> drcode = new DRCode<T>();
		drl = drcode.DRgetFirst(drl);
		if (drl.success == -1) return null;
		return drl.dl.obj;
	}
	//==================================================
	public T DRgetLast(){
		DRCode<T> drcode = new DRCode<T>();
		drl = drcode.DRgetLast(drl);
		if (drl.success == -1) return null;
		return drl.dl.obj;
	}
	//=================================================
	public void DRclear(){
		DRCode<T> drcode = new DRCode<T>();
		drl = drcode.clear(drl);
	}
	//==================================================
	public int DRsize(){
		return drl.size;
	}

	//=========================================================
	public DRArrayList<T>[] toArray(){

		DRCode<T> drcode = new DRCode<T>();
		return drcode.toArraySub(drl.dl,drl.fdl,drl.size);
	}

	//=========================================================
	public void toDRList(DRArrayList<T>[] xx){

		DRCode<T> drcode = new DRCode<T>();
		drl = drcode.toDRListsub(xx,drl);

		return;
	}
	//=========================================================
	public void DRsortAsc(){

		DRCode<T> drcode = new DRCode<T>();

		long sti = System.currentTimeMillis();
		debug("asc - size="+drcode.DRsize(drl)+" fsk="+drl.fdl.sortKey);

		drl = drcode.DRsort(drl, 1);

		long diff = System.currentTimeMillis() - sti;
		debug("asc - size="+drcode.DRsize(drl)+" elapsed="+diff+"ms");
		return;
	}

	//=========================================================
	public void DRsortDsc(){

		DRCode<T> drcode = new DRCode<T>();

		long sti = System.currentTimeMillis();
		debug("dsc - size="+drcode.DRsize(drl)+" fsk="+drl.fdl.sortKey);

		drl = drcode.DRsort(drl, -1);

		long diff = System.currentTimeMillis() - sti;
		debug("dsc - size="+drcode.DRsize(drl)+" elapsed="+diff+"ms");
		return;
	}
}