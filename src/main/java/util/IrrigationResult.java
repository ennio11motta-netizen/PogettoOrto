package util;



import exception.IrrigationLevel;

public class IrrigationResult {

    private IrrigationLevel level;
    private String advice;

    public IrrigationResult() {
    }

    public IrrigationResult(IrrigationLevel level, String advice) {
        this.level = level;
        this.advice = advice;
    }

    public IrrigationLevel getLevel() {
        return level;
    }

    public void setLevel(IrrigationLevel level) {
        this.level = level;
    }

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }
}