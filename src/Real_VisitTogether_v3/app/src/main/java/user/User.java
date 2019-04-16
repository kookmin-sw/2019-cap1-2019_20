package user;



public class User implements Comparable<User> {
    public String name;
    public int num;
    public User(String name , int num)
    {
        this.name = name;
        this.num = num;
    }

    public int getNum() {
        return num;
    }

    public String getName() {

       return  name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(User o) {
        if (this.num < o.getNum()) {
            return 1;
        } else if (this.num > o.getNum()) {
            return -1;
        }
        return 0;


    }
}
