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

public class DRListTBL<T>
{

	DRArrayList<T> dl = new DRArrayList<T>();
	DRArrayList<T> fdl = new DRArrayList<T>();			//1st element

	DRIndex<T> di = new DRIndex<T>();
	DRIndex<T> fdi = new DRIndex<T>();

	DRBTree bt = new DRBTree();
	DRBTree root = new DRBTree();

	int success;
	int deleteNum;
	int size;
	boolean fromGetKey;
}

