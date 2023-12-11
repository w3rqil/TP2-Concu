import java.util.Random;

public class Policy {
    private String policyType;

    public Policy(String policyType) {
        this.policyType = policyType;
    }

    public int fireChoice(int segment) {
        double prob1 = 0.5;
        double prob2 = 0.2;
        double randomNum = Math.random();
        if(this.policyType.equalsIgnoreCase("Equitative")) {
            if(segment == 1) {
                if(randomNum < prob1) {
                    return 0;
                }
                else {
                    return 1;
                }
            }
            else if(segment == 2) {
                if(randomNum < prob1) {
                    return 4;
                }
                else {
                    return 5;
                }
            }
            else if(segment == 3) {
                if(randomNum < prob1) {
                    return 8;
                }
                else {
                    return 9;
                }
            }
        }
        else {
            if(segment == 1) {
                if(randomNum < prob1) {
                    return 0;
                }
                else {
                    return 1;
                }
            }
            else if(segment == 2) {
                if(randomNum < prob1) {
                    return 4;
                }
                else {
                    return 5;
                }
            }
            else if(segment == 3) {
                if(randomNum > prob2) {
                    return 8;
                }
                else {
                    return 9;
                }
            }
        }
        return 5000;
    }
 

}
