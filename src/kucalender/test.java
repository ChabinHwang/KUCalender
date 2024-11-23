package kucalender;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class test {

	public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
while(true) {
        // 날짜 입력
        System.out.print("날짜를 입력하세요 (형식: YYYY-MM-DD): ");
        String inputDate = scanner.nextLine();
        
        // 정수 입력
        System.out.print("추가할 달 수를 입력하세요: ");
        int monthsToAdd = scanner.nextInt();
        scanner.nextLine();
        // 같은 일을 리턴하는 메소드 호출
        LocalDate resultDate = getSameDayAfterMonths(inputDate, monthsToAdd);
        
        // 결과 출력
        System.out.println("결과 날짜: " + resultDate.format(formatter));
}
        //scanner.close();
    }

    public static LocalDate getSameDayAfterMonths(String dateString, int months) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(dateString, formatter);
        
        // 정수 달 뒤의 같은 일을 계산
        LocalDate newDate = date.plusMonths(months);
        
        return newDate;
    }

}
