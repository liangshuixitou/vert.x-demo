
package org.nit.dqmserver.message;

import io.vertx.core.json.JsonObject;


/**
 * @author sensordb
 * @date 2018/5/21
 */

public abstract class AbstractResponse {
    int id;

    public int getId() {
        return this.id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public JsonObject toJsonObject() {
        return null;
    }

    @Override
    public String toString() {
        return this.toJsonObject().toString();
    }

}
