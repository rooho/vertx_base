package cc.phonecard.support;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.redis.client.Redis;
import io.vertx.redis.client.RedisAPI;
import io.vertx.redis.client.RedisClientType;
import io.vertx.redis.client.RedisConnection;
import io.vertx.redis.client.RedisOptions;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class RedisSupport {

  private static final String HOST_CONFIG = "datasource.host";
  private static final String PORT_CONFIG = "datasource.port";
  private static final String DATABASE_CONFIG = "datasource.database";
  private static final String USERNAME_CONFIG = "datasource.username";
  private static final String DB_URL = "mongo.url";
  private static final String DB_NAME = "mongo.name";

  private final AtomicBoolean CONNECTING = new AtomicBoolean();

  private static final int MAX_RECONNECT_RETRIES = 16;

  private static Redis redis;
  private static Future<RedisConnection> redisConns;
  private static RedisAPI redisApi;

  public RedisSupport() {
  }

  public static void initWithVertx(Vertx vertx) {
    RedisSupport redisSupport = new RedisSupport();
    redisConns = redisSupport.createRedisClient(vertx);
    redisApi = RedisAPI.api(redis);
  }

  public static Future<RedisConnection> getRedisConns() {
    return redisConns;
  }

  public static RedisAPI getRedisApi() {
    return redisApi;
  }

  private Future<RedisConnection> createRedisClient(Vertx vertx) {
    Promise<RedisConnection> promise = Promise.promise();

    // make sure to invalidate old connection if present
    if (Objects.nonNull(redis)) {
      redis.close();
    }

    if (CONNECTING.compareAndSet(false, true)) {

      RedisOptions redisOptions =
          new RedisOptions()
              .setType(RedisClientType.STANDALONE)
              .addConnectionString("redis://localhost:16379/7")
              .setMaxPoolSize(4)
              .setMaxPoolWaiting(16);

      redis = Redis.createClient(vertx, redisOptions);

      redis
          .connect()
          .onSuccess(
              conn -> {
                // make sure the client is reconnected on error
                // eg, the underlying TCP connection is closed but the client side doesn't know it
                // yet
                //     the client tries to use the staled connection to talk to server. An
                // exceptions will be raised
                conn.exceptionHandler(
                    e -> {
                      attemptReconnect(vertx, 0);
                    });

                // make sure the client is reconnected on connection close
                // eg, the underlying TCP connection is closed with normal 4-Way-Handshake
                //     this handler will be notified instantly
                conn.endHandler(
                    placeHolder -> {
                      attemptReconnect(vertx, 0);
                    });

                // allow further processing
                promise.complete(conn);
                CONNECTING.set(false);
              })
          .onFailure(
              t -> {
                System.out.println("Support.connect.onFailure:"+t.getMessage());
                promise.fail(t);
                CONNECTING.set(false);
              });
    } else {
      promise.complete();
    }
    return promise.future();
  }

  private void attemptReconnect(Vertx vertx, int retry) {
    if (retry > MAX_RECONNECT_RETRIES) {
      // we should stop now, as there's nothing we can do.
      CONNECTING.set(false);
    } else {
      // retry with backoff up to 10240 ms
      long backoff = (long) (Math.pow(2, Math.min(retry, 10)) * 10);

      vertx.setTimer(
          backoff,
          timer -> {
            createRedisClient(vertx).onFailure(t -> attemptReconnect(vertx, retry + 1));
          });
    }
  }
}
