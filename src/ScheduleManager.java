import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
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
                if(part.length==5){
                    schedules.add(new Schedule(part[0], part[1], part[2], part[3], Integer.parseInt(part[4]), null));
                }else{
                    schedules.add(new Schedule(part[0], part[1], part[2], part[3], Integer.parseInt(part[4]), part[5]));
                }

            }
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("데이터베이스에 문제가 있습니다. 프로그램을 종료합니다.");
            System.exit(0);
        }
    }

    public void storeSchedule() {
        try {
            PrintWriter writeFile = new PrintWriter(new OutputStreamWriter
                    (new FileOutputStream("schedule.txt",false),StandardCharsets.UTF_8));
            for(Schedule schedule:schedules) {
                if(schedule.memo==null){
                    writeFile.println(schedule.ID + "\t" + schedule.title + "\t" + schedule.date
                            + "\t" + schedule.time + "\t"+ schedule.access + "\t" );
                }
                else{
                    writeFile.println(schedule.ID + "\t" + schedule.title + "\t" + schedule.date
                            + "\t" + schedule.time + "\t"+ schedule.access + "\t" + schedule.memo);
                }
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

    public void ScheduleCheck() { //일정 확인

        Schedule selectSchedule;

        System.out.print("누구의 일정을 보시겠습니까? ID를 입력해 주세요.\n>>");
        String ID = Main.scan.nextLine();
        System.out.println("--------------------------------------------");

        if(ID.equals(Main.user.getID())||ID.trim().isEmpty()) { //사용자 ID
            while((selectSchedule = SelectSchedules(Main.user.getID()))!=null){
                UpdateDeleteMenu(selectSchedule);
            }
        }
        else if(LoginSignup.getInstance().users.stream().filter(a->a.getID().equals(ID)).toList().isEmpty()){ //없는 ID
            System.out.println("ID가 존재하지 않습니다.");
            System.out.println("--------------------------------------------");
        }
        else{
            while(SelectSchedules(ID)!=null){ //다른 사람 ID
                System.out.println("--------------------------------------------");
                System.out.print("Enter 키를 누르시면 일정 목록으로 돌아갑니다.");
                Main.scan.nextLine();
                System.out.println("--------------------------------------------");
            }
            System.out.println("--------------------------------------------");
        }
    }

    public void AddSchedule() { //일정 등록
        System.out.println("[일정 등록]");
        String title, date, time, memo;
        int access;
        boolean oneDay;

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
            System.out.print("시작 날짜와 종료 날짜를 공백으로 구분하여 입력하세요. 예) 2024.10.31 2024.11.01\n>>");
            date = Main.scan.nextLine();
            if(FileManager.getInstance().isValidDate(date)){
                String[] part = date.split(" ");
                oneDay = part[0].equals(part[1]);
                break;
            }
            else{
                System.out.println("<오류: 올바른 형식이 아닙니다>");
            }
        }
        while(true){
            System.out.print("시작 시간과 종료 시간을 공백으로 구분하여 입력하세요. 예) 14:00 15:00\n>>");
            time = Main.scan.nextLine();
            if(FileManager.getInstance().isValidTime(time,oneDay)){
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

        //-----------------------------공개 여부(추가)--------------------------
        while(true){
            System.out.print("공개여부을 입력하세요(1: 공개/2: 비공개)\n>>");

            try {
                access = Integer.parseInt(Main.scan.nextLine());
            }catch(Exception e) {
                System.out.println("<오류: 올바른 형식이 아닙니다>");
                continue;
            }

            if(!FileManager.getInstance().isValidAccess(access)){
                System.out.println("<오류: 올바른 형식이 아닙니다>");
            }
            else{
                break;
            }
        }


        //일정 등록
        schedules.add(new Schedule(Main.user.getID(),title,date,time,access,memo));

        System.out.println("--------------------------------------------");
        System.out.println("일정 등록이 완료되었습니다");
        System.out.println("--------------------------------------------");

    }

    //------------------일정 선택 UpdateDeleteMenu에서 빼옴-------------------
    public Schedule SelectSchedules(String ID){
        int n = 1;
        int selectSchedule;
        ArrayList<Schedule> schedulesOfID = getSchedulesOfID(ID);//사용자의 스케줄 List
        System.out.println("[일정]");

        if(schedulesOfID.isEmpty()) {
            System.out.println("등록된 일정이 없습니다");
            System.out.println("--------------------------------------------");
            return null;
        }

        System.out.println("<공개>");
        List<Schedule> publicSchedules = schedulesOfID.stream().filter(a->a.access==1).toList();
        for(Schedule schedule:publicSchedules) {
            System.out.print(n+".");
            schedule.printSchedule();
            n++;
        }

        List<Schedule> privateSchedules = new ArrayList<>();

        if(Main.user.getID().equals(ID)){
            System.out.println("<비공개>");
            privateSchedules = schedulesOfID.stream().filter(a->a.access==2).toList();
            for(Schedule schedule:privateSchedules) {
                System.out.print(n+".");
                schedule.printSchedule();
                n++;
            }
        }

        System.out.println("--------------------------------------------");

        while(true){

            System.out.print("세부사항을 확인할 일정을 골라주세요. 돌아가려면 0번을 입력해 주세요.\n>>");

            //입력 예외처리
            try {
                selectSchedule = Integer.parseInt(Main.scan.nextLine());
            }catch(Exception e) {

                System.out.println("<오류:올바른 형식이 아닙니다>");
                continue;
            }

            //조건 판별
            if(selectSchedule==0)
                return null;
            else if(((selectSchedule < 1 || selectSchedule > schedulesOfID.size())&& !privateSchedules.isEmpty())||((selectSchedule < 1 || selectSchedule > publicSchedules.size())&&privateSchedules.isEmpty() )) {

                System.out.println("<오류:올바른 형식이 아닙니다>");
                continue;
            }

            selectSchedule--;
            break;
        }

        //재출력
        System.out.println("--------------------------------------------");
        Schedule s = selectSchedule<=publicSchedules.size()-1?publicSchedules.get(selectSchedule):privateSchedules.get(selectSchedule-publicSchedules.size());
        s.printScheduleAddMemo();
        System.out.println("--------------------------------------------");
        return s;
    }
    //--------------------------------------------------------------------

    public void UpdateDeleteMenu(Schedule selectSchedule) {
        if(selectSchedule == null) {
            return ;
        }


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
                System.out.println("<오류:올바른 형식이 아닙니다>");
                continue;
            }
            if(input < 1 || input > 3) {
                System.out.println("<오류:올바른 형식이 아닙니다>");
                continue;
            }

            switch(input) {
                case 1->{
                    if(!Update(selectSchedule))
                        return ;
                }
                case 2->{
                    Delete(selectSchedule);
                    return ;
                }
                case 3->{return ;}
            }
        }
    }

    public boolean Update(Schedule schedule) {

        while(true) {
            System.out.println("<수정 메뉴>");
            System.out.println("1.제목 수정");
            System.out.println("2.날짜 수정");
            System.out.println("3.시간 수정");
            System.out.println("4.메모 수정");
            System.out.println("5.돌아가기");
            System.out.println("--------------------------------------------");
            System.out.print("메뉴를 선택해주세요\n>>");

            //입력 예외처리
            int input;
            try {
                input = Integer.parseInt(Main.scan.nextLine());
            }catch(Exception e) {
                System.out.println("<오류:올바른 형식이 아닙니다.>");
                continue;
            }
            if(input < 1 || input > 5) {
                System.out.println("<오류:올바른 형식이 아닙니다.>");
                continue;
            }

            switch(input) {
                case 1->{
                    UpdateTitle(schedule);
                    schedule.printScheduleAddMemo();
                    System.out.println("--------------------------------------------");
                    return true;
                }
                case 2->{
                    UpdateDate(schedule);
                    schedule.printScheduleAddMemo();
                    System.out.println("--------------------------------------------");
                    return true;
                }
                case 3->{
                    UpdateTime(schedule);
                    schedule.printScheduleAddMemo();
                    System.out.println("--------------------------------------------");
                    return true;
                }
                case 4->{
                    UpdateMemo(schedule);
                    schedule.printScheduleAddMemo();
                    System.out.println("--------------------------------------------");
                    return true;
                }
                case 5->{return false;}
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

    private void UpdateDate(Schedule schedule) {
        System.out.println("[날짜 수정]");
        String date;

        while(true) {
            System.out.print("시작 날짜와 종료 날짜를 공백으로 구분하여 입력하세요. 예) 2024.10.31 2024.11.01\n>>");
            date = Main.scan.nextLine();

            String[] s = date.split(" ");
            if(s[0].equals(s[1]))
            {
                String[] part = schedule.time.split(" ");

                String start = part[0];
                String end = part[1];

                if (start.compareTo(end) >= 0) {
                    System.out.println("<오류: 날짜가 하루로 수정하려면 시작 시간이 종료 시간보다 앞서야 합니다>");
                    continue;
                }
            }

            if(FileManager.getInstance().isValidDate(date)){
                break;
            }
            else{

                System.out.println("<오류:  올바른 형식이 아닙니다>");
            }
        }
        schedule.date = date;
        System.out.println("--------------------------------------------");
        System.out.println("일정 수정이 완료되었습니다");
        System.out.println("--------------------------------------------");
    }

    private void UpdateTime(Schedule schedule) {
        System.out.println("[시간 수정]");
        String time;
        boolean oneDay;
        String[] part = schedule.time.split(" ");
        oneDay = part[0].equals(part[1]);

        while(true) {
            System.out.print("시작 시간과 종료 시간을 공백으로 구분하여 입력하세요. 예) 14:00 16:00\n>>");
            time = Main.scan.nextLine();
            if(FileManager.getInstance().isValidTime(time,oneDay)){
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