package Expense;

import User.User;

import java.util.List;
import java.util.UUID;

import Split.EqualSplitStrategy;
import Split.ISplitStrategy;
import Split.PercentageSplitStrategy;
import Split.Split;
import Split.UnequalSplitStrategy;

public class Expense {
    private String id;
    private String description;
    private User paidBy;
    private double totalAmount;
    private List<Split>splits;

    private Expense(Builder builder){
        this.id=UUID.randomUUID().toString();
        this.description=builder.description;
        this.paidBy=builder.paidBy;
        this.totalAmount=builder.totalAmount;

        if(builder.expenseType == null){
            throw new IllegalArgumentException("Expense Type cannot be null");
        }
        if(builder.paidBy == null){
            throw new IllegalArgumentException("Payer cannot be null");
        }
        if(builder.participants == null || builder.participants.isEmpty()){
            throw new IllegalArgumentException("Participants list cannot be null or empty");
        }

        ISplitStrategy splitStrategy;
        switch(builder.expenseType){
            case EQUAL:
                splitStrategy=new EqualSplitStrategy();
                break;
            case UNEQUAL:
                splitStrategy=new UnequalSplitStrategy();
                break;
            case PERCENTAGE:
                splitStrategy=new PercentageSplitStrategy();
                break;
            default:
                throw new IllegalArgumentException("Invalid Expense Type");
        }
        this.splits=splitStrategy.validateAndCreateSplits(paidBy, totalAmount, builder.participants, builder.splitValues);
    }

    public String getId(){
        return id;
    }
    public String getDescription(){
        return description;
    }
    public List<Split> getSplits(){
        return splits;
    }
    public double getTotalAmount(){
        return totalAmount;
    }
    public User getPaidBy(){
        return paidBy;
    }

    public static class Builder{
        private String description;
        private User paidBy;
        private double totalAmount;
        private ExpenseType expenseType;
        private List<User>participants;
        private List<Double>splitValues;

        public Builder description(String description){
            this.description=description;
            return this;
        }
        public Builder paidBy(User user){
            this.paidBy=user;
            return this;
        }
        public Builder totalAmount(double totalAmount){
            this.totalAmount=totalAmount;
            return this;
        }
        public Builder expenseType(ExpenseType type){
            this.expenseType=type;
            return this;
        }
        public Builder ExpenseType(ExpenseType type){
            return this.expenseType(type);
        }
        public Builder splitValues(List<Double> splitValues){
            this.splitValues=splitValues;
            return this;
        }
        public Builder participants(List<User>participants){
            this.participants=participants;
            return this;
        }
        public Expense build(){
            return new Expense(this);
        }
    }
}
