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
package demoapp.dom.types.primitive.chars.holder;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.PromptStyle;
import org.apache.isis.applib.annotation.SemanticsOf;

import lombok.RequiredArgsConstructor;


@Action(
        semantics = SemanticsOf.IDEMPOTENT,
        associateWith = "readOnlyProperty2",
        associateWithSequence = "1"
)
@ActionLayout(promptStyle = PromptStyle.INLINE_AS_IF_EDIT)
@RequiredArgsConstructor
public class PrimitiveCharHolder_updateReadOnlyProperty2PromptInlineAsIfEdit {

    private final demoapp.dom.types.primitive.chars.holder.PrimitiveCharHolder primitiveCharHolder;

    public demoapp.dom.types.primitive.chars.holder.PrimitiveCharHolder act(char newValue) {
        primitiveCharHolder.setReadOnlyProperty2(newValue);
        return primitiveCharHolder;
    }
    public char default0Act() {
        return primitiveCharHolder.getReadOnlyProperty2();
    }


}
