// to compile do from folder above c:\>javac BJSS_TEST\PricingBasket.java
// to compile do from folder above c:\>java BJSS_TEST.PricingBasket SOUP

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

public class DRCode<T>
{

    	//===================================================================================
    	public static void debug(String msg){
		//System.out.println(msg);
    	}
    	//===================================================================================
    	public static void debug1(String msg){
		//System.out.println(msg);
    	}
    	//===================================================================================
    	public static void debug2(String msg){
		//System.out.println(msg);
    	}

	//================================================
	//currency needs to be set first using DRget or DRgetKey
	public DRListTBL<T> DRset(T obj, DRListTBL<T> drl){

		drl.dl.obj = obj;		
		return drl;
	}
	//================================================
	public boolean hasDuplicates(DRListTBL<T> drl){
		debug("hd = bkey="+drl.bt.key);
		if (drl.bt.duplicate > 0) return true;
		return false;
	}
	//================================================
	public DRListTBL<T> deleteBTree(int count, String key, DRListTBL<T> drl){

		DRCode<T> drcode = new DRCode<T>();
		long sti = System.currentTimeMillis();
		String dir = "";
		String dupKey = key;
		int ind = 0;
		int dupCount = 0;

		drl.bt = drl.root;

		while (true){
			ind = drl.bt.key.indexOf("$$D");
			if (ind == -1) ind = drl.bt.key.length();
			if (drcode.isGreater(drl.bt.key.substring(0,ind),key)==0){
				if (drl.bt.count == count){		//not a duplicate, original
					drl.bt.deleted = true;
					debug("dbt - delete key="+drl.bt.key+" cnt="+drl.bt.count+" dupc="+drl.bt.duplicate);
					break;
				}
				dupKey = drl.bt.key;
			}
			if (drcode.isGreater(drl.bt.key,dupKey)==1){		//key < parent key so go left
				if (drl.bt.left == null){
					dir += "l";
					count = -1;
					break;
				}else{
					dir += "l";
					drl.bt = drl.bt.left;
				}
			}else{						//must > parent key so goto right
				if (drl.bt.right == null){
					dir += "r";
					count = -1;
					break;
				}else{
					dir += "r";
					drl.bt = drl.bt.right;
				}
			}
		}

		debug("dbt - "+dir+" cnt="+count+" key="+key+" bkey="+drl.bt.key+" bcnt="+drl.bt.count+
			" ti="+(System.currentTimeMillis()-sti)+"ms");
		return drl;
	}
	//================================================
	//returns element number in an array so currency can be set to allow duplicate to be deleted
	public int[] DRgetKeyDupI(String key, DRListTBL<T> drl){

		DRCode<T> drcode = new DRCode<T>();
		long sti = System.currentTimeMillis();

		drl = drcode.DRgetKey(key, drl);			//get original key
		if (drl.success == -1) return null;
		debug("gdupi - "+drl.dl.obj+" key="+key+" bkey="+drl.bt.key+" cnt="+drl.bt.count+" dup="+drl.bt.duplicate);

		int i = 0;
		int count = 0;
		int ind = 0;
		String dir = "";
		String cntStr = "";

		drl.success = 0;
		String dupKey = key + "$$D";

		drl.bt = drl.root;

		while (true){
			ind = drl.bt.key.indexOf("$$D");
			if (ind == -1) ind = drl.bt.key.length();
			if (drcode.isGreater(drl.bt.key.substring(0,ind),key)==0){
				if (!drl.bt.deleted){
					cntStr += drl.bt.count +"/";
					i++;
				}
				dupKey = drl.bt.key;
				debug("gdup - "+dupKey+" cnt="+drl.bt.count+" key="+key+" i="+i);
			}
			if (drcode.isGreater(drl.bt.key,dupKey)==1){		//key < parent key so go left
				if (drl.bt.left == null){
					dir += "l";
					count = -1;
					break;
				}else{
					dir += "l";
					drl.bt = drl.bt.left;
				}
			}else{						//must be -1 key > parent key so goto right
				if (drl.bt.right == null){
					dir += "r";
					count = -1;
					break;
				}else{
					dir += "r";
					drl.bt = drl.bt.right;
				}
			}
		}

		String[] str = cntStr.split("/");
		int[] cnt = new int[str.length];
		for (i = 0; i < str.length; i++)
			cnt[i] = Integer.parseInt(str[i]);

		debug("gdupi - "+dir+" cstr="+cntStr+" str0="+str[0]+" siz="+drl.size+" strl="+str.length+
			" ti="+(System.currentTimeMillis()-sti)+"ms");

		return cnt;
	}

