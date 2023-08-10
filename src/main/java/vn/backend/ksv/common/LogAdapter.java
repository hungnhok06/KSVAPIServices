package vn.backend.ksv.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Creator: Nguyen Quang Hung
 * Date: 8/8/23
 * Time: 3:57 PM
 */
public class LogAdapter {
    public enum LogType {
        END_USER("USER_"), CORE("CORE_"), AUTO("AUTO_");
        private String type;

        LogType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }

    private final Logger logger;
    private static final String _logger_signal = "{}";
    private static final String _line = "%line%";
    private static final String _message = "%message%";
    private static final String _exception = "%exception%";
    private static final String _threadName = "%threadName%";
    private final String pattern_normal = "[id: " + _threadName + "] [line: " + _line + "] - " + _message;
    private final String pattern_exception = "[id: " + _threadName + "] [line: " + _line + "] - Error: " + _exception + " - " + _message;
    private final String pattern_only_exception = "[id: " + _threadName + "] [line: " + _line + "] - Error: " + _exception;
    //    private IMqAdapter mqAdapter = CommonModule.mqAdapter;
    private final String info_queue_name = "ksv_log_info_queue";
    private final String error_queue_name = "ksv_log_error_queue";
    private final String warn_queue_name = "ksv_log_warn_queue";

    public static <T> LogAdapter newInstance(Class<T> instance) {
        return new LogAdapter(instance);
    }

    public String getIdLog() {
        String currentName = Thread.currentThread().getName();
        try {
            return currentName.substring(currentName.length() - 37);
            //37 = generator.length + refix
        } catch (Exception e) {
            return "";
        }
    }

    private <T> LogAdapter(Class<T> instance) {
        logger = LogManager.getLogger(instance);
    }

    public <T extends Throwable> void error(T e) {
        Integer currentLine = Thread.currentThread().getStackTrace()[2].getLineNumber();
        String name = Thread.currentThread().getName();
        String log = pattern_only_exception
                .replace(_line, currentLine.toString())
                .replace(_exception, e.getClass() + " " + e.getMessage())
                .replace(_threadName, name);
        logger.error(log);
//        try {
//            mqAdapter.pushToMQNotLog(new io.vertx.core.json.JsonObject()
//                            .put("function", Thread.currentThread().getStackTrace()[2].getMethodName())
//                            .put("class", Thread.currentThread().getStackTrace()[2].getClassName())
//                            .put("time", DateTimes.getCurrentTimeMillis())
//                            .put("message", log)
//                            .put("line", currentLine.toString())
//                            .put("id", name.substring(name.length() - 37)).encode()
//                    , error_queue_name);
//        } catch (Exception ex) {
//        }
    }

    //
    public void error(String message) {
        message = checkNull(message);
        Integer currentLine = Thread.currentThread().getStackTrace()[2].getLineNumber();
        String name = Thread.currentThread().getName();
        String log = pattern_normal.replace(_line, currentLine.toString()).replace(_message, message).replace(_threadName, name);
        logger.error(log);
//        try {
//            mqAdapter.pushToMQNotLog(new io.vertx.core.json.JsonObject()
//                            .put("function", Thread.currentThread().getStackTrace()[2].getMethodName())
//                            .put("class", Thread.currentThread().getStackTrace()[2].getClassName())
//                            .put("time", DateTimes.getCurrentTimeMillis())
//                            .put("message", message)
//                            .put("line", currentLine.toString())
//                            .put("id", name.substring(name.length() - 37)).encode()
//                    , error_queue_name);
//        } catch (Exception ex) {
//        }
    }

