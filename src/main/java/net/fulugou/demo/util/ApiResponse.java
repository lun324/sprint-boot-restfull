package net.fulugou.demo.util;


public class ApiResponse {

    private String code;
    private String message;
    private Object data;

    public ApiResponse(String code, String error) {
        this.code = code;
        this.message = error;
    }

    public ApiResponse(String code, String error, Object data) {
        this.code = code;
        this.message = error;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getData()
    {
        return  data;
    }
}