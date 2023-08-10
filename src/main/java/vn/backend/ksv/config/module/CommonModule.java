package vn.backend.ksv.config.module;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import vn.backend.ksv.common.connector.mq.IMqAdapter;
import vn.backend.ksv.common.connector.mq.MqAdapter;

/**
 * Creator: Nguyen Quang Hung
 * Date: 8/9/23
 * Time: 2:19 PM
 */
public class CommonModule extends AbstractModule {

    public static IMqAdapter mqAdapter;

    @Provides
    @Singleton
    public IMqAdapter getMqAdapter() {
        return this.mqAdapter;
    }


    @Override
    protected void configure() {
        //bind any class here
        this.mqAdapter = new MqAdapter();
    }
}
