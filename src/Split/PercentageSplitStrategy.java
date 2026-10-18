package Split;
import java.util.ArrayList;
import java.util.List;

import User.User;

public class PercentageSplitStrategy implements ISplitStrategy {

    @Override
    public List<Split> validateAndCreateSplits(User paidBy, double totalAmount, List<User> participants,
            List<Double> splitValues) {
            if (participants == null || participants.isEmpty()) {
                throw new IllegalArgumentException("Participant list is empty");
            }
            if (splitValues == null || participants.size() != splitValues.size()) {
                throw new IllegalArgumentException("Mismatch in participants and split array");
            }
            if (totalAmount <= 0) {
                throw new IllegalArgumentException("Total amount must be greater than 0");
            }

            double percentageSum = 0;
            for (Double value : splitValues) {
                percentageSum += value;
            }
            if (Math.abs(percentageSum - 100) > 0.001) {
                throw new IllegalArgumentException("Sum of % should be 100");
            }

            List<Split> splits = new ArrayList<>();
            for (int i = 0; i < participants.size(); i++) { 
                double amountOwed = ((splitValues.get(i) * totalAmount) / 100.0);
                splits.add(new Split(participants.get(i), amountOwed));
            }
            return splits;
        }
    
}
