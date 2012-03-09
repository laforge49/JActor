/*
 * Copyright 2011 Bill La Forge
 *
 * This file is part of AgileWiki and is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License (LGPL) as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 * or navigate to the following url http://www.gnu.org/licenses/lgpl-2.1.txt
 *
 * Note however that only Scala, Java and JavaScript files are being covered by LGPL.
 * All other files are covered by the Common Public License (CPL).
 * A copy of this license is also included and can be
 * found as well at http://www.opensource.org/licenses/cpl1.0.txt
 */

/**
 * <h2>Introduction to JActor</h2>
 * <p>
 *     JActor is blazing fast, passing messages at a rate of up to 1 billion messages per second. It achieves this
 *     by using synchronous operations whenever possible. Indeed, JActor is fast enough that you can use actors
 *     everywhere.
 * </p>
 * <p>
 *     On the other hand, it is easy enough to force an actor to operate asynchronously--without changing how messages
 *     are sent to it. So I/O and computations taking longer than a few microseconds can execute on a different thread.
 *     Jactor is entirely free of locks and parallel operations are easy to code, making JActor ideal for applications
 *     that need to scale vertically.
 * </p>
 *
 * <h2>The Mailbox Factory</h2>
 * <p>
 *     Every program needs a MailboxFactory. It allocates the threads used by JActor and creates the mailboxes used
 *     by the actors. The MailboxFactory.newMailboxFactory method takes one argument--the number of threads to be
 *     allocated. This should be at least as large as the number of hardware threads on your computer, but add
 *     additional threads to support parallel I/O operations.
 * </p>
 * <pre>
 *     MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(t);
 * </pre>
 * <p>
 *     The MailboxFactory.close method is used to halt the threads as they become idle. A call to this method is not
 *     required unless you are running a series of tests, where each test creates a new mailbox factory.
 * </p>
 * <pre>
 *     mailboxFactory.close();
 * </pre>
 *
 * <h2>Mailboxes</h2>
 * <p>
 *     Mailboxes are light-weight threads.
 * </p>
 * <p>
 *     Every actor needs a mailbox. It is used for passing messages between actors. But actors can share a common
 *     mailbox and when they do, all messages passed between them are handled synchronously. Actors which exchange a lot
 *     of messages should, whenever possible, share the same mailbox.
 * </p>
 * <p>
 *     A mailbox is created by calling the MailboxFactory.createMailbox method.
 * </p>
 * <pre>
 *     Mailbox mailbox = mailboxFactory.createMailbox();
 * </pre>
 * <p>
 *     Mailboxes have a getMailboxFactory method, so if you have one mailbox you can easily create another.
 * </p>
 * <pre>
 *     Mailbox mailbox2 = mailbox.getMailboxFactory().createMailbox();
 * </pre>
 *
 * <h2>Asynchronous Mailboxes</h2>
 * <p>
 *     An actor with an asynchronous mailbox operates on a separate thread, which is to say that it does not
 *     synchronously process messages from other actors.
 * </p>
 * <p>
 *     The createAsyncMailbox method on MailboxFactory creates an asynchronous mailbox.
 * </p>
 * <pre>
 *     Mailbox asyncMailbox = mailboxFactory.createAsyncMailbox();
 * </pre>
 * <p>
 *     The isAsync method on mailbox can be used to see if a mailbox is asynchronous.
 * </p>
 * <pre>
 *     boolean async = mailbox.isAsync();
 * </pre>
 *
 * <h2>Actors</h2>
 * <p>
 *     Actors are thread-safe objects which exchange request and response messages. Most message passing is 2-way, and
 *     an actor expects a response, or an exception, when a request is sent.
 * </p>
 * <p>
 *     There are three types of actors: JLPCActor, JBActor and JCActor. They all implement the Actor interface and take
 *     a single argument, a mailbox, in their constructors.
 * </p>
 * <p>
 *     Actors have a getMailbox method, so if you have an actor you can create a mailbox or an asynchronous mailbox.
 * </p>
 * <pre>
 *     MailboxFactory mailboxFactory = actor.getMailbox().getMailboxFactory();
 *     Mailbox mailbox = mailboxFactory.createMailbox();
 *     Mailbox asyncMailbox = mailboxFactory.createAsyncMailbox();
 * </pre>
 *
 * <h2>JAFuture and JAEvent</h2>
 * <p>
 *     To send a request message (an Object) to an actor from non-actor code, you can use either JAFuture or JAEvent.
 *     The JAFuture.send method sends the request, waits for and then returns a result. And if there was an exception
 *     while processing the request, even if it occurred on another thread, the exception is rethrown. JAFuture is
 *     used mostly in testing, or to start up a top-level actor, as it ties up a thread until a response is received.
 * </p>
 * <pre>
 *     JAFuture future = new JAFuture();
 *     Object result = future.send(actor, request);
 * </pre>
 * <p>
 *     In contrast, JAEvent.send is used to send a request message without waiting for a response. Any response or
 *     exception resulting from the processing of the request message is discarded. JAEvent can be used by asynchronous
 *     code, e.g. in a CometD application, to pass events to an actor for processing.
 * </p>
 * <pre>
 *     JAEvent jaEvent = new JAEvent();
 *     jaEvent.sendEvent(actor, request);
 * </pre>
 * <p>
 *     The application logic for processing a request need not distinguish between 1-way and 2-way requests.
 * </p>
 *
 * <h2>The RP Class</h2>
 * <p>
 *     When an actor receives a request object, it is also given an accompanying RP object. The processResponse
 *     method on the RP class handles the transport and/or processing of the response.
 * </p>
 * <pre>
 *     protected void processRequest(Object request, RP rp) throws Exception {
 *         rp.processResponse(request); //echo back the request
 *     }
 * </pre>
 * <p>
 *     RP does have a second method, isEvent, which can be used to detect 1-way messaging. When isEvent
 *     returns true, a call to the processResponse method does nothing.
 * </p>
 * <pre>
 *     boolean event = rp.isEvent();
 * </pre>
 * <p>
 *     The send method of an actor is used to send a request to another actor. And the 3rd parameter is a
 *     RP object. But the RP object sent by one actor is often not the same object that
 *     is received by the other actor. The JActor code does a lot of intermediation with RP objects.
 * </p>
 * <pre>
 *     send(anotherActor, someRequest, new RP() {
 *         public void processResponse(Object response) {
 *             System.out.println("sent " + someRequest + " and got back " + response);
 *         }
 *     });
 * </pre>
 *
 * <h2>The ExceptionHandler</h2>
 * <p>
 *     Trapping exceptions using try/catch when there are a lot of callbacks like RP can be tedious and
 *     error prone. So instead we use an ExceptionHandler. The ExceptionHandler interface defines one method, process,
 *     for processing an exception. Calling the setExceptionHandler method of an actor result in all exceptions being
 *     passed to the application's own exception handler. If an actor does not have an exception handler, exceptions
 *     which occur while processing a request message are passed back to the actor which sent the request, recursively.
 * </p>
 * <pre>
 *     protected void processRequest(Object request, RP rp) throws Exception {
 *         setExceptionHandler(new ExceptionHandler(){
 *             public void process(Exception ex) {
 *                 System.err.println("exception occurred while processing request " + request):
 *                 pr.process(null);
 *             }
 *         });
 *         .
 *         .
 *         .
 *     }
 * </pre>
 *
 * <h2>JAIterator and JANull</h2>
 * <p>
 *     Writing a loop when a callback is involved is difficult, especially if the number of iterations is so large that
 *     a recursive approach results in stack overflow. One answer is to use JAIterator. The JAIterator class has two
 *     methods, iterate and process, where the iterate method calls the process method repeatedly until the process
 *     returns a result that is non-null--which is then returned as iterate's result.
 * </p>
 * <pre>
 *     final int max = 5;
 *     RP printResult = new RP() {
 *         public void processResponse(Object rsp) {
 *             System.out.println(rsp);
 *         }
 *     };
 *
 *     (new JAIterator() {
 *         int i;
 *         int r = 1;
 *
 *         public void process(RP rp) throws Exception {
 *             if (i >= max) rp.processResponse(new Integer(r));
 *             else {
 *                 i += 1;
 *                 r = r * i;
 *                 rp.processResponse(null);
 *             }
 *         }
 *     }).iterate(printResult);
 * </pre>
 * <p>
 *     Sometimes however we want iterate to return a null result. To do this, we just have the processResponse method return
 *     a JANull object, which the iterate method recognizes as being non-null but which it converts to a null when
 *     returning the result.
 * </p>
 *
 * <h2>SMBuilder</h2>
 * <p>
 *     The SMBuilder class builds and runs a state machine, and can be very helpful when the application logic gets a
 *     bit complex and the resulting mess of code becomes unreadable. State machines are quite commonly used with
 *     actors. They let you focus on the application logic and avoid having to deal with low-level control flow.
 * </p>
 * <p>
 *     A good example of this can be found in the
 *     <a target="_blank"
 * href="https://github.com/laforge49/JActor/blob/master/src/main/java/org/agilewiki/jactor/components/factory/Factory.java"
 * >Factory</a> class, though it only makes limited use of SMBuilder's capabilities.
 * </p>
 * <pre>
 *     SMBuilder smb = new SMBuilder();
 *     smb._send(actor, include);
 *     smb._if(actorName == null, "fin");
 *     smb._send(actor, new SetActorName(actorName));
 *     smb._send(getActor(), new RegisterActor(actor));
 *     smb._label("fin");
 *     smb._return(actor);
 *     smb.call(rp);
 * </pre>
 *
 * <h2>Parallel Processing</h2>
 * <p>
 *     Parallel processing is not at all difficult. You create some asynchronous actors and then do a series of calls to
 *     send, aggregating the responses. The aggregated response can then be returned as a result once the last response
 *     has been received.
 * </p>
 * <p>
 *     Benchmarks for parallelism are probably the simplest of all, as the responses do not need to be aggregated. The
 *     JAParallel class was developed specifically to support this,
 *     <a target="_blank"
 * href="https://github.com/laforge49/JActor/blob/master/src/test/java/org/agilewiki/jactor/lpc/timingTest/SharedMailboxTest.java"
 * >SharedMailboxTest</a> being a good case in point.
 * </p>
 *
 * <h2>Publish/Subscribe</h2>
 * <p>
 *     An actor that implements publish/subscribe provides a means of broadcasting events. Subscribers are actors which
 *     have been "signed up" to recieve the published requests. Requests then are broadcast by wrapping them in a
 *     publish request and sending them to the publish/subscribe actor.
 * </p>
 * <p>
 *     Implemented using 2-way messages, the response of a publish request holds the count of the number of subscribers
 *     which have received and processed the request.
 * </p>
 * <p>
 *     There is no particular order in which subscribers receive requests. And the request is sent to all subscribers
 *     at once without waiting for a response. Only the response to the publish request is delayed until all subscribers
 *     have responded.
 * </p>
 */

package org.agilewiki.jactor;
