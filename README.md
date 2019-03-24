# DRList
Collection List in Java
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
