# JActor

JActor is a high-throughput Java Actor framework, one of the fastest Actor implementations in any language.
*   Two-way messages provide implicit flow control, for better performance under load.
*   A simple machine builder is included to help keep application code clear and simple.
*   A billion actors a second can be created on a single thread.

License: [LGPL](http://www.gnu.org/licenses/lgpl-2.1.txt)

Dependencies: Java 6, Maven 2

Available on [The (Maven) Central Repository](http://search.maven.org/#search|ga|1|org.agilewiki),
so you just need to add this to your POM file:
```
<dependency>
    <groupId>org.agilewiki.jactor</groupId>
    <artifactId>jactor</artifactId>
    <version>4.1.0</version>
</dependency>
```
(Be sure to update the version number appropriately, of course.)

| Links |
| --------------- |
| [Releases](https://freecode.com/projects/jactor/releases) |
| [Announcements](https://freecode.com/projects/jactor/announcements) |
| [Downloads](https://sourceforge.net/projects/jactor/files/) |
| [API](http://jactor.sourceforge.net/) |
| VCS: [Files](https://github.com/laforge49/JActor/), [Commits](https://github.com/laforge49/JActor/commits/master) |
| [Issues](https://github.com/laforge49/JActor/issues/) |
| [free(code)](http://freecode.com/projects/jactor/) |
| Google group: [agilewikidevelopers](http://groups.google.com/group/agilewikidevelopers/) |

## Message Passing between Actors in the same JVM
|              | Shared Mailbox | Across Mailboxes | Asynchronous Mailboxes |
| :----------- | -------------: | ---------------: | ---------------------: |
| **msgs per sec** |    222,861,399 |      151,987,233 |             81,083,272 |

### Test Environment
```
i7-3770 @ 3.40 GHz
ASUS P8Z77-M Motherboard
16 GB Corsair DDR3 1600 MHz RAM
Vertex 3 SATA III SSD
```

## Documentation
1.  [The Problem with Actors](https://www.ibm.com/developerworks/mydeveloperworks/blogs/jactor/entry/the_problem_with_actors56)

     Actor-based programming is quite appealing and holds a lot of potential. ...
     [more](https://www.ibm.com/developerworks/mydeveloperworks/blogs/jactor/entry/the_problem_with_actors56)

1.  [Introducing JActor](https://www.ibm.com/developerworks/mydeveloperworks/blogs/jactor/entry/introducing_jactor4)

     Vertical scaling is today a major issue when writing server code. Threads and locks are the traditional approach to
     making full utilization of fat (multi-core) computers, but result is code that is difficult to maintain and which to
     often does not run much faster than single-threaded code. ...
     [more](https://www.ibm.com/developerworks/mydeveloperworks/blogs/jactor/entry/introducing_jactor4)

1.  [JActor API Basics](https://www.ibm.com/developerworks/mydeveloperworks/blogs/jactor/entry/jactor_api_basics17)

    Here we cover the basic API of JActor, which is very easy to use as you will see from the examples provided. ...
    [more](https://www.ibm.com/developerworks/mydeveloperworks/blogs/jactor/entry/jactor_api_basics17)

1.  [Exception Handling made Easy](https://www.ibm.com/developerworks/mydeveloperworks/blogs/jactor/entry/exception_handling_made_easy4)

    Message passing between threads and callbacks can both make exception handling more difficult. On the other hand,
    the use of 2-way messages (request/response) provides us with a natural default: uncaught exceptions should be
    passed back to the requesting actor for handling. And that is exactly what JActor does. ...
    [more](https://www.ibm.com/developerworks/mydeveloperworks/blogs/jactor/entry/exception_handling_made_easy4)

1.  [A Bi-Modal Iterator for JActor](https://www.ibm.com/developerworks/mydeveloperworks/blogs/jactor/entry/a_bi_modal_iterator_for_jactor6)

    Loops are usually just a while loop with a hasNext() method in the condition and a next() method in the body. But
    when the response to these methods is asynchronous the loop fails, because the response is not received until after
    the while loop has completed. ...
    [more](https://www.ibm.com/developerworks/mydeveloperworks/blogs/jactor/entry/a_bi_modal_iterator_for_jactor6)

1.  [The Simple Machines of JActor](https://www.ibm.com/developerworks/mydeveloperworks/blogs/jactor/entry/the_simple_machines_of_jactor4)

    When multiple requests must be sent to other actors in the course of processing a request, the code can become quite
    muddled by the use of anonymous classes used to implement the callbacks for processing the responses of those
    requests. ...
    [more](https://www.ibm.com/developerworks/mydeveloperworks/blogs/jactor/entry/the_simple_machines_of_jactor4)

1.  [JActor Factories](https://www.ibm.com/developerworks/mydeveloperworks/blogs/jactor/entry/jactor_factories4)

    The JAFactory actor binds actor type names to actor factories, allowing type names to be used in place of class
    names for serialization/deserialization. ...
    [more](https://www.ibm.com/developerworks/mydeveloperworks/blogs/jactor/entry/jactor_factories4)

## Dependent Projects
*   [Incremental Deserialization](https://github.com/laforge49/JID)
*   [JActor Sockets](https://github.com/laforge49/JASocket)
*   [File Persistence](https://github.com/laforge49/JFile)

## Contact
*   email:   laforge49@gmail.com
*   twitter: @laforge49

## Slides
*   [Actors in the Small](http://www.slideshare.net/laforge49/actors-in-the-small)
*   [Code Samples](http://www.slideshare.net/laforge49/code-samples-13901008)