	//================================================
	public Object[] DRgetKeyDup(String key, DRListTBL<T> drl){

		DRCode<T> drcode = new DRCode<T>();
		long sti = System.currentTimeMillis();

		drl = drcode.DRgetKey(key, drl);			//get original key
		if (drl.success == -1) return null;

		int i = 0;
		int j = 0;
		int count = 0;
		int ind = 0;
		String dir = "";
		String cntStr = "";

		drl.success = 0;
		String dupKey = key + "$$D";

		debug("gdup - "+drl.dl.obj+" key="+drl.dl.sortKey+" cnt="+drl.bt.count);

		drl.bt = drl.root;

		while (true){
			ind = drl.bt.key.indexOf("$$D");
			if (ind == -1) ind = drl.bt.key.length();
			if (drcode.isGreater(drl.bt.key.substring(0,ind),key)==0){
				if (!drl.bt.deleted){
					cntStr += drl.bt.count + "/";
				}
				dupKey = drl.bt.key;
				debug("gdup - "+dupKey+" cnt="+drl.bt.count+" key="+key+" i="+i);
			}
			if (drcode.isGreater(drl.bt.key,dupKey)==1){		//key < parent key so go left
				if (drl.bt.left == null){
					dir += "l";
					break;
				}else{
					dir += "l";
					drl.bt = drl.bt.left;
				}
			}else{						//must be -1 key > parent key so goto right
				if (drl.bt.right == null){
					dir += "r";
					break;
				}else{
					dir += "r";
					drl.bt = drl.bt.right;
				}
			}
		}

		String[] str = cntStr.split("/");
		int[] cnt = new int[str.length];
		for (i = 0; i < str.length; i++)
			cnt[i] = Integer.parseInt(str[i]);
		Object[] o = new Object[cnt.length];

		if (cnt.length != 0){				//key not found
			drl.fromGetKey = true;
			i = 0;
			while (i < cnt.length){
				drl = drcode.DRgetEle(cnt[i],drl);
				if (drl.success != -1){			//getele has failed either deleted or cnt wrong
					o[i] = drl.dl.obj;
					debug("gdup - dcnt="+drl.dl.count+" ocnt="+cnt[i]+"  dsk="+drl.dl.sortKey+
							" o="+drl.dl.obj);
					i++;
				}
			}
			drl.fromGetKey = false;
		}
		debug("gdup - "+dir+" "+count+" key="+drl.bt.key+" cl="+cnt.length+" siz="+drl.size+" dn="+drl.deleteNum+
			" ti="+(System.currentTimeMillis()-sti)+"ms");

		return o;
	}
	//================================================
	public DRListTBL<T> DRdelete(DRListTBL<T> drl){

		long sti = System.currentTimeMillis();
		DRCode<T> drcode = new DRCode<T>();

		DRArrayList<T> dl = drl.dl;
		DRArrayList<T> fdl = drl.fdl;

		//currency on dl should be set already
		if (dl == null) return null;

		dl.deleted = true;				//set the delete flag
		drl.deleteNum++;
		drl.size--;

		drl.dl = dl;
		drl.fdl = fdl;

		if (drl.dl.sortKey != null){
			drl = drcode.deleteBTree(drl.dl.count,drl.dl.sortKey,drl);	//set drbtree delete flag to true	
			if ((drl.bt.key.indexOf("$$D") > -1)||(drl.bt.duplicate>0)){
				drl = drcode.DRgetKey(drl.dl.sortKey,drl);			//get original key
				debug("del - deleted ele - "+drl.dl.count+" dsk="+drl.dl.sortKey);
				if (drl.bt.key.indexOf("$$D") == -1){
					drl.bt.duplicate--;				//update original count
				}else{
					drl.bt.duplicate++;			//orig deleted so set dupcnt
				}
			}
		}
		if (drl.deleteNum == 100) drl = drcode.reloadIndex(drl);	//reload index and btree and reset delete count

		drl.success = 1;			//success

		debug("del - ti="+(System.currentTimeMillis() - sti)+"ms");
		return drl;
	}

