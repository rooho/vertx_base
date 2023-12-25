package cc.phonecard.service;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;

@ProxyGen
public interface GoodsService {

  void readAll(final String hashKey, Handler<AsyncResult<JsonObject>> resultHandler);

}
