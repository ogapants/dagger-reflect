package dagger.reflect;

import dagger.Binds;
import dagger.BindsOptionalOf;
import dagger.Provides;
import dagger.multibindings.ElementsIntoSet;
import dagger.multibindings.IntoMap;
import dagger.multibindings.IntoSet;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Optional;
import org.jetbrains.annotations.Nullable;

import static dagger.reflect.DaggerReflect.notImplemented;
import static dagger.reflect.Reflection.findQualifier;
import static dagger.reflect.Reflection.findScope;
import static java.lang.reflect.Modifier.ABSTRACT;
import static java.lang.reflect.Modifier.PRIVATE;
import static java.lang.reflect.Modifier.STATIC;

final class ReflectiveModuleParser {
  static void parse(Class<?> moduleClass, @Nullable Object instance,
      BindingMap.Builder bindingsBuilder) {
    for (Class<?> target : Reflection.getDistinctTypeHierarchy(moduleClass)) {
      for (Method method : target.getDeclaredMethods()) {
        if ((method.getModifiers() & PRIVATE) != 0) {
          throw new IllegalArgumentException("Private module methods are not allowed: " + method);
        }

        Binding binding;
        TypeWrapper wrapper = TypeWrapper.NONE;
        if ((method.getModifiers() & ABSTRACT) != 0) {
          if (method.getAnnotation(Binds.class) != null) {
            binding = new UnlinkedBindsBinding(method);
          } else if (method.getAnnotation(BindsOptionalOf.class) != null) {
            wrapper = TypeWrapper.OPTIONAL;
            binding = new UnlinkedOptionalBinding(method);
          } else {
            continue;
          }
        } else {
          if ((method.getModifiers() & STATIC) == 0 && instance == null) {
            throw new IllegalStateException(moduleClass.getCanonicalName() + " must be set");
          }

          if (method.getAnnotation(Provides.class) != null) {
            binding = new UnlinkedProvidesBinding(instance, method);
          } else {
            continue;
          }
        }

        if (method.getAnnotation(IntoSet.class) != null) {
          wrapper = TypeWrapper.SET;
          throw notImplemented("@IntoSet");
        }
        if (method.getAnnotation(ElementsIntoSet.class) != null) {
          wrapper = TypeWrapper.SET;
          throw notImplemented("@ElementsIntoSet");
        }
        if (method.getAnnotation(IntoMap.class) != null) {
          wrapper = TypeWrapper.MAP;
          throw notImplemented("@IntoMap");
        }

        Annotation[] annotations = method.getAnnotations();
        Type returnType;
        switch (wrapper) {
          case NONE:
            returnType = method.getGenericReturnType();
            break;
          case OPTIONAL:
            Type genericReturnType = method.getGenericReturnType();
            returnType = new TypeUtil.ParameterizedTypeImpl(null, Optional.class,
                genericReturnType);
            break;
          default:
            throw notImplemented(wrapper.toString());
        }
        Key key = Key.of(findQualifier(annotations), returnType);

        Annotation scope = findScope(annotations);
        if (scope != null) {
          // TODO check correct scope.
          throw notImplemented("Scoped bindings");
        }

        bindingsBuilder.add(key, binding);
      }
    }
  }

  enum TypeWrapper {
    NONE, SET, MAP, OPTIONAL
  }
}
