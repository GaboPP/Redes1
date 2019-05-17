NAME=Tarea1_client
BUILD_PATH=../../build
BIN_PATH=$(BUILD_PATH)/bin
JAR_PATH=$(BUILD_PATH)/jar

main: servidor

servidor:
	mkdir -p $(BIN_PATH)
	javac -Xlint -classpath .:json/json-20180813.jar ./*.java -d $(BIN_PATH)
	mkdir -p build/jar/
	jar cfm $(JAR_PATH)/$(NAME).jar manifest -C $(BIN_PATH) .

run:
	java -cp .:java-json.jar -jar $(JAR_PATH)/$(NAME).jar
	rm -rf $(BUILD_PATH)

clean:
	rm -rf $(BUILD_PATH)