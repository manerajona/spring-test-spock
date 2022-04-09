import java.text.SimpleDateFormat
import java.util.stream.IntStream

class Groovy101 {

    static void main(String[] args) {

        // style
        println("Hello World")
        println 'Hello World' // same but not quite

        // Functions
        println doubleIt(69)

        def x = doubleIt 20
        println "x is $x"

        // Strings
        def s = "JoNaThAn"
        println "My name is ${s.toUpperCase()}"
        println 'My name is ${s.toUpperCase()}' // Literal

        def date = new Date()
        println "Today is ${sdf.format(date)}"

        def s1 = "A string"
        def s2 = 'A string'
        def s3 = """Groovy
                is
                cool!
         """
        println([s1, s2, s3])

        // Objects
        def jonathan = new Person("Jonathan", 30)
        jonathan.describePerson()

        jonathan.name = "Jona"
        jonathan.age = 33
        jonathan.describePerson()

        // Collections
        Map m = new HashMap()
        m.put("p1", jonathan)
        m.put("p2", new Person("Julia", 23))

        ((Person) m.get("p2")).describePerson()

        m.p1 = new Person("Margaret", 10)
        ((Person) m.p1).describePerson()

        // Iterators
        for (int j in [1, 2, 3]) println j

        [3, 2, 1].each {
            println it
        }

        // Closures
        def echoIt = {
            println it
        }
        echoIt "Hello closure!"

        def echoThat = { that ->
            println that
        }
        echoThat "This is that..."

        def echoABC = { a, b, c ->
            println a; println b; println c
        }
        echoABC("A", "B", "C")

        def i = oneArgMethod { 10 }

        assert i == 20

        def j = oneArgMethod { def y = 3; y * 2 }

        assert j == 12

        // Closure Resolution
        jonathan.countdownYears()
        jonathan.executeInside { println "age $age" }
    }

    static final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy")

    static Integer doubleIt(i) {
        return i * 2
    }

    static class Person {
        String name
        Integer age

        Person(String name, Integer age) {
            this.name = name; this.age = age
        }

        void describePerson() {
            println "Person name is ${name}, ${age} years old"
        }

        Closure countdownYears = {
            println 'counting down years...'
            IntStream.rangeClosed(0, age)boxed()sorted(Comparator.reverseOrder())forEach(i -> {
                sleep(150); print "$i-"
            }); println 'boom!'
        }

        def executeInside(Closure c) {
            c.delegate = this; c()
        }
    }

    static def oneArgMethod(closure) {
        closure() * 2
    }

}
