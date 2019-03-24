// to compile do from folder above c:\>javac BJSS_TEST\PricingBasket.java
// to compile do from folder above c:\>java BJSS_TEST.PricingBasket SOUP

package DRList;

import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;
import java.sql.Timestamp;
import java.time.*;

public class DRArrayList<T>
{
	String sortKey;
	int count;
	T obj;

	DRArrayList<T> prev;
	DRArrayList<T> next;

	DRArrayList<T> save;
	int saveIndex;

	boolean deleted;

}

