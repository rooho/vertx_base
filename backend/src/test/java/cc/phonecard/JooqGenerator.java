package cc.phonecard;

import org.jooq.codegen.GenerationTool;
import org.jooq.meta.jaxb.Configuration;
import org.jooq.meta.jaxb.Database;
import org.jooq.meta.jaxb.Generate;
import org.jooq.meta.jaxb.Generator;
import org.jooq.meta.jaxb.Jdbc;
import org.jooq.meta.jaxb.Strategy;
import org.jooq.meta.jaxb.Target;
import org.junit.jupiter.api.Test;

public class JooqGenerator {

  @Test
  void testGenerate() {
    Configuration configuration = new Configuration()

      // Configure the database connection here
      .withJdbc(new Jdbc()
        .withDriver("org.sqlite.JDBC")
        .withUrl("jdbc:sqlite:D://data//phone_card.db")
      )
      .withGenerator(
        new Generator()
        .withDatabase(new Database()
          .withName("org.jooq.meta.sqlite.SQLiteDatabase")
          .withIncludes(".*")
          .withExcludes("")
        )
        // Generation flags: See advanced configuration properties
        .withGenerate(new Generate()
          .withPojos(true)
          .withComments(true)
          .withCommentsOnCatalogs(true)
          .withRelations(true)
          .withImmutablePojos(false) // if true, cannot use 'into()' method
          .withInterfaces(true)
          .withDaos(true))
        .withTarget(
          new Target()
            .withPackageName("cc.phonecard.jooq")
            .withDirectory("D:\\codeGen\\jooq")
        ));
    try {
      GenerationTool.generate(configuration);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  void testGenerate2() {
    Configuration configuration = new Configuration()

      // Configure the database connection here
      .withJdbc(new Jdbc()
        .withDriver("org.sqlite.JDBC")
        .withUrl("jdbc:sqlite:D://data//phone_card.db")
      )
      .withGenerator(new Generator()
        .withName("io.github.jklingsporn.vertx.jooq.generate.classic.ClassicJDBCVertxGenerator")
        .withDatabase(new Database()
            .withName("org.jooq.meta.sqlite.SQLiteDatabase")
            .withIncludes(".*")
            .withExcludes("")
        )
        .withTarget(new Target()
          .withPackageName("cc.phonecard.jooq")
          .withDirectory("D:\\codeGen\\jooq")
        )
        .withGenerate(new Generate().withInterfaces(true).withFluentSetters(true).withDaos(true))
        .withStrategy(new Strategy().withName("io.github.jklingsporn.vertx.jooq.generate.VertxGeneratorStrategy")));
    try {
      GenerationTool.generate(configuration);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
