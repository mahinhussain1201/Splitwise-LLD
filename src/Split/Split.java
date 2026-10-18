package Split;
import User.User;

public class Split {
    private User user;
    private double amountOwed;

    Split(User user, double amountOwed){
        this.user=user;
        this.amountOwed=amountOwed;
    }

    public User getUser(){
        return user;
    }
    public double getAmountOwed(){
        return amountOwed;
    }
}
