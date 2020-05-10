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
package org.apache.isis.viewer.common.model.action.form;

import org.apache.isis.core.metamodel.consent.Consent;
import org.apache.isis.core.metamodel.consent.InteractionInitiatedBy;
import org.apache.isis.core.metamodel.interactions.InteractionContext.Head;
import org.apache.isis.core.metamodel.specloader.specimpl.PendingParameterModel;
import org.apache.isis.viewer.common.model.feature.ParameterUiModel;

import lombok.Value;
import lombok.val;

@Value(staticConstructor = "of")
public class FormPendingParamUiModel {
    
    final ParameterUiModel paramModel;
    final Consent visibilityConsent;
    final Consent usabilityConsent;
    
    public static FormPendingParamUiModel of(
            Head head,
            ParameterUiModel paramUiModel, 
            PendingParameterModel pendingArgs) {

        val objectActionParamter = paramUiModel.getMetaModel();
        val pendingArgValues = pendingArgs.getParamValues();

        paramUiModel.setPendingParameterModel(pendingArgs);
        
        // visibility
        val visibilityConsent = objectActionParamter
                .isVisible(head, pendingArgValues, InteractionInitiatedBy.USER);

        // usability
        val usabilityConsent = objectActionParamter
                .isUsable(head, pendingArgValues, InteractionInitiatedBy.USER);
        
        return of(paramUiModel, visibilityConsent, usabilityConsent);
    }
}
