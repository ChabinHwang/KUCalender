import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

public class LoginSignup {
    static LoginSignup loginSignup = new LoginSignup();
    ArrayList<User> users;

    private LoginSignup() {
        users = new ArrayList<>();
        loadUser(); //프로그램 실행 시 계정 로드
    }

    public static LoginSignup getInstance() {
        if (loginSignup == null) {
            loginSignup = new LoginSignup();
        }

        return loginSignup;
    }

    private void loadUser() { //불러오기
        try { // 파일 내 Scanner위치 초기화
            Scanner openFile = new Scanner(new InputStreamReader(new FileInputStream
                    ("account.txt"), StandardCharsets.UTF_8));
            while (openFile.hasNextLine()) {
                String line = openFile.nextLine();
                String[] part = line.split("\t");

                users.add(new User(part[0], part[1]));
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("1");
        }
        catch (Exception e) {
            System.out.println("데이터베이스에 문제가 있습니다. 프로그램을 종료합니다.");
            System.exit(0);
        }
    }

    public void storeUser() { //저장
        try {
            PrintWriter writeFile = new PrintWriter(new OutputStreamWriter(new FileOutputStream
                    ("account.txt",false), StandardCharsets.UTF_8));

            for(User user:users) {
                writeFile.println(user.getID()+ "\t" + user.getPW());
            }

            writeFile.close();
            users.clear();
        } catch (Exception e) {
            System.out.println("데이터베이스에 문제가 있습니다. 프로그램을 종료합니다.");
            System.exit(0);
        }
    }

    public void login() {
        String id,pw;
        System.out.println( "[로그인]");

        Loop:while (true) { //저장된 계정 정보 없을 시 데드락
            //입력
            System.out.print("ID:");
            id = Main.scan.nextLine();
            System.out.print("PW:");
            pw = Main.scan.nextLine();


            //조건 검사
            for (User user : users) {
                if (user.equals(new User(id, pw))) {
                    Main.user = user;
                    break Loop;
                }
            }
            //System.out.println(users.get(0).getID()+ "\t" + users.get(0).getPW());
            System.out.println("<오류: 존재하지 않는 계정입니다>");
        }

        //완료 시 로그인
        System.out.println( id + " 계정에 로그인 합니다");
        ScheduleManager.getInstance().loadSchedule();//로그인 시 스케줄 로드
        Main.mainMenu();
    }

    public void signup() {
        String id,pw;

        System.out.println( "[회원 가입]");
        Loop:while (true) {
            ///입력
            System.out.print("ID:");
            id = Main.scan.nextLine();
            System.out.print("PW:");
            pw = Main.scan.nextLine();

            //조건 검사
            if(!FileManager.getInstance().isValidIDPW(id,pw)){
                continue Loop;
            }

            for(User user:users) {
                if(id.equals(user.getID())){
                    System.out.println("<오류: 중복된 ID 입니다>");
                    continue Loop;
                }
            }

            break;
        }

        //완료 시 add
        users.add(new User(id,pw));
        System.out.println("* 회원 가입이 완료 되었습니다. 시작 메뉴로 돌아갑니다");
    }
}