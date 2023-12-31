package com.scaffolding.core.verticle;

import com.scaffolding.core.base.BaseAsyncService;
import com.scaffolding.core.util.ReflectionUtil;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.serviceproxy.ServiceBinder;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务注册到EventBus
 */
public class AsyncRegistryVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncRegistryVerticle.class);

    private String packageAddress;

    public AsyncRegistryVerticle(String packageAddress) {
        Objects.requireNonNull(packageAddress, "given scan package address is empty");
        this.packageAddress = packageAddress;
    }

    @Override
    public void start(Promise<Void> startPromise) {
        Set<Class<? extends BaseAsyncService>> handlers = ReflectionUtil.getReflections(packageAddress).getSubTypesOf(BaseAsyncService.class);
        ServiceBinder binder = new ServiceBinder(vertx);
        if (null != handlers && handlers.size() > 0) {
            handlers.forEach(asyncService -> {
                try {
                    Object asInstance = asyncService.getDeclaredConstructor().newInstance();
                    Method getAddressMethod = asyncService.getMethod("getAddress");
                    String address = (String) getAddressMethod.invoke(asInstance);
                    Method getAsyncInterfaceClassMethod = asyncService.getMethod("getAsyncInterfaceClass");
                    Class clazz = (Class) getAsyncInterfaceClassMethod.invoke(asInstance);
                    binder.setAddress(address).register(clazz, asInstance);
                } catch (Exception e) {
                    LOGGER.error(e.getMessage(), e);
                }
            });
            LOGGER.info("All async services registered");
        }
        startPromise.complete();
    }
}
