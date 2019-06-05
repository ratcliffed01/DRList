// to compile do from folder above C:\projects\DRList>javac -Xlint:unchecked -cp ../ DRList.java
//========================================================================
//	Author - David Ratcliffe	Version - 1.2	Date - 21/03/2019
//
//	ver1.1	- Add new functions to allow duplicates in BTree and reIndex, plus fix some bugs
//	ver1.2	- Add DRFind, new search facility and sort on Objects field
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
//	boolean hasDuplicates()		- checks for duplicate keys
//	int[] DRgetKeyDupI(String key)	- gets all element nos for a duplicatekey, so currency can be set on any 1 duplicate
//	Object[] DRgetKeyDup(String key)- gets all objects for duplicate key, so duplicates can be seen
//	boolean reIndex()		- reloads index and btree and removes deleted elements, takes about 90ms for 10000
//	void DRset(T obj1)		- reset existing value to the object, currency needs to be set first
//	boolean DRinsert(T obj)		- insert new element after current element, currency needs to be set first
//	boolean DRinsertKey(String sk, T obj)
//					- insert new element with key after current element,  currency needs to be set first
//	void DRsortNoKeyAsc(String fieldName) throws NoSuchFieldException
//					- sorts ascending on field in object which is diffeent from sorkkey
//	void DRsortNoKeyDsc(String fieldName) throws NoSuchFieldException
//					- sorts descending on field in object which is diffeent from sorkkey
//	DRFindObjVO<T> DRFind(String fieldName, String operator, String value) 
//					- returns class DRFindObjVO<T> which contains T[] array with DRFindAnd, DRFindOr and DRFMinus,
//					  searches the collection on a particular field within the object or
//					  on the whole object if just 1 field. Fieldname can be null of a valid fieldname, the
//					  operator can be ">","<","Like","Min","Max", the value is null for Max/Min else is passed as 
//					  a String and will be converted to the same field type of the fieldname. If fieldname has //					  asc/dsc delimited by a space then the return object array is sorted ascending/descending
//					  accordingly 
//	DRFindObjVO<T> DRFindAnd(String fieldName, String operator, String value) 
//					- based on DRFind and allows searching on the Object array passed via class DRFindObjVO<T>
//					  using fieldName, operator and value as extra criteria. These parameters are the same DRFind. 
//					  A reduced Object array via class DRFindObjVO<T> is passed back.
//	DRFindObjVO<T> DRFindOr(String fieldName, String operator, String value)
//					- Based on DRFind and returns an Object array combined with the passed Object array. The
//					  search criteria is the same as DRFind and serches the whole collection and combines
//					  the 2 arrays.
//	DRFindObjVO<T> DRFindMinus(String fieldName, String operator, String value)
//					- Based on DRFind and returns an Object array with the new selection criteria removed 
//	T[] getObjArray()		- returns the Object array from the DRFindObjVO<T> and is used at the end ie
// Object[] obj = dl4.DRFind("surName","=","Number100").DRFindOr("surName","=","Number101").DRFindOr("surName","=","Number102").getObjArray();

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
import DRList.DRFind;

public class DRList<T>
{

	DRListTBL<T> drl = new DRListTBL<T>();

    	//===================================================================================
    	public static void debug(String msg){
		//System.out.println(msg);
    	}
	//================================================
	public DRFindObjVO<T> DRFind(String fieldName, String operator, String value)
		throws DRNoMatchException
	{
		DRFind<T> drf = new DRFind<T>();
		return drf.DRFind(fieldName,operator,value,drl);
	}
	//=========================================================
	public void DRsortNoKeyAsc(String fieldName) throws DRNoMatchException
	{

		DRFind<T> drf = new DRFind<T>();
		DRCode<T> drcode = new DRCode<T>();

		long sti = System.currentTimeMillis();
		debug("asc - size="+drcode.DRsize(drl)+" sn="+fieldName);

		drl = drf.DRsortNoKey(drl, 1,fieldName);

		long diff = System.currentTimeMillis() - sti;
		debug("asc - size="+drcode.DRsize(drl)+" elapsed="+diff+"ms");
		return;
	}
	//=========================================================
	public void DRsortNoKeyDsc(String fieldName) throws DRNoMatchException
	{

		DRFind<T> drf = new DRFind<T>();
		DRCode<T> drcode = new DRCode<T>();

		long sti = System.currentTimeMillis();
		debug("dsc - size="+drcode.DRsize(drl)+" sn="+fieldName);

		drl = drf.DRsortNoKey(drl, -1,fieldName);

		long diff = System.currentTimeMillis() - sti;
		debug("asc - size="+drcode.DRsize(drl)+" elapsed="+diff+"ms");
		return;
	}
	//================================================
	//check current has duplicates
	public boolean hasDuplicates(){
		DRCode<T> drcode = new DRCode<T>();
		return drcode.hasDuplicates(drl);
	}
	//================================================
	public int[] DRgetKeyDupI(String key){
		DRCode<T> drcode = new DRCode<T>();
		return drcode.DRgetKeyDupI(key,drl);
	}
	//================================================
	public Object[] DRgetKeyDup(String key){
		DRCode<T> drcode = new DRCode<T>();
		return drcode.DRgetKeyDup(key,drl);
	}
	//================================================
	public boolean reIndex(){

		DRCode<T> drcode = new DRCode<T>();
		drl.success = 1;
		drl = drcode.reloadIndex(drl);
		
		boolean reindex = true;
		if (drl.success == -1) reindex = false;
		return reindex;
	}
	//================================================
	public boolean DRdelete(){

		DRCode<T> drcode = new DRCode<T>();
		drl.success = 1;
		drl = drcode.DRdelete(drl);
		
		boolean deleted = true;
		if (drl.success == -1) deleted = false;
		return deleted;
	}
	//================================================
	// currency should have been set via DRget or DRgetKey, this will insert with no key
	public boolean DRinsert(T obj){

		try
		{
			DRCode<T> drcode = new DRCode<T>();
			drl = drcode.DRinsert(null,obj,drl);
		}
		catch (DRListException de)
		{
			return false;
		}
		return true;
	}

	//================================================
	// currency should have been set via DRget or DRgetKey, this will insert with a key
	public boolean DRinsertKey(String sk, T obj){

		try
		{
			DRCode<T> drcode = new DRCode<T>();
			drl = drcode.DRinsert(sk,obj,drl);
		}
		catch (DRListException de)
		{
			return false;
		}
		return true;
	}
	//================================================
	public void DRset(T obj1){

		DRCode<T> drcode = new DRCode<T>();
		drl = drcode.DRset(obj1,drl);

		return;
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