# S4.Web-Dynamique.FRAMEWORK

Framework Java web développé dans le cadre du module **Web Dynamique**.
Il simplifie la création d'applications web basées sur Jakarta Servlet,
Spring et des contrôleurs Java annotés.

## Fonctionnalités

- découverte automatique des contrôleurs avec `@Controller` ;
- définition des routes avec `@UrlMapping` ;
- support des méthodes HTTP, notamment `GET` et `POST` ;
- affichage de vues avec `ModelAndView` ;
- création d'API JSON avec `@WebApi` et Jackson ;
- récupération automatique des paramètres des requêtes ;
- binding de paramètres vers des objets Java ;
- upload d'un ou plusieurs fichiers avec `MapFile` ;
- gestion simplifiée des sessions avec `MapSession` ;
- contrôle d'accès avec `@Auth` et les rôles.

## Dépendances du framework

Les JAR suivants se trouvent dans le dossier `lib/` du framework :

| JAR | Version |
|---|---:|
| `servlet-api.jar` | Jakarta Servlet API |
| `classgraph-4.8.179.jar` | 4.8.179 |
| `spring-beans-7.0.0-M8.jar` | 7.0.0-M8 |
| `spring-context-7.0.0-M8.jar` | 7.0.0-M8 |
| `spring-core-7.0.0-M8.jar` | 7.0.0-M8 |
| `spring-expression-7.0.0-M8.jar` | 7.0.0-M8 |
| `spring-web-7.0.8.jar` | 7.0.8 |
| `spring-jcl-6.2.19.jar` | 6.2.19 |
| `jackson-databind-2.19.2.jar` | 2.19.2 |
| `jackson-core-2.19.2.jar` | 2.19.2 |
| `jackson-annotations-2.19.2.jar` | 2.19.2 |

Le JAR du framework généré après compilation est :

```text
build/WEB-INF/lib/Framework.jar
```

Compilation sous Windows :

```bat
run.bat
```

## JAR de l'application utilisant le framework

Pour une application utilisant `Framework.jar`, ajouter également les JAR
nécessaires à ses fonctionnalités. La liste ci-dessous correspond à
l'application exemple utilisée avec le framework.

### Spring, JPA et Hibernate

```text
spring-aop-7.0.0-M8.jar
spring-data-commons-4.0.4.jar
spring-data-jpa-4.0.4.jar
spring-jdbc-7.0.0-M8.jar
spring-orm-7.0.0-M8.jar
spring-tx-7.0.0-M8.jar
hibernate-core-7.3.0.Final.jar
hibernate-commons-annotations-7.0.3.Final.jar
hibernate-models-1.1.0.jar
jakarta.persistence-api-3.2.0.jar
jakarta.transaction-api-2.0.1.jar
byte-buddy-1.18.8.jar
classmate-1.7.3.jar
jandex-3.5.3.jar
jboss-logging-3.6.3.Final.jar
```

### Base de données, JAXB et logging

```text
mysql-connector-j-8.0.31.jar
jakarta.activation-api-2.1.4.jar
jakarta.xml.bind-api-4.0.5.jar
jaxb-core-4.0.7.jar
jaxb-impl-4.0.7.jar
istack-commons-runtime-4.2.0.jar
antlr4-runtime-4.13.2.jar
jspecify-1.0.0.jar
slf4j-api-2.0.17.jar
slf4j-simple-2.0.17.jar
```

Le fichier `Framework.jar` doit être placé dans le dossier `WEB-INF/lib` de
l'application, avec les dépendances nécessaires ci-dessus.

## Exemple rapide

```java
@Controller
public class HomeController {

    @UrlMapping(url = "/", method = "GET")
    public ModelAndView accueil() {
        return new ModelAndView("accueil");
    }

    @WebApi
    @UrlMapping(url = "/api/test", method = "GET")
    public String api() {
        return "{\"status\":\"OK\"}";
    }
}
```

## Licence

Projet distribué sous licence MIT. Voir [LICENSE](LICENSE).
