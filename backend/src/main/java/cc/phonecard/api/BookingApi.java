package cc.phonecard.api;

import cc.phonecard.service.BookingService;
import com.scaffolding.core.annotaions.RouteHandler;
import com.scaffolding.core.annotaions.RouteMapping;
import com.scaffolding.core.annotaions.RouteMethod;
import com.scaffolding.core.base.BaseRestApi;
import com.scaffolding.core.model.JsonResult;
import com.scaffolding.core.util.AsyncServiceUtil;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

@RouteHandler(value = "booking")
public class BookingApi extends BaseRestApi {

  private static final String ID_PARAMETER = "id";
  private static final String PAGE_PARAMETER = "page";
  private static final String LIMIT_PARAMETER = "limit";

  private final BookingService bookingService = AsyncServiceUtil.getAsyncServiceInstance(
    BookingService.class);

  @RouteMapping(value = "/findOrderById/:orderId", method = RouteMethod.GET)
  public Handler<RoutingContext> findOrderById() {
    return ctx -> {
      String orderId = ctx.pathParam("orderId");
      bookingService.findOrderAndItemById(Long.valueOf(orderId), ar -> {
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
