package projects.DRList.Jar;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.lang.reflect.*;
import java.util.*;

import projects.DRList.Jar.DRNoMatchException;

public class DRLocalDateTime {

	private long dateMillisec;
	private String dateStr;
	private LocalDateTime dateLDT;
	private LocalDate dateLocalDate;
	private Date dateDate;
	private String dayOfWeek;
	private int dayNum;
	private int dayDiff;

   	HashMap<String,String> val = new HashMap<String,String>();
   	HashMap<String,Integer> week = new HashMap<String,Integer>();

   	public DRLocalDateTime() {
   		val.put("h","Hours");
   		val.put("m","Minutes");
   		val.put("s","Seconds");
   		val.put("d","Days");
   		val.put("w","Weeks");
   		val.put("M","Months");
   		val.put("y","Years");

		week.put("MON",1);
		week.put("TUE",2);
		week.put("WED",3);
		week.put("THU",4);
		week.put("FRI",5);
		week.put("SAT",6);
		week.put("SUN",7);
   	}
	//========================================================
   	public void debug(String msg){
		//System.out.println(msg);
	}
	//========================================================
   	public void debug1(String msg){
		System.out.println(msg);
	}
	//========================================================
	public long getDateMillisec(){
		return this.dateMillisec;
	}
	public String getDateStr(){
		return this.dateStr;
	}
	public LocalDateTime getDateLDT(){
		return this.dateLDT;
	}
	public LocalDate getDateLocalDate(){
		return this.dateLocalDate;
	}
	public String getDayOfWeek(){
		return this.dayOfWeek;
	}
	public int getDayNum(){
		return this.dayNum;
	}
	public int getDayDiff(){
		return this.dayDiff;
	}
	public LocalDateTime getDateX(){
		return this.dateLDT;
	}
	//=======================================
	public DRLocalDateTime setDateMillisec(long x){
		this.dateMillisec = x;
		return this.MillisecToLocalDate(x);
	}
	public DRLocalDateTime setDateStr(String x){
		this.dateStr = x;
		return this.StringToLDT(x);
	}
	public DRLocalDateTime setLDString(String x){
		this.dateStr = x;
		return this.StringToLocalDate(x);
	}
	public DRLocalDateTime setDateLocalDate(LocalDate x){
		this.dateLocalDate = x;
		this.dateLDT = this.dateLocalDate.atStartOfDay();
		return this;
	}
	public DRLocalDateTime setDateLDT(LocalDateTime x){
		this.dateLDT = x;
		this.dateLocalDate = this.dateLDT.toLocalDate();
		return this;
	}
	public DRLocalDateTime setDayOfWeek(String x){
		this.dayOfWeek = x;
		return this;
	}
	public DRLocalDateTime setDayNum(int x){
		this.dayNum = x;
		return this;
	}
	public DRLocalDateTime setDayDiff(int x){
		this.dayDiff = x;
		return this;
	}
   	public DRLocalDateTime setDateDate(Date x) {
		this.dateDate = x;
		this.dateLDT = x.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		return this;
	}
	//========================================================
	public long toMillisec(){
		ZonedDateTime zdt = this.dateLDT.atZone(ZoneId.systemDefault());
		return zdt.toInstant().toEpochMilli();
	}
	public long toMillisecLD(){
		LocalDateTime ldt = this.dateLocalDate.atStartOfDay();
		ZonedDateTime zdt = ldt.atZone(ZoneId.systemDefault());
		return zdt.toInstant().toEpochMilli();
	}
	public String toStringDate(){
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        	return this.dateLDT.format(formatter);
	}
	public String toStringLocalDate(){
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        	return this.dateLocalDate.format(formatter);
	}
	public String toLDTStr(String x){
		return this.StrLDTToLocalDate(x).toStringDate();
	}
	public Date toDateDate(){
 		return Date.from(this.dateLDT.atZone(ZoneId.systemDefault()).toInstant());

	}
	public LocalDate toLocalDate(){
		return dateLocalDate;
	}
	//========================================================
   	public boolean validateDateTime(String dateStr){

		String dateCheck = dateStr;
		debug("dateCheck = "+dateCheck);
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		if (dateCheck.length() < 11) formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        	try {
        	    	LocalDate localDate = LocalDate.parse(dateCheck, formatter);
	    		int domCheck = Integer.parseInt(dateCheck.substring(0,2));
	    		int domLocal = localDate.getDayOfMonth();
	    		if (domCheck != domLocal) return false;
            		debug("Valid date = "+localDate+" domc="+domCheck+" doml="+domLocal);
			return true;
        	} catch ( DateTimeException ex ) {
            		debug("INValid date = "+dateCheck);
			return false;
        	}
   	}
	//============================================
   	public boolean validFieldType(String fieldType) {
		String validField = "LongDateLocalDateLocalDateTime";
		return (validField.indexOf(fieldType) > -1);
	}
	//============================================
   	public boolean checkTimeUnits(String unit) {
		return (val.containsKey(unit));
	}
	//============================================
   	public boolean checkDayOfWeek(String dow) {
		dow = dow.toUpperCase();
		return (week.containsKey(dow));
	}
	//============================================
   	public DRLocalDateTime getDayOfWeek(LocalDate ld) {
		this.dayOfWeek = ld.getDayOfWeek().toString();
		debug("dow="+this.dayOfWeek+" ld="+ld);
		this.dayNum = week.get(this.dayOfWeek.substring(0,3));
		this.dateLDT = ld.atStartOfDay();
		this.dateLocalDate = ld;
		return this;
	}
	//============================================
   	public DRLocalDateTime getDayOfWeek(LocalDateTime ldt) {
		LocalDate ld = ldt.toLocalDate();
		this.dayOfWeek = ld.getDayOfWeek().toString();
		debug("dow="+this.dayOfWeek+" ld="+ld);
		this.dayNum = week.get(this.dayOfWeek.substring(0,3));
		this.dateLDT = ldt;
		this.dateLocalDate = ld;
		return this;
	}
	//============================================
   	public DRLocalDateTime StringToLocalDate(String dateCheck) {
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        	try {
        	    	LocalDate localDate = LocalDate.parse(dateCheck, formatter);
			this.dateLocalDate = localDate;
			return this;
        	} catch ( DateTimeException ex ) {
			return this;
		}
	}
	//============================================
   	public DRLocalDateTime StringToLDT(String dateCheck) {
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        	try {
        	    	LocalDateTime localDate = LocalDateTime.parse(dateCheck, formatter);
			this.dateLDT = localDate;
			return this;
        	} catch ( DateTimeException ex ) {
			return this;
		}
	}
	//============================================
   	public DRLocalDateTime StrLDTToLocalDate(String dateCheck) {
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
		if (dateCheck.indexOf("T") > -1) dateCheck = dateCheck.replaceAll("T"," ");
        	try {
        	    	LocalDateTime localDate = LocalDateTime.parse(dateCheck, formatter);
			this.dateLDT = localDate;
			this.dateLocalDate = localDate.toLocalDate();
			debug("s2ldt - dc="+dateCheck+" ldt="+this.dateLDT);
			return this;
        	} catch ( DateTimeException ex ) {
			debug1("s2ldtexcep - "+ex.toString());
			return this;
		}
	}
	//============================================
   	public DRLocalDateTime StringToMillisec(String dateCheck) {
		DRLocalDateTime drldt = new DRLocalDateTime();
		drldt = this.StringToLocalDate(dateCheck);
		drldt = this.LocalDateToMillisec(drldt.dateLDT);
		return drldt;
	}
	//============================================
   	public DRLocalDateTime LocalDateToString(LocalDateTime ldt) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        	this.dateStr = ldt.format(formatter);
		this.dateLDT = ldt;
		return this;
	}
	//============================================
   	public DRLocalDateTime LocalDateToString(LocalDate ld) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        	this.dateStr = ld.format(formatter);
		this.dateLDT = ld.atStartOfDay();
		this.dateLocalDate = ld;
		return this;
	}
	//============================================
   	public DRLocalDateTime MillisecToLocalDate(long m) {
		this.dateLDT = LocalDateTime.ofInstant(Instant.ofEpochMilli(m), ZoneId.systemDefault());
		this.dateLocalDate = this.dateLDT.toLocalDate();
		return this;
	}
	//============================================
   	public DRLocalDateTime LocalDateToMillisec(LocalDateTime ldt) {
		ZonedDateTime zdt = ldt.atZone(ZoneId.systemDefault());
		this.dateMillisec = zdt.toInstant().toEpochMilli();
		return this;
	}
	//============================================
   	public DRLocalDateTime LocalDateToMillisec(LocalDate ld) {
		LocalDateTime ldt = ld.atStartOfDay();
		ZonedDateTime zdt = ldt.atZone(ZoneId.systemDefault());
		this.dateMillisec = zdt.toInstant().toEpochMilli();
		return this;
	}
	//============================================
   	public DRLocalDateTime DateToLocalDate(Date date) {
		DRLocalDateTime drldt = new DRLocalDateTime();
		drldt.dateLDT = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        	drldt = drldt.LocalDateToString(drldt.dateLDT);
		drldt = drldt.LocalDateToMillisec(drldt.dateLDT);
		drldt = drldt.getDayOfWeek(drldt.dateLDT);
		return drldt;
	}
	//============================================
   	public DRLocalDateTime getNewDayNum(String dow, int noOfWeeks) {
		String todayDow = getDayOfWeek(LocalDateTime.now()).dayOfWeek;
		int dowNum = week.get(dow.substring(0,3).toUpperCase());
		int tdowNum = week.get(todayDow.substring(0,3));
		this.dayDiff = (noOfWeeks * 7) - (tdowNum - dowNum);
		debug("gndn - tdyn="+tdowNum+" down="+dowNum+" wn="+noOfWeeks+" diff="+this.dayDiff);
		return this;
	}
	//============================================
   	public DRLocalDateTime getNewDate(String dateStr) throws DRNoMatchException {

	      	DRLocalDateTime  jdt = new DRLocalDateTime();
		String dateType = dateStr.substring(dateStr.length()-1);	//format of dayrstr 10w etc
		if (!val.containsKey(dateType)) throw new DRNoMatchException("Invalid date type - "+dateType); 
		String methodName = "add"+val.get(dateType);
		try {
			Class[] cArg = new Class[1]; 
			cArg[0] = String.class;
 			Method m = jdt.getClass().getDeclaredMethod(methodName,cArg);

			Object[] param = {dateStr.substring(0,dateStr.length()-1)};
			LocalDateTime ndt = (LocalDateTime)m.invoke(jdt,param);
			debug("num="+param+" dt="+dateType+" ndt="+ndt);
        		jdt = jdt.LocalDateToString(ndt);
			jdt.dateLDT = ndt;
			jdt.dateLocalDate = ndt.toLocalDate();

			return jdt;
		} catch (IllegalAccessException|NullPointerException|InvocationTargetException|NoSuchMethodException xx){ 
			throw new DRNoMatchException("Error invoking method "+methodName+" "+xx.toString());
		}
   	}

   	//======================================================
   	public LocalDateTime addDays(String numStr){
		int num = Integer.parseInt(numStr);
		LocalDateTime ldt = LocalDateTime.now();
		LocalDate ld = ldt.toLocalDate();
		return ld.plusDays(num).atStartOfDay();
   	}
   	//======================================================
   	public LocalDateTime addWeeks(String numStr){
		int num = Integer.parseInt(numStr);
		LocalDateTime ldt = LocalDateTime.now();
		LocalDate ld = ldt.toLocalDate();
		return ld.plusWeeks(num).atStartOfDay();
   	}
   	//======================================================
   	public LocalDateTime addMonths(String numStr){
		int num = Integer.parseInt(numStr);
		LocalDateTime ldt = LocalDateTime.now();
		LocalDate ld = ldt.toLocalDate();
		return ld.plusMonths(num).atStartOfDay();
   	}
   	//======================================================
   	public LocalDateTime addYears(String numStr){
		int num = Integer.parseInt(numStr);
		LocalDateTime ldt = LocalDateTime.now();
		LocalDate ld = ldt.toLocalDate();
		return ld.plusYears(num).atStartOfDay();
   	}
   	//======================================================
   	public LocalDateTime addHours(String numStr){
		int num = Integer.parseInt(numStr);
		LocalDateTime ldt = LocalDateTime.now();
		return ldt.plusHours(num);
   	}
   	//======================================================
   	public LocalDateTime addMinutes(String numStr){
		int num = Integer.parseInt(numStr);
		LocalDateTime ldt = LocalDateTime.now();
		return ldt.plusMinutes(num);
   	}
   	//======================================================
   	public LocalDateTime addSeconds(String numStr){
		int num = Integer.parseInt(numStr);
		LocalDateTime ldt = LocalDateTime.now();
		return ldt.plusSeconds(num);
   	}
}
