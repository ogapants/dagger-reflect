/*
 * Copyright (C) 2018 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dagger.reflect.compiler;

import static com.google.common.truth.Truth.assertAbout;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

import com.google.testing.compile.JavaFileObjects;
import javax.tools.JavaFileObject;
import org.junit.Test;

public final class DaggerReflectCompilerTest {
  private static final String generatedAnnotationImport;

  static {
    String line = "import javax.annotation.processing.Generated;\n";
    try {
      Class.forName("javax.annotation.processing.Generated");
    } catch (ClassNotFoundException ignored) {
      line = "import javax.annotation.Generated;\n";
    }
    generatedAnnotationImport = line;
  }

  @Test
  public void simple() {
    JavaFileObject component =
        JavaFileObjects.forSourceString(
            "example.TestComponent",
            ""
                + "package example;\n"
                + "\n"
                + "import dagger.Component;\n"
                + "\n"
                + "@Component\n"
                + "interface TestComponent {\n"
                + "}\n");

    JavaFileObject expected =
        JavaFileObjects.forSourceString(
            "example.DaggerTestComponent",
            ""
                + "package example;\n"
                + "\n"
                + "import dagger.reflect.DaggerReflect;\n"
                + "import java.lang.AssertionError;\n"
                + generatedAnnotationImport
                + "\n"
                + "@Generated(\n"
                + "    value = \"dagger.reflect.compiler.DaggerReflectCompiler\",\n"
                + "    comments = \"https://github.com/JakeWharton/dagger-reflect\"\n"
                + ")\n"
                + "public final class DaggerTestComponent {\n"
                + "  private DaggerTestComponent() {\n"
                + "    throw new AssertionError();\n"
                + "  }\n"
                + "  public static TestComponent create() {\n"
                + "    return DaggerReflect.create(TestComponent.class);\n"
                + "  }\n"
                + "}\n");

    assertAbout(javaSource())
        .that(component)
        .processedWith(new DaggerReflectCompiler())
        .compilesWithoutError()
        .and()
        .generatesSources(expected);
  }

  @Test
  public void nested() {
    JavaFileObject component =
        JavaFileObjects.forSourceString(
            "example.TestComponent",
            ""
                + "package example;\n"
                + "\n"
                + "import dagger.Component;\n"
                + "\n"
                + "interface One {\n"
                + "  interface Two {\n"
                + "    @Component\n"
                + "    interface TestComponent {\n"
                + "    }\n"
                + "  }\n"
                + "}\n");

    JavaFileObject expected =
        JavaFileObjects.forSourceString(
            "example.DaggerOne_Two_TestComponent",
            ""
                + "package example;\n"
                + "\n"
                + "import dagger.reflect.DaggerReflect;\n"
                + "import java.lang.AssertionError;\n"
                + generatedAnnotationImport
                + "\n"
                + "@Generated(\n"
                + "    value = \"dagger.reflect.compiler.DaggerReflectCompiler\",\n"
                + "    comments = \"https://github.com/JakeWharton/dagger-reflect\"\n"
                + ")\n"
                + "public final class DaggerOne_Two_TestComponent {\n"
                + "  private DaggerOne_Two_TestComponent() {\n"
                + "    throw new AssertionError();\n"
                + "  }\n"
                + "  public static One.Two.TestComponent create() {\n"
                + "    return DaggerReflect.create(One.Two.TestComponent.class);\n"
                + "  }\n"
                + "}\n");

    assertAbout(javaSource())
        .that(component)
        .processedWith(new DaggerReflectCompiler())
        .compilesWithoutError()
        .and()
        .generatesSources(expected);
  }

  @Test
  public void builder() {
    JavaFileObject component =
        JavaFileObjects.forSourceString(
            "example.TestComponent",
            ""
                + "package example;\n"
                + "\n"
                + "import dagger.Component;\n"
                + "\n"
                + "@Component\n"
                + "interface TestComponent {\n"
                + "  @Component.Builder\n"
                + "  interface Builder {\n"
                + "  }\n"
                + "}\n");

    JavaFileObject expected =
        JavaFileObjects.forSourceString(
            "example.DaggerTestComponent",
            ""
                + "package example;\n"
                + "\n"
                + "import dagger.reflect.DaggerReflect;\n"
                + "import java.lang.AssertionError;\n"
                + generatedAnnotationImport
                + "\n"
                + "@Generated(\n"
                + "    value = \"dagger.reflect.compiler.DaggerReflectCompiler\",\n"
                + "    comments = \"https://github.com/JakeWharton/dagger-reflect\"\n"
                + ")\n"
                + "public final class DaggerTestComponent {\n"
                + "  private DaggerTestComponent() {\n"
                + "    throw new AssertionError();\n"
                + "  }\n"
                + "  public static TestComponent create() {\n"
                + "    return DaggerReflect.create(TestComponent.class);\n"
                + "  }\n"
                + "  public static TestComponent.Builder builder() {\n"
                + "    return DaggerReflect.builder(TestComponent.Builder.class);\n"
                + "  }\n"
                + "}\n");

    assertAbout(javaSource())
        .that(component)
        .processedWith(new DaggerReflectCompiler())
        .compilesWithoutError()
        .and()
        .generatesSources(expected);
  }

  @Test
  public void factory() {
    JavaFileObject component =
        JavaFileObjects.forSourceString(
            "example.TestComponent",
            ""
                + "package example;\n"
                + "\n"
                + "import dagger.Component;\n"
                + "\n"
                + "@Component\n"
                + "interface TestComponent {\n"
                + "  @Component.Factory\n"
                + "  interface Factory {\n"
                + "  }\n"
                + "}\n");

    JavaFileObject expected =
        JavaFileObjects.forSourceString(
            "example.DaggerTestComponent",
            ""
                + "package example;\n"
                + "\n"
                + "import dagger.reflect.DaggerReflect;\n"
                + "import java.lang.AssertionError;\n"
                + generatedAnnotationImport
                + "\n"
                + "@Generated(\n"
                + "    value = \"dagger.reflect.compiler.DaggerReflectCompiler\",\n"
                + "    comments = \"https://github.com/JakeWharton/dagger-reflect\"\n"
                + ")\n"
                + "public final class DaggerTestComponent {\n"
                + "  private DaggerTestComponent() {\n"
                + "    throw new AssertionError();\n"
                + "  }\n"
                + "  public static TestComponent create() {\n"
                + "    return DaggerReflect.create(TestComponent.class);\n"
                + "  }\n"
                + "  public static TestComponent.Factory factory() {\n"
                + "    return DaggerReflect.factory(TestComponent.Factory.class);\n"
                + "  }\n"
                + "}\n");

    assertAbout(javaSource())
        .that(component)
        .processedWith(new DaggerReflectCompiler())
        .compilesWithoutError()
        .and()
        .generatesSources(expected);
  }
}
