package handler.user;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import org.nit.dqmserver.util.IniUtil;

/**
 * @author: eda
 * @date: 2018/7/5
 * @description:
 */

public class LoginTest {
    public static void main(final String[] args) {
        IniUtil.changeMode(IniUtil.LOCAL_DEBUG_MODE);
//        IniUtil.changeMode(IniUtil.PRODUCT_MODE);

        Vertx vertx = Vertx.vertx();
        WebClient client = WebClient.create(vertx);

        JsonObject params1 = new JsonObject();
        params1.put("username", "eda");
        params1.put("password", "940620");

        JsonObject form = new JsonObject();
        form.put("version", "1.0");
        form.put("method", "user.Login");
        form.put("params", params1);
        form.put("token", "adminToken");
        form.put("id", 1);
        client
                .post(IniUtil.getInstance().getHTTPServerPort(), IniUtil.getInstance().getServerHostName(), "/")
                .sendJsonObject(form, ar -> {
                    if (ar.succeeded()) {

                        HttpResponse<Buffer> response = ar.result();

                        // Decode the body as a json object
                        JsonObject body = response.bodyAsJsonObject();

                        System.out.println(body);
                    } else {
                        System.out.println("Something went wrong " + ar.cause().getMessage());
                    }
                    vertx.close();
                });


    }

}
