
mvn -Pprod -DskipTests clean package
mvn jib:build -Djib.from.image=board-updates:latest -Djib.to.image=ghcr.io/dashasan/board-updates:latest -Djib.to.auth.username=DasHasan -Djib.to.auth.password=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
mvn jib:build -Djib.to.auth.username=DasHasan -Djib.to.auth.password=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
