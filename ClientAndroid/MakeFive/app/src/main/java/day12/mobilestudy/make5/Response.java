package day12.mobilestudy.make5;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.List;

public class Response {

    //API Response code
    public static final int STATUS_SUCCESS = 1;
    public static final int STATUS_NO_RESULT = 0;
    public static final int STATUS_FAIL = -1;
    public static final int STATUS_SERVER_ERROR = -2;

    public static final String MESSAGE_SUCCESS = "Success";
    public static final String MESSAGE_NO_RESULT = "No result";
    public static final String MESSAGE_FAIL = "Fail";
    public static final String MESSAGE_SERVER_ERROR = "Server error";


    @Expose
    @SerializedName(value = "status_code")
    private int statusCode;

    @Expose
    @SerializedName(value = "message")
    private String message;

    @Expose
    @SerializedName(value = "data")
    private List<String> data;

    public Response(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public void setResponse(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public void setResponse(int statusCode, String message, String... data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = Arrays.asList(data);
    }

    @Override
    public String toString() {
        return "Response{statusCode=" + statusCode + ", message='" + message + "', data=" + data + "}";
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public List<String> getData() {
        return data;
    }
}