	//================================================
	public DRListTBL<T> reloadIndex(DRListTBL<T> drl){

		long sti = System.currentTimeMillis();
		DRCode<T> drcode = new DRCode<T>();

		DRArrayList<T> dl = drl.dl;
		DRArrayList<T> fdl = drl.fdl;

		int saveCnt = dl.count;

		debug("rli - get1st="+fdl.count+" fsk="+fdl.sortKey);

		DRArrayList<T>[] xx = drcode.toArraySub(dl,fdl,drl.size);
		debug2("rli - xxl="+xx.length+" xsk="+xx[xx.length - 1].sortKey+" xcnt="+xx[xx.length - 1].count);

		dl = fdl;
		drl = drcode.clear(drl);

		drl = drcode.toDRListsub(xx,drl);

		drl.deleteNum = 0;			//initialise delete count

		Arrays.fill(xx, null );				//clear down the array

		//get currency to restart
		drl = drcode.DRgetEle(saveCnt,drl);

		debug2("rli - size="+drl.size+" lastcnt="+fdl.prev.count+" ti="+(System.currentTimeMillis() - sti)+"ms");

		return drl;
	}

	//================================================
	public DRBTree DRaddBTree(String key, int count,int line,DRBTree bt,DRBTree root){

		DRCode<T> drcode = new DRCode<T>();

		String dir = "";
		//debug("abt - start cnt="+count);
		if (line == 1){
			bt.count = count;
			bt.line = 1;
			bt.key = key;
			bt.left = null;
			bt.right = null;
			root = bt;
			debug("abt - root load cnt="+count+" rkey="+root.key);
		}else{
			DRBTree nbt = new DRBTree();		//new node
			nbt.count = count;
			nbt.line = line;
			nbt.key = key;
			nbt.left = null;
			nbt.right = null;
			bt = root;
			debug("bkey="+bt.key+" key="+key+" rkey="+root.key);
			while (true){
				if (drcode.isGreater(bt.key,key)==0){		//if duplacter add $$D1$ to key
					bt.duplicate++;				//upd dupcnt on original
					if (key.indexOf("$$D") > -1) key = key.substring(0,key.indexOf("$$D"));
					key += "$$D"+count+"$";
					nbt.key = key;
					debug("abt - dup found key="+key+" btk="+bt.key+" cnt="+count+" bcnt="+bt.count);
				}
				if (drcode.isGreater(bt.key,key)==1){		//key < parent key so go left
					if (bt.left == null){
						dir += "l";
						bt.left = nbt;
						break;
					}else{
						dir += "l";
						bt = bt.left;
					}
				}
				if (drcode.isGreater(bt.key,key)==-1){		//must be -1 key > parent key so goto right
					if (bt.right == null){
						dir += "r";
						bt.right = nbt;
						break;
					}else{
						dir += "r";
						bt = bt.right;
					}
				}
			}
		}
		debug("abt - bt key="+bt.key+ " "+dir);
		return bt;
	}

	//================================================
	public DRListTBL<T> DRadd(T obj1,DRListTBL<T> drl){

		DRCode<T> drcode = new DRCode<T>();
		DRArrayList<T> dl = drl.dl;
		DRArrayList<T> fdl = drl.fdl;
		DRIndex<T> fdi = drl.fdi;
		DRIndex<T> di = drl.di;

		DRArrayList<T> ldl = new DRArrayList<T>();		//last ele

		dl = fdl;						//set to 1st

		//debug("add obj1 ");

		if (dl.prev == null){
			dl.count = 1;
			drl.size = 1;
			dl.sortKey = null;
			dl.obj = obj1;
			dl.prev = dl;
			dl.next = dl;
			fdl = dl;
			debug("add key 1");
		}else{
			ldl = fdl.prev;				//1st prev goes to last element
			DRArrayList<T> ndl = new DRArrayList<T>();
			ndl.count = ldl.count + 1;
			drl.size++;
			ndl.sortKey = null;
			ndl.obj = obj1;
			ndl.prev = ldl;
			ndl.next = fdl;			//last next = 1st
			fdl.prev = ndl;
			ldl.next = ndl;
			dl = ndl;

			if (ndl.count%100 == 0){
				//debug("dls="+ldl.count);
				di = drcode.indAdd(dl,di,fdi);
				if (di.ind == 1) fdi = di;
			}
		}

		drl.dl = dl;
		drl.fdl = fdl;
		drl.di = di;
		drl.fdi = fdi;
		drl.success = 1;			//success

		return drl;
	}

