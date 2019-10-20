package de.javaansehz

import com.google.inject.AbstractModule

class AppModule extends AbstractModule {
    void configure() {
        install(new ComponentScanModule("com.trivago.cluecumber", Singleton.class))
    }
}
