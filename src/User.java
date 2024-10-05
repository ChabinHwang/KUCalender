public class User {
    private String ID;
    private String PW;

    public User(String ID,String PW) {
        this.ID = ID;
        this.PW = PW;
    }

    public String getID() {
        return ID;
    }

    public String getPW() {
        return PW;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof User) {
            return ID.equals(((User) o).getID()) && PW.equals(((User) o).getPW());
        }
        return false;
    }
}
