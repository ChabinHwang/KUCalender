public class Schedule {
    String ID;
    String date;
    String time;
    String title;
    String memo;

    public Schedule(String ID,String title,String date,String time,String memo) {
        this.ID =ID;
        this.date = date;
        this.time = time;
        this.title = title;
        this.memo = memo;
    }

    public void printSchedule(){
        System.out.print(date+" ");
        System.out.print("("+time+")-");
        System.out.println(title);
    }

    public void printScheduleAddMemo(){
        System.out.print(date+" ");
        System.out.print("("+time+")-");
        System.out.println(title);
        System.out.println("메모: "+memo);
    }
}
