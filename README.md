Steps:

Build war: mvn clean compile package test -P ${env} (dev/uat/staging/prod)

Run locally: mvn tomcat7:run

psql to db: psql -d integ -U integ -h $hostname -W
