package de.javaansehz

import com.google.inject.AbstractModule
import org.reflections.Reflections

import java.lang.annotation.Annotation

class ComponentScanModule extends AbstractModule {
    private final String packageName
    private final Set<Class<? extends Annotation>> bindingAnnotations

    @SafeVarargs
    ComponentScanModule(String packageName, final Class<? extends Annotation>... bindingAnnotations) {
        this.packageName = packageName
        this.bindingAnnotations = new HashSet<>(Arrays.asList(bindingAnnotations))
    }

    @Override
    void configure() {
        Reflections packageReflections = new Reflections(packageName)
        bindingAnnotations.each { bA ->
            packageReflections.getTypesAnnotatedWith(bA).each { type ->
                bind(type)
            }
        }
    }
}
