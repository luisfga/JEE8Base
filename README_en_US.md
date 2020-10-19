# JEE8Base

A multi-module pom project who parents the layers of a base JEE8 project with Register, Login, Password Recovery logics.

The Domain layer deal with entities and business logic.
The Presentation layer, showing decoupling from domain layer, is divided between the mutually exclusive options for presentation: JSF, Struts and Angular(#TODO).
Apache Shiro is also shipped with presentation.

It's a first commit. There are somethings to improve, but its working nicely.
It was developed with TomEE 8.0.3 in a jdk 1.8.
It was tested successfully on WildFly20, with only some especific changes like jndi paths and scope change of some dependencies, like openjpa and hsqldb, which are TomEE default but not WildFly.
