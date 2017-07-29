# README #

### Upon usage add following to your atlassian-plugin.xml: ###

```
<ao key="...">
    ...
    <entity>fam.atlas.utils.ao.PluginParameter</entity>
    <entity>fam.atlas.utils.ao.PluginParameterExt</entity>
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