	//================================================
	public  DRListTBL<T> DRaddkey(T obj1, String sk, DRListTBL<T> drl){

		DRCode<T> drcode = new DRCode<T>();
		DRArrayList<T> ldl = new DRArrayList<T>();		//last ele

		DRArrayList<T> dl = drl.dl;
		DRArrayList<T> fdl = drl.fdl;
		DRIndex<T> fdi = drl.fdi;
		DRIndex<T> di = drl.di;
		DRBTree bt = drl.bt;
		DRBTree root = drl.root;

		String key = "";
		int ncnt = 0;

		dl = fdl;					//set to 1st

		if (dl.prev == null){
			dl.count = 1;
			ncnt = dl.count;
			drl.size = 1;
			dl.sortKey = sk;
			dl.obj = obj1;
			dl.prev = dl;
			dl.next = dl;
			fdl = dl;
			debug("add key 1");
		}else{
			ldl = fdl.prev;				//1st prev goes to last element
			DRArrayList<T> ndl = new DRArrayList<T>();
			ndl.count = ldl.count + 1;
			ncnt = ndl.count;
			drl.size++;
			ndl.sortKey = sk;
			ndl.obj = obj1;
			ndl.prev = ldl;
			ndl.next = fdl;			//last next = 1st
			fdl.prev = ndl;
			ldl.next = ndl;
			dl = ndl;
			if (ndl.count%100 == 0){
				//debug("dls="+ldl.count);
				di = drcode.indAdd(dl,di,fdi);
				if (di.ind == 1) fdi = di;
			}
		}
		if (sk != null){
			bt = drcode.DRaddBTree(sk,ncnt,ncnt,bt,root);
			if (bt.line == 1) root = bt;
			debug("ak - add obj1 sk="+sk+" dup="+bt.duplicate+" ncnt="+ncnt);
		}

		drl.dl = dl;
		drl.fdl = fdl;
		drl.di = di;
		drl.fdi = fdi;
		drl.bt = bt;
		drl.root = root;
		drl.success = 1;			//success

		return drl;
	}


	//==================================================
	public DRListTBL<T> DRgetKey(String key,DRListTBL<T> drl){

		DRBTree bt = drl.bt;
		DRBTree root = drl.root;
		long sti = System.currentTimeMillis();

		DRCode<T> drcode = new DRCode<T>();

		if (root.left == null && root.right == null){ 
			drl.success = -1;
			return drl;
		}

		int count = 0;
		String dir = "";
		bt = root;
		String rkey = root.key +"";

		boolean ret1 = rkey.matches("^[0-9]+$");
		boolean ret2 = key.matches("^[0-9]+$");
		if (ret1 && ret2){
			if (rkey.length() == 10){
				if (rkey.charAt(0)=='0' && rkey.charAt(1)=='0'){
					key = "0000000000".substring(key.length()) + key;
				}
			}
		}
		debug("bkey="+bt.key+" key="+key+" rkey=["+rkey+"] "+ret1+ret2+rkey.length());
		int ind = 0;
		String dupKey = key + "$$D";

		while (true){
			//if original marked deleted find duplicate that is not deleted
			ind = bt.key.indexOf("$$D");
			if (ind == -1) ind = bt.key.length();
			if (drcode.isGreater(bt.key.substring(0,ind),key)==0){		//found so exit
				count = bt.count;
				if (!bt.deleted) break;
				dupKey = bt.key;
			}
			if (drcode.isGreater(bt.key,dupKey)==1){		//key < parent key so go left
				if (bt.left == null){
					dir += "l";
					count = -1;
					break;
				}else{
					dir += "l";
					bt = bt.left;
				}
			}else{						//must be -1 key > parent key so goto right
				if (bt.right == null){
					dir += "r";
					count = -1;
					break;
				}else{
					dir += "r";
					bt = bt.right;
				}
			}
		}

		debug("gbt - "+dir+" "+count+" bkey="+bt.key+" key="+key+" ti="+(System.currentTimeMillis()-sti)+"ms");
		drl.bt = bt;
		if (count == -1){				//key not found
			drl.success = -1;
		}else{
			drl.fromGetKey = true;
			drl = drcode.DRgetEle(count,drl);
			drl.fromGetKey = false;
		}

		return drl;
	}

