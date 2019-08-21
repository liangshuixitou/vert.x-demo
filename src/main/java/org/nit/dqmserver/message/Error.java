package org.nit.dqmserver.message;

import io.vertx.core.json.JsonObject;

/**
 * @author eda
 * @date 2018/5/21
 */

public class Error extends AbstractResponse {
    private int code;
    private String message;
    private JsonObject error;

    public Error(final int id, final int code, final String message) {

        this.message = message;
        this.code = code;

        JsonObject error = new JsonObject();
        error.put("code", this.code);
        error.put("message", this.message);

        this.error = error;
        this.id = id;
    }

    public Error(final int id, final int code) {

        this.code = code;

        JsonObject error = new JsonObject();
        error.put("code", this.code);
        error.put("message", "");

        this.error = error;
        this.id = id;
    }


    public String getMessage() {
        return this.message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(final int code) {
        this.code = code;
    }

    public JsonObject getError() {
        return this.error;
    }

    public void setError(final JsonObject error) {
        this.error = error;
    }

    @Override
    public JsonObject toJsonObject() {
        final JsonObject r = new JsonObject();
        r.put("version", "1.0");
        r.put("error", this.error);
        r.put("id", this.id);
        return r;
    }

}
