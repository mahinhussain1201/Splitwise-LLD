package Split;
import java.util.ArrayList;
import java.util.List;

import User.User;

public class UnequalSplitStrategy implements ISplitStrategy {

    @Override
    public List<Split> validateAndCreateSplits(User paidBy, double totalAmount, List<User> participants,
            List<Double> splitValues) {
            if (participants == null || participants.isEmpty()) {
                throw new IllegalArgumentException("Participant list is empty");
            }
            if (splitValues == null || participants.size() != splitValues.size()) {
                throw new IllegalArgumentException("Mismatch in participants and split");
            }
            if (totalAmount <= 0) {
                throw new IllegalArgumentException("Total amount must be greater than 0");
            }

            double totalSum = 0;
            for (Double splitValue : splitValues) {
                totalSum += splitValue;
            }
            if (Math.abs(totalSum - totalAmount) > 0.00001) {
                throw new IllegalArgumentException("Sum of expenses don't match");
            }

            List<Split> splits = new ArrayList<>();
            for (int i = 0; i < participants.size(); i++) {
                splits.add(new Split(participants.get(i), splitValues.get(i)));
            }
            return splits;
    }
    
}
