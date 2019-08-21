package org.nit.dqmserver.util;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.nit.dqmserver.constant.GlobalConsts.YZ_HOME;

/**
 * @author sensordb
 * @date 2018/5/21
 */

public class Tools {

    public static void initSystemProperties() {
        if (System.getenv(YZ_HOME) != null) {
            System.setProperty(YZ_HOME, System.getenv(YZ_HOME));
        }
        else {
            System.setProperty(YZ_HOME, ".");
        }
    }

    
    public static String getTrace(final Throwable t) {
        final StringWriter stringWriter = new StringWriter();
        final PrintWriter writer = new PrintWriter(stringWriter);
        t.printStackTrace(writer);
        final StringBuffer buffer = stringWriter.getBuffer();
        return buffer.toString();
    }
    
    public static String getTrace(final String request, final Throwable t) {
        final StringWriter stringWriter = new StringWriter();
        final PrintWriter writer = new PrintWriter(stringWriter);
        if (request != null) {
            writer.append("request:");
            writer.append(request);
            writer.append('\n');
        }
        t.printStackTrace(writer);
        final StringBuffer buffer = stringWriter.getBuffer();
        return buffer.toString();
    }
    
    public static String getTrace(final String request, final String context, final Throwable t) {
        final StringWriter stringWriter = new StringWriter();
        final PrintWriter writer = new PrintWriter(stringWriter);
        if (request != null) {
            writer.append("request:");
            writer.append(request);
            writer.append('\n');
        }
        if (context != null) {
            writer.append("context:");
            writer.append(context);
            writer.append('\n');
        }
        t.printStackTrace(writer);
        final StringBuffer buffer = stringWriter.getBuffer();
        return buffer.toString();
    }

    public static JsonArray jsonObjectToJsonArray(JsonObject arr, String x, String y) {
        JsonArray res = new JsonArray();
        arr.forEach(tmp -> {
            JsonObject onePoint = new JsonObject();
            onePoint.put(x, tmp.getKey());
            onePoint.put(y, tmp.getValue());
            res.add(onePoint);
        });
        return res;
    }

}
