
mvn -Pprod -DskipTests clean package jib:build -Djib.from.image=boardUpdates:latest -Djib.to.image=gcr.io/dashasan/boardUpdates:latest -Djib.to.auth.username="DasHasan" -Djib.to.auth.password="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"
