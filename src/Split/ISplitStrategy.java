package Split;
import java.util.List;

import User.User;
public interface ISplitStrategy {

    List<Split>validateAndCreateSplits(User paidBy, double totalAmount,List<User>participants,List<Double>splitValues);
} 