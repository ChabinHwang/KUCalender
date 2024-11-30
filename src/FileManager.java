import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class FileManager {
    static FileManager Fm;

    public static FileManager getInstance() {
        if (Fm == null) {
            Fm = new FileManager();
        }
        return Fm;
    }

    public boolean isValidIDPW(String ID,String PW) {
        if (ID == null || PW == null) return false;
        int IDLength = ID.length();
        int PWLength = PW.length();

        // ID에 공백이 있는지 확인
        if (ID.contains(" ")||ID.contains("\t")) {
            System.out.println("<오류: ID와 PW는 띄어쓰기를 포함할 수 없습니다>");
            return false;
        }

        // PW에 공백이 있는지 확인
        if (PW.contains(" ")||PW.contains("\t")) {
            System.out.println("<오류: ID와 PW는 띄어쓰기를 포함할 수 없습니다>");
            return false;
        }

        if (IDLength < 1 || IDLength > 15) {
            System.out.println("<오류: ID와 PW는 1자 이상 15자 이하이어야 합니다>");
            return false;
        }

        if (PWLength < 1 || PWLength > 15) {
            System.out.println("<오류: ID와 PW는 1자 이상 15자 이하이어야 합니다>");
            return false;
        }

        return true;
    }

    public boolean isValidTitle(String title) {
        if (title == null) return false;
        if(title.contains("\t")) {
            System.out.println("<오류: 제목은 탭을 포함할 수 없습니다>");
            return false;
        }
        if(title.trim().isEmpty()) {
            System.out.println("<오류: 제목은 공백일 수 없습니다>");
            return false;
        }
        if(title.matches("^\\s.*")) {
            System.out.println("<오류: 제목은 공백으로 시작할 수 없습니다>");
            return false;
        }
        int len = title.length();
        if(!(len <= 20 && len >= 1)) {
            System.out.println("<오류: 1~20자 범위의 문자열을 입력하세요>");
            return false;
        }
        return true;
    }

    public boolean isValidDate(String date) {
        if (date == null) return false;

        String regex = "^\\d{4}([./_-])\\d{2}\\1\\d{2} \\d{4}\\1\\d{2}\\1\\d{2}$"; //공백도 구분자임

        if (!date.matches(regex)) {
            System.out.println("<오류: 날짜는 YYYY.MM.DD YYYY.MM.DD 형식이어야 합니다(구분자는 온점(.), 하이픈(-), 슬래시(/), 언더바(_) 가능)> ");
            return false;
        }

        String[] part0 = date.split(" ");
        
        for(int i = 0; i < part0.length; i++) {
        	if(!isValidDateCheck(part0[i],true))
        		return false;
        }

        //System.out.println("모든 문법/의미규칙 만족");
        if(!isSameOrLater(part0[0],part0[1])){
            System.out.println("<오류: 시작 날짜는 종료 날짜보다 앞서거나 같아야 합니다> ");
            return false;
        }
        return true;
    }

    public static boolean isSameOrLater(String date1, String date2) {
       
        // 문자열을 LocalDate로 변환
        LocalDate parsedDate1 = parsingDate(date1);
        LocalDate parsedDate2 = parsingDate(date2);

        // 같은 날짜이거나 두 번째 날짜가 더 나중인지 확인
        return !parsedDate1.isAfter(parsedDate2);
    }

    public static boolean isLater(String date1, String date2) {

        if(date2.matches("^0000([./_-])00\\100 0000\\100\\100$"))
            return true;

        // 문자열을 LocalDate로 변환
        LocalDate parsedDate1 = parsingDate(date1);
        LocalDate parsedDate2 = parsingDate(date2);

        if(parsedDate1==null||parsedDate2==null)return false;
        
        // 같은 날짜이거나 두 번째 날짜가 더 나중인지 확인
        return parsedDate2.isAfter(parsedDate1);
    }

    public boolean isValidTime(String time,boolean oneDay) {
        if (time == null) return false;

        String regex = "^\\d{2}:\\d{2} \\d{2}:\\d{2}$";

        if (!time.matches(regex)) {
            System.out.println("<오류: 시간은 HH:MM HH:MM 형식이어야 합니다> ");
            return false;
        }

        String[] part = time.split(" ");

        String start = part[0];
        String end = part[1];

        //System.out.println(start +"~" + end);
        if(oneDay){
            if (start.compareTo(end) >= 0) {
                System.out.println("<오류: 일정이 하루일 경우 시작 시간은 종료 시간보다 앞서야 합니다> ");
                return false;
            }
        }


        //시,분의 숫자 범위
        String[] S = start.split(":");
        String[] E = end.split(":");

        int S_HOUR = Integer.parseInt(S[0]);
        int S_MIN = Integer.parseInt(S[1]);
        int E_HOUR = Integer.parseInt(E[0]);
        int E_MIN = Integer.parseInt(E[1]);




        if (S_HOUR < 0 || S_MIN < 0 || E_HOUR < 0 || E_MIN < 0) {
            System.out.println("<오류: 시간 입력이 잘못되었습니다>");
            return false;
        }

        if (S_HOUR > 23 || S_MIN > 59 || E_HOUR > 23 || E_MIN > 59) {
            System.out.println("<오류: 시간 입력이 잘못되었습니다>");
            return false;
        }



        return true;
    }

    public boolean isValidMemo(String memo) {
        if (memo == null) return false;
        if(memo.contains("\t")) {
            System.out.println("<오류: 메모는 탭을 포함할 수 없습니다>");
            return false;
        }
        int len = memo.length();
        if(!(len<= 100)){
            System.out.println("<오류: 메모는 100자 이하의 문자열을 입력해야 합니다>");
            return false;
        }
        return true;
    }

    public boolean isValidAccess(int access) {
        return access >= 1 && access <= 2;
    }



    public boolean isValidBusy(int busy) {
        return busy >= 0 && busy <= 1;
    }

    public boolean isValidCycleType(int cycleType) {
        return cycleType >= -2 && cycleType <= 0;
    }

    public boolean isValidCycleHaltDate(String cycleHaltDate) {
    	if (!cycleHaltDate.matches("^\\d{4}([./_-])\\d{2}\\1\\d{2}$")) {
            System.out.println("<오류: 날짜는 YYYY.MM.DD 형식이어야 합니다(구분자는 온점(.), 하이픈(-), 슬래시(/), 언더바(_) 가능)> ");
            return false;
        }
    	
    	if(!isValidDateCheck(cycleHaltDate,true)) {
    		return false;
    	}
        //여기
        return true;
    }

    //요일(날짜)
    public static String getDayOfWeek(String dateString) {
        
        LocalDate date = parsingDate(dateString);
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return switch (dayOfWeek) {
            case MONDAY -> "월";
            case TUESDAY -> "화";
            case WEDNESDAY -> "수";
            case THURSDAY -> "목";
            case FRIDAY -> "금";
            case SATURDAY -> "토";
            case SUNDAY -> "일";
        }; // 한글 요일로 변환
    
    }

    //일 = 날짜-날짜
    public static int calculateDateDifference(String startDateString, String endDateString) {
    
        LocalDate startDate = parsingDate(startDateString);
        LocalDate endDate = parsingDate(endDateString);
        return (int) java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate);
        
    }
    //날짜 = 날짜 + 일
    public static LocalDate addDaysToDate(String startDateString, int daysToAdd) {
        LocalDate startDate = parsingDate(startDateString);
        return startDate.plusDays(daysToAdd);
        
    }

    //일 뽑기
    public static String getDayOfYear(String date) {
    	String str = parsePattern(date);
    	int day= Integer.parseInt(str.split("/")[1]);
    	if(day>=10)return String.valueOf(day);
    	else return "0"+String.valueOf(day);
    }

    //겹치는거 확인하는 메소드
    public static boolean areSchedulesNonOverlapping(String AstartDate, String AstartTime,String AendDate,String AendTime,String BstartDate, String BstartTime,String BendDate,String BendTime) {
    	
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
    	
    	String pAstartDate = parsePattern(AstartDate);
    	String pAendDate = parsePattern(AendDate);
    	String pBstartDate = parsePattern(BstartDate);
    	String pBendDate = parsePattern(BendDate);
    	
    	LocalDateTime startA = LocalDateTime.parse(pAstartDate +" "+ AstartTime, formatter);
    	LocalDateTime endA = LocalDateTime.parse(pAendDate +" "+ AendTime, formatter);
        LocalDateTime startB = LocalDateTime.parse(pBstartDate +" "+ BstartTime, formatter);
        LocalDateTime endB = LocalDateTime.parse(pBendDate +" "+ BendTime, formatter);

        //System.out.println(AstartDate+" "+AstartTime+"\t"+AendDate+" "+AendTime+"\t"+BstartDate+" "+BstartTime+"\t"+BendDate+" "+BendTime);
        // 두 일정이 겹치지 않거나 동일한 경우(동일한 경우는 안되는듯)
        return endA.isBefore(startB) || endB.isBefore(startA);
    }
    
    public static String parsePattern(String date) {
    	char seperator = date.charAt(4);
    	String[] part;
        if (seperator == '.') {
            part = date.split("\\.");
        } else {
            part = date.split(String.valueOf(seperator));
        }
        return part[0]+"/"+part[1]+"/"+part[2];
    }

    public static boolean checkOverlap(Schedule checkSchedule, ArrayList<Schedule> schedules) {
    	int result=-1;
    	int cycle1 = 0;
    	while(true) {
    		 for (Schedule schedule : schedules) { 
    			 
    			 int cycle2 = 0;
    			 while(true) {
            		 
    				 result=processRecurringSchedule(checkSchedule,cycle1,schedule,cycle2);
    				 if(result==1)return true;
    				 else if(result==2)return false;
    				 else if(result==3)break;
    				 if(schedule.cycleType==0){
    					 break;
    				 }

    				 cycle2++;
            	 }
	            
    		 }

    		 if(checkSchedule.cycleType==0)
    			 break;
             else if(checkSchedule.busy==1){
                 int cycle3 = 0;
                 while(true) {

                     int result2 = processRecurringSchedule(checkSchedule,cycle1,checkSchedule,cycle3);

                     if(cycle1==cycle3){
                         cycle3++;
                         continue;
                     }
                     if(result2==1)  return true;
                     else if(result2==2)return false;
                     else if(result2==3)break;
                     cycle3++;
                 }
             }
             else if(result==-1)return false;//비교할게 없음
    		 cycle1++;
    	}
       
        return false;
    }
    
    public static boolean isValidDateCheck(String date,boolean printFlag) {
    	char seperator = date.charAt(4);
    	String[] part;
        if (seperator == '.') {
            part = date.split("\\.");
        } else {
            part = date.split(String.valueOf(seperator));
        }

        int YEAR = Integer.parseInt(part[0]);
        int MONTH = Integer.parseInt(part[1]);
        int DAY = Integer.parseInt(part[2]);

        //기본적인 년,월,달
        if (YEAR <2024 || YEAR > 2099) {
        	if(printFlag)
        		System.out.println("<오류: 년은 2024~2099 사이 범위여야 합니다> ");
            return false;
        }

        if (MONTH <1 || MONTH > 12) {
        	if(printFlag)System.out.println("<오류: 월은 01~12 사이 범위여야 합니다> ");
            return false;
        }

        if (DAY <1 || DAY > 31) {
        	if(printFlag)System.out.println("<오류: 일은 01~31 사이 범위여야 합니다> ");
            return false;
        }

        //달에따라 다른 일수, 율리우스력
        boolean isJulius = false; //isJulius == true -> 2월은 29일까지
        boolean monthWith31Days = false; //사용되지 않는 변수이지만 가독성(집합의 관점)을 위해 선언
        boolean monthWith30Days = false;
        boolean monthWith28Days = false;
        boolean monthWith29Days = false;

        if (YEAR % 4 == 0) isJulius = true;
        if (MONTH == 1 || MONTH == 3 || MONTH == 5 || MONTH == 7 || MONTH == 8 || MONTH == 10 || MONTH == 12)
            monthWith31Days = true;
        if (MONTH == 4 || MONTH == 6 || MONTH == 9 || MONTH == 11)
            monthWith30Days = true;
        if (MONTH == 2) {
            if (isJulius) monthWith29Days = true;
            else monthWith28Days = true;
        }

        if (monthWith30Days) {
            if (DAY > 30) {
            	if(printFlag)System.out.println("<오류: 4,6,9,11월 은 30일까지만 있습니다> ");
                return false;
            }
        } else if (monthWith28Days) {
            if (DAY > 28) {
            	if(printFlag)System.out.println("<오류: 윤년이 아닌 년도의 2월은 28일까지만 있습니다> ");
                return false;
            }
        } else if (monthWith29Days) {
            if (DAY > 29) {
            	if(printFlag)System.out.println("<오류: 윤년인 년도의 2월은 29일까지만 있습니다> ");
                return false;
            }
        }
        return true;
    }
    
    public static int processRecurringSchedule(Schedule schedule1,int cycle1,Schedule schedule2,int cycle2) {//0:안겹침 1:겹침 2:S1 cycle종료 3:S2 cycle 종료
    	int duration1 = calculateDateDifference(schedule1.date.split(" ")[0],schedule1.date.split(" ")[1]);
    	int duration2 = calculateDateDifference(schedule2.date.split(" ")[0],schedule2.date.split(" ")[1]);
    	
		String s1sd = schedule1.date.split(" ")[0]; 
		String s1ed = schedule1.date.split(" ")[1];
		String s1st = schedule1.time.split(" ")[0];
		String s1et = schedule1.time.split(" ")[1];
		String s2sd = schedule2.date.split(" ")[0];
		String s2ed = schedule2.date.split(" ")[1];
		String s2st = schedule2.time.split(" ")[0];
		String s2et = schedule2.time.split(" ")[1];
		
		if(cycle1==0&&cycle2==0) {
			if(areSchedulesNonOverlapping(s1sd,s1st,s1ed,s1et,s2sd,s2st,s2ed,s2et))
				return 0;
			else
				return 1;
		}
		LocalDate ns1sd = null; 
		LocalDate ns1ed = null;
		LocalDate ns2sd = null;
		LocalDate ns2ed = null;
		
		if(schedule1.cycleType==0) {
			ns1sd = parsingDate(s1sd);
			ns1ed = parsingDate(s1ed);
		}
		if(schedule1.cycleType==-1) {
			ns1sd = getCycleWeek(s1sd,cycle1);
			ns1ed = getCycleWeek(s1ed,cycle1);
		}
		if(schedule1.cycleType==-2) {
			ns1sd = getSameDayAfterMonths(s1sd,cycle1);
			ns1ed = getSameDayAfterMonths(s1ed,cycle1);
		}
		if(schedule2.cycleType==0) {
			ns2sd = parsingDate(s2sd);
			ns2ed = parsingDate(s2ed);
		}
		if(schedule2.cycleType==-1) {
			ns2sd = getCycleWeek(s2sd,cycle2);
			ns2ed = getCycleWeek(s2ed,cycle2);
		}
		if(schedule2.cycleType==-2) {
			ns2sd = getSameDayAfterMonths(s2sd,cycle2);
			ns2ed = getSameDayAfterMonths(s2ed,cycle2);
		}
		
		// 1. 두 날짜 모두 달력에 없을 경우
		if (ns1sd==null && ns1ed==null) {
			//System.out.println("이 회차는 건너뜁니다.");
			return 0;
		}
		
		if (ns2sd==null && ns2ed==null) {
			//System.out.println("이 회차는 건너뜁니다.");
			return 0;
		}
		
		// 2. 시작일만 있는 경우
		if (ns1sd!=null && ns1ed==null) {
			ns1ed = ns1sd.plusDays(duration1);
			
		}
		if (ns2sd!=null && ns2ed==null) {
			ns2ed = ns2sd.plusDays(duration2);
			
		}
		
		// 3. 종료일만 있는 경우
		if (ns1sd==null && ns1ed!=null) {
			ns1sd = ns1ed.minusDays(duration1);
			
		}
		if (ns2sd==null && ns2ed!=null) {
			ns2sd = ns2ed.minusDays(duration2);
			
		}
		
		
		// 4. 두 날짜 모두 달력에 있고 i보다 짧은 경우
		if (ns1sd!=null && ns1ed!=null&&ns1ed.toEpochDay() - ns1sd.toEpochDay()<duration1 ) {
			ns1sd = ns1ed.minusDays(duration1);
			
		}
		if (ns2sd!=null && ns2ed!=null&&ns2ed.toEpochDay() - ns2sd.toEpochDay()<duration2) {
			ns2sd = ns2ed.minusDays(duration2);
			
		}
		
		String Sns1sd = parseDateToString(ns1sd); 
		String Sns1ed = parseDateToString(ns1ed); 
		String Sns2sd = parseDateToString(ns2sd); 
		String Sns2ed = parseDateToString(ns2ed);

		if(!isSameOrLater(Sns1ed,schedule1.cycleHaltDate))
        {
            //System.out.println(schedule1.cycleHaltDate+" "+Sns1ed);
            return 2;
        }
		if(!isSameOrLater(Sns2ed,schedule2.cycleHaltDate))
        {
            return 3;
        }
		
		if(areSchedulesNonOverlapping(Sns1sd,s1st,Sns1ed,s1et,Sns2sd,s2st,Sns2ed,s2et))
			return 0;
		else return 1;
		
	}
    
    public static LocalDate getSameDayAfterMonths(String dateString, int months) {
    	LocalDate date = parsingDate(dateString);
    	LocalDate newDate = date.plusMonths(months);

    	if(date.getDayOfMonth()!=(newDate.getDayOfMonth()))return null;
    	return newDate;
    }
    
    public static LocalDate getCycleWeek(String dateString, int weeks) {
    	LocalDate date = parsingDate(dateString);
    	return date.plusWeeks(weeks);
    }
    
    public static LocalDate parsingDate(String date) {
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    	
    	char seperator = date.charAt(4);
    	String[] part;
        if (seperator == '.') {
            part = date.split("\\.");
        } else {
            part = date.split(String.valueOf(seperator));
        }
        
        String str = String.join("/", part[0], part[1], part[2]);
        
    	try {
            LocalDate localDate = LocalDate.parse(str, formatter);
            return localDate;
        } catch (DateTimeParseException e) {
            System.out.println("잘못된 날짜 형식입니다. YYYY/MM/DD 형식으로 입력하세요.");
            return null; 
        }
    }
    
    public static String parseDateToString(LocalDate date) {
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    	return date.format(formatter);
    }
}