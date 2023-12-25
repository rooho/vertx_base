package cc.phonecard;

import static com.scaffolding.core.verticle.RouterRegistryVerticle.CONFIG_MAP_KEY;

import cc.phonecard.dao.DaoConfigurationHolder;
import cc.phonecard.dao.JooqDaoHolder;
import cc.phonecard.datasource.DataSourceHolder;
import cc.phonecard.support.RedisSupport;
import cc.phonecard.utils.SignUtil;
import com.scaffolding.core.handlerfactory.RouterHandlerFactory;
import com.scaffolding.core.util.DeployVertxServer;
import com.scaffolding.core.util.VertxHolder;
import io.vertx.config.ConfigRetriever;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.LocalMap;
import io.vertx.core.shareddata.SharedData;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import java.util.Objects;
import org.apache.logging.log4j.util.Strings;

public class AppMain {

  private static String env = "dev";

  private static final Logger LOGGER = LoggerFactory.getLogger(AppMain.class);

  public static void main(String[] args) {
    if (args.length > 0) {
      env = args[0];
    }
    Vertx tempVertx = Vertx.vertx();
    ConfigRetriever retriever = ConfigRetriever.create(tempVertx);
    //获取conf/config.json中的配置
    retriever.getConfig(ar -> {
      tempVertx.close();
      JsonObject result = ar.result();
      LOGGER.info("配置读取成功：" + result.encode());
      //默认读取dev开发环境配置
      JsonObject envConfig = result.getJsonObject(env);
      JsonObject serverConfig = envConfig.getJsonObject("server");
      JsonObject vertxConfig = envConfig.getJsonObject("vertx");
      JsonObject dataSourceConfig = envConfig.getJsonObject("dataSource");
      JsonObject customConfig = envConfig.getJsonObject("custom");
      Vertx vertx = Vertx.vertx(new VertxOptions(vertxConfig));
      VertxHolder.init(vertx);
      //配置保存在共享数据中
      SharedData sharedData = vertx.sharedData();
      LocalMap<String, Object> localMap = sharedData.getLocalMap(CONFIG_MAP_KEY);
      localMap.put("env", env);
      localMap.put("envConfig", envConfig);
      //先初始化再发布Http服务
      vertx.executeBlocking(p -> {
        //顺序不能乱
        try {
          //初始化数据源
          DataSourceHolder.init(dataSourceConfig);
          //初始化jooq dao配置
          DaoConfigurationHolder.init();
          //初始化dao
          JooqDaoHolder.init(customConfig.getString("daoLocations"));

          RedisSupport.initWithVertx(vertx);

          p.complete();
        } catch (Exception e) {
          p.fail(e);
        }
      }).onComplete(ar2 -> {
        if (ar2.succeeded()) {
          Router router = new RouterHandlerFactory(customConfig.getString("routerLocations"),
            serverConfig.getString("contextPath")).createRouter(createSignValidate());

          DeployVertxServer.startDeploy(router, customConfig.getString("handlerLocations"),
            serverConfig.getInteger("port"),
            customConfig.getInteger("asyncServiceInstances"));
        } else {
          LOGGER.error(ar.cause().getMessage(), ar.cause());
        }
      });
    });
  }

  private final static String SECRET = "LQpiL#jiSSvu%s9QZN4k6DbpSF%&P$f#";

  private static Handler<RoutingContext> createSignValidate() {
    return ctx -> {
      HttpServerRequest request = ctx.request();
      String authToken = request.getHeader("auth-sign");
      String ts = request.getHeader("ts");
      if (Strings.isNotBlank(authToken) && Strings.isNotBlank(ts) && authToken.equals(signature(ctx, Long.valueOf(ts)))) {
        ctx.next();
      } else {
        ctx.response()
          .setStatusCode(401)
          .setStatusMessage("UNAUTHORIZED")
          .end("401 UNAUTHORIZED");
      }
    };
  }

  private static String signature(RoutingContext ctx, Long ts) {
    HttpServerRequest request = ctx.request();
    String uri = request.absoluteURI();
    HttpMethod method = request.method();
    String bodyStr = ctx.body().asString();
    try {
      String signStr = SignUtil.generateSignature(SECRET, method.toString(), uri, ts, bodyStr);
      LOGGER.info("signature:" + signStr);
      return signStr;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}