	//==================================================
	public DRListTBL<T> DRgetEle(int i,DRListTBL<T> drl){

		DRArrayList<T> dl = drl.dl;
		DRArrayList<T> fdl = drl.fdl;
		DRIndex<T> fdi = drl.fdi;
		DRIndex<T> di = drl.di;

		drl.success = 1;
		debug("ge - start i="+i+" siz="+drl.size);

		DRCode<T> drcode = new DRCode<T>();

		DRArrayList<T> ldl = new DRArrayList<T>();		//last ele

		if (i < 0){
			drl.success = -1;
			return drl;			//element count must be > 0
		}
		if (i > (drl.size+drl.deleteNum)){		//element count must be > 0
			drl.success = -1;
			return drl;			
		}

		dl = fdl;					//set to 1st
		int j = 0;

		if (dl.prev == null){
			drl.success = -1;
			return drl;			
		}else{
			ldl = dl.prev;				//1st prev goes to last
			if (ldl.count < i) return null;
			if (fdl.saveIndex > 0 && fdl.saveIndex < i){
				j = fdl.saveIndex;
				dl = fdl.save;
			}
			debug("ge - i="+i+" ldlc="+ldl.count+" j="+j);
			if (i > 100 && (i - j) > 100){				//if true use index
				dl = drcode.getIndex(i,di,fdi);
				j = dl.count;
			}
			while (j < i && dl != null){
				dl = dl.next;
				j++;
			}
			debug("ge - j="+j+" i="+i+" dlc="+dl.count+" sk="+dl.sortKey+" del="+dl.deleted);
			if (drl.fromGetKey){
				if (dl.deleted){
					drl.success = -1;
					return drl;
				}
			}else{
				if (dl.deleted){
					drl.dl = dl;
					drl = drcode.nextNonDeleted(drl);
					if (drl.success == -1) return drl;
					dl = drl.dl;
					debug1("ge - dlc="+dl.count+" sk="+dl.sortKey+" del="+dl.deleted);
				}
			}
		}
		fdl.saveIndex = j;
		fdl.save = dl;

		drl.dl = dl;
		drl.fdl = fdl;
		drl.fdi = fdi;
		drl.di = di;
		drl.success = 0;

		return drl;
	}

	//=========================================================
	public DRArrayList<T> getIndex(int cnt, DRIndex<T> di, DRIndex<T> fdi){

		//should only be called if cnt > 100 therefore drindex should be there.

		di = fdi;
		int ind = (cnt / 1000) + 1;
		int rem = cnt % 1000;
		debug("gi - cnt="+cnt+" rem="+rem+" diind="+di.ind+" ind="+ind+" indn="+di.indNext.ind);
		if (di.indPrev.ind < ind) ind = di.indPrev.ind;		//get last if < ind set to ind else loop forever
		while (di.ind < ind)
			di = di.indNext;
		if (rem < 101) return di.indPrev.ind10;
		if (rem < 201) return di.ind1;
		if (rem < 301) return di.ind2;
		if (rem < 401) return di.ind3;
		if (rem < 501) return di.ind4;
		if (rem < 601) return di.ind5;
		if (rem < 701) return di.ind6;
		if (rem < 801) return di.ind7;
		if (rem < 901) return di.ind8;
		if (rem < 1001) return di.ind9;
		return null;
	}

