JActor is a high-throughput Java Actor framework, one of the fastest Actor implementations in any language.
*   Two-way messages provide implicit flow control, for better performance under load.
*   A simple machine builder is included to help keep application code clear and simple.
*   A billion actors a second can be created on a single thread.

# Message Passing Benchmarks
```
                         Shared Mailbox       Across Mailboxes  Asynchronous Mailboxes
msgs per sec                222,861,399            151,987,233              81,083,272
```

# Test Environment
```
i7-3770 @ 3.40 GHz
ASUS P8Z77-M Motherboard
16 GB Corsair DDR3 1600 MHz RAM
Vertex 3 SATA III SSD
```

# Documentation
1.  [The Problem with Actors](https://www.ibm.com/developerworks/mydeveloperworks/blogs/jactor/entry/the_problem_with_actors56)
1.  [Introducing JActor](https://www.ibm.com/developerworks/mydeveloperworks/blogs/jactor/entry/introducing_jactor4)
1.  [JActor API Basics](https://www.ibm.com/developerworks/mydeveloperworks/blogs/jactor/entry/jactor_api_basics17)
1.  [Exception Handling made Easy](https://www.ibm.com/developerworks/mydeveloperworks/blogs/jactor/entry/exception_handling_made_easy4)
1.  [A Bi-Modal Iterator for JActor](https://www.ibm.com/developerworks/mydeveloperworks/blogs/jactor/entry/a_bi_modal_iterator_for_jactor6)
1.  [The Simple Machines of JActor](https://www.ibm.com/developerworks/mydeveloperworks/blogs/jactor/entry/the_simple_machines_of_jactor4)
1.  [JActor Factories](https://www.ibm.com/developerworks/mydeveloperworks/blogs/jactor/entry/jactor_factories4)

# Slides
*   [Actors in the Small](http://www.slideshare.net/laforge49/actors-in-the-small)
*   [Code Samples](http://www.slideshare.net/laforge49/code-samples-13901008)
*   [High-Performance Java](http://www.slideshare.net/laforge49/high-performancejava)

# Dependent Projects
*   [Incremental Deserialization](https://github.com/laforge49/JID)
*   [File Persistence](https://github.com/laforge49/JFile)

Available on The Central Repository (Maven): http://search.maven.org/#search|ga|1|org.agilewiki
So you just need to add this to your POM file:
```
<dependency>
    <groupId>org.agilewiki.jactor</groupId>
    <artifactId>jactor</artifactId>
    <version>4.1.0</version>
</dependency>
```
(Be sure to update the version number appropriately, of course.)

# Misc. Links
*   [Downloads](https://sourceforge.net/projects/jactor/files/)
*   [API](http://jactor.sourceforge.net/)
*   [VCS](https://github.com/laforge49/JActor/)
*   [Issues](https://github.com/laforge49/JActor/issues/)
*   [free(code)](http://freecode.com/projects/jactor/)
*   Google group: [agilewikidevelopers](http://groups.google.com/group/agilewikidevelopers/)

# Contact
*   email:   laforge49@gmail.com
*   twitter: @laforge49
