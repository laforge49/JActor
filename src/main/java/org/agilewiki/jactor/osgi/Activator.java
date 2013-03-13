/*
 * Copyright 2013 Bill La Forge
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
package org.agilewiki.jactor.osgi;

import org.agilewiki.jactor.JAMailboxFactory;
import org.agilewiki.jactor.MailboxFactory;
import org.osgi.framework.*;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Dictionary;
import java.util.Hashtable;

public final class Activator implements BundleActivator {
    private static final String CONFIG_PID = "org.agilewiki.jactor";

    final Logger logger = LoggerFactory.getLogger(ConfigUpdater.class);
    private BundleContext bundleContext;
    private ServiceRegistration configUpdaterRegistration;
    private ServiceRegistration mailboxFactoryRegistration;

    private int threadCount = 0;
    private MailboxFactory mailboxFactory;

    public void start(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
        ConfigUpdater configUpdater = new ConfigUpdater();
        Hashtable<String, Object> properties = new Hashtable<String, Object>();
        properties.put(Constants.SERVICE_PID, CONFIG_PID);
        configUpdaterRegistration = bundleContext.registerService(
                ManagedService.class.getName(), configUpdater, properties);
    }

    public void stop(BundleContext context) {
        configUpdaterRegistration.unregister();
        mailboxFactoryRegistration.unregister();
        mailboxFactory.close();
    }

    private class ConfigUpdater implements ManagedService {
        @Override
        public void updated(Dictionary config) throws ConfigurationException {
            if (config == null)
                return;
            if (threadCount > 0) {
                logger.warn("restart needed for new threadCount to take effect");
                return;
            }
            String tc = (String) config.get("threadCount");
            try {
                threadCount = Integer.valueOf(tc);
            } catch (Throwable ex) {
                throw new ConfigurationException("threadCount", "not an int: " + tc, ex);
            }
            logger.info("threadCount: " + threadCount);
            mailboxFactory = JAMailboxFactory.newMailboxFactory(threadCount);
            mailboxFactoryRegistration = bundleContext.registerService(
                    MailboxFactory.class.getName(), mailboxFactory, new Hashtable<String, Object>());
        }
    }
}
