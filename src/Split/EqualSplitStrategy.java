package Split;
import java.util.ArrayList;
import java.util.List;

import User.User;

public class EqualSplitStrategy implements ISplitStrategy {

    @Override
    public List<Split> validateAndCreateSplits(User paidBy, double totalAmount, List<User> participants,
            List<Double> splitValues) {
            if (participants == null || participants.isEmpty()) {
                throw new IllegalArgumentException("Participant list is empty");
            }
            if (totalAmount <= 0) {
                throw new IllegalArgumentException("Total amount must be greater than 0");
            }
            List<Split> splits = new ArrayList<>();
            double amountPerUser = totalAmount / participants.size();
            for(User participant:participants){
                splits.add(new Split(participant,amountPerUser));
            }
            return splits;
    }
    
}
