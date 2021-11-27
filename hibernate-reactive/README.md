- [ ] Create database
- [ ] test to connect
- [ ] run the project and connect
- [ ] run reactive way using hibernate reactive
- [] Ref - https://github.com/hibernate/hibernate-reactive

```sql
CREATE ROLE "16bits_user" WITH
LOGIN
NOSUPERUSER
CREATEDB
CREATEROLE
INHERIT
NOREPLICATION
CONNECTION LIMIT -1
PASSWORD 'xxxxxx';
COMMENT ON ROLE "16bits_user" IS 'User to test the hibernate reactive';
```

Start the Postgresql service
```bash
brew services start postgres
```

Stop the Postgresql service
```bash
brew services stop postgres
```

References
https://hibernate.org/orm/tooling/
https://docs.jboss.org/hibernate/orm/5.6/topical/html_single/metamodelgen/MetamodelGenerator.html