import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

public class ScheduleManager {
    ArrayList<Schedule> schedules;
    private static ScheduleManager scheduleManager = new ScheduleManager();;

    private ScheduleManager() {
        schedules = new ArrayList<Schedule>();

    }

    public static ScheduleManager getInstance() {
        if (scheduleManager == null) {
            scheduleManager = new ScheduleManager();
        }
        return scheduleManager;
    }

    public void loadSchedule() {//불러오기
        File file = new File("schedule.txt");

        try {
            // 파일이 없을 경우 새 파일 생성
            if (!file.exists()) {
                System.out.println("schedule.txt 파일이 존재하지 않습니다. 새 파일을 생성합니다.");
                file.createNewFile(); // 빈 파일 생성
                // 필요한 경우 파일에 기본 데이터를 추가할 수 있음
            }

            // 파일 내 Scanner 위치 초기화
            Scanner openFile = new Scanner(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));

            while (openFile.hasNextLine()) {
                String line = openFile.nextLine();
                String[] part = line.split("\t");

                // 데이터를 스케줄에 추가
                if(part.length==4){
                    schedules.add(new Schedule(part[0], part[1], part[2], part[3], ""));
                }else{
                    schedules.add(new Schedule(part[0], part[1], part[2], part[3], part[4]));
                }

            } }catch (Exception e) {
                e.printStackTrace();
                System.out.println("데이터베이스에 문제가 있습니다. 프로그램을 종료합니다.");
                System.exit(0);
            }
        }

        public void storeSchedule() {
            try {
                PrintWriter writeFile = new PrintWriter(new OutputStreamWriter
                        (new FileOutputStream("schedule.txt"), StandardCharsets.UTF_8));

                for(Schedule schedule:schedules) {
                    writeFile.println(schedule.ID + "\t" + schedule.title + "\t" + schedule.date
                            + "\t" + schedule.time + "\t" + schedule.memo);
                }
                writeFile.close();
                schedules.clear();
            } catch (Exception e) {
                System.out.println("데이터베이스에 문제가 있습니다. 프로그램을 종료합니다.");
                System.exit(0);
            }
        }

        private ArrayList<Schedule> getSchedulesOfID(String ID) {
            ArrayList<Schedule> schedulesOfID = new ArrayList<>();
            for(Schedule schedule:schedules) {
                if(schedule.ID.equals(ID)) {
                    schedulesOfID.add(schedule);
                }
            }
            return schedulesOfID;
        }

        public void ScheduleCheck() {
            System.out.println("[일정]");
            int n = 1;
            ArrayList<Schedule> schedulesOfID = getSchedulesOfID(Main.user.getID());
            if(schedulesOfID.isEmpty()) {
                System.out.println("등록된 일정이 없습니다");
            }
            else{
                for(Schedule schedule:schedulesOfID) {
                    System.out.print(n+".");
                    schedule.printScheduleAddMemo();
                    n++;
                }
            }
            System.out.println("--------------------------------------------");
        }

        public void AddSchedule() {
            System.out.println("[일정 등록]");
            String title, date, time, memo;
            while(true){
                System.out.print("일정의 제목을 입력하세요\n>>");
                title = Main.scan.nextLine();
                if(FileManager.getInstance().isValidTitle(title)){
                    break;
                }
                else{
                    System.out.println("<오류: 1~20자 범위의 문자열을 입력하세요>");
                }
            }

            while(true){
                System.out.print("날짜를 입력하세요\n>>");
                date = Main.scan.nextLine();
                if(FileManager.getInstance().isValidDate(date)){
                    break;
                }
                else{
                    System.out.println("<오류: 올바른 형식이 아닙니다>");
                }
            }
            while(true){
                System.out.print("시간을 입력하세요\n>>");
                time = Main.scan.nextLine();
                if(FileManager.getInstance().isValidTime(time)){
                    break;
                }
                else{
                    System.out.println("<오류: 올바른 형식이 아닙니다>");
                }
            }
            while(true){
                System.out.print("메모을 입력하세요\n>>");
                memo = Main.scan.nextLine();
                if(!FileManager.getInstance().isValidMemo(memo)){
                    System.out.println("<오류: 올바른 형식이 아닙니다>");
                }
                else{
                    break;
                }
            }

            //일정 등록
            schedules.add(new Schedule(Main.user.getID(),title,date,time,memo));

            System.out.println("--------------------------------------------");
            System.out.println("일정 등록이 완료되었습니다");
            System.out.println("--------------------------------------------");

        }

        public void UpdateDeleteMenu() {
            System.out.println("[일정 수정 및 삭제]");

            int n = 1;
            int selectSchedule;
            ArrayList<Schedule> schedulesOfID = getSchedulesOfID(Main.user.getID());//사용자의 스케줄 List

            if(schedulesOfID.isEmpty()) {
                System.out.println("등록된 일정이 없습니다");
                System.out.println("--------------------------------------------");
                return;
            }

            for(Schedule schedule:schedulesOfID) {
                System.out.print(n+".");
                schedule.printSchedule();
                n++;
            }
            while(true){
                System.out.print("수정 및 삭제할 일정을 선택해주세요\n>>");

                //입력 예외처리
                try {
                    selectSchedule = Integer.parseInt(Main.scan.nextLine());
                }catch(Exception e) {
                    System.out.println("<오류: 인덱스 범위 내의 정수를 입력하세요>");
                    continue;
                }

                //조건 판별
                if(selectSchedule < 1 || selectSchedule > schedulesOfID.size() ) {
                    System.out.println("<오류: 인덱스 범위 내의 정수를 입력하세요>");
                    continue;
                }

                selectSchedule--;
                break;

            }

            //재출력
            System.out.print("일정: ");
            schedulesOfID.get(selectSchedule).printScheduleAddMemo();

            //삭제 및 수정 메뉴
            while(true) {
                System.out.println("<수정 및 삭제 메뉴>");
                System.out.println("1.수정");
                System.out.println("2.삭제");
                System.out.println("3.돌아가기");
                System.out.println("--------------------------------------------");
                System.out.print("메뉴를 선택해주세요\n>>");

                //입력 예외처리
                int input;
                try {
                    input = Integer.parseInt(Main.scan.nextLine());
                }catch(Exception e) {
                    System.out.println("<오류: 올바른 형식이 아닙니다>");
                    continue;
                }
                if(input < 1 || input > 3) {
                    System.out.println("<오류: 올바른 형식이 아닙니다>");
                    continue;
                }

                switch(input) {
                    case 1->{
                        if(Update(schedulesOfID.get(selectSchedule)))
                            return;
                    }
                    case 2->{
                        Delete(schedulesOfID.get(selectSchedule));
                        return;
                    }
                    case 3->{return;}
                    default->System.out.println("오류");
                }
            }
        }

        public boolean Update(Schedule schedule) {
            while(true) {
                System.out.println("<수정 메뉴>");
                System.out.println("1.제목 수정");
                System.out.println("2.시간 수정");
                System.out.println("3.메모 수정");
                System.out.println("4.돌아가기");
                System.out.println("--------------------------------------------");
                System.out.print("메뉴를 선택해주세요\n>>");

                //입력 예외처리
                int input;
                try {
                    input = Integer.parseInt(Main.scan.nextLine());
                }catch(Exception e) {
                    System.out.println("<오류: 올바른 메뉴를 선택해주세요>");
                    continue;
                }
                if(input < 1 || input > 4) {
                    System.out.println("<오류: 올바른 메뉴를 선택해주세요>");
                    continue;
                }

                switch(input) {
                    case 1->{
                        UpdateTitle(schedule);
                        return true;
                    }
                    case 2->{
                        UpdateTime(schedule);
                        return true;
                    }
                    case 3->{
                        UpdateMemo(schedule);
                        return true;
                    }
                    case 4->{return false;}
                    default->System.out.println("오류");
                }
            }
        }

        private void UpdateTitle(Schedule schedule) {
            System.out.println("[제목 수정]");
            String title;
            while(true) {
                System.out.print("일정의 제목을 입력하세요\n>>");
                title = Main.scan.nextLine();
                if(FileManager.getInstance().isValidTitle(title)){
                    break;
                }
                else{
                    System.out.println("<오류: 1~20자 범위의 문자열을 입력하세요>");
                }
            }
            schedule.title = title;
            System.out.println("--------------------------------------------");
            System.out.println("일정 수정이 완료되었습니다");
            System.out.println("--------------------------------------------");
        }

        private void UpdateTime(Schedule schedule) {
            System.out.println("[시간 수정]");
            String time;
            while(true) {
                System.out.print("일정의 시간을 입력하세요\n>>");
                time = Main.scan.nextLine();
                if(FileManager.getInstance().isValidTime(time)){
                    break;
                }
                else{
                    System.out.println("<오류: 올바른 형식이 아닙니다> ");
                }
            }
            schedule.time = time;
            System.out.println("--------------------------------------------");
            System.out.println("일정 수정이 완료되었습니다");
            System.out.println("--------------------------------------------");
        }

        private void UpdateMemo(Schedule schedule) {
            System.out.println("[메모 수정]");
            String memo;
            while(true) {
                System.out.print("일정의 메모를 입력하세요\n>>");
                memo = Main.scan.nextLine();
                if(FileManager.getInstance().isValidMemo(memo)){
                    break;
                }
                else{
                    System.out.println("<오류: 올바른 형식이 아닙니다> ");
                }
            }
            schedule.memo = memo;
            System.out.println("--------------------------------------------");
            System.out.println("일정 수정이 완료되었습니다");
            System.out.println("--------------------------------------------");
        }

        public void Delete(Schedule schedule) {
            schedules.remove(schedule);

            System.out.println("--------------------------------------------");
            System.out.println("일정 삭제가 완료되었습니다");
            System.out.println("--------------------------------------------");
        }
    }
