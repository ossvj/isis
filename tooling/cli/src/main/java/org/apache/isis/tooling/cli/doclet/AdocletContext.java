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
package org.apache.isis.tooling.cli.doclet;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.isis.commons.internal.collections._Maps;
import org.apache.isis.commons.internal.exceptions._Exceptions;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.val;

@Value @Builder
public class AdocletContext {

    private final @NonNull String xrefPageIdFormat;
    
    @Builder.Default
    private final @NonNull String methodFormat = "`%s %s(%s)`"; // return-type | name | param-list
    
    @Builder.Default
    private final @NonNull String staticMethodFormat = "`%s _%s_(%s)`"; // return-type | name | param-list
    
    @Builder.Default
    private final @NonNull String methodDescriptionFormat = "\n<.> %s %s\n"; // method | description
    
    @Builder.Default
    private final boolean includeJavaSource = true;
    
    private final Map<String, Adoclet> adocletIndex = _Maps.newTreeMap();

    public AdocletContext add(final @NonNull Adoclet adoclet) {
        val previousKey = adocletIndex.put(adoclet.getName(), adoclet);
        if(previousKey!=null) {
            throw _Exceptions.unrecoverableFormatted(
                    "doclet index entries must be unique (index key collision on %s)", 
                    previousKey);
        }
        return this;
    }
    
    public Stream<Adoclet> add(final @NonNull File sourceFile) {
        return Adoclet.parse(sourceFile)
        .peek(this::add)
        // ensure the stream is consumed here, 
        // current implementation does not expect more than 1 result per source file
        .collect(Collectors.toCollection(()->new ArrayList<>(1))) 
        .stream();
    }
    
    public Stream<Adoclet> streamAdoclets() {
        return adocletIndex.values().stream();
    }

    public Optional<Adoclet> getAdoclet(String key) {
        return Optional.ofNullable(adocletIndex.get(key));
    }
    
    public static AdocletContextBuilder compactFormat() {
        return AdocletContext.builder()
                .methodFormat("`*%2$s*(%3$s)` : `%1$s`") // return-type | name | param-list)
                .staticMethodFormat("`*_%2$s_*(%3$s)` : `%1$s`") // return-type | name | param-list)
                .methodDescriptionFormat("\n* %s\n%s\n") // method | description
                .includeJavaSource(false)
                ;        
    }
    
}
