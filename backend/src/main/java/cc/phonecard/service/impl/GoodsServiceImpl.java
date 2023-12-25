package cc.phonecard.service.impl;

import cc.phonecard.service.GoodsService;
import cc.phonecard.support.RedisSupport;
import com.scaffolding.core.base.BaseAsyncService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.redis.client.Response;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GoodsServiceImpl extends BaseAsyncService implements GoodsService {

  protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

  private static final String GOODS_DICT = ":goods:dict";

  @Override
  public void readAll(final String hashKey, final Handler<AsyncResult<JsonObject>> resultHandler) {
    RedisSupport.getRedisConns()
      .compose(
        conn ->
          RedisSupport.getRedisApi()
            .hget(GOODS_DICT, hashKey)
            .compose(resp -> this.convertResp(resp)))
      .onSuccess(resp -> {
        resultHandler.handle(Future.succeededFuture(resp));
      }).onFailure(throwable -> {
        resultHandler.handle(Future.failedFuture(throwable));
      });
  }

  private Future<JsonObject> convertResp(Response resp) {
    if (Objects.nonNull(resp) && Objects.nonNull(resp.toString())) {
      JsonObject jsonObject = new JsonObject(resp.toString());
      return Future.succeededFuture(jsonObject);
    }
    return Future.failedFuture("");
  }
}
