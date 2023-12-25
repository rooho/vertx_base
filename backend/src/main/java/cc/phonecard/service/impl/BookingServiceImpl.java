package cc.phonecard.service.impl;

import cc.phonecard.dao.JooqDaoHolder;
import cc.phonecard.jooq.tables.daos.BooksDao;
import cc.phonecard.jooq.tables.pojos.Books;
import cc.phonecard.service.BookingService;
import com.scaffolding.core.base.BaseAsyncService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Booking service */
public class BookingServiceImpl extends BaseAsyncService implements BookingService {

  protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

  private final BooksDao booksDao = JooqDaoHolder.getDaoInstance(BooksDao.class);

  @Override
  public void findOrderAndItemById(Long orderId, Handler<AsyncResult<JsonObject>> resultHandler) {
    LOGGER.info("do.findOrderAndItemById {}", orderId);
    Future<Books> bookFindBy = booksDao.findOneById(orderId.intValue());
    bookFindBy.onSuccess(data -> {
      JsonObject result = data.toJson();
      resultHandler.handle(Future.succeededFuture(result));
    }).onFailure(throwable -> {
      resultHandler.handle(Future.failedFuture(throwable));
    });
  }

  @Override
  public void listOrderPage(Integer current,
    Handler<AsyncResult<JsonObject>> resultHandler) {

  }

  @Override
  public void deleteOrder(Long orderId,
    Handler<AsyncResult<Void>> resultHandler) {

  }

  @Override
  public void saveOrder(JsonObject json,
    Handler<AsyncResult<JsonObject>> resultHandler) {

  }
}
