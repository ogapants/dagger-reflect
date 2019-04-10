package com.example;

import dagger.Component;
import java.lang.annotation.Retention;
import javax.inject.Scope;
import javax.inject.Singleton;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Singleton
@ComponentScopeCycleWithMultipleAnnotations.Onesie
@Component(dependencies = ComponentScopeCycleWithMultipleAnnotations.RequestComponent.class)
public interface ComponentScopeCycleWithMultipleAnnotations {
  @Request
  @Component(dependencies = SingletonComponent.class)
  interface RequestComponent {
  }

  @Doubleton
  @Onesie
  @Component
  interface SingletonComponent {
  }

  @Scope
  @Retention(RUNTIME)
  @interface Request {}

  @Scope
  @Retention(RUNTIME)
  @interface Onesie {}

  @Scope
  @Retention(RUNTIME)
  @interface Doubleton {}
}
