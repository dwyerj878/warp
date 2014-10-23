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
package net.dev.jcd.data;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import net.dev.jcd.model.Application;

@ApplicationScoped
public class ApplicationRepository {

    @Inject
    private EntityManager em;

    public Application findById(Long id) {
        return em.find(Application.class, id);
    }

    public Application findByName(String name) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Application> criteria = cb.createQuery(Application.class);
        Root<Application> app = criteria.from(Application.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        // criteria.select(member).where(cb.equal(member.get(Member_.email), email));
        criteria.select(app).where(cb.equal(app.get("name"), name));
        return em.createQuery(criteria).getSingleResult();
    }

    public List<Application> findAllOrderedByName() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Application> criteria = cb.createQuery(Application.class);
        Root<Application> app = criteria.from(Application.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        // criteria.select(member).orderBy(cb.asc(member.get(Member_.name)));
        criteria.select(app).orderBy(cb.asc(app.get("name")));
        return em.createQuery(criteria).getResultList();
    }
}
