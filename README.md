The JActor project implements actors in Java that can process 80 to 200 million messages
per second, depending on the transport mechanism.

JActor 2 supports method calls (without callbacks) for initialization, synchronous
and concurrent requests, as well as static typing, all of which make for a more
natural coding style when developing applications.

Downloads:     https://sourceforge.net/projects/jactor/files/
Blog:          https://sourceforge.net/p/jactor/blog/
API:           http://jactor.sourceforge.net/
VCS:           https://github.com/laforge49/JActor/
Issues:        https://github.com/laforge49/JActor/issues/
free(code):    http://freecode.com/projects/jactor/
Google groups: http://groups.google.com/group/agilewikidevelopers/

Documentation
 - The Problem with Actors:        https://www.ibm.com/developerworks/mydeveloperworks/blogs/jactor/entry/the_problem_with_actors56
 - Introducing JActor:             https://www.ibm.com/developerworks/mydeveloperworks/blogs/jactor/entry/introducing_jactor4
 - JActor API Basics:              https://www.ibm.com/developerworks/mydeveloperworks/blogs/jactor/entry/jactor_api_basics17
 - Exception Handling made Easy:   https://www.ibm.com/developerworks/mydeveloperworks/blogs/jactor/entry/exception_handling_made_easy4
 - A Bi-Modal Iterator for JActor: https://www.ibm.com/developerworks/mydeveloperworks/blogs/jactor/entry/a_bi_modal_iterator_for_jactor6
 - The Simple Machines of JActor:  https://www.ibm.com/developerworks/mydeveloperworks/blogs/jactor/entry/the_simple_machines_of_jactor4
 - JActor Factories:               https://www.ibm.com/developerworks/mydeveloperworks/blogs/jactor/entry/jactor_factories4

Slides
 - Actors in the Small:   http://www.slideshare.net/laforge49/actors-in-the-small
 - Code Samples:          http://www.slideshare.net/laforge49/code-samples-13901008
 - High-Performance Java: http://www.slideshare.net/laforge49/high-performancejava

Dependent Projects
 - Incremental Deserialization    https://github.com/laforge49/JID
 - File Persistence               https://github.com/laforge49/JFile
 - Netty Integration              https://github.com/kumarshantanu/JANetty

Available on The Central Repository (Maven): http://search.maven.org/#search|ga|1|org.agilewiki
So you just need to add this to your POM file:
    <dependency>
      <groupId>org.agilewiki.jactor</groupId>
      <artifactId>jactor</artifactId>
      <version>4.1.0</version>
    </dependency>
(Be sure to update the version number appropriately, of course.)

This project is a reimplementation of a portion of the AsyncFP Scala project:
    https://github.com/laforge49/Asynchronous-Functional-Programming/wiki

Message passing between actors mostly uses 2-way messages (request / response).
There are several reasons for this:

    o With 2-way messaging, sending a request is very similar to a method call with
      a callback. Most requests are processed synchronously, which is why JActor is
      so much faster than other actor implementations.
    o Mailboxes are used mostly when passing messages between threads and are
      first-class objects. As first-class objects, mailboxes can be used by more
      than one actor. Passing messages between actors with a common mailbox is
      always done synchronously and is very fast.
    o Flow control is implicit to 2-way messaging. Systems with good flow control
      are generally well-behaved when operating with a full load.

Two-way messaging is so much faster than 1-way messaging that it is practical to use
2-way messages when only 1-way messages are needed. There is however one case where
you shouldn't use 2-way messages: when events from non-actor code need to be sent to
an actor. The JAEvent class is used to do this.

Exception Handling

The extensive use of callbacks complicates control flow, which is only made worse
with some callbacks being executed asynchronously. Exception trapping then can be
quite error prone. So exception handling is supported. A default exception handler
is also provided which passes any uncaught exceptions that occurred while processing
a request back to the actor which sent the request, recursively.

Bi-Modal Iterator

Loops with 2-way messages can be problematic, as iterations typically must wait for
the response from the previous iteration. A bi-modal iterator is provided to cover
this. Each iteration takes 5 nanoseconds for synchronous responses and 8 nanoseconds
when a response is asynchronous.

State Machine

State machines are often used with actors and can add considerable clarity to the
code. JActor includes classes for composing and executing state machines that are
compatible with 2-way messages.

Dependency Injection

If an actor receives a request of a type that it does not recognize and that actor
has been assigned a parent actor, then the request is immediately forwarded to the
parent actor.

Message Passing Benchmarks

                         Shared Mailbox       Across Mailboxes  Asyncronous Mailboxes
msgs per sec                222,861,399            151,987,233             81,083,272

Test Environment

i7-3770 @ 3.40 GHz
ASUS P8Z77-M Motherboard
16 GB Corsair DDR3 1600 MHz RAM
Vertex 3 SATA III SSD

Contact

email:   laforge49@gmail.com
twitter: @laforge49
