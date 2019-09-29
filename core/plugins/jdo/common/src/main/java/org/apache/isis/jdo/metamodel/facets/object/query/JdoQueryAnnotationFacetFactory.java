/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.apache.isis.jdo.metamodel.facets.object.query;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;

import org.apache.isis.config.IsisConfigurationLegacy;
import org.apache.isis.config.internal._Config;
import org.apache.isis.metamodel.JdoMetamodelUtil;
import org.apache.isis.metamodel.facetapi.FacetHolder;
import org.apache.isis.metamodel.facetapi.FacetUtil;
import org.apache.isis.metamodel.facetapi.FeatureType;
import org.apache.isis.metamodel.facetapi.MetaModelValidatorRefiner;
import org.apache.isis.metamodel.facets.Annotations;
import org.apache.isis.metamodel.facets.FacetFactoryAbstract;
import org.apache.isis.metamodel.specloader.validator.MetaModelValidator;
import org.apache.isis.metamodel.specloader.validator.MetaModelValidatorComposite;
import org.apache.isis.metamodel.specloader.validator.MetaModelValidatorVisiting;

public class JdoQueryAnnotationFacetFactory extends FacetFactoryAbstract implements MetaModelValidatorRefiner {

    public JdoQueryAnnotationFacetFactory() {
        super(FeatureType.OBJECTS_ONLY);
    }

    @Override
    public void process(ProcessClassContext processClassContext) {
        final Class<?> cls = processClassContext.getCls();

        // only applies to JDO entities; ignore any view models
        if(!JdoMetamodelUtil.isPersistenceEnhanced(cls)) {
            return;
        }

        final Queries namedQueriesAnnotation = Annotations.getAnnotation(cls, Queries.class);
        final FacetHolder facetHolder = processClassContext.getFacetHolder();

        if (namedQueriesAnnotation != null) {
            FacetUtil.addFacet(new JdoQueriesFacetAnnotation(
                    namedQueriesAnnotation.value(), facetHolder));
            return;
        }

        final Query namedQueryAnnotation = Annotations.getAnnotation(cls, Query.class);
        if (namedQueryAnnotation != null) {
            FacetUtil.addFacet(new JdoQueryFacetAnnotation(
                    namedQueryAnnotation, facetHolder));
        }
    }

    @Override
    public void refineMetaModelValidator(
            final MetaModelValidatorComposite metaModelValidator) {

        final boolean validateFromClause = getConfiguration().getReflector().getValidator().isJdoqlFromClause();
        if (validateFromClause) {
            final MetaModelValidator queryFromValidator = new MetaModelValidatorVisiting(new VisitorForFromClause(this));
            metaModelValidator.add(queryFromValidator);
        }

        final boolean validateVariablesClause = getConfiguration().getReflector().getValidator().isJdoqlVariablesClause();
        if (validateVariablesClause) {
            final MetaModelValidator queryFromValidator = new MetaModelValidatorVisiting(new VisitorForVariablesClause(this));
            metaModelValidator.add(queryFromValidator);
        }
    }

    private final static Pattern fromPattern = Pattern.compile("SELECT.*?FROM[\\s]+([^\\s]+).*", Pattern.CASE_INSENSITIVE);

    static String from(final String query) {
        final Matcher matcher = fromPattern.matcher(query);
        return matcher.matches() ? matcher.group(1) :  null;
    }

    private final static Pattern variablesPattern = Pattern.compile(".*?VARIABLES[\\s]+([^\\s]+).*", Pattern.CASE_INSENSITIVE);
    static String variables(final String query) {
        final Matcher matcher = variablesPattern.matcher(query);
        return matcher.matches() ? matcher.group(1) :  null;
    }

}