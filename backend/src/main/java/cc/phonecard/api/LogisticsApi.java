package cc.phonecard.api;

import com.scaffolding.core.annotaions.RouteHandler;
import com.scaffolding.core.annotaions.RouteMapping;
import com.scaffolding.core.annotaions.RouteMethod;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

@RouteHandler(value = "goods")
public class LogisticsApi {

  @RouteMapping(value = "/findOrderById/:logisticsId", method = RouteMethod.GET)
  public Handler<RoutingContext> findOrderById() {
    return ctx -> {
      String orderId = ctx.pathParam("logisticsId");
    };
  }
}