    public <T extends Throwable> void error(T e, String message) {
        message = checkNull(message);
        Integer currentLine = Thread.currentThread().getStackTrace()[2].getLineNumber();
        String name = Thread.currentThread().getName();
        String log = pattern_exception
                .replace(_exception, e.getClass() + " " + e.getMessage())
                .replace(_line, currentLine.toString()).replace(_message, message)
                .replace(_threadName, name);
        logger.error(log);
//        try {
//            mqAdapter.pushToMQNotLog(new io.vertx.core.json.JsonObject()
//                            .put("function", Thread.currentThread().getStackTrace()[2].getMethodName())
//                            .put("class", Thread.currentThread().getStackTrace()[2].getClassName())
//                            .put("time", DateTimes.getCurrentTimeMillis())
//                            .put("message", message)
//                            .put("line", currentLine.toString())
//                            .put("id", name.substring(name.length() - 37)).encode()
//                    , error_queue_name);
//        } catch (Exception ex) {
//        }
    }

    public void error(String message, Object... lstObject) {
        message = checkNull(message);
        Integer currentLine = Thread.currentThread().getStackTrace()[2].getLineNumber();
        String name = Thread.currentThread().getName();
        String log = pattern_normal
                .replace(_threadName, name)
                .replace(_line, currentLine.toString())
                .replace(_message, message);
        logger.error(log, lstObject);
//        try {
//            mqAdapter.pushToMQNotLog(new io.vertx.core.json.JsonObject()
//                            .put("function", Thread.currentThread().getStackTrace()[2].getMethodName())
//                            .put("class", Thread.currentThread().getStackTrace()[2].getClassName())
//                            .put("time", DateTimes.getCurrentTimeMillis())
//                            .put("message", message)
//                            .put("line", currentLine.toString())
//                            .put("id", name.substring(name.length() - 37)).encode()
//                    , error_queue_name);
//        } catch (Exception ex) {
//        }
    }

    public <T extends Throwable> void error(T e, String message, Object... lstObject) {
        message = checkNull(message);
        Integer currentLine = Thread.currentThread().getStackTrace()[2].getLineNumber();
        String name = Thread.currentThread().getName();
        String log = pattern_exception
                .replace(_line, currentLine.toString())
                .replace(_message, message)
                .replace(_exception, e.getClass() + " " + e.getMessage())
                .replace(_threadName, name);
        logger.error(log);
//        try {
//            mqAdapter.pushToMQNotLog(new io.vertx.core.json.JsonObject()
//                            .put("function", Thread.currentThread().getStackTrace()[2].getMethodName())
//                            .put("class", Thread.currentThread().getStackTrace()[2].getClassName())
//                            .put("time", DateTimes.getCurrentTimeMillis())
//                            .put("message", message)
//                            .put("line", currentLine.toString())
//                            .put("id", name.substring(name.length() - 37)).encode()
//                    , error_queue_name);
//        } catch (Exception ex) {
//        }
    }

    public <T extends Throwable> void errorStackTrace(T e, String message, Object... lstObject) {
        message = checkNull(message);
        Integer currentLine = Thread.currentThread().getStackTrace()[2].getLineNumber();
        String name = Thread.currentThread().getName();
        String log = pattern_exception
                .replace(_line, currentLine.toString())
                .replace(_exception, e.getClass() + " " + e.getMessage())
                .replace(_message, message)
                .replace(_threadName, name);
        logger.error(log, lstObject, e);
    }

    public <T extends Throwable> void errorStackTrace(T e, String message) {
        message = checkNull(message);
        Integer currentLine = Thread.currentThread().getStackTrace()[2].getLineNumber();
        String name = Thread.currentThread().getName();
        String log = pattern_exception
                .replace(_line, currentLine.toString()).replace(_message, message)
                .replace(_exception, e.getClass() + " " + e.getMessage())
                .replace(_threadName, name);
        logger.error(log, e);
    }

    public <T extends Throwable> void errorStackTrace(T e) {
        Integer currentLine = Thread.currentThread().getStackTrace()[2].getLineNumber();
        String name = Thread.currentThread().getName();
        String log = pattern_exception
                .replace(_line, currentLine.toString()).replace(_message, "ERROR!")
                .replace(_exception, e.getClass() + " " + e.getMessage())
                .replace(_threadName, name);
        logger.error(log, e);
    }

