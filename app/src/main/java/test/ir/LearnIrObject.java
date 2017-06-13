package test.ir;

import java.util.Arrays;

/**
 * Created by 411370845 on 2017/6/13.
 */

public class LearnIrObject {
    private String learnMangleCode;
    private int[] learn;

    public String getLearnMangleCode() {
        return learnMangleCode;
    }

    public void setLearnMangleCode(String learnMangleCode) {
        this.learnMangleCode = learnMangleCode;
    }

    public int[] getLearn() {
        return learn;
    }

    public void setLearn(int[] learn) {
        this.learn = learn;
    }

    @Override
    public String toString() {
        return "LearnIrObject{" +
                "learnMangleCode='" + learnMangleCode + '\'' +
                ", learn=" + Arrays.toString(learn) +
                '}';
    }
}
