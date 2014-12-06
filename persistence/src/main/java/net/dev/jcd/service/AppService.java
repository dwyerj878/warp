/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.dev.jcd.service;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import net.dev.jcd.model.Application;

// The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class AppService {

    @Inject
    private Logger log;

    @Inject
    private EntityManager em;

    @Inject
    private Event<Application> memberEventSrc;

    public void register(Application application) throws Exception {
        log.info("Registering " + application.getName());
        if (application.getId() == null)
        	em.persist(application);
        else 
        {
        	Application existing = em.find(Application.class, application.getId());
        	existing.setName(application.getName());
        	existing.setProperties(application.getProperties());
        	em.merge(existing);
        }
        memberEventSrc.fire(application);
    }
}
