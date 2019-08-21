
package org.nit.dqmserver.message;

import io.vertx.core.json.JsonObject;


/**
 * @author sensordb
 * @date 2018/5/21
 */

public class Request extends JsonObject {
    private static final String PARAMS = "params";
    private static final String VERSION = "version";
    private static final String METHOD = "method";
    private static final String ID = "id";

    public Request(final String request) throws Exception {
        super(request);
        this.getID();
        this.getJsonObject(PARAMS);
        if (this.containsKey(VERSION) && this.containsKey(METHOD)) {
            return;
        }
        throw new Exception(request);
    }

    public Request(final int id, final String method) {
        this.put(VERSION, 1);
        this.setID(id);
        this.setMethod(method);
        this.setParams(new JsonObject());
    }

    public int getID() {
        return this.getInteger(ID);
    }

    public void setID(final int id) {
        this.put(ID, id);
    }

    public String getMethod() {
        return this.getString(METHOD);
    }

    public void setMethod(final String method) {
        this.put(METHOD, method);
    }

    public JsonObject getParams() {
        return this.getJsonObject(PARAMS);
    }

    public void setParams(final JsonObject params) {
        this.put(PARAMS, params);
    }

    public String getVersion() {
        return this.getString(VERSION);
    }

    @Override
    public JsonObject copy() {
        return super.copy();
    }

    public Object getParamWithKey(String key) {

        JsonObject params = this.getJsonObject(PARAMS);
        if (params == null) {
            return null;
        }
        Object param = params.getValue(key);
        return param;

    }

}
