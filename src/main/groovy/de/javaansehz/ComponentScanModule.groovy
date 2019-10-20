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
        for (bA in bindingAnnotations) {
            def with = packageReflections.getTypesAnnotatedWith(bA)
            for (a in with) {
                bind(a)
            }
        }
//        bindingAnnotations.stream()
//                .map {it -> packageReflections.getTypesAnnotatedWith(it)}
//                .flatMap {it.map(Stream.&of)}
//                .forEach {it -> bind(it)}
    }
}