	//=========================================================
	public DRIndex<T> indAdd(DRArrayList<T> xdl, DRIndex<T> di, DRIndex<T> fdi){

		int cnt = xdl.count;

		if (xdl.count == 100){
			di.ind = 1;
			di.ind1 = xdl;
			di.indPrev = di;
			di.indNext = di;
			fdi = di;
			//debug1("1st ind diind="+di.ind+" xdlcnt="+xdl.count+" fdi="+fdi.ind);
		}

		//debug("1st ind diind="+di.ind+" xdlcnt="+xdl.count+" fdi="+fdi.ind);
		di = fdi.indPrev;				// get last element
		if (cnt > 1000 && cnt%1000 == 100){
			DRIndex<T> ndi = new DRIndex<T>();
			DRIndex<T> ldi = new DRIndex<T>();
			ldi = fdi.indPrev;
			ndi.ind = (cnt / 1000) + 1;
			ndi.indPrev = ldi;
			ndi.indNext = fdi;
			fdi.indPrev = ndi;
			ldi.indNext = ndi;
			di = ndi;
			//debug1("new ind diind="+di.ind);
		}
		//debug("cnt="+cnt+" diind="+di.ind+" rem="+cnt%1000+" find="+fdi.ind+" dlc="+xdl.count);

		if (cnt%1000 == 100) di.ind1 = xdl;
		if (cnt%1000 == 200) di.ind2 = xdl;
		if (cnt%1000 == 300) di.ind3 = xdl;
		if (cnt%1000 == 400) di.ind4 = xdl;
		if (cnt%1000 == 500) di.ind5 = xdl;
		if (cnt%1000 == 600) di.ind6 = xdl;
		if (cnt%1000 == 700) di.ind7 = xdl;
		if (cnt%1000 == 800) di.ind8 = xdl;
		if (cnt%1000 == 900) di.ind9 = xdl;
		if (cnt%1000 == 0) di.ind10 = xdl;

		return di;
	}

	//=========================================================
	public DRArrayList<T>[] toArraySub(DRArrayList<T> dl,DRArrayList<T> fdl, int size){

		DRArrayList<T> ldl = new DRArrayList<T>();		//last ele

		dl = fdl;
		ldl = dl.prev;
		@SuppressWarnings("unchecked")
		//DRArrayList<T>[] dla = new DRArrayList<T>[size];
		DRArrayList<T>[] dla = (DRArrayList<T>[]) java.lang.reflect.Array.newInstance(fdl.getClass(), size);

		boolean ret1 = false;
		int i = 0;
		while (i < size){
			ret1 = dl.sortKey.matches("^[0-9]+$");
			if (ret1) dl.sortKey = "0000000000".substring(dl.sortKey.length()) + dl.sortKey;
			if (!dl.deleted){
				dl.count = i + 1;		//ensure count is sequential with none missing
			 	dla[i] = dl;
				i++;
			}
			dl = dl.next;
		}
		debug1("ta - size="+size+" dlacnt="+dla.length+" i="+i+" dla0="+dla[0].sortKey+
			" ldla="+dla[dla.length - 1].sortKey);

		return dla;
	}

	//=========================================================
	public DRListTBL<T> loadBTree(DRArrayList<T>[] xx, DRListTBL<T> drl){

		DRBTree bt = drl.bt;
		DRBTree root = drl.root;

		DRCode drcode = new DRCode();

		int pow = 2;
		int j = 0;
		int k = 0;
		int line = 1;
		while (pow < xx.length/2){
			j = xx.length/pow;
			k = j;
			while (k < xx.length){
				if (xx[k] != null){
					//debug("lbt - sk="+xx[k].sortKey);
					bt = drcode.DRaddBTree(xx[k].sortKey,k+1,line,bt,root);
					if (bt.line == 1) root = bt;
					xx[k] = null;
				}
				line++;
				k += j;
			}
			pow = pow * 2;
		}
		for (int i = 0; i < xx.length; i++){
			if (xx[i] != null) bt = drcode.DRaddBTree(xx[i].sortKey,i+1,i,bt,root);
		}
		debug("lbt - xsk="+xx[0].sortKey);

		drl.bt = bt;
		drl.root = root;

		return drl;
	}