    //
    public void warn(Object message) {
        message = checkNull(message);
        String name = Thread.currentThread().getName();
        Integer currentLine = Thread.currentThread().getStackTrace()[2].getLineNumber();
        logger.warn(pattern_normal
                .replace(_message, message.toString())
                .replace(_line, currentLine.toString())
                .replace(_threadName, name));
//        try {
//            mqAdapter.pushToMQNotLog(new io.vertx.core.json.JsonObject()
//                            .put("function", Thread.currentThread().getStackTrace()[2].getMethodName())
//                            .put("class", Thread.currentThread().getStackTrace()[2].getClassName())
//                            .put("time", DateTimes.getCurrentTimeMillis())
//                            .put("message", message)
//                            .put("line", currentLine.toString())
//                            .put("id", name.substring(name.length() - 37)).encode()
//                    , warn_queue_name);
//        } catch (Exception e) {
//        }
    }

    public void warn(String message) {
        message = checkNull(message);
        String name = Thread.currentThread().getName();
        Integer currentLine = Thread.currentThread().getStackTrace()[2].getLineNumber();
        logger.warn(pattern_normal
                .replace(_message, message)
                .replace(_line, currentLine.toString())
                .replace(_threadName, name));
//        try {
//            mqAdapter.pushToMQNotLog(new io.vertx.core.json.JsonObject()
//                            .put("function", Thread.currentThread().getStackTrace()[2].getMethodName())
//                            .put("class", Thread.currentThread().getStackTrace()[2].getClassName())
//                            .put("time", DateTimes.getCurrentTimeMillis())
//                            .put("message", message)
//                            .put("line", currentLine.toString())
//                            .put("id", name.substring(name.length() - 37)).encode()
//                    , warn_queue_name);
//        } catch (Exception e) {
//        }
    }

    public void warn(String message, Object... lstObject) {
        message = checkNull(message);
        String name = Thread.currentThread().getName();
        Integer currentLine = Thread.currentThread().getStackTrace()[2].getLineNumber();
        logger.warn(pattern_normal
                .replace(_message, message)
                .replace(_line, currentLine.toString())
                .replace(_threadName, name), lstObject);
//        try {
//            mqAdapter.pushToMQNotLog(new io.vertx.core.json.JsonObject()
//                            .put("function", Thread.currentThread().getStackTrace()[2].getMethodName())
//                            .put("class", Thread.currentThread().getStackTrace()[2].getClassName())
//                            .put("time", DateTimes.getCurrentTimeMillis())
//                            .put("message", message)
//                            .put("line", currentLine.toString())
//                            .put("id", name.substring(name.length() - 37)).encode()
//                    , warn_queue_name);
//        } catch (Exception e) {
//        }
    }

    //
    public void info(Object message) {
        message = checkNull(message);
        String name = Thread.currentThread().getName();
        Integer currentLine = Thread.currentThread().getStackTrace()[2].getLineNumber();
        logger.info(pattern_normal
                .replace(_message, message.toString())
                .replace(_line, currentLine.toString())
                .replace(_threadName, name));
//        try {
//            mqAdapter.pushToMQNotLog(new io.vertx.core.json.JsonObject()
//                            .put("function", Thread.currentThread().getStackTrace()[2].getMethodName())
//                            .put("class", Thread.currentThread().getStackTrace()[2].getClassName())
//                            .put("time", DateTimes.getCurrentTimeMillis())
//                            .put("message", message)
//                            .put("line", currentLine.toString())
//                            .put("id", name.substring(name.length() - 37)).encode()
//                    , info_queue_name);
//        } catch (Exception e) {
//        }
    }

