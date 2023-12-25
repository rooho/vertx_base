package cc.phonecard.api;

import cc.phonecard.service.GoodsService;
import com.scaffolding.core.annotaions.RouteHandler;
import com.scaffolding.core.annotaions.RouteMapping;
import com.scaffolding.core.annotaions.RouteMethod;
import com.scaffolding.core.base.BaseRestApi;
import com.scaffolding.core.model.JsonResult;
import com.scaffolding.core.util.AsyncServiceUtil;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

@RouteHandler(value = "goods")
public class GoodsApi extends BaseRestApi {

  private final GoodsService goodsService = AsyncServiceUtil.getAsyncServiceInstance(
    GoodsService.class);

  @RouteMapping(value = "/readAll", method = RouteMethod.GET)
  public Handler<RoutingContext> readAll() {
    return ctx -> {
      goodsService.readAll("C901:1", ar -> {
        if (ar.succeeded()) {
          JsonObject product = ar.result();
          fireJsonResponse(ctx, new JsonResult(product));
        } else {
          fireErrorJsonResponse(ctx, ar.cause().getMessage());
        }
      });
    };
  }
}
