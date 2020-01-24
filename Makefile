clean:
	./gradlew clean

assemble:
	./gradlew assemble

publishLocal:
	./gradlew publishToMavenLocal

test:
	./gradlew test --debug

dependencies:
	./gradlew dependencies