package org.nit.dqmserver.message;

import io.vertx.core.json.JsonObject;


/**
 * @author eda
 * @date 2018/5/21
 */
public class Success extends AbstractResponse {

    JsonObject result;

    public Success(final int id, final JsonObject result) {
        this.result = new JsonObject();
        this.id = id;
        this.result = result;
    }

    public JsonObject getResult() {
        return this.result;
    }

    public void setResult(final JsonObject result) {
        this.result = result;
    }

    @Override
    public JsonObject toJsonObject() {
        final JsonObject r = new JsonObject();
        r.put("version", "1.0");
        r.put("result", this.result);
        r.put("id", this.id);
        return r;
    }


}
