package com.scaffolding.core.base;

import com.scaffolding.core.model.JsonResult;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.apache.commons.lang3.StringUtils;

public abstract class BaseRestApi {

    protected static void fireJsonResponse(RoutingContext ctx, JsonResult jsonResult) {
        JsonObject jsonObject = JsonObject.mapFrom(jsonResult);
        ctx.response().putHeader("content-type", "application/json; charset=utf-8").setStatusCode(200).end(jsonObject.encode());
    }

    protected static void fireErrorJsonResponse(RoutingContext ctx, String msg) {
        JsonResult jsonResult = new JsonResult().setCode(JsonResult.FAIL_CODE).setMsg(StringUtils.isBlank(msg) ? JsonResult.FAIL_MESSAGE : msg);
        JsonObject jsonObject = JsonObject.mapFrom(jsonResult);
        ctx.response().putHeader("content-type", "application/json; charset=utf-8").setStatusCode(200).end(jsonObject.encode());
    }

    protected static void fireTextResponse(RoutingContext ctx, String text) {
        ctx.response().putHeader("content-type", "text/html; charset=utf-8").end(text);
    }

    protected static void sendError(int statusCode, RoutingContext ctx) {
        ctx.response().setStatusCode(statusCode).end();
    }
}
