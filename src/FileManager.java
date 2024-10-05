import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileManager {
    static FileManager Fm;


    private FileManager() {
        try{
            File file = new File("schedule.txt");

            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;

                // 파일의 각 줄을 읽고 스케줄 객체로 변환
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split("\t");

                    Schedule schedule = null;

                    //memo가 없으면 parts의 길이 : 4
                    if (parts.length == 4) {
                        schedule = new Schedule(parts[0], parts[1], parts[2], parts[3], null);
                    } else if (parts.length == 5) {
                        schedule = new Schedule(parts[0], parts[1], parts[2], parts[3], parts[4]);
                    }

                    // ScheduleManager의 schedules 리스트에 추가
                    if (schedule != null) {
                        ScheduleManager.getInstance().schedules.add(schedule);
                    }

                }
                reader.close();
            }
        } catch (IOException e){
            System.err.println("데이터베이스에 문제가 있습니다.");
        }
    }

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
        if (ID.contains(" ")) {
            System.out.println("ID는 공백을 포함할 수 없습니다.");
            return false;
        }

        // PW에 공백이 있는지 확인
        if (PW.contains(" ")) {
            System.out.println("PW는 공백을 포함할 수 없습니다.");
            return false;
        }

        if (IDLength < 1 || IDLength > 15) {
            System.out.println("ID는 1자 이상 15자 이하이어야 합니다.");
            return false;
        }

        if (PWLength < 1 || PWLength > 15) {
            System.out.println("PW는 1자 이상 15자 이하이어야 합니다.");
            return false;
        }

        return true;
    }

    public boolean isValidTitle(String title) {
        if (title == null) return false;

        int len = title.length();
        return len <= 20 && len >= 1;
    }

    public boolean isValidDate(String date) {
        if (date == null) return false;

        String regex = "^\\d{4}([./_ -])\\d{2}\\1\\d{2}$"; //공백도 구분자임

        if (!date.matches(regex)) {
            //System.out.println("정규표현식 불만족");
            return false;
        }

        char separator = date.charAt(4);

        String[] part;

        if (separator == '.') {
            part = date.split("\\.");
        } else {
            part = date.split(String.valueOf(separator));
        }

        int YEAR = Integer.parseInt(part[0]);
        int MONTH = Integer.parseInt(part[1]);
        int DAY = Integer.parseInt(part[2]);

        //기본적인 년,월,달
        if (YEAR <2024 || YEAR > 2099) {
            //System.out.println("2024~2099사이의 숫자만 가능합니다.(YEAR)");
            return false;
        }

        if (MONTH <1 || MONTH > 12) {
            //System.out.println("1~12사이의 숫자만 가능합니다.(MONTH)");
            return false;
        }

        if (DAY <1 || DAY > 31) {
            //System.out.println("1~31사이의 숫자만 가능합니다.(DAY)");
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
                //System.out.println("해당 달은 30일까지만 있습니다.");
                return false;
            }
        } else if (monthWith28Days) {
            if (DAY > 28) {
                //System.out.println("해당 달은 28일까지만 있습니다.");
                return false;
            }
        } else if (monthWith29Days) {
            if (DAY > 29) {
                //System.out.println("해당 달은 29일까지만 있습니다.");
                return false;
            }
        }

        //System.out.println("모든 문법/의미규칙 만족");
        return true;
    }

    public boolean isValidTime(String time) {
        if (time == null) return false;

        String regex = "^\\d{2}:\\d{2}[~\\-]\\d{2}:\\d{2}$";

        if (!time.matches(regex)) {
            //System.out.println("HH:MM~HH:MM 형태로 작성 바랍니다.");
            return false;
        }

        String[] part = time.split("[~\\-]");

        String start = part[0];
        String end = part[1];

        System.out.println(start +"~" + end);

        if (start.compareTo(end) >= 0) {
            //System.out.println("시작시각이 끝나는시각보다 앞서야 합니다.");
            return false;
        }

        //시,분의 숫자 범위
        String[] S = start.split(":");
        String[] E = end.split(":");

        int S_HOUR = Integer.parseInt(S[0]);
        int S_MIN = Integer.parseInt(S[1]);
        int E_HOUR = Integer.parseInt(E[0]);
        int E_MIN = Integer.parseInt(E[1]);

        if (S_HOUR < 0 || S_MIN < 0 || E_HOUR < 0 || E_MIN < 0) {
            //System.out.println("시간과 분은 0보다 커야합니다.");
            return false;
        }

        if (S_HOUR > 23 || S_MIN > 59 || E_HOUR > 23 || E_MIN > 59) {
            //System.out.println("시간은 23이하이며 분은 59이하여야 합니다.");
            return false;
        }

        return true;
    }

    public boolean isValidMemo(String memo) {
        if (memo == null) return false;

        int len = memo.length();
        return len<= 100;
    }
}
