package io.geekidea.springbootplus.test.concurrent;


public class AcctConstant {

    public static final int DEFAULT_GET_LOCK_TIME_SECONDS = 30;
    public static final int DEFAULT_LOCK_TIME_SECONDS = 30;
    public static final int MAX_RELEASE_LOCK_RETRY = 5;
    public enum EnumDebtType{
        DEBT_IN(1,"入款"),
        DEBT_OUT(2,"出款")
        ;

        private int code;
        private String msg;

        EnumDebtType(int code,String msg) {
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getString(){
            return "code = " + code + ",msg= " + msg;
        }
    }

    public enum EnumTransStatus{
        TRANS_STATUS_WAITING(10,"待处理"),
        TRANS_STATUS_PROCESSING(20,"处理中"),
        TRANS_STATUS_FINISHED(30,"处理完成"),
        TRANS_STATUS_CANCELED(40,"取消"),
        TRANS_STATUS_FAILED(50,"失败")
        ;

        private int code;
        private String msg;

        EnumTransStatus(int code,String msg) {
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getString(){
            return "code = " + code + ",msg= " + msg;
        }
    }

    public enum EnumTransResult{
        TRANS_RESULT_SUCCEED(1,"交易成功"),
        TRANS_RESULT_FAILED(2,"交易失败"),
        TRANS_RESULT_UNKNOW(3,"交易未知")
        ;

        private int code;
        private String msg;

        EnumTransResult(int code,String msg) {
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getString(){
            return "code = " + code + ",msg= " + msg;
        }
    }

}
