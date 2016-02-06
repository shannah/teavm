/*
 *  Copyright 2016 "Alexey Andreev"
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.teavm.classlib.java.lang;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashSet;
import java.util.ServiceLoader;
import java.util.Set;
import org.apache.commons.io.IOUtils;
import org.teavm.classlib.ResourceSupplier;
import org.teavm.codegen.SourceWriter;
import org.teavm.javascript.Renderer;
import org.teavm.javascript.spi.Injector;
import org.teavm.javascript.spi.InjectorContext;
import org.teavm.model.MethodReference;

/**
 *
 * @author Alexey Andreev
 */
public class ClassLoaderNativeGenerator implements Injector {
    @Override
    public void generate(InjectorContext context, MethodReference methodRef) throws IOException {
        switch (methodRef.getName()) {
            case "supplyResources":
                generateSupplyResources(context);
                break;
        }
    }

    private void generateSupplyResources(InjectorContext context) throws IOException {
        SourceWriter writer = context.getWriter();
        writer.append("{").indent();

        ClassLoader classLoader = context.getClassLoader();
        Set<String> resourceSet = new HashSet<>();
        for (ResourceSupplier supplier : ServiceLoader.load(ResourceSupplier.class, classLoader)) {
            String[] resources = supplier.supplyResources(classLoader, context.getClassSource());
            if (resources != null) {
                resourceSet.addAll(Arrays.asList(resources));
            }
        }

        boolean first = true;
        for (String resource : resourceSet) {
            try (InputStream input = classLoader.getResourceAsStream(resource)) {
                if (input == null) {
                    continue;
                }
                if (!first) {
                    writer.append(',');
                }
                first = false;
                writer.newLine();
                String data = Base64.getEncoder().encodeToString(IOUtils.toByteArray(input));
                writer.append("\"").append(Renderer.escapeString(resource)).append("\"");
                writer.ws().append(':').ws();
                writer.append("\"").append(data).append("\"");
            }
        }

        if (!first) {
            writer.newLine();
        }
        writer.outdent().append('}');
    }
}