	//=========================================================
	public DRListTBL<T> DRsort(DRListTBL<T> drl,int asc){

		DRCode<T> drcode = new DRCode<T>();

		DRArrayList<T> dl = drl.dl;
		DRArrayList<T> fdl = drl.fdl;

		debug("get1st="+fdl.count+" fsk="+fdl.sortKey);

		DRArrayList<T>[] xx = drcode.toArraySub(dl,fdl,drl.size);
		debug("sa - xxl="+xx.length+" xsk="+xx[xx.length - 1].sortKey);

		if (xx[0].sortKey == null) return drl;		//sortkey not set so no point sorting

		Arrays.sort(xx, new Comparator<DRArrayList<T>>() {
			@Override
			public int compare(DRArrayList<T> dl1, DRArrayList<T> dl2) {
				if (isGreater(dl1.sortKey, dl2.sortKey)==1){	// is dl1 > dl2 then true
					return asc;				// if asc = 1 then asc if -1 desc
				}else{
					return asc*-1;
				}
			}
		});

		debug("sa - post sort xxl="+xx.length+" xsk1="+xx[0].sortKey+" xsklast="+xx[xx.length - 10].sortKey);
		drl = drcode.clear(drl);
		drl = drcode.toDRListsub(xx,drl);

		String zz = drcode.DRgetEle(1,drl).dl.sortKey+" last="+drcode.DRgetEle((drl.size - 1),drl).dl.sortKey;
		//debug("zz == "+zz.getClass().getSimpleName());

		Arrays.fill(xx, null );				//clear down the array
		debug("sa - size="+drl.size+" 1st="+zz);

		return drl;
	}

	//===================================================
	static public int isGreater(String x1, String x2){

		int greater = 0;

		int result = x1.compareTo(x2);
		if (result == 0) greater = 0;
		if (result > 0) greater = 1;
		if (result < 0) greater = -1;
		//debug("ig - x1="+x1+" x2="+x2+" gr="+greater);

		return greater;
	}
	//=========================================================
	public DRListTBL<T> toDRListsub(DRArrayList<T>[] xx, DRListTBL<T> drl1){

		DRCode<T> drcode = new DRCode<T>();

		int size = xx.length;

		DRListTBL<T> drl = new DRListTBL<T>();
		drl.size = xx.length;

		if (xx[0].sortKey == null){
			debug("tdl sknull - xxl="+xx.length+" xsk="+xx[0].sortKey);
			for (int i = 0; i < size; i++){
				drl = drcode.DRadd(xx[i].obj,drl);
				if (i == 0) drl.fdl = drl.dl;
			}
		}else{
			debug1("tdl - xxl="+xx.length+" xsk="+xx[0].sortKey);
			for (int i = 0; i < size; i++){
				//debug1("tdl - i = "+i+" "+xx[i].sortKey);
				drl = drcode.DRadd(xx[i].obj,drl);
				//stor the key seperately as the BTree needs to be added out of sort order
				drl.dl.sortKey = xx[i].sortKey;
				if (i == 0){ 
					drl.fdl = drl.dl;
				}
				drl.dl = drl.dl.next;
			}
			// sort key is requred but if stored in order will just by all right or all left
			debug("tdl - xx110 sk="+xx[110].sortKey+" cnt="+xx[110].count);
			drl = drcode.loadBTree(xx,drl);
		}
		debug("tdl - dls="+drl.size+" fsk="+drl.fdl.sortKey);
		return drl;
	}
	//==================================================
	public int DRsize(DRListTBL<T> drl){

		return drl.size;
	}
	//==================================================
	public boolean hasNext(DRListTBL<T> drl){

		DRArrayList<T> dl = drl.dl;

		boolean nextFound = true;
		if (dl == null) return false;
		if (dl.next.count == 1) nextFound = false;
		//debug("hn - cnt="+dl.next.count+" "+dl.count);

		return nextFound;

	}
	//=========================================================
	public DRListTBL<T> clear(DRListTBL<T> drl){

		DRArrayList<T> dl = drl.dl;
		DRArrayList<T> fdl = drl.fdl;
		DRIndex<T> di = drl.di;
		DRIndex<T> fdi = drl.fdi;
		DRBTree bt = drl.bt;
		DRBTree root = drl.root;

		dl = fdl;
		int size = dl.prev.count;		//get last count

		dl = null;
		fdl = null;

		di = null;
		fdl = null;

		if (bt != null){
			bt = root;
			bt = null;
			root = null;			//clears btree down
		}

		drl.dl = dl;
		drl.fdl = dl;
		drl.di = di;
		drl.fdi = fdi;
		drl.bt = bt;
		drl.root = root;

		drl = null;
		DRListTBL<T> drl1 = new DRListTBL<T>();
		drl1.size = 0;

		return drl1;
	}
	//==================================================
	public DRListTBL<T> DRnext(DRListTBL<T> drl){

		DRCode<T> drcode = new DRCode<T>();
		DRArrayList<T> dl = drl.dl;
		DRArrayList<T> fdl = drl.fdl;

		drl.success = 1;

		int lastcnt = fdl.prev.count;			//if last rec cnt is 0 then all deleted
		if (lastcnt == 0) drl.success = -1;
		if (drl.success == -1) return drl;

		if (dl == null) return null;			// arraylist is null, no currency
		int cnt = dl.count;
		dl = dl.next;
		if (dl.count == fdl.count && cnt > 0) drl.success = -1;	//end of list
		if (drl.success == -1) return drl;

		fdl.save = dl;
		fdl.saveIndex = dl.count;

		drl.dl = dl;

		if (dl.deleted) drl = drcode.nextNonDeleted(drl);

		return drl;
	}

