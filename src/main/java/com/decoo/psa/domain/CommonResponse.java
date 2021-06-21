package com.decoo.psa.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility= JsonAutoDetect.Visibility.ANY,getterVisibility= JsonAutoDetect.Visibility.NONE)
public class CommonResponse {
    private Integer Code;
    private String Msg;
    private Object Data;
    public static CommonResponse ok(Object data) {
        return new CommonResponse(CommonResponseEnum.SUCCESS.getCode(), CommonResponseEnum.SUCCESS.getMsg(), data);
    }

    public static CommonResponse create(Integer code, String msg) {
        return new CommonResponse(code, parseCommonMsg(msg), null);
    }

    public static CommonResponse create(Integer code, String msg, Object data) {
        return new CommonResponse(code, parseCommonMsg(msg), data);
    }

    public static CommonResponse ok() {
        return new CommonResponse(CommonResponseEnum.SUCCESS.getCode(), CommonResponseEnum.SUCCESS.getMsg(), null);
    }

    public static CommonResponse error() {
        return new CommonResponse(CommonResponseEnum.SERVER_ERROR.getCode(), CommonResponseEnum.SERVER_ERROR.getMsg(), null);
    }

    public static CommonResponse notFound() {
        return new CommonResponse(CommonResponseEnum.NOT_FOUND.getCode(), CommonResponseEnum.NOT_FOUND.getMsg(), null);
    }

    public static CommonResponse needAuth() {
        return needAuth(CommonResponseEnum.NEED_AUTH.getMsg(), null);
    }

    public static CommonResponse needAuth(String msg, Object data) {
        return new CommonResponse(CommonResponseEnum.NEED_AUTH.getCode(), parseCommonMsg(msg), data);
    }

    public static CommonResponse badRequest() {
        return new CommonResponse(CommonResponseEnum.BAD_REQUEST.getCode(), CommonResponseEnum.BAD_REQUEST.getMsg(), null);
    }

    public static CommonResponse badRequest(String msg) {
        return new CommonResponse(CommonResponseEnum.BAD_REQUEST.getCode(), parseCommonMsg(msg), null);
    }

    public static CommonResponse serverError() {
        return serverError(null);
    }

    public static CommonResponse serverError(Object data) {
        return new CommonResponse(CommonResponseEnum.SERVER_ERROR.getCode(), CommonResponseEnum.SERVER_ERROR.getMsg(), data);
    }

    private static String parseCommonMsg(String msg) {
        return msg != null && msg.length() > 0 ? msg.substring(0, 1).toUpperCase() + msg.substring(1) : msg;
    }

    public enum CommonResponseEnum {
        SUCCESS(200,"Success"),
        NOT_FOUND(404,"Not found"),
        BAD_REQUEST(400,"Bad request"),
        NEED_AUTH(401,"Need auth"),
        SERVER_ERROR(500,"Server error");

        private Integer code;
        private String msg;

        CommonResponseEnum(Integer code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public Integer getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }
}
