import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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

        String regex = "^\\d{4}([./_-])\\d{2}\\1\\d{2} \\d{4}([./_-])\\d{2}\\1\\d{2}$"; //공백도 구분자임

        if (!date.matches(regex)) {
            System.out.println("<오류: 날짜는 YYYY.MM.DD YYYY.MM.DD 형식이어야 합니다(구분자는 온점(.), 하이픈(-), 슬래시(/), 언더바(_) 가능)> ");
            return false;
        }

        char separator = date.charAt(4);

        String[] part0 = date.split(" ");
        String[] part;
        String[] dates = new String[2];

        for(int i = 0; i < part0.length; i++) {
            if (separator == '.') {
                part = part0[i].split("\\.");
            } else {
                part = part0[i].split(String.valueOf(separator));
            }

            dates[i]=part[0]+"/"+part[1]+"/"+part[2];

            int YEAR = Integer.parseInt(part[0]);
            int MONTH = Integer.parseInt(part[1]);
            int DAY = Integer.parseInt(part[2]);

            //기본적인 년,월,달
            if (YEAR <2024 || YEAR > 2099) {
                System.out.println("<오류: 년은 2024~2099 사이 범위여야 합니다> ");
                return false;
            }

            if (MONTH <1 || MONTH > 12) {
                System.out.println("<오류: 월은 01~12 사이 범위여야 합니다> ");
                return false;
            }

            if (DAY <1 || DAY > 31) {
                System.out.println("<오류: 일은 01~31 사이 범위여야 합니다> ");
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
                    System.out.println("<오류: 4,6,9,11월 은 30일까지만 있습니다> ");
                    return false;
                }
            } else if (monthWith28Days) {
                if (DAY > 28) {
                    System.out.println("<오류: 윤년이 아닌 년도의 2월은 28일까지만 있습니다> ");
                    return false;
                }
            } else if (monthWith29Days) {
                if (DAY > 29) {
                    System.out.println("<오류: 윤년인 년도의 2월은 29일까지만 있습니다> ");
                    return false;
                }
            }
        }

        //System.out.println("모든 문법/의미규칙 만족");
        if(!isSameOrLater(dates[0],dates[1])){
            System.out.println("<오류: 시작 날짜는 종료 날짜보다 앞서거나 같아야 합니다> ");
            return false;
        }
        return true;
    }

    public static boolean isSameOrLater(String date1, String date2) {
        // 날짜 형식 정의
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

        // 문자열을 LocalDate로 변환
        LocalDate parsedDate1 = LocalDate.parse(date1, formatter);
        LocalDate parsedDate2 = LocalDate.parse(date2, formatter);

        // 같은 날짜이거나 두 번째 날짜가 더 나중인지 확인
        return !parsedDate1.isAfter(parsedDate2);
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

}