	//==================================================
	public DRListTBL<T> DRprev(DRListTBL<T> drl){

		DRCode<T> drcode = new DRCode<T>();
		DRArrayList<T> dl = drl.dl;
		DRArrayList<T> fdl = drl.fdl;

		drl.success = 1;

		if (dl == null) drl.success = -1;			// arraylist is null, no currency
		if (drl.success == -1) return drl;
		int cnt = dl.count;
		dl = dl.prev;
		if (dl.count == fdl.prev.count && cnt > 0) drl.success = -1;	//end of list
		if (drl.success == -1) return drl;

		fdl.save = dl;
		fdl.saveIndex = dl.count;
		drl.dl = dl;

		if (dl.deleted) drl = drcode.prevNonDeleted(drl);

		return drl;
	}
	//==================================================
	public DRListTBL<T> DRgetFirst(DRListTBL<T> drl){

		DRCode<T> drcode = new DRCode<T>();
		DRArrayList<T> dl = drl.dl;
		DRArrayList<T> fdl = drl.fdl;

		drl.success = 1;
		if (fdl.next == null) drl.success = -1;
		if (drl.success == -1) return drl;
		dl = fdl;
		drl.dl = dl;

		if (dl.deleted)	drl = drcode.nextNonDeleted(drl);

		return drl;
	}
	//==================================================
	public DRListTBL<T> DRgetLast(DRListTBL<T> drl){

		DRCode<T> drcode = new DRCode<T>();
		DRArrayList<T> dl = drl.dl;
		DRArrayList<T> fdl = drl.fdl;

		drl.success = 1;
		if (fdl.next == null) drl.success = -1;;
		if (drl.success == -1) return drl;
		dl = fdl.prev;
		drl.dl = dl;

		debug1("gl - lastcnt="+dl.count);
		if (dl.deleted) drl = drcode.prevNonDeleted(drl);

		return drl;
	}

	//==================================================
	public DRListTBL<T> nextNonDeleted(DRListTBL<T> drl){

		if (drl.dl.count == drl.fdl.count && drl.dl.deleted) drl.dl = drl.dl.next; //if 1st deleted get next 1st
		while (drl.dl.deleted && drl.dl.count != drl.fdl.count){
			drl.dl = drl.dl.next;
		}
		if (drl.dl.count == drl.fdl.count) drl.success = -1;
		return drl;
	}
	//==================================================
	public DRListTBL<T> prevNonDeleted(DRListTBL<T> drl){
		
		if (drl.dl.count == drl.fdl.prev.count && drl.dl.deleted) drl.dl = drl.dl.prev; //if last deleted get prev
		while (drl.dl.deleted && drl.dl.count != drl.fdl.prev.count){
			drl.dl = drl.dl.prev;
		}
		if (drl.dl.count == drl.fdl.prev.count) drl.success = -1;
		return drl;
	}
}