.PHONY: release-bintray release-maven
release-maven:
	./gradlew clean uploadArchives

release-bintray:
	./gradlew clean bintray
