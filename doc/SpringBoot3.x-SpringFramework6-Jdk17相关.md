# SpringBoot3.x/SpringFramework6.x/JDK17相关注意点

---

## 处理警告Using deprecated '-debug' fallback for parameter name resolution. Compile the affected code with '-parameters' instead or avoid its introspection: 类全限定名

在springboot3.x中使用到了aop时，比如@Cacheable注解中使用了aop和SPEL，就可能会出现这个警告。

```xml
<build>
        <!-- mvn clean source:jar javadoc:jar deploy -DskipTests -->
        <plugins>
            <!-- mvn clean deploy -DskipTests -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>${maven-compiler-plugin.version}</version>
                <!--解决警告Using deprecated '-debug' fallback for parameter name resolution. Compile the affected code with '-parameters' instead or avoid its introspection: 类全限定名-->
                <configuration>
                    <compilerArgs>
                        <arg>-parameters</arg>
                    </compilerArgs>
                </configuration>
            </plugin> 
        </plugins>
</build>
```
