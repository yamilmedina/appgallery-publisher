clean:
	./gradlew clean

assemble:
	./gradlew assemble

publishLocal:
	./gradlew publishToMavenLocal

test:
	./gradlew test --verbose

dependencies:
	./gradlew dependencies