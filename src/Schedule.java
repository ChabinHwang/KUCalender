public class Schedule {
    String ID;
    String date;
    String time;
    String title;
    String memo;
    int access;

    public Schedule(String ID,String title,String date,String time,int access,String memo) {
        this.ID =ID;
        this.date = date;
        this.time = time;
        this.title = title;
        this.access = access;
        this.memo = memo;
    }

    public void printSchedule(){
        String[] partDate = date.split(" ");
        String[] partTime = time.split(" ");

        if(partDate[0].equals(partDate[1])){
            System.out.println(partDate[0]+" "+partTime[0]+" ~ "+partTime[1]+" - "+title);
        }
        else{
            System.out.println(partDate[0]+" "+partTime[0]+" ~ "+partDate[1]+" "+partTime[1]+" - "+title);
        }
    }

    public void printScheduleAddMemo(){
        String[] partDate = date.split(" ");
        String[] partTime = time.split(" ");

        if(partDate[0].equals(partDate[1])){
            System.out.println(partDate[0]+" "+partTime[0]+" ~ "+partTime[1]+" - "+title);
            if(memo==null){
                System.out.println("메모 : ");
            }
            else
                System.out.println("메모 : "+memo);
        }
        else{
            System.out.println(partDate[0]+" "+partTime[0]+" ~ "+partDate[1]+" "+partTime[1]+" - "+title);
            if(memo==null){
                System.out.println("메모 : ");
            }
            else
                System.out.println("메모 : "+memo);
        }
    }
}