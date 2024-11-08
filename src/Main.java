import java.util.Scanner;

public class Main {
    static User user; //현재 이용중인 사용자
    static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        startMenu();
    }

    public static void startMenu() {
        LoginSignup Ls = LoginSignup.getInstance();

        while(true) {
            System.out.println("<메뉴>");
            System.out.println("1.로그인");
            System.out.println("2.회원가입");
            System.out.println("3.종료");
            System.out.println("--------------------------------------------");
            System.out.println("메뉴를 선택해주세요");
            System.out.print(">>");

            //입력 예외처리
            int input;
            try {
                input = Integer.parseInt(scan.nextLine());
            }catch(Exception e) {
                System.out.println("<오류: 숫자를 입력해주세요>");
                continue;
            }
            if(input < 1 || input > 3) {
                System.out.println("<오류: 올바른 번호를 입력해주세요>");
                continue;
            }

            switch(input) {
                case 1->Ls.login();
                case 2->Ls.signup();
                case 3->{
                    System.out.println("프로그램을 종료합니다.");
                    Ls.storeUser(); //프로그램 종료 시 계정 저장
                    return;}
                default->System.out.println("오류");
            }
        }
    }

    public static void mainMenu() {
        ScheduleManager Sc = ScheduleManager.getInstance();

        while(true) {
            System.out.println("<메뉴>");
            System.out.println("1.일정 확인");
            System.out.println("2.일정 등록");
            System.out.println("3.로그아웃");
            System.out.println("--------------------------------------------");
            System.out.println("메뉴를 선택해주세요");
            System.out.print(">>");

            //입력 예외처리
            int input;

            try {
                input = Integer.parseInt(scan.nextLine());
            }catch(Exception e) {
                System.out.println("<오류: 숫자를 입력해주세요>");
                continue;
            }
            if(input < 1 || input > 4) {
                System.out.println("<오류: 올바른 메뉴를 선택해주세요>");
                continue;
            }

            switch(input) {
                case 1->Sc.ScheduleCheck();
                case 2->Sc.AddSchedule();
                case 3->{
                    //저장 및 사용자 초기화
                    user = null;
                    Sc.storeSchedule();//로그아웃 시 스케줄 저장
                    System.out.println("* 로그아웃 합니다");
                    return;}
                default->System.out.println("<오류:올바은 형식이 아닙니다>");
            }
        }
    }
}