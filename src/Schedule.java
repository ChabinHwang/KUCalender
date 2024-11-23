public class Schedule {
    String ID;
    String date;
    String time;
    String title;
    String memo;
    int access;
    int cycleType;
    String cycleHaltDate;
    int busy;

    public Schedule(String ID,String title,String date,String time,int access,int busy,int cycleType,String cycleHaltDate,String memo) {
        this.ID =ID;
        this.date = date;
        this.time = time;
        this.title = title;
        this.access = access;
        this.memo = memo;
        this.cycleType = cycleType;
        this.cycleHaltDate = cycleHaltDate;
        this.busy = busy;
    }

    public void printSchedule(){
        String[] partDate = date.split(" ");
        String[] partTime = time.split(" ");

        if(partDate[0].equals(partDate[1])){
            System.out.print(partDate[0]+" "+partTime[0]+" ~ "+partTime[1]);
        }
        else{
            System.out.print(partDate[0]+" "+partTime[0]+" ~ "+partDate[1]+" "+partTime[1]);
        }
        if(cycleType==0)System.out.print(" ");
        else if(cycleType==-1)System.out.print(" [매주]");
        else if(cycleType==-2)System.out.print(" [매달]");
        if(busy == 1)System.out.print("[바쁨]");
        System.out.println(" - "+title);

    }

    public void printScheduleBusyPriavte(){
        String[] partDate = date.split(" ");
        String[] partTime = time.split(" ");

        if(partDate[0].equals(partDate[1])){
            System.out.print(partDate[0]+" "+partTime[0]+" ~ "+partTime[1]);
        }
        else{
            System.out.print(partDate[0]+" "+partTime[0]+" ~ "+partDate[1]+" "+partTime[1]);
        }
        if(cycleType==0)System.out.print(" ");
        else if(cycleType==-1)System.out.print(" [매주]");
        else if(cycleType==-2)System.out.print(" [매달]");
        if(busy == 1)System.out.print("[바쁨]");
        System.out.println(" - <바쁨>");

    }



    public void printScheduleAddMemo(){
        String[] partDate = date.split(" ");
        String[] partTime = time.split(" ");
        ;
        if(busy==1)System.out.println("[바쁜 일정]");
        switch(cycleType){
            case 0:
                if(partDate[0].equals(partDate[1])){
                    System.out.println(partDate[0]+" "+partTime[0]+" ~ "+partTime[1]+" - "+title);
                }
                else{
                    System.out.println(partDate[0]+" "+partTime[0]+" ~ "+partDate[1]+" "+partTime[1]+" - "+title);
                }
                if(memo==null){
                    System.out.println("메모 : ");
                }
                else
                    System.out.println("메모 : "+memo);
                break;
            case -1:
                System.out.println("매주 "+FileManager.getDayOfWeek(partDate[0])+" "+partTime[0]+" ~ ("+FileManager.calculateDateDifference(partDate[0],partDate[1])+"일 뒤) "+FileManager.getDayOfWeek(partDate[1])+" "+partTime[1]+" - "+title);
                System.out.println("반복 기간 : "+partDate[0]+" ~ "+cycleHaltDate);
                if(memo==null){
                    System.out.println("메모 : ");
                }
                else
                    System.out.println("메모 : "+memo);
                break;
            case -2:
                System.out.println("매달 "+FileManager.getDayOfYear(partDate[0])+"일 "+partTime[0]+" ~ ("+FileManager.calculateDateDifference(partDate[0],partDate[1])+"일 뒤) "+FileManager.getDayOfYear(partDate[1])+"일 "+partTime[1]+" - "+title);
                System.out.println("반복 기간 : "+partDate[0]+" ~ "+cycleHaltDate);
                if(memo==null){
                    System.out.println("메모 : ");
                }
                else
                    System.out.println("메모 : "+memo);
                break;
        }

    }
}