    public void info(String message) {
        message = checkNull(message);
        String name = Thread.currentThread().getName();
        Integer currentLine = Thread.currentThread().getStackTrace()[2].getLineNumber();
        logger.info(pattern_normal
                .replace(_message, message)
                .replace(_line, currentLine.toString())
                .replace(_threadName, name));
//        try {
//            mqAdapter.pushToMQNotLog(new io.vertx.core.json.JsonObject()
//                            .put("function", Thread.currentThread().getStackTrace()[2].getMethodName())
//                            .put("class", Thread.currentThread().getStackTrace()[2].getClassName())
//                            .put("time", DateTimes.getCurrentTimeMillis())
//                            .put("message", message)
//                            .put("line", currentLine.toString())
//                            .put("id", name.substring(name.length() - 37)).encode()
//                    , info_queue_name);
//        } catch (Exception e) {
//        }
    }

    public void info(String message, Object... lstObject) {
        message = checkNull(message);
        String name = Thread.currentThread().getName();
        Integer currentLine = Thread.currentThread().getStackTrace()[2].getLineNumber();
        logger.info(pattern_normal
                .replace(_message, message)
                .replace(_line, currentLine.toString())
                .replace(_threadName, name), lstObject);
//        try {
//            for (Object o : lstObject) {
//                message = message.replace("{}", o.toString());
//            }
//            mqAdapter.pushToMQNotLog(new io.vertx.core.json.JsonObject()
//                            .put("function", Thread.currentThread().getStackTrace()[2].getMethodName())
//                            .put("class", Thread.currentThread().getStackTrace()[2].getClassName())
//                            .put("time", DateTimes.getCurrentTimeMillis())
//                            .put("message", message)
//                            .put("line", currentLine.toString())
//                            .put("id", name.substring(name.length() - 37)).encode()
//                    , info_queue_name);
//        } catch (Exception e) {
//        }
    }

    //
    public void debug(Object message) {
        message = checkNull(message);
        String name = Thread.currentThread().getName();
        Integer currentLine = Thread.currentThread().getStackTrace()[2].getLineNumber();
        logger.debug(pattern_normal
                .replace(_message, message.toString())
                .replace(_line, currentLine.toString())
                .replace(_threadName, name));
    }

    public void debug(String message) {
        message = checkNull(message);
        String name = Thread.currentThread().getName();
        Integer currentLine = Thread.currentThread().getStackTrace()[2].getLineNumber();
        logger.debug(pattern_normal
                .replace(_message, message)
                .replace(_line, currentLine.toString())
                .replace(_threadName, name));
    }

    public void debug(String message, Object... lstObject) {
        message = checkNull(message);
        String name = Thread.currentThread().getName();
        Integer currentLine = Thread.currentThread().getStackTrace()[2].getLineNumber();
        logger.debug(pattern_normal
                .replace(_message, message)
                .replace(_line, currentLine.toString())
                .replace(_threadName, name), lstObject);
    }

    //
    public void trace(Object message) {
        message = checkNull(message);
        String name = Thread.currentThread().getName();
        Integer currentLine = Thread.currentThread().getStackTrace()[2].getLineNumber();
        logger.trace(pattern_normal
                .replace(_message, message.toString())
                .replace(_line, currentLine.toString())
                .replace(_threadName, name));
    }

    public void trace(String message) {
        message = checkNull(message);
        String name = Thread.currentThread().getName();
        Integer currentLine = Thread.currentThread().getStackTrace()[2].getLineNumber();
        logger.trace(pattern_normal
                .replace(_message, message)
                .replace(_line, currentLine.toString())
                .replace(_threadName, name));
    }

    public void trace(String message, Object... lstObject) {
        message = checkNull(message);
        String name = Thread.currentThread().getName();
        Integer currentLine = Thread.currentThread().getStackTrace()[2].getLineNumber();
        logger.trace(pattern_normal
                .replace(_message, message)
                .replace(_line, currentLine.toString())
                .replace(_threadName, name), lstObject);
    }

    private String checkNull(Object param) {
        if (param == null) return "null";
        return param.toString();
    }
}
