### In order to run your code, you need to make sure that the .jar files we downloaded earlier are in a place where Java can find them. There are many ways to set this up elegantly, but for now we'll just point the javac and java programs to the paths of three .jar files the code above needs to run. Try the command below that applies to your operating system; if it doesn't work and you've verified that all the files are in the current directory, see the part below for instructions on how to unpack the .jar files and get it working:

```shell
javac -classpath ".:./ejml-0.23.jar:./stanford-corenlp-3.9.2.jar:./stanford-english-corenlp-2018-10-05-models.jar" *.java 
```
### for Mac/linux, or

```shell
javac -classpath ".\stanford-corenlp-3.9.2.jar;.;./ejml-0.23.jar;./stanford-english-corenlp-2018-10-05-models.jar" *.java 
```

### for Windows.

### and then

```shell
java -classpath ".:./ejml-0.23.jar:./stanford-corenlp-3.9.2.jar:./stanford-english-corenlp-2018-10-05-models.jar" 
Driver 
```

### for Mac/linux, or

```shell
java -classpath ".\stanford-corenlp-3.9.2.jar;.;./ejml-0.23.jar;./stanford-english-corenlp-2018-10-05-models.jar" Driver
```

###  for Windows.



Source URL:

https://www2.seas.gwu.edu/~kinga/CS1111_F22/NLP_project.html