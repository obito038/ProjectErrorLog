import java.util.*;
import java.util.stream.Collectors;

class LogEntry {
    long timestamp;
    String logType;
    double severity;

    LogEntry(long timestamp, String logType, double severity) {
        this.timestamp = timestamp;
        this.logType = logType;
        this.severity = severity;
    }
}

public class ErrorLog {

    private List<LogEntry> logEntries = new ArrayList<>();

    public void submitLogEntry(long timestamp, String logType, double severity) {
        logEntries.add(new LogEntry(timestamp, logType, severity));
    }

    public double meanSeverityLogType(String logType) {
        return logEntries.stream()
                .filter(entry -> entry.logType.equals(logType))
                .mapToDouble(entry -> entry.severity)
                .average()
                .orElse(0.0);
    }

    public double meanSeverityBeforeTimestamp(long timestamp) {
        return logEntries.stream()
                .filter(entry -> entry.timestamp < timestamp)
                .mapToDouble(entry -> entry.severity)
                .average()
                .orElse(0.0);
    }

    public double meanSeverityAfterTimestamp(long timestamp) {
        return logEntries.stream()
                .filter(entry -> entry.timestamp > timestamp)
                .mapToDouble(entry -> entry.severity)
                .average()
                .orElse(0.0);
    }

    public double meanSeverityLogTypeBeforeTimestamp(String logType, long timestamp) {
        return logEntries.stream()
                .filter(entry -> entry.logType.equals(logType) && entry.timestamp < timestamp)
                .mapToDouble(entry -> entry.severity)
                .average()
                .orElse(0.0);
    }

    public double meanSeverityLogTypeAfterTimestamp(String logType, long timestamp) {
        return logEntries.stream()
                .filter(entry -> entry.logType.equals(logType) && entry.timestamp > timestamp)
                .mapToDouble(entry -> entry.severity)
                .average()
                .orElse(0.0);
    }

    public static void main(String[] args) {
        ErrorLog logMonitoring = new ErrorLog();
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            String[] parts = input.split(" ");
            String command = parts[0];

            switch (command) {
                case "1":
                    String[] logParts = parts[1].split(";");
                    long timestamp = Long.parseLong(logParts[0]);
                    String logType = logParts[1];
                    double severity = Double.parseDouble(logParts[2]);
                    logMonitoring.submitLogEntry(timestamp, logType, severity);
                    break;

                case "2":
                    String logTypeQuery = parts[1];
                    double meanSeverityLogType = logMonitoring.meanSeverityLogType(logTypeQuery);
                    System.out.printf("Mean: %.6f%n", meanSeverityLogType);
                    break;

                case "3":
                    String timeCondition = parts[1];
                    long timeQuery = Long.parseLong(parts[2]);
                    if (timeCondition.equals("BEFORE")) {
                        double meanSeverityBefore = logMonitoring.meanSeverityBeforeTimestamp(timeQuery);
                        System.out.printf("Mean: %.6f%n", meanSeverityBefore);
                    } else if (timeCondition.equals("AFTER")) {
                        double meanSeverityAfter = logMonitoring.meanSeverityAfterTimestamp(timeQuery);
                        System.out.printf("Mean: %.6f%n", meanSeverityAfter);
                    }
                    break;

                case "4":
                    String timeTypeCondition = parts[1];
                    String logTypeTimeQuery = parts[2];
                    long timestampTimeQuery = Long.parseLong(parts[3]);
                    if (timeTypeCondition.equals("BEFORE")) {
                        double meanSeverityLogTypeBefore = logMonitoring.meanSeverityLogTypeBeforeTimestamp(logTypeTimeQuery, timestampTimeQuery);
                        System.out.printf("Mean: %.6f%n", meanSeverityLogTypeBefore);
                    } else if (timeTypeCondition.equals("AFTER")) {
                        double meanSeverityLogTypeAfter = logMonitoring.meanSeverityLogTypeAfterTimestamp(logTypeTimeQuery, timestampTimeQuery);
                        System.out.printf("Mean: %.6f%n", meanSeverityLogTypeAfter);
                    }
                    break;

                default:
                    System.out.println("Invalid command");
                    break;
            }
        }
        scanner.close();
    }
}
