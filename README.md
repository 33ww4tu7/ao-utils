# README #

### Upon usage add following to your atlassian-plugin.xml: ###

```
<ao key="...">
    ...
    <entity>com.famberlin.atlas.props.ao.PluginParameter</entity>
    ...
</ao>
```

### Spring Scanner v1.0 configuration ###
```
<plugin>
    <groupId>com.atlassian.plugin</groupId>
    <artifactId>atlassian-spring-scanner-maven-plugin</artifactId>
    ...
    <configuration>
        <scannedDependencies>
            ...
            <dependency>
                <dependency>
                    <groupId>com.famberlin.atlas</groupId>
                    <artifactId>ao-utils</artifactId>
                </dependency>
            </dependency>
        </scannedDependencies>
        <verbose>false</verbose>
    </configuration>
</plugin>
```