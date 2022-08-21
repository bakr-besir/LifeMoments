package bkr.other;

public class CodeSpeedTester {
    private long time;
    private final StringBuilder result = new StringBuilder("This is Code Speed Tester makes your life better!\n");

    public void start(String name) {
        result.append(name);
        time = System.currentTimeMillis();
    }

    public void stop() {
        long timeRequired = System.currentTimeMillis() - time;
        result.append(" finished in ").append(timeRequired / 1000f).append(" second.\n");
    }

    public String getResults() {
        return result.toString();
    }
}