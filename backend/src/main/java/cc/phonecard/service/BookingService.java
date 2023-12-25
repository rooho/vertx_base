package cc.phonecard.service;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;

@ProxyGen
public interface BookingService {

  void findOrderAndItemById(Long orderId, Handler<AsyncResult<JsonObject>> resultHandler);

  void listOrderPage(Integer current, Handler<AsyncResult<JsonObject>> resultHandler);

  void deleteOrder(Long orderId, Handler<AsyncResult<Void>> resultHandler);

  void saveOrder(JsonObject json, Handler<AsyncResult<JsonObject>> resultHandler);

}
