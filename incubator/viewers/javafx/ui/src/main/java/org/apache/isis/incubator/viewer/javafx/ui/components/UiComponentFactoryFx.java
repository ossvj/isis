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
package org.apache.isis.incubator.viewer.javafx.ui.components;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import org.apache.isis.core.commons.handler.ChainOfResponsibility;
import org.apache.isis.core.commons.internal.environment.IsisSystemEnvironment;
import org.apache.isis.core.commons.internal.exceptions._Exceptions;
import org.apache.isis.core.metamodel.interactions.managed.ManagedAction;
import org.apache.isis.incubator.viewer.javafx.ui.components.form.FormField;
import org.apache.isis.incubator.viewer.javafx.ui.decorator.prototyping.PrototypingButtonDecorator;
import org.apache.isis.incubator.viewer.javafx.ui.decorator.prototyping.PrototypingFormFieldDecorator;
import org.apache.isis.viewer.common.model.binding.UiComponentFactory;
import org.apache.isis.viewer.common.model.decorator.prototyping.PrototypingUiModel;

import javafx.scene.Node;
import javafx.scene.control.Button;
import lombok.Getter;
import lombok.val;

@Service
public class UiComponentFactoryFx implements UiComponentFactory<FormField> {

    private final boolean isPrototyping;
    private final PrototypingButtonDecorator prototypingButtonDecorator;
    private final PrototypingFormFieldDecorator prototypingFormFieldDecorator;
    private final ChainOfResponsibility<Request, FormField> chainOfHandlers;
    
    /** handlers in order of precedence (debug info)*/
    @Getter 
    private final List<Class<?>> registeredHandlers; 
    
    @Inject
    private UiComponentFactoryFx(
            IsisSystemEnvironment isisSystemEnvironment,
            PrototypingButtonDecorator prototypingButtonDecorator,
            PrototypingFormFieldDecorator prototypingFormFieldDecorator,
            List<UiComponentHandlerFx> handlers) {
        
        this.isPrototyping = isisSystemEnvironment.isPrototyping();
        this.prototypingButtonDecorator = prototypingButtonDecorator;
        this.prototypingFormFieldDecorator = prototypingFormFieldDecorator;
        this.chainOfHandlers = ChainOfResponsibility.of(handlers);
        this.registeredHandlers = handlers.stream()
                .map(Handler::getClass)
                .collect(Collectors.toList());
    }
    
    @Override
    public FormField componentFor(Request request) {
        
        val formField = chainOfHandlers
                .handle(request)
                .orElseThrow(()->_Exceptions.unrecoverableFormatted(
                        "Component Mapper failed to handle request %s", request));
        
        return isPrototyping
                ? prototypingFormFieldDecorator.decorate(formField, PrototypingUiModel.of(request.getObjectFeature()))
                : formField;
    }
    
    public Node buttonFor(
            final ManagedAction managedAction, 
            final Consumer<ManagedAction> actionEventHandler) {
        val uiButton = new Button(managedAction.getName());
        uiButton.setOnAction(event->actionEventHandler.accept(managedAction));
        
        return isPrototyping
                ? prototypingButtonDecorator.decorate(uiButton, PrototypingUiModel.of(managedAction))
                : uiButton;
    }
    
    